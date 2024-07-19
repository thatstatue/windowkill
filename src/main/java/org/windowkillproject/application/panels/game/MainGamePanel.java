package org.windowkillproject.application.panels.game;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.Wave;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.abilities.PortalModel;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.abs;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesTotal;

public class MainGamePanel extends GamePanel {
    private final JLabel clock = new JLabel("0:00");
    private final JLabel xp = new JLabel("✦0");
    private final JLabel hp = new JLabel("100 ♡");
    private final JLabel wave = new JLabel("~1");
    private final JLabel writ = new JLabel("");
    private final JLabel ability = new JLabel("");


    private static MainGamePanel mainGamePanel;
    public static MainGamePanel newInstance(){
        gamePanels.remove(mainGamePanel);
        gamePanelsBounds.remove(mainGamePanel);
        if (mainGamePanel!=null) mainGamePanel.setEnabled(false);
        mainGamePanel = new MainGamePanel();
        return mainGamePanel;
    }
    public static MainGamePanel getInstance(){
        if (mainGamePanel==null) return newInstance();
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
                , new JLabel(String.valueOf(getKilledEnemiesTotal())), wave, writ};
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

        wave.setText("~" +Wave.getLevel());
        wave.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        wave.setForeground(Color.red);
        wave.setBounds(140, 1, 300, 20);
        this.add(wave);

        writ.setText("^" + Writ.getChosenSkill());
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

}
