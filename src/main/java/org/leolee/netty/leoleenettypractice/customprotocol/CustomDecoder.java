package org.leolee.netty.leoleenettypractice.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:15
 * @since:
 */
public class CustomDecoder extends ByteToMessageDecoder {
    /*
    核心思想：
    - 1 在开始读取数据的时候先判断字节大小是否基本数据长度 （标志+数据长度）
    - 2 如果缓冲区数据太大，这种情况不正常，应该移动2048个字节，直接处理后面的字节。因为，可能是网络延迟导致，或者是恶意发送大量数据。
    - 3 开始读取缓冲区了，对缓冲区的操作。首先标记一下阅读标记点，然后开始寻找开始标记，如果不是开始标记，那么就跳过一个标记节点。
    - 4 如果找到了开始标记，那么就继续获取长度。如果长度大小大于缓冲区的可读长度，那么就证明还有数据还没到。就回滚到阅读标记点。继续等待数据。
    - 5 如果数据已经到达了，那么就开始读取数据区。
     */

    private final int BASE_LENGTH = 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        /**
         * 协议开始的标准head_data，int类型，占据4个字节.
         * 表示数据的长度contentLength，int类型，占据4个字节.
         */
        if(buffer.readableBytes() > BASE_LENGTH){
            // 2.
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
            if (buffer.readableBytes() > 2048) {
                // 将readerIndex移动
                buffer = buffer.skipBytes(buffer.readableBytes());
            }

            int beginRead;
            while (true){
                beginRead = buffer.readerIndex();
                buffer.markReaderIndex();
                // 如果读到协议头,跳出循环，继续读取长度
                if(buffer.readInt() == CustomConstants.HEAD_DATA){
                    break;
                }
                // 未读到协议头,退回标记
                buffer.resetReaderIndex();

                buffer.readByte();
                if(buffer.readableBytes() < BASE_LENGTH){
                    return;
                }
            }

            // 下一个int为消息长度
            int length = buffer.readInt();
            // 内容小于长度，说明还未发送完全，等待
            if(buffer.readableBytes() < length){
                buffer.resetReaderIndex();
                return;
            }

            byte[] data = new byte[length];
            buffer.readBytes(data);
            CustomData customData = new CustomData(length,data);
            out.add(customData);
        }
    }
}
