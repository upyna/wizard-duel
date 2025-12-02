package org.example.wizard;

import java.util.ArrayList;
import java.util.List;


public class Spell {
    private final String name;
    private final int manaCost;
    private final SpellType type;
    private final int value;
    private final StatusEffect statusEffect;
    private final List<String> comboTriggers;
    
    public Spell(String name, int manaCost, SpellType type, int value) {
        this(name, manaCost, type, value, null, new ArrayList<>());
    }
    
    public Spell(String name, int manaCost, SpellType type, int value, StatusEffect statusEffect) {
        this(name, manaCost, type, value, statusEffect, new ArrayList<>());
    }
    
    public Spell(String name, int manaCost, SpellType type, int value, 
                 StatusEffect statusEffect, List<String> comboTriggers) {
        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
        this.value = value;
        this.statusEffect = statusEffect;
        this.comboTriggers = comboTriggers;
    }
    
    public String getName() {
        return name;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public SpellType getType() {
        return type;
    }
    
    public int getValue() {
        return value;
    }
    
    public StatusEffect getStatusEffect() {
        return statusEffect;
    }
    
    public List<String> getComboTriggers() {
        return comboTriggers;
    }
    
    public boolean canUse(int currentMana) {
        return currentMana >= manaCost;
    }
    
    public enum SpellType {
        DAMAGE,
        HEAL,
        PROTECTION,
        MANA_DRAIN,
        STATUS
    }
    
    @Override
    public String toString() {
        return String.format("%s (Mana: %d, %s: %d)", name, manaCost, type, value);
    }
}