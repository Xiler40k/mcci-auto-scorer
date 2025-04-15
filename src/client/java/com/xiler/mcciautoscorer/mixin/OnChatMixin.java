package com.xiler.mcciautoscorer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.network.message.MessageType;
import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class OnChatMixin {

    @Inject(method = "addMessage", at = @At("RETURN"))
    public void onGameMessage(Text message, CallbackInfo ci) {
        String rawMessage = message.getString();

        System.out.println("[MIXIN CHAT] " + rawMessage);

        if(rawMessage.contains("[\uE0FF] Game Over!")) {
            System.out.println("AWESOME STUFF NOW ONLY SERVER CAN END GAME. NOW TAKE SCOREBOARD STATS FOR BB??");
            //this works!
        }


        Scoreboard scoreboard = MinecraftClient.getInstance().world.getScoreboard();
        ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(sidebar);

        if(rawMessage.contains("Game Over!")) {
            scoreboard = MinecraftClient.getInstance().world.getScoreboard();
            sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
            entries = scoreboard.getScoreboardEntries(sidebar);

            for (ScoreboardEntry entry : entries) {
                Text nameText = entry.name();
                String rawName = nameText.getString();

                int score = entry.value();

                System.out.println(rawName + score);
            }

        }



//        if (rawMessage.toLowerCase().contains("match score") || rawMessage.toLowerCase().contains("eliminated")) {
//            System.out.println("🎯 Important message detected: " + rawMessage);
//        }
    }

}