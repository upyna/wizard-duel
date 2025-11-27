package org.example.wizard;

/**
 * Polimorfizmas: AIWizard realizuoja Combatant interfeisą,
 * bet attack() metodą realizuoja kitaip nei Wizard.
 * Abstrakcija: Demonstruoja, kaip skirtingi tipai gali skirtingai realizuoti tą patį interfeisą.
 */
public class AIWizard extends Character {
    
    public AIWizard(String name, int health, int mana) {
        super(name, health, mana);
    }
    
    /**
     * Polimorfizmas: Realizuoja attack() metodą kitaip nei Wizard.
     * AIWizard naudoja strateginę ataką su didesne žala.
     */
    @Override
    public int attack(Combatant target) {
        if (target instanceof Character) {
            Character characterTarget = (Character) target;
            // AIWizard daro didesnę žalą nei Wizard
            int damage = 15;
            characterTarget.takeDamage(damage);
            return damage;
        }
        return 0;
    }
}

