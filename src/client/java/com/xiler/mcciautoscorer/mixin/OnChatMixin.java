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

    @Unique
    private DjrScoreHandler djrScoreHandler = new DjrScoreHandler();

    @Unique
    private ExtrScoreHandler extrScoreHandler = new ExtrScoreHandler();

    @Unique
    private HitwScoreHandler hitwScoreHandler = new HitwScoreHandler();

    @Unique
    private PbScoreHandler pbScoreHandler = new PbScoreHandler();

    @Unique
    private PkwScoreHandler pkwScoreHandler = new PkwScoreHandler();

    @Unique
    private RsrScoreHandler rsrScoreHandler = new RsrScoreHandler();

    @Unique
    private SbScoreHandler sbScoreHandler = new SbScoreHandler();

    @Unique
    private TgttosScoreHandler tgttosScoreHandler = new TgttosScoreHandler();

    @Inject(method = "addMessage", at = @At("RETURN"))
    public void onGameMessage(Text message, CallbackInfo ci) {

        //if player has no username, get it
        if(gameHandler.getUsername() == null) {
            gameHandler.setUsername(MinecraftClient.getInstance().getSession().getUsername());
        }

        //gets raw message from chat
        String rawMessage = message.getString();
        System.out.println("[MIXIN CHAT] " + rawMessage);

        if (rawMessage.contains("Game Started: Dojo Rush") && gameHandler.getCurrentGame() == null && rawMessage.contains("Xiler40k")) {
            int startTime; //mod60 for time?
        }

        if (rawMessage.contains("Game aborted") && rawMessage.contains("Xiler40k")) { //should probs make games like paintball and extraction easier to queue this way
            gameHandler.setCurrentGame(null);
            gameHandler.setGameActive(false);
            //reset relevant score/all scoring classes
            battleBoxScoreHandler.reset();
        } else if (rawMessage.contains("Game queued: Extraction") && gameHandler.getCurrentGame().equals("Battle Box")) {
            gameHandler.setCurrentGame("Extraction");
            gameHandler.setGameActive(true);
            extrScoreHandler.reset();
            System.out.println("EXTRACTION GAME ACTIVE");
            extrScoreHandler.initialisePatterns(gameHandler);
        }

        if (gameHandler.getCurrentGame() == null) {
            //if message contains name of a game or plobby message with right structure (AND STAFF SENT THIS), start a specific game.
            if (rawMessage.contains("[\uE0F2] Battle Box")) {
                gameHandler.setCurrentGame("Battle Box");
                gameHandler.setGameActive(true);
                battleBoxScoreHandler.reset();
                System.out.println("BB GAME ACTIVE");
                battleBoxScoreHandler.initialisePatterns(gameHandler);
            } else if (rawMessage.contains("Challenge run started")) {
                gameHandler.setCurrentGame("Dojo Rush");
                gameHandler.setGameActive(true);
                djrScoreHandler.reset();
                System.out.println("DJR GAME ACTIVE");
            }
            //custom games
            if (rawMessage.contains("Game queued: Extraction") && rawMessage.contains("Xiler40k")) {
                gameHandler.setCurrentGame("Extraction");
                gameHandler.setGameActive(true);
                extrScoreHandler.reset();
                System.out.println("EXTRACTION GAME ACTIVE");
                extrScoreHandler.initialisePatterns(gameHandler);
            }
        }

        //if no game, skip.
        if (gameHandler.getCurrentGame() != null) {

            //if message in game contains your username, score the message
            if(gameHandler.getCurrentGame().equals("Battle Box") && rawMessage.contains(gameHandler.getUsername())) {
                System.out.println("Sending score Message");
                battleBoxScoreHandler.scoreMessage(rawMessage, gameHandler);
            } else if(gameHandler.getCurrentGame().equals("Dojo Rush") && rawMessage.contains("\uE016\uE012 Time:") && gameHandler.isScoreAllowed()) {
                System.out.println("Sending score Message");
                djrScoreHandler.scoreMessage(rawMessage, gameHandler);
                gameHandler.setAllowScore(false);
            } else if(gameHandler.getCurrentGame().equals("Dojo Rush") && rawMessage.contains("Run complete!")) {
                gameHandler.setAllowScore(true);
            } else if(gameHandler.getCurrentGame().equals("Extraction") && rawMessage.contains(gameHandler.getUsername())) {
                System.out.println("Sending score Message");
                extrScoreHandler.scoreMessage(rawMessage, gameHandler);
            }
        }

        //WHEN DOING TIMER, CHECK THAT THE USERNAME APPEARS THE 3rd IN THE MESSAGE
        //CHECK POSITION

        //NO! YOU CAN JUST CHECK THAT A USERNAME APPEARS IN THE REGEX BUT NOT OUT

        //[] for sb. When game ends, end relevant game
        if(rawMessage.contains("[\uE0F2] Game Over!")) {
            if (gameHandler.getCurrentGame().equals("Battle Box")) {
                battleBoxScoreHandler.endGame(gameHandler);
                DelayedTaskManager.schedule(() -> { battleBoxScoreHandler.reset(); } , 40);
            } else if (gameHandler.getCurrentGame().equals("Extraction")) {
                extrScoreHandler.endGame(gameHandler);
                DelayedTaskManager.schedule(() -> { extrScoreHandler.reset(); } , 40);
            } else if (gameHandler.getCurrentGame().equals("Paintball")) {

            }
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