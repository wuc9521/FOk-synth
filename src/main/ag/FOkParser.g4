parser grammar FOkParser;

options {
    tokenVocab=FOkLexer;
}

prog
    : COMMENT? sentence+ ENDLINE? EOF   
    ;

sentence
    : formula
    ;
    
formula 
    : NOT formula                      
    | formula (IFF | IMPLIES | AND | OR) formula 
    | (FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
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
