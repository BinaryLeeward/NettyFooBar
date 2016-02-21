package com.binaryleeward.foobar;

import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.SkillMessage;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelHandlerAdapter { // (1)
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		try {
			System.err.println("channel:"+ctx.channel().id());
			System.err.println(msg);
			if(msg instanceof WrapMessage){
				System.err.println(((WrapMessage) msg).getMessageType());
			}
			ctx.writeAndFlush(WrapMessage.newBuilder().setMessageType(WrapMessage.MessageType.SkillMessage).setSkillMessage(SkillMessage.newBuilder().setSkillId(1111).build()).build());
		} finally {
//			 ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}