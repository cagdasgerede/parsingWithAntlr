grammar Expr;

s: e;
e: e op=('*'|'/') e |
   e op=('+'|'-') e |
   INT;

INT: [0-9]+;
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
WS: [ \t\r\n]+ -> skip;