package org.leolee.netty.leoleenettypractice.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:30
 * @since:
 */
public class CustomEncoder extends MessageToByteEncoder<CustomData> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CustomData msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getHead());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getConctent());
    }
}
