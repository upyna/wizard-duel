package org.example.wizard;

import java.util.List;
import java.util.Random;

public class AI {
    private final Random random;
    
    public AI() {
        this.random = new Random();
    }
    
    public Spell chooseSpell(Wizard aiWizard, Wizard playerWizard) {
        List<Spell> availableSpells = aiWizard.getAvailableSpells();
        
        if (availableSpells.isEmpty()) {
            return null; 
        }
        
        double healthRatio = (double) aiWizard.getHealth() / aiWizard.getMaxHealth();
        double playerHealthRatio = (double) playerWizard.getHealth() / playerWizard.getMaxHealth();
        double manaRatio = (double) aiWizard.getMana() / aiWizard.getMaxMana();
        
        if (healthRatio < 0.3) {
            Spell healSpell = findSpellByType(availableSpells, Spell.SpellType.HEAL);
            if (healSpell != null) {
                return healSpell;
            }
        }
        
        if (manaRatio < 0.3 && playerWizard.getMana() > 5) {
            Spell manaDrainSpell = findSpellByType(availableSpells, Spell.SpellType.MANA_DRAIN);
            if (manaDrainSpell != null) {
                return manaDrainSpell;
            }
        }
        
        if (playerHealthRatio < 0.3) {
            Spell highDamageSpell = findHighestDamageSpell(availableSpells);
            if (highDamageSpell != null) {
                return highDamageSpell;
            }
        }
        
        if (!playerWizard.getStatusEffects().stream()
                .anyMatch(e -> e.getType() == StatusEffect.StatusType.POISON)) {
            Spell poisonSpell = findSpellWithStatus(availableSpells, StatusEffect.StatusType.POISON);
            if (poisonSpell != null && random.nextDouble() < 0.4) {
                return poisonSpell;
            }
        }
        
        Spell damageSpell = findHighestDamageSpell(availableSpells);
        if (damageSpell != null && random.nextDouble() < 0.7) {
            return damageSpell;
        }
        
        return availableSpells.get(random.nextInt(availableSpells.size()));
    }
    
    private Spell findSpellByType(List<Spell> spells, Spell.SpellType type) {
        return spells.stream()
                .filter(s -> s.getType() == type)
                .findFirst()
                .orElse(null);
    }
    
    private Spell findHighestDamageSpell(List<Spell> spells) {
        return spells.stream()
                .filter(s -> s.getType() == Spell.SpellType.DAMAGE)
                .max((s1, s2) -> Integer.compare(s1.getValue(), s2.getValue()))
                .orElse(null);
    }
    
    private Spell findSpellWithStatus(List<Spell> spells, StatusEffect.StatusType statusType) {
        return spells.stream()
                .filter(s -> s.getStatusEffect() != null && 
                            s.getStatusEffect().getType() == statusType)
                .findFirst()
                .orElse(null);
    }
}

