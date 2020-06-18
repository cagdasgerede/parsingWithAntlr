grammar Expr;

s: e;
e: <assoc=right> e '^' e #Expo
    | e op=('*'|'/') e #MulDiv
    | e op=('+'|'-') e #AddSub
    | INT #Int;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
WS: [ \t\r\n]+ -> skip;