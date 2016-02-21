package com.binaryleeward.foobar.service;

public interface MessageProcessService<REQ,RES> {
	RES process(REQ req);
}
