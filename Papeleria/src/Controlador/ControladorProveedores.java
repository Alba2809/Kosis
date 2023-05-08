/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.Proveedor;
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
public class ControladorProveedores {
    ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
    private DefaultTableModel modelo;
    private Proveedor proveedorActual;
    private int indiceProveedorSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorProveedores(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("Select * from proveedor where Estado = 'NE' order by IDProveedor");
            else if(orden.equals("Nombre"))
                resultados = sentencia.executeQuery("Select * from proveedor where Estado = 'NE' order by NombreProv");
            else
                resultados = sentencia.executeQuery("Select * from proveedor where Estado = 'NE' order by IDProveedor");
            while(resultados.next()){
                proveedores.add(
                    new Proveedor(
                            resultados.getInt("IDProveedor"),
                            resultados.getString("NombreProv"),
                            resultados.getInt("NumPedidos"),
                            resultados.getString("Telefono"),
                            resultados.getString("Direccion")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceProveedorSeleccionado=0;        
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Dirección");
        modelo.addColumn("Compras realizadas");
        
        Object[] cliente;
        for(Proveedor proveedorTemp : proveedores){
            cliente = new Object[]{
                proveedorTemp.getId_proveedor(),proveedorTemp.getNombre(),proveedorTemp.getTelefono(),
                proveedorTemp.getDireccion(), proveedorTemp.getNum_pedidos()
            };
            
            getModelo().addRow(cliente);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the proveedorActual
     */
    public Proveedor getClienteLActual() {
        return proveedorActual;
    }

    /**
     * @param proveedorActual the proveedorActual to set
     */
    public void setClienteLActual(Proveedor proveedorActual) {
        this.proveedorActual = proveedorActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonClienteL){
        indiceProveedorSeleccionado = renglonClienteL;
        setClienteLActual(proveedores.get(renglonClienteL));
    }
    
    
    //función para buscar a un registro por su ID
    public Proveedor buscarProveedorPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Proveedor proveedorEncontrado = null;
        
        for(Proveedor proveedorTemp : proveedores){
            if(proveedorTemp.getId_proveedor()==ID){
                proveedorEncontrado = proveedorTemp;
                break;
            }
        }
        return proveedorEncontrado;
    }
    
    //funcion que sirve para agregar un registro
    public boolean agregarProveedor(Proveedor proveedor) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO proveedor " 
                    + "(IDProveedor,NombreProv,NumPedidos,Telefono,Direccion,Estado) "
                    + "VALUES (IDProveedor,?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setString(1, proveedor.getNombre());
            sentencia.setInt(2, proveedor.getNum_pedidos());
            sentencia.setString(3, proveedor.getTelefono());
            sentencia.setString(4, proveedor.getDireccion());
            sentencia.setString(5, "NE");
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            proveedores.add(proveedor);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funciona que 'elimina' un producto
    public boolean eliminarProveedor(int ID){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "UPDATE proveedor Set Estado = 'E' "
                    + "WHERE IDProveedor = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar un registro
    public boolean actualizaProveedor(Proveedor proveedor) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE proveedor "
                    + "SET NombreProv = ?,"
                    + "Telefono = ?, "
                    + "Direccion = ? "
                    + "WHERE IDProveedor = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setString(1, proveedor.getNombre());
            sentencia.setString(2, proveedor.getTelefono());
            sentencia.setString(3, proveedor.getDireccion());
            sentencia.setInt(4, proveedor.getId_proveedor());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar un registro
    public boolean actualizaProveedorP(int ID) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE proveedor SET NumPedidos = NumPedidos + 1 WHERE IDProveedor IN (SELECT DISTINCT pr.IDProveedor FROM productoscompra pc JOIN productos p ON pc.IDProducto = p.IDProducto JOIN proveedor pr ON p.IDProveedor = pr.IDProveedor WHERE pc.IDCompra = ?);";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //función para buscar a un registro por su ID y actualizar la tabla de la ventana
    public boolean buscarProveedorAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Dirección");
        modelo.addColumn("Compras realizadas");
        
        
        ArrayList<Proveedor> proveedorCoincidentes = new ArrayList<Proveedor>();
        Object[] proveedor;
        for(Proveedor proveedorTemp : proveedores){
            if(proveedorTemp.getId_proveedor() == ID){
                proveedor = new Object[]{
                    proveedorTemp.getId_proveedor(),proveedorTemp.getNombre(),proveedorTemp.getTelefono(),
                    proveedorTemp.getDireccion(),proveedorTemp.getNum_pedidos()
                };
                modelo.addRow(proveedor);
                proveedorCoincidentes.add(proveedorTemp);
            }
        }
        return !proveedorCoincidentes.isEmpty();
    }
    
}
