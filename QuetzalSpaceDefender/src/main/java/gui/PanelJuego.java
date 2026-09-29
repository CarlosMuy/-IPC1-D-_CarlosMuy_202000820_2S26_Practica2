
package gui;

import hilos.HiloObjetoEspecial;
import hilos.HiloProyectil;
import hilos.TipoObjeto;
import modelo.Nave;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelJuego extends JPanel implements ActionListener {
    
    private Nave naveJugador;
    private int yNave;
    private int xNave = 50;
    
    private boolean bloqueado = false;
    private int puntaje = 0;
    
    private HiloProyectil[] proyectiles;
    private int contadorProyectiles;
    
    private HiloObjetoEspecial[] objetosEspaciales;
    private int contadorObjetos;
    
    private Timer timerRedibujo;
    private Timer timerGenerador;
    
    public PanelJuego(Nave  nave) {
        this.naveJugador = nave;
        this.yNave = 250;
        this.setBackground(Color.BLACK);
        
        this.proyectiles = new HiloProyectil[100];
        this.contadorProyectiles = 0;
        
        this.objetosEspaciales = new HiloObjetoEspecial[100];
        this.contadorObjetos = 0;
        
        this.timerRedibujo = new Timer(16, this);
        this.timerRedibujo.start();
        
        this.timerGenerador = new Timer(1500, e -> generarObjetoAleatorio());
        this.timerGenerador.start();
    }
    
    public void moverNave(int deltaY) {
        if (bloqueado) return;
        int nuevaY = yNave + deltaY;
        if (nuevaY >= 0 && nuevaY <= getHeight() -50) {
            yNave = nuevaY;
        }
    }
    
    public void disparar() {
        if (bloqueado) return;
        if (contadorProyectiles < proyectiles.length) {
            HiloProyectil p = new HiloProyectil(xNave + 40, yNave + 15);
            proyectiles[contadorProyectiles++] = p;
            p.start();
        }
    }
    
    private void generarObjetoAleatorio() {
        if (contadorObjetos >= objetosEspaciales.length) return;
        
        int yAleatoria = (int) (Math.random() * (getHeight() - 50));
        int aleatorio = (int) (Math.random() * 100);
        
        TipoObjeto tipo;
        if (aleatorio < 50) tipo = TipoObjeto.ENEMIGO;
        else if (aleatorio < 70) tipo = TipoObjeto.ASTEROIDE;
        else if (aleatorio < 85) tipo =TipoObjeto.QUAFFLE;
        else tipo = TipoObjeto.SNITCH_ESPECIAL;
        
        HiloObjetoEspecial obj = new HiloObjetoEspecial(getWidth(), yAleatoria, 20, tipo);
        objetosEspaciales[contadorObjetos++] = obj;
        obj.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        verificarColisiones();
        repaint();
    }
    
    private void verificarColisiones() {
        
        for (int i = 0; i < contadorProyectiles; i++) {
            HiloProyectil p = proyectiles[i];
            if (p != null && p.isActivo()) {
                for (int j = 0; j < contadorObjetos; j++) {
                    HiloObjetoEspecial obj = objetosEspaciales[j];
                    if (obj != null && obj.isActivo()) {
                        if (Math.abs(getX() - obj.getX()) < 30 && Math.abs(p.getY() - obj.getY()) < 30) {
                            p.detener();
                            if (obj.getTipo() == TipoObjeto.ENEMIGO) puntaje += 20;
                            else if (obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                                puntaje += 150;
                                destruirEnemigos();
                            }
                        }
                    }
                }
            }
        }
        
        for (int j = 0; j < contadorObjetos; j++) {
            HiloObjetoEspecial obj = objetosEspaciales[j];
            if (obj != null && obj.isActivo()) {
                if (Math.abs(xNave - obj.getX()) < 35 && Math.abs(yNave - obj.getY()) < 35) {
                    obj.detener();
                    if (obj.getTipo() == TipoObjeto.ASTEROIDE) bloquearNave();
                    else if (obj.getTipo() == TipoObjeto.QUAFFLE) puntaje += 10;
                    else if(obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                        puntaje += 150;
                        destruirEnemigos();
                    }
                }
            }
        }
    }
    
    private void bloquearNave() {
        bloqueado = true;
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignore) {}
            bloqueado = false;
        }).start();
    }
    
    private void destruirEnemigos() {
        for (int i = 0; i < contadorObjetos; i++) {
            if (objetosEspaciales[i] != null && objetosEspaciales[i].getTipo() == TipoObjeto.ENEMIGO) {
                objetosEspaciales[i].detener();
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(bloqueado ? Color.GRAY : Color.CYAN);
        g.fillRect(xNave, yNave, 40, 30);
        
        g.setColor(Color.YELLOW);
        for (int i = 0; i < contadorProyectiles; i++) {
            if (proyectiles[i] != null && proyectiles[i].isActivo()) {
                g.fillRect(proyectiles[i].getX(), proyectiles[i].getY(), 10, 4);
            }
        }
        
        for (int i = 0; i < contadorObjetos; i++) {
            HiloObjetoEspecial obj = objetosEspaciales[i];
            if (obj != null && obj.isActivo()) {
                switch (obj.getTipo()) {
                    case ENEMIGO -> { g.setColor(Color.RED); g.fillRect(obj.getX(), obj.getY(), 30 ,30); }
                    case ASTEROIDE -> { g.setColor(Color.ORANGE); g.fillOval(obj.getX(), obj.getY(), 35, 35); }
                    case QUAFFLE -> { g.setColor(Color.MAGENTA); g.fillOval(obj.getX(), obj.getY(), 20, 20); }
                    case SNITCH_ESPECIAL -> {g.setColor(Color.GREEN); g.fillOval(obj.getX(), obj.getY(), 15, 15); }
                }
            }
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Puntaje: " + puntaje, 20, 30);
        if (bloqueado) {
            g.setColor(Color.RED);
            g.drawString("¡BLOQUEADO Por Asteroide!", 20, 60);
        }
    }
}
