package com.mybatis.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mybatis.inter.UserOperation;
import com.mybatis.method.log.WriteLogToFile;
import com.mybatis.model.CreateNewUser;
import com.mybatis.model.UpdateUserInfo;
import com.mybatis.model.UserInfo;
import com.mybatis.model.UserValue;
import com.mybatis.model.hashPassword;
import com.mybatis.model.inputControl;

@Path("/")
@ApplicationPath("/api")
//http://localhost:8080/WebAtmCastle/api/
public class UserServer extends Application{
	
	
    private	 static final String S_SUCCESSFUL   = 		"operation succeeded";
    
    private  static final String F_LENGTH 	    = 		"the length error";
    private  static final String F_UNCORRECT    = 		"the input uncorrect";
    private  static final String F_EMPTY 	    = 		"empty input";
    private  static final String F_HTML 	    = 		"read html file has been error";
	

    @Context
    ServletContext servletContext;
	hashPassword hashPassword = new hashPassword();
	inputControl inputControl = new inputControl();
	WriteLogToFile writeLogToFile = new WriteLogToFile();
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader; 
    

    
    static{
        try{
            reader    = Resources.getResourceAsReader("Configuration.xml"); //¬M®gxml
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
	UserValue userValue = new UserValue();
	UpdateUserInfo updateUserInfo = new UpdateUserInfo();
	CreateNewUser createNewUser = new CreateNewUser();
	
	@GET
	@Path("/hello")
	public String hello(){
		return "hello world";
	}
	

	@GET
	@Path("/home/balance/account")
	public String getAccountBalance(@CookieParam("user_id") int user_id){
		if(user_id == 0){
			return null;
		}
		int result = userValue.checkBalance(user_id);
		String str = "\n" + "---check account balance--- \n" + "account balance : " + result;
		writeLogToFile.logRecord(str);
		System.out.println(str);
		return String.valueOf(result);
	}
	
	@GET
	@Path("/home/balance/debt")
	public String getDebt(@CookieParam("user_id") int user_id){
		if(user_id == 0){
			return null;
		}
		int result = userValue.checkDebt(user_id);
		String str = "\n" + "---check debt--- \n" + "debt : " + result;
		writeLogToFile.logRecord(str);
		return String.valueOf(result);
	}

	

	@GET
	@Path("/user/changepassword")
	public String changePassword(@QueryParam("account") String account , @QueryParam("newpassword") String password , 
								@QueryParam("checkPassword") String secondpassword){
		SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		UserInfo userInfo = userOperation.SelectUserInfoByAccount(account);
		if(userInfo == null){
			System.out.println("the account does not exist ");
			writeLogToFile.logRecord("---change password error---  \n" + F_UNCORRECT);
			return "the account does not exist ";
		}
		if(!password.equals(secondpassword)){
			System.out.println("there it's different between the new password and check new password , please try again ! ");
			writeLogToFile.logRecord("---change password error--- \n" + F_UNCORRECT);
			return "there it's different between the new password and check new password , please try again ! ";
		}
		String checkPassword = userInfo.getPassword();
		String hashPasswordString = hashPassword.getSHA256(password);
		if(hashPasswordString.equals(checkPassword)){
			writeLogToFile.logRecord("---change password error--- \n" + F_UNCORRECT);
			return "the new password cannot consistent with now";
		}
		updateUserInfo.chanegeUserPassword(account ,hashPasswordString);
		writeLogToFile.logRecord("---change password successful--- \n" + S_SUCCESSFUL);
		return "update successful";
	}

	@GET
	@Path("/home/debtortransfer")
	public String updateValue(@CookieParam("user_id") int user_id , @QueryParam("account") String passiveUserAccount ,
								 @QueryParam("number") String number , @QueryParam("method") String method){
		SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		UserInfo userInfo = userOperation.SelectUserInfoById(user_id);
		String userAccount = userInfo.getAccount();
		String userID = userInfo.getID();
        boolean checkNumber = inputControl.checkNumberSpcialChar(number);
        userID = userID.toUpperCase();
        if(checkNumber){
        	writeLogToFile.logRecord("---transfer or debt error--- \n" + "the number has special characters");
        	return "the number has special characters";
        }

		if(method.equals("debt")){
			passiveUserAccount = passiveUserAccount.toUpperCase();
			if(!passiveUserAccount.equals(userID)){
				writeLogToFile.logRecord("---transfer or debt error--- \n" + F_UNCORRECT);
				return "the ID is wrong please try again";
			}
			int numberInt = Integer.parseInt(number);
			if(numberInt > 50000){
				writeLogToFile.logRecord("---transfer or debt error--- \n"  + "the debt limit is 50K");
				return "the debt limit is 50K";
			}
			boolean checkError = updateUserInfo.updateUserBalanceOrDebt(user_id, passiveUserAccount, method, numberInt);
			if(checkError == false){
				String infoStr = "the " + method + " is successful and the " + method + " number is : " + numberInt;
				System.out.println("---transfer or debt---");
				System.out.println(infoStr);
				writeLogToFile.logRecord("---transfer or debt--- \n" + infoStr);
				return infoStr;
			}else{
				writeLogToFile.logRecord("---transfer or debt error--- \n"  + "the account cannot be minus");
				return "the account cannot be minus ! ";
			}
		}else{
			if(userOperation.SelectUserInfoByAccount(passiveUserAccount) == null ){
				writeLogToFile.logRecord("---transfer or debt error--- \n"  + F_UNCORRECT);
				return "the target account does not exist";
			}
			if(userAccount.equals(passiveUserAccount)){
				writeLogToFile.logRecord("---transfer or debt error--- \n" + "cannot tansfer to yourself");
				return "cannot tansfer to yourself";
			}
			int numberInt = Integer.parseInt(number);
			if(numberInt > 50000){
				writeLogToFile.logRecord("---transfer or debt error--- \n"  + "the transfer number limit is 50K");
				return "the transfer number limit is 50K";
			}
			boolean checkError = updateUserInfo.updateUserBalanceOrDebt(user_id, passiveUserAccount, method, numberInt);
			if(checkError == false){
				String infoStr = "the " + method + " is successful and the " + method + " number is : " + numberInt;
				System.out.println("---transfer or debt---");
				System.out.println(infoStr);
				writeLogToFile.logRecord("---transfer or debt--- \n"  + infoStr);
				return infoStr;
			}else{
				writeLogToFile.logRecord("---transfer or debt error--- \n"  + "the account cannot be minus");
				return "the account cannot be minus";
			}
		}
	}

	@POST
	@Path("/user/create/new")
	public String createNewUser(@FormParam("ID") String ID , @FormParam("name") String name ,
								@FormParam("account") String account , @FormParam("password") String password ,
								@FormParam("checkPassword") String checkPassword){
		SqlSession session = sqlSessionFactory.openSession();
		UserOperation userOperation=session.getMapper(UserOperation.class);
		inputControl inputControl = new inputControl();
		boolean checkID = inputControl.checkID(ID);
		System.out.println(checkID);

		if(ID.isEmpty() || name.isEmpty() || account.isEmpty() || password.isEmpty()){
			writeLogToFile.logRecord("---create account error--- \n"  + F_EMPTY);
			return "somethings is empty please fill it up";
		}		
		if(ID.length() != 10 || name.length() > 20 || account.length() > 30 || password.length() > 12){
			writeLogToFile.logRecord("---create account error--- \n" + F_LENGTH);
			return "input length error";
		}
		if(!password.equals(checkPassword)){
			writeLogToFile.logRecord("---create account error--- \n" + F_UNCORRECT);
			return "the password is not same please try again";
		}
		if(!checkID){
			writeLogToFile.logRecord("---create account error--- \n"  + F_UNCORRECT);
			return "the ID is uncorrect please try again";
		}
		if(userOperation.SelectUserInfoByIdcard(ID) != null || userOperation.SelectUserInfoByAccount(account) != null){
			writeLogToFile.logRecord("---create account error--- \n"  + F_UNCORRECT);
			return "the ID or Account has been repeat";
		}
		
		String hashPasswordString = hashPassword.getSHA256(password);
		createNewUser.createUser(ID ,name , account ,hashPasswordString);
		writeLogToFile.logRecord("---create account successful--- \n"  + S_SUCCESSFUL);
		return "create seccess";
	}

	@POST
	@Path("/home/login")
	public String userLogins(@FormParam("account")String account , @FormParam("password") String password , 
							@Context HttpServletResponse servletResponse)throws IOException{
		if(account.isEmpty() || password.isEmpty()){
			writeLogToFile.logRecord("---login error--- \n" + F_EMPTY);
			return "somethings is empty please fill it up";
		}
		String hashPasswordString = hashPassword.getSHA256(password);
		String result = userValue.logIn(account, hashPasswordString);
		System.out.println(result);
		if(result.equals("error")){
			writeLogToFile.logRecord("---login error--- \n" + F_UNCORRECT);
			return "the account or password is wrong";
		}
		Cookie cookie = new Cookie("user_id" ,result);
		cookie.setMaxAge(-1);
		servletResponse.addCookie(cookie);
		writeLogToFile.logRecord("---login successful--- \n" + S_SUCCESSFUL);
		System.out.println("\n"  + "---login successful---" + "\n" + "user account : " + account + "\n" + "user password : " + password);
		servletResponse.sendRedirect("/WebAtmCastle/api/home");
		System.out.println("cookie value is :  " + cookie.getValue());
		System.out.println("cookie key is :  " + cookie.getName());
		return "login successful";
	}
	
	
	private File readHtml(String path){
            String base = servletContext.getRealPath(path);
            File f = new File(String.format("%s", base));
            return f;
	}
	
	

	@GET
	@Path("/loginpage")
	public InputStream loginPage(){
        try {
        	return new FileInputStream(readHtml("/WEB-INF/website/login.html"));
        } catch (FileNotFoundException e) {
        	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	

	@GET
	@Path("/home")
	public InputStream homePage(@CookieParam("user_id") int user_id ){
		if(user_id == 0){
			return null;
		}
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/home.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/home/transfer")
	public InputStream transferPage(@CookieParam("user_id") int user_id ){
		if(user_id == 0){
			return null;
		}
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/transfer.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/home/debt")
	public InputStream debtPage(@CookieParam("user_id") int user_id ){
		if(user_id == 0){
			return null;
		}
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/debt.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/user/createpage")
	public InputStream createPage(){
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/createAccountPage.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/user/changepasswordpage")
	public InputStream changepasswordPage(){
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/changePasswordPage.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/home/balance/uservalue")
	public InputStream userValue(HttpServletRequest request){
        try {
            return new FileInputStream(readHtml("/WEB-INF/website/UserValue.html"));
        } catch (FileNotFoundException e) {
           	writeLogToFile.logRecord(F_HTML);
            return null;
        }
	}
	
	@GET
	@Path("/home/logout")
	public void  logout(@Context HttpServletResponse servletResponse) throws IOException{
		Cookie cookie = new Cookie("user_id", "");
		cookie.setMaxAge(0);
		servletResponse.addCookie(cookie);
		writeLogToFile.logRecord("---logout--- \n" + S_SUCCESSFUL);
		System.out.println("logout ! ");
		servletResponse.sendRedirect("/WebAtmCastle/api/loginpage");
	}

	
	
	
	
}
