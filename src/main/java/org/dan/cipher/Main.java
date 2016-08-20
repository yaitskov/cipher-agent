package org.dan.cipher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void icon() {
        logger.info("Init argument cipher\n"
                 + "       \\`\\ /`/\n"
                 + "        \\ V /  \n"
                 + "        /. .\\  \n"
                 + "       =\\ T /= \n"
                 + "        / ^ \\  \n"
                 + "     {}/\\\\ //\\\n"
                 + "     __\\ \" \" /__\n"
                 + "    (____/^\\____)");
    }

    public static void premain(String propertyFile, Instrumentation inst) {
        icon();
        if (propertyFile == null || propertyFile.isEmpty()) {
            throw new RuntimeException("Pass java agent path to property file");
        }
        final Props props = loadProps(Paths.get(propertyFile));
        Util.setProps(props.getMaster(), convert(props.getProfiles()));
        inst.addTransformer((classLoader, className, aClass, protectionDomain, bytes) -> {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, 0);
            CipherClassVisitor cipherClassVisitor = new CipherClassVisitor(Opcodes.ASM4,
                    writer,
                    Util.class,
                    adaptClassName(Decrypt.class),
                    adaptClassName(Encrypt.class));
            reader.accept(cipherClassVisitor, 0);
            byte[] result = writer.toByteArray();
            if (cipherClassVisitor.isAnnotated()) {
                logger.info("Class [{}] was mangled", className);
            }
            return result;
        });
    }

    private static String adaptClassName(Class c) {
        return "L" + c.getName().replace('.', '/') + ";";
    }

    private static Props loadProps(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException("File [" + path + "] doesn't exist");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return validateProps(objectMapper.readValue(path.toFile(), Props.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load/parse property file ["
                    + path + "]", e);
        }
    }

    private static Props validateProps(Props props) {
        if (!props.getProfiles().containsKey(props.getMaster())) {
            throw new RuntimeException("Master profile ["
                    + props.getMaster() + "] is not defined");
        }
        return props;
    }

    private static Map<String, Profile> convert(Map<String, ProfileInfo> profiles) {
        Map<String, Profile> result = new HashMap<>();
        profiles.forEach((profileName, info) -> {
            Profile profile = new Profile();
            if (profileName.contains(":")) {
                throw new RuntimeException(
                        "Bad profile name [" + profileName
                                + "]. ':' is not allowed");
            }
            if (info.getPassword() == null) {
                throw new RuntimeException("Profile [" + profileName
                        + "] doesn't have 'password' field");
            }
            final byte[] passwordBytes = info.getPassword().getBytes();
            profile.setPassword(passwordBytes);
            if (info.getAlgorithm() == null) {
                throw new RuntimeException("Profile [" + profileName
                        + "] doesn't have 'algorithm' field");
            }
            profile.setAlgorithm(info.getAlgorithm());
            String[] parts = info.getAlgorithm().split("/");
            if (parts.length != 3) {
                throw new RuntimeException("Bad algorithm name ["
                        + info.getAlgorithm()
                        + "]. Should be like AAA/BBB/CCC");
            }
            profile.setBaseAlgorithm(parts[0]);
            try {
                final Cipher instance = Cipher.getInstance(profile.getAlgorithm());
                int blockSize = instance.getBlockSize();
                if (blockSize > 0 && passwordBytes.length != blockSize) {
                    throw new RuntimeException("Wrong password length. Must be "
                            + blockSize + " bytes");
                }
            } catch (NoSuchPaddingException|NoSuchAlgorithmException e) {
                throw new RuntimeException("Algorithm [" + profile.getAlgorithm()
                        + "] is not available", e);
            }
            result.put(profileName, profile);
            logger.info("Profile {} is loaded", profileName);
        });
        return result;
    }
}
