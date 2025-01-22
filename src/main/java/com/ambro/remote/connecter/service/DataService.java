package com.ambro.remote.connecter.service;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private DataSource dataSource;

    public List<String> getData(String query) throws SQLException, JSchException {
        List<String> results = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Assuming the first column is of type String
                String result = resultSet.getString(1);
                results.add(result);
            }
        }

        return results;
    }
}