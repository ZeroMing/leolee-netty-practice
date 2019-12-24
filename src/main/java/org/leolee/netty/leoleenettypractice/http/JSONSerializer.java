package org.leolee.netty.leoleenettypractice.http;

import com.alibaba.fastjson.JSON;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 11:09
 * @since:
 */
public class JSONSerializer implements Serializer{

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
