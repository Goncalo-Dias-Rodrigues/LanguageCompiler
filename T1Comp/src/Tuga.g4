grammar Tuga;

prog   : stat+ EOF ;

stat   : 'escreve' expr ';' ;

expr   : '(' expr ')'                                       # Parens
       | '-' expr                                           # Uminus
       | 'nao' expr                                         # Not
       | expr op = ('*'|'/'|'%') expr                       # MulDivMod
       | expr op = ('+'|'-') expr                           # AddSub
       | expr op = ( '<' | '>' | '<=' | '>=' ) expr         # Compare
       | expr op = ('igual' | 'diferente') expr             # EqNotEq
       | expr 'e' expr                                      # And
       | expr 'ou' expr                                     # Or
       | INT                                                # Int
       | BOOL                                               # Bool
       | DOUBLE                                             # Real
       | STR                                                # String
       ;

INT      : DIGIT+ ; //integer
BOOL     : 'verdadeiro' | 'falso' ;  //boolean
DOUBLE   : DIGIT+'.'DIGIT+ ; //double/real
STR      : '"' CHAR*? '"' ; //string



WS       : [ \t\r\n]+ -> skip ; //white spaces
SL_COMMENT : '//' .*? (EOF|'\n') -> skip ; // single-line comment
ML_COMMENT : '/*' .*? '*/' -> skip ; // multi-line comment

fragment
DIGIT    : [0-9] ;   //digit

fragment
CHAR     : ~["] ;   //character