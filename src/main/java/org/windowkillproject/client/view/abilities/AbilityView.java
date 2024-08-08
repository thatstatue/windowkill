package org.windowkillproject.client.view.abilities;

import org.windowkillproject.client.view.ObjectView;

import java.util.ArrayList;

public abstract class AbilityView extends ObjectView {
    public AbilityView(String id, int x, int y) {
        super(id);
        this.x = x;
        this.y = y;
        getAbilityViews().add(this);
        setEnabled(true);
    }
    public static ArrayList<AbilityView> abilityViews = new ArrayList<>();

    public static ArrayList<AbilityView> getAbilityViews() {
        return abilityViews;
    }

    public static void setAbilityViews(ArrayList<AbilityView> abilityViews) {
        AbilityView.abilityViews = abilityViews;
    }


    public void set(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }
}
