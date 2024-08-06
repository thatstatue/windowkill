package org.windowkillproject.controller;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.game.PanelView;
import org.windowkillproject.client.view.ObjectView;
import org.windowkillproject.client.view.Viewable;
import org.windowkillproject.client.view.abilities.*;
import org.windowkillproject.client.view.entities.enemies.EnemyView;
import org.windowkillproject.client.view.entities.enemies.normals.*;
import org.windowkillproject.server.model.Drawable;
import org.windowkillproject.server.model.abilities.*;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.server.model.entities.enemies.finalboss.LeftHandModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.PunchFistModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.RightHandModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.SmileyHeadModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.normals.*;
import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.client.view.entities.EpsilonView;
import org.windowkillproject.client.view.entities.enemies.finalboss.LeftHandView;
import org.windowkillproject.client.view.entities.enemies.finalboss.PunchFistView;
import org.windowkillproject.client.view.entities.enemies.finalboss.RightHandView;
import org.windowkillproject.client.view.entities.enemies.finalboss.SmileyHeadView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BarricadosView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BlackOrbView;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.globe.GlobesManager;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static org.windowkillproject.client.GameClient.clients;

public class GlobeController {
    private final GlobeModel globeModel;
    public GlobeController(GlobeModel globeModel){
        this.globeModel = globeModel;
    }

    public  <T extends ObjectView> void createObjectView(String id, int x, int y, int width, int height){
//        ObjectModel objectModel = findModel(id);
//        if (objectModel instanceof EntityModel) createEntityView(id, x, y, width, height);
//        else createAbilityView(id,x, y);
    }

