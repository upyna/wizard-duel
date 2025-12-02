package org.example.wizard;

import java.util.List;

public interface AIStrategy {
    Spell chooseSpell(Character aiWizard, Character playerWizard, List<Spell> availableSpells);
}