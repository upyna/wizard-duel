package org.example.wizard;

/**
 * Status efektas, kurį gali turėti burtininkas
 */
public class StatusEffect {
    private final StatusType type;
    private int duration;
    private final int value;
    
    public StatusEffect(StatusType type, int duration, int value) {
        this.type = type;
        this.duration = duration;
        this.value = value;
    }
    
    public StatusType getType() {
        return type;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void reduceDuration() {
        if (duration > 0) {
            duration--;
        }
    }
    
    public boolean isActive() {
        return duration > 0;
    }
    
    public int getValue() {
        return value;
    }
    
    public enum StatusType {
        POISON,    // Nuodai - daro žalą kiekvieną ėjimą
        SILENCE,   // Nutildymas - negali naudoti burtų
        SHIELD,    // Apsauga - sumažina gaunamą žalą
        REGENERATION // Regeneracija - gydo kiekvieną ėjimą
    }
}

