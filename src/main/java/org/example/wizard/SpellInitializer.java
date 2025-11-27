package org.example.wizard;

/**
 * DRY principas: centralizuota burtų inicializacija, kad būtų išvengta pasikartojančio kodo.
 */
public class SpellInitializer {
    
    public static void initializePlayerSpells(Character player) {
        player.addSpell(new Spell("Ugnies kamuolys", 10, Spell.SpellType.DAMAGE, 15));
        player.addSpell(new Spell("Šalčio strėlė", 8, Spell.SpellType.DAMAGE, 12));
        player.addSpell(new Spell("Žaibo smūgis", 15, Spell.SpellType.DAMAGE, 25));
        player.addSpell(new Spell("Gydymo burtas", 12, Spell.SpellType.HEAL, 20));
        player.addSpell(new Spell("Mano vanduo", 5, Spell.SpellType.MANA_DRAIN, 10));
        player.addSpell(new Spell("Nuodų dūmai", 10, Spell.SpellType.STATUS, 0, 
                new StatusEffect(StatusEffect.StatusType.POISON, 3, 5)));
        player.addSpell(new Spell("Apsaugos skydas", 8, Spell.SpellType.PROTECTION, 10));
        player.addSpell(new Spell("Nutildymas", 12, Spell.SpellType.STATUS, 0,
                new StatusEffect(StatusEffect.StatusType.SILENCE, 2, 0)));
    }
    
    public static void initializeAISpells(Character ai) {
        ai.addSpell(new Spell("Tamsos strėlė", 10, Spell.SpellType.DAMAGE, 15));
        ai.addSpell(new Spell("Demonų liepsna", 12, Spell.SpellType.DAMAGE, 18));
        ai.addSpell(new Spell("Mirties žvilgsnis", 18, Spell.SpellType.DAMAGE, 30));
        ai.addSpell(new Spell("Gyvybės atgavimas", 12, Spell.SpellType.HEAL, 20));
        ai.addSpell(new Spell("Mano vamzdis", 5, Spell.SpellType.MANA_DRAIN, 10));
        ai.addSpell(new Spell("Nuodų dūmai", 10, Spell.SpellType.STATUS, 0,
                new StatusEffect(StatusEffect.StatusType.POISON, 3, 5)));
        ai.addSpell(new Spell("Apsaugos skydas", 8, Spell.SpellType.PROTECTION, 10));
    }
}

