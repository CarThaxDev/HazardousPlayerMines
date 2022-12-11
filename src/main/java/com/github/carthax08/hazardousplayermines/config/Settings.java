package com.github.carthax08.hazardousplayermines.config;

import com.clubobsidian.wrappy.inject.Node;

public class Settings {

    @Node("prefix")
    private String prefix;

    public String getPrefix() {
        return prefix;
    }


}
