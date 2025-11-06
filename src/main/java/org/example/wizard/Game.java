package org.example.wizard;

import java.util.List;
import java.util.Scanner;

/**
 * Pagrindinė žaidimo logika
 */
public class Game {
    private final Wizard player;
    private final Wizard ai;
    private final AI aiController;
    private final Scanner scanner;
    private int turnNumber;
    private String lastPlayerSpell; // Paskutinis žaidėjo burtas (kombo sinergijoms)
    
    public Game(String playerName) {
        this.player = new Wizard(playerName, 100, 50);
        this.ai = new Wizard("AI Burtininkas", 100, 50);
        this.aiController = new AI();
        this.scanner = new Scanner(System.in);
        this.turnNumber = 1;
        this.lastPlayerSpell = null;
        
        initializeSpells();
    }
    
    private void initializeSpells() {
        // Žaidėjo burtai
        player.addSpell(new Spell("Ugnies kamuolys", 10, Spell.SpellType.DAMAGE, 15));
        player.addSpell(new Spell("Šalčio strėlė", 8, Spell.SpellType.DAMAGE, 12));
        player.addSpell(new Spell("Žaibo smūgis", 15, Spell.SpellType.DAMAGE, 25));
        player.addSpell(new Spell("Gydymo burtas", 12, Spell.SpellType.HEAL, 20));
        player.addSpell(new Spell("Mano vanduo", 5, Spell.SpellType.MANA_DRAIN, 10));
        player.addSpell(new Spell("Nuodų dūmai", 10, Spell.SpellType.STATUS, 0, 
                new StatusEffect(StatusEffect.StatusType.POISON, 3, 5)));
        player.addSpell(new Spell("Apsaugos skydas", 8, Spell.SpellType.PROTECTION, 10));
        player.addSpell(new Spell("Nutildymas", 12, Spell.SpellType.STATUS, 0,
                new StatusEffect(StatusEffect.StatusType.SILENCE, 2, 0)));
        
        // AI burtai
        ai.addSpell(new Spell("Tamsos strėlė", 10, Spell.SpellType.DAMAGE, 15));
        ai.addSpell(new Spell("Demonų liepsna", 12, Spell.SpellType.DAMAGE, 18));
        ai.addSpell(new Spell("Mirties žvilgsnis", 18, Spell.SpellType.DAMAGE, 30));
        ai.addSpell(new Spell("Gyvybės atgavimas", 12, Spell.SpellType.HEAL, 20));
        ai.addSpell(new Spell("Mano vamzdis", 5, Spell.SpellType.MANA_DRAIN, 10));
        ai.addSpell(new Spell("Nuodų dūmai", 10, Spell.SpellType.STATUS, 0,
                new StatusEffect(StatusEffect.StatusType.POISON, 3, 5)));
        ai.addSpell(new Spell("Apsaugos skydas", 8, Spell.SpellType.PROTECTION, 10));
    }
    
    public void start() {
        System.out.println("========================================");
        System.out.println("   BURTININKO DVIKOVA");
        System.out.println("========================================");
        System.out.println();
        
        while (player.isAlive() && ai.isAlive()) {
            System.out.println("\n--- ĖJIMAS #" + turnNumber + " ---");
            System.out.println();
            
            // Rodo burtininkų būseną
            System.out.println(player.getStatusString());
            System.out.println(ai.getStatusString());
            System.out.println();
            
            // Taikome status efektus prieš ėjimą
            player.applyStatusEffects();
            ai.applyStatusEffects();
            
            if (!player.isAlive() || !ai.isAlive()) {
                break;
            }
            
            // Žaidėjo ėjimas
            if (!player.isSilenced()) {
                playerTurn();
            } else {
                System.out.println("Jūs esate nutildytas ir negalite naudoti burtų!");
            }
            
            if (!ai.isAlive()) {
                break;
            }
            
            // Manos regeneracija
            player.restoreMana(5);
            ai.restoreMana(5);
            
            // AI ėjimas
            if (!ai.isSilenced()) {
                aiTurn();
            } else {
                System.out.println("AI burtininkas nutildytas ir negali naudoti burtų!");
            }
            
            if (!player.isAlive()) {
                break;
            }
            
            turnNumber++;
        }
        
        endGame();
    }
    
