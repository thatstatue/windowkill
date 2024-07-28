package org.windowkillproject.client.ui.panels.game;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.Wave;
import org.windowkillproject.server.model.Writ;
import org.windowkillproject.server.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.abs;
import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.REQ_EPSILON_XP;
import static org.windowkillproject.Request.REQ_WRIT_CHOSEN;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.server.model.entities.enemies.EnemyModel.getKilledEnemiesTotal;

public class MainGamePanel extends GamePanel {
    private final JLabel clock = new JLabel("0:00");
    private final JLabel xp = new JLabel("✦0");
    private final JLabel hp = new JLabel("100 ♡");
    private final JLabel wave = new JLabel("~1");
    private final JLabel writ = new JLabel("");
    private final JLabel ability = new JLabel("");

    private MainGamePanel mainGamePanel;
    public MainGamePanel newInstance(GameClient client){
        gamePanels.remove(mainGamePanel);
        gamePanelsBounds.remove(mainGamePanel);
        if (mainGamePanel!=null) mainGamePanel.setEnabled(false);
        mainGamePanel = new MainGamePanel(client);
        return mainGamePanel;
    }
    public MainGamePanel getInstance(){
        return mainGamePanel;
    }


    private MainGamePanel(GameClient client) {
        super(PanelStatus.shrinkable, true, client);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setLocation((getDefaultToolkit().getScreenSize().width - getWidth())/2,
                (getDefaultToolkit().getScreenSize().height - getHeight())/2);
    }


    public JLabel[] getLabels() {
        client.sendMessage(REQ_EPSILON_XP);
        client.sendMessage(REQ_WRIT_CHOSEN);
        return new JLabel[]{new JLabel(""), clock, xp
                , new JLabel(String.valueOf(getKilledEnemiesTotal())), wave, writ};
    }


    public void setClockTime(String time) {
        clock.setText(time);
    }

    public void setWaveLevel(int level) {
        wave.setText("~" + level);
    }

    public void setXpAmount(int xp) { this.xp.setText("✦" + xp);
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

        wave.setText("~" +Wave.getLevel());
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

    public boolean isPunched() {
        return punched;
    }

    public void gotPunched(Point2D punchPoint){
        var rect = gamePanelsBounds.get(this);
        if (rect == null) rect = getBounds();
        double rightD = punchPoint.getX()- (rect.x + rect.width/2D); // if positive, is hitting the right
        double bottomD = punchPoint.getY()- (rect.y + rect.height/2D); // if positive, is hitting the bottom

        int newX = getX();
        int newY = getY();
        int newWidth = getWidth();
        int newHeight = getHeight();

        if (abs(rightD)< abs(bottomD)){
            newWidth -= 2*FRAME_SHRINKAGE_SPEED;
            if (rightD<0)
                newX += FRAME_SHRINKAGE_SPEED;
        }else{
            newHeight -= 2*FRAME_SHRINKAGE_SPEED;
            if (bottomD<0)
                newY += FRAME_SHRINKAGE_SPEED;
        }
        this.setBounds(newX, newY, newWidth, newHeight);
        gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));
        punched = true;
    }
    private boolean punched;
    private int epsilonRadius= EPSILON_RADIUS;

    public int getEpsilonRadius() {
        return epsilonRadius;
    }

    public void setEpsilonRadius(int epsilonRadius) {
        this.epsilonRadius = epsilonRadius;
    }
}
