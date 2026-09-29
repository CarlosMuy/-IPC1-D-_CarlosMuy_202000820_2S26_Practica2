
package hilos;

public class HiloObjetoEspecial extends HiloMovil {
    private final TipoObjeto tipo;
    
    public HiloObjetoEspecial(int x, int y, int velocidadMs, TipoObjeto tipo) {
        super(x, y, velocidadMs, 5);
        this.tipo = tipo;
    }
    
    @Override
    protected void mover() {
        this.x -= pasoX;
        
        if(this.x < -50) {
            this.activo = false;
        }
    }
    
    public TipoObjeto getTipo() { 
        return tipo;
    }
    
}
