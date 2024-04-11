package org.windowkillproject;

import org.windowkillproject.application.Application;
import org.windowkillproject.controller.Update;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       // SwingUtilities.invokeLater(Update::new);
        new Application().run();
    }
}