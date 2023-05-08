/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josei
 */
public class ControladorPedidos {
    ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
    private DefaultTableModel modelo;
    private Pedido pedidoActual;
    private int indicePedidoSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorPedidos(){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select pp.IDPedido,pp.IDProducto,p.Nombre,p.PrecioC,p.CatProducto,p.Marca,p.Stock,p.Limite,pp.Fecha,pp.Estado from productospedido pp JOIN productos p ON pp.IDProducto = p.IDProducto ORDER BY pp.IDPedido DESC");
            while(resultados.next()){
                pedidos.add(
                    new Pedido(
                            resultados.getInt("pp.IDPedido"),
                            resultados.getInt("pp.IDProducto"),
                            resultados.getString("p.Nombre"),
                            resultados.getFloat("p.PrecioC"),
                            resultados.getString("p.CatProducto"),
                            resultados.getString("p.Marca"),
                            resultados.getInt("p.Stock"),
                            resultados.getInt("p.Limite"),
                            resultados.getString("pp.Fecha"),
                            resultados.getString("pp.Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indicePedidoSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("ID Pedido");
        getModelo().addColumn("ID Producto");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Categoría");
        getModelo().addColumn("Marca");
        getModelo().addColumn("Stock");
        getModelo().addColumn("Limite");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Estado");
        
        Object[] pedido;
        for(Pedido pedidoTemp : pedidos){
            pedido = new Object[]{
                pedidoTemp.getId_pedido(),pedidoTemp.getId_producto(),pedidoTemp.getNombre(),pedidoTemp.getCategoria(),
                pedidoTemp.getMarca(),pedidoTemp.getStock(),pedidoTemp.getLimite(),pedidoTemp.getFecha(),
                pedidoTemp.getEstado()
            };
            
            getModelo().addRow(pedido);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the pedidoActual
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    /**
     * @param pedidoActual the pedidoActual to set
     */
    public void setPedidoActual(Pedido pedidoActual) {
        this.pedidoActual = pedidoActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonPedido){
        indicePedidoSeleccionado = renglonPedido;
        setPedidoActual(pedidos.get(renglonPedido));
    }
    
    //funcion que sirve para agregar registro
    public boolean agregarPedido(Pedido pedido) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO productospedido " 
                    + "(IDPedido,IDProducto,Fecha,Estado) "
                    + "VALUES (?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, pedido.getId_pedido());
            sentencia.setInt(2, pedido.getId_producto());
            sentencia.setString(3, pedido.getFecha());
            sentencia.setString(4, "Pendiente");
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            pedidos.add(pedido);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //Se buscan los pedidos que existen
    public ControladorPedidos(String id){    
        int ID = Integer.parseInt(id);
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("Select pp.IDPedido,pp.IDProducto,p.Nombre,p.PrecioC,p.CatProducto,p.Marca,p.Stock,p.Limite,pp.Fecha,pp.Estado from productospedido pp JOIN productos p ON pp.IDProducto = p.IDProducto where pp.IDPedido = ?");
            miSentencia.setInt(1, ID);
            //se realiza una consulta
            resultados = miSentencia.executeQuery();
            while(resultados.next()){
                pedidos.add(
                    new Pedido(
                            resultados.getInt("pp.IDPedido"),
                            resultados.getInt("pp.IDProducto"),
                            resultados.getString("p.Nombre"),
                            resultados.getFloat("p.PrecioC"),
                            resultados.getString("p.CatProducto"),
                            resultados.getString("p.Marca"),
                            resultados.getInt("p.Stock"),
                            resultados.getInt("p.Limite"),
                            resultados.getString("pp.Fecha"),
                            resultados.getString("pp.Estado")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        buscarProductosP();
    }
    
    public void buscarProductosP(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Total");
        
        Object[] pedido;
        for(Pedido pedidoTemp : pedidos){
            pedido = new Object[]{
                pedidoTemp.getId_producto(),pedidoTemp.getNombre(),0,0
            };
            
            getModelo().addRow(pedido);
        }
    }
    
    //función para buscar a un registro
    public Pedido buscarProd(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Pedido productosEncontrado = null;
        
        for(Pedido pedidoTemp : pedidos){
            if(pedidoTemp.getId_pedido() == ID){
                productosEncontrado = pedidoTemp;
                break;
            }
        }
        return productosEncontrado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaPedido(int ID) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE productospedido "
                    + "SET Estado = ? "
                    + "WHERE IDPedido = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setString(1, "Comprado");
            sentencia.setInt(2, ID);
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //función para buscar a un registro por su ID y actualizar la tabla de la ventana
    public boolean buscarPedidoAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("ID Pedido");
        getModelo().addColumn("ID Producto");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Categoría");
        getModelo().addColumn("Marca");
        getModelo().addColumn("Stock");
        getModelo().addColumn("Limite");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Estado");
        
        ArrayList<Pedido> pedidosCoincidentes = new ArrayList<Pedido>();
        Object[] pedido;
        for(Pedido pedidoTemp : pedidos){
            if(pedidoTemp.getId_pedido() == ID){
                pedido = new Object[]{
                    pedidoTemp.getId_pedido(),pedidoTemp.getId_producto(),pedidoTemp.getNombre(),pedidoTemp.getCategoria(),
                    pedidoTemp.getMarca(),pedidoTemp.getStock(),pedidoTemp.getLimite(),pedidoTemp.getFecha(),
                    pedidoTemp.getEstado()
                };
                modelo.addRow(pedido);
                pedidosCoincidentes.add(pedidoTemp);
            }
        }
        return !pedidosCoincidentes.isEmpty();
    }
}
