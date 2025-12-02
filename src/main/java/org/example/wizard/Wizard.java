package org.example.wizard;

public class Wizard extends Character {
    
    public Wizard(String name, int health, int mana) {
        super(name, health, mana);
    }

    @Override
    public int attack(Combatant target) {
        if (target instanceof Character) {
            Character characterTarget = (Character) target;
            int baseDamage = 10;
            characterTarget.takeDamage(baseDamage);
            return baseDamage;
        }
        return 0;
    }
}