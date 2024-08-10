package org.windowkillproject.client.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.league.Alert;
import org.windowkillproject.client.ui.panels.game.InternalPanelView;
import org.windowkillproject.client.ui.panels.game.MainPanelView;
import org.windowkillproject.client.ui.panels.game.PanelView;
import org.windowkillproject.client.view.abilities.*;
import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.client.view.entities.EpsilonView;
import org.windowkillproject.client.view.entities.enemies.EnemyView;
import org.windowkillproject.client.view.entities.enemies.finalboss.LeftHandView;
import org.windowkillproject.client.view.entities.enemies.finalboss.PunchFistView;
import org.windowkillproject.client.view.entities.enemies.finalboss.RightHandView;
import org.windowkillproject.client.view.entities.enemies.finalboss.SmileyHeadView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BarricadosView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BlackOrbView;
import org.windowkillproject.client.view.entities.enemies.normals.*;
import org.windowkillproject.controller.json.JacksonMapper;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.OK_OPTION;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.client.GameClient.clients;
import static org.windowkillproject.client.GameClient.usernameMap;
import static org.windowkillproject.client.ui.panels.game.PanelView.panelViews;
import static org.windowkillproject.client.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.client.view.entities.EntityView.entityViews;
import static org.windowkillproject.client.view.entities.EntityView.getEntityViews;

public class ViewsRenderer {
    public static void updateViews(String[] parts) {
        //second part is globe id and the process continues only if the client belongs there
        String globeId = parts[1];
        String request = parts[2];
        String objectId = parts[3];
        //inbetween clients : redirect + null + req + to + from
        for (GameClient gameClient : clients) {
            if (request.equals(REQ_NOTIFY_START_BATTLE)) {
                gameClient.setAlert(Alert.startBattle);
            }else if(request.equals(REQ_NEW_GAME_MONOMACHIA)){
                if (gameClient.getUsername().equals(objectId))
                    handleMonomachia(gameClient,parts[4]);
            }else {
                if (gameClient.getApp().getGlobeId() != null && gameClient.getApp().getGlobeId().equals(globeId)) {
                    switch (request) {
                        case REQ_CREATE_PANEL -> handleCreatePanel(parts, gameClient);
                        case REQ_CREATE_ABILITY -> handleCreateAbility(parts);
                        case REQ_CREATE_ENTITY -> handleCreateEntity(parts);
                        case REQ_REMOVE_OBJECT -> handleRemoveId(objectId);
                        case REQ_MODIFY_OBJECT -> handleModifyObject(parts);
                    }
                }
            }
        }
    }

