package org.windowkillproject;

public enum SpecialityName {

    Banish("O' Hephaestus، Banish"),
    Empower("O’ Athena، Empower"),
    Heal("O' Apollo، Heal"),
    Dismay("O' Demios, Dismay"),
    Slumber("O' Hypnos، Slumber"),
    Slaughter("O' Phonoi، Slaughter"),



    Ares("Writ of Ares"),
    Aceso("Writ of Aceso"),
    Proteus("Writ of Proteus"),
    Astrape("Writ of Astrape"),
    Cerberus("Writ of Cerberus"),
    Melampus("Writ of Melampus"),
    Chiron("Writ of Chiron"),
    Empusa("Writ of Empusa"),
    Dolus("Writ of Dolus");


    private final String displayName;

    SpecialityName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}