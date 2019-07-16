package question4;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

import java.io.*;
import java.util.*;

/**
 * 模拟linux命令处理
 */
public class DealWithShell {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        //获得处理的结果并且输出
        dealCommand(input).forEach(System.out :: println);
    }

    /**
     * 处理命令，获取输出结果的list
     * @param command
     * @return
     * @throws IOException
     */
    public static List<String> dealCommand(String command) throws IOException {
        //根据|分割命令
        List<String> splitCommandList = Splitter.on("|").trimResults().splitToList(command);
        //根据第一个命令获取到需要读取的文件名
        String fileName = getFileName(splitCommandList.get(0));
        //读取当前文件
        List<String> readLines = Files.readLines(new File(fileName), Charsets.UTF_8);
        //默认第一次的输入结果为读取到的文件的内容
        List<String> result = readLines;
        for (String partCommand:splitCommandList) {
            //获取到处理的命令用于判断该调用哪一个方法
            String dealCommand = Splitter.on(" ").splitToList(partCommand).get(0);
            if(dealCommand.equals("wc")){
                readLines = dealWcCommand(result);
            }else if(dealCommand.equals("cat")){
                readLines = dealCatCommand(result);
            }else if(dealCommand.equals("grep")){
                readLines = dealGrepCommand(result,Splitter.on(" ").splitToList(partCommand).get(1));
            }
            //上一次命令的结果会作为下一次命令的输入
            result = readLines;
        }
        return result;
    }

    /**
     * 处理cat命令
     * @param input
     * @return
     */
    public static List<String> dealCatCommand(List<String> input){
        return  input;
    }

    /**
     * 处理grep命令
     * @param input
     * @param keyword
     * @return
     */
    public static List<String> dealGrepCommand(List<String> input,String keyword){
        ArrayList<String> result = Lists.newArrayList();
        for (String str:input) {
            if(str.contains(keyword)){
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 处理Wc -l 命令
     * @param input
     * @return
     */
    public static List<String> dealWcCommand(List<String> input){
        ArrayList<String> result = Lists.newArrayList();
        result.add(String.valueOf(input.size()));
        return result;
    }

    /**
     * 获取命令里的文件名
     * @param commond
     * @return
     */
    public static String getFileName(String commond){
        List<String> commondList = Splitter.on(" ").splitToList(commond);
        //根据命令的不同，文件名存储于list的索引也不同
        if(commondList.get(0).equals("cat")){
            return commondList.get(1);
        }else {
            return commondList.get(2);
        }
    }


}
