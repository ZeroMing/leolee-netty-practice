package org.leolee.netty.leoleenettypractice.customprotocol;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:33
 * @since:
 */
public class CustomServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CustomData) {
            CustomData customDate= (CustomData) msg;
            System.out.println(customDate.getHead() + "-"+customDate.getContentLength() + "-" + new String(customDate.getConctent()));
            ReferenceCountUtil.release(msg);
            String message = "服务端已收到!";
            ctx.writeAndFlush(new CustomData(message.length(),message.getBytes()));
        }else if(msg instanceof ByteBuf){
            ByteBuf byteBuffer = (ByteBuf)msg;
            int dataLength = byteBuffer.readableBytes();
            byte[] data = new byte[dataLength];
            byteBuffer.readBytes(data);
            System.out.println("收取:" + new String(data));

        }
    }
}
