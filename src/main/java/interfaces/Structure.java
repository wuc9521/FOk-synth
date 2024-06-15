package interfaces;
import java.util.*;

public abstract class Structure<DType> {
    public boolean pos = true;
    public Set<DType> data = new HashSet<DType>(); // the domain of the structure

} 