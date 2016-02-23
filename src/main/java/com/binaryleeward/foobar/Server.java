package com.binaryleeward.foobar;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.binaryleeward.foobar.handler.ProtobufMessageHandler;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage;

@Component
public class Server {
	
	@Autowired
	private ProtobufMessageHandler protobufMessageHandler;
	@Value("#{server['port']}")
	private int port;

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 ch.pipeline().addLast("frameDecoder",new ProtobufVarint32FrameDecoder());
                	 ch.pipeline().addLast("protobufDecoder",new ProtobufDecoder(WrapMessage.getDefaultInstance()));
                	 ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                	 ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                	 ch.pipeline().addLast(protobufMessageHandler);
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    public void start(){
    	 int port = 9999;
         try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
