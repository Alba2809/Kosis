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
public class Proveedor {
    private int id_proveedor;
    private String nombre;
    private int num_pedidos;
    private String telefono;
    private String direccion;
    
    public Proveedor(){}
    
    public Proveedor(int id_proveedor,String nombre,int num_pedidos,String telefono, String direccion){
        this.id_proveedor=id_proveedor;
        this.nombre=nombre;
        this.num_pedidos=num_pedidos;
        this.telefono=telefono;
        this.direccion = direccion;
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
     * @return the num_pedidos
     */
    public int getNum_pedidos() {
        return num_pedidos;
    }

    /**
     * @param num_pedidos the num_pedidos to set
     */
    public void setNum_pedidos(int num_pedidos) {
        this.num_pedidos = num_pedidos;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
    
    
}
