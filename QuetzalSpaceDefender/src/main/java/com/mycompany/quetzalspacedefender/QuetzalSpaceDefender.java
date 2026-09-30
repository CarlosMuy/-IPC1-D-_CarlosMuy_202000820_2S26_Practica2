
package com.mycompany.quetzalspacedefender;

import gui.VentanaMenu;

import javax.swing.SwingUtilities;

public class QuetzalSpaceDefender {
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
        
          VentanaMenu menu = new VentanaMenu();
          menu.setVisible(true);
      });
    }
}
