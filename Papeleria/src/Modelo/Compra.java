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
public class Compra {
    private int id_compra;
    private float precio_total;
    private String fecha;
    private int cant_productos;
    
    public Compra(){}
    
    public Compra(int id_compra,float precio_total,String fecha,int cant_productos){
        this.id_compra = id_compra;
        this.precio_total = precio_total;
        this.fecha = fecha;
        this.cant_productos = cant_productos;
    }

    /**
     * @return the id_compra
     */
    public int getId_compra() {
        return id_compra;
    }

    /**
     * @param id_compra the id_compra to set
     */
    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    /**
     * @return the precio_total
     */
    public float getPrecio_total() {
        return precio_total;
    }

    /**
     * @param precio_total the precio_total to set
     */
    public void setPrecio_total(float precio_total) {
        this.precio_total = precio_total;
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
    
    
    
}
