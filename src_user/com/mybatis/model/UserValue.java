package com.mybatis.model;

import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mybatis.inter.UserOperation;

public class UserValue {
	
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
    
    
    public int checkBalance(int user_id){
    	SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		UserBalance userBalance = userOperation.SelectUserBalanceById(user_id);
		return userBalance.getAccount_balance();
    }
    
    public int checkDebt(int user_id){
    	SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		UserBalance userBalance = userOperation.SelectUserBalanceById(user_id);
		return userBalance.getDebt();
    }
    
    public String logIn(String account ,String password) {
    	SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		UserInfo checkAccount = userOperation.SelectUserInfoByAccount(account); 
		if(checkAccount == null){
			System.out.println("---login error---");
			System.out.println("the account does not exist");
			return "error";
		}
		String checkPassword = checkAccount.getPassword();
		if(!checkPassword.equals(password)){
			System.out.println("---login error---");
			System.out.println("password error");
			return "error";
		}
		UserInfo userInfo = userOperation.SelectUserInfoByAccountAndPassword(account, password);
		System.out.println(userInfo.getUser_id());
		return String.valueOf(userInfo.getUser_id());

			
    }
    
    
	
}
