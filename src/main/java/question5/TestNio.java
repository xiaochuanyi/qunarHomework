package question5;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class TestNio {
    @Test
    public void test() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\课用软件\\workplace\\qunar\\src\\main\\java\\question5\\test.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (channel.read(buf) != -1){
            buf.flip();
            while(buf.hasRemaining()){
                System.out.println((char)buf.get());
            }
            buf.clear();
        }

    }
}
