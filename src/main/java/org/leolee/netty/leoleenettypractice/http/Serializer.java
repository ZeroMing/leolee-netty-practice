package org.leolee.netty.leoleenettypractice.http;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 11:09
 * @since:
 */
public interface Serializer {
    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
