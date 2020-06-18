grammar Cymbol;

file    :   (functionDecl | varDecl)+;

varDecl :   type ID ('=' expr)? ';'
        ;

type    :   'float' | 'int' | 'void';

functionDecl
    :       type ID '('formalParameters')' block
    ;

formalParameters
    :   formalParameter (',' formalParameter)*
    |
    ;

formalParameter
    :   type ID
    ;


/** one block contains many statement */
block:  '{' stat* '}'    
     ;

/** stat : statement */
stat    :   block
        |   varDecl
        |   'if' expr 'then' stat ('else' stat)?
        |   'return' expr? ';'
        |   expr '=' expr  ';'
        |   expr ';'
        ;

expr    :   ID '(' exprList? ')'    #Call
        |   expr '[' expr ']'       #Index
        |   '-' expr                #Negate
        |   '!' expr                #Not
        |   expr '*' expr           #Mult
        |   expr ('+' | '-') expr   #AddSub
        |   expr '==' expr          #Equal
        |   ID                      #Var
        |   INT                     #Int
        |   '(' expr ')'            #Parens
        ;

exprList:   expr (',' expr)*;

ID      : LETTER (LETTER | [0-9])*;
fragment
LETTER  :   [a-zA-Z_];
INT     :   [0-9]+;
WS      :   [ \t\n\r]+ -> skip ;

SL_COMMENT
    :   '//' .*? '\n' -> skip
    ;