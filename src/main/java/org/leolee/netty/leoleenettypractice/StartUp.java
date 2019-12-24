package org.leolee.netty.leoleenettypractice;

import org.leolee.netty.leoleenettypractice.customprotocol.CustomServer;
import org.leolee.netty.leoleenettypractice.http.HttpServer;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 11:21
 * @since:
 */
public class StartUp {
    public static void main(String[] args) throws InterruptedException {
        CustomServer server = new CustomServer(9999);
        server.start();
    }
}
