package org.dan.cipher.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Init");
        ObjectMapper objectMapper = new ObjectMapper();
        Dao dao = new Dao(Paths.get("."), objectMapper);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line.isEmpty()) {
                System.out.println("exit.");
                break;
            }
            String[] parts = line.trim().split("=");
            Pojo pojo;
            switch (parts.length) {
                case 1:
                    try {
                        pojo = dao.load(parts[0].trim());
                        System.out.println("pojo is ["
                                + pojo + "] but plain data ["
                                + pojo.getPlainData() + "]");
                    } catch (Exception e) {
                        System.out.println("Failed to load key " + parts[0]);
                    }
                    break;
                case 2:
                    pojo = new Pojo();
                    pojo.setId(parts[0]);
                    pojo.setPlainData(parts[1]);
                    dao.store(pojo);
                    System.out.println("Stored as " + pojo.getId()
                            + " => [" + pojo.getData() + "]");
                    break;
                default:
                    System.out.println("Error enter \"key=value\"");
            }
        }
    }
}
