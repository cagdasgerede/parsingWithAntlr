grammar Expr;

s: e;
e: e op=('*'|'/') INT |
   e op=('+'|'-') INT |
   INT;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
WS: [ \t\r\n]+ -> skip;