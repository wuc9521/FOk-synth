parser grammar FOkParser;

options {
    tokenVocab=FOkLexer;
}

prog: COMMENT? sentence+ ENDLINE? EOF;

sentence: formula; // technically a sentence should be a formula without any free variables

formula 
    : NOT formula                      
    | formula op=(IFF | IMPLIES | AND | OR) formula 
    | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
    | RELATION (LPAREN term (COMMA term)* RPAREN)?
    | term EQUALS term        
    | value                    
	| LPAREN formula RPAREN    
    ;

term
    : FUNC LPAREN (term (COMMA term)*)? RPAREN
    | CONST                      
    | VARIABLE     
    ;
    
value
    : TRUE | FALSE
    ;
