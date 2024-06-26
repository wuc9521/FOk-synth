package FO;

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
    public final Map<String, Function> functions = new HashMap<>(); // the functions in the structure

    @Getter
    @AllArgsConstructor
    public class Element { // define the type of elements in the structure
        public DType value;
        private boolean undefined = false;

        public Element(DType value) {
            this.value = value;
            this.undefined = false;
        }

        public Element() {
            this.undefined = true;
        }

        /**
         * We specially define two elements to be equal if they are both undefined.
         * check if the element is undefined
         * 
         * @param element the element to be checked
         * @return whether the element is undefined
         */
        public boolean equals(Element element) {
            if (this.undefined && element.undefined) {
                return true;
            } else if (this.undefined || element.undefined) {
                return false;
            }
            return this.value.equals(element.value);
        }
    }

    @Getter
    public abstract class Relation {
        public int arity = 0; // the arity of the relation
        private List<List<Element>> tuples = new ArrayList<>(); // the tuples in the relation

        public abstract boolean holds(List<Element> args); // check if the relation holds

        public void addTuple(List<Element> tuple) {
            if (this.arity == 0) {
                this.arity = tuple.size();
            } else {
                assert tuple.size() == this.arity;
            }
            this.tuples.add(tuple);
        }
    }

    @Getter
    public class Function {
    }
}