/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.Facturacion;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josei
 */
public class ControladorFacturacion {
    ArrayList<Facturacion> facturas = new ArrayList<Facturacion>();
    ControladorMetodos controladorMetodo = new ControladorMetodos();
    private DefaultTableModel modelo;
    private Facturacion facturaActual;
    private int indiceFacturaSeleccionado;
    protected Connection conectar = null;
    private ResultSet resultados;
    //se se buscarán los facturas de una venta
    public ControladorFacturacion(){
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT * from facturacion order by NumFactura");
            resultados = miSentencia.executeQuery();
            while(resultados.next()){
                facturas.add(
                    new Facturacion(
                            resultados.getInt("NumFactura"),
                            resultados.getString("FechaExp"),
                            resultados.getString("DescServicio"),
                            resultados.getFloat("PagoTotal"),
                            resultados.getString("FormaPago")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        indiceFacturaSeleccionado=0;
    }
    
    //función para buscar a un registro por su ID
    public Facturacion buscarFactura(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Facturacion facturaEncontrada = null;
        
        for(Facturacion facturaTemp : facturas){
            if(facturaTemp.getNum_factura() == ID){
                facturaEncontrada = facturaTemp;
                break;
            }
        }
        return facturaEncontrada;
    }
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarFactura(int ID){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM facturacion "
                    + "WHERE NumFactura = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    
    //funcion que sirve para agregar a un registro
    public boolean agregarFactura(Facturacion factura) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO facturacion " 
                    + "(NumFactura, FechaExp, DescServicio, PagoTotal, FormaPago) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, factura.getNum_factura());
            sentencia.setString(2, factura.getFecha_exp());
            sentencia.setString(3, factura.getDesc_servicio());
            sentencia.setFloat(4, factura.getPago_total());
            sentencia.setString(5, factura.getForma_pago());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            facturas.add(factura);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }    
    
}
