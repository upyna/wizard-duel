package org.example.wizard;

import java.util.List;

public class AI {
    private AIStrategy strategy;
    
    public AI() {
        this.strategy = new BalancedStrategy();
    }
    
    public AI(AIStrategy strategy) {
        this.strategy = strategy;
    }

    public Spell chooseSpell(Character aiWizard, Character playerWizard) {
        List<Spell> availableSpells = aiWizard.getAvailableSpells();
        return strategy.chooseSpell(aiWizard, playerWizard, availableSpells);
    }

    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }
}