package org.example.wizard;

import java.util.List;

public class AggressiveStrategy implements AIStrategy {
    
    @Override
    public Spell chooseSpell(Character aiWizard, Character playerWizard, List<Spell> availableSpells) {
        if (availableSpells.isEmpty()) {
            return null;
        }
        
        Spell bestDamageSpell = findHighestDamageSpell(availableSpells);
        if (bestDamageSpell != null) {
            return bestDamageSpell;
        }
        
        return availableSpells.isEmpty() ? null : availableSpells.get(0);
    }
    
    private Spell findHighestDamageSpell(List<Spell> spells) {
        return spells.stream()
                .filter(s -> s.getType() == Spell.SpellType.DAMAGE)
                .max((s1, s2) -> Integer.compare(s1.getValue(), s2.getValue()))
                .orElse(null);
    }
}