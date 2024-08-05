package org.windowkillproject.client.ui.panels.game;

import org.windowkillproject.Request;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.view.Viewable;

import java.awt.*;
import java.util.ArrayList;

public abstract class PanelView extends Panel implements Viewable {

    public static ArrayList<PanelView> panelViews = new ArrayList<>();
    private final String id;
    public String getId() {
        return id;
    }

    public PanelView(String id, GameClient client) {
        super(client);
        this.id = id;
        setBackground(Color.black);
        setFocusable(true);
        requestFocusInWindow();
        synchronized (Request.LOCK) {
            panelViews.add(this);
        }

    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;
    }

    @Override
    public void set(int x, int y, int width, int height) {
        setBounds(x,y,width,height);
    }
}
