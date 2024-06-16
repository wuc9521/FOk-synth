package interfaces;
import java.util.*;

import org.antlr.v4.parse.ANTLRParser.prequelConstruct_return;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Structure<DType> {
    @Setter
    public boolean pos = true; // whether the structure is tagged as positive
    public final Set<Element> domain = new HashSet<>(); // the domain of the structure
    public final Set<Relation> relations = new HashSet<>(); // the relations in the structure
    public final Set<Const> constants = new HashSet<>(); // the constants in the structure

    @Setter @Getter
    @AllArgsConstructor
    public class Element { // define the type of elements in the structure
        public DType value;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public abstract class Const{
        private DType value;
        public String name;
    }

    public abstract class Relation {
        public int arity; // the arity of the relation
        public String name; // the name of the relation
        public abstract boolean holds(List<Element> args); // check if the relation holds
    }
} 