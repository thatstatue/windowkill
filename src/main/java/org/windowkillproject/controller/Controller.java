package org.windowkillproject.controller;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.abilities.*;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.*;
import org.windowkillproject.model.abilities.MomentModel;
import org.windowkillproject.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.model.entities.enemies.finalboss.LeftHandModel;
import org.windowkillproject.model.entities.enemies.finalboss.PunchFistModel;
import org.windowkillproject.model.entities.enemies.finalboss.RightHandModel;
import org.windowkillproject.model.entities.enemies.finalboss.SmileyHeadModel;
import org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.model.entities.enemies.normals.*;
import org.windowkillproject.view.*;
import org.windowkillproject.view.abilities.*;
import org.windowkillproject.view.entities.EntityView;
import org.windowkillproject.view.entities.EpsilonView;
import org.windowkillproject.view.entities.enemies.*;
import org.windowkillproject.view.entities.enemies.finalboss.LeftHandView;
import org.windowkillproject.view.entities.enemies.finalboss.PunchFistView;
import org.windowkillproject.view.entities.enemies.finalboss.RightHandView;
import org.windowkillproject.view.entities.enemies.finalboss.SmileyHeadView;
import org.windowkillproject.view.entities.enemies.minibosses.BarricadosView;
import org.windowkillproject.view.entities.enemies.minibosses.BlackOrbView;
import org.windowkillproject.view.entities.enemies.normals.*;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.model.abilities.AbilityModel.abilityModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class Controller {

    public static <T extends EntityView> void createEntityView(String id, int x, int y, int width, int height) {
        EntityModel entityModel = findModel(id);

        Class entityViewCls = getEntityViewClass(entityModel);
        if (entityViewCls != null) {
            try {
                Constructor<T> constructor = (Constructor<T>) entityViewCls.getConstructor(String.class);
                var entityView = constructor.newInstance(id);
                entityView.set(x, y, width, height);

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

    public static <T extends AbilityView> void createAbilityView(String id, int x, int y) {
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
        }
        return abilityViewCls;
    }

    public static <T extends Viewable> void setViewBounds(T view) {
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
                    momentView.setColorOpacity(100 - (ElapsedTime.getTotalSeconds()-momentModel.getTime())*17);
                } else {
                    view.set(
                            abilityModel.getX(),
                            abilityModel.getY(),
                            5, 5);
                }
            } else view.setEnabled(false);

        } else {
            view.setEnabled(false);
        }

    }

    public static <T extends Drawable> T findModel(String id) {
        for (EntityModel entityModel : entityModels) {
            if (entityModel.getId().equals(id)) return (T) entityModel;
        }
        for (int i = 0; i < abilityModels.size(); i++) {
            AbilityModel abilityModel = abilityModels.get(i);
            if (abilityModel.getId().equals(id)) return (T) abilityModel;
        }

        return null;
    }
    public static void deleteGamePanel(GamePanel gamePanel) {
        if (gamePanel!=null) {
            gamePanelsBounds.remove(gamePanel);
            gamePanels.remove(gamePanel);
            gamePanel.setEnabled(false);
            getGameFrame().getLayeredPane().remove(gamePanel);
        }
    }
}
