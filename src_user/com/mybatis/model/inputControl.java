package com.mybatis.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class inputControl {

	public boolean checkNumberSpcialChar(String num){

        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher checkNumber = pattern.matcher(num);
        boolean result = checkNumber.find();
		return result;
	}
	
	 public boolean checkID(String id){
		  int[] num=new int[10];
		  int[] rdd={10,11,12,13,14,15,16,17,34,18,19,20,21,22,35,23,24,25,26,27,28,29,32,30,31,33};
		        id=id.toUpperCase();
		  if(id.length() < 10){
			  System.out.println("ID長度錯誤!!");
			  return false;
		  }
		  if(id.charAt(0)<'A'||id.charAt(0)>'Z'){
		         System.out.println("ID第一個字錯誤!!");
		         return false;
		        }
		        if(id.charAt(1)!='1' && id.charAt(1)!='2'){
		         System.out.println("ID第二個字錯誤!!");
		         return false;
		        }
		        for(int i=1;i<10;i++){
		         if(id.charAt(i)<'0'||id.charAt(i)>'9'){
		          System.out.println("ID輸入錯誤!!");
		          return false;
		         }
		        }
		  for(int i=1;i<10;i++){
		   num[i]=(id.charAt(i)-'0');
		  }
		  num[0]=rdd[id.charAt(0)-'A'];
		  int sum=((int)num[0]/10+(num[0]%10)*9);
		  for(int i=0;i<8;i++){
		   sum+=num[i+1]*(8-i);
		  }
		  if(10-sum%10==num[9]){
		   return true;
		  }
		  else{
		   System.out.println("身分證號錯誤");
		   return false;
		  }
		 }
		
	
	
}
