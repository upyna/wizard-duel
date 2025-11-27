package org.example.wizard;

/**
 * Paveldėjimas: Wizard klasė paveldi iš Character klasės ir perima visą jos elgesį.
 * Polimorfizmas: Wizard realizuoja Combatant interfeisą, todėl gali būti naudojamas per bendrą tipą.
 */
public class Wizard extends Character {
    
    public Wizard(String name, int health, int mana) {
        super(name, health, mana);
    }
    
    /**
     * Polimorfizmas: Realizuoja attack() metodą iš Combatant interfeiso.
     * Wizard naudoja burtus atakoms.
     * 
     * @param target Priešininkas, prieš kurį atakuojama
     * @return Žalos kiekis
     */
    @Override
    public int attack(Combatant target) {
        // Wizard naudoja burtus atakoms, bet šis metodas yra realizuotas
        // konkrečiai Game klasėje per castSpell metodą
        // Čia grąžiname bazinę ataką
        if (target instanceof Character) {
            Character characterTarget = (Character) target;
            int baseDamage = 10;
            characterTarget.takeDamage(baseDamage);
            return baseDamage;
        }
        return 0;
    }
}