    private void playerTurn() {
        List<Spell> availableSpells = player.getAvailableSpells();
        
        if (availableSpells.isEmpty()) {
            System.out.println("Jūs neturite pakankamai manos burtų naudoti!");
            return;
        }
        
        System.out.println("Jūsų galimi burtai:");
        for (int i = 0; i < availableSpells.size(); i++) {
            System.out.println((i + 1) + ". " + availableSpells.get(i));
        }
        
        System.out.print("Pasirinkite burto numerį: ");
        int choice = -1;
        
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > availableSpells.size()) {
                System.out.println("Netinkamas pasirinkimas! Naudojamas pirmas galimas burtas.");
                choice = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Netinkamas įvestis! Naudojamas pirmas galimas burtas.");
            choice = 1;
        }
        
        Spell chosenSpell = availableSpells.get(choice - 1);
        lastPlayerSpell = chosenSpell.getName();
        castSpell(player, ai, chosenSpell);
    }
    
    private void aiTurn() {
        Spell chosenSpell = aiController.chooseSpell(ai, player);
        
        if (chosenSpell == null) {
            System.out.println("AI burtininkas neturi pakankamai manos!");
            return;
        }
        
        System.out.println("AI burtininkas naudoja: " + chosenSpell.getName());
        castSpell(ai, player, chosenSpell);
    }
    
    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        // Patikriname, ar caster turi pakankamai manos
        if (!spell.canUse(caster.getMana())) {
            System.out.println(caster.getName() + " neturi pakankamai manos!");
            return;
        }
        
        // Sunaudojame maną
        caster.useMana(spell.getManaCost());
        
        // Kombo sinergijos patikrinimas
        int comboMultiplier = checkCombo(caster, spell);
        
        // Taikome burto efektą
        switch (spell.getType()) {
            case DAMAGE:
                int damage = spell.getValue() * comboMultiplier;
                target.takeDamage(damage);
                System.out.println("  " + caster.getName() + " daro " + damage + " žalos " + target.getName() + "!");
                if (comboMultiplier > 1) {
                    System.out.println("  KOMBO! Žala padidinta " + comboMultiplier + "x!");
                }
                break;
                
            case HEAL:
                int heal = spell.getValue() * comboMultiplier;
                caster.heal(heal);
                System.out.println("  " + caster.getName() + " atsistato " + heal + " gyvybių!");
                if (comboMultiplier > 1) {
                    System.out.println("  KOMBO! Gydymas padidintas " + comboMultiplier + "x!");
                }
                break;
                
            case PROTECTION:
                int shield = spell.getValue();
                // Pridedame status efektą, kad apsauga išnyktų po 3 ėjimų
                StatusEffect shieldEffect = new StatusEffect(StatusEffect.StatusType.SHIELD, 3, shield);
                caster.addStatusEffect(shieldEffect); // addStatusEffect nustato shield automatiškai
                System.out.println("  " + caster.getName() + " gauna " + shield + " apsaugos (3 ėjimai)!");
                break;
                
            case MANA_DRAIN:
                int drain = spell.getValue();
                target.useMana(drain);
                System.out.println("  " + caster.getName() + " sunaudoja " + drain + " manos " + target.getName() + "!");
                break;
                
            case STATUS:
                if (spell.getStatusEffect() != null) {
                    StatusEffect effect = new StatusEffect(
                            spell.getStatusEffect().getType(),
                            spell.getStatusEffect().getDuration(),
                            spell.getStatusEffect().getValue()
                    );
                    target.addStatusEffect(effect);
                    System.out.println("  " + target.getName() + " gauna status efektą: " + effect.getType());
                }
                break;
        }
    }
    
    private int checkCombo(Wizard caster, Spell spell) {
        // Kombo sinergijos: jei naudojamas tas pats burtas du kartus iš eilės
        if (caster == player && lastPlayerSpell != null && 
            lastPlayerSpell.equals(spell.getName())) {
            return 2; // 2x daugiau žalos/gydymo
        }
        return 1;
    }
    
    private void endGame() {
        System.out.println("\n========================================");
        System.out.println("   ŽAIDIMAS BAIGTAS");
        System.out.println("========================================");
        
        if (!player.isAlive() && !ai.isAlive()) {
            System.out.println("LYGIOSIOS! Abu burtininkai pralaimėjo!");
        } else if (!player.isAlive()) {
            System.out.println("AI BURTININKAS LAIMĖJO!");
        } else {
            System.out.println("JŪS LAIMĖJOTE!");
        }
        
        System.out.println("\nGalutinė būsena:");
        System.out.println(player.getStatusString());
        System.out.println(ai.getStatusString());
    }
}

