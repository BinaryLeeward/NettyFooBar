package com.binaryleeward.foobar.handler;
import io.netty.channel.ChannelHandler.Sharable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage;
import com.binaryleeward.foobar.service.ProtobufMessageProxyService;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
@Component
@Sharable
public class ProtobufMessageHandler extends ChannelHandlerAdapter { // (1)
	
	@Autowired
	private ProtobufMessageProxyService protobufMessageProxyService;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		try {
			if(!(msg instanceof WrapMessage)){
				ctx.close();
				return;
			}
			WrapMessage wrapMsg = (WrapMessage)msg;
			WrapMessage resultMsg = protobufMessageProxyService.process(wrapMsg);
			if(resultMsg != null){
				ctx.writeAndFlush(resultMsg);
			}
		} finally {
			 ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}