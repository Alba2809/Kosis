/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.ProductosVenta;
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
public class ControladorProductosVenta {
    ArrayList<ProductosVenta> productos = new ArrayList<ProductosVenta>();
    ControladorMetodos controladorMetodo = new ControladorMetodos();
    private DefaultTableModel modelo;
    private ProductosVenta prodVentaActual;
    private int indiceProdVentaSeleccionado;
    protected Connection conectar = null;
    private ResultSet resultados;
    
    public ControladorProductosVenta(){}
    
    //se se buscarán los productos de una venta
    public ControladorProductosVenta(String id,int modo){    
        int ID = Integer.parseInt(id);
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT pv.IDVenta,pv.IDProducto,p.Nombre,pv.CantProducto,pv.PrecioVenta,pv.EstadoP,pv.PrecioCompra FROM productosventa pv JOIN productos p on pv.IDProducto = p.IDProducto where pv.IDVenta=?");
            miSentencia.setInt(1, ID);
            //se realiza una consulta
            resultados = miSentencia.executeQuery();
            while(resultados.next()){
                productos.add(
                    new ProductosVenta(
                            resultados.getInt("pv.IDVenta"),
                            resultados.getInt("pv.IDProducto"),
                            resultados.getString("p.Nombre"),
                            resultados.getInt("pv.CantProducto"),
                            resultados.getFloat("pv.PrecioVenta"),
                            resultados.getString("pv.EstadoP"),
                            resultados.getFloat("pv.PrecioCompra")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        if(modo == 0)
            actualizarModelo();
        else
            actGanancia();
        indiceProdVentaSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio Unitario");        
        getModelo().addColumn("Estado");    
        Object[] producto;
        for(ProductosVenta productoTemp : productos){
            producto = new Object[]{
                productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCant_producto(),
                productoTemp.getPrecio_venta(),productoTemp.getEstado()
            };
            
            getModelo().addRow(producto);
        }
    }
    
    //funcion que sirve para actualizar el modelo (esto se ocupa para el area de finanzas)
    public void actGanancia(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio Venta");
        getModelo().addColumn("Precio Compra");
        getModelo().addColumn("Estado");
        Object[] producto;
        for(ProductosVenta productoTemp : productos){
            producto = new Object[]{
                productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCant_producto(),
                productoTemp.getPrecio_venta(),productoTemp.getPrecio_compra(),productoTemp.getEstado()
            };
            
            getModelo().addRow(producto);
        }
    }
    
    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the prodVentaActual
     */
    public ProductosVenta getProdVentaActual() {
        return prodVentaActual;
    }

    /**
     * @param prodVentaActual the prodVentaActual to set
     */
    public void setProdVentaActual(ProductosVenta prodVentaActual) {
        this.prodVentaActual = prodVentaActual;
    }
    
    //funcion que sirve para actualiza la habitacion actual e indice de la habitacion seleccionada
    public void Selecciona(int renglonProd){
        indiceProdVentaSeleccionado = renglonProd;
        setProdVentaActual(productos.get(renglonProd));
    }
    
    public boolean buscarProductosV(String id){
        int ID = Integer.parseInt(id);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio unitario");
        
        ArrayList<ProductosVenta> productosCoincidentes = new ArrayList<ProductosVenta>();
        Object[] producto;
        for(ProductosVenta productoTemp : productos){
            if(productoTemp.getId_producto() == ID){
                producto = new Object[]{
                    productoTemp.getId_venta(),productoTemp.getNombre(),
                    productoTemp.getCant_producto(),productoTemp.getPrecio_venta()
                };
                modelo.addRow(producto);
                productosCoincidentes.add(productoTemp);
            }
        }
        return !productosCoincidentes.isEmpty();
    }
    
    //función para buscar a un registro por su IDV
    public ProductosVenta buscarProd(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        ProductosVenta productosEncontrado = null;
        
        for(ProductosVenta productoTemp : productos){
            if(productoTemp.getId_venta()==ID){
                productosEncontrado = productoTemp;
                break;
            }
        }
        return productosEncontrado;
    }
    
    //función para buscar a un registro por su IDV
    public ProductosVenta buscarProdDañado(String idv,String idP){
        int IDV = Integer.parseInt(idv);
        int IDP = Integer.parseInt(idP);
        ProductosVenta productosEncontrado = null;
        
        for(ProductosVenta productoTemp : productos){
            if((productoTemp.getId_venta() == IDV) && (productoTemp.getId_producto() == IDP) && (productoTemp.getEstado().equals("Dañado"))){
                productosEncontrado = productoTemp;
                break;
            }
        }
        return productosEncontrado;
    }
    
    //funcion que sirve para agregar a un venta
    public boolean agregarProdVL(ProductosVenta producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO productosventa " 
                    + "(IDVenta,IDProducto,CantProducto,PrecioVenta,EstadoP) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, producto.getId_venta());
            sentencia.setInt(2, producto.getId_producto());
            sentencia.setInt(3, producto.getCant_producto());
            sentencia.setFloat(4, producto.getPrecio_venta());
            sentencia.setString(5, producto.getEstado());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            productos.add(producto);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaProdDev(ProductosVenta producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE productosventa "
                    + "SET CantProducto = ?, "
                    + "EstadoP = ? "
                    + "WHERE IDVenta = ? and IDProducto = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setInt(1, producto.getCant_producto());
            sentencia.setString(2, producto.getEstado());
            sentencia.setInt(3, producto.getId_venta());
            sentencia.setInt(4, producto.getId_producto());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaProdDevDañado(ProductosVenta producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE productosventa "
                    + "SET CantProducto = ? "
                    + "WHERE IDVenta = ? and IDProducto = ? and EstadoP = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setInt(1, producto.getCant_producto());
            sentencia.setInt(2, producto.getId_venta());
            sentencia.setInt(3, producto.getId_producto());
            sentencia.setString(4, producto.getEstado());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarProdV(int IDV,int IDP){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM productosventa "
                    + "WHERE IDVenta = ? and IDProducto = ? and EstadoP = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, IDV);
            sentencia.setInt(2, IDP);
            sentencia.setString(3, "Funcional");
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    
}
