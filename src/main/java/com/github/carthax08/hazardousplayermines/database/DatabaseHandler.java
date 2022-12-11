package com.github.carthax08.hazardousplayermines.database;

import com.github.carthax08.hazardousplayermines.config.Database;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    public HikariDataSource dataSource;

    public DatabaseHandler(Database database){
        HikariConfig config = new HikariConfig();
        config.setUsername(database.getUsername());
        config.setPassword(database.getPassword());
        config.setJdbcUrl(String.format("jdbc:%s://%s:%d/%s", database.getType(), database.getUrl(), database.getPort(), database.getDatabase()));
        config.setConnectionTestQuery("SELECT 1;");

        dataSource = new HikariDataSource(config);
    }

    public boolean execute(String statement){
        try(Connection connection = dataSource.getConnection()){

            PreparedStatement pstatement = connection.prepareStatement(statement);
            pstatement.execute();
            return true;

        } catch (SQLException ignored) {
            return false;
        }
    }

    public ResultSet query(String statement){
        try(Connection connection = dataSource.getConnection()){

            PreparedStatement pstatement = connection.prepareStatement(statement);
            return pstatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect(){
        dataSource.close();
    }
}
