
package modelo;

public class Nave {
    private String nombreModelo;
    private Dificultad dificultad;
    private int sleepMovimientoMs;
    private int sleepDisparoMs;
    
    public Nave(Dificultad dificultad) {
        this.dificultad = dificultad;
        this.nombreModelo = dificultad.getTipoNave();
        this.sleepMovimientoMs = dificultad.getSleepMovimientoMs();
        this.sleepDisparoMs = dificultad.getSleepDisparoMs();
    }
    
    public String getNombreModelo() { return nombreModelo; }
    public Dificultad getDificultad() { return dificultad; }
    public int getSleepMovimientoMs() { return sleepMovimientoMs; }
    public int getSleepDisparoMs() { return sleepDisparoMs; }
}
