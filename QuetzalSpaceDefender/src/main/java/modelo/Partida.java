
package modelo;

public class Partida {
   private Piloto piloto;
   private Dificultad dificultad;
   private int puntajeObtenido;
   
   public Partida(Piloto piloto, Dificultad dificultad, int puntajeObtenido) {
       this.piloto = piloto;
       this.dificultad = dificultad;
       this.puntajeObtenido = puntajeObtenido;
   }
   
   public Piloto getPiloto() { return piloto; }
   public Dificultad getDificultad() { return dificultad; }
   public int getPuntajeObtenido() { return puntajeObtenido; }
}
