/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Notificacion;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josei
 */
public class ControladorNotificaciones {
    ArrayList<Notificacion> notificaciones = new ArrayList<Notificacion>();
    private DefaultTableModel modelo;
    private Notificacion notificacionActual;
    private int indiceNotificacionSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorNotificaciones(){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from notificacion order by IDVentaWeb");
            while(resultados.next()){
                notificaciones.add(
                    new Notificacion(
                            resultados.getInt("IDVentaWeb"),
                            resultados.getString("Mensaje"),
                            resultados.getString("Fecha")
                    ));
                
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceNotificacionSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel(new String[]{"Venta", "Mensaje", "Fecha"}, 0){
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
 
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        
        Object[] notificacion;
        for(Notificacion notificacionTemp : notificaciones){
            notificacion = new Object[]{
                notificacionTemp.getId_venta_web(),notificacionTemp.getMensaje(),notificacionTemp.getFecha()
            };
            
            getModelo().addRow(notificacion);
            
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the notificacionActual
     */
    public Notificacion getNotificacionActual() {
        return notificacionActual;
    }

    /**
     * @param notificacionActual the notificacionActual to set
     */
    public void setNotificacionActual(Notificacion notificacionActual) {
        this.notificacionActual = notificacionActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonNotificacion){
        indiceNotificacionSeleccionado = renglonNotificacion;
        setNotificacionActual(notificaciones.get(renglonNotificacion));
    }
}
