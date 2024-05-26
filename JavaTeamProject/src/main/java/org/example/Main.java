package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Server server=new Server();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                org.example.GUI.GUIPlay();
            }
        });

    }
}
