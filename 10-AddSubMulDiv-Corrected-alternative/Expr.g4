grammar Expr;

s: e;
e: e op=('*'|'/') e #MulDiv
    | e op=('+'|'-') e #AddSub
    | INT #Int;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
WS: [ \t\r\n]+ -> skip;