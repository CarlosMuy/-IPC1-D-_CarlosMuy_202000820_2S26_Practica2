
package com.mycompany.quetzalspacedefender;

import gui.VentanaJuego;

import javax.swing.SwingUtilities;

public class QuetzalSpaceDefender {
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
        
          VentanaJuego ventana = new VentanaJuego(null);
          ventana.setVisible(true);
      });
    }
}
