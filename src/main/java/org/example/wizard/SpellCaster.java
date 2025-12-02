package org.example.wizard;

public class SpellCaster {
    
    private static final int SHIELD_DURATION = 3;
    private static final int DEFAULT_MULTIPLIER = 1;
    
    public static void castSpell(Character caster, Character target, Spell spell, 
                                 int comboMultiplier) {
        if (!spell.canUse(caster.getMana())) {
            System.out.println(caster.getName() + " neturi pakankamai manos!");
            return;
        }
        
        caster.useMana(spell.getManaCost());
        
        switch (spell.getType()) {
            case DAMAGE:
                handleDamageSpell(caster, target, spell, comboMultiplier);
                break;
            case HEAL:
                handleHealSpell(caster, spell, comboMultiplier);
                break;
            case PROTECTION:
                handleProtectionSpell(caster, spell);
                break;
            case MANA_DRAIN:
                handleManaDrainSpell(caster, target, spell);
                break;
            case STATUS:
                handleStatusSpell(target, spell);
                break;
        }
    }
    
    private static void handleDamageSpell(Character caster, Character target, 
                                         Spell spell, int comboMultiplier) {
        int damage = spell.getValue() * comboMultiplier;
        target.takeDamage(damage);
        System.out.println("  " + caster.getName() + " daro " + damage + " žalos " + target.getName() + "!");
        if (comboMultiplier > DEFAULT_MULTIPLIER) {
            System.out.println("  KOMBO! Žala padidinta " + comboMultiplier + "x!");
        }
    }
    
    private static void handleHealSpell(Character caster, Spell spell, int comboMultiplier) {
        int heal = spell.getValue() * comboMultiplier;
        caster.heal(heal);
        System.out.println("  " + caster.getName() + " atsistato " + heal + " gyvybių!");
        if (comboMultiplier > DEFAULT_MULTIPLIER) {
            System.out.println("  KOMBO! Gydymas padidintas " + comboMultiplier + "x!");
        }
    }
    
    private static void handleProtectionSpell(Character caster, Spell spell) {
        int shield = spell.getValue();
        StatusEffect shieldEffect = new StatusEffect(StatusEffect.StatusType.SHIELD, SHIELD_DURATION, shield);
        caster.addStatusEffect(shieldEffect);
        System.out.println("  " + caster.getName() + " gauna " + shield + " apsaugos (3 ėjimai)!");
    }
    
    private static void handleManaDrainSpell(Character caster, Character target, Spell spell) {
        int drain = spell.getValue();
        target.useMana(drain);
        System.out.println("  " + caster.getName() + " sunaudoja " + drain + " manos " + target.getName() + "!");
    }
    
    private static void handleStatusSpell(Character target, Spell spell) {
        if (spell.getStatusEffect() != null) {
            StatusEffect effect = new StatusEffect(
                    spell.getStatusEffect().getType(),
                    spell.getStatusEffect().getDuration(),
                    spell.getStatusEffect().getValue()
            );
            target.addStatusEffect(effect);
            System.out.println("  " + target.getName() + " gauna status efektą: " + effect.getType());
        }
    }
    
    public static int checkCombo(Character caster, Character player, 
                                 String lastPlayerSpell, Spell spell, 
                                 int comboMultiplier, int defaultMultiplier) {
        if (caster == player && lastPlayerSpell != null && 
            lastPlayerSpell.equals(spell.getName())) {
            return comboMultiplier;
        }
        return defaultMultiplier;
    }
}