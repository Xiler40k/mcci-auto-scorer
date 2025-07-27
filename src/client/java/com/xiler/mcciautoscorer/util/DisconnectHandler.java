package com.xiler.mcciautoscorer.util;

import com.xiler.mcciautoscorer.mixin.OnChatMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class DisconnectHandler {

    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            System.out.println("[DISCONNECT] Resetting GameHandler.");
            GameHandlerRegistry.reset();
        });
    }
}