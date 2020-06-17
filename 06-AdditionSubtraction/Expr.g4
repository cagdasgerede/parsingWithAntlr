grammar Expr;

s: e;
e: INT op=('+'|'-') e | INT;

INT: [0-9]+;
ADD: '+';
SUB: '-';
WS: [ \t\r\n]+ -> skip;