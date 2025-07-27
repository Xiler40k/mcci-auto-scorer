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

import java.util.ArrayList;
import java.util.Arrays;


@Mixin(ChatHud.class)
public class OnChatMixin {

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
<<<<<<< Updated upstream
=======
        GameHandlerRegistry.set(gameHandler);
        if(message == null) {
            return;
        }

        //if player has no username, get it
        if(gameHandler.getUsername() == null) {
            gameHandler.setUsername(MinecraftClient.getInstance().getSession().getUsername());
        }

        //gets raw message from chat
>>>>>>> Stashed changes
        String rawMessage = message.getString();

        System.out.println("[MIXIN CHAT] " + rawMessage);

<<<<<<< Updated upstream
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
=======
        if (rawMessage.contains("Game Started: Dojo Rush") && gameHandler.getCurrentGame() == null && verifyStaffMessage(rawMessage)) {
            int startTime; //mod60 for time?
        }


        //ABORTING
        if (rawMessage.contains("ICC Game: Aborted") && verifyStaffMessage(rawMessage)) {//should probs make games like paintball and extraction easier to queue this way
            //CHECKING USERNAME IS 2nd POSITION
            System.out.println("Aborting game");
            gameHandler.setCurrentGame(null);
            gameHandler.setGameActive(false);
            //reset relevant score/all scoring classes
            battleBoxScoreHandler.reset();
        }

        //QUEUEING
        if (gameHandler.getCurrentGame() == null) {
            //if message contains name of a game or plobby message with right structure (AND STAFF SENT THIS), start a specific game.
            if (rawMessage.contains("[\uE11A] Battle Box") || rawMessage.contains("[\uE11A] Game Started!")) {
                gameHandler.setCurrentGame("Battle Box");
                gameHandler.setGameActive(true);
                battleBoxScoreHandler.reset();
                System.out.println("BB GAME ACTIVE");
                if (battleBoxScoreHandler.cleanPatternsKills.isEmpty()) { battleBoxScoreHandler.initialisePatterns(gameHandler); }
            } else if (rawMessage.contains("Challenge run started")) { //people can't play djr in between games - ISSUE
                gameHandler.setCurrentGame("Dojo Rush");
                gameHandler.setGameActive(true);
                djrScoreHandler.reset();
                System.out.println("DJR GAME ACTIVE");
            } else if (rawMessage.contains("[\uE120] Parkour Warrior") || rawMessage.contains("[\uE120] Game Started!")) {
                gameHandler.setCurrentGame("Parkour Warrior");
                gameHandler.setGameActive(true);
                pkwScoreHandler.reset();
                System.out.println("PKW GAME ACTIVE");
                pkwScoreHandler.initialisePatterns(gameHandler);
            } else if (rawMessage.contains("[\uE12A] TGTTOS") || rawMessage.contains("[\uE12A] Game Started!")) {
                gameHandler.setCurrentGame("TGTTOS");
                gameHandler.setGameActive(true);
                tgttosScoreHandler.reset();
            } else if (rawMessage.contains("[\uE11E] Hole in the Wall") || rawMessage.contains("[\uE11E] Game Started!")) {
                gameHandler.setCurrentGame("Hole In The Wall");
                gameHandler.setGameActive(true);
                hitwScoreHandler.reset();
                System.out.println("HITW GAME ACTIVE");
            } else if (rawMessage.contains("[\uE125] Rocket Spleef Rush") || rawMessage.contains("[\uE125] Game Started!")) {
                gameHandler.setCurrentGame("Rocket Spleef Rush");
                gameHandler.setGameActive(true);
                rsrScoreHandler.reset();
                System.out.println("RSR GAME ACTIVE");
                if (rsrScoreHandler.cleanPatternsKills.isEmpty()) { rsrScoreHandler.initialisePatterns(gameHandler); }
            } else if (rawMessage.contains("[\uE127] Sky Battle") || rawMessage.contains("[\uE127] Game Started!")) {
                gameHandler.setCurrentGame("Sky Battle");
                gameHandler.setGameActive(true);
                sbScoreHandler.reset();
                sbScoreHandler.initialisePatterns(gameHandler);
                System.out.println("SKY BATTLE GAME ACTIVE");
            }
            //custom games
            if (rawMessage.contains("ICC Game: Extraction") && verifyStaffMessage(rawMessage)) { //NEED to check for list of approved usernames. DB check? Good enough for MVP
                gameHandler.setCurrentGame("Extraction");
                gameHandler.setGameActive(true);
                extrScoreHandler.reset();
                System.out.println("EXTRACTION GAME ACTIVE");
                if (extrScoreHandler.cleanPatternsKills.isEmpty()) { extrScoreHandler.initialisePatterns(gameHandler); }
            } else if (rawMessage.contains("ICC Game: Paintball") && verifyStaffMessage(rawMessage)) {
                gameHandler.setCurrentGame("Paintball");
                gameHandler.setGameActive(true);
                pbScoreHandler.reset();
                System.out.println("PAINTBALL GAME ACTIVE");
                if (pbScoreHandler.cleanPatternsKills.isEmpty()) { pbScoreHandler.initialisePatterns(gameHandler); }
>>>>>>> Stashed changes
            }

<<<<<<< Updated upstream
=======
        //SCORING
        if (gameHandler.getCurrentGame() != null) {

            //if message in game contains your username, score the message
            if(gameHandler.getCurrentGame().equals("Battle Box") && rawMessage.contains(gameHandler.getUsername())) {
                System.out.println("Sending score Message");
                battleBoxScoreHandler.scoreMessage(rawMessage, gameHandler);
            } else if(gameHandler.getCurrentGame().equals("Dojo Rush") && rawMessage.contains("\uE016\uE012 Time:") && gameHandler.isScoreAllowed()) {
                System.out.println("Sending score Message");
                djrScoreHandler.scoreMessage(rawMessage, gameHandler);
                gameHandler.setAllowScore(false);
            } else if(gameHandler.getCurrentGame().equals("Extraction") && rawMessage.contains(gameHandler.getUsername())) {
                System.out.println("Sending score Message");
                extrScoreHandler.scoreMessage(rawMessage, gameHandler);
            } else if (gameHandler.getCurrentGame().equals("Sky Battle") && (rawMessage.contains("Outlived") || rawMessage.contains(gameHandler.getUsername()))) {
                sbScoreHandler.scoreMessage(rawMessage, gameHandler);
            } else if (gameHandler.getCurrentGame().equals("Paintball") && rawMessage.contains(gameHandler.getUsername())) {
                pbScoreHandler.scoreMessage(rawMessage, gameHandler);
            } else if (gameHandler.getCurrentGame().equals("Rocket Spleef Rush") && rawMessage.contains(gameHandler.getUsername())) {
                rsrScoreHandler.scoreMessage(rawMessage, gameHandler);
            }

            //djr
>>>>>>> Stashed changes
        }

        //WHEN DOING TIMER, CHECK THAT THE USERNAME APPEARS THE 3rd IN THE MESSAGE
        //CHECK POSITION

        //NO! YOU CAN JUST CHECK THAT A USERNAME APPEARS IN THE REGEX BUT NOT OUT

        //[] for sb. When game ends, end relevant game
        //ENDING
        if(gameHandler.getCurrentGame() != null) {
            if (rawMessage.contains("[\uE11A] Game Over!")) {
                if (gameHandler.getCurrentGame().equals("Battle Box")) {
                    battleBoxScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                } else if (gameHandler.getCurrentGame().equals("Extraction")) {
                    extrScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                } else if (gameHandler.getCurrentGame().equals("Paintball")) {
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
            } else if (pkwScoreHandler.getDeathPattern() != null && gameHandler.getCurrentGame().equals("Parkour Warrior")) {
                if (rawMessage.matches(pkwScoreHandler.getDeathPattern())) {
                    System.out.println("Sending to endGame function");
                    pkwScoreHandler.endGame(rawMessage, gameHandler);

                    //pkwScoreHandler.printScoreboard();
                }
            } else if (rawMessage.contains("[\uE12A] Game Over!")) {
                if (gameHandler.getCurrentGame().equals("TGTTOS")) {
                    System.out.println("Sending to tg endGame function");
                    tgttosScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
            } else if (rawMessage.contains(gameHandler.getUsername() + ", you were eliminated in") && SystemMessageTracker.isSystem(rawMessage) && gameHandler.getCurrentGame() != null) {
                if (gameHandler.getCurrentGame().equals("Hole In The Wall")) {
                    hitwScoreHandler.scoreMessage(rawMessage, gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 100);
                } else if (gameHandler.getCurrentGame().equals("Rocket Spleef Rush")) {
                    rsrScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
            } else if (rawMessage.contains("you survived the") && SystemMessageTracker.isSystem(rawMessage) && gameHandler.getCurrentGame() != null) {
                if (gameHandler.getCurrentGame().equals("Hole In The Wall") && SystemMessageTracker.isSystem(rawMessage)) {
                    hitwScoreHandler.endGame(gameHandler, 1);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 100);
                }
                if (gameHandler.getCurrentGame().equals("Sky Battle") && SystemMessageTracker.isSystem(rawMessage)) {
                    sbScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
                if (gameHandler.getCurrentGame().equals("Parkour Warrior") && SystemMessageTracker.isSystem(rawMessage)) {
                    pkwScoreHandler.endGame("[\uE29C] \uE28E\uE049\uE002" + gameHandler.getUsername() + ", you were eliminated in 1st (Score: 213\uE296)", gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
            } else if (rawMessage.contains("[\uE127] Game Over!") && SystemMessageTracker.isSystem(rawMessage)) {
                if (gameHandler.getCurrentGame().equals("Sky Battle")) {
                    sbScoreHandler.endGame(gameHandler);
                    DelayedTaskManager.schedule(() -> {
                        gameHandler.setCurrentGame(null);
                    }, 40);
                }
            }
        }

        //else if (rawMessage.contains("")) TGTTOS


//        if (rawMessage.toLowerCase().contains("match score") || rawMessage.toLowerCase().contains("eliminated")) {
//            System.out.println("🎯 Important message detected: " + rawMessage);
//        }
    }

    public boolean verifyStaffMessage(String message) {
        ArrayList<String> staffList = new ArrayList<>(Arrays.asList("Xiler40k", "iiWawa_")); //TODO: get from DB

        //check Staffname comes before the first ':'
        for (String staff : staffList) {
            if (message.contains(staff)) {
                int index = message.indexOf(staff);
                if (index != -1 && index < message.indexOf(':')) {
                    return true;
                }
            }
        }
        return false;
    }
}