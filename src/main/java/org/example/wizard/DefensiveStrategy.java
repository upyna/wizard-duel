package org.example.wizard;

import java.util.List;

public class DefensiveStrategy implements AIStrategy {
    
    @Override
    public Spell chooseSpell(Character aiWizard, Character playerWizard, List<Spell> availableSpells) {
        if (availableSpells.isEmpty()) {
            return null;
        }
        
        double healthRatio = (double) aiWizard.getHealth() / aiWizard.getMaxHealth();

        if (healthRatio < 0.3) {
            Spell healSpell = findSpellByType(availableSpells, Spell.SpellType.HEAL);
            if (healSpell != null) {
                return healSpell;
            }
        }

        if (aiWizard.getShield() == 0) {
            Spell shieldSpell = findSpellByType(availableSpells, Spell.SpellType.PROTECTION);
            if (shieldSpell != null) {
                return shieldSpell;
            }
        }

        return findHighestDamageSpell(availableSpells);
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
}