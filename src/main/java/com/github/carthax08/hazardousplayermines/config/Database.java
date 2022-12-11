package com.github.carthax08.hazardousplayermines.config;

import com.clubobsidian.wrappy.inject.Node;

public class Database {
    @Node("type")
    private String type;
    @Node("url")
    private String url;
    @Node("port")
    private int port;
    @Node("username")
    private String username;
    @Node("password")
    private String password;
    @Node("database")
    private String database;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
