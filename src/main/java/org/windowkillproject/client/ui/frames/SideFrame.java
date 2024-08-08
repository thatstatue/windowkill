package org.windowkillproject.client.ui.frames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.windowkillproject.Constants.*;
@JsonIgnoreProperties({
        "parent", "valid", "bounds", "graphics", "peer", "treeLock",
        "componentOrientation", "mouseListeners", "keyListeners", "focusListeners",
        "containerListeners", "windowListeners", "hierarchyListeners", "propertyChangeListeners",
        "inputMethodListeners", "ancestorListeners", "accessibleContext", "ui",
        "inputVerifier", "actionMap", "border", "graphicsConfiguration", "raster",
        "size", "x", "y", "width", "height", "boundsOp", "bounds2D", "fontMetrics",
        "font", "cursor", "background", "foreground", "enabled", "doubleBuffered",
        "insets", "window", "inputContext", "layout", "autoFocusTransferOnDisposal",
        "focusTraversalKeys", "focusTraversalPolicy", "rootPane", "rootPaneCheckingEnabled",
        "focusTraversalKeysEnabled", "warningString", "temporaryLostComponent",
        "focusTraversalPolicyProvider", "bufferStrategy", "ownedWindows","setOwnedWindowsAlwaysOnTop",
        "temporaryLostComponent", "ownedWindows", "focusedWindow", "transientData",
        "focusTraversalPolicyProvider", "focusTraversalKeysEnabled", "ownedWindowList",
        "componentOrientation", "defaultFocusTraversalPolicy", "layeredPane", "glassPane"})
public class SideFrame extends JFrame {
    public <T extends Panel> SideFrame(Class<T> panel , GameClient client) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(APP_TITLE);
        this.setLayout(null);
        try {
            Constructor<T> constructor = panel.getConstructor(GameClient.class);
            setContentPane(constructor.newInstance(client));
        } catch (NoSuchMethodException | InstantiationException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
