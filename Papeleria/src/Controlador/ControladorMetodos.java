/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author josei
 */
public class ControladorMetodos {
    protected Connection conectar = null;
    ResultSet resultados;
    
    public ControladorMetodos(){}
    
    public String buscarUsuario(String Nombre,String Contraseña){
        String area = null;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("Select AreaT from usuarios where Nombre = ? and Contraseña = ?");
            miSentencia.setString(1, Nombre);
            miSentencia.setString(2, Contraseña);
            resultados = miSentencia.executeQuery();
            
            if(resultados.next()){
                area = resultados.getString("AreaT");
            }else{
                area = "";
            }
            //System.out.println("Area del select:"+area);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return area;
    }
    
    public int ultimaVenta(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(IDVentaLocal) FROM ventalocal;");
            //miSentencia.setInt(1, ID);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(IDVentaLocal)");
            }else{
                ID = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public int ultimoCliente(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(IDClienteLocal) FROM clientelocal;");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(IDClienteLocal)");
            }else{
                ID = 0;
            }
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public int ultimaFactura(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(NumFactura) FROM facturacion;");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(NumFactura)");
            }else{
                ID = 0;
            }
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public float precioVenta(int ID){
        float precio = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT PrecioV FROM productos where IDProducto = ?;");
            miSentencia.setInt(1, ID);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                precio = resultados.getFloat("PrecioV");
            }else{
                precio = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return precio;
    }
    
    public int cantidadProdVenta(int IDV,int IDP){
        int cantidad = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT count(*) FROM productosventa where IDVenta = ? and IDProducto = ?;");
            miSentencia.setInt(1, IDV);
            miSentencia.setInt(2, IDP);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                cantidad = resultados.getInt("count(*)");
            }else{
                cantidad = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return cantidad;
    }
    
    public int existeProdEstado(int IDV,int IDP, String Estado){
        int cantidad = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT count(*) FROM productosventa where IDVenta = ? and IDProducto = ? and EstadoP = ?;");
            miSentencia.setInt(1, IDV);
            miSentencia.setInt(2, IDP);
            miSentencia.setString(3, Estado);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                cantidad = resultados.getInt("count(*)");
            }else{
                cantidad = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return cantidad;
    }
    
    public int existePedido(int IDP){
        int cantidad = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT count(*) FROM productospedido where IDProducto = ? and Estado = ?;");
            miSentencia.setInt(1, IDP);
            miSentencia.setString(2, "Pendiente");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                cantidad = resultados.getInt("count(*)");
            }else{
                cantidad = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return cantidad;
    }
    
    public int cantidadProdDañado(int IDV,int IDP, String Estado){
        int cantidad = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT CantProducto FROM productosventa where IDVenta = ? and IDProducto = ? and EstadoP = ?;");
            miSentencia.setInt(1, IDV);
            miSentencia.setInt(2, IDP);
            miSentencia.setString(3, Estado);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                cantidad = resultados.getInt("CantProducto");
            }else{
                cantidad = 0;
            }
            //System.out.println("Ultima venta:"+ID);
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return cantidad;
    }
    
    public int ultimoPedido(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(IDPedido) FROM productospedido;");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(IDPedido)");
            }else{
                ID = 0;
            }
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public int ultimaCompra(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(IDCompra) FROM compra;");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(IDCompra)");
            }else{
                ID = 0;
            }
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public int ultimoGasto(){
        int ID = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT MAX(IDGasto) FROM gastos;");
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                ID = resultados.getInt("MAX(IDGasto)");
            }else{
                ID = 0;
            }
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return ID;
    }
    
    public String estadoProducto(int ID){
        String estado = "NE";
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT Estado FROM productos where IDProducto = ?;");
            miSentencia.setInt(1, ID);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                estado = resultados.getString("Estado");
            }else{
                estado = "E";
            }
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return estado;
    }
    
    public int provProducto(int ID){
        int id = 0;
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT IDProveedor FROM productos where IDProducto = ?;");
            miSentencia.setInt(1, ID);
            resultados = miSentencia.executeQuery();
            if(resultados.next()){
                id = resultados.getInt("IDProveedor");
            }else{
                id = 0;
            }
            
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        return id;
    }
    
}
