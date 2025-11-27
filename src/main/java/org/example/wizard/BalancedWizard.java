package org.example.wizard;

import java.util.List;
import java.util.Random;

/**
 * Polimorfizmas: BalancedWizard realizuoja Combatant interfeisą,
 * bet attack() metodą realizuoja subalansuotai - naudoja įvairius burtus.
 */
public class BalancedWizard extends Character {
    private final Random random;
    
    public BalancedWizard(String name, int health, int mana) {
        super(name, health, mana);
        this.random = new Random();
    }
    
    /**
     * Polimorfizmas: Realizuoja attack() metodą subalansuotai -
     * naudoja įvairius burtus pagal situaciją.
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
        
        // Subalansuotas priešas naudoja įvairius burtus
        double healthRatio = (double) getHealth() / getMaxHealth();
        
        // Jei mažai gyvybių, bando gydytis
        if (healthRatio < 0.3 && random.nextDouble() < 0.6) {
            Spell healSpell = availableSpells.stream()
                    .filter(s -> s.getType() == Spell.SpellType.HEAL)
                    .findFirst()
                    .orElse(null);
            
            if (healSpell != null && healSpell.canUse(getMana())) {
                useMana(healSpell.getManaCost());
                heal(healSpell.getValue());
                System.out.println("  " + getName() + " naudoja " + healSpell.getName() + 
                                 " ir atsistato " + healSpell.getValue() + " gyvybių!");
                return 0;
            }
        }
        
        // Kartais naudoja status efektus
        if (random.nextDouble() < 0.3) {
            Spell statusSpell = availableSpells.stream()
                    .filter(s -> s.getType() == Spell.SpellType.STATUS && s.getStatusEffect() != null)
                    .findFirst()
                    .orElse(null);
            
            if (statusSpell != null && statusSpell.canUse(getMana())) {
                useMana(statusSpell.getManaCost());
                if (statusSpell.getStatusEffect() != null) {
                    StatusEffect effect = new StatusEffect(
                            statusSpell.getStatusEffect().getType(),
                            statusSpell.getStatusEffect().getDuration(),
                            statusSpell.getStatusEffect().getValue()
                    );
                    characterTarget.addStatusEffect(effect);
                    System.out.println("  " + getName() + " naudoja " + statusSpell.getName() + 
                                     " ir uždeda " + effect.getType() + " efektą!");
                }
                return 0;
            }
        }
        
        // Dažniausiai atakuoja
        Spell damageSpell = availableSpells.stream()
                .filter(s -> s.getType() == Spell.SpellType.DAMAGE)
                .max((s1, s2) -> Integer.compare(s1.getValue(), s2.getValue()))
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

