package org.example.wizard;

import java.util.List;

/**
 * Design Pattern: Strategy
 * Interfeisas, apibrėžiantis algoritmą burto pasirinkimui.
 * Skirtingos strategijos gali realizuoti šį interfeisą skirtingai.
 */
public interface AIStrategy {
    /**
     * Pasirenka burtą pagal strategiją.
     * 
     * @param aiWizard AI burtininkas
     * @param playerWizard Žaidėjo burtininkas
     * @param availableSpells Galimi burtai
     * @return Pasirinktas burtas arba null, jei nėra tinkamo
     */
    Spell chooseSpell(Character aiWizard, Character playerWizard, List<Spell> availableSpells);
}

