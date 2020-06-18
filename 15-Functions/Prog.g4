grammar Prog;

prog: s+ ;

s: e NEWLINE                #Print
    | ID '=' e NEWLINE      #Assign
    | NEWLINE               #Blank
    | 'func' ID
        '{' prog '}'        #FuncDec
    ;

e: <assoc=right> e '^' e    #Expo
    | e op=('*'|'/') e      #MulDiv
    | e op=('+'|'-') e      #AddSub
    | INT                   #Int
    | ID                    #Id
    | '(' e ')'             #Paren
    | ID '(' ')'            #FuncCall
    ;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
ID: [a-zA-Z]+;
NEWLINE: '\r'? '\n';
WS: [ \t]+ -> skip;