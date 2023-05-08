/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.Productos;
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
public class ControladorProductos {
    ArrayList<Productos> productos = new ArrayList<Productos>();
    private DefaultTableModel modelo;
    private Productos productoActual;
    private int indiceProductoSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorProductos(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("Select IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite from productos where Estado = 'NE' order by IDProducto");
            else if(orden.equals("Nombre"))
                resultados = sentencia.executeQuery("Select IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite from productos where Estado = 'NE' order by Nombre");
            else if(orden.equals("Cat"))
                resultados = sentencia.executeQuery("Select IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite from productos where Estado = 'NE' order by CatProducto");
            else if(orden.equals("Disp"))
                resultados = sentencia.executeQuery("Select IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite from productos where Estado = 'NE' order by Disponibilidad");
            else
                resultados = sentencia.executeQuery("Select IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite from productos where Estado = 'NE' order by IDProducto");
            while(resultados.next()){
                productos.add(
                    new Productos(
                            resultados.getInt("IDProducto"),
                            resultados.getInt("IDProveedor"),
                            resultados.getFloat("PrecioC"),
                            resultados.getFloat("PrecioV"),
                            resultados.getString("Marca"),
                            resultados.getString("Nombre"),
                            resultados.getString("CatProducto"),
                            resultados.getString("Disponibilidad"),
                            resultados.getInt("Stock"),
                            resultados.getInt("Limite")
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
        getModelo().addColumn("Proveedor");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Precio_Compra");
        getModelo().addColumn("Precio_Venta");
        getModelo().addColumn("Marca");
        getModelo().addColumn("Categoria");
        getModelo().addColumn("Disponibilidad");
        getModelo().addColumn("Stock");
        getModelo().addColumn("Limite");
        
        Object[] producto;
        for(Productos productoTemp : productos){
            producto = new Object[]{
                productoTemp.getId_producto(),productoTemp.getId_proveedor(),productoTemp.getNombre(),
                productoTemp.getPrecioC(),productoTemp.getPrecioV(),productoTemp.getMarca(),productoTemp.getCat_producto(),
                productoTemp.getDisponibilidad(),productoTemp.getStock(),productoTemp.getLimite()
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
    public Productos getProductoActual() {
        return productoActual;
    }

    /**
     * @param productoActual the productoActual to set
     */
    public void setProductoActual(Productos productoActual) {
        this.productoActual = productoActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonProducto){
        indiceProductoSeleccionado = renglonProducto;
        setProductoActual(productos.get(renglonProducto));
    }
    
    //funcion que sirve para agregar a un registro
    public boolean agregarProducto(Productos producto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO productos " 
                    + "(IDProducto,IDProveedor,PrecioC,PrecioV,Marca,Nombre,CatProducto,Disponibilidad,Stock,Limite,Estado) "
                    + "VALUES (IDProducto,?,?,?,?,?,?,?,?,?,'NE')";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, producto.getId_proveedor());
            sentencia.setFloat(2, producto.getPrecioC());
            sentencia.setFloat(3, producto.getPrecioV());
            sentencia.setString(4, producto.getMarca());
            sentencia.setString(5, producto.getNombre());
            sentencia.setString(6, producto.getCat_producto());
            sentencia.setString(7, producto.getDisponibilidad());
            sentencia.setInt(8, producto.getStock());
            sentencia.setInt(9, producto.getLimite());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            productos.add(producto);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaProducto() {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE productos "
                    + "SET PrecioC = ?,"
                    + "PrecioV = ?,"
                    + "Marca = ?,"
                    + "Nombre = ?,"
                    + "CatProducto = ?,"
                    + "Disponibilidad = ?, "
                    + "Stock = ?, "
                    + "Limite = ?, "
                    + "IDProveedor = ? "
                    + "WHERE IDProducto = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setFloat(1, productoActual.getPrecioC());
            sentencia.setFloat(2, productoActual.getPrecioV());
            sentencia.setString(3, productoActual.getMarca());
            sentencia.setString(4, productoActual.getNombre());
            sentencia.setString(5, productoActual.getCat_producto());
            sentencia.setString(6, productoActual.getDisponibilidad());
            sentencia.setInt(7, productoActual.getStock());
            sentencia.setInt(8, productoActual.getLimite());
            sentencia.setInt(9, productoActual.getId_proveedor());
            sentencia.setInt(10, productoActual.getId_producto());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para eliminar un registro
    //en lugar de eliminarlo por completo de la BD, sólo se marcará como estado eliminado (E)
    //esto se realiza para evitar concurrencia en otro procesos
    public boolean eliminarProducto(int ID){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "UPDATE productos Set Estado = 'E' "
                    + "WHERE IDProducto = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //función para buscar a un registro por su ID
    public Productos buscarProductoPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Productos productoEncontrado = null;
        for(Productos productoTemp : productos){
            if(productoTemp.getId_producto()==ID){
                productoEncontrado = productoTemp;
                break;
            }
        }
        return productoEncontrado;
    }
    
    //función para buscar a un registro por su ID y actualizar la tabla de la ventana
    public boolean buscarProductoAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("ID");
        getModelo().addColumn("Proveedor");
        getModelo().addColumn("Nombre");
        getModelo().addColumn("Precio_Compra");
        getModelo().addColumn("Precio_Venta");
        getModelo().addColumn("Marca");
        getModelo().addColumn("Categoria");
        getModelo().addColumn("Disponibilidad");
        getModelo().addColumn("Stock");
        getModelo().addColumn("Limite");
        
        Productos productoMuestra = new Productos(ID, 0,0, 0,"","","","",0,0);
        
        ArrayList<Productos> productoCoincidentes = new ArrayList<Productos>();
        Object[] producto;
        for(Productos productoTemp : productos){
            if(productoTemp.equals(productoMuestra)){
                producto = new Object[]{
                    productoTemp.getId_producto(),productoTemp.getId_proveedor(),productoTemp.getNombre(),
                    productoTemp.getPrecioC(),productoTemp.getPrecioV(),productoTemp.getMarca(),
                    productoTemp.getCat_producto(),productoTemp.getDisponibilidad(),productoTemp.getStock(),
                    productoTemp.getLimite()
                };
                modelo.addRow(producto);
                productoCoincidentes.add(productoTemp);
            }
        }
        return !productoCoincidentes.isEmpty();
    }
    
    //función que sirve para buscar a un producto por categoria
    public boolean buscarProductosIDCat(int idBuscado,String categoria){        
        modelo = new DefaultTableModel();
        getModelo().addColumn("Codigo");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Precio");
        getModelo().addColumn("Stock");
        
        ArrayList<Productos> productosCoincidentes = new ArrayList<Productos>();
        Object[] producto;
        //se busca al producto con el id, para que aparezca primero en la tabla
        for(Productos productoTemp : productos){
            if(productoTemp.getId_producto()==idBuscado){
                producto = new Object[]{
                    productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getPrecioV(),productoTemp.getStock()
                };
                modelo.addRow(producto);
                productosCoincidentes.add(productoTemp);
            }
        }
        //se buscan los productos con la misma categoria (pero diferente al producto anterior)
        for(Productos productoTemp : productos){
            if((productoTemp.getCat_producto().equals(categoria)) && (productoTemp.getId_producto()!=idBuscado)){
                producto = new Object[]{
                    productoTemp.getId_producto(),productoTemp.getNombre(),productoTemp.getPrecioV(),productoTemp.getStock()
                };
                modelo.addRow(producto);
                productosCoincidentes.add(productoTemp);
            }
        }
        return !productosCoincidentes.isEmpty();
    }
    
    public void actualizarDisponibiilidad(){
        //boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            String actualizar = "UPDATE productos SET Disponibilidad = 'ND' WHERE Stock = 0;";
            sentencia.executeUpdate(actualizar);
            actualizar = "UPDATE productos SET Disponibilidad = 'D' WHERE Stock > 0;";
            sentencia.executeUpdate(actualizar);
            //System.out.println(sentencia);
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        //return resultado;
    }
    
}
