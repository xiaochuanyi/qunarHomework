package question5;

import com.google.common.base.*;

import java.io.*;
import java.net.*;
import java.util.*;

class ServerThread implements Runnable{
    Socket socket = null;
    ServerSocket serverSocket = null;
    InputStream inputStream = null;
    public ServerThread(Socket socket, ServerSocket serverSocket){
        this.socket = socket;
        this.serverSocket = serverSocket;
    }
    @Override
    public void run(){
        try {
            inputStream = socket.getInputStream();
            byte[] inputByte = new byte[1024];
            int len;
            while((len = inputStream.read(inputByte))!=-1){
                //将数组传入字符串中
                String str = new String(inputByte,0,len,"UTF-8");
                //返回结果
                outInfo(str,socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据输入的url返回给客户端对应的结果
     * @param url
     * @param socket
     */
    public static void outInfo(String url,Socket socket){
        int allCharacterCount = 0;
        int chineseCount = 0;
        int englishCount = 0;
        int punctuationCount = 0;
        String info = "";

        try {
            //把字符串分割为数组
            List<String> list = Splitter.fixedLength(1).trimResults().omitEmptyStrings().splitToList(getUrlInfo(url));
            //总字符数
            allCharacterCount = list.size();
            //判断是否输入了网址
            if(!url.equals("")) {
                for (String string : list) {
                    if (string.matches("^[\u4e00-\u9fa5]+$")) {
                        chineseCount++;
                    } else if (string.matches("^[a-zA-Z]*")) {
                        englishCount++;
                    } else if (string.matches("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")) {
                        punctuationCount++;
                    }
                }
                info = "总字符数为" + allCharacterCount + "\n中文数为" + chineseCount + "\n英文数为" + englishCount+ "\n标点符号数为" + punctuationCount;
                //向client端返回结果
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(info.getBytes("UTF-8"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过url获取到对应的信息
     * @param urlAddress
     * @return
     */
    public static String getUrlInfo(String urlAddress){
        StringBuilder resultReader = null;
        try {
            //获取到url对应的http请求
            URL url = new URL(urlAddress);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.connect();
            //请求码为200时获取网页内容
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //获取内容
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader urlInputStreamReader = new InputStreamReader(inputStream);
                //将网页信息写入到BufferReader里
                BufferedReader bufferedReader = new BufferedReader(urlInputStreamReader);
                resultReader = new StringBuilder();
                String everyLine = null;
                while ((everyLine = bufferedReader.readLine()) != null){
                    resultReader.append(everyLine);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultReader.toString();
    }
}
public class Server{
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        Thread server = null;
        OutputStream out = null;
        try {
            //创建一个ServerSocket对象，指明自身端口号
            serverSocket = new ServerSocket(7700);
            //调用这个方法获取socket对象
            socket=serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, serverSocket);
            //创建线程对象，并传入参数
            server = new Thread(serverThread);
            server.start();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
