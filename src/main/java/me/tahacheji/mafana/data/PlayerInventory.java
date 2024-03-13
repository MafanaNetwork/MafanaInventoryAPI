package me.tahacheji.mafana.data;

import java.util.List;
import java.util.UUID;

public class PlayerInventory {

    private final String user;
    private String rawInventory;
    private String rawArmor;
    private List<String> rawStash;
    private List<String> userValues;
    private String lastSeen;

    public PlayerInventory(String user, String rawInventory, String rawArmor, List<String> rawStash, List<String> userValues, String lastSeen) {
        this.user = user;
        this.rawInventory = rawInventory;
        this.rawArmor = rawArmor;
        this.rawStash = rawStash;
        this.userValues = userValues;
        this.lastSeen = lastSeen;
    }

    public UUID getUUID () {
        return UUID.fromString(user);
    }

    public void setRawInventory(String rawInventory) {
        this.rawInventory = rawInventory;
    }

    public void setRawArmor(String rawArmor) {
        this.rawArmor = rawArmor;
    }

    public void setRawStash(List<String> rawStash) {
        this.rawStash = rawStash;
    }

    public void setUserValues(List<String> userValues) {
        this.userValues = userValues;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getUser() {
        return user;
    }

    public String getRawInventory() {
        return rawInventory;
    }

    public String getRawArmor() {
        return rawArmor;
    }

    public List<String> getRawStash() {
        return rawStash;
    }

    public List<String> getUserValues() {
        return userValues;
    }

    public String getLastSeen() {
        return lastSeen;
    }
}
