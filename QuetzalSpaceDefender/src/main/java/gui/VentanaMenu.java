
package gui;

import modelo.Dificultad;
import modelo.Nave;
import modelo.Piloto;
        
import javax.swing.*;
import java.awt.*;

public class VentanaMenu extends JFrame {
    
    public VentanaMenu() {
        setTitle("Quetzal Space Defender - Menú Principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        
        JLabel lblTitulo = new JLabel("Quetzal Space Defender", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton btnJugar = new JButton("1. Jugar");
        JButton btnCrearPiloto = new JButton("2. Crear / Seleccionar Piloto");
        JButton btnTopPuntajes = new JButton("3. Top Puntajes");
        JButton btnSalir = new JButton("4. Salir");
        
        add(lblTitulo);
        add(btnJugar);
        add(btnCrearPiloto);
        add(btnTopPuntajes);
        add(btnSalir);
        
        btnJugar.addActionListener(e -> iniciarJuego());
        btnCrearPiloto.addActionListener(e -> registrarPiloto());
        btnTopPuntajes.addActionListener(e -> JOptionPane.showInternalMessageDialog(this, "SSección Top Puntajes", "Ranking", JOptionPane.INFORMATION_MESSAGE));
        btnSalir.addActionListener(e -> System.exit(0));
    }
    
    private Piloto pilotoActual = null;
    private Nave naveActual = null;
    
    private void registrarPiloto() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del Piloto:");
        if (nombre == null || nombre.isEmpty()) return;
        
        pilotoActual = new Piloto("101", nombre);
        
        String[] opciones = { "Fácil - Explorador (veloz / Disparo 2.0s)", "Normal - Caza Estelar (Equilibrado / Disparo 1.0s)", "Dificil - Acorazado (Pesado / Disparo 0.3s)" };
        
       String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione el modelo de la nave y dificultad:", "Personalización de Nave", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        
       if (seleccion == null) return;
       
        Dificultad dif = Dificultad.FACIL;
        if (seleccion.startsWith("Normal")) { dif = Dificultad.NORMAL;
        } else if (seleccion.startsWith("Dificil")) { dif = Dificultad.DIFICIL; }
        
        naveActual = new Nave(dif.getTipoNave(), dif, pilotoActual);
        
        JOptionPane.showMessageDialog(this, "Piloto ´" + nombre + "´ registrado con exito./n" + "Nave: " + dif.getTipoNave() + "/n" + "Caracteristicas: " + dif.getCaracteristicas());
        
    }
    
    private void iniciarJuego() {
        if (naveActual == null) {
            Piloto pDefecto = new Piloto("000", "Piloto Novato");
            naveActual = new Nave("Explorador", Dificultad.FACIL, pDefecto);
        }
        
        VentanaJuego juego = new VentanaJuego(naveActual);
        juego.setVisible(true);
    }
}
