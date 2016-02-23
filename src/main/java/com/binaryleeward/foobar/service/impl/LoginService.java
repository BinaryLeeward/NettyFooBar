package com.binaryleeward.foobar.service.impl;

import org.springframework.stereotype.Service;

import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.LoginMessage;
import com.binaryleeward.foobar.service.MessageProcessService;

@Service
public class LoginService implements MessageProcessService<LoginMessage,LoginMessage>{

	public LoginMessage process(LoginMessage loginMessage) {
		return LoginMessage.newBuilder().setUserName("test").setPassword("pwd").build();
	}
}
