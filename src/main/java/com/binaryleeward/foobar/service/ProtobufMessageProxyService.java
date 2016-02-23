package com.binaryleeward.foobar.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.binaryleeward.foobar.exception.BaseException;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.ErrorMessage;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage;
import com.binaryleeward.foobar.protocol.protos.WrapMessageProtos.WrapMessage.Builder;
import com.binaryleeward.foobar.service.impl.LoginService;
import com.binaryleeward.foobar.util.LogUtil;
import com.google.protobuf.Descriptors.FieldDescriptor;

@Service
public class ProtobufMessageProxyService implements InitializingBean{
	
	@Autowired
	private LoginService loginService;
	
	@SuppressWarnings("rawtypes")
	private Map<WrapMessage.MessageType,MessageProcessService> messageProcessorMap = new HashMap<WrapMessage.MessageType,MessageProcessService>();
	
	private void init(){
		messageProcessorMap.put(WrapMessage.MessageType.LoginMessage, loginService);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WrapMessage process(WrapMessage wrapMsg) {
		try {
			MessageProcessService ps = messageProcessorMap.get(wrapMsg.getMessageType());
			if(ps == null){
				throw new BaseException(wrapMsg.getMessageType().toString() +" process service not init");
			}
			return wrap(ps.process(parse(wrapMsg)));
		} catch (BaseException e) {
			LogUtil.error(e);
			return WrapMessage.newBuilder()
					.setMessageType(WrapMessage.MessageType.ErrorMessage)
					.setErrorMessage(ErrorMessage.newBuilder().setCode(e.getCode()).setText(e.getMessage()).build())
					.build();
		}
	}
	
	//parse real msg from wrapMsg
	private Object parse(WrapMessage wrapMsg) throws BaseException{
		Map<FieldDescriptor, Object> fields = wrapMsg.getAllFields();
		for(Map.Entry<FieldDescriptor, Object> entry : fields.entrySet()){
			if(entry.getKey().getType() == FieldDescriptor.Type.MESSAGE && wrapMsg.getMessageType().toString().equals(entry.getKey().getMessageType().getName())){
				return entry.getValue();
			}
		}
		throw new BaseException("parse msg error");
	} 
	
	//wrap msg
	private WrapMessage wrap(Object msg) throws BaseException{
		if(msg == null){
			return null;
		}
		Builder wm = WrapMessage.newBuilder();
		wm.setMessageType(WrapMessage.MessageType.valueOf(msg.getClass().getSimpleName()));
		boolean wrapSuccess = false;
		for(FieldDescriptor fd : wm.getDescriptorForType().getFields()){
			if (fd.getType() == FieldDescriptor.Type.MESSAGE
					&& fd.getMessageType().getName().equals(msg.getClass().getSimpleName())) {
				wm.setField(fd, msg);
				wrapSuccess = true;
			}
		}
		if(!wrapSuccess){
			throw new BaseException("wrap msg error");
		}
		return wm.build();
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}
}
