package org.leolee.netty.leoleenettypractice.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;
import static org.springframework.http.HttpHeaders.CONNECTION;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/24 11:18
 * @since:
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //100 Continue
        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
        }

        // 获取请求的uri
        String uri = req.uri();
        HttpMethod method = req.method();
        if(method.name().equals(HttpMethod.GET.name())){
            QueryStringDecoder stringDecoder = new QueryStringDecoder(uri);
            // stringDecoder.path()
            Map<String, List<String>> parameters = stringDecoder.parameters();
            Set<Map.Entry<String, List<String>>> entries = parameters.entrySet();
            for(Map.Entry<String, List<String>> entry:entries){
                for(String value:entry.getValue()){
                    System.out.println(entry.getKey()+":"+value);
                }
            }
        }else if(method.equals(HttpMethod.POST)){
            HttpHeaders headers = req.headers();
            String contentType = headers.get("content-type");
            contentType = contentType.split(";")[0];

            if("application/json".equalsIgnoreCase(contentType)){


            }else if("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)){


            }else if("multipart/form-data".equals(contentType)){


            }else{
                // TODO DO Nothing
            }
        }


        User user = new User();
        user.setUserName("LeoLee");
        JSONSerializer jsonSerializer = new JSONSerializer();
        //将Java对象序列化成为二级制数据包
        byte[] content = jsonSerializer.serialize(user);

        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(content));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        // 将html write到客户端
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        /*if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
        }*/
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }



}
