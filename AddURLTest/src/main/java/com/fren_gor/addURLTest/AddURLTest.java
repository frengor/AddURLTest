package com.fren_gor.addURLTest;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AddURLTest {
    public static void test(Player p) {
        CraftWorld w = (CraftWorld) Bukkit.getWorld("world");
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(new Zombie(w.getHandle()));
        ((CraftPlayer) p).getHandle().connection.send(packet);
    }
}
