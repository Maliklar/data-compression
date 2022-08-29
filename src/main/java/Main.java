import org.apache.commons.lang3.ArrayUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Main {
    public static HuffmanTree huffmanTree;
    public static final String CMD_COMPRESS = "compress";
    public static final String CMD_UNCOMPRESS = "uncompress";

    public static void main(String[] args) throws Exception {
        if( args.length == 3 && args[0].equals(CMD_COMPRESS)){
            compressFile(args[1], args[2]);
        }
        else if(args[0].equals(CMD_UNCOMPRESS)){
            uncompressFile(args[1], args[2]);
        }
        else{
            System.out.println("Error");
        }


    }


    public static void compressFile(String path, String out) throws Exception{
        System.out.println("Compressing File");
        Path filePath = Paths.get(path);
        byte[] fileBytes = Files.readAllBytes(filePath);
        huffmanTree = new HuffmanTree(fileBytes);

        BitSet bitSet = new BitSet();
        int len = 0;
        for (byte fileByte : fileBytes) {
            String bString = huffmanTree.codesTable.get(fileByte);
            for (int j = 0; j < bString.length(); j++) {
                bitSet.set(len, bString.charAt(j) == '1');
                len++;
            }
        }
        byte[] compressedBytes = bitSet.toByteArray();
        FileOutputStream fos = new FileOutputStream(out+".cmp");
        fos.write(compressedBytes);
        fos.close();
        createFileTable(path, out);
        System.out.println("Done.");
        System.out.println("original size: "+fileBytes.length+"bytes");
        System.out.println("compressed size: "+compressedBytes.length+"bytes");
        System.out.println("File reduced in size by "+((float) compressedBytes.length/(float) fileBytes.length )*100+"%");
    }


    public static void uncompressFile(String filePath, String tablePath) throws Exception{
        FileInputStream fos = new FileInputStream(filePath);
        byte[] fileBytes = fos.readAllBytes();


        BitSet bitSet = BitSet.valueOf(fileBytes);
        List<Byte> bList = new ArrayList<>();
        StringBuilder code = new StringBuilder();
        FileData fileData = getFileTable(tablePath);
        System.out.println(bitSet.length());

        for (int i = 0; i < bitSet.length()+1; i++) {
            if(fileData.table.containsKey(code.toString())){
                bList.add(fileData.table.get(code.toString()));
                code = new StringBuilder();
            }
            if(bitSet.get(i))
                code.append("1");
            else
                code.append("0");

//            float percent = (float) ((float) i/(float)bitSet.length()) * 100;
//
//            System.out.printf("\r %.2f%s",percent, "%");

        }

        byte[] uncompressed = ArrayUtils.toPrimitive(bList.toArray(new Byte[0]));
        FileOutputStream fos3 = new FileOutputStream("output."+fileData.extension);
        fos3.write(uncompressed);
        fos3.close();
    }

    public static void createFileTable(String name, String out) throws Exception{
        FileData fileData = new FileData(name, huffmanTree.originalTable);
        FileOutputStream fos1 = new FileOutputStream("ct_"+out+".obj");
        ObjectOutputStream oos = new ObjectOutputStream(fos1);
        oos.writeObject(fileData);
        fos1.close();
    }

    public static FileData getFileTable(String dataPath) throws Exception{
        FileInputStream fos1 = new FileInputStream(dataPath);
        ObjectInputStream oos = new ObjectInputStream(fos1);
        FileData fileData = (FileData) oos.readObject();
        oos.close();
        fos1.close();
        return fileData;
    }

}
