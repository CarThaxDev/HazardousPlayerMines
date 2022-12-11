package com.github.carthax08.hazardousplayermines.transformers;


import com.clubobsidian.wrappy.transformer.NodeTransformer;
import org.bukkit.ChatColor;

public class ChatColorTransformer extends NodeTransformer<String> {

    public ChatColorTransformer() {
        super(String.class);
    }

    @Override
    public String transform(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}

