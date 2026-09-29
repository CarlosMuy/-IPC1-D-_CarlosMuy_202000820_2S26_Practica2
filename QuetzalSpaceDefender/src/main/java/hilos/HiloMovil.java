
package hilos;

public abstract class HiloMovil extends Thread{
    protected int x, y;
    protected int velocidadMs;
    protected int pasoX;
    protected boolean activo;
    
    public HiloMovil(int x, int y, int velocidadMs, int pasoX) {
        this.x = x;
        this.y = y;
        this.velocidadMs = velocidadMs;
        this.pasoX = pasoX;
        this.activo = true;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(velocidadMs);
                mover();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected abstract void mover();
       
    public void detener() {
        this.activo = false;
        }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActivo() { return activo; }
    
}
