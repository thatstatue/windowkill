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

import static org.windowkillproject.Request.*;

public abstract class GlobeModel {
    private ArrayList<EpsilonModel> team1 = new ArrayList<>();

    public ArrayList<EpsilonModel> getTeam1() {
        return team1;
    }

    public String getId() {
        return id;
    }

    private final String id;

    public ArrayList<EpsilonModel> getEpsilons() {
        return new ArrayList<>(getTeam1());
    }
    public ArrayList<CollectableModel> getCollectableModels() {
        return collectableModels;
    }

    private ArrayList<ObjectModel> objectModels;
    private ArrayList<EntityModel> entityModels;
    private ArrayList<AbilityModel> abilityModels;
    private ArrayList<ProjectileModel> projectileModels;
    private ArrayList<OmenoctModel> omenoctModels;
    private ArrayList<BlackOrbModel> blackOrbModels;
    private ArrayList<BarricadosModel> barricadosModels;

    public ArrayList<AbilityModel> getAbilityModels() {
//        System.out.println("________________________");
//        for (AbilityModel ability : abilityModels){
//            System.out.println(" hi im "+ ability.getId() + "  "+ ability.getClass());
//        }
//        System.out.println("________________________");
        return abilityModels;
    }
    private GlobeController globeController;

    public GlobeController getGlobeController() {
        return globeController;
    }

    private ArrayList<CollectableModel> collectableModels;
    private ArrayList<ArchmireModel> archmireModels;
    private ArrayList<BulletModel> bulletModels;
    private SmileyHeadModel smileyHeadModel;

    public SmileyHeadModel getSmileyHeadModel() {
        return smileyHeadModel;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    private ArrayList<PanelModel> panelModels;
    public MainPanelModel mainPanelModel;
    private WaveFactory waveFactory;
    private GameLoop gameLoop;
    private GameManager gameManager;
    private ElapsedTime elapsedTime;

    public GameManager getGameManager() {
        return gameManager;
    }

    private int killedEnemiesInWave;
    private int killedEnemiesTotal;
    public void pause(boolean force){
        getGameLoop().stop();
        getWaveFactory().setForceStop(force);
        getWaveFactory().waveTimer.stop();
        getElapsedTime().pause();
    }

    public int getKilledEnemiesInWave() {
        return killedEnemiesInWave;
    }
    public void addKilledEnemiesInWave() {
        setKilledEnemiesInWave(getKilledEnemiesInWave() + 1);
    }
    public void resetKilledEnemiesInWave() {
        setKilledEnemiesInWave(0);
    }

    public int getKilledEnemiesTotal() {
        return killedEnemiesTotal;
    }

    public ElapsedTime getElapsedTime() {
        return elapsedTime;
    }

    public void addKilledEnemiesTotal() {
        setKilledEnemiesTotal(getKilledEnemiesTotal() + 1);
    }
    public void resetKilledEnemiesTotal(){
        setKilledEnemiesTotal(0);
    }

    public MainPanelModel getMainPanelModel() {
        return mainPanelModel;
    }

    public WaveFactory getWaveFactory() {
        return waveFactory;
    }


    public GlobeModel(String id, EpsilonModel epsilon1) {
        this.id =id;
        setGlobeController(new GlobeController(id));
        getTeam1().add(epsilon1);
        setObjectModels(new ArrayList<>());
        setEntityModels(new ArrayList<>());
        setAbilityModels(new ArrayList<>());
        setPanelModels(new ArrayList<>());

        setArchmireModels(new ArrayList<>());
        setBulletModels(new ArrayList<>());
        setCollectableModels(new ArrayList<>());

        setBlackOrbModels(new ArrayList<>());
        setOmenoctModels(new ArrayList<>());
        setBarricadosModels(new ArrayList<>());
        setProjectileModels(new ArrayList<>());

//        mainPanelModel = new MainPanelModel(id, id);

        setWaveFactory(new WaveFactory(id));
        setGameLoop(new GameLoop(id));
        setGameManager(new GameManager(id));
        setElapsedTime(new ElapsedTime(id));

    }
    public void initProperties(){
//        mainPanelModel = new MainPanelModel(this);
        setSmileyHeadModel(new SmileyHeadModel(id));
        getSmileyHeadModel().initHands();
        //shrinkFast();
    }

    public void performAction(String message) {

        for (EpsilonModel epsilonModel : getTeam1()) {
            epsilonModel.getMessageQueue().enqueue(message);
        }
    }
    public void broadcast(String message){
        getTeam1().getFirst().getMessageQueue().enqueue(BROADCAST_REDIRECT+ REGEX_SPLIT + getId() + REGEX_SPLIT +message);
    }

    public void shrinkAll(){
        for (PanelModel panelModel : getPanelModels())
            panelModel.shrink();
    }
    public boolean isExploding() {
        return getMainPanelModel().isExploding();
    }

    public void shrinkFast() {
        getMainPanelModel().shrinkFast();
    }


    public void stretch(PanelModel panel, int code) {
        panel.stretch(code);

    }
    public void endingScene() {
        getMainPanelModel().endingScene();
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
//        System.out.println("________________________");
//        for (EntityModel entityModel : entityModels){
//            System.out.println(" hi im "+ entityModel.getId() + "  "+ entityModel.getClass());
//        }
//        System.out.println("________________________");
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


    public void setTeam1(ArrayList<EpsilonModel> team1) {
        this.team1 = team1;
    }

    public void setAbilityModels(ArrayList<AbilityModel> abilityModels) {
        this.abilityModels = abilityModels;
    }

    public ArrayList<ProjectileModel> getProjectileModels() {
        return projectileModels;
    }

    public void setProjectileModels(ArrayList<ProjectileModel> projectileModels) {
        this.projectileModels = projectileModels;
    }

    public ArrayList<OmenoctModel> getOmenoctModels() {
        return omenoctModels;
    }

    public void setOmenoctModels(ArrayList<OmenoctModel> omenoctModels) {
        this.omenoctModels = omenoctModels;
    }

    public ArrayList<BlackOrbModel> getBlackOrbModels() {
        return blackOrbModels;
    }

    public void setBlackOrbModels(ArrayList<BlackOrbModel> blackOrbModels) {
        this.blackOrbModels = blackOrbModels;
    }

    public ArrayList<BarricadosModel> getBarricadosModels() {
        return barricadosModels;
    }

    public void setBarricadosModels(ArrayList<BarricadosModel> barricadosModels) {
        this.barricadosModels = barricadosModels;
    }

    public void setGlobeController(GlobeController globeController) {
        this.globeController = globeController;
    }

    public void setCollectableModels(ArrayList<CollectableModel> collectableModels) {
        this.collectableModels = collectableModels;
    }

    public void setSmileyHeadModel(SmileyHeadModel smileyHeadModel) {
        this.smileyHeadModel = smileyHeadModel;
    }


    public void setWaveFactory(WaveFactory waveFactory) {
        this.waveFactory = waveFactory;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setElapsedTime(ElapsedTime elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setKilledEnemiesInWave(int killedEnemiesInWave) {
        this.killedEnemiesInWave = killedEnemiesInWave;
    }

    public void setKilledEnemiesTotal(int killedEnemiesTotal) {
        this.killedEnemiesTotal = killedEnemiesTotal;
    }
}
