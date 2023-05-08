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
public class ProductosVenta {
    private int id_venta;
    private int id_producto;
    private String nombre;
    private int cant_producto;
    private float precio_venta;
    private String estado;
    private Float precio_compra;
    
    public ProductosVenta(){}
    
    public ProductosVenta(int id_venta,int id_producto,String nombre,int cant_producto,float precio_venta,String estado){
        this.id_venta=id_venta;
        this.id_producto=id_producto;
        this.nombre=nombre;
        this.cant_producto=cant_producto;
        this.precio_venta=precio_venta;
        this.estado=estado;
    }
    
    public ProductosVenta(int id_venta,int id_producto,String nombre,int cant_producto,float precio_venta,String estado,float precio_compra){
        this.id_venta=id_venta;
        this.id_producto=id_producto;
        this.nombre=nombre;
        this.cant_producto=cant_producto;
        this.precio_venta=precio_venta;
        this.estado=estado;
        this.precio_compra = precio_compra;
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
     * @return the precio_venta
     */
    public float getPrecio_venta() {
        return precio_venta;
    }

    /**
     * @param precio_venta the precio_venta to set
     */
    public void setPrecio_venta(float precio_venta) {
        this.precio_venta = precio_venta;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the precio_compra
     */
    public Float getPrecio_compra() {
        return precio_compra;
    }

    /**
     * @param precio_compra the precio_compra to set
     */
    public void setPrecio_compra(Float precio_compra) {
        this.precio_compra = precio_compra;
    }
    
    
    
}
