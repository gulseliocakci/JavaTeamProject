package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                org.example.GUI.GUIPlay();
            }
        });

    }
}
