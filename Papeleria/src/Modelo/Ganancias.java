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
public class Ganancias {
    private int folio;
    private int cliente;
    private int cant_productos;
    private String fecha;
    private float total_venta;
    private float ganancia;
    
    public Ganancias(){}
    
    public Ganancias(int folio,int cliente,int cant_productos,String fecha,float total_venta,float ganancia){
        this.folio = folio;
        this.cliente = cliente;
        this.cant_productos = cant_productos;
        this.fecha = fecha;
        this.total_venta = total_venta;
        this.ganancia = ganancia;
    }

    /**
     * @return the folio
     */
    public int getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(int folio) {
        this.folio = folio;
    }

    /**
     * @return the cliente
     */
    public int getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the cant_productos
     */
    public int getCant_productos() {
        return cant_productos;
    }

    /**
     * @param cant_productos the cant_productos to set
     */
    public void setCant_productos(int cant_productos) {
        this.cant_productos = cant_productos;
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
     * @return the total_venta
     */
    public float getTotal_venta() {
        return total_venta;
    }

    /**
     * @param total_venta the total_venta to set
     */
    public void setTotal_venta(float total_venta) {
        this.total_venta = total_venta;
    }

    /**
     * @return the ganancia
     */
    public float getGanancia() {
        return ganancia;
    }

    /**
     * @param ganancia the ganancia to set
     */
    public void setGanancia(float ganancia) {
        this.ganancia = ganancia;
    }
    
    
}
