package org.dan.cipher;

import java.util.Map;

public class Util {
    private static String name;
    private static Profile master;
    private static Map<String, Profile> profiles;

    public static String encrypt(String plain) {
        if (plain == null) {
            return null;
        }
        try {
            return name + CipherUtil.encrypt(master, plain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cake) {
        if (cake == null) {
            return null;
        }
        try {
            int colon = cake.indexOf(":");
            if (colon < 0) {
                throw new IllegalArgumentException("Value [" +
                        cake + "] is not encrypted");
            }
            String usedProfileName = cake.substring(0, colon);
            Profile usedProfile = profiles.get(usedProfileName);
            if (usedProfile == null) {
                throw new IllegalArgumentException("Profile [" + usedProfileName
                        + "] is not defined");
            }
            return CipherUtil.decrypt(usedProfile, cake.substring(colon + 1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setProps(String masterName, Map<String, Profile> profiles) {
        name = masterName + ":";
        master = profiles.get(masterName);
        Util.profiles = profiles;
    }
}
