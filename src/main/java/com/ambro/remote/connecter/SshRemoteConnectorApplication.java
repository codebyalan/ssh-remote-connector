package com.ambro.remote.connecter;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SshRemoteConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SshRemoteConnectorApplication.class, args);
	}

}
