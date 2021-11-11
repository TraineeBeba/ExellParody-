/**
* Define a grammar called Grammar
*/
grammar Grammar;
/*
* Parser Rules
*/

expression :
LPAREN expression RPAREN #ParenthesizedExpr
| expression EXPONENT expression #ExponentialExpr
| expression operatorToken=(MULTIPLY | DIVIDE) expression #MultiplicativeExpr
| expression operatorToken=(MOD | DIV) expression #DividiveWithRemainderExpr
| expression operatorToken=(ADD | SUBTRACT) expression #AdditiveExpr
| operatorToken=(MAX | MIN) LPAREN expression COMA expression RPAREN #ComparativeExpr
| NUMBER #NumberExpr
;
/*
* Lexer Rules
*/
NUMBER : INT ('.' INT)?;
INT : ('0'..'9')+;
EXPONENT : '^';
MULTIPLY : '*';
DIVIDE : '/';
SUBTRACT : '-';
ADD : '+';
MOD : 'mod';
DIV : 'div';
MAX : 'max';
MIN : 'min';
LPAREN : '(';
RPAREN : ')';
COMA : ',';
WS : [ \t\r\n] -> channel(HIDDEN);