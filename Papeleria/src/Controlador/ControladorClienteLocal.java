/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.ClienteLocal;
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
public class ControladorClienteLocal {
    ArrayList<ClienteLocal> clientesL = new ArrayList<ClienteLocal>();
    private DefaultTableModel modelo;
    private ClienteLocal clienteLActual;
    private int indiceClienteLSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorClienteLocal(){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            resultados = sentencia.executeQuery("Select * from clientelocal order by IDClienteLocal");
            while(resultados.next()){
                clientesL.add(
                    new ClienteLocal(
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
        indiceClienteLSeleccionado=0;
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
        for(ClienteLocal clienteTemp : clientesL){
            cliente = new Object[]{
                clienteTemp.getId_cliente_local(),clienteTemp.getNombre(),clienteTemp.getApellidos(),
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
     * @return the clienteLActual
     */
    public ClienteLocal getClienteLActual() {
        return clienteLActual;
    }

    /**
     * @param clienteLActual the clienteLActual to set
     */
    public void setClienteLActual(ClienteLocal clienteLActual) {
        this.clienteLActual = clienteLActual;
    }
    
    //funcion que sirve para actualiza la seleccion
    public void Selecciona(int renglonClienteL){
        indiceClienteLSeleccionado = renglonClienteL;
        setClienteLActual(clientesL.get(renglonClienteL));
    }
    
    //función para buscar a un registro por su ID
    public ClienteLocal buscarClienteLPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        ClienteLocal clienteEncontrado = null;
        
        for(ClienteLocal clienteTemp : clientesL){
            if(clienteTemp.getId_cliente_local()==ID){
                clienteEncontrado = clienteTemp;
                break;
            }
        }
        return clienteEncontrado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaClienteL() {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE clientelocal "
                    + "SET Nombre = ?,"
                    + "Apellidos = ?,"
                    + "Edad = ?,"
                    + "Correo = ?,"
                    + "Telefono = ?,"
                    + "RFC = ?, "
                    + "DomicilioF = ?, "
                    + "NomEmpresa = ? "
                    + "WHERE IDClienteLocal = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setString(1, clienteLActual.getNombre());
            sentencia.setString(2, clienteLActual.getApellidos());
            sentencia.setInt(3, clienteLActual.getEdad());
            sentencia.setString(4, clienteLActual.getCorreo());
            sentencia.setString(5, clienteLActual.getTelefono());
            sentencia.setString(6, clienteLActual.getRfc());
            sentencia.setString(7, clienteLActual.getDomicilio_f());
            sentencia.setString(8, clienteLActual.getNom_empresa());
            sentencia.setInt(9, clienteLActual.getId_cliente_local());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para agregar a un registro
    public boolean agregarClienteL(ClienteLocal cliente) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO clientelocal " 
                    + "(IDClienteLocal, Nombre, Apellidos, Edad, Correo, Telefono, RFC, DomicilioF, NomEmpresa) "
                    + "VALUES (IDClienteLocal,?,?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellidos());
            sentencia.setInt(3, cliente.getEdad());
            sentencia.setString(4, cliente.getCorreo());
            sentencia.setString(5, cliente.getTelefono());
            sentencia.setString(6, cliente.getRfc());
            sentencia.setString(7, cliente.getDomicilio_f());
            sentencia.setString(8, cliente.getNom_empresa());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            clientesL.add(cliente);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
}
