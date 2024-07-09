package org.windowkillproject.controller;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.abilities.*;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.*;
import org.windowkillproject.view.*;
import org.windowkillproject.view.abilities.*;
import org.windowkillproject.view.entities.EntityView;
import org.windowkillproject.view.entities.EpsilonView;
import org.windowkillproject.view.entities.enemies.*;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.windowkillproject.model.abilities.AbilityModel.abilityModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class Controller {
//    public static void createEntityView(String id) {
//        var entityModel = findModel(id);
//        if (entityModel instanceof EnemyModel) {
//            if (entityModel instanceof TrigorathModel) {
//                var trigorathModel = (TrigorathModel) entityModel;
//                var trigorathView = new TrigorathView(id, trigorathModel.getLocalPanel());
//                trigorathView.set(
//                        trigorathModel.getX(),
//                        trigorathModel.getY(),
//                        trigorathModel.getWidth(),
//                        trigorathModel.getHeight()
//                );
//            } else if (entityModel instanceof SquarantineModel) {
//                var squarantineModel = (SquarantineModel) entityModel;
//                var squarantineView = new SquarantineView(id, squarantineModel.getLocalPanel());
//                squarantineView.set(
//                        squarantineModel.getX(),
//                        squarantineModel.getY(),
//                        squarantineModel.getWidth(),
//                        squarantineModel.getHeight()
//                );
//            } else if (entityModel instanceof OmenoctModel) {
//                var omenoctModel = (OmenoctModel) entityModel;
//                var omenoctView = new OmenoctView(id, omenoctModel.getLocalPanel());
//                omenoctView.set(
//                        omenoctModel.getX(),
//                        omenoctModel.getY(),
//                        omenoctModel.getWidth(),
//                        omenoctModel.getHeight()
//                );
//            } else if (entityModel instanceof NecropickModel) {
//                var necropickModel = (NecropickModel) entityModel;
//                var necropickView = new NecropickView(id, necropickModel.getLocalPanel());
//                necropickView.set(
//                        necropickModel.getX(),
//                        necropickModel.getY(),
//                        necropickModel.getWidth(),
//                        necropickModel.getHeight()
//                );
//            } else if (entityModel instanceof WyrmModel) {
//                var wyrmModel = (WyrmModel) entityModel;
//                var wyrmView = new WyrmView(id, wyrmModel.getLocalPanel());
//                wyrmView.set(
//                        wyrmModel.getX(),
//                        wyrmModel.getY(),
//                        wyrmModel.getWidth(),
//                        wyrmModel.getHeight()
//                );
//            }
//        } else {
//            var epsilonModel = (EpsilonModel) entityModel;
//            assert epsilonModel != null;
//            var epsilonView = new EpsilonView(id, Application.getGameFrame().getMainGamePanel());
//
//            epsilonView.set(
//                    epsilonModel.getX(),
//                    epsilonModel.getY(),
//                    epsilonModel.getWidth(),
//                    epsilonModel.getHeight()
//            );
//        }
//    }

    public static <T extends EntityView> void createEntityView( String id, int x, int y, int width, int height) {
        EntityModel entityModel = findModel(id);

        Class entityViewCls = getEntityViewClass(entityModel);
        if (entityViewCls != null) {
            try {
                Constructor<T> constructor = (Constructor<T>) entityViewCls.getConstructor(String.class);
                var entityView = constructor.newInstance(id);
                entityView.set(x,y,width,height);

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
        }else if (entityModel instanceof EpsilonModel) {
            entityViewCls = EpsilonView.class;
        }
        return entityViewCls;
    }

    public static <T extends AbilityView> void createAbilityView(/*Class<T> tClass,*/ String id, int x, int y) {
        AbilityModel abilityModel = findModel(id);
        Class abilityViewCls = getAbilityViewCls(abilityModel);
        if (abilityViewCls != null) {
            try {
                if (abilityModel instanceof ProjectileModel) {
                    var projectileModel = (ProjectileModel) abilityModel;
                    Constructor<T> constructor = (Constructor<T>) abilityViewCls.getConstructor
                            (String.class, int.class, int.class, Color.class, Color.class);
                    constructor.newInstance(id, x, y, projectileModel.getTopColor(), projectileModel.getBottomColor());
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
            } else view.setEnabled(false);
        } else if (findModel(view.getId()) instanceof AbilityModel) {
            AbilityModel abilityModel = findModel(view.getId());
            if (abilityModel != null) {
                view.set(
                        abilityModel.getX(),
                        abilityModel.getY(),
                        5, 5);
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
}
