package com.xiler.mcciautoscorer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;

public class McciAutoScorer implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("MCCI Auto Scorer mod has loaded!");
        // You can initialize your networking or hooks here later

    }


}
