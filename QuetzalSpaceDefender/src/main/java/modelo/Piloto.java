
package modelo;

public class Piloto {
    private String nickname;
    private int puntajeMaximo;
    private int partidasJugadas;
    
    public Piloto(String nickname) {
        this.nickname = nickname;
        this.puntajeMaximo = 0;
        this.partidasJugadas = 0;
    }
    
    public String getNickname() { return nickname; }
    public int getPuntajeMaximo() { return puntajeMaximo; }
    public int getPartidasJugadas() { return partidasJugadas; }
    
    public void actualizarPuntajeMaximo(int nuevoPuntaje) {
        if (nuevoPuntaje > this.puntajeMaximo) {
            this.puntajeMaximo = nuevoPuntaje;
        }
        this.partidasJugadas++;
    }
}
