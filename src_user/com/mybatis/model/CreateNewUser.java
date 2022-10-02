package com.mybatis.model;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mybatis.inter.UserOperation;

public class CreateNewUser {

	
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
    
    
    public void createUser(String ID ,String name , String account , String password){
        UserInfo userInfo=new UserInfo();
        userInfo.setID(ID);
        userInfo.setName(name);
        userInfo.setAccount(account);
        userInfo.setPassword(password);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserOperation userOperation=session.getMapper(UserOperation.class);
            userOperation.addUserInfo(userInfo);         
            int userId = userInfo.getUser_id();
            userOperation.addUserBalance(userId);
            session.commit();
        } finally {
            session.close();
        }
    }
    
    
	
	
}
