grammar Expr;

s: e;
e: e op=('+'|'-') INT | INT;

INT: [0-9]+;
ADD: '+';
SUB: '-';
WS: [ \t\r\n]+ -> skip;