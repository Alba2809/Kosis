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
public class Gastos {
    private int id_gasto;
    private String nombre;
    private String fecha;
    private float monto;
    private String tipo;
    
    public Gastos(){}
    
    public Gastos(int id_gasto,String nombre,String fecha,float monto,String tipo){
        this.id_gasto = id_gasto;
        this.nombre = nombre;
        this.fecha = fecha;
        this.monto = monto;
        this.tipo = tipo;
    }

    /**
     * @return the id_gasto
     */
    public int getId_gasto() {
        return id_gasto;
    }

    /**
     * @param id_gasto the id_gasto to set
     */
    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
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
     * @return the monto
     */
    public float getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(float monto) {
        this.monto = monto;
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
    
    
}
