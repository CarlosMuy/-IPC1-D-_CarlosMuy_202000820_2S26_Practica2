
package modelo;


public enum Dificultad {
    FACIL("Explorador", "Alta velocidad / Disparo lento", 10, 2000),
    NORMAL("Caza Estelar", "Velocidad media / Disparo medio", 20, 1000),
    DIFICIL("Acorazado", "Velocidad baja / Disparo rápido", 40, 300);
    
    private final String tipoNave;
    private final String caracteristicas;
    private final int sleepMovimientoMs;
    private final int sleepDisparoMs;
    
    Dificultad(String tipoNave, String caracteristicas, int sleepMovimientoMs, int sleepDisparoMs) {
        this.tipoNave = tipoNave;
        this.caracteristicas = caracteristicas;
        this.sleepMovimientoMs = sleepMovimientoMs;
        this.sleepDisparoMs = sleepDisparoMs;
    }
    
    public String getTipoNave() { return tipoNave; }
    public String getCaracteristicas() { return caracteristicas; }
    public int getSleepMovimientoMs() { return sleepMovimientoMs; }
    public int getSleepDisparoMs() { return sleepDisparoMs; }
    
}
