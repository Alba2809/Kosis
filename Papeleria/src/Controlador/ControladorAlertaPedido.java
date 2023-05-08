/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.AlertaPedido;
import Modelo.Conexion;
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
public class ControladorAlertaPedido {
    ArrayList<AlertaPedido> alertasP = new ArrayList<AlertaPedido>();
    private DefaultTableModel modelo;
    private AlertaPedido alertaActual;
    private int indiceAlertaSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    private int tipo;
    
    public ControladorAlertaPedido(int tipo){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from alerta_pedido where Estado = 'NL' order by IDProducto");
            while(resultados.next()){
                alertasP.add(
                    new AlertaPedido(
                            resultados.getInt("IDProducto"),
                            resultados.getInt("Cantidad"),
                            resultados.getString("Razon"),
                            resultados.getString("Fecha"),
                            resultados.getString("Estado")
                    ));
                
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        this.tipo = tipo;
        actualizarModelo();
        indiceAlertaSeleccionado=0;
    }
    public String saltosDeLinea(String descripcion) {
        String convertido = null;
        String sinSaltos = descripcion.replaceAll(",", "<p> ");
        convertido = "<html>" + sinSaltos + " </html>";
        return convertido;
    }
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        //tipo == 0  --  Area Compras
        //tipo != 0 --  Area Ventas
        if(tipo == 0){
            modelo = new DefaultTableModel(new String[]{"Producto", "Mensaje", "Fecha"," "}, 0){
                Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            };

            Object[] alerta;
            for(AlertaPedido alertaTemp : alertasP){
                alerta = new Object[]{
                    alertaTemp.getId_producto(),saltosDeLinea("¡Hacen falta " + alertaTemp.getCantidad() + " de este, producto! ( " + alertaTemp.getRazon() + " )"),alertaTemp.getFecha(),false
                };

                getModelo().addRow(alerta);

            }
        }else{
            modelo = new DefaultTableModel(new String[]{"Producto", "Mensaje", "Fecha"}, 0){
                Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            };

            Object[] alerta;
            for(AlertaPedido alertaTemp : alertasP){
                alerta = new Object[]{
                    alertaTemp.getId_producto(),saltosDeLinea("¡Hacen falta " + alertaTemp.getCantidad() + " de este producto! ( " + alertaTemp.getRazon() + " )"),alertaTemp.getFecha()
                };

                getModelo().addRow(alerta);

            }
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the alertaActual
     */
    public AlertaPedido getAlertaActual() {
        return alertaActual;
    }

    /**
     * @param alertaActual the alertaActual to set
     */
    public void setAlertaActual(AlertaPedido alertaActual) {
        this.alertaActual = alertaActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonAlerta){
        indiceAlertaSeleccionado = renglonAlerta;
        setAlertaActual(alertasP.get(renglonAlerta));
    }
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarAlerta(int ID,String fecha){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM alerta_pedido "
                    + "WHERE IDProducto = ? and Fecha = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            sentencia.setString(2, fecha);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    //funcion que sirve para agregar a un registro
    public boolean agregarAlerta(AlertaPedido alerta) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO alerta_pedido " 
                    + "(IDProducto, Cantidad, Razon, Fecha, Estado) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, alerta.getId_producto());
            sentencia.setInt(2, alerta.getCantidad());
            sentencia.setString(3, alerta.getRazon());
            sentencia.setString(4, alerta.getFecha());
            sentencia.setString(5, alerta.getEstado());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            alertasP.add(alerta);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }

}
