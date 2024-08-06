package org.windowkillproject.server.model.globe;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.GameLoop;
import org.windowkillproject.controller.GameManager;
import org.windowkillproject.controller.GlobeController;
import org.windowkillproject.server.model.ObjectModel;
import org.windowkillproject.server.model.WaveFactory;
import org.windowkillproject.server.model.abilities.AbilityModel;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.SmileyHeadModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.normals.OmenoctModel;
import org.windowkillproject.server.model.panelmodels.MainPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.abilities.CollectableModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.normals.ArchmireModel;


import java.util.ArrayList;
import java.util.UUID;

public abstract class GlobeModel {
    public ArrayList<EpsilonModel> team1 = new ArrayList<>();

    public ArrayList<EpsilonModel> getTeam1() {
        return team1;
    }

    public String getId() {
        return id;
    }

    private final String id = UUID.randomUUID().toString();

    public ArrayList<EpsilonModel> getEpsilons() {
        return new ArrayList<>(team1);
    }
    public ArrayList<CollectableModel> getCollectableModels() {
        return collectableModels;
    }

    public ArrayList<ObjectModel> objectModels;
    public ArrayList<EntityModel> entityModels;
    public ArrayList<AbilityModel> abilityModels;
    public ArrayList<ProjectileModel> projectileModels;
    public ArrayList<OmenoctModel> omenoctModels;
    public ArrayList<BlackOrbModel> blackOrbModels;
    public ArrayList<BarricadosModel> barricadosModels;

    public ArrayList<AbilityModel> getAbilityModels() {
        return abilityModels;
    }
    private GlobeController globeController;

    public GlobeController getGlobeController() {
        return globeController;
    }

    public ArrayList<CollectableModel> collectableModels;
    public ArrayList<ArchmireModel> archmireModels;
    public ArrayList<BulletModel> bulletModels;
    public SmileyHeadModel smileyHeadModel;

    public SmileyHeadModel getSmileyHeadModel() {
        return smileyHeadModel;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public ArrayList<PanelModel> panelModels;
    public MainPanelModel mainPanelModel;
    public WaveFactory waveFactory;
    public GameLoop gameLoop;
    private GameManager gameManager;
    private ElapsedTime elapsedTime;

    public GameManager getGameManager() {
        return gameManager;
    }

    private int killedEnemiesInWave, killedEnemiesTotal;

    public int getKilledEnemiesInWave() {
        return killedEnemiesInWave;
    }
    public void addKilledEnemiesInWave() {
        killedEnemiesInWave ++;
    }
    public void resetKilledEnemiesInWave() {
        killedEnemiesInWave = 0;
    }

    public int getKilledEnemiesTotal() {
        return killedEnemiesTotal;
    }

    public ElapsedTime getElapsedTime() {
        return elapsedTime;
    }

    public void addKilledEnemiesTotal() {
        killedEnemiesTotal++;
    }
    public void resetKilledEnemiesTotal(){
        killedEnemiesTotal = 0;
    }

    public MainPanelModel getMainPanelModel() {
        return mainPanelModel;
    }

    public WaveFactory getWaveFactory() {
        return waveFactory;
    }

    public GlobeModel(EpsilonModel epsilon1) {
        globeController = new GlobeController(this);
        team1.add(epsilon1);
        objectModels = new ArrayList<>();
        entityModels = new ArrayList<>();
        abilityModels = new ArrayList<>();
        panelModels = new ArrayList<>();
        mainPanelModel = new MainPanelModel(this);
        smileyHeadModel = new SmileyHeadModel(this);
        smileyHeadModel.initHands();
        archmireModels = new ArrayList<>();
        bulletModels = new ArrayList<>();
        collectableModels = new ArrayList<>();

        blackOrbModels = new ArrayList<>();
        omenoctModels = new ArrayList<>();
        barricadosModels = new ArrayList<>();
        projectileModels = new ArrayList<>();

        waveFactory = new WaveFactory(this);
        gameLoop = new GameLoop(this);
        gameManager = new GameManager(this);
        elapsedTime = new ElapsedTime(this);

    }

    public void performAction(String message) {
        for (EpsilonModel epsilonModel : team1) {
            epsilonModel.getMessageQueue().enqueue(message);
        }
    }
    public void shrinkAll(){
        for (PanelModel panelModel :panelModels)
            panelModel.shrink();
    }
    public boolean isExploding() {
        return mainPanelModel.isExploding();
    }

    public void shrinkFast() {
        mainPanelModel.shrinkFast();
    }


    public void stretch(PanelModel panel, int code) {
        panel.stretch(code);

    }
    public void endingScene() {
        mainPanelModel.endingScene();
    }
    public ArrayList<ObjectModel> getObjectModels() {
        return objectModels;
    }

    public void setObjectModels(ArrayList<ObjectModel> objectModels) {
        this.objectModels = objectModels;
    }

    public ArrayList<PanelModel> getPanelModels() {
        return panelModels;
    }

    public void setPanelModels(ArrayList<PanelModel> panelModels) {
        this.panelModels = panelModels;
    }

    public ArrayList<EntityModel> getEntityModels() {
        return entityModels;
    }

    public void setEntityModels(ArrayList<EntityModel> entityModels) {
        this.entityModels = entityModels;
    }

    public ArrayList<ArchmireModel> getArchmireModels() {
        return archmireModels;
    }

    public void setArchmireModels(ArrayList<ArchmireModel> archmireModels) {
        this.archmireModels = archmireModels;
    }

    public ArrayList<BulletModel> getBulletModels() {
        return bulletModels;
    }

    public void setBulletModels(ArrayList<BulletModel> bulletModels) {
        this.bulletModels = bulletModels;
    }


}
