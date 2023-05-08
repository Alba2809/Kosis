/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.VentaWeb;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josei
 */
public class ControladorVentaWeb {
    ArrayList<VentaWeb> ventas = new ArrayList<VentaWeb>();
    private DefaultTableModel modelo;
    private VentaWeb ventaWebActual;
    private int indiceVentaWebSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorVentaWeb(){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from ventaweb order by IDVentaWeb");
            while(resultados.next()){
                ventas.add(
                    new VentaWeb(
                            resultados.getInt("IDVentaWeb"),
                            resultados.getInt("IDClienteWeb"),
                            resultados.getInt("NumFactura"),
                            resultados.getString("FechaCompra"),
                            resultados.getString("FechaEntrega"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceVentaWebSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Folio");
        getModelo().addColumn("Cliente");
        getModelo().addColumn("Fecha_Compra");
        getModelo().addColumn("Fecha_Entrega");
        getModelo().addColumn("Precio_Total");
        getModelo().addColumn("Estado");
        getModelo().addColumn("Factura");
        
        Object[] venta;
        for(VentaWeb ventaTemp : ventas){
            venta = new Object[]{
                ventaTemp.getId_venta_web(),ventaTemp.getId_cliente_web(),ventaTemp.getFecha_compra(),
                ventaTemp.getFecha_entrega(),ventaTemp.getPrecio_total(),ventaTemp.getEstado(),
                ventaTemp.getNum_factura()
            };
            
            getModelo().addRow(venta);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the ventaWebActual
     */
    public VentaWeb getVentaWebActual() {
        return ventaWebActual;
    }

    /**
     * @param ventaWebActual the ventaWebActual to set
     */
    public void setVentaWebActual(VentaWeb ventaWebActual) {
        this.ventaWebActual = ventaWebActual;
    }
    
    //funcion que sirve para actualiza la habitacion actual e indice de la habitacion seleccionada
    public void Selecciona(int renglonVentaW){
        indiceVentaWebSeleccionado = renglonVentaW;
        setVentaWebActual(ventas.get(renglonVentaW));
    }
    
    //función para buscar a un registro por su ID
    public VentaWeb buscarVentaWPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        VentaWeb ventaEncontrado = null;
        
        for(VentaWeb ventaTemp : ventas){
            if(ventaTemp.getId_venta_web()==ID){
                ventaEncontrado = ventaTemp;
                break;
            }
        }
        return ventaEncontrado;
    }
    
    public boolean buscarVentaWAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Folio");
        getModelo().addColumn("Cliente");
        getModelo().addColumn("Fecha_Compra");
        getModelo().addColumn("Fecha_Entrega");
        getModelo().addColumn("Precio_Total");
        getModelo().addColumn("Estado");
        getModelo().addColumn("Factura");
        
        //VentaLocal ventaMuestra = new VentaLocal(ID, 0, 0,0,0,"");
        
        ArrayList<VentaWeb> ventaCoincidentes = new ArrayList<VentaWeb>();
        Object[] venta;
        for(VentaWeb ventaTemp : ventas){
            if(ventaTemp.getId_venta_web()== ID){
                venta = new Object[]{
                    ventaTemp.getId_venta_web(),ventaTemp.getId_cliente_web(),ventaTemp.getFecha_compra(),
                    ventaTemp.getFecha_entrega(),ventaTemp.getPrecio_total(),ventaTemp.getEstado(),
                    ventaTemp.getNum_factura()
                };
                modelo.addRow(venta);
                ventaCoincidentes.add(ventaTemp);
            }
        }
        return !ventaCoincidentes.isEmpty();
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaVentaW() {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE ventaweb "
                    + "SET IDClienteWeb = ?,"
                    + "NumFactura = ?,"
                    + "FechaCompra = ?,"
                    + "FechaEntrega = ?,"
                    + "PrecioTotal = ?,"
                    + "CantProductos = ?, "
                    + "Estado = ? "
                    + "WHERE IDVentaWeb = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setInt(1, ventaWebActual.getId_cliente_web());
            sentencia.setInt(2, ventaWebActual.getNum_factura());
            sentencia.setString(3, ventaWebActual.getFecha_compra());
            sentencia.setString(4, ventaWebActual.getFecha_entrega());
            sentencia.setFloat(5, ventaWebActual.getPrecio_total());
            sentencia.setInt(6, ventaWebActual.getCant_productos());
            sentencia.setString(7, ventaWebActual.getEstado());
            sentencia.setInt(8, ventaWebActual.getId_venta_web());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    //constructor para ordenar los registros por fecha de compra descendente
    public ControladorVentaWeb(String fechaC){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from ventaweb order by FechaCompra DESC");
            while(resultados.next()){
                ventas.add(
                    new VentaWeb(
                            resultados.getInt("IDVentaWeb"),
                            resultados.getInt("IDClienteWeb"),
                            resultados.getInt("NumFactura"),
                            resultados.getString("FechaCompra"),
                            resultados.getString("FechaEntrega"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceVentaWebSeleccionado=0;
    }
    
    //constructor para ordenar los registros por fecha de entrega descendente
    public ControladorVentaWeb(String fechaE,String desc){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from ventaweb order by FechaEntrega DESC");
            while(resultados.next()){
                ventas.add(
                    new VentaWeb(
                            resultados.getInt("IDVentaWeb"),
                            resultados.getInt("IDClienteWeb"),
                            resultados.getInt("NumFactura"),
                            resultados.getString("FechaCompra"),
                            resultados.getString("FechaEntrega"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceVentaWebSeleccionado=0;
    }
    
    //constructor para ordenar los registros por estado y de forma ascendente la fecha de entrega
    public ControladorVentaWeb(String fechaE,String desc,String es){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from ventaweb order by Estado DESC,FechaEntrega ASC");
            while(resultados.next()){
                ventas.add(
                    new VentaWeb(
                            resultados.getInt("IDVentaWeb"),
                            resultados.getInt("IDClienteWeb"),
                            resultados.getInt("NumFactura"),
                            resultados.getString("FechaCompra"),
                            resultados.getString("FechaEntrega"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceVentaWebSeleccionado=0;
    }
    
}
