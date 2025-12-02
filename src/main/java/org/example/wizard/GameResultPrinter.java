package org.example.wizard;

public class GameResultPrinter {
    
    public static void printResult(Character player, Character ai) {
        System.out.println("\n========================================");
        System.out.println("   ŽAIDIMAS BAIGTAS");
        System.out.println("========================================");
        
        boolean gameStuck = isGameStuck(player, ai);
        
        if (gameStuck) {
            printResultByHealth(player, ai, "ŽAIDIMAS PASIBAIGĖ - ");
        } else if (!player.isAlive() && !ai.isAlive()) {
            System.out.println("LYGIOSIOS! Abu burtininkai pralaimėjo!");
        } else if (!player.isAlive()) {
            System.out.println("AI BURTININKAS LAIMĖJO!");
        } else if (!ai.isAlive()) {
            System.out.println("JŪS LAIMĖJOTE!");
        } else {
            printResultByHealth(player, ai, "");
        }
        
        printFinalStatus(player, ai);
    }
    
    private static boolean isGameStuck(Character player, Character ai) {
        boolean playerCanAct = !player.isSilenced() && !player.getAvailableSpells().isEmpty();
        boolean aiCanAct = !ai.isSilenced() && !ai.getAvailableSpells().isEmpty();
        boolean hasActiveStatusEffects = !player.getStatusEffects().isEmpty() || !ai.getStatusEffects().isEmpty();
        return player.isAlive() && ai.isAlive() && !playerCanAct && !aiCanAct && !hasActiveStatusEffects;
    }
    
    private static void printResultByHealth(Character player, Character ai, String prefix) {
        int playerHealth = player.getHealth();
        int aiHealth = ai.getHealth();
        
        if (playerHealth > aiHealth) {
            System.out.println(prefix + "JŪS LAIMĖJOTE!");
            System.out.println("(Jūs turite daugiau gyvybių: " + playerHealth + " vs " + aiHealth + ")");
        } else if (aiHealth > playerHealth) {
            System.out.println(prefix + "AI BURTININKAS LAIMĖJO!");
            System.out.println("(AI turi daugiau gyvybių: " + aiHealth + " vs " + playerHealth + ")");
        } else {
            System.out.println(prefix + "LYGIOSIOS!");
            System.out.println("(Abi pusės turi vienodas gyvybes: " + playerHealth + ")");
        }
    }
    
    private static void printFinalStatus(Character player, Character ai) {
        System.out.println("\nGalutinė būsena:");
        System.out.println(player.getStatusString());
        System.out.println(ai.getStatusString());
    }
}