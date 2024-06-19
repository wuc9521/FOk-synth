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
}
