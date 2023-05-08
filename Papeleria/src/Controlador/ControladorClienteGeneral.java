/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.ClienteGeneral;
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
public class ControladorClienteGeneral {
    ArrayList<ClienteGeneral> clientesG = new ArrayList<ClienteGeneral>();
    private DefaultTableModel modelo;
    private ClienteGeneral clienteGActual;
    private int indiceClienteGSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorClienteGeneral(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("ID"))
                resultados = sentencia.executeQuery("SELECT * FROM (SELECT * FROM clientelocal UNION ALL SELECT IDClienteWeb,Nombre,Apellidos,Edad,Correo,Telefono,RFC,DomicilioF,NomEmpresa FROM clienteweb) a ORDER BY IDClienteLocal;");
            else if(orden.equals("Apellidos"))
                resultados = sentencia.executeQuery("SELECT * FROM (SELECT * FROM clientelocal UNION ALL SELECT IDClienteWeb,Nombre,Apellidos,Edad,Correo,Telefono,RFC,DomicilioF,NomEmpresa FROM clienteweb) a ORDER BY Apellidos;");
            else if(orden.equals("Nombre"))
                resultados = sentencia.executeQuery("SELECT * FROM (SELECT * FROM clientelocal UNION ALL SELECT IDClienteWeb,Nombre,Apellidos,Edad,Correo,Telefono,RFC,DomicilioF,NomEmpresa FROM clienteweb) a ORDER BY Nombre;");
            else
                resultados = sentencia.executeQuery("SELECT * FROM (SELECT * FROM clientelocal UNION ALL SELECT IDClienteWeb,Nombre,Apellidos,Edad,Correo,Telefono,RFC,DomicilioF,NomEmpresa FROM clienteweb) a ORDER BY IDClienteLocal;");
            while(resultados.next()){
                clientesG.add(
                    new ClienteGeneral(
                            resultados.getInt("IDClienteLocal"),
                            resultados.getString("Nombre"),
                            resultados.getString("Apellidos"),
                            resultados.getInt("Edad"),
                            resultados.getString("Correo"),
                            resultados.getString("Telefono"),
                            resultados.getString("RFC"),
                            resultados.getString("DomicilioF"),
                            resultados.getString("NomEmpresa")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceClienteGSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre (s)");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Edad");
        modelo.addColumn("Teléfono");
        modelo.addColumn("RFC");
        modelo.addColumn("Domicilio Fiscal");
        modelo.addColumn("Empresa");
        modelo.addColumn("Correo");
        
        Object[] cliente;
        for(ClienteGeneral clienteTemp : clientesG){
            cliente = new Object[]{
                clienteTemp.getId_cliente(),clienteTemp.getNombre(),clienteTemp.getApellidos(),
                clienteTemp.getEdad(),clienteTemp.getTelefono(),clienteTemp.getRfc(),clienteTemp.getDomicilio_f(),
                clienteTemp.getNom_empresa(),clienteTemp.getCorreo()
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
     * @return the clienteGActual
     */
    public ClienteGeneral getClienteGActual() {
        return clienteGActual;
    }

    /**
     * @param clienteGActual the clienteGActual to set
     */
    public void setClienteGActual(ClienteGeneral clienteGActual) {
        this.clienteGActual = clienteGActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonClienteG){
        indiceClienteGSeleccionado = renglonClienteG;
        setClienteGActual(clientesG.get(renglonClienteG));
    }
    
    //función para buscar a un registro por su ID
    public ClienteGeneral buscarClienteLPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        ClienteGeneral clienteEncontrado = null;
        
        for(ClienteGeneral clienteTemp : clientesG){
            if(clienteTemp.getId_cliente()==ID){
                clienteEncontrado = clienteTemp;
                break;
            }
        }
        return clienteEncontrado;
    }
    
    public boolean buscarClienteGAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre (s)");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Edad");
        modelo.addColumn("Teléfono");
        modelo.addColumn("RFC");
        modelo.addColumn("Domicilio Fiscal");
        modelo.addColumn("Empresa");
        modelo.addColumn("Correo");
                
        ArrayList<ClienteGeneral> clienteCoincidentes = new ArrayList<ClienteGeneral>();
        Object[] cliente;
        for(ClienteGeneral clienteTemp : clientesG){
            if(clienteTemp.getId_cliente()== ID){
                cliente = new Object[]{
                    clienteTemp.getId_cliente(),clienteTemp.getNombre(),clienteTemp.getApellidos(),
                    clienteTemp.getEdad(),clienteTemp.getTelefono(),clienteTemp.getRfc(),clienteTemp.getDomicilio_f(),
                    clienteTemp.getNom_empresa(),clienteTemp.getCorreo()
                };
                modelo.addRow(cliente);
                clienteCoincidentes.add(clienteTemp);
            }
        }
        return !clienteCoincidentes.isEmpty();
    }
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarClienteL(int ID){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM clientelocal "
                    + "WHERE IDClienteLocal = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para eliminar un registro
    public boolean eliminarClienteW(int ID){
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String borrar = "DELETE FROM clienteweb "
                    + "WHERE IDClienteWeb = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(borrar);
            sentencia.setInt(1, ID);
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
}
