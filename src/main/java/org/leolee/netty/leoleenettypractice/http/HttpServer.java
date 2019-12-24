package org.leolee.netty.leoleenettypractice.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 10:47
 * @since:
 */
public class HttpServer {

    public static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    public static EventLoopGroup workerGroup = new NioEventLoopGroup();

    int port ;

    public HttpServer(int port){
        this.port = port;
    }


    public  void star() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                // 自适应接收缓冲区
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                .option(ChannelOption.SO_BACKLOG,100)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();
                        // netty是基于http的，所以要添加http编码、解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 对写大数据流的支持
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 设置单次请求的文件大小上限
                        //  HttpMessage 和 HttpContent 聚合成一个 FullHttpRequest 或者 FullHttpResponse （取决于是处理请求还是响应），
                        //  而且它还可以帮助你在解码时忽略是否为“块”传输方式。
                        pipeline.addLast("httpAggregator",new HttpObjectAggregator(1024*1024*10));
                        // 在现实应用中，通过在POST大数据时，才会使用100-continue协议
                        pipeline.addLast(new HttpServerExpectContinueHandler());
                        pipeline.addLast(new HttpRequestHandler());
//                        // websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
//                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));


                    }
                })
                .bind(port).sync().channel().closeFuture().sync();
    }
}
