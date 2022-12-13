package com.github.carthax08.hazardousplayermines.config;

import com.clubobsidian.wrappy.inject.Node;

public class Settings {

    @Node("prefix")
    private String prefix;

    @Node("commandAliases")
    private String[] aliases;

    @Node("notificationDelay")
    private double notificationDelay;

    public String getPrefix() {
        return prefix;
    }
    
    public String[] getCustomAliases() {
        return aliases;
    }
    
    public double getNotificationDelay(){
        return notificationDelay;
    }
}
