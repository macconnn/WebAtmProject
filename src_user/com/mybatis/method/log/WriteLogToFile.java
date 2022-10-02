package com.mybatis.method.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public class WriteLogToFile {

	public void logRecord(String msg){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dtf.format(LocalDateTime.now());
    	try{
    	Log mylog = new Log(date +  " log.txt");
    	mylog.logger.setLevel(Level.INFO);
    	mylog.logger.info("info :"  + msg);
    }catch(Exception e){
    	
    }
}
	
	
}
