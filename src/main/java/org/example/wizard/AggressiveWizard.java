package org.example.wizard;

import java.util.List;

/**
 * Polimorfizmas: AggressiveWizard realizuoja Combatant interfeisą,
 * bet attack() metodą realizuoja kitaip nei kiti priešų tipai.
 * Šis priešas fokusuojasi į didžiausią žalą.
 */
public class AggressiveWizard extends Character {
    
    public AggressiveWizard(String name, int health, int mana) {
        super(name, health, mana);
    }
    
    /**
     * Polimorfizmas: Realizuoja attack() metodą agresyviai -
     * visada renkasi didžiausią žalą darantį burtą.
     */
    @Override
    public int attack(Combatant target) {
        if (!(target instanceof Character)) {
            return 0;
        }
        
        Character characterTarget = (Character) target;
        List<Spell> availableSpells = getAvailableSpells();
        
        if (availableSpells.isEmpty() || isSilenced()) {
            return 0;
        }
        
        // Agresyvus priešas renkasi didžiausią žalą darantį burtą
        Spell bestDamageSpell = availableSpells.stream()
                .filter(s -> s.getType() == Spell.SpellType.DAMAGE)
                .max((s1, s2) -> Integer.compare(s1.getValue(), s2.getValue()))
                .orElse(null);
        
        if (bestDamageSpell != null && bestDamageSpell.canUse(getMana())) {
            useMana(bestDamageSpell.getManaCost());
            int damage = bestDamageSpell.getValue();
            characterTarget.takeDamage(damage);
            System.out.println("  " + getName() + " naudoja " + bestDamageSpell.getName() + 
                             " ir daro " + damage + " žalos!");
            return damage;
        }
        
        // Jei nėra žalos darančių burtų, naudoja bet kurį galimą
        if (!availableSpells.isEmpty()) {
            Spell spell = availableSpells.get(0);
            useMana(spell.getManaCost());
            if (spell.getType() == Spell.SpellType.DAMAGE) {
                int damage = spell.getValue();
                characterTarget.takeDamage(damage);
                return damage;
            }
        }
        
        return 0;
    }
}

