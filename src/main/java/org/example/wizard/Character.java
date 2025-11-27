package org.example.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstrakcija: Abstrakti klasė, apibrėžianti bendrą visų veikėjų elgesį.
 * Paveldėjimas: Wizard ir kitos klasės paveldės iš šios klasės.
 */
public abstract class Character implements Combatant {
    // Inkapsuliacija: visi laukai yra private
    private final String name;
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private final List<Spell> spells;
    private final List<StatusEffect> statusEffects;
    private boolean silenced;
    private int shield;
    
    protected Character(String name, int health, int mana) {
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
    
    // Inkapsuliacija: prieiga prie duomenų per getterius/setterius
    public String getName() {
        return name;
    }
    
    public int getHealth() {
        return health;
    }
    
    protected void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public int getMana() {
        return mana;
    }
    
    protected void setMana(int mana) {
        this.mana = Math.max(0, Math.min(maxMana, mana));
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
    
    protected void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }
    
    public int getShield() {
        return shield;
    }
    
    protected void setShield(int shield) {
        this.shield = shield;
    }
    
    public void addSpell(Spell spell) {
        spells.add(spell);
    }
    
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - shield);
        setHealth(health - actualDamage);
    }
    
    public void heal(int amount) {
        setHealth(health + amount);
    }
    
    public void useMana(int amount) {
        setMana(mana - amount);
    }
    
    public void restoreMana(int amount) {
        setMana(mana + amount);
    }
    
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        if (effect.getType() == StatusEffect.StatusType.SILENCE) {
            setSilenced(true);
        }
        if (effect.getType() == StatusEffect.StatusType.SHIELD) {
            setShield(effect.getValue());
        }
    }
    
    /**
     * Polimorfizmas: update() metodas, kurį gali naudoti skirtingi objektai per bendrą tipą.
     * Visi Character objektai gali būti atnaujinami per šį metodą.
     */
    @Override
    public void update() {
        applyStatusEffects();
    }
    
    public void applyStatusEffects() {
        List<StatusEffect> toRemove = new ArrayList<>();
        
        for (StatusEffect effect : statusEffects) {
            switch (effect.getType()) {
                case POISON:
                    takeDamage(effect.getValue());
                    System.out.println("  " + getName() + " kenčia nuo nuodų: -" + effect.getValue() + " gyvybių");
                    break;
                case REGENERATION:
                    heal(effect.getValue());
                    System.out.println("  " + getName() + " regeneruojasi: +" + effect.getValue() + " gyvybių");
                    break;
                case SILENCE:
                    break;
                case SHIELD:
                    break;
            }
            
            effect.reduceDuration();
            
            if (!effect.isActive()) {
                toRemove.add(effect);
                if (effect.getType() == StatusEffect.StatusType.SILENCE) {
                    setSilenced(false);
                }
                if (effect.getType() == StatusEffect.StatusType.SHIELD) {
                    setShield(0);
                }
            }
        }
        
        statusEffects.removeAll(toRemove);
    }
    
    @Override
    public boolean isAlive() {
        return health > 0;
    }
    
    public List<StatusEffect> getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }
    
    public String getStatusString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: Gyvybės=%d/%d, Mana=%d/%d", 
                getName(), getHealth(), getMaxHealth(), getMana(), getMaxMana()));
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

