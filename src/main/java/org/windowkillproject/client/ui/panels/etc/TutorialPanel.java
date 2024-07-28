package org.windowkillproject.client.ui.panels.etc;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.ui.App;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.server.Config.APP_HEIGHT;
import static org.windowkillproject.server.Config.APP_WIDTH;

public class TutorialPanel extends Panel {
    public TutorialPanel(GameClient client) {
        super(client);
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();

        components.add(jLabelMaker("while (isGame()) {", 100, 50, 500, 50));
        components.add(jLabelMaker("    press [space] to open shop", 100, 100, 500, 50));
        components.add(jLabelMaker("    press [slash /] to open shop", 100, 150, 500, 50));
        components.add(jLabelMaker("    press [escape] to exit shop", 100, 200, 500, 50));
        components.add(jLabelMaker("    press [your_Direction_Key] to move in that/those directions", 100, 250, 600, 50));
        components.add(jLabelMaker("    //you can check/change the default Direction keys in Settings", 100, 300, 600, 50));
        components.add(jLabelMaker("}", 100, 350, 100, 50));
        components.add(jLabelMaker("if (isChangingADirection())", 100, 450, 280, 50));
        components.add(jLabelMaker("    use [escape] if you decided not to!", 380, 450, 400, 50));
        components.add(buttonMaker("Menu", 730, 370, e -> App.initPFrame()));


        return components;
    }
}
