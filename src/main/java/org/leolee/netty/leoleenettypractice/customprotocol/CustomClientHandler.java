package org.leolee.netty.leoleenettypractice.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 15:03
 * @since:
 */
public class CustomClientHandler extends SimpleChannelInboundHandler<CustomData> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CustomData message) throws Exception {
        System.out.println("服务端:" + message);
    }

}
