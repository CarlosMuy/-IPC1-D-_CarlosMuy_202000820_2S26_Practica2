
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
        
        addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
                paneljuego.moverNave(-15);
            } else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                paneljuego.moverNave(15);
            } else if (code == KeyEvent.VK_SPACE) {
                paneljuego.disparar();
            }
        }
    });
    }
    
}
