/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.Alerta;
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
public class ControladorAlerta {
    ArrayList<Alerta> alertas = new ArrayList<Alerta>();
    private DefaultTableModel modelo;
    private Alerta alertaActual;
    private int indiceAlertaSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    private int tipo;
    
    public ControladorAlerta(int tipo){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from alerta where Estado = 'NL' order by IDProducto");
            while(resultados.next()){
                alertas.add(
                    new Alerta(
                            resultados.getInt("IDProducto"),
                            resultados.getString("Aviso"),
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
            modelo = new DefaultTableModel(new String[]{"Producto", "Aviso", "Fecha"," "}, 0){
                Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            };

            Object[] alerta;
            for(Alerta alertaTemp : alertas){
                alerta = new Object[]{
                    alertaTemp.getId_producto(),saltosDeLinea(alertaTemp.getAviso()),alertaTemp.getFecha(),false
                };

                getModelo().addRow(alerta);

            }
        }else{
            modelo = new DefaultTableModel(new String[]{"Producto", "Aviso", "Fecha"}, 0){
                Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            };

            Object[] alerta;
            for(Alerta alertaTemp : alertas){
                alerta = new Object[]{
                    alertaTemp.getId_producto(),saltosDeLinea(alertaTemp.getAviso()),alertaTemp.getFecha()
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
    public Alerta getAlertaActual() {
        return alertaActual;
    }

    /**
     * @param alertaActual the alertaActual to set
     */
    public void setAlertaActual(Alerta alertaActual) {
        this.alertaActual = alertaActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonAlerta){
        indiceAlertaSeleccionado = renglonAlerta;
        setAlertaActual(alertas.get(renglonAlerta));
    }
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarAlerta(int ID,String fecha){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM alerta "
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
    
    
}