    public <T extends EntityView> void createEntityView(String id, int x, int y, int width, int height) {
        EntityModel entityModel = findModel(id);

        Class entityViewCls = getEntityViewClass(entityModel);
        if (entityViewCls != null) {
            try {
                EntityView entityView;
                if (entityModel instanceof EnemyModel) {
                    EnemyModel enemyModel = (EnemyModel) entityModel;
                    Constructor<T> constructor = (Constructor<T>) entityViewCls.getConstructor(String.class, Polygon.class);
                    entityView = constructor.newInstance(id, enemyModel.getPolygon());
                }else {
                    Constructor<T> constructor = (Constructor<T>) entityViewCls.getConstructor(String.class);
                    entityView = constructor.newInstance(id);
                }
                entityView.set(x, y, width, height);

            } catch (NoSuchMethodException | InstantiationException |
                     InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void createPanelView(String id, int x, int y, int width, int height) {
        PanelModel panelModel = findModel(id);
        Class<PanelView> panelViewCls = PanelView.class;
        if (panelModel != null) {
            try {
                Constructor<PanelView> constructor = panelViewCls.getConstructor(String.class, GameClient.class);
                String globeId = globeModel.getId();
                for (GameClient player : getPlayers(clients, globeId)) {
                    var panelView = constructor.newInstance(id, player);
                    panelView.set(x, y, width, height);
                }

            } catch (NoSuchMethodException | InstantiationException |
                     InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Class getEntityViewClass(EntityModel entityModel) {
        Class entityViewCls = null;
        if (entityModel instanceof TrigorathModel) {
            entityViewCls = TrigorathView.class;
        } else if (entityModel instanceof SquarantineModel) {
            entityViewCls = SquarantineView.class;
        } else if (entityModel instanceof NecropickModel) {
            entityViewCls = NecropickView.class;
        } else if (entityModel instanceof OmenoctModel) {
            entityViewCls = OmenoctView.class;
        } else if (entityModel instanceof WyrmModel) {
            entityViewCls = WyrmView.class;
        } else if (entityModel instanceof EpsilonModel) {
            entityViewCls = EpsilonView.class;
        } else if (entityModel instanceof ArchmireModel) {
            entityViewCls = ArchmireView.class;
        }else if (entityModel instanceof BarricadosModel){
            entityViewCls = BarricadosView.class;
        }else if (entityModel instanceof BlackOrbModel){
            entityViewCls = BlackOrbView.class;
        }else if (entityModel instanceof SmileyHeadModel){
            entityViewCls = SmileyHeadView.class;
        }else if (entityModel instanceof RightHandModel){
            entityViewCls = RightHandView.class;
        }else if (entityModel instanceof PunchFistModel){
            entityViewCls = PunchFistView.class;
        }else if (entityModel instanceof LeftHandModel){
            entityViewCls = LeftHandView.class;
        }
        return entityViewCls;
    }

    public  <T extends AbilityView> void createAbilityView(String id, int x, int y) {
        AbilityModel abilityModel = findModel(id);
        Class abilityViewCls = getAbilityViewCls(abilityModel);
        if (abilityViewCls != null) {
            try {
                if (abilityModel instanceof ProjectileModel) {
                    var projectileModel = (ProjectileModel) abilityModel;
                    Constructor<T> constructor = (Constructor<T>) abilityViewCls.getConstructor
                            (String.class, int.class, int.class, Color.class, Color.class);
                    constructor.newInstance(id, x, y, projectileModel.getTopColor(), projectileModel.getBottomColor());

                } else if (abilityModel instanceof MomentModel) {
                    MomentModel momentModel = (MomentModel) abilityModel;
                    Constructor<T> constructor = (Constructor<T>) abilityViewCls.getConstructor
                            (String.class, int.class, int.class, int.class);
                    constructor.newInstance(id, x, y, momentModel.getRadius());

                } else {
                    Constructor<T> constructor = (Constructor<T>) abilityViewCls.getConstructor
                            (String.class, int.class, int.class);
                    constructor.newInstance(id, x, y);
                }
            } catch (NoSuchMethodException | InstantiationException |
                     InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Class getAbilityViewCls(AbilityModel abilityModel) {
        Class abilityViewCls = null;
        if (abilityModel instanceof BulletModel) {
            abilityViewCls = BulletView.class;
        } else if (abilityModel instanceof CollectableModel) {
            abilityViewCls = CollectableView.class;
        } else if (abilityModel instanceof VertexModel) {
            abilityViewCls = VertexView.class;
        } else if (abilityModel instanceof ProjectileModel) {
            abilityViewCls = ProjectileView.class;
        } else if (abilityModel instanceof MomentModel) {
            abilityViewCls = MomentView.class;
        }else if (abilityModel instanceof PortalModel){
            abilityViewCls = PortalView.class;
        }
        return abilityViewCls;
    }

    public  <T extends Viewable> void setViewBounds(T view) {
        if (findModel(view.getId()) instanceof EntityModel) {
            EntityModel entityModel = findModel(view.getId());
            if (entityModel != null) {
                view.set(
                        entityModel.getX(),
                        entityModel.getY(),
                        entityModel.getWidth(),
                        entityModel.getHeight()
                );
                if (entityModel instanceof EnemyModel) {
                    var enemyView = (EnemyView) view;
                    enemyView.setPolygon(((EnemyModel) entityModel).getPolygon());
                }
                if (entityModel instanceof Hideable){
                    boolean visible = ((Hideable) entityModel).isVisible();
                    var entityView = (EntityView) view;
                    entityView.setVisible(visible);
                }
            } else view.setEnabled(false);
        } else if (findModel(view.getId()) instanceof AbilityModel) {
            AbilityModel abilityModel = findModel(view.getId());
            if (abilityModel != null) {
                if (abilityModel instanceof MomentModel) {
                    MomentModel momentModel = (MomentModel) abilityModel;
                    view.set(
                            momentModel.getX(),
                            momentModel.getY(),
                            momentModel.getRadius(),
                            momentModel.getRadius()
                    );
                    MomentView momentView = (MomentView) view;
                  //  momentView.setColorOpacity(100 - (glapsedTime.getTotalSeconds()-momentModel.getTime())*17); todo for now isn't necessary
                } else {
                    view.set(
                            abilityModel.getX(),
                            abilityModel.getY(),
                            5, 5);
                }
            } else if(findModel(view.getId()) instanceof PanelModel) {
                PanelModel panelModel = findModel(view.getId());
                if (panelModel != null) {
                    view.set(
                            panelModel.getX(),
                            panelModel.getY(),
                            panelModel.getWidth(),
                            panelModel.getHeight()
                    );
                }else view.setEnabled(false);
            }
            else{
                view.setEnabled(false);
            }

        } else {
            view.setEnabled(false);
        }

    }

    public <T extends Drawable> T findModel(String id) {
        for (EntityModel entityModel : globeModel.getEntityModels()) {
            if (entityModel.getId().equals(id)) return (T) entityModel;
        }
        for (int i = 0; i < globeModel.getAbilityModels().size(); i++) {
            AbilityModel abilityModel = globeModel.getAbilityModels().get(i);
            if (abilityModel.getId().equals(id)) return (T) abilityModel;
        }
        for (int i = 0 ; i< globeModel.getPanelModels().size(); i++){
            PanelModel panelModel = globeModel.getPanelModels().get(i);
            if (panelModel.getId().equals(id)) return (T) panelModel;
        }

        return null;
    }
    private static ArrayList<GameClient> getPlayers(ArrayList<GameClient> clients, String globeId){
        ArrayList<GameClient> ans = new ArrayList<>();
        for (GameClient client: clients){
            if (client.getApp().getGlobeId().equals(globeId)) ans.add(client);
        }
        return ans;
    }
}
