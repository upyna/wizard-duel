package org.example.wizard;

import java.util.List;

/**
 * Design Pattern: Strategy
 * Context klasė, kuri naudoja AIStrategy interfeisą.
 * Galima keisti strategiją runtime metu.
 */
public class AI {
    private AIStrategy strategy;
    
    public AI() {
        // Nustatome numatytąją strategiją - BalancedStrategy
        this.strategy = new BalancedStrategy();
    }
    
    public AI(AIStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Design Pattern: Strategy - deleguoja burto pasirinkimą strategijai.
     */
    public Spell chooseSpell(Character aiWizard, Character playerWizard) {
        List<Spell> availableSpells = aiWizard.getAvailableSpells();
        return strategy.chooseSpell(aiWizard, playerWizard, availableSpells);
    }
    
    /**
     * Design Pattern: Strategy - leidžia keisti strategiją runtime metu.
     */
    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }
}