package org.example.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Polimorfizmas: Game klasėje naudojami Combatant objektai per bendrą tipą.
 * Visi veikėjai gali būti atnaujinami per update() metodą, nepaisant jų konkretaus tipo.
 */
public class Game {
    private final Wizard player;
    private final Character ai;
    private final AI aiController;
    private final Scanner scanner;
    private int turnNumber;
    private String lastPlayerSpell;
    
    // Polimorfizmas: sąrašas, kuriame laikomi skirtingų tipų objektai per bendrą Combatant tipą
    private final List<Combatant> combatants;
    
    // Konstantos vietoj magic numbers
    private static final int MAX_TURNS = 100;
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_MANA = 50;
    private static final int MANA_RESTORE_PER_TURN = 5;
    private static final int COMBO_MULTIPLIER = 2;
    private static final int DEFAULT_CHOICE = 1; 
    
    public Game(String playerName) {
        this.player = new Wizard(playerName, DEFAULT_HEALTH, DEFAULT_MANA);
        // Polimorfizmas: AI yra AIWizard tipo, kuris skirtingai realizuoja attack() metodą
        this.ai = new AIWizard("AI Burtininkas", DEFAULT_HEALTH, DEFAULT_MANA);
        this.aiController = new AI();
        this.scanner = new Scanner(System.in);
        this.turnNumber = 1;
        this.lastPlayerSpell = null;
        
        // Polimorfizmas: įtraukiame visus kovotojus į sąrašą per bendrą Combatant tipą
        // Visi objektai gali būti naudojami per bendrą tipą, bet elgiasi taip pat kaip prieš refaktoringą
        this.combatants = new ArrayList<>();
        this.combatants.add(player);
        this.combatants.add(ai);
        
        initializeSpells();
    }
    
    private void initializeSpells() {
        SpellInitializer.initializePlayerSpells(player);
        SpellInitializer.initializeAISpells(ai);
    }
    
    public void start() {
        GameTurnHandler.printGameHeader();
        playGameLoop();
        GameTurnHandler.checkMaxTurns(turnNumber, MAX_TURNS);
        endGame();
    }
    
    private void playGameLoop() {
        while (GameTurnHandler.isGameRunning(player, ai, turnNumber, MAX_TURNS)) {
            executeTurn();
            if (shouldEndGame()) {
                break;
            }
            turnNumber++;
        }
    }
    
    private void executeTurn() {
        GameTurnHandler.printTurnHeader(turnNumber);
        GameTurnHandler.printStatus(player, ai);
        GameTurnHandler.updateCombatants(combatants);
        
        if (!player.isAlive() || !ai.isAlive()) {
            return;
        }
        
        if (isGameStuck()) {
            System.out.println("\nŽaidimas užstrigo - abi pusės negali nieko padaryti!");
            System.out.println("Žaidimas baigiamas pagal dabartinę būseną.");
            return;
        }
        
        executePlayerTurn();
        
        if (!ai.isAlive()) {
            return;
        }
        
        GameTurnHandler.restoreMana(player, ai, MANA_RESTORE_PER_TURN);
        executeAITurn();
    }
    
    private boolean shouldEndGame() {
        return !player.isAlive() || !ai.isAlive() || isGameStuck();
    }
    
    private void executePlayerTurn() {
        if (!player.isSilenced()) {
            playerTurn();
        } else {
            System.out.println("Jūs esate nutildytas ir negalite naudoti burtų!");
        }
    }
    
    private void executeAITurn() {
        if (!ai.isSilenced()) {
            List<Spell> aiAvailableSpells = ai.getAvailableSpells();
            if (aiAvailableSpells.isEmpty()) {
                System.out.println("AI burtininkas neturi pakankamai manos burtų naudoti!");
            } else {
                aiTurn();
            }
        } else {
            System.out.println("AI burtininkas nutildytas ir negali naudoti burtų!");
        }
    }
    
    private void playerTurn() {
        List<Spell> availableSpells = player.getAvailableSpells();
        
        if (availableSpells.isEmpty()) {
            System.out.println("Jūs neturite pakankamai manos burtų naudoti!");
            return;
        }
        
        printAvailableSpells(availableSpells);
        Spell chosenSpell = getPlayerSpellChoice(availableSpells);
        castPlayerSpell(chosenSpell);
    }
    
    private void printAvailableSpells(List<Spell> availableSpells) {
        System.out.println("Jūsų galimi burtai:");
        for (int i = 0; i < availableSpells.size(); i++) {
            System.out.println((i + 1) + ". " + availableSpells.get(i));
        }
    }
    
    private Spell getPlayerSpellChoice(List<Spell> availableSpells) {
        System.out.print("Pasirinkite burto numerį: ");
        int choice = -1;
        
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > availableSpells.size()) {
                System.out.println("Netinkamas pasirinkimas! Naudojamas pirmas galimas burtas.");
                choice = DEFAULT_CHOICE;
            }
        } catch (NumberFormatException e) {
            System.out.println("Netinkamas įvestis! Naudojamas pirmas galimas burtas.");
            choice = DEFAULT_CHOICE;
        }
        
        return availableSpells.get(choice - 1);
    }
    
    private void castPlayerSpell(Spell chosenSpell) {
        int comboMultiplier = SpellCaster.checkCombo(player, player, lastPlayerSpell, chosenSpell, 
                                                      COMBO_MULTIPLIER, DEFAULT_CHOICE);
        SpellCaster.castSpell(player, ai, chosenSpell, comboMultiplier);
        lastPlayerSpell = chosenSpell.getName(); // Kombo sistemai
    }
    
    private void aiTurn() {
        List<Spell> availableSpells = ai.getAvailableSpells();
        if (availableSpells.isEmpty()) {
            System.out.println("AI burtininkas neturi pakankamai manos!");
            return;
        }
        
        Spell chosenSpell = aiController.chooseSpell(ai, player); // AI renkas burta
        
        if (chosenSpell == null) {
            System.out.println("AI burtininkas neturi pakankamai manos!");
            return;
        }
        
        if (!chosenSpell.canUse(ai.getMana())) {
            System.out.println("AI burtininkas neturi pakankamai manos naudoti " + chosenSpell.getName() + "!");
            return;
        }
        
        System.out.println("AI burtininkas naudoja: " + chosenSpell.getName());
        int comboMultiplier = SpellCaster.checkCombo(ai, player, lastPlayerSpell, chosenSpell, 
                                                      COMBO_MULTIPLIER, DEFAULT_CHOICE);
        SpellCaster.castSpell(ai, player, chosenSpell, comboMultiplier);
    }
    
    
    private boolean isGameStuck() {
        boolean playerCanAct = !player.isSilenced() && !player.getAvailableSpells().isEmpty();
        boolean aiCanAct = !ai.isSilenced() && !ai.getAvailableSpells().isEmpty();
        boolean hasActiveStatusEffects = !player.getStatusEffects().isEmpty() || !ai.getStatusEffects().isEmpty();
        return player.isAlive() && ai.isAlive() && !playerCanAct && !aiCanAct && !hasActiveStatusEffects;
    }
    
    private void endGame() {
        GameResultPrinter.printResult(player, ai);
    }
}