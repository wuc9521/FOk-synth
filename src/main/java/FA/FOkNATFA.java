package FA;
import java.util.List;

import FO.Structure;


public class FOkNATFA<T> extends FOkATFA<T>{

    /**
     * the constructor of the FOkNTFA class
     * 
     * @param vars      a list of variables that can be used to construct the states
     * @param structure the structure for the automaton to run on
     *                  observe that both the type of visitor and type of TFA are
     *                  dependent on the type of the structure
     */
    public FOkNATFA(List<String> vars, Structure<T> structure) {
        super(vars, structure);
    }
}
