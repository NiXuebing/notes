package com.ice.notes.code.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServer extends Thread {

    @Override
    public void run() {
        // 获得一个ServerSocket通道
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            // 设置通道为非阻塞
            serverChannel.configureBlocking(false);
            // 将该通道对应的ServerSocket绑定到port端口
            serverChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), 8000));
            // 获得一个通道管理器
            ;
            //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
            //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端启动成功！");
            // 轮询访问selector
            while (true) {
                //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
                selector.select();
                // 获得selector中选中的项的迭代器，选中的项为注册的事件
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // 客户端请求连接事件
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        // 获得和客户端连接的通道
                        try (SocketChannel channel = server.accept()) {
                            // 设置成非阻塞
                            //channel.configureBlocking(false);
                            //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                            //channel.register(selector, SelectionKey.OP_READ);
                            //在这里可以给客户端发送信息哦
                            System.out.println("2222222");
                            channel.write(Charset.defaultCharset().encode("向客户端发送了一条信息!"));
                        }
                    } else if (key.isReadable()) {
                        // 获得了可读的事件
                        read(key);
                    }
                    // 删除已选的key,以防重复处理
                    iter.remove();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 处理读取客户端发来的信息 的事件
     *
     * @param key
     * @throws IOException
     */
    private void read(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端收到信息：" + msg);
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
        // 将消息回送给客户端
        channel.write(outBuffer);
    }

    /**
     * 启动服务端测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.start();
//        NIODemoServer nioServer = new NIODemoServer();
//        nioServer.start();


//        NIOClient client = new NIOClient();
//        client.initClient("localhost", 8000);
//        client.start();

//        try (Socket c = new Socket(InetAddress.getLocalHost(), 8000)) {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
//            bufferedReader.lines().forEach(System.out::println);
//        }

        ExecutorService executor = Executors.newFixedThreadPool(8);
        while (true) {
            try (Socket c = new Socket(InetAddress.getLocalHost(), 8000)) {
                BR requestHandler = new BR(c);
                executor.execute(requestHandler);
                System.out.println(Thread.activeCount());
                Thread.sleep(500);

                if (Thread.activeCount() >= 10) {
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

class BR extends Thread {
    private Socket socket;

    BR(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedReader.lines().forEach(System.out::println);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
}
