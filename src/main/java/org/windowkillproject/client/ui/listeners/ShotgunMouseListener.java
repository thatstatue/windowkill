package org.windowkillproject.client.ui.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import org.windowkillproject.client.GameClient;
import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.Request.*;
public class ShotgunMouseListener implements NativeMouseListener {
    public long empowerInitSeconds = Long.MAX_VALUE;
    public boolean slaughter;
    private final GameClient client;

    public void setEmpowerInitSeconds(long empowerInitSeconds) {
        this.empowerInitSeconds = empowerInitSeconds;
    }

    public ShotgunMouseListener(GameClient client) {
        super();
        this.client = client;
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        if (client.getApp().getGameFrame().isVisible()) {
            Point2D mouseLoc = MouseInfo.getPointerInfo().getLocation();

            client.sendMessage(REQ_SHOOT_BULLET+ REGEX_SPLIT+ empowerInitSeconds+ REGEX_SPLIT+ mouseLoc.getX()+ REGEX_SPLIT+ mouseLoc.getY());
//            if (slaughter) { todo slaughter
//                bulletModel.setSlaughter(true);
//                slaughter = false;
//            }
        }
    }


    private static boolean started;

    public static boolean isStarted() {
        return started;
    }

    public void startListener() {
        GlobalScreen.addNativeMouseListener(this);
        started = true;
    }

}
