package FO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Assignment{
    private Map<String, Structure<?>.Element> kvMap;
    private int size;
    public Assignment(Map<String, Structure<?>.Element> kvMap) {
        this.kvMap = kvMap;
        this.size = kvMap.size();
    }
    public boolean equals(Assignment assignment) {
        if (this.size != assignment.size) {
            return false;
        }
        for (Map.Entry<String, Structure<?>.Element> entry : this.kvMap.entrySet()) {
            if (!assignment.kvMap.containsKey(entry.getKey())) {
                return false;
            }
            if (entry.getValue() == null || assignment.kvMap.get(entry.getKey()) == null) {
                if (entry.getValue() != assignment.kvMap.get(entry.getKey())) {
                    return false;
                }
            }
            if (!entry.getValue().equals(assignment.kvMap.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
