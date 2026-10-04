
package gui;

import modelo.Nave;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class VentanaJuego extends JFrame {
    
    private PanelJuego paneljuego;
    
    public VentanaJuego(Nave nave) {
        setTitle("Quetzal Space Defender");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.paneljuego = new PanelJuego(nave);
        this.add(this.paneljuego);
        
        this.pack();
        this.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> {
        this.paneljuego.requestFocusInWindow();
    });
   }     
}    
