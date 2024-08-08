package org.windowkillproject.server.model;

import org.windowkillproject.server.model.globe.GlobeModel;

public interface Drawable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    String getId();
    GlobeModel getGlobeModel();
}
