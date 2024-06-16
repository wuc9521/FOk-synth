package interfaces;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Structure<DType> {
    @Setter
    public boolean pos = true; // whether the structure is tagged as positive
    public final Set<Element> domain = new HashSet<>(); // the domain of the structure
    public final Map<String, Relation> relations = new HashMap<>(); // the relations in the structure
    public final Map<String, DType> constants = new HashMap<>(); // the constants in the structure

    @Setter @Getter
    @AllArgsConstructor
    public class Element { // define the type of elements in the structure
        public DType value;
    }

    public abstract class Relation {
        public int arity; // the arity of the relation
        public String name; // the name of the relation
        public abstract boolean holds(List<Element> args); // check if the relation holds
    }
} 