
package persistencia;

import modelo.Partida;
import modelo.Piloto;

public class ControladorPersistencia {
  
   private Piloto[] pilotos;
   private int contadorPilotos;
   
   private Partida[] partidas;
   private int contadorPartidas;
   
   public ControladorPersistencia(int capacidadMaxPilotos, int capacidadMaxPartidas) {
       this.pilotos = new Piloto[capacidadMaxPilotos];
       this.contadorPilotos = 0;
       
       this.partidas = new Partida[capacidadMaxPartidas];
       this.contadorPartidas = 0;
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
               p.actualizarPuntajeMaximo(nuevaPartida.getPuntajeObtenido());
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
       
       public Piloto[] getPilotos() { return pilotos; }
       public int getContadorPilotos() { return contadorPilotos; }
       public Partida[] getPartidas() { return partidas; }
       public int getContadorPartidas() { return contadorPartidas; }
   }
