
package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Partida;
import modelo.Piloto;

public class ControladorPersistencia {
  
   private Piloto[] pilotos;
   private int contadorPilotos;
   
   private Partida[] partidas;
   private int contadorPartidas;
   
   private static final String ARCHIVO_PUNTAJES = "puntajes.txt";
   
   public ControladorPersistencia(int capacidadMaxPilotos, int capacidadMaxPartidas) {
       this.pilotos = new Piloto[capacidadMaxPilotos];
       this.contadorPilotos = 0;
       
       this.partidas = new Partida[capacidadMaxPartidas];
       this.contadorPartidas = 0;
       
       cargarPuntajesDesdeArchivo();
   }
   
   public boolean agregarPiloto(Piloto nuevoPiloto) {
       if (buscarPiloto(nuevoPiloto.getNickname()) != null) {
           return false;
       }
       if (contadorPilotos < pilotos.length) {
           pilotos[contadorPilotos] = nuevoPiloto;
           contadorPilotos++;
           return true;
       }
       return false;
   }   
       
   public Piloto buscarPiloto(String nickname) {
           for (int i = 0; i < contadorPilotos; i++) {
               if (pilotos[i].getNickname().equalsIgnoreCase(nickname)) {
                   return pilotos[i];
               }
           }
           return null;
       }
       
       public boolean registrarPartida(Partida nuevaPartida) {
           if (contadorPartidas < partidas.length) {
               partidas[contadorPartidas] = nuevaPartida;
               contadorPartidas++;
               
               Piloto p = nuevaPartida.getPiloto();
               if (p != null) {
               p.actualizarPuntajeMaximo(nuevaPartida.getPuntajeObtenido());
           }
           
           guardarPartidaEnArchivo(nuevaPartida);    
           return true;
       }
       return false;
    }
       
       public Partida[] obtenerTopPuntajes(int limiteTop) {
           Partida[] topPartidas = new Partida[contadorPartidas];
           for (int i = 0; i < contadorPartidas; i++) {
               topPartidas[i] = partidas[i];
           }
           
           for (int i = 0; i < contadorPartidas - 1; i++) {
               for (int j = 0; j < contadorPartidas - 1 - i; j++) {
                   if (topPartidas[j].getPuntajeObtenido() < topPartidas[j + 1].getPuntajeObtenido()) {
                       Partida aux = topPartidas[j];
                       topPartidas[j] = topPartidas[j + 1];
                       topPartidas[j + 1] = aux;
                   }
               }
           }
           
           int tamanioFinal = Math.min(limiteTop, contadorPartidas);
           Partida[] resultado = new Partida[tamanioFinal];
           for (int k = 0; k < tamanioFinal; k++) {
               resultado[k] = topPartidas[k];
           }
           
           return resultado;
       }
       
       private void guardarPartidaEnArchivo(Partida p) {
           try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PUNTAJES, true))) {
               String nick = (p.getPiloto() != null) ? p.getPiloto().getNickname() : "Anonimo";
               bw.write(nick + "," + p.getPuntajeObtenido());
               bw.newLine();
           } catch (IOException e) {
               System.err.println("Error al escribir puntaje en archivo: " + e.getMessage());
           }
       }
       
       private void cargarPuntajesDesdeArchivo() {
           File f = new File(ARCHIVO_PUNTAJES);
           if (!f.exists()) return;
           
           try (BufferedReader br = new BufferedReader(new FileReader(f))){
               String linea;
               while ((linea = br.readLine()) != null) {
                   String[] datos = linea.split(",");
                   if (datos.length >= 2) {
                       String nick = datos[0];
                       int pts = Integer.parseInt(datos[1].trim());
                       
                       Piloto piloto = buscarPiloto(nick);
                       if (piloto == null) {
                           piloto = new Piloto("101", nick);
                           agregarPiloto(piloto);
                       }
                       
                       Partida p = new Partida(piloto, null, pts);
                       if (contadorPartidas < partidas.length) {
                           partidas[contadorPartidas++] = p;
                       }
                   }
               }
           } catch (Exception e) {
               System.err.println("Error al cargar puntajes del archivo: " + e.getMessage());
           }
       }
       
       public Piloto[] getPilotos() { return pilotos; }
       public int getContadorPilotos() { return contadorPilotos; }
       public Partida[] getPartidas() { return partidas; }
       public int getContadorPartidas() { return contadorPartidas; }
   }
