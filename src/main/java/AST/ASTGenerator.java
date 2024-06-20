package AST;

import java.util.HashSet;
import java.util.Set;

public class ASTGenerator {
    public Set<AST> getAllPossibleASTs(int size) {
        Set<AST> allASTs = new HashSet<>();
        generateASTsRecursively(new AST(), size, allASTs);
        return allASTs;
    }

    private void generateASTsRecursively(AST currentAst, int size, Set<AST> allASTs) {
        // if (currentAst.getSize() == size) {
        //     allASTs.add(currentAst);
        //     return;
        // }

        // // 假设我们有一个方法来获取所有可能的子节点类型
        // for (AST.FOk type : AST.FOk.values()) {
        //     AST newAst = new AST(currentAst);  // 创建一个基于当前AST的新副本
        //     newAst.addNode(type, "Value", null); // 假设添加到根节点
        //     generateASTsRecursively(newAst, size, allASTs);
        // }
    }


    /**
     * Grow the AST by adding a unit of size to the given AST.
     * @param ast
     * @return the set of ASTs that are one unit larger than the given AST.
     */
    public Set<AST> grow(AST ast) {
        return null;
    }
}
