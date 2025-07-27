package com.xiler.mcciautoscorer.mixin;

import com.xiler.mcciautoscorer.util.SystemMessageTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class OnGameMessageMixin {

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    public void interceptServerChat(GameMessageS2CPacket packet, CallbackInfo ci) {

        Text message = packet.content();
        SystemMessageTracker.add(message.getString());

    }
}