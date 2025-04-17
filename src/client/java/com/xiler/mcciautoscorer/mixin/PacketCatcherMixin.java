package com.xiler.mcciautoscorer.mixin;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;



@Mixin(ClientPlayNetworkHandler.class)
public class PacketCatcherMixin {
//    @Inject(method = "onGameMessage", at = @At("HEAD"))
//    public void onGameMessageInject(GameMessageS2CPacket packet, CallbackInfo ci) {
//        System.out.println("[PACKET] GameMessage: " + packet.content().getString());
//    }
//
//    @Inject(method = "onPlayerList", at = @At("HEAD"))
//    public void onPlayerListInject(PlayerListS2CPacket packet, CallbackInfo ci) {
//        System.out.println("[PACKET] PlayerList update received! : " + packet.toString());
//    }
}
