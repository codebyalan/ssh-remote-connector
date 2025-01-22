package com.ambro.remote.connecter.config;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;


import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("spring.datasource.server-url")
    private String dbServerJUrl;

    @Value("spring.datasource.server-port")
    private String dbServerPort;

    @Value("${connector.ssh.key-filename}")
    private String sshFilename;

    @Value("${connector.ssh.port}")
    private int sshPort;

    @Value("${connector.ssh.username}")
    private String sshUsername;

    @Value("${connector.ssh.server}")
    private String sshServer;

    @Bean
    public Session sshSession() throws JSchException,IOException {

        JSch jsch = new JSch();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sshFilename);
        if (inputStream == null) {
            throw new IOException("Private key file not found in resources");
        }

        File tempKeyFile = File.createTempFile("ssh-key", ".key");
        FileCopyUtils.copy(inputStream, new FileOutputStream(tempKeyFile));

        tempKeyFile.setReadable(true, true);
        tempKeyFile.setWritable(true, true);

        jsch.addIdentity(tempKeyFile.getAbsolutePath());

        Session session = jsch.getSession(sshUsername, sshServer, sshPort);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        return session;
    }

    @Bean
    public DataSource dataSource() throws JSchException, IOException {
        Session session = sshSession();
        int localPort = 3306; // Choose a free local port
        int remotePort = 3306; // MySQL Server Port
        String d360Pass = "Alfarispsh@2021";

        String pshPass = "Alfaris@271127";

        try {
            session.setPortForwardingL(localPort, "localhost", remotePort);
        } catch (JSchException e) {
            // Handle exception
        }

        String jdbcUrl = "jdbc:mysql:/"+dbServerJUrl+":" + localPort + "/IPSH"; // Replace with your database name


//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(jdbcUrl);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);

        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(jdbcUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}