package com.binaryleeward.foobar.exception;

public enum ErrorCode {
	
	LOGIN(1000,"login error"), //LOGIN ERROR
	
	
	SYSTEM(9999,"system error"),
	;
	
	private int code;
	private String msg;
	private ErrorCode(int code,String msg){
		this.code = code;
		this.msg = msg;
	}
	public int getCode(){
		return code;
	}
	public String getMsg(){
		return msg;
	}
	
}
