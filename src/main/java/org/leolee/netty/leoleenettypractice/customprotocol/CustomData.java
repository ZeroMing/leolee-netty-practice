package org.leolee.netty.leoleenettypractice.customprotocol;

/**
 * 自定义协议内容
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:12
 * @since:
 */
public class CustomData {

    /**
     *
     * 自己定义的协议
     *  数据包格式
     * +——----——+——-----——+——----——+
     * |协议开始标志|  长度             |   数据       |
     * +——----——+——-----——+——----——+
     * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
     * 2.传输数据的长度contentLength，int类型
     * 3.要传输的数据
     *
     */

    /**
     * 消息开头的信息标志
     * 是一个常量 X077
     */
    private  final int head = CustomConstants.HEAD_DATA;

    /**
     * 消息的长度
     */
    private int contentLength;

    /**
     * 消息的内容
     */
    private byte[] conctent;


    public CustomData() {
        super();
    }

    public CustomData(int contentLength, byte[] conctent) {
        this.contentLength = contentLength;
        this.conctent = conctent;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getConctent() {
        return conctent;
    }

    public void setConctent(byte[] conctent) {
        this.conctent = conctent;
    }

    public int getHead() {
        return head;
    }
}
