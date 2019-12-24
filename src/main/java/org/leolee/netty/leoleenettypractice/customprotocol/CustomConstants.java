package org.leolee.netty.leoleenettypractice.customprotocol;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 14:22
 * @since:
 */
public class CustomConstants {
    // 4 字节
    public static final int HEAD_DATA = 0xCAFE;

    public static void main(String[] args) {
        System.out.println(HEAD_DATA);
    }

}
