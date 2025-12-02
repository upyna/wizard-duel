package org.example.wizard;

import java.util.List;

public class GameTurnHandler {
    
    public static void printGameHeader() {
        System.out.println("========================================");
        System.out.println("   BURTININKO DVIKOVA");
        System.out.println("========================================");
        System.out.println();
    }
    
    public static boolean isGameRunning(Character player, Character ai, int turnNumber, int maxTurns) {
        return player.isAlive() && ai.isAlive() && turnNumber <= maxTurns;
    }
    
    public static void printTurnHeader(int turnNumber) {
        System.out.println("\n--- ĖJIMAS #" + turnNumber + " ---");
        System.out.println();
    }
    
    public static void printStatus(Character player, Character ai) {
        System.out.println(player.getStatusString());
        System.out.println(ai.getStatusString());
        System.out.println();
    }
    
    public static void updateCombatants(List<Combatant> combatants) {
        for (Combatant combatant : combatants) {
            combatant.update();
        }
    }
    
    public static void restoreMana(Character player, Character ai, int manaRestore) {
        player.restoreMana(manaRestore);
        ai.restoreMana(manaRestore);
    }
    
    public static void checkMaxTurns(int turnNumber, int maxTurns) {
        if (turnNumber > maxTurns) {
            System.out.println("\nPasiektas maksimalus ėjimų skaičius (" + maxTurns + ")!");
            System.out.println("Žaidimas baigiamas pagal dabartinę būseną.");
        }
    }
}