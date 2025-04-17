package com.xiler.mcciautoscorer;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class ClientMain implements ClientModInitializer {

    private final Set<String> seenMessages = new HashSet<>();
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        System.out.println("Client mod is running!");
        String username = MinecraftClient.getInstance().getSession().getUsername();
        System.out.println("Logged in as " + username + "!!");

//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if(client.player == null || client.world == null) return;
//
//            tickCounter++;
//            if(tickCounter % 20 != 0) return; else tickCounter = 0;
//            checkChatMessages(client);
//        });


    }

    private void checkChatMessages(MinecraftClient client) {
        List<String> lines = client.inGameHud.getChatHud().getMessageHistory();
        for (String line : lines) {

            if(seenMessages.contains(line)) continue;
            seenMessages.add(line);

            System.out.println("[Chat] " + line);
            if(seenMessages.size() > 100) seenMessages.clear();
        }
    }
}
