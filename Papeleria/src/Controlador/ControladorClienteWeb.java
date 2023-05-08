/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.ClienteWeb;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author josei
 */
public class ControladorClienteWeb {
    ArrayList<ClienteWeb> clientesW = new ArrayList<ClienteWeb>();
    private DefaultTableModel modelo;
    private ClienteWeb clienteWActual;
    private int indiceClienteWSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorClienteWeb(){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select IDClienteWeb,Nombre,Apellidos,Edad,Correo,Telefono,RFC,DomicilioF,NomEmpresa,Contrasena from clienteweb order by IDClienteWeb");
            while(resultados.next()){
                clientesW.add(
                    new ClienteWeb(
                            resultados.getInt("IDClienteWeb"),
                            resultados.getString("Nombre"),
                            resultados.getString("Apellidos"),
                            resultados.getInt("Edad"),
                            resultados.getString("Correo"),
                            resultados.getString("Telefono"),
                            resultados.getString("RFC"),
                            resultados.getString("DomicilioF"),
                            resultados.getString("NomEmpresa"),
                            resultados.getString("Contrasena")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceClienteWSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Edad");
        modelo.addColumn("Teléfono");
        modelo.addColumn("RFC");
        modelo.addColumn("Domicilio Fiscal");
        modelo.addColumn("Empresa");
        modelo.addColumn("Correo");
        
        Object[] cliente;
        for(ClienteWeb clienteTemp : clientesW){
            cliente = new Object[]{
                clienteTemp.getId_cliente_web(),clienteTemp.getNombre(),clienteTemp.getApellidos(),
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
     * @return the clienteWActual
     */
    public ClienteWeb getClienteWActual() {
        return clienteWActual;
    }

    /**
     * @param clienteWActual the clienteWActual to set
     */
    public void setClienteWActual(ClienteWeb clienteWActual) {
        this.clienteWActual = clienteWActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonClienteW){
        indiceClienteWSeleccionado = renglonClienteW;
        setClienteWActual(clientesW.get(renglonClienteW));
    }
    
    //función para buscar a un registro por su ID
    public ClienteWeb buscarClienteWPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        ClienteWeb clienteEncontrado = null;
        
        for(ClienteWeb clienteTemp : clientesW){
            if(clienteTemp.getId_cliente_web()==ID){
                clienteEncontrado = clienteTemp;
                break;
            }
        }
        return clienteEncontrado;
    }
    
    //función para buscar a un registro por su ID
    public boolean buscarClienteWAct(String cadenaBusqueda){
        modelo = new DefaultTableModel();
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Domicilio Fiscal");
        
        //VentaLocal ventaMuestra = new VentaLocal(ID, 0, 0,0,0,"");
        
        ArrayList<ClienteWeb> clienteCoincidentes = new ArrayList<ClienteWeb>();
        Object[] cliente;
        for(ClienteWeb clienteTemp : clientesW){
            if(clienteTemp.getId_cliente_web() == ID){
                cliente = new Object[]{
                    clienteTemp.getId_cliente_web(),clienteTemp.getNombre(),clienteTemp.getApellidos(),
                    clienteTemp.getTelefono(),clienteTemp.getDomicilio_f()
                };
                modelo.addRow(cliente);
                clienteCoincidentes.add(clienteTemp);
            }
        }
        return !clienteCoincidentes.isEmpty();
    }
}
