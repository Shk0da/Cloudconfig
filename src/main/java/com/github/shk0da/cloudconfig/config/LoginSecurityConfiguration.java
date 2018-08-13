package com.github.shk0da.cloudconfig.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security", ignoreUnknownFields = false)
public class LoginSecurityConfiguration {

    private User[] users;

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public static class User {

        private String name;
        private String password;
        private String[] roles;

        public User() {
        }

        public User(String name, String password, String[] roles) {
            this.name = name;
            this.password = password;
            this.roles = roles;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
    }
}
