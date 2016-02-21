package com.binaryleeward.foobar.exception;

public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;
	
	public BaseException(){
		this.errorCode = ErrorCode.SYSTEM;
	}
	public BaseException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public int getCode(){
		return errorCode.getCode();
	}
	public String getMessage(){
		return errorCode.getMsg();
	}
}
