parser grammar FOkParser;

options { tokenVocab=FOkLexer; } // Use the tokens from the lexer

// Entry point
formula : implication EOF ;

// Logical expressions
implication
    : disjunction (IMPLIES disjunction)* ;
    
disjunction
    : conjunction (OR conjunction)* ;
    
conjunction
    : negation (AND negation)* ;
    
negation
    : NOT negation
    | atom ;
    
atom
    : LPAREN formula RPAREN
    | predicate
    | quantifier ;
    
predicate
    : PREDICATE LPAREN termList RPAREN ;
    
termList
    : term (COMMA term)* ;
    
term
    : VARIABLE
    | CONSTANT
    | FUNCTION LPAREN termList RPAREN ;

quantifier
    : FORALL VARIABLE DOT formula
    | EXISTS VARIABLE DOT formula ;
