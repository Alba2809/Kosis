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

/**
 *
 * @author josei
 */
public class ControladorProductosDev {
    ArrayList<ProductosVenta> productos = new ArrayList<ProductosVenta>();
    ControladorMetodos controladorMetodo = new ControladorMetodos();
    private DefaultTableModel modelo;
    private ProductosVenta prodVentaActual;
    private int indiceProdVentaSeleccionado;
    protected Connection conectar = null;
    private ResultSet resultados;
    
    public ControladorProductosDev(){}
    
    //se se buscarán los productos de una venta
    public ControladorProductosDev(String id){    
        int ID = Integer.parseInt(id);
        try{
            conectar = Conexion.getConexion().getConector();
            PreparedStatement miSentencia = conectar.prepareStatement("SELECT pv.IDVenta,pv.IDProducto,p.Nombre,pv.CantProducto,pv.PrecioVenta,pv.EstadoP FROM productosventa pv Join productos p On pv.IDProducto = p.IDProducto where pv.IDVenta=?");
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
                            resultados.getString("pv.EstadoP")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceProdVentaSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        //getModelo().addColumn("Venta");
        getModelo().addColumn("Código");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Precio unitario");        
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
    
    //función para buscar a un registro por su ID
    public ProductosVenta buscarProd(String idVenta,String idProd){
        int IDV = Integer.parseInt(idVenta);
        int IDP = Integer.parseInt(idProd);
        ProductosVenta productosEncontrado = new ProductosVenta();
        
        for(ProductosVenta productoTemp : productos){
            if((productoTemp.getId_venta() == IDV) ){
                productosEncontrado = productoTemp;
                break;
            }
        }
        return productosEncontrado;
    }
    
    
    
}
