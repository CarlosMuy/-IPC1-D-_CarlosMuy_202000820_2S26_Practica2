
package gui;

import modelo.Nave;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaJuego extends JFrame {
    
    private PanelJuego paneljuego;
    
    public VentanaJuego(Nave nave) {
        setTitle("Quetzal Space Defender");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.paneljuego = new PanelJuego(nave);
        add(paneljuego);
        
        this.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            int code = e.getKeyCode();
            
            if (code == java.awt.event.KeyEvent.VK_P || code == java.awt.event.KeyEvent.VK_ESCAPE) {
                paneljuego.alternarPausa();
                return;
            }
            
            if (paneljuego.isEstaPausado()) {
                return;
            }
            
            if (code == java.awt.event.KeyEvent.VK_UP || code == java.awt.event.KeyEvent.VK_W) {
                paneljuego.moverNave(-15);
            } else if (code == java.awt.event.KeyEvent.VK_DOWN || code == java.awt.event.KeyEvent.VK_S) {
                paneljuego.moverNave(15);
            } else if (code == java.awt.event.KeyEvent.VK_SPACE) {
                paneljuego.disparar();
            }
        }
    });
    }
    
}
