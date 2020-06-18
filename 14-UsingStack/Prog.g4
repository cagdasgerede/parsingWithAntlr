grammar Prog;

prog: s+ ;

s: e NEWLINE                #Print
    | ID '=' e NEWLINE      #Assign
    | NEWLINE               #Blank
    ;

e: <assoc=right> e '^' e    #Expo
    | e op=('*'|'/') e      #MulDiv
    | e op=('+'|'-') e      #AddSub
    | INT                   #Int
    | ID                    #Id
    | '(' e ')'             #Paren
    ;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
ID: [a-zA-Z]+;
NEWLINE: '\r'? '\n';
WS: [ \t]+ -> skip;