package com.mybatis.inter;



import org.apache.ibatis.annotations.Param;

import com.mybatis.model.UserBalance;
import com.mybatis.model.UserInfo;

public interface UserOperation {
	//上面兩個是判斷登入時張號密碼是否有錯誤
	public UserInfo SelectUserInfoByAccountAndPassword(@Param("account")String account , @Param("password")String password);
	//利用ID 在userinfo搜尋資料
	public UserInfo SelectUserInfoPassword(String password);
	public UserInfo SelectUserInfoByIdcard(String ID);
	public UserInfo SelectUserInfoByAccount(String account);
	//利用user_id 在兩個table搜尋資料
	public UserInfo SelectUserInfoById(int user_id);
	public UserBalance SelectUserBalanceById(int user_id);
	//轉帳貸款
    public void addUserBalance(int user_id);
    public void addUserInfo(UserInfo userInfo);
    public void updateBalanceOrDebt(UserBalance userBalance);
    //換密碼
    public void updatepassword(UserInfo userInfo);
}
