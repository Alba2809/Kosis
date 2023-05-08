/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.ProductosCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josei
 */
public class ControladorProductosCompra {
    ArrayList<ProductosCompra> productos = new ArrayList<ProductosCompra>();
    ControladorMetodos controladorMetodo = new ControladorMetodos();
    private DefaultTableModel modelo;
    private ProductosCompra prodCompraActual;
    private int indiceProdCompraSeleccionado;
    protected Connection conectar = null;
    private ResultSet resultados;
    
    public ControladorProductosCompra(){}
    
    //se se buscarán los productos de una venta
    public ControladorProductosCompra(String id){    
        int ID = Integer.parseInt(id);
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT pc.IDCompra,pc.IDProducto,p.Nombre,pc.CantProducto,pc.PrecioCompra FROM productoscompra pc JOIN productos p ON pc.IDProducto = p.IDProducto where IDCompra = ?");
            miSentencia.setInt(1, ID);
            //se realiza una consulta
            resultados = miSentencia.executeQuery();
            while(resultados.next()){
                productos.add(
                    new ProductosCompra(
                            resultados.getInt("pc.IDCompra"),
                            resultados.getInt("pc.IDProducto"),
                            resultados.getString("p.Nombre"),
                            resultados.getInt("pc.CantProducto"),
                            resultados.getFloat("pc.PrecioCompra")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceProdCompraSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio Unitario");        
        Object[] producto;
        for(ProductosCompra productoTemp : productos){
            producto = new Object[]{
                productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCant_producto(),
                productoTemp.getPrecio_compra()
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
     * @return the prodCompraActual
     */
    public ProductosCompra getProdCompraActual() {
        return prodCompraActual;
    }

    /**
     * @param prodCompraActual the prodCompraActual to set
     */
    public void setProdCompraActual(ProductosCompra prodCompraActual) {
        this.prodCompraActual = prodCompraActual;
    }
    
    //funcion que sirve para actualiza la habitacion actual e indice de la habitacion seleccionada
    public void Selecciona(int renglonProd){
        indiceProdCompraSeleccionado = renglonProd;
        setProdCompraActual(productos.get(renglonProd));
    }
    
    public boolean buscarProductosV(String id){
        int ID = Integer.parseInt(id);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio Unitario");
        
        ArrayList<ProductosCompra> productosCoincidentes = new ArrayList<ProductosCompra>();
        Object[] producto;
        for(ProductosCompra productoTemp : productos){
            if(productoTemp.getId_producto() == ID){
                producto = new Object[]{
                    productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCant_producto(),
                    productoTemp.getPrecio_compra()
                };
                modelo.addRow(producto);
                productosCoincidentes.add(productoTemp);
            }
        }
        return !productosCoincidentes.isEmpty();
    }
    //funcion que sirve para agregar a un venta
    public boolean agregarProdCompra(ProductosCompra producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO productoscompra " 
                    + "(IDCompra,IDProducto,CantProducto,PrecioCompra) "
                    + "VALUES (?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, producto.getId_compra());
            sentencia.setInt(2, producto.getId_producto());
            sentencia.setInt(3, producto.getCant_producto());
            sentencia.setFloat(4, producto.getPrecio_compra());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            productos.add(producto);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
}
