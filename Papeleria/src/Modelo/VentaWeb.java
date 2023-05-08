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
public class VentaWeb {
    private int id_venta_web;
    private int id_cliente_web;
    private int num_factura;
    private String fecha_compra;
    private String fecha_entrega;
    private float precio_total;
    private int cant_productos;
    private String estado;
    
    public VentaWeb(){}
    
    public VentaWeb(int id_venta_web,int id_cliente_web,int num_factura,String fecha_compra,String fecha_entrega,
            float precio_total,int cant_productos,String estado){
        this.id_venta_web=id_venta_web;
        this.id_cliente_web=id_cliente_web;
        this.num_factura=num_factura;
        this.fecha_compra=fecha_compra;
        this.fecha_entrega=fecha_entrega;
        this.precio_total=precio_total;
        this.cant_productos=cant_productos;
        this.estado=estado;
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
     * @return the id_cliente_web
     */
    public int getId_cliente_web() {
        return id_cliente_web;
    }

    /**
     * @param id_cliente_web the id_cliente_web to set
     */
    public void setId_cliente_web(int id_cliente_web) {
        this.id_cliente_web = id_cliente_web;
    }

    /**
     * @return the num_factura
     */
    public int getNum_factura() {
        return num_factura;
    }

    /**
     * @param num_factura the num_factura to set
     */
    public void setNum_factura(int num_factura) {
        this.num_factura = num_factura;
    }

    /**
     * @return the fecha_compra
     */
    public String getFecha_compra() {
        return fecha_compra;
    }

    /**
     * @param fecha_compra the fecha_compra to set
     */
    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    /**
     * @return the fecha_entrega
     */
    public String getFecha_entrega() {
        return fecha_entrega;
    }

    /**
     * @param fecha_entrega the fecha_entrega to set
     */
    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
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
    
    
    
    
}
