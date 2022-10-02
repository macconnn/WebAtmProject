package com.main;

import java.io.Reader;
import java.util.List;
import java.util.logging.Level;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mybatis.inter.UserOperation;
import com.mybatis.method.log.Log;
import com.mybatis.model.CreateNewUser;
import com.mybatis.model.UpdateUserInfo;
import com.mybatis.model.UserBalance;
import com.mybatis.model.UserInfo;
import com.mybatis.model.UserValue;
import com.mybatis.model.inputControl;

public class Main {
	
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader; 

    static{
        try{
            reader    = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    

    public static void main(String[] args) {
    	try{
    	Log mylog = new Log("log.txt");
    	mylog.logger.setLevel(Level.ALL);
    
    	mylog.logger.info("this is info");
    	mylog.logger.warning("this is warning");
    	
    }catch(Exception e){
    	
    }
   
    }
}
