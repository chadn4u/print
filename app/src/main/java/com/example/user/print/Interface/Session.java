package com.example.user.print.Interface;

public interface Session {
    boolean isLoggedIn();

    void saveToken(String token);

    String getToken();

    void saveUserId(String id);

    String getUserId();

    void invalidate();
}
