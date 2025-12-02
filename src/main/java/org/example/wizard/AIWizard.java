package org.example.wizard;

public class AIWizard extends Character {
    
    public AIWizard(String name, int health, int mana) {
        super(name, health, mana);
    }

    @Override
    public int attack(Combatant target) {
        if (target instanceof Character) {
            Character characterTarget = (Character) target;
            int damage = 15;
            characterTarget.takeDamage(damage);
            return damage;
        }
        return 0;
    }
}