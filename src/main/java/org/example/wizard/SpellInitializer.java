package org.example.wizard;

public class SpellInitializer {
    
    public static void initializePlayerSpells(Character player) {
        player.addSpell(SpellFactory.createDamageSpell("Ugnies kamuolys", 10, 15));
        player.addSpell(SpellFactory.createDamageSpell("Šalčio strėlė", 8, 12));
        player.addSpell(SpellFactory.createDamageSpell("Žaibo smūgis", 15, 25));
        player.addSpell(SpellFactory.createHealSpell("Gydymo burtas", 12, 20));
        player.addSpell(SpellFactory.createManaDrainSpell("Mano vanduo", 5, 10));
        player.addSpell(SpellFactory.createPoisonSpell("Nuodų dūmai", 10, 3, 5));
        player.addSpell(SpellFactory.createProtectionSpell("Apsaugos skydas", 8, 10));
        player.addSpell(SpellFactory.createSilenceSpell("Nutildymas", 12, 2));
    }
    
    public static void initializeAISpells(Character ai) {
        ai.addSpell(SpellFactory.createDamageSpell("Tamsos strėlė", 10, 15));
        ai.addSpell(SpellFactory.createDamageSpell("Demonų liepsna", 12, 18));
        ai.addSpell(SpellFactory.createDamageSpell("Mirties žvilgsnis", 18, 30));
        ai.addSpell(SpellFactory.createHealSpell("Gyvybės atgavimas", 12, 20));
        ai.addSpell(SpellFactory.createManaDrainSpell("Mano vamzdis", 5, 10));
        ai.addSpell(SpellFactory.createPoisonSpell("Nuodų dūmai", 10, 3, 5));
        ai.addSpell(SpellFactory.createProtectionSpell("Apsaugos skydas", 8, 10));
    }
}