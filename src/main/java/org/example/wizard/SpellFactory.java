package org.example.wizard;

/**
 * Design Pattern: Factory Method
 * Sukuria Spell objektus pagal tipą, paslėpdamas sudėtingą objektų sukūrimo logiką.
 */
public class SpellFactory {
    
    public static Spell createDamageSpell(String name, int manaCost, int damage) {
        return new Spell(name, manaCost, Spell.SpellType.DAMAGE, damage);
    }
    
    public static Spell createHealSpell(String name, int manaCost, int healAmount) {
        return new Spell(name, manaCost, Spell.SpellType.HEAL, healAmount);
    }
    
    public static Spell createProtectionSpell(String name, int manaCost, int shieldValue) {
        return new Spell(name, manaCost, Spell.SpellType.PROTECTION, shieldValue);
    }
    
    public static Spell createManaDrainSpell(String name, int manaCost, int drainAmount) {
        return new Spell(name, manaCost, Spell.SpellType.MANA_DRAIN, drainAmount);
    }
    
    public static Spell createStatusSpell(String name, int manaCost, StatusEffect statusEffect) {
        return new Spell(name, manaCost, Spell.SpellType.STATUS, 0, statusEffect);
    }
    
    public static Spell createPoisonSpell(String name, int manaCost, int duration, int damage) {
        StatusEffect poisonEffect = new StatusEffect(StatusEffect.StatusType.POISON, duration, damage);
        return createStatusSpell(name, manaCost, poisonEffect);
    }
    
    public static Spell createSilenceSpell(String name, int manaCost, int duration) {
        StatusEffect silenceEffect = new StatusEffect(StatusEffect.StatusType.SILENCE, duration, 0);
        return createStatusSpell(name, manaCost, silenceEffect);
    }
}

