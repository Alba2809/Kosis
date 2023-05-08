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
public class VentaLocal {
    private int id_venta_local;
    private int id_cliente_local;
    private int num_factura;
    private float precio_total;
    private int cant_productos;
    private String fecha;
    private String forma_pago;
    
    public VentaLocal(){}
    
    public VentaLocal(int id_venta_local,int id_cliente_local,int num_factura,float precio_total,
            int cant_productos,String fecha,String forma_pago){
        this.id_venta_local=id_venta_local;
        this.id_cliente_local=id_cliente_local;
        this.num_factura=num_factura;
        this.precio_total=precio_total;
        this.cant_productos=cant_productos;
        this.fecha=fecha;
        this.forma_pago=forma_pago;
    }

    /**
     * @return the id_venta_local
     */
    public int getId_venta_local() {
        return id_venta_local;
    }

    /**
     * @param id_venta_local the id_venta_local to set
     */
    public void setId_venta_local(int id_venta_local) {
        this.id_venta_local = id_venta_local;
    }

    /**
     * @return the id_cliente_local
     */
    public int getId_cliente_local() {
        return id_cliente_local;
    }

    /**
     * @param id_cliente_local the id_cliente_local to set
     */
    public void setId_cliente_local(int id_cliente_local) {
        this.id_cliente_local = id_cliente_local;
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
     * @return the forma_pago
     */
    public String getForma_pago() {
        return forma_pago;
    }

    /**
     * @param forma_pago the forma_pago to set
     */
    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }
    
    
    
}
