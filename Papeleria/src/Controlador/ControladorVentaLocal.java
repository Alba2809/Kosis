/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Modelo.VentaLocal;
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
public class ControladorVentaLocal {
    ArrayList<VentaLocal> ventas = new ArrayList<VentaLocal>();
    private DefaultTableModel modelo;
    private VentaLocal ventaLocalActual;
    private int indiceVentaLocalSeleccionado;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorVentaLocal(String orden){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if(orden.equals("Folio"))
                resultados = sentencia.executeQuery("Select * from ventalocal order by IDVentaLocal");
            else if(orden.equals("FechaASC"))
                resultados = sentencia.executeQuery("Select * from ventalocal order by Fecha ASC");
            else if(orden.equals("FechaDESC"))
                resultados = sentencia.executeQuery("Select * from ventalocal order by Fecha DESC");
            else
                resultados = sentencia.executeQuery("Select * from ventalocal order by IDVentaLocal");
            while(resultados.next()){
                ventas.add(
                    new VentaLocal(
                            resultados.getInt("IDVentaLocal"),
                            resultados.getInt("IDClienteLocal"),
                            resultados.getInt("NumFactura"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Fecha"),
                            resultados.getString("FormaPago")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        
        actualizarModelo();
        indiceVentaLocalSeleccionado=0;
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Folio");
        getModelo().addColumn("Cliente");
        getModelo().addColumn("Factura");
        getModelo().addColumn("Precio_Total");
        getModelo().addColumn("Productos");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Forma_Pago");
        
        Object[] venta;
        for(VentaLocal ventaTemp : ventas){
            venta = new Object[]{
                ventaTemp.getId_venta_local(),ventaTemp.getId_cliente_local(),ventaTemp.getNum_factura(),
                ventaTemp.getPrecio_total(),ventaTemp.getCant_productos(),ventaTemp.getFecha(),
                ventaTemp.getForma_pago()
            };
            
            getModelo().addRow(venta);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * @return the ventaLocalActual
     */
    public VentaLocal getVentaLocalActual() {
        return ventaLocalActual;
    }

    /**
     * @param ventaLocalActual the ventaLocalActual to set
     */
    public void setVentaLocalActual(VentaLocal ventaLocalActual) {
        this.ventaLocalActual = ventaLocalActual;
    }
    
    //funcion que sirve para actualiza la habitacion actual e indice de la habitacion seleccionada
    public void Selecciona(int renglonVentaL){
        indiceVentaLocalSeleccionado = renglonVentaL;
        setVentaLocalActual(ventas.get(renglonVentaL));
    }
    
    //función para buscar a un registro por su ID
    public VentaLocal buscarVentaLPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        VentaLocal ventaEncontrado = null;
        
        for(VentaLocal ventaTemp : ventas){
            if(ventaTemp.getId_venta_local()==ID){
                ventaEncontrado = ventaTemp;
                break;
            }
        }
        return ventaEncontrado;
    }
    
    public boolean buscarVentaLAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        getModelo().addColumn("Folio");
        getModelo().addColumn("Cliente");
        getModelo().addColumn("Factura");
        getModelo().addColumn("Precio_Total");
        getModelo().addColumn("Productos");
        getModelo().addColumn("Fecha");
        getModelo().addColumn("Forma_pago");
        
        ArrayList<VentaLocal> ventaCoincidentes = new ArrayList<VentaLocal>();
        Object[] venta;
        for(VentaLocal ventaTemp : ventas){
            if(ventaTemp.getId_venta_local() == ID){
                venta = new Object[]{
                    ventaTemp.getId_venta_local(),ventaTemp.getId_cliente_local(),ventaTemp.getNum_factura(),
                    ventaTemp.getPrecio_total(),ventaTemp.getCant_productos(),ventaTemp.getFecha(),
                    ventaTemp.getForma_pago()
                };
                modelo.addRow(venta);
                ventaCoincidentes.add(ventaTemp);
            }
        }
        return !ventaCoincidentes.isEmpty();
    }
    
    //funcion que sirve para agregar a un venta
    public boolean agregarVentaL(VentaLocal venta) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO ventalocal " 
                    + "(IDVentaLocal,IDClienteLocal,NumFactura,PrecioTotal,CantProductos,Fecha,FormaPago) "
                    + "VALUES (?,Null,Null,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, venta.getId_venta_local());
            sentencia.setFloat(2, venta.getPrecio_total());
            sentencia.setInt(3, venta.getCant_productos());
            sentencia.setString(4, venta.getFecha());
            sentencia.setString(5, venta.getForma_pago());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            ventas.add(venta);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaVenta() {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE ventalocal "
                    + "SET IDClienteLocal = ?, "
                    + "NumFactura = ? "
                    + "WHERE IDVentaLocal = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setInt(1, ventaLocalActual.getId_cliente_local());
            sentencia.setInt(2, ventaLocalActual.getNum_factura());
            sentencia.setInt(3, ventaLocalActual.getId_venta_local());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
}
