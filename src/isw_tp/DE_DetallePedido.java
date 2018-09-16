/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isw_tp;

/**
 *
 * @author nicolastomassi
 */
public class DE_DetallePedido {
    
    private DE_Articulo articulo;
    private int cantidad;

    public DE_DetallePedido(DE_Articulo articulo, int cantidad) {
        this.articulo = articulo;
        this.cantidad = cantidad;
    }

    public DE_DetallePedido() {
    }

    public DE_Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(DE_Articulo articulo) {
        this.articulo = articulo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    
    
}
