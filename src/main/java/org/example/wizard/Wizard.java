package org.example.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Burtininkas su gyvybėmis, mana ir burtų rinkiniu
 */
public class Wizard {
    private final String name;
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private final List<Spell> spells;
    private final List<StatusEffect> statusEffects;
    private boolean silenced; // Ar nutildytas (negali naudoti burtų)
    private int shield; // Apsaugos kiekis
    
    public Wizard(String name, int health, int mana) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.maxMana = mana;
        this.mana = mana;
        this.spells = new ArrayList<>();
        this.statusEffects = new ArrayList<>();
        this.silenced = false;
        this.shield = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public int getMana() {
        return mana;
    }
    
    public int getMaxMana() {
        return maxMana;
    }
    
    public List<Spell> getSpells() {
        return new ArrayList<>(spells);
    }
    
    public List<Spell> getAvailableSpells() {
        return spells.stream()
                .filter(spell -> spell.canUse(mana))
                .collect(Collectors.toList());
    }
    
    public boolean isSilenced() {
        return silenced;
    }
    
    public int getShield() {
        return shield;
    }
    
    public void addSpell(Spell spell) {
        spells.add(spell);
    }
    
    public void takeDamage(int damage) {
        // Apsauga sumažina žalą
        int actualDamage = Math.max(0, damage - shield);
        health = Math.max(0, health - actualDamage);
    }
    
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }
    
    public void useMana(int amount) {
        mana = Math.max(0, mana - amount);
    }
    
    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }
    
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        if (effect.getType() == StatusEffect.StatusType.SILENCE) {
            silenced = true;
        }
        if (effect.getType() == StatusEffect.StatusType.SHIELD) {
            shield = effect.getValue();
        }
    }
    
    public void applyStatusEffects() {
        // Taikome status efektus kiekvieną ėjimą
        List<StatusEffect> toRemove = new ArrayList<>();
        
        for (StatusEffect effect : statusEffects) {
            switch (effect.getType()) {
                case POISON:
                    takeDamage(effect.getValue());
                    System.out.println("  " + name + " kenčia nuo nuodų: -" + effect.getValue() + " gyvybių");
                    break;
                case REGENERATION:
                    heal(effect.getValue());
                    System.out.println("  " + name + " regeneruojasi: +" + effect.getValue() + " gyvybių");
                    break;
                case SILENCE:
                    // Silence is handled by the silenced flag, no action needed per turn
                    break;
                case SHIELD:
                    // Shield is handled by the shield field, no action needed per turn
                    break;
            }
            
            effect.reduceDuration();
            if (!effect.isActive()) {
                toRemove.add(effect);
                if (effect.getType() == StatusEffect.StatusType.SILENCE) {
                    silenced = false;
                }
                if (effect.getType() == StatusEffect.StatusType.SHIELD) {
                    shield = 0;
                }
            }
        }
        
        statusEffects.removeAll(toRemove);
    }
    
    public void setShield(int shield) {
        this.shield = shield;
    }
    
    public boolean isAlive() {
        return health > 0;
    }
    
    public List<StatusEffect> getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }
    
    public String getStatusString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: Gyvybės=%d/%d, Mana=%d/%d", 
                name, health, maxHealth, mana, maxMana));
        if (shield > 0) {
            sb.append(", Apsauga=").append(shield);
        }
        if (silenced) {
            sb.append(", NUTILDYTAS");
        }
        if (!statusEffects.isEmpty()) {
            sb.append(", Statusai: ");
            for (StatusEffect effect : statusEffects) {
                sb.append(effect.getType()).append("(").append(effect.getDuration()).append(") ");
            }
        }
        return sb.toString();
    }
}

