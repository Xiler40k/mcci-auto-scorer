package com.xiler.mcciautoscorer.mixin;
import com.xiler.mcciautoscorer.util.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.network.message.MessageType;
import java.util.Collection;
import java.util.regex.Pattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class OnChatMixin {

    @Unique
    ScoreHandler scoreHandler = new ScoreHandler();

    @Inject(method = "addMessage", at = @At("RETURN"))
    public void onGameMessage(Text message, CallbackInfo ci) {

        String rawMessage = message.getString();
        System.out.println("[MIXIN CHAT] " + rawMessage);
        String username = MinecraftClient.getInstance().getSession().getUsername();

        if(rawMessage.contains("[\uE0F2] Battle Box")) {
            scoreHandler.setGameName("Battle Box");
            scoreHandler.setGameActive(true);
            System.out.println("BB GAME ACTIVE");
        }

        String cleanPattern = "was slain by .*?" + Pattern.quote(username);

        if(scoreHandler.getGameName().equals("Battle Box")) {
            if(rawMessage.matches(".*" + cleanPattern + ".*")) {
                scoreHandler.incrementKill();
                System.out.println("KILL");
            }
            if(rawMessage.contains(username + " was")) {
                scoreHandler.incrementDeath();
                System.out.println("DEATH");
            }
        }

        if(rawMessage.contains("[\uE0FF] Game Over!") || rawMessage.contains("[\uE0F2] Game Over!")) {
            scoreHandler.setGameActive(false);
            System.out.println("AWESOME STUFF NOW ONLY SERVER CAN END GAME. NOW TAKE SCOREBOARD STATS FOR BB??");
            scoreHandler.postScores();

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

//            Collection<PlayerListEntry> playerList = MinecraftClient.getInstance().getNetworkHandler().getPlayerList();
//
//            for (PlayerListEntry player : playerList) {
//                String name = player.getProfile().getName();
//                Text displayName = player.getDisplayName();
//                String rawText = player.toString();
//
//                String readableText = displayName != null ? displayName.getString() : "(no display name/text)";
//                String styleText = displayName != null ? displayName.getStyle().toString() : "(no display name/style)";
//                System.out.println("Name: " + name + " Style: " + styleText +  "Readable text: " + readableText);
//
//            }

        }



//        if (rawMessage.toLowerCase().contains("match score") || rawMessage.toLowerCase().contains("eliminated")) {
//            System.out.println("🎯 Important message detected: " + rawMessage);
//        }
    }

}