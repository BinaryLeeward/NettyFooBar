package com.binaryleeward.foobar.handler;

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
public class ProtobufMessageHandler extends ChannelHandlerAdapter { // (1)
	
	private ProtobufMessageProxyService messageProxyService;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		try {
			if(!(msg instanceof WrapMessage)){
				ctx.close();
				return;
			}
			WrapMessage wrapMsg = (WrapMessage)msg;
			ctx.writeAndFlush(messageProxyService.process(wrapMsg));
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