package com.mybatis.model;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mybatis.inter.UserOperation;

public class UpdateUserInfo {

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
	
    public boolean updateUserBalanceOrDebt(int activeID , String passiveAccountOfID , String method , int num){
        //先得到用户,然后修改，提交。
        SqlSession session = sqlSessionFactory.openSession();
        boolean errorMark = false;
        try {
            UserOperation userOperation=session.getMapper(UserOperation.class);
            if(method.equals("debt")){
            	UserBalance userBalanceActiveBefore = userOperation.SelectUserBalanceById(activeID);
            	UserBalance userBalanceActiveAfter = userOperation.SelectUserBalanceById(activeID);
            	UserInfo input = userOperation.SelectUserInfoByIdcard(passiveAccountOfID);
            	UserInfo cookie = userOperation.SelectUserInfoById(activeID);
            	String inputID = input.getID();
            	String cookieID = cookie.getID();
            	if(!inputID.equals(cookieID)){
            		return errorMark ;
            	}
            	int before = userBalanceActiveBefore.getDebt();
            	userBalanceActiveAfter.setDebt(num + before);
                userOperation.updateBalanceOrDebt(userBalanceActiveAfter);
                session.commit();
            }else{
                UserInfo userInfoPassiveBefore = userOperation.SelectUserInfoByAccount(passiveAccountOfID);
                int userInfoPassiveBeforeInt = userInfoPassiveBefore.getUser_id();
            	UserBalance userBalancePassiveAfter = userOperation.SelectUserBalanceById(userInfoPassiveBeforeInt);
            	UserBalance userBalanceActiveBefore = userOperation.SelectUserBalanceById(activeID);
            	int ActiveBefore = userBalanceActiveBefore.getAccount_balance();
            	//控制不能為負數
            	if(ActiveBefore - num <= 0){
            		System.out.println("the account is not enough");         		
            		return errorMark = true;
            	}
            	int PassiveAfter = userBalancePassiveAfter.getAccount_balance();
            	userBalanceActiveBefore.setAccount_balance(ActiveBefore - num);
            	userBalancePassiveAfter.setAccount_balance(PassiveAfter + num);
            	userOperation.updateBalanceOrDebt(userBalanceActiveBefore);
            	userOperation.updateBalanceOrDebt(userBalancePassiveAfter);
                session.commit();
            	}            
        } finally {
            session.close();
        }
        return errorMark;
    }
    
	public void chanegeUserPassword(String account , String password){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			UserOperation userOperation=session.getMapper(UserOperation.class);
			UserInfo userInfo = userOperation.SelectUserInfoByAccount(account);
			
			userInfo.setPassword(password);
			System.out.println("---change passoword successful---");
			System.out.println(userInfo.getID());
			System.out.println(userInfo.getName());
			System.out.println(userInfo.getAccount());
			System.out.println(userInfo.getPassword());
			userOperation.updatepassword(userInfo);
			session.commit();
		}finally{
			session.close();
		}
		
	}
	
	
}
