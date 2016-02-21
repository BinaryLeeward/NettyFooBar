package com.binaryleeward.foobar;

import java.net.Socket;

import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.LoginMessage;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.SkillMessage;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage;

public class TestClient {

	public static void main(String[] args) {
		try {
			Socket s  = new Socket("localhost", 9999);
			WrapMessage.newBuilder().setMessageType(WrapMessage.MessageType.LoginMessage).setLoginMessage(LoginMessage.newBuilder().setUserName("user1").setPassword("pwd1").build()).build().writeDelimitedTo(s.getOutputStream());
			WrapMessage.newBuilder().setMessageType(WrapMessage.MessageType.SkillMessage).setSkillMessage(SkillMessage.newBuilder().setSkillId(222).build()).build().writeDelimitedTo(s.getOutputStream());
			WrapMessage p = WrapMessage.parseDelimitedFrom(s.getInputStream());
			System.err.println(p);
			Thread.currentThread().join();
			s.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
