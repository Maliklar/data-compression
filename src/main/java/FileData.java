import java.io.Serializable;
import java.util.Map;

public class FileData implements Serializable {
    public String name;
    public String extension;
    public Map<String, Byte> table;
    FileData(String name, Map<String, Byte> table){
        this.name = name;
        this.extension = this.name.split("\\.")[1];
        this.table = table;
    }
}
