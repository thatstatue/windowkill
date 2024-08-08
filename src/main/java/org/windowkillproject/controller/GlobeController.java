package org.windowkillproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.game.MainPanelView;
import org.windowkillproject.client.ui.panels.game.PanelView;
import org.windowkillproject.client.view.Viewable;
import org.windowkillproject.client.view.abilities.*;
import org.windowkillproject.client.view.entities.enemies.EnemyView;
import org.windowkillproject.client.view.entities.enemies.normals.*;
import org.windowkillproject.json.JacksonMapper;
import org.windowkillproject.server.connections.Database;
import org.windowkillproject.server.connections.online.OnlinePlayer;
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
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.MainPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;


import static org.windowkillproject.Request.*;


public class GlobeController {
    private final GlobeModel globeModel;

    public GlobeController(GlobeModel globeModel) {
        this.globeModel = globeModel;
    }

    public void createEntityView(String id, int x, int y, int width, int height) {
        EntityModel entityModel = findModel(id);

        int entityViewClsIndex = getEntityViewClassIndex(entityModel);
        if (entityViewClsIndex != -1) {
            try {
                if (entityModel instanceof EnemyModel enemyModel) {
                    System.out.println("im broadcasting enemy model "+ id);
                    String polygonJson = JacksonMapper.getInstance().writeValueAsString(enemyModel.getPolygon());
                    globeModel.broadcast(REQ_CREATE_ENTITY + REGEX_SPLIT + id + REGEX_SPLIT +
                            x + REGEX_SPLIT + y + REGEX_SPLIT + width + REGEX_SPLIT + height +
                            REGEX_SPLIT +  polygonJson + REGEX_SPLIT +entityViewClsIndex);

                } else if (entityModel instanceof EpsilonModel) {
                    globeModel.broadcast(REQ_CREATE_ENTITY + REGEX_SPLIT + id + REGEX_SPLIT +
                            x + REGEX_SPLIT + y + REGEX_SPLIT + width + REGEX_SPLIT + height + REGEX_SPLIT + entityViewClsIndex);
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createPanelView(String id, int x, int y, int width, int height) {
        var panelModel = findModel(id);
        if (panelModel instanceof PanelModel) {
            boolean isMain = panelModel instanceof MainPanelModel;
            globeModel.broadcast(REQ_CREATE_PANEL + REGEX_SPLIT + id + REGEX_SPLIT + isMain + REGEX_SPLIT +
                    x + REGEX_SPLIT + y + REGEX_SPLIT + width + REGEX_SPLIT + height);
        }
    }

    private static int getEntityViewClassIndex(EntityModel entityModel) {
        if (entityModel instanceof TrigorathModel) {
            return 0;
        } else if (entityModel instanceof SquarantineModel) {
            return 1;
        } else if (entityModel instanceof NecropickModel) {
            return 2;
        } else if (entityModel instanceof OmenoctModel) {
            return 3;
        } else if (entityModel instanceof WyrmModel) {
            return 4;
        } else if (entityModel instanceof EpsilonModel) {
            return 5;
        } else if (entityModel instanceof ArchmireModel) {
            return 6;
        } else if (entityModel instanceof BarricadosModel) {
            return 7;
        } else if (entityModel instanceof BlackOrbModel) {
            return 8;
        } else if (entityModel instanceof SmileyHeadModel) {
            return 9;
        } else if (entityModel instanceof RightHandModel) {
            return 10;
        } else if (entityModel instanceof PunchFistModel) {
            return 11;
        } else if (entityModel instanceof LeftHandModel) {
            return 12;
        }
        return -1;
    }

    public  void createAbilityView(String id, int x, int y) {
        AbilityModel abilityModel = findModel(id);
        int abilityViewClsIndex = getAbilityViewClsIndex(abilityModel);
        if (abilityViewClsIndex != -1) {
            if (abilityModel instanceof MomentModel momentModel) {
                globeModel.broadcast(REQ_CREATE_ABILITY + REGEX_SPLIT + id + REGEX_SPLIT +
                        x + REGEX_SPLIT + y + REGEX_SPLIT + momentModel.getRadius() + REGEX_SPLIT + abilityViewClsIndex);
            } else {
                globeModel.broadcast(REQ_CREATE_ABILITY + REGEX_SPLIT + id + REGEX_SPLIT +
                        x + REGEX_SPLIT + y + REGEX_SPLIT + abilityViewClsIndex);
            }

        }
    }

    private static int getAbilityViewClsIndex(AbilityModel abilityModel) {
        if (abilityModel instanceof BulletModel) {
            return 0;
        } else if (abilityModel instanceof CollectableModel) {
            return 1;
        } else if (abilityModel instanceof VertexModel) {
            return 2;
        } else if (abilityModel instanceof ProjectileModel) {
            return 3;
        } else if (abilityModel instanceof MomentModel) {
            return 4;
        } else if (abilityModel instanceof PortalModel) {
            return 5;
        }
        return -1;
    }

    public <T extends Drawable> void setViewBounds(T model) throws JsonProcessingException {
        // brod + globeId+ req + id + x + y + w + h + polygon/null + boolvisible?
        String message = REQ_MODIFY_OBJECT + REGEX_SPLIT + model.getId() + REGEX_SPLIT +
                model.getX() + REGEX_SPLIT +
                model.getY() + REGEX_SPLIT +
                model.getWidth() + REGEX_SPLIT +
                model.getHeight();
        if (model instanceof EntityModel) {
            if (model instanceof EnemyModel enemyModel) {
                String json = JacksonMapper.getInstance().writeValueAsString(enemyModel);
                message += REGEX_SPLIT + json;
            } else {
                message += REGEX_SPLIT + "null";
            }
            if (model instanceof Hideable) {
                boolean visible = ((Hideable) model).isVisible();
                message += REGEX_SPLIT + visible;
            }
        }
        globeModel.broadcast(message);
    }

    public <T extends Drawable> T findModel(String id) {
        for (EntityModel entityModel : globeModel.getEntityModels()) {
            if (entityModel.getId().equals(id)) return (T) entityModel;
        }
        for (int i = 0; i < globeModel.getAbilityModels().size(); i++) {
            AbilityModel abilityModel = globeModel.getAbilityModels().get(i);
            if (abilityModel.getId().equals(id)) return (T) abilityModel;
        }
        for (int i = 0; i < globeModel.getPanelModels().size(); i++) {
            PanelModel panelModel = globeModel.getPanelModels().get(i);
            if (panelModel.getId().equals(id)) return (T) panelModel;
        }

        return null;
    }

}