    private static void handleMonomachia(GameClient to, String fromUsername){
        String ans = LEAGUE_REDIRECT+ REGEX_SPLIT+ RES_NEW_GAME_MONOMACHIA +
                REGEX_SPLIT+ to.getUsername() + REGEX_SPLIT+ fromUsername + REGEX_SPLIT;
        if (OK_OPTION == JOptionPane.showConfirmDialog(null,
                "player " + fromUsername+
                        "has requested\nMONOMACHIA battle, accept?", "battle request",
                JOptionPane.OK_CANCEL_OPTION)){

           to.sendMessage(ans+ true);
       }else to.setUsername(ans +false);

    }
    private static void handleCreatePanel(String[] parts, GameClient gameClient) {
        String id = parts[3];
        boolean isMain = Boolean.parseBoolean(parts[4]);
        if (isMain) {
            panelViews.removeIf(panelView -> panelView instanceof MainPanelView || panelView.getId() == null);
            System.out.println(panelViews.size() + "   hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
            var mainPanel = new MainPanelView(id, gameClient);
            panelViews.add(mainPanel);
            gameClient.getApp().getGameFrame().setMainPanelView(mainPanel);
        } else {
            int x = Integer.parseInt(parts[5]);
            int y = Integer.parseInt(parts[6]);
            int width = Integer.parseInt(parts[7]);
            int height = Integer.parseInt(parts[8]);

            new InternalPanelView(gameClient, id, x, y, width, height);
        }
    }

    private static <T extends EntityView> void handleCreateEntity(String[] parts) {
        int size = parts.length;
        try {
            Class entityViewCls = getEntityClassFromIndex(Integer.parseInt(parts[size - 1]));
            if (entityViewCls != null) {
                Constructor<T> constructor = null;
                T entityView;
                if (entityViewCls.equals(EpsilonView.class)) {
                    constructor = entityViewCls.getConstructor(String.class, String.class);
                    entityView = constructor.newInstance(parts[1], parts[3]);
                    System.out.println("i just created epsilon view with id " + parts[3]);
                } else {
                    Polygon polygon = JacksonMapper.getInstance().readValue(parts[size - 2], Polygon.class);
                    constructor = entityViewCls.getConstructor(String.class, String.class, Polygon.class);
                    entityView = constructor.newInstance(parts[1], parts[3], polygon);
                }
                entityView.set(Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5]),
                        Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
            }

        } catch (NoSuchMethodException | InvocationTargetException | JsonProcessingException |
                 InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <T extends Viewable> void handleCreateAbility(String[] parts) {
        int size = parts.length;
        try {
            Class abilityViewCls = getAbilityClassFromIndex(Integer.parseInt(parts[size - 1]));
            Constructor<T> constructor;
            if (abilityViewCls != null) {
                T abilityView;
                int radius = 5;
                if (size > 7) {
                    radius = Integer.parseInt(parts[7]);
                    constructor = abilityViewCls.getConstructor(String.class, String.class, int.class, int.class, int.class);
                    abilityView = constructor.newInstance(parts[1], parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
                } else {
                    constructor = abilityViewCls.getConstructor(String.class, String.class, int.class, int.class);
                    abilityView = constructor.newInstance(parts[1], parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));

                }
                abilityView.set(Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5]),
                        radius, radius);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRemoveId(String id) {
        ArrayList<EntityView> views = getEntityViews();
        for (int i = 0; i < views.size(); i++) {
            EntityView entityView = views.get(i);
            if (entityView.getId().equals(id)) {
                entityView.setEnabled(false);
                entityViews.remove(entityView);
                return;
            }
        }
        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            if (abilityView.getId().equals(id)) {
//                abilityView.setEnabled(false);
                abilityView.setVisible(false);

            }
        }
        for (int i = 0; i < panelViews.size(); i++) {
            PanelView panelView = panelViews.get(i);
            if (panelView.getId() != null && panelView.getId().equals(id)) {
                var layeredPane = panelView.getClient().getApp().getGameFrame().getLayeredPane();
                layeredPane.remove(panelView);
                panelViews.remove(panelView);
                panelView.setEnabled(false);

            }
        }
    }

    private static void handleModifyObject(String[] parts) {
        String globeId = parts[1];
        String id = parts[3];
        int x = Integer.parseInt(parts[4]);
        int y = Integer.parseInt(parts[5]);
        int width = Integer.parseInt(parts[6]);
        int height = Integer.parseInt(parts[7]);

        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            if (entityView.getGlobeId().equals(globeId)
                    && entityView.getId().equals(id)) {
                entityView.set(x, y, width, height);

                if (entityView instanceof EnemyView enemyView) {
                    if (!parts[8].equals("null")) {
                        try {
                            Polygon polygon = JacksonMapper.getInstance().readValue(parts[8], Polygon.class);
                            enemyView.setPolygon(polygon);
//                            System.out.println("i just set polygon for enemy in rendering");
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (parts.length > 9) entityView.setVisible(Boolean.parseBoolean(parts[9]));
                return;
            }
        }
        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            if (abilityView.getGlobeId().equals(globeId) &&
                    abilityView.getId().equals(id)) {
                abilityView.set(x, y, width, height);
            }
        }
        for (int i = 0; i < panelViews.size(); i++) {
            PanelView panelView = panelViews.get(i);
            if (panelView.getId() != null && panelView.getId().equals(id)) {
                panelView.set(x, y, width, height);
            }
        }
    }


    private static Class getEntityClassFromIndex(int index) {
        switch (index) {
            case 0 -> {
                return TrigorathView.class;
            }
            case 1 -> {
                return SquarantineView.class;
            }
            case 2 -> {
                return NecropickView.class;
            }
            case 3 -> {
                return OmenoctView.class;
            }
            case 4 -> {
                return WyrmView.class;
            }
            case 5 -> {
                return EpsilonView.class;
            }
            case 6 -> {
                return ArchmireView.class;
            }
            case 7 -> {
                return BarricadosView.class;
            }
            case 8 -> {
                return BlackOrbView.class;
            }
            case 9 -> {
                return SmileyHeadView.class;
            }
            case 10 -> {
                return RightHandView.class;
            }
            case 11 -> {
                return PunchFistView.class;
            }
            case 12 -> {
                return LeftHandView.class;
            }
            default -> {
                return null;
            }
        }
    }

    private static Class getAbilityClassFromIndex(int index) {
        switch (index) {
            case 0 -> {
                return BulletView.class;
            }
            case 1 -> {
                return CollectableView.class;
            }
            case 2 -> {
                return VertexView.class;
            }
            case 3 -> {
                return ProjectileView.class;
            }
            case 4 -> {
                return MomentView.class;
            }
            case 5 -> {
                return PortalView.class;
            }
            default -> {
                return null;
            }
        }
    }
}
