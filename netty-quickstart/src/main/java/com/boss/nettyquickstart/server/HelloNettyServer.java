package com.boss.nettyquickstart.server;

import com.boss.nettyquickstart.handler.HelloNettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

/**
 * @Author 黄杰峰
 * @Date 2020/7/19 0019 14:16
 * @Description Netty入门
 */
public class HelloNettyServer {

    /**
     * 服务器返回Hello信息到客户端
     */
    @Test
    public void sayHello() {
        /**
         * 定义一对线程组（两个线程池）
         * 主线程组用来接收客户端的请求，但不做任何处理，只是把任务转发给从线程组处理
         * 从线程组处理请求
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 服务启动类，分配任务自动处理
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 建立线程模型
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置NIO双向通道
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HelloNettyServerInitializer());

            // 绑定端口，并设置为异步的Channel
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            //获取某个客户端所对应的chanel，关闭并设置同步方式
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //使用一种优雅的方式进行关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
