/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.Compra;
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
public class ControladorCompras {
    ArrayList<Compra> compras = new ArrayList<Compra>();
    private DefaultTableModel modelo;
    private Compra compraActual;
    private int indiceCompraSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorCompras(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("Select * from compra order by IDCompra");
            else if(orden.equals("FechaASC"))
                resultados = sentencia.executeQuery("Select * from compra order by Fecha ASC");
            else if(orden.equals("FechaDESC"))
                resultados = sentencia.executeQuery("Select * from compra order by Fecha DESC");
            else
                resultados = sentencia.executeQuery("Select * from compra order by IDCompra");
            while(resultados.next()){
                compras.add(
                    new Compra(
                            resultados.getInt("IDCompra"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getString("Fecha"),
                            resultados.getInt("CantProductos")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceCompraSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Cantidad Productos");
        getModelo().addColumn("Precio Total");
        
        Object[] compra;
        for(Compra compraTemp : compras){
            compra = new Object[]{
                compraTemp.getId_compra(),compraTemp.getFecha(),compraTemp.getCant_productos(),compraTemp.getPrecio_total()
            };
            
            getModelo().addRow(compra);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the compraActual
     */
    public Compra getCompraActual() {
        return compraActual;
    }

    /**
     * @param compraActual the compraActual to set
     */
    public void setCompraActual(Compra compraActual) {
        this.compraActual = compraActual;
    }
    
    //funcion que sirve para actualiza la habitacion actual e indice de la habitacion seleccionada
    public void Selecciona(int renglonCompra){
        indiceCompraSeleccionado = renglonCompra;
        setCompraActual(compras.get(renglonCompra));
    }
    
    //función para buscar a un registro por su ID
    public Compra buscarCompraPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Compra compraEncontrado = null;
        
        for(Compra compraTemp : compras){
            if(compraTemp.getId_compra() == ID){
                compraEncontrado = compraTemp;
                break;
            }
        }
        return compraEncontrado;
    }
    
    public boolean buscarCompraAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Cantidad Productos");
        getModelo().addColumn("Precio Total");
        
        ArrayList<Compra> compraCoincidentes = new ArrayList<Compra>();
        Object[] compra;
        for(Compra compraTemp : compras){
            if(compraTemp.getId_compra()== ID){
                compra = new Object[]{
                    compraTemp.getId_compra(),compraTemp.getFecha(),compraTemp.getCant_productos(),compraTemp.getPrecio_total()
                };
                modelo.addRow(compra);
                compraCoincidentes.add(compraTemp);
            }
        }
        return !compraCoincidentes.isEmpty();
    }
    
    //funcion que sirve para agregar a un compra
    public boolean agregarCompra(Compra compra) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO compra " 
                    + "(IDCompra,PrecioTotal,Fecha,CantProductos) "
                    + "VALUES (?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, compra.getId_compra());
            sentencia.setFloat(2, compra.getPrecio_total());
            sentencia.setString(3, compra.getFecha());
            sentencia.setInt(4, compra.getCant_productos());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            compras.add(compra);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    
}
