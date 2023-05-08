/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author josei
 */
public class Devoluciones {
    private int id_devolucion;
    private int id_venta;
    private int id_producto;
    private int id_proveedor;
    private String defecto;
    private String fecha;
    private String tipo;
    private int Cantidad;
    
    public Devoluciones(){}
    
    public Devoluciones(int id_devolucion,int id_venta,int id_producto,int id_proveedor,String defecto,
            String fecha,String tipo,int Cantidad){
        this.id_devolucion=id_devolucion;
        this.id_venta=id_venta;
        this.id_producto=id_producto;
        this.id_proveedor=id_proveedor;
        this.defecto=defecto;
        this.fecha=fecha;
        this.tipo=tipo;
        this.Cantidad=Cantidad;
    }

    /**
     * @return the id_devolucion
     */
    public int getId_devolucion() {
        return id_devolucion;
    }

    /**
     * @param id_devolucion the id_devolucion to set
     */
    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    /**
     * @return the id_venta
     */
    public int getId_venta() {
        return id_venta;
    }

    /**
     * @param id_venta the id_venta to set
     */
    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    /**
     * @return the id_producto
     */
    public int getId_producto() {
        return id_producto;
    }

    /**
     * @param id_producto the id_producto to set
     */
    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    /**
     * @return the id_proveedor
     */
    public int getId_proveedor() {
        return id_proveedor;
    }

    /**
     * @param id_proveedor the id_proveedor to set
     */
    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    /**
     * @return the defecto
     */
    public String getDefecto() {
        return defecto;
    }

    /**
     * @param defecto the defecto to set
     */
    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the Cantidad
     */
    public int getCantidad() {
        return Cantidad;
    }

    /**
     * @param Cantidad the Cantidad to set
     */
    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }
    
    
    
}
