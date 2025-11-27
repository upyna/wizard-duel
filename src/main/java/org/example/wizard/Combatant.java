package org.example.wizard;

/**
 * Abstrakcija: Interfeisas, apibrėžiantis bendrą kovotojų elgesį.
 * Skirtingi priešų tipai realizuoja šį interfeisą skirtingai (polimorfizmas).
 */
public interface Combatant {
    /**
     * Ataka prieš priešininką.
     * Kiekvienas priešų tipas realizuoja šį metodą skirtingai.
     * 
     * @param target Priešininkas, prieš kurį atakuojama
     * @return Žalos kiekis, kurį padarė ataka
     */
    int attack(Combatant target);
    
    /**
     * Atnaujina būseną (pvz., status efektai).
     * Naudojamas polimorfiniam elgesiui su skirtingais objektų tipais.
     */
    void update();
    
    /**
     * Patikrina, ar kovotojas dar gyvas.
     * 
     * @return true, jei gyvas, false - jei ne
     */
    boolean isAlive();
    
    /**
     * Gauna kovotojo vardą.
     * 
     * @return Kovotojo vardas
     */
    String getName();
}

