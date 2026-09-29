
package hilos;

public class HiloProyectil extends HiloMovil {
    
    public HiloProyectil(int x, int y) {
        super(x, y, 15, 10);
    }
    
    @Override
    protected void mover() {
        this.x += pasoX;
        
        if(this.x > 800) {
            this.activo = false;
        }
    }
    
}
