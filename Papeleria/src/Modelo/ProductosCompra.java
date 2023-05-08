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
public class ProductosCompra {
    private int id_compra;
    private int id_producto;
    private String nombre;
    private int cant_producto;
    private float precio_compra;
    
    public ProductosCompra(){}
    
    public ProductosCompra(int id_compra,int id_producto,String nombre,int cant_producto,float precio_compra){
        this.id_compra = id_compra;
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.cant_producto = cant_producto;
        this.precio_compra = precio_compra;
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the cant_producto
     */
    public int getCant_producto() {
        return cant_producto;
    }

    /**
     * @param cant_producto the cant_producto to set
     */
    public void setCant_producto(int cant_producto) {
        this.cant_producto = cant_producto;
    }

    /**
     * @return the precio_compra
     */
    public float getPrecio_compra() {
        return precio_compra;
    }

    /**
     * @param precio_compra the precio_compra to set
     */
    public void setPrecio_compra(float precio_compra) {
        this.precio_compra = precio_compra;
    }
    
    
}
