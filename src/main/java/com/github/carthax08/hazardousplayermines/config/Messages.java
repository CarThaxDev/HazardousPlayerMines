package com.github.carthax08.hazardousplayermines.config;

import com.clubobsidian.wrappy.inject.Node;

public class Messages {

    @Node("mine-inactive")
    private String mineInactiveMessage;

    @Node("mine-reset")
    private String mineResetMessage;

    public String getMineInactiveMessage() {
        return mineInactiveMessage;
    }

    public String getMineResetMessage(){
        return mineResetMessage;
    }

}
