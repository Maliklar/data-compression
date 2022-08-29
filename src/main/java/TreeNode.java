import java.util.Comparator;

public class TreeNode {
    int count;
    byte aByte;
    TreeNode left;
    TreeNode right;
    TreeNode(){
        this.left = null;
        this.right = null;
    }
}

class  ImplementComparator implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode x, TreeNode y) {
        return x.count - y.count;
    }
}