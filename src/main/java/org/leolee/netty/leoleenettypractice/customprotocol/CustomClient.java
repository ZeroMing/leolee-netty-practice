package org.leolee.netty.leoleenettypractice.customprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.UUID;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:57
 * @since:
 */
public class CustomClient {
    public static void main(String args[]) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 添加编解码器, 由于ByteToMessageDecoder的子类无法使用@Sharable注解,
                            // 这里必须给每个Handler都添加一个独立的Decoder.
                            // pipeline.addLast(new CustomDecoder());
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,4,4,0,0));
                            pipeline.addLast(new CustomEncoder());
                            // and then business logic.
                            pipeline.addLast(new CustomClientHandler());
                        }
                    });

            // Start the connection attempt.
            Channel ch = b.connect("127.0.0.1", 9999).sync().channel();
            String content = "I'm the luck protocol!";

            CustomData customData = new CustomData(content.length(), content.getBytes());
            ch.writeAndFlush(customData);

        } finally {
            // group.shutdownGracefully();
        }
    }

}
