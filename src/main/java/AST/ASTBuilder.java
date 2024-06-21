package AST;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import java.util.HashMap;

@Getter
public class ASTBuilder {
    private AST ast;

    // size -> AST
    private HashMap<Integer, Set<AST>> asts = new HashMap<>();

    public ASTBuilder() {
        this.ast = new AST();
    }

    public ASTBuilder(AST ast) {
        this.ast = ast;
        this.initTrivialASTs();
    }


    private void initTrivialASTs() {
        // Initialize base ASTs for size 1 (assuming basic elements of the language can
        // form an AST of size 1)
        Set<AST> baseASTs = new HashSet<>();
        baseASTs.add(new AST(AST.FOk.FORMULA, "$T"));
        baseASTs.add(new AST(AST.FOk.VALUE, "$F"));
        // Potentially more base cases
        this.asts.put(1, baseASTs);
    }



    /**
     * return all the possible ASTs that can be grown from the given AST by one step
     * (a bottom-up approach)
     * 
     * @param givenAst
     * @return
     */
    public Set<AST> grow(AST givenAst) {
        if (givenAst == null) {
            return new HashSet<>();
        }
        AST.Node node = givenAst.getRoot();
        assert node.getType() == AST.FOk.FORMULA;
        Set<AST> result = new HashSet<>();
        for (AST.FOk type : AST.FOk.values()) {
            result.addAll(growbyType(givenAst, type));
        }
        return result;
    }

    /**
     * return all the possible ASTs that can be grown from the given AST by one step
     * by type. This is SO DAMN elegant!
     * formula
     * : NOT formula
     * | formula op=(IFF | IMPLIES | AND | OR) formula
     * | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * | RELATION (LPAREN term (COMMA term)* RPAREN)?
     * | term EQUALS term
     * | value
     * | LPAREN formula RPAREN
     * ;
     * 
     * @param givenAst the given AST
     * @param type     the type that the AST should be grown by
     * @return the set of ASTs that can be grown from the given AST by one step by
     *         type
     */
    private Set<AST> growbyType(AST givenAst, AST.FOk type) {
        Set<AST> result = new HashSet<>();
        switch (type) {
            case NOT:
                break;
            case FORMULA:
                // add 
                break;
            case QOP:
                // add forall and exists
                break;
            case RELATION:
                break;
            case TERM:
                break;
            case VALUE:
                break;
            case LPAREN:
                break;
            default:
                break;
        }
        return result;
    }

}