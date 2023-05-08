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
public class Notificacion {
    private int id_venta_web;
    private String mensaje;
    private String fecha;
    
    public Notificacion(){}
    
    public Notificacion(int id_venta_web,String mensaje,String fecha){
        this.id_venta_web=id_venta_web;
        this.mensaje=mensaje;
        this.fecha=fecha;
    }

    /**
     * @return the id_venta_web
     */
    public int getId_venta_web() {
        return id_venta_web;
    }

    /**
     * @param id_venta_web the id_venta_web to set
     */
    public void setId_venta_web(int id_venta_web) {
        this.id_venta_web = id_venta_web;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
    
}
