package AST;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import utils.*;
import FO.Structure;

@Getter
@Setter
public class ASTBuilder<T> {
    private Structure<T> structure;

    // name, used
    private HashMap<String, Boolean> vars;

    private Set<AST> trivialASTs = new HashSet<>() {
        {
            add(new AST(AST.FOk.FORMULA, "$T"));
            add(new AST(AST.FOk.FORMULA, "$F"));
        }
    };

    public ASTBuilder() {
    }

    public ASTBuilder(Structure<T> structure, List<String> vars) {
        this.structure = structure;
        this.vars = new HashMap<>();
        for (String var : vars) {
            this.vars.put(var, false);
        }
    }

    public List<String> getAllPossibleTermLiterals() {
        List<String> possibleTerms = new ArrayList<>();
        possibleTerms.addAll(this.structure.constants.keySet());
        possibleTerms.addAll(this.vars.keySet()); // 关系中出现的可以是常量或者是变量.
        return possibleTerms;
    }

    /**
     * return all the possible ASTs that can be grown from the given AST by one step
     * (a bottom-up approach)
     * 
     * @param givenAST
     * @return
     */
    public Set<AST> grow(Set<AST> astSet) {
        if (astSet.isEmpty()) {
            return this.trivialASTs; // TODO: 要处理一下非叶节点.
        }
        HashSet<AST> result = new HashSet<>();
        for (AST givenAST : astSet) {
            AST.Node node = givenAST.getRoot();
            assert node.getType() == AST.FOk.FORMULA;
            for (AST.FOk type : AST.FOk.values()) {
                result.addAll(growbyType(astSet, givenAST, type));
            }
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
     * @param astSet   the set of ASTs that can be grown from
     * @param givenAST the given AST
     * @param type     the type that the AST should be grown by
     * @return the set of ASTs that can be grown from the given AST by one step by
     *         type
     */
    private Set<AST> growbyType(Set<AST> astSet, AST givenAST, AST.FOk type) {
        assert givenAST.getRoot().getType() == AST.FOk.FORMULA; // 给定一个formula
        Set<AST> result = new HashSet<>();
        List<String> possibleTermLiterals = this.getAllPossibleTermLiterals();
        AST newAst = null;
        switch (type) {
            case NOT: // formula -> not (formula)
                // 这种情况是绝对的
                AST notAst = new AST(AST.FOk.FORMULA, "~");
                newAst = new AST(AST.FOk.FORMULA, notAst.toString() + "(" + givenAST.toString() + ")");
                newAst.setSize(givenAST.getSize() + 3);
                newAst.getRoot().addChild(notAst.getRoot());
                newAst.getRoot().addChild(givenAST.getRoot());
                result.add(newAst);
                return result;
            case FORMULA:
                break;
            case QOP:
                // add forall and exists
                // consider var set now.
                Set<String> vars = this.vars.keySet();   
                for (String var : vars) {
                    if (this.vars.get(var)) {
                        continue; // if the var has been used, skip
                    }
                    AST.Node node = new AST.Node(AST.FOk.QOP, "forall " + var + ".");
                    newAst = new AST(AST.FOk.FORMULA, node.toString() + "(" + givenAST.toString() + ")");
                    newAst.setSize(givenAST.getSize() + 7);
                    newAst.getRoot().addChild(node);
                    newAst.getRoot().addChild(givenAST.getRoot());
                    result.add(newAst);
                    node = new AST.Node(AST.FOk.QOP, "exists " + var + ".");
                    newAst = new AST(AST.FOk.FORMULA, node.toString() + "(" + givenAST.toString() + ")");
                    newAst.setSize(givenAST.getSize() + 7);
                    newAst.getRoot().addChild(node);
                    newAst.getRoot().addChild(givenAST.getRoot());
                    result.add(newAst);
                }
                return result;
            case LPAREN:
                AST lparenAst = new AST(AST.FOk.FORMULA, "(");
                AST rparenAst = new AST(AST.FOk.FORMULA, ")");
                newAst = new AST(AST.FOk.FORMULA, lparenAst.toString() + givenAST.toString() + rparenAst.toString());
                newAst.setSize(givenAST.getSize() + 2);
                newAst.getRoot().addChild(lparenAst.getRoot());
                newAst.getRoot().addChild(givenAST.getRoot());
                newAst.getRoot().addChild(rparenAst.getRoot());
                result.add(newAst);
                return result;
            case RELATION:
                // add relation
                Set<String> relationLiterals = this.structure.relations.keySet();
                for (String relation : relationLiterals) {
                    Structure<T>.Relation rel = this.structure.relations.get(relation);
                    int arity = rel.arity;
                    List<List<String>> tuples = this.getTuplesFromPossibleTerms(arity);
                    for (List<String> tuple : tuples) {
                        AST.Node node = new AST.Node(AST.FOk.RELATION, relation + "(" + String.join(", ", tuple) + ")");
                        newAst = new AST(AST.FOk.FORMULA, node.toString());
                        newAst.setSize(1 + arity);
                        newAst.getRoot().addChild(node);
                        result.add(newAst);
                    }
                }
                return result;
            case TERM:
                // add term
                Set<AST.Node> possibleTerms = new HashSet<>();
                for (String term : possibleTermLiterals) {
                    AST.Node node = new AST.Node(AST.FOk.TERM, term);
                    possibleTerms.add(node);
                }
                for (AST.Node t1 : possibleTerms) {
                    for (AST.Node t2 : possibleTerms) {
                        AST.Node node = new AST.Node(AST.FOk.EQUALS, t1.toString() + " = " + t2.toString());
                        newAst = new AST(AST.FOk.FORMULA, node.toString());
                        newAst.setSize(3);
                        newAst.getRoot().addChild(node);
                        result.add(newAst);
                    }
                }
                return result;
            case VALUE:
            default:
                break;
        }
        return result;
    }

    /**
     * Get all possible terms that can be used in the AST
     * 
     * @param arity the arity of the relation
     * @return all possible terms that can be used in the AST
     */
    private List<List<String>> getTuplesFromPossibleTerms(int arity) {
        List<String> possibleTerms = this.getAllPossibleTermLiterals();
        return TupleGenerator.generateKTuplesWithoutNull(arity, possibleTerms);
    }
}