import interfaces.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@Setter
@Getter
public class Assignment{
    private Map<String, Structure<?>.Element> kvMap;
    private int size;
    public Assignment(Map<String, Structure<?>.Element> kvMap) {
        this.kvMap = kvMap;
        this.size = kvMap.size();
    }
}
