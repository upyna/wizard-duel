package org.example.wizard;

public interface Combatant {
    int attack(Combatant target);

    void update();

    boolean isAlive();

    String getName();
}