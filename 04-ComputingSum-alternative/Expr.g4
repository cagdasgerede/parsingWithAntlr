grammar Expr;

s: e+;
e: INT '+' e | INT;

INT: [0-9]+;
WS: [ \t\r\n]+ -> skip;