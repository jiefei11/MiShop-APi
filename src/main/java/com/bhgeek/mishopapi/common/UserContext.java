package com.bhgeek.mishopapi.common;

import com.bhgeek.mishopapi.entity.User;

public class UserContext {
    private static final ThreadLocal<User> user = new ThreadLocal<>();
  
    private UserContext() {
    }
  
    public static Integer getUserId() {
        User user1 = getUser();
        return user1.getId();
    }
  
    public static User getUser() {
        User baseUser = user.get();
        return baseUser;
    }
  
  
  
    public static void setBaseUser(User baseUser) {
        user.set(baseUser);
    }
  
    public static void remove() {
        user.remove();
    }
  
}