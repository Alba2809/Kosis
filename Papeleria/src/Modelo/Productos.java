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
public class Productos {
    private int id_producto;
    private int id_proveedor;
    private float precioC;
    private float precioV;
    private String marca;
    private String nombre;
    private String cat_producto;
    private String disponibilidad;
    private int stock;
    private int limite;
    
    public Productos(){}
    
    public Productos(int id_producto,int id_proveedor,float precioC,float precioV,String marca,String nombre,String cat_producto,
            String disponibilidad,int stock,int limite){
        this.id_producto=id_producto;
        this.precioC=precioC;
        this.precioV=precioV;
        this.marca=marca;
        this.nombre=nombre;
        this.cat_producto=cat_producto;
        this.disponibilidad=disponibilidad;
        this.stock=stock;
        this.limite=limite;
        this.id_proveedor=id_proveedor;
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
     * @return the precioC
     */
    public float getPrecioC() {
        return precioC;
    }

    /**
     * @param precioC the precioC to set
     */
    public void setPrecioC(float precioC) {
        this.precioC = precioC;
    }

    /**
     * @return the precioV
     */
    public float getPrecioV() {
        return precioV;
    }

    /**
     * @param precioV the precioV to set
     */
    public void setPrecioV(float precioV) {
        this.precioV = precioV;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
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
     * @return the cat_producto
     */
    public String getCat_producto() {
        return cat_producto;
    }

    /**
     * @param cat_producto the cat_producto to set
     */
    public void setCat_producto(String cat_producto) {
        this.cat_producto = cat_producto;
    }

    /**
     * @return the disponibilidad
     */
    public String getDisponibilidad() {
        return disponibilidad;
    }

    /**
     * @param disponibilidad the disponibilidad to set
     */
    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the limite
     */
    public int getLimite() {
        return limite;
    }

    /**
     * @param limite the limite to set
     */
    public void setLimite(int limite) {
        this.limite = limite;
    }
    
    //función que sirve para buscar un producto por su id
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Productos producto = (Productos) o;
        return this.id_producto==producto.id_producto;
    }
    
    
}
