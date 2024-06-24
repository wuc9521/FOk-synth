package AST;

import java.util.HashSet;
import java.util.LinkedList;
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

    private List<String> possibleTermLiterals = new LinkedList<>();

    public ASTBuilder() {
    }

    public ASTBuilder(Structure<T> structure, List<String> vars) {
        this.structure = structure;
        this.vars = new HashMap<>();
        for (String var : vars) {
            this.vars.put(var, false);
        }
        this.possibleTermLiterals = this.getAllPossibleTermLiterals();
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
            assert givenAST.getRoot().getType() == AST.FOk.FORMULA;
            for (AST.FOk type : AST.FOk.values()) {
                result.addAll(growbyType(astSet, givenAST, type));
            }
        }
        return result;
    }

    /**
     * return all the possible ASTs that can be grown from the given AST by one step
     * @param astSet the set of ASTs that can be grown from
     * @return all the possible ASTs that can be grown from the given AST by one step
     */
    public Set<AST> getAllRelationLiterals(){
        Set<AST> result = new HashSet<>();
        result.addAll(growbyType(new HashSet<>(), new AST(AST.FOk.FORMULA, "$T"), AST.FOk.RELATION));
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
        if (type != AST.FOk.FORMULA && type != AST.FOk.NOT && type != AST.FOk.QOP && type != AST.FOk.RELATION
                && type != AST.FOk.TERM && type != AST.FOk.VALUE && type != AST.FOk.LPAREN) {
            return new HashSet<>();
        }
        Set<AST> result = new HashSet<>();
        assert givenAST.getRoot().getType() == AST.FOk.FORMULA; // 给定一个formula
        AST resultAST = null;
        switch (type) {
            case NOT: // formula -> not (formula)
                // 这种情况是绝对的
                // if current formula is trivial, then break;
                if (givenAST.equals(new AST(AST.FOk.FORMULA, "$T"))
                        || givenAST.equals(new AST(AST.FOk.FORMULA, "$F"))) {
                    return result;
                }
                AST notAst = new AST(AST.FOk.NOT, "~");
                resultAST = new AST(AST.FOk.FORMULA, notAst.toString() + "(" + givenAST.toString() + ")");
                resultAST.setSize(givenAST.getSize() + 1);
                resultAST.getRoot().addChild(notAst.getRoot());
                resultAST.getRoot().addChild(givenAST.getRoot());
                result.add(resultAST);
                return result;
            case FORMULA:
                if (givenAST.equals(new AST(AST.FOk.FORMULA, "$T"))
                        || givenAST.equals(new AST(AST.FOk.FORMULA, "$F"))) {
                    return result;
                }
                String[] ops = {
                        // "<->", // TODO: 为了方便我们先不实现这两个
                        // "->",
                        "&",
                        "|"
                };
                AST lAST = null;
                AST rAST = null;
                for (String op : ops) {
                    AST.Node node = new AST.Node(AST.FOk.OP, op);
                    for (AST otherAST : astSet) {
                        if (otherAST.equals(givenAST) || otherAST.equals(new AST(AST.FOk.FORMULA, "$T"))
                                || otherAST.equals(new AST(AST.FOk.FORMULA, "$F"))) {
                            continue;
                        }
                        lAST = new AST(AST.FOk.FORMULA,
                                "(" + otherAST.toString() + ")" + node.toString() + "(" + givenAST.toString() + ")");
                        lAST.setSize(otherAST.getSize() + givenAST.getSize() + 1);
                        lAST.getRoot().addChild(otherAST.getRoot());
                        lAST.getRoot().addChild(node);
                        lAST.getRoot().addChild(givenAST.getRoot());
                        rAST = new AST(AST.FOk.FORMULA,
                                "(" + givenAST.toString() + ")" + node.toString() + "(" + otherAST.toString() + ")");
                        rAST.setSize(givenAST.getSize() + otherAST.getSize() + 1);
                        rAST.getRoot().addChild(givenAST.getRoot());
                        rAST.getRoot().addChild(node);
                        rAST.getRoot().addChild(otherAST.getRoot());
                        result.add(lAST);
                        result.add(rAST);
                    }
                }
                break;
            case QOP:
                // add forall and exists
                // consider var set now.
                // if (givenAST.equals(new AST(AST.FOk.FORMULA, "$T"))
                // || givenAST.equals(new AST(AST.FOk.FORMULA, "$F"))) {
                // return result;
                // }
                Set<String> vars = this.vars.keySet();
                for (String var : vars) {
                    if (this.vars.get(var)) {
                        continue; // if the var has been used, skip
                    }
                    AST.Node node = new AST.Node(AST.FOk.QOP, "forall " + var + ".");
                    resultAST = new AST(AST.FOk.FORMULA, node.toString() + "(" + givenAST.toString() + ")");
                    resultAST.setSize(givenAST.getSize() + 7);
                    resultAST.getRoot().addChild(node);
                    resultAST.getRoot().addChild(givenAST.getRoot());
                    result.add(resultAST);
                    node = new AST.Node(AST.FOk.QOP, "exists " + var + ".");
                    resultAST = new AST(AST.FOk.FORMULA, node.toString() + "(" + givenAST.toString() + ")");
                    resultAST.setSize(givenAST.getSize() + 7);
                    resultAST.getRoot().addChild(node);
                    resultAST.getRoot().addChild(givenAST.getRoot());
                    result.add(resultAST);
                }
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
                        resultAST = new AST(AST.FOk.FORMULA, node.toString());
                        resultAST.setSize(1 + arity);
                        resultAST.getRoot().addChild(node);
                        result.add(resultAST);
                    }
                }
                return result;
            case TERM:
                // add term
                Set<AST.Node> possibleTerms = new HashSet<>();
                for (String term : this.possibleTermLiterals) {
                    AST.Node node = new AST.Node(AST.FOk.TERM, term);
                    possibleTerms.add(node);
                }
                for (AST.Node t1 : possibleTerms) {
                    for (AST.Node t2 : possibleTerms) {
                        String equalsLiteral = "=";
                        AST.Node node = new AST.Node(AST.FOk.FORMULA, t1.toString() + equalsLiteral + t2.toString());
                        resultAST = new AST(AST.FOk.FORMULA, node.toString());
                        resultAST.setSize(3);
                        resultAST.getRoot().addChild(node);
                        result.add(resultAST);
                    }
                }
                return result;
            case VALUE:
            case LPAREN:
                // if (givenAST.equals(new AST(AST.FOk.FORMULA, "$T"))
                // || givenAST.equals(new AST(AST.FOk.FORMULA, "$F"))) {
                // return result;
                // }
                // AST lparenAst = new AST(AST.FOk.FORMULA, "(");
                // AST rparenAst = new AST(AST.FOk.FORMULA, ")");
                // newAst = new AST(AST.FOk.FORMULA, lparenAst.toString() + givenAST.toString()
                // + rparenAst.toString());
                // newAst.setSize(givenAST.getSize() + 2);
                // newAst.getRoot().addChild(lparenAst.getRoot());
                // newAst.getRoot().addChild(givenAST.getRoot());
                // newAst.getRoot().addChild(rparenAst.getRoot());
                // result.add(newAst);
                // return result;
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
        return TupleUtils.generateKTuplesWithoutNull(arity, possibleTerms);
    }
}