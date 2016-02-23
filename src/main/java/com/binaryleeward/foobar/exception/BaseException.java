package com.binaryleeward.foobar.exception;

import org.springframework.util.StringUtils;

public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;
	private String extMsg;
	
	public BaseException(String msg){
		this.errorCode = ErrorCode.SYSTEM;
		this.extMsg = msg;
	}
	public BaseException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public int getCode(){
		return errorCode.getCode();
	}
	public String getMessage(){
		String msg = errorCode.getMsg();
		if(StringUtils.hasText(extMsg)){
			msg += ","+extMsg;
		}
		return msg;
	}
}
