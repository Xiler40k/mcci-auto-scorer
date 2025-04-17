package com.xiler.mcciautoscorer.mixin;
import com.xiler.mcciautoscorer.util.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class OnChatMixin {

    @Unique
    private final static GameHandler gameHandler = new GameHandler();

    @Unique
    private BbScoreHandler battleBoxScoreHandler = new BbScoreHandler();

    @Inject(method = "addMessage", at = @At("RETURN"))
    public void onGameMessage(Text message, CallbackInfo ci) {

        if(gameHandler.getUsername() == null) {
            gameHandler.setUsername(MinecraftClient.getInstance().getSession().getUsername());
        }

        String rawMessage = message.getString();
        System.out.println("[MIXIN CHAT] " + rawMessage);
        //String username = MinecraftClient.getInstance().getSession().getUsername();

        if(rawMessage.contains("[\uE0F2] Battle Box")) {
            gameHandler.setCurrentGame("Battle Box");
            gameHandler.setGameActive(true);
            battleBoxScoreHandler.reset();
            System.out.println("BB GAME ACTIVE");
        }

        //String cleanPattern = "was slain by .*?" + Pattern.quote(username);

        if (gameHandler.getCurrentGame() != null) {
            if(gameHandler.getCurrentGame().equals("Battle Box") && rawMessage.contains(gameHandler.getUsername())) {
                System.out.println("Sending score Message");
                battleBoxScoreHandler.scoreMessage(rawMessage, gameHandler);
            }
        }

        //[] for sb.
        if(rawMessage.contains("[\uE0F2] Game Over!")) {
            battleBoxScoreHandler.endGame(gameHandler);
            //battleBoxScoreHandler.reset();
        }


//        Scoreboard scoreboard = MinecraftClient.getInstance().world.getScoreboard();
//        ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
//        Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(sidebar);

//        if(rawMessage.contains("Game Over!")) {
//            scoreboard = MinecraftClient.getInstance().world.getScoreboard();
//            sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
//            entries = scoreboard.getScoreboardEntries(sidebar);
//
//            for (ScoreboardEntry entry : entries) {
//                Text nameText = entry.name();
//                String rawName = nameText.getString();
//
//                int score = entry.value();
//
//                System.out.println("[Line] : " + rawName + score);
//            }

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

//        if (rawMessage.toLowerCase().contains("match score") || rawMessage.toLowerCase().contains("eliminated")) {
//            System.out.println("🎯 Important message detected: " + rawMessage);
//        }
    }

}