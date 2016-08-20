package org.dan.cipher;

import java.util.Map;

public class Props {
    private String master;
    private Map<String, ProfileInfo> profiles;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public Map<String, ProfileInfo> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, ProfileInfo> profiles) {
        this.profiles = profiles;
    }
}
