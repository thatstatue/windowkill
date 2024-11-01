package org.windowkillproject.client.view;

public interface Viewable {
    void set(int x, int y, int width, int height);
    void setEnabled(boolean enabled);
    boolean isEnabled();
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    String getId();
}
