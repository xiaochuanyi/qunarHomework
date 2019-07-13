package question5;

import com.google.common.base.*;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread implements Runnable{
    OutputStream outputStream = null;
    Socket socket = null;
    public ClientThread(Socket socket){
        this.socket = socket;
        try {
            outputStream = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        try {
            String str = null;
            Scanner a = new Scanner(System.in);
            while((str=a.nextLine()) != null){
                outputStream.write(str.getBytes());
            }
            socket.shutdownOutput();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

public class Client{
    public static void main(String[] args) {
        Socket socket = null;
        InputStream inputStream = null;
        Thread client = null;
        try {
            //创建一个socket对象，通过构造器指明服务端的ip地址及接受的端口号
            socket = new Socket("127.0.0.1",7700);
            client = new Thread(new ClientThread(socket));
            client.start();
            inputStream = socket.getInputStream();//接受服务端发送的消息
            byte[] b = new byte[1024];
            int len;
            while((len = inputStream.read(b)) !=-1){
                //将数组传入字符串中
                String str = new String(b,0,len,"UTF-8");
                System.out.println(str);
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
