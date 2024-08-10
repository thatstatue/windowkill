package org.windowkillproject.client.ui.panels.game;

import org.windowkillproject.client.GameClient;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;

public class MainPanelView extends PanelView {
    private final JLabel clock = new JLabel("0:00");
    private final JLabel xp = new JLabel("✦0");
    private final JLabel hp = new JLabel("100 ♡");
    private final JLabel wave = new JLabel("~1");
    private final JLabel writ = new JLabel("");
    private final JLabel kills = new JLabel("");

    public MainPanelView(String id, GameClient client){
        super(id, client);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setLocation((getDefaultToolkit().getScreenSize().width - getWidth())/2,
                (getDefaultToolkit().getScreenSize().height - getHeight())/2);
//        setEnabled(false);
    }


    private String waveLevel = "1";

    public void setTotalKills(String totalKills) {
        kills.setText(totalKills);
    }

    public void setWaveLevel(String waveLevel) {
        this.waveLevel = waveLevel;
    }

    public JLabel[] getLabels() {
        client.sendMessage(REQ_GET_EPSILON_XP);
        client.sendMessage(REQ_WRIT_CHOSEN);
        client.sendMessage(REQ_TOTAL_KILLS);
        client.sendMessage(REQ_WAVE_LEVEL);
        return new JLabel[]{new JLabel(""), clock, xp
                , kills, wave, writ};
    }


    public void setClockTime(String time) {
        clock.setText(time);
    }

    public void setWaveLevel(int level) {
        wave.setText("~" + level);
    }

    public void setXpAmount(int xp) { this.xp.setText("✦" + xp);}
    public int getXpAmount(){
        return Integer.parseInt(xp.getText().substring(1));
    }

    public void setHpAmount(int hp) {
        this.hp.setText(hp + " ♡");
    }
    public void setWrit(String chosenSkill){ this.writ.setText("^"+ chosenSkill);}

    public void initLabels() {
        clock.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        clock.setForeground(Color.white);
        clock.setBounds(3, 1, 300, 20);
        this.add(clock);

        xp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        xp.setForeground(Color.cyan);
        xp.setBounds(60, 1, 300, 20);
        xp.setText(""+ 0);
        this.add(xp);

        hp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        hp.setForeground(Color.green);
        hp.setBounds(180, 1, 300, 20);
        this.add(hp);

        wave.setText("~" +waveLevel);
        wave.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        wave.setForeground(Color.red);
        wave.setBounds(140, 1, 300, 20);
        this.add(wave);

        writ.setText("^null");
        writ.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        writ.setForeground(Color.yellow);
        writ.setBounds(1, 50, 300, 20);
        this.add(writ);

    }


}
