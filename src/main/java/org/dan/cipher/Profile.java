package org.dan.cipher;

public class Profile {
    private byte[] password;
    private String baseAlgorithm;
    private String algorithm;
    private String profileName;

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getBaseAlgorithm() {
        return baseAlgorithm;
    }

    public void setBaseAlgorithm(String baseAlgorithm) {
        this.baseAlgorithm = baseAlgorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
