package org.leolee.netty.leoleenettypractice.customprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.leolee.netty.leoleenettypractice.http.HttpRequestHandler;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 10:47
 * @since:
 */
public class CustomServer {

    public static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    public static EventLoopGroup workerGroup = new NioEventLoopGroup();

    int port ;

    public CustomServer(int port){
        this.port = port;
    }


    public  void start() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new LoggingHandler(LogLevel.DEBUG))
                .option(ChannelOption.TCP_NODELAY,true)
                // 自适应接收缓冲区
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                .option(ChannelOption.SO_BACKLOG,100)
                .option(ChannelOption.SO_BROADCAST,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // pipeline.addLast(new CustomDecoder());
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,4,4,0,0));
                        pipeline.addLast(new CustomEncoder());
                        pipeline.addLast(new CustomServerHandler());
                    }
                })
                .bind(port).sync().channel().closeFuture().sync();
    }
}
