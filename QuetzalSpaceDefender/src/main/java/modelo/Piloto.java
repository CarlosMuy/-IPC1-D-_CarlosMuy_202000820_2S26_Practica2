
package modelo;

public class Piloto {
    private String id;
    private String nickname;
    private int puntajeMaximo;
    private int partidasJugadas;
    
    public Piloto(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.puntajeMaximo = 0;
        this.partidasJugadas = 0;
    }
    
    public String getId() { return id; }
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
