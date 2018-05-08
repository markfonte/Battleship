package com.example.mfonte.battleship;

public class LoginResponseData {
    private int userId;
    private boolean isSuccess;
    private String userName;
    public LoginResponseData(int userId_in, boolean isSuccess_in, String userName_in) {
        userId = userId_in;
        isSuccess =  isSuccess_in;
        userName = userName_in;
    }
    public void setUserId(int userId_in) {
        userId = userId_in;
    }
    public void setSuccess(boolean isSuccess_in) {
        isSuccess = isSuccess_in;
    }
    public void setUserName(String userName_in) {
        userName = userName_in;
    }

    public int getUserId() {
        return userId;
    }
    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getUserName() {
        return userName;
    }
}
