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
public class Facturacion {
    private int num_factura;
    private String fecha_exp;
    private String desc_servicio;
    private float pago_total;
    private String forma_pago;
    
    public Facturacion(){}
    
    public Facturacion(int num_factura,String fecha_exp,String desc_servicio,float pago_total,String forma_pago){
        this.num_factura=num_factura;
        this.fecha_exp=fecha_exp;
        this.desc_servicio=desc_servicio;
        this.pago_total=pago_total;
        this.forma_pago=forma_pago;
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
     * @return the fecha_exp
     */
    public String getFecha_exp() {
        return fecha_exp;
    }

    /**
     * @param fecha_exp the fecha_exp to set
     */
    public void setFecha_exp(String fecha_exp) {
        this.fecha_exp = fecha_exp;
    }

    /**
     * @return the desc_servicio
     */
    public String getDesc_servicio() {
        return desc_servicio;
    }

    /**
     * @param desc_servicio the desc_servicio to set
     */
    public void setDesc_servicio(String desc_servicio) {
        this.desc_servicio = desc_servicio;
    }

    /**
     * @return the pago_total
     */
    public float getPago_total() {
        return pago_total;
    }

    /**
     * @param pago_total the pago_total to set
     */
    public void setPago_total(float pago_total) {
        this.pago_total = pago_total;
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
