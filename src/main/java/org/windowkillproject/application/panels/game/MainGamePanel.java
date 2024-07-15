package org.windowkillproject.application.panels.game;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesInWave;

public class MainGamePanel extends GamePanel {
    private final JLabel clock = new JLabel("0:00");
    private final JLabel xp = new JLabel("✦0");
    private final JLabel hp = new JLabel("100 ♡");
    private final JLabel wave = new JLabel("~1");
//todo currentAbility and ON writ to be added
    private static MainGamePanel mainGamePanel;
    public static MainGamePanel getInstance(){
        if (mainGamePanel==null) mainGamePanel = new MainGamePanel();
        return mainGamePanel;
    }
    private MainGamePanel() {
        super(PanelStatus.shrinkable, true);
        setPreferredSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        setSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        setLocation((getDefaultToolkit().getScreenSize().width - getWidth())/2,
                (getDefaultToolkit().getScreenSize().height - getHeight())/2);
    }


    public JLabel[] getLabels() {
        return new JLabel[]{new JLabel(""), clock, xp
                , new JLabel(String.valueOf(getKilledEnemiesInWave())), wave};
    }


    public void setClockTime(String time) {
        clock.setText(time);
    }

    public void setWaveLevel(int level) {
        wave.setText("~" + level);
    }

    public void setXpAmount(int xp) {
        this.xp.setText("✦" + xp);
    }

    public void setHpAmount(int hp) {
        this.hp.setText(hp + " ♡");
    }

    public void initLabels() {
        clock.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        clock.setForeground(Color.white);
        clock.setBounds(3, 1, 300, 20);
        this.add(clock);

        xp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        xp.setForeground(Color.cyan);
        xp.setBounds(60, 1, 300, 20);
        xp.setText(""+ EpsilonModel.getINSTANCE().getXp());
        this.add(xp);

        hp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        hp.setForeground(Color.green);
        hp.setBounds(180, 1, 300, 20);
        this.add(hp);

        wave.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        wave.setForeground(Color.red);
        wave.setBounds(140, 1, 300, 20);
        this.add(wave);

    }

}
