/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.Devoluciones;
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
public class ControladorDevoluciones {
    ArrayList<Devoluciones> devoluciones = new ArrayList<Devoluciones>();
    private DefaultTableModel modelo;
    private Devoluciones devolucionActual;
    private int indiceDevolucionSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorDevoluciones(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("Select * from devolucion order by IDDevolucion");
            else if(orden.equals("FechaASC"))
                resultados = sentencia.executeQuery("Select * from devolucion order by Fecha ASC");
            else if(orden.equals("FechaDESC"))
                resultados = sentencia.executeQuery("Select * from devolucion order by Fecha DESC");
            else
                resultados = sentencia.executeQuery("Select * from devolucion order by IDDevolucion");
            while(resultados.next()){
                devoluciones.add(
                    new Devoluciones(
                            resultados.getInt("IDDevolucion"),
                            resultados.getInt("IDVenta"),
                            resultados.getInt("IDProducto"),
                            resultados.getInt("IDProveedor"),
                            resultados.getString("DefectoP"),
                            resultados.getString("Fecha"),
                            resultados.getString("TipoDev"),
                            resultados.getInt("Cantidad")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceDevolucionSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Devolucion");
        getModelo().addColumn("Venta");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Proveedor");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Defecto");
        getModelo().addColumn("Tipo");
        getModelo().addColumn("Cantidad");
        
        Object[] devolucion;
        for(Devoluciones devolucionTemp : devoluciones){
            devolucion = new Object[]{
                devolucionTemp.getId_devolucion(),devolucionTemp.getId_venta(),devolucionTemp.getId_producto(),
                devolucionTemp.getId_proveedor(),devolucionTemp.getFecha(),devolucionTemp.getDefecto(),
                devolucionTemp.getTipo(),devolucionTemp.getCantidad()
            };
            
            getModelo().addRow(devolucion);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the devolucionActual
     */
    public Devoluciones getDevolucionActual() {
        return devolucionActual;
    }

    /**
     * @param devolucionActual the devolucionActual to set
     */
    public void setDevolucionActual(Devoluciones devolucionActual) {
        this.devolucionActual = devolucionActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonDevolucion){
        indiceDevolucionSeleccionado = renglonDevolucion;
        setDevolucionActual(devoluciones.get(renglonDevolucion));
    }
    
    //función para buscar a un registro por su ID
    public Devoluciones buscarDevPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Devoluciones devolucionEncontrado = null;
        
        for(Devoluciones devTemp : devoluciones){
            if(devTemp.getId_devolucion()==ID){
                devolucionEncontrado = devTemp;
                break;
            }
        }
        return devolucionEncontrado;
    }
    
    //función para buscar a un registro por su ID y actualizar la tabla de la ventana
    public boolean buscarDevAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Devolucion");
        getModelo().addColumn("Venta");
        getModelo().addColumn("Producto");
        getModelo().addColumn("Proveedor");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Defecto");
        getModelo().addColumn("Tipo");
        getModelo().addColumn("Cantidad");
        
        ArrayList<Devoluciones> devCoincidentes = new ArrayList<Devoluciones>();
        Object[] devolucion;
        for(Devoluciones devolucionTemp : devoluciones){
            if(devolucionTemp.getId_devolucion() == ID){
                devolucion = new Object[]{
                    devolucionTemp.getId_devolucion(),devolucionTemp.getId_venta(),devolucionTemp.getId_producto(),
                    devolucionTemp.getId_proveedor(),devolucionTemp.getFecha(),devolucionTemp.getDefecto(),
                    devolucionTemp.getTipo(),devolucionTemp.getCantidad()
                };
                modelo.addRow(devolucion);
                devCoincidentes.add(devolucionTemp);
            }
        }
        return !devCoincidentes.isEmpty();
    }
    
    //funcion que sirve para agregar un registro
    public boolean agregarDevolucion(Devoluciones devolucion) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO devolucion " 
                    + "(IDDevolucion,IDVenta,IDProducto,IDProveedor,DefectoP,Fecha,TipoDev,Cantidad) "
                    + "VALUES (IDDevolucion,?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, devolucion.getId_venta());
            sentencia.setInt(2, devolucion.getId_producto());
            sentencia.setInt(3, devolucion.getId_proveedor());
            sentencia.setString(4, devolucion.getDefecto());
            sentencia.setString(5, devolucion.getFecha());
            sentencia.setString(6, devolucion.getTipo());
            sentencia.setInt(7, devolucion.getCantidad());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            devoluciones.add(devolucion);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    
}
