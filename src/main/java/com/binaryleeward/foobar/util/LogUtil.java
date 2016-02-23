package com.binaryleeward.foobar.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtil {
	
	private static Log log = LogFactory.getLog(LogUtil.class);

	public static void error(Exception e){
		log.error(e);
	}
	
	public static void info(String msg){
		log.info(msg);
	}
	
	
}
