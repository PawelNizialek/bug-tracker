package com.example.ztpai.security;

public enum ApplicationUserPermission {
    PROJECT_READ("user:read"),
    PROJECT_WRITE("user:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
