package com.mybatis.inter;



import org.apache.ibatis.annotations.Param;

import com.mybatis.model.UserBalance;
import com.mybatis.model.UserInfo;

public interface UserOperation {
	//�W����ӬO�P�_�n�J�ɱi���K�X�O�_�����~
	public UserInfo SelectUserInfoByAccountAndPassword(@Param("account")String account , @Param("password")String password);
	//�Q��ID �buserinfo�j�M���
	public UserInfo SelectUserInfoPassword(String password);
	public UserInfo SelectUserInfoByIdcard(String ID);
	public UserInfo SelectUserInfoByAccount(String account);
	//�Q��user_id �b���table�j�M���
	public UserInfo SelectUserInfoById(int user_id);
	public UserBalance SelectUserBalanceById(int user_id);
	//��b�U��
    public void addUserBalance(int user_id);
    public void addUserInfo(UserInfo userInfo);
    public void updateBalanceOrDebt(UserBalance userBalance);
    //���K�X
    public void updatepassword(UserInfo userInfo);
}
