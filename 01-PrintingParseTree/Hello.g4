// cd C:\Users\vatan\Projects\antlr4-parser\Hello1
// java org.antlr.v4.gui.TestRig Hello r -tokens input.txt
grammar Hello;
r: 'hello' ID;

ID: [a-z]+;
WS: [ \t\r\n] -> skip;
