package com.fren_gor.addURLTestPlugin;

import com.fren_gor.addURLTest.AddURLTest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        AddURLTest.test(e.getPlayer());
    }
}
