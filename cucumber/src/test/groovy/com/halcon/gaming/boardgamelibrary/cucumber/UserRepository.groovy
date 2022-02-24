package com.halcon.gaming.boardgamelibrary.cucumber;

class UserRepository {
    private Map<String,String> userIdMap = [:]
    private Map<String,String> userPasswordMap = [:]

    public void registerUser(String id, String username, String password) {
        userIdMap[username] = id
        userPasswordMap[username] = password
    }

    public String userId(username) {
        return userIdMap[username]
    }

    public String userPassword(username) {
        return userPasswordMap[username]
    }
}
