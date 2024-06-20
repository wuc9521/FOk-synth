package utils;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import antlr.FOkLexer;
import antlr.FOkParser;

public class ParserUtils {

    /**
     * Helper method to parse a formula string into a ParseTree
     * 
     * @param formulaString formula in string form
     * @return the ParseTree of the formula
     */
    public static ParseTree parse(String formulaString) {
        FOkParser parser = new FOkParser(new CommonTokenStream(new FOkLexer(CharStreams.fromString(formulaString))));
        return parser.formula();
    }
}
