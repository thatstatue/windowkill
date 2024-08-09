package org.windowkillproject.client.ui.panels.etc;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;

public class LeaguePanel extends Panel {
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    public LeaguePanel(GameClient client) {
        super(client);
//        client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT+REQ_SQUADS_LIST);

        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));

        scrollPane = new JScrollPane(createItemTable());
        scrollPane.setBounds(50, 100, 300, 300);

        add(scrollPane);
    }
    private ArrayList<String> squadNames = new ArrayList<>();
    private ArrayList<String> occupants = new ArrayList<>();

    public void setOccupants(String[] occupants){
        for (int i = 1; i< occupants.length;i++){
            this.occupants.add(occupants[i]);
        }
        this.occupants.add("22");this.occupants.add("22");this.occupants.add("22");this.occupants.add("22");
        renew();
    }

    public void setSquadNames(String[] squadNames){
        for (int i = 1; i< squadNames.length;i++){
            this.squadNames.add(squadNames[i]);
        }
        this.squadNames.add("DUMMy");this.squadNames.add("DUMMy");this.squadNames.add("DUMMy");this.squadNames.add("DUMMy");

    }
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();

        JLabel name = jLabelMaker("LEAGUE", 50, 20, 200, 50);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        name.setForeground(BUTTON_BG_COLOR);
        componentArrayList.add(name);

        componentArrayList.add(buttonMaker("Menu", 790, 20, e -> client.getApp().initPFrame()));

        return componentArrayList;
    }
    private JTable createItemTable() {
        String[] columnNames = {"Squad Name", "Occupants"};
        tableModel = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < squadNames.size(); i++) {
            String item = squadNames.get(i);
            String amount = occupants.get(i);
            tableModel.addRow(new Object[]{item, amount});
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        return table;
    }

    public void renew(){
        System.out.println("renew begin at "+ tableModel.getRowCount());
        for (int i = tableModel.getRowCount(); i>0; i--){
            tableModel.removeRow(i-1);
        }

        scrollPane = new JScrollPane(createItemTable());
        scrollPane.revalidate();
        scrollPane.repaint();
        System.out.println("renew end at "+ tableModel.getRowCount());

    }
}