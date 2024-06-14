lexer grammar FOkLexer;

// Keywords (Quantifiers and Logical Connectives)
FORALL : 'forall' | '∀';
EXISTS : 'exists' | '∃';
AND    : 'and' | '∧';
OR     : 'or' | '∨';
NOT    : 'not' | '¬';
IMPLIES: 'implies' | '→';
IFF    : 'iff' | '↔';


// Punctuation
LPAREN : '(';
RPAREN : ')';
COMMA  : ',';
DOT    : '.';


// Variables and Constants
VARIABLE : [a-z][a-zA-Z0-9_]*;
CONSTANT : [A-Z][a-zA-Z0-9_]*;

// Predicates and Functions
PREDICATE : [A-Z][a-zA-Z0-9_]*;
FUNCTION  : [a-z][a-zA-Z0-9_]*;

// Whitespace and Comments
WS : [ \t\r\n]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;