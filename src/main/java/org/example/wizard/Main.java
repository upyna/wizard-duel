package org.example.wizard;

import java.util.Scanner;

/**
 * Pagrindinė klasė, kuri paleidžia žaidimą
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   BURTININKO DVIKOVA");
        System.out.println("========================================");
        System.out.println();
        System.out.print("Įveskite savo burtininko vardą: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            playerName = "Žaidėjas";
        }
        
        Game game = new Game(playerName);
        game.start();
        
        scanner.close();
    }
}

