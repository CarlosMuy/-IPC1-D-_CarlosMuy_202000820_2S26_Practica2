
package gui;

import hilos.HiloObjetoEspecial;
import hilos.HiloProyectil;
import hilos.TipoObjeto;
import modelo.Nave;
import modelo.Partida;
import persistencia.ControladorPersistencia;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.Rectangle;

public class PanelJuego extends JPanel implements ActionListener {
    
    private boolean estaPausado = false;
    
    private final Nave naveJugador;
    private int yNave;
    private int xNave = 50;
    
    private boolean bloqueado = false;
    private int puntaje = 0;
    
    private final HiloProyectil[] proyectiles;
    private int contadorProyectiles;
    
    private final HiloObjetoEspecial[] objetosEspaciales;
    private int contadorObjetos;
    
    private final Timer timerRedibujo;
    private final Timer timerGenerador;
    
    public PanelJuego(Nave  nave) {
        this.naveJugador = nave;
        this.yNave = 250;
        this.setBackground(Color.BLACK);
        
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        this.proyectiles = new HiloProyectil[100];
        this.contadorProyectiles = 0;
        
        this.objetosEspaciales = new HiloObjetoEspecial[100];
        this.contadorObjetos = 0;
        
        this.timerRedibujo = new Timer(16, e -> {
            if (!estaPausado) {
                verificarColisiones();
            }
            repaint();
        });
        this.timerRedibujo.start();
        
        this.timerGenerador = new Timer(1500, e -> generarObjetoAleatorio());
        this.timerGenerador.start();
        
        this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            int clave = e.getKeyCode();
   
               if (clave == KeyEvent.VK_P) {
                   alternarPausa();
                   return;
               }
               if (clave == KeyEvent.VK_ESCAPE) {
                   if (!estaPausado) {
                       alternarPausa();
                   }
                Window parentWindow = SwingUtilities.getWindowAncestor(PanelJuego.this);
                int opcion = JOptionPane.showConfirmDialog(parentWindow, "¿Deseas salir de la partida actual?\nSe guardará tu puntaje actual (" + puntaje + " pts)." , "Salir del juego", JOptionPane.YES_NO_OPTION);
                 
                if (opcion == JOptionPane.YES_OPTION) {
                    finalizarPartida("Partida guardada exitosamente.");
                } else {
                    if (estaPausado) {
                        alternarPausa();
                    }
                    requestFocusInWindow();
                }
                return;
            }
               if (estaPausado) {
                   return;
               }
            
            int anchoPanel = getWidth();
            int altoPanel = getHeight();

            int limiteDerecho = Math.max(0, anchoPanel -50);
            int limiteInferior = Math.max(0, altoPanel - 40);

            switch (clave) {
                case KeyEvent.VK_UP, KeyEvent.VK_W -> yNave = Math.max(0, yNave -15);
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> yNave = Math.min(limiteInferior, yNave + 15);
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> xNave = Math.max(0, xNave + 15);
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> xNave = Math.min(limiteDerecho, xNave -15);
                case KeyEvent.VK_SPACE -> disparar();
                default -> {}
            }
            }
    });
    }
    
    public int getPuntaje() {
        return this.puntaje;
    }
    
    public void moverNave(int deltaY) {
        if (bloqueado) return;
        int nuevaY = yNave + deltaY;
        if (nuevaY >= 0 && nuevaY <= getHeight() -50) {
            yNave = nuevaY;
        }
    }
    
    public void alternarPausa() {
        estaPausado = !estaPausado;
        
        if (estaPausado) {
            if (timerGenerador != null) timerGenerador.stop();
        } else {
            if (timerGenerador != null) timerGenerador.start();
        }
        repaint();
    }
    
    public void disparar() {
        if (contadorProyectiles < proyectiles.length) {
            HiloProyectil nuevoProyectil = new HiloProyectil(xNave + 40, yNave + 15);
            proyectiles[contadorProyectiles] = nuevoProyectil;
            contadorProyectiles++;
            
            nuevoProyectil.start();
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
                Rectangle rectProyectil = new Rectangle(p.getX(), p.getY(), 10, 4);
                
                for (int j = 0; j < contadorObjetos; j++) {
                    HiloObjetoEspecial obj = objetosEspaciales[j];
                    if (obj != null && obj.isActivo()) {
                        
                        int ancho = 30, alto = 30;
                        if (obj.getTipo() == TipoObjeto.ASTEROIDE) {
                            ancho = 35; alto = 35;
                        } else if (obj.getTipo() == TipoObjeto.QUAFFLE) {
                            ancho = 20; alto = 20;
                        } else if (obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                            ancho = 15; alto = 15;
                        }
                        
                        Rectangle rectObjeto = new Rectangle(obj.getX(), obj.getY(), ancho, alto);
                        if (rectProyectil.intersects(rectObjeto)) {
                            p.detener();
                            obj.detener();
                            proyectiles[i] = null;
                            objetosEspaciales[j] = null;
                            
                            if (obj.getTipo() == TipoObjeto.ENEMIGO) {
                                puntaje += 20;
                            } else if (obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                                puntaje += 150;
                                destruirTodosLosEnemigosPantalla();
                            } 
                            break; 
                            }
                        }
                    }
                }
            }
            Rectangle rectNave = new Rectangle(xNave, yNave, 40, 30);
            for (int j = 0; j < contadorObjetos; j++) {
                HiloObjetoEspecial obj = objetosEspaciales[j];
                if (obj != null && obj.isActivo()) {
                    
                    int ancho = 30;
                    int alto = 30;
                    if (obj.getTipo() == TipoObjeto.ASTEROIDE) { 
                        ancho = 35; alto = 35; }
                    else if (obj.getTipo() == TipoObjeto.QUAFFLE) {
                        ancho = 20; alto = 20; }
                    else if (obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                        ancho = 15; alto = 15; }
                    
                    Rectangle rectObjeto = new Rectangle(obj.getX(), obj.getY(), ancho, alto);
                    
                    if (rectNave.intersects(rectObjeto)) {
                        obj.detener();
                        objetosEspaciales[j] = null;
                        
                        if (obj.getTipo() == TipoObjeto.ENEMIGO) {
                            finalizarPartida("¡Game Over! Has chocado con un enemigo.");
                            return;
                        } else if (obj.getTipo() == TipoObjeto.SNITCH_ESPECIAL) {
                            puntaje += 150;
                            destruirTodosLosEnemigosPantalla();
                        } else if (obj.getTipo() == TipoObjeto.ASTEROIDE) {
                            bloquearNave();
                        } else if (obj.getTipo() == TipoObjeto.QUAFFLE) {
                            puntaje += 10;
                        }
                    }
                    
                    if (obj.getTipo() == TipoObjeto.ENEMIGO && obj.getX() <= 0) {
                        obj.detener();
                        objetosEspaciales[j] = null;
                        finalizarPartida("¡Game Over! Un enemigo ha logrado escapar.");
                        return;
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
    
    public void finalizarPartida(String mensaje) {
        if (timerRedibujo != null) timerRedibujo.stop();
        if (timerGenerador != null) timerGenerador.stop();
        
        guardarPuntajeFinal();
        
        javax.swing.JOptionPane.showMessageDialog(this, mensaje + "\nPuntaje total: " + puntaje, "Fin del Juego", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        new gui.VentanaMenu().setVisible(true);
        
        java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (ventana != null) {
            ventana.dispose();
        }
    }
    private void destruirTodosLosEnemigosPantalla() {
        for (int j = 0; j < contadorObjetos; j++) {
            HiloObjetoEspecial obj = objetosEspaciales[j];
            if (obj != null && obj.isActivo() && obj.getTipo() == TipoObjeto.ENEMIGO) {
                obj.detener();
                puntaje += 20;
            }
        }
    }
    
    
    public void guardarPuntajeFinal() {
        if (naveJugador != null) {
            Partida partida = new Partida(naveJugador.getPiloto(), naveJugador.getDificultad(), puntaje);
            ControladorPersistencia pers = new ControladorPersistencia(100, 100);
            pers.registrarPartida(partida);
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
        
        if (estaPausado) {
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            String msg = "JUEGO EN PAUSA";
            int anchoTexto = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - anchoTexto) / 2, getHeight() / 2 - 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            String subMsg = "Presiona ´P´ para Reanudar";
            int anchoSub = g2d.getFontMetrics().stringWidth(subMsg);
            g2d.drawString(subMsg, (getWidth() - anchoSub) / 2, getHeight() / 2 + 20);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Puntaje: " + puntaje, 20, 30);
        if (bloqueado) {
            g.setColor(Color.RED);
            g.drawString("¡BLOQUEADO Por Asteroide!", 20, 60);
        }
    }
    
    public boolean isEstaPausado() {
        return estaPausado;
    }
}
