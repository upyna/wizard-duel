package org.example.wizard;

import java.util.List;

/**
 * Polimorfizmas: DefensiveWizard realizuoja Combatant interfeisą,
 * bet attack() metodą realizuoja kitaip - fokusuojasi į gynybą ir gydymą.
 */
public class DefensiveWizard extends Character {
    
    public DefensiveWizard(String name, int health, int mana) {
        super(name, health, mana);
    }
    
    /**
     * Polimorfizmas: Realizuoja attack() metodą gynybiniu būdu -
     * pirmiausia bando gydytis arba apsisaugoti, tik tada atakuoja.
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
        
        // Gynybiniu priešas pirmiausia bando gydytis, jei mažai gyvybių
        double healthRatio = (double) getHealth() / getMaxHealth();
        if (healthRatio < 0.5) {
            Spell healSpell = availableSpells.stream()
                    .filter(s -> s.getType() == Spell.SpellType.HEAL)
                    .findFirst()
                    .orElse(null);
            
            if (healSpell != null && healSpell.canUse(getMana())) {
                useMana(healSpell.getManaCost());
                heal(healSpell.getValue());
                System.out.println("  " + getName() + " naudoja " + healSpell.getName() + 
                                 " ir atsistato " + healSpell.getValue() + " gyvybių!");
                return 0; // Gydymas nedaro žalos priešininkui
            }
        }
        
        // Jei nėra skydo, bando jį sukurti
        if (getShield() == 0) {
            Spell shieldSpell = availableSpells.stream()
                    .filter(s -> s.getType() == Spell.SpellType.PROTECTION)
                    .findFirst()
                    .orElse(null);
            
            if (shieldSpell != null && shieldSpell.canUse(getMana())) {
                useMana(shieldSpell.getManaCost());
                StatusEffect shieldEffect = new StatusEffect(StatusEffect.StatusType.SHIELD, 3, shieldSpell.getValue());
                addStatusEffect(shieldEffect);
                System.out.println("  " + getName() + " naudoja " + shieldSpell.getName() + 
                                 " ir gauna " + shieldSpell.getValue() + " apsaugos!");
                return 0;
            }
        }
        
        // Tik tada atakuoja
        Spell damageSpell = availableSpells.stream()
                .filter(s -> s.getType() == Spell.SpellType.DAMAGE)
                .findFirst()
                .orElse(null);
        
        if (damageSpell != null && damageSpell.canUse(getMana())) {
            useMana(damageSpell.getManaCost());
            int damage = damageSpell.getValue();
            characterTarget.takeDamage(damage);
            System.out.println("  " + getName() + " naudoja " + damageSpell.getName() + 
                             " ir daro " + damage + " žalos!");
            return damage;
        }
        
        return 0;
    }
}

