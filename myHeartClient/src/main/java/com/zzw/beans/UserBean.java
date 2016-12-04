package com.zzw.beans;

public class UserBean {
	public int userID;
	public String userName;
	public String userPswd;
	public String userPhone;




	public UserBean( String userName, String userPswd, String userPhone) {
		super();
//		this.userID = userID;
		this.userName = userName;
		this.userPswd = userPswd;
		this.userPhone = userPhone;
	}

	public UserBean() {
		// TODO Auto-generated constructor stub
		super();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPswd() {
		return userPswd;
	}

	public void setUserPswd(String userPswd) {
		this.userPswd = userPswd;
	}

	public String getUserPhone() {
		return userPhone;
	}
	
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}
