package question1;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class LogAnalyze {
    private static final String RESOURCES = "src/main/resources/access.log";
    public static void main(String[] args) throws IOException {
        File file = new File(RESOURCES);
        //POST、GET请求数量和请求总数
        Map<String, Integer> requestTotal = getRequestTotal(file);
        System.out.println(requestTotal);
        //10个请求次数最多的url和对应的次数
        List<Url> mostRequest = mostRequest(file);
        mostRequest.forEach(System.out :: println);
        //按AAA分类输出
        LinkedListMultimap<String, String> classifyUrl = getClassifyUrl(file);
        Set<String> keySet = classifyUrl.keySet();
        keySet.forEach((String key)-> System.out.println(key+"="+classifyUrl.get(key)));
    }

    /**
     * 获取到文件里GET和POST的数量.
     * 获取文件里请求总量
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String,Integer> getRequestTotal(File file) throws IOException {
        Map<String,Integer> result = new HashMap<>();
        int requestTotal = 0;
        Multiset typeTotal = HashMultiset.create();
        List<String> list = Files.readLines(file, Charsets.UTF_8);
        Iterator i = list.iterator();
        while (i.hasNext()){
            //对每一行分割，获得一个list。第一位是请求类型，第二位是请求
            typeTotal.add(Splitter.on(" ").splitToList((String)i.next()).get(0));
            requestTotal++;
        }
        result.put("POST", typeTotal.count("POST"));
        result.put("GET", typeTotal.count("GET"));
        result.put("Total", requestTotal);
        return result;
    }

    /**
     * 获得请求最频繁的10个接口和接口数量
     * @param file
     * @return
     */
    public static List<Url> mostRequest(File file) throws IOException {
        //一个小根堆
        PriorityQueue<Url> priorityQueue = new PriorityQueue<>(10);
        ArrayList<Url> result = Lists.newArrayList();
        //一个存储了uri和对应次数的Multset
        Multiset uriAndTotal = getUriAndTotal(file);
        //存储了文件里所有且不重复url的set
        final Set<String> urlSet = getUrlSet(file);
        //遍历set，循环取值，依次比较
        for (String urlName:urlSet) {
            int count = uriAndTotal.count(urlName);
            //把每一个url和对应的次数封装进url对象。
            Url url = new Url(urlName,count);
            //维护一个大小为10的优先级队列
            if(priorityQueue.size() < 10){
                priorityQueue.add(url);
            }else {
                //将每一个url的次数和优先级队列里第一个的url次数比较，两者中大的会重新放入优先级队列
                Url url1 = priorityQueue.poll();
                priorityQueue.add(url1.getTotal() > url.getTotal() ? url1 : url);
            }
        }
        //将结果放入map中
        while (!priorityQueue.isEmpty()){
            result.add(priorityQueue.poll());
        }
        return result;
    }

    /**
     * 根据AAA分类输出
     * @param file
     * @return
     */
    public static LinkedListMultimap<String, String> getClassifyUrl(File file) throws IOException {
        LinkedListMultimap<String, String> result = LinkedListMultimap.create();
        //获取每一行
        Set<String> urlSet = getUrlSet(file);
        for (String url:urlSet) {
            result.put(Splitter.on("/").splitToList(url).get(1),url);
        }
        return result;
    }

    /**
     * 获取到文件里一个所有url且不重复的set
     * @param file
     * @return
     * @throws IOException
     */
    public static Set<String> getUrlSet(File file) throws IOException {
        Set<String> result = new HashSet<>();
        List<String> readLines = Files.readLines(file, Charsets.UTF_8);
        Iterator i = readLines.iterator();
        while (i.hasNext()){
            List<String> list = Splitter.on(" ").splitToList((String) i.next());
            if(!result.contains(list.get(1))){
                result.add(list.get(1));
            }
        }
        return result;
    }

    /**
     * 获得一个Multiset，此set包含了url和对应的请求次数。
     * @param file
     * @return
     * @throws IOException
     */
    public static Multiset getUriAndTotal(File file) throws IOException {
        HashMultiset<String> multiset = HashMultiset.create();
        List<String> list = Files.readLines(file, Charsets.UTF_8);
        Iterator i = list.iterator();
        while (i.hasNext()){
            multiset.add(Splitter.on(" ").splitToList((String)i.next()).get(1));
        }
        return multiset;
    }

}

