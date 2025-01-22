package com.ambro.remote.connecter.controller;

import com.ambro.remote.connecter.service.DataService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping
public class SshController {

    @Autowired
    DataService dataService;

    @Autowired
    Session session;

    @GetMapping("/h")
    public String getConnected(){
        String res = "";
        try {
//            JSch jsch = new JSch();
//
//
//            // Load the private key from resources
//            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ssh-key-2021-10-20.key");
//            if (inputStream == null) {
//                throw new IOException("Private key file not found in resources");
//            }
//
//            // Create a temporary file to store the key
//            File tempKeyFile = File.createTempFile("ssh-key", ".key");
//            FileCopyUtils.copy(inputStream, new FileOutputStream(tempKeyFile));
//
//            // Set permissions to the key file (optional, but recommended)
//            tempKeyFile.setReadable(true, true); // Readable by owner only
//            tempKeyFile.setWritable(true, true); // Writable by owner only
//
//            // Add the private key to JSch
//            jsch.addIdentity(tempKeyFile.getAbsolutePath());
//
////            jsch.addIdentity("ssh-key-2021-10-20.key"); // Specify the private key file path
//            Session session = jsch.getSession("ubuntu", "10.40.2.71", 22); // Update with actual values
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.connect();



            String commandOutput = executeCommand(session, "ls -la"); // Replace with your command
            System.out.println("Command Output:\n" + commandOutput);
            res = commandOutput;
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String executeCommand(Session session, String command) throws JSchException, IOException {
        // Open an exec channel
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);

        // Get the input stream to read the output of the command
        InputStream inputStream = channelExec.getInputStream();

        // Execute the command
        channelExec.connect();

        // Read the output
        StringBuilder outputBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuffer.append(line).append("\n");
            }
        }

        // Disconnect the channel
        channelExec.disconnect();

        return outputBuffer.toString();
    }

    @GetMapping
    public List<String> get() throws JSchException, SQLException {
        return dataService.getData("SELECT * FROM IPSH.psh_all_cfg;");
    }

}
