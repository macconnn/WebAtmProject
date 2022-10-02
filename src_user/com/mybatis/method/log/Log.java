package com.mybatis.method.log;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javassist.bytecode.stackmap.BasicBlock.Catch;


public class Log {

	public Logger logger;
	FileHandler fh;
	
	
	public Log(String fileName) throws SecurityException ,IOException{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dtf.format(LocalDateTime.now());
		File dir = new File("/Users/sylvia/Desktop/javaee/webatm/log/" + date + " log");
		if(!dir.exists()){
			dir.mkdir();
		}
		File file = new File("/Users/sylvia/Desktop/javaee/webatm/log/" + date + " log/"   + fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		fh = new FileHandler("/Users/sylvia/Desktop/javaee/webatm/log/" + date + " log/" + fileName ,true);
		logger = Logger.getLogger("test");
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);	
	}


}
