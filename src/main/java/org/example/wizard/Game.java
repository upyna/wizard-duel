package org.example.wizard;

import java.util.List;
import java.util.Scanner;


public class Game {
    private final Wizard player;
    private final Wizard ai;
    private final AI aiController;
    private final Scanner scanner;
    private int turnNumber;
    private String lastPlayerSpell;     
    private static final int MAX_TURNS = 100; 
    
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
        
        while (player.isAlive() && ai.isAlive() && turnNumber <= MAX_TURNS) {
            System.out.println("\n--- ĖJIMAS #" + turnNumber + " ---");
            System.out.println();
            
            System.out.println(player.getStatusString());
            System.out.println(ai.getStatusString());
            System.out.println();
            
            player.applyStatusEffects();
            ai.applyStatusEffects();
            
            if (!player.isAlive() || !ai.isAlive()) {
                break;
            }
            
            boolean playerCanAct = !player.isSilenced() && !player.getAvailableSpells().isEmpty();
            boolean aiCanAct = !ai.isSilenced() && !ai.getAvailableSpells().isEmpty();
            boolean hasActiveStatusEffects = !player.getStatusEffects().isEmpty() || !ai.getStatusEffects().isEmpty();
            
            if (!playerCanAct && !aiCanAct && !hasActiveStatusEffects) {
                System.out.println("\nŽaidimas užstrigo - abi pusės negali nieko padaryti!");
                System.out.println("Žaidimas baigiamas pagal dabartinę būseną.");
                break;
            }
            
            if (!player.isSilenced()) {
                playerTurn();
            } else {
                System.out.println("Jūs esate nutildytas ir negalite naudoti burtų!");
            }
            
            if (!ai.isAlive()) {
                break;
            }
            
            player.restoreMana(5);
            ai.restoreMana(5);
            
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
            
            if (!player.isAlive()) {
                break;
            }
            
            turnNumber++;
        }
        
        if (turnNumber > MAX_TURNS) {
            System.out.println("\nPasiektas maksimalus ėjimų skaičius (" + MAX_TURNS + ")!");
            System.out.println("Žaidimas baigiamas pagal dabartinę būseną.");
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
        castSpell(player, ai, chosenSpell);
        lastPlayerSpell = chosenSpell.getName();
    }
    
    private void aiTurn() {
        List<Spell> availableSpells = ai.getAvailableSpells();
        if (availableSpells.isEmpty()) {
            System.out.println("AI burtininkas neturi pakankamai manos!");
            return;
        }
        
        Spell chosenSpell = aiController.chooseSpell(ai, player);
        
        if (chosenSpell == null) {
            System.out.println("AI burtininkas neturi pakankamai manos!");
            return;
        }
        
        if (!chosenSpell.canUse(ai.getMana())) {
            System.out.println("AI burtininkas neturi pakankamai manos naudoti " + chosenSpell.getName() + "!");
            return;
        }
        
        System.out.println("AI burtininkas naudoja: " + chosenSpell.getName());
        castSpell(ai, player, chosenSpell);
    }
    
    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        if (!spell.canUse(caster.getMana())) {
            System.out.println(caster.getName() + " neturi pakankamai manos!");
            return;
        }
        
        caster.useMana(spell.getManaCost());
        
        int comboMultiplier = checkCombo(caster, spell);
        
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
                StatusEffect shieldEffect = new StatusEffect(StatusEffect.StatusType.SHIELD, 3, shield);
                caster.addStatusEffect(shieldEffect); 
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
        if (caster == player && lastPlayerSpell != null && 
            lastPlayerSpell.equals(spell.getName())) {
            return 2; 
        }
        return 1;
    }
    
    private void endGame() {
        System.out.println("\n========================================");
        System.out.println("   ŽAIDIMAS BAIGTAS");
        System.out.println("========================================");
        
        boolean playerCanAct = !player.isSilenced() && !player.getAvailableSpells().isEmpty();
        boolean aiCanAct = !ai.isSilenced() && !ai.getAvailableSpells().isEmpty();
        boolean hasActiveStatusEffects = !player.getStatusEffects().isEmpty() || !ai.getStatusEffects().isEmpty();
        boolean gameStuck = player.isAlive() && ai.isAlive() && !playerCanAct && !aiCanAct && !hasActiveStatusEffects;
        
        if (gameStuck) {
            if (player.getHealth() > ai.getHealth()) {
                System.out.println("ŽAIDIMAS UŽSTRIGO - JŪS LAIMĖJOTE!");
                System.out.println("(Jūs turite daugiau gyvybių: " + player.getHealth() + " vs " + ai.getHealth() + ")");
            } else if (ai.getHealth() > player.getHealth()) {
                System.out.println("ŽAIDIMAS UŽSTRIGO - AI BURTININKAS LAIMĖJO!");
                System.out.println("(AI turi daugiau gyvybių: " + ai.getHealth() + " vs " + player.getHealth() + ")");
            } else {
                System.out.println("ŽAIDIMAS UŽSTRIGO - LYGIOSIOS!");
                System.out.println("(Abi pusės turi vienodas gyvybes: " + player.getHealth() + ")");
            }
        } else if (!player.isAlive() && !ai.isAlive()) {
            System.out.println("LYGIOSIOS! Abu burtininkai pralaimėjo!");
        } else if (!player.isAlive()) {
            System.out.println("AI BURTININKAS LAIMĖJO!");
        } else if (!ai.isAlive()) {
            System.out.println("JŪS LAIMĖJOTE!");
        } else {
            if (player.getHealth() > ai.getHealth()) {
                System.out.println("JŪS LAIMĖJOTE!");
                System.out.println("(Jūs turite daugiau gyvybių: " + player.getHealth() + " vs " + ai.getHealth() + ")");
            } else if (ai.getHealth() > player.getHealth()) {
                System.out.println("AI BURTININKAS LAIMĖJO!");
                System.out.println("(AI turi daugiau gyvybių: " + ai.getHealth() + " vs " + player.getHealth() + ")");
            } else {
                System.out.println("LYGIOSIOS!");
                System.out.println("(Abi pusės turi vienodas gyvybes: " + player.getHealth() + ")");
            }
        }
        
        System.out.println("\nGalutinė būsena:");
        System.out.println(player.getStatusString());
        System.out.println(ai.getStatusString());
    }
}

