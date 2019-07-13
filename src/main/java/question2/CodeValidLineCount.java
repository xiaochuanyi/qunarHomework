package question2;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import java.io.*;
import java.util.*;

/**
 * 统计有效字符串
 */
public class CodeValidLineCount {
    private static final String RESOURCES = "src/main/resources/StringUtils.java";
    private static final String TARGET_RESOURCES = "src/main/resources/validLineCount.txt";
    public static void main(String[] args) throws IOException {
        int count = 0;
        File file = new File(RESOURCES);
        List<String> list = Files.readLines(file, Charsets.UTF_8);
        //过滤到空行
        FluentIterable<String> from = FluentIterable.from(list).filter(e -> e != null && !e.trim().equals(""));
        for (String str:from) {
            //判断每一行是否以/ 或者 * 开头，/结尾。如果不是则判断为有效代码
            if(!Strings.commonPrefix(str.trim(),"/").equals("/")
                    &&!Strings.commonPrefix(str.trim(),"*").equals("*")
                    &&!Strings.commonSuffix(str.trim(),"/").equals("*/")){
                count++;
            }
            //创建目标文件
            File targetFile = getTargetFile(TARGET_RESOURCES);
            //将统计行数写入目标文件
            Files.asCharSink(targetFile,Charsets.UTF_8).write(String.valueOf(count));
        }
    }

    /**
     * 创建一个文件
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static File getTargetFile(String targetFile) throws IOException {
        File file = new File(targetFile);
        Files.touch(file);
        return file;
    }
}
