import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
public class HuffmanTree {
    Map<Byte, String> codesTable = new HashMap<>();
    Map<String, Byte> originalTable = new HashMap<>();
    Map<Byte, Integer> frequencyMap;


    byte[] fileBytes;
    HuffmanTree(byte[] fileBytes){
        this.fileBytes = fileBytes;
        this.frequencyMap = getFrequencyMap(fileBytes);
        int size = frequencyMap.size();
        PriorityQueue<TreeNode> queue = new PriorityQueue<>(size, new ImplementComparator());


        for(Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()){
            TreeNode node = new TreeNode();
            node.aByte = entry.getKey();
            node.count = entry.getValue();
            queue.add(node);
        }

        TreeNode root = null;

        while(queue.size() > 1){
            TreeNode x = queue.peek();
            queue.poll();

            TreeNode y = queue.peek();
            queue.poll();

            TreeNode f = new TreeNode();
            f.count = x.count + y.count;
            f.aByte = 0;
            f.left = x;
            f.right = y;
            root = f;
            queue.add(f);
        }

        generateHashTables(root, "");


    }

    public void generateHashTables(TreeNode root, String s) {
        if (root.left == null && root.right == null) {
            codesTable.put(root.aByte, s);
            originalTable.put(s, root.aByte);
            return;
        }
        generateHashTables(root.left, s + "0");
        generateHashTables(root.right, s + "1");
    }

    public static Map<Byte, Integer> getFrequencyMap(byte[] data){
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte datum : data) {
            if (frequencyMap.containsKey(datum)) {
                int count = frequencyMap.get(datum);
                count++;
                frequencyMap.put(datum, count);
            } else {
                frequencyMap.put(datum, 1);
            }
        }
        return frequencyMap;
    }
}