/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Ventanas.AlertaWarning;
import java.awt.Frame;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author josei
 */
public class Conexion{
    
    private static final Conexion conexion = new Conexion();
    private final String url = "jdbc:mysql://localhost:3306/papeleria";
    private final String usuario = "";
    private final String password = "";
    private static Connection conector;
    private boolean terminar;
    
    private Frame findActiveFrame() {
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
    
    //creacion del constructor que servirá para la conexión a la base de datos
    private Conexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conector = DriverManager.getConnection(url,usuario,password);
            System.out.println("Conexión Exitosa");
        } catch (SQLException ex){
            Frame ventana = findActiveFrame();
            if(ventana != null){
                String m = "<html><center>" + "Error con la conexión a la base de datos.<p>Revise su conexión a internet.<center></html>";
                AlertaWarning salir = new AlertaWarning(ventana, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Error con la conexión a la base de datos. Revise su conexión a internet.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            System.exit(0);
            //System.out.println("Error al abrir Conexion: " + ex.getMessage());
        } catch(ClassNotFoundException ex){
            Frame ventana = findActiveFrame();
            if(ventana != null){
                String m = "<html><center>" + "Error en librería de mysql.<p>Comuniquese con los desarrolladores.<center></html>";
                AlertaWarning salir = new AlertaWarning(ventana, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Error en librería de mysql. Comuniquese con los desarrolladores.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            
            System.exit(0);
            //System.out.println("Clase no encontrada: " + ex.getMessage());
        }
    }
    
    /**
     * @return the conexion
     */
    public static Conexion getConexion() {
        return conexion;
    }

    /**
     * @return the conector
     */
    public static Connection getConector() {
        return conector;
    }    

    /**
     * @return the terminar
     */
    public boolean isTerminar() {
        return terminar;
    }

    /**
     * @param terminar the terminar to set
     */
    public void setTerminar(boolean terminar) {
        this.terminar = terminar;
    }
    
}
