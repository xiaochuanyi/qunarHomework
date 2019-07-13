package question3;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

/**
 * 文本解密
 */
public class TextDecryption {
    private static final String ENCRYPTION_TXT = "src/main/resources/sdxl_template.txt";
    private static final String KEY_TXT = "src/main/resources/sdxl_prop.txt";
    private static final String DECRYPTION_TXT = "src/main/resources/sdxl.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(ENCRYPTION_TXT);
        List<String> encryptionList = Files.readLines(file, Charsets.UTF_8);
        for (String str:encryptionList) {
           if(str.contains("$natureOrder")){
               writeWithNatureOrder(str);
           }else if(str.contains("$indexOrder")){
               writeWithIndexOrder(str);
           }else if(str.contains("$charOrderDESC")) {
               writeWithCharOrderDESC(str);
           }else if(str.contains("$charOrder")){
               writeWithCharOrder(str);
           } else {
               //都不满足时，说明不需要解密，直接写入
              writeToTargetFile(DECRYPTION_TXT,str+"\n");
           }
        }
    }

    /**
     * 对IndexOrder的密文解密并且输入解密后的明文到目标文件中
     * @param incryption
     * @throws IOException
     */
    public static void writeWithIndexOrder(String incryption) throws IOException {
        Map<String, String> indexOrderMap = getIndexOrderMap(KEY_TXT);
        //获取密文明文对应的key
        String key = CharMatcher.DIGIT.retainFrom(incryption);
        //获得解密后的明文
        String decryption = incryption.replace("$indexOrder("+key+")", indexOrderMap.get(key));
        writeToTargetFile(DECRYPTION_TXT,decryption + "\n");
    }

    /**
     * 对NatureOrder的密文解密并且输入解密后的明文到目标文件中
     * @param incryption
     */
    public static void writeWithNatureOrder(String incryption) throws IOException {
        List<String> natureOrderList = getNatureOrderList(KEY_TXT);
        //获取密文明文对应的key
        Integer key =Integer.parseInt( CharMatcher.DIGIT.retainFrom(incryption));
        //获得解密后的明文
        String decryption = incryption.replace("$natureOrder("+key+")",natureOrderList.get(key));
        writeToTargetFile(DECRYPTION_TXT,decryption + "\n");
    }

    /**
     * 对CharOrder的密文解密并且输入解密后的明文到目标文件
     * @param incryption
     * @throws IOException
     */
    public static void writeWithCharOrder(String incryption) throws IOException {
        List<String> charOrderList = getCharOrderList(KEY_TXT);
        //获取密文明文对应的key
        Integer key =Integer.parseInt( CharMatcher.DIGIT.retainFrom(incryption));
        //获得解密后的明文
        String decryption = incryption.replace("$charOrder("+key+")",charOrderList.get(key));
        writeToTargetFile(DECRYPTION_TXT,decryption + "\n");
    }

    /**
     * 对CharOrderDESC的密文解密并且输入解密后的明文到目标文件
     * @param incryption
     * @throws IOException
     */
    public static void writeWithCharOrderDESC(String incryption) throws IOException{
        //获取密文明文对应关系的list
        List<String> getCharOrderDescList = Lists.reverse(getCharOrderList(KEY_TXT));
        //获取密文明文对应的key
        Integer key =Integer.parseInt( CharMatcher.DIGIT.retainFrom(incryption));
        //获得解密后的明文
        String decryption = incryption.replace("$charOrderDESC("+key+")",getCharOrderDescList.get(key));
        writeToTargetFile(DECRYPTION_TXT,decryption + "\n");
    }

    /**
     * 获取NatureOrder的List。
     * List索引对应解密的key
     * @return
     */
    public static List<String> getNatureOrderList(String keyFile) throws IOException {
        ArrayList<String> result = Lists.newArrayList();
        File file = new File(keyFile);
        for (String str:Files.readLines(file, Charsets.UTF_8)) {
            //list里，索引即为密文索引，索引对应的字符串即为密文索引对应的明文
            result.add(Splitter.on("\t").splitToList(str).get(1));
        }
        return result;
    }
    /**
     * 获得indexOrder 解密的map，密文为map的key，解密后的明文为map的value
     * @param keyFile
     * @return
     * @throws IOException
     */
    public static Map<String,String> getIndexOrderMap(String keyFile) throws IOException {
        Map<String,String> result = new HashMap<>();
        File file = new File(keyFile);
        for (String str:Files.readLines(file, Charsets.UTF_8)) {
            //分割后的每一行，第一位为密文索引，第二个为索引对应的明文
            List<String> keyList = Splitter.on("\t").splitToList(str);
            result.put(keyList.get(0),keyList.get(1));
        }
        return result;

    }
    /**
     * 获得字符排序的明文list。list索引即为key
     * @param keyFile
     * @return
     * @throws IOException
     */
    public static List<String> getCharOrderList(String keyFile) throws IOException {
        ArrayList<String> result = Lists.newArrayList();
        File file = new File(keyFile);
        List<String> list = Files.readLines(file, Charsets.UTF_8);
        for (String str:list) {
            result.add(Splitter.on("\t").splitToList(str).get(1));
        }
        //对result集合进行字符排序
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return result;
    }

    /**
     * 写入解密后的明文到目标文件中
     * @param fileName
     * @param decryption
     * @throws IOException
     */
    public static void writeToTargetFile(String fileName,String decryption) throws IOException {
        File file = new File(fileName);
        //写入文件内容。
        Files.asCharSink(file,Charsets.UTF_8,FileWriteMode.APPEND).write(decryption);
    }
}
