/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.ProductosElim;
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
public class ControladorProducElim {
    ArrayList<ProductosElim> productos = new ArrayList<ProductosElim>();
    private DefaultTableModel modelo;
    private ProductosElim productoActual;
    private int indiceProductoSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorProducElim(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("Select * from productoseliminados order by IDProducto");
            else if(orden.equals("Nombre"))
                resultados = sentencia.executeQuery("Select * from productoseliminados order by Nombre");
            else if(orden.equals("FechaDESC"))
                resultados = sentencia.executeQuery("Select * from productoseliminados order by Fecha DESC");
            else if(orden.equals("FechaASC"))
                resultados = sentencia.executeQuery("Select * from productoseliminados order by Fecha ASC");
            else
                resultados = sentencia.executeQuery("Select * from productoseliminados order by IDProducto");
            while(resultados.next()){
                productos.add(
                    new ProductosElim(
                            resultados.getInt("IDProducto"),
                            resultados.getString("Nombre"),
                            resultados.getInt("Cantidad"),
                            resultados.getString("Motivo"),
                            resultados.getString("Fecha")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceProductoSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("ID");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Motivo");
        getModelo().addColumn("Fecha");
        
        Object[] producto;
        for(ProductosElim productoTemp : productos){
            producto = new Object[]{
                productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCantidad(),
                productoTemp.getMotivo(),productoTemp.getFecha()
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
     * @return the productoActual
     */
    public ProductosElim getProductoActual() {
        return productoActual;
    }

    /**
     * @param productoActual the productoActual to set
     */
    public void setProductoActual(ProductosElim productoActual) {
        this.productoActual = productoActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonProducto){
        indiceProductoSeleccionado = renglonProducto;
        setProductoActual(productos.get(renglonProducto));
    }
    
    //funcion que sirve para agregar registro
    public boolean agregarEliminacion(ProductosElim producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO productoseliminados " 
                    + "(IDProducto,Nombre,Cantidad,Motivo,Fecha) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, producto.getId_producto());
            sentencia.setString(2, producto.getNombre());
            sentencia.setInt(3, producto.getCantidad());
            sentencia.setString(4, producto.getMotivo());
            sentencia.setString(5, producto.getFecha());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            productos.add(producto);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //función para buscar a un registro por su ID y actualizar la tabla de la ventana
    public boolean buscarProductoAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("ID");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Cantidad");
        getModelo().addColumn("Motivo");
        getModelo().addColumn("Fecha");
        
        ArrayList<ProductosElim> productoCoincidentes = new ArrayList<ProductosElim>();
        Object[] producto;
        for(ProductosElim productoTemp : productos){
            if(productoTemp.getId_producto() == ID){
                producto = new Object[]{
                    productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getCantidad(),
                    productoTemp.getMotivo(),productoTemp.getFecha()
                };
                modelo.addRow(producto);
                productoCoincidentes.add(productoTemp);
            }
        }
        return !productoCoincidentes.isEmpty();
    }
    
    
}
