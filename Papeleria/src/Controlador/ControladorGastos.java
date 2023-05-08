/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.Gastos;
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
public class ControladorGastos {
    ArrayList<Gastos> gastos = new ArrayList<Gastos>();
    private DefaultTableModel modelo;
    //private Gastos gananciaActual;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorGastos(int modo,String orden,String auxiliar){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if((orden.equals("Folio")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT * FROM gastos UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra) ORDER BY IDGasto;");
            else if((orden.equals("FechaASC")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT * FROM gastos UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra) ORDER BY Fecha ASC;");
            else if((orden.equals("FechaDESC")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT * FROM gastos UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra) ORDER BY Fecha DESC;");
            else if(modo == 1){
                //esta consulta es para obtener las gastos de un día
                //orden = Fecha del día
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT * FROM gastos WHERE Fecha = ? UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra WHERE Fecha = ?) ORDER BY IDGasto;");
                miSentencia.setString(1, orden);
                miSentencia.setString(2, orden);
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else if(modo == 2){
                //esta consulta es para obtener las gastos de una semama
                //orden = Fecha inicial de la semana
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT * FROM gastos WHERE Fecha BETWEEN ? AND DATE_ADD(?,INTERVAL 6 DAY) UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra WHERE Fecha BETWEEN ? AND DATE_ADD(?,INTERVAL 6 DAY)) ORDER BY IDGasto;");
                miSentencia.setString(1, orden);
                miSentencia.setString(2, orden);
                miSentencia.setString(3, orden);
                miSentencia.setString(4, orden);
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else if(modo == 3){
                //esta consulta es para buscar las gastos por mes
                //orden = Mes
                //auxiliar = Año
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT * FROM gastos WHERE MONTH(Fecha) = ? AND YEAR(Fecha) = ? UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra WHERE MONTH(Fecha) = ? AND YEAR(Fecha) = ?) ORDER BY IDGasto;");
                miSentencia.setInt(1, Integer.parseInt(orden));
                miSentencia.setInt(2, Integer.parseInt(auxiliar));
                miSentencia.setInt(3, Integer.parseInt(orden));
                miSentencia.setInt(4, Integer.parseInt(auxiliar));
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else
                resultados = sentencia.executeQuery("(SELECT * FROM gastos UNION ALL SELECT IDCompra,'Compra de mercancía',Fecha,PrecioTotal,'Resurtido' FROM compra) ORDER BY IDGasto;");
            while(resultados.next()){
                gastos.add(
                    new Gastos(
                            resultados.getInt("IDGasto"),
                            resultados.getString("Nombre"),
                            resultados.getString("Fecha"),
                            resultados.getFloat("Monto"),
                            resultados.getString("Tipo")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        if(modo == 0)
            actualizarModelo();
        else
            buscarGasto();
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Monto");
        modelo.addColumn("Fecha");
        modelo.addColumn("Tipo Gasto");
        
        Object[] gasto;
        for(Gastos gastoTemp : gastos){
            gasto = new Object[]{
                gastoTemp.getId_gasto(),gastoTemp.getNombre(),gastoTemp.getMonto(),gastoTemp.getFecha(),gastoTemp.getTipo()
            };
            
            getModelo().addRow(gasto);
        }
    }
    //este mdelo se utilizará para las tablas de calculo de gastos
    public void buscarGasto(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Monto");
        
        Object[] gasto;
        for(Gastos gastoTemp : gastos){
            gasto = new Object[]{
                gastoTemp.getId_gasto(),gastoTemp.getMonto()
            };
            
            getModelo().addRow(gasto);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }
    
    //controlador que sirve para buscar una ganancia(s) y mostralo en una tabla
    public boolean buscarGastoAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Monto");
        modelo.addColumn("Fecha");
        modelo.addColumn("Tipo Gasto");
        
        ArrayList<Gastos> gastoCoincidentes = new ArrayList<Gastos>();
        Object[] gasto;
        for(Gastos gastoTemp : gastos){
            if(gastoTemp.getId_gasto() == ID){
                gasto = new Object[]{
                    gastoTemp.getId_gasto(),gastoTemp.getNombre(),gastoTemp.getMonto(),gastoTemp.getFecha(),gastoTemp.getTipo()
                };
                modelo.addRow(gasto);
                gastoCoincidentes.add(gastoTemp);
            }
        }
        return !gastoCoincidentes.isEmpty();
    }
    
    //funcion que sirve para agregar a un registro
    public boolean agregarGasto(Gastos gasto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String insertar = "INSERT INTO gastos " 
                    + "(IDGasto, Nombre, Fecha, Monto, Tipo) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement sentencia = conectar.prepareStatement(insertar);
            sentencia.setInt(1, gasto.getId_gasto());
            sentencia.setString(2, gasto.getNombre());
            sentencia.setString(3, gasto.getFecha());
            sentencia.setFloat(4, gasto.getMonto());
            sentencia.setString(5, gasto.getTipo());
            //System.out.println(sentencia);
            resultado = !sentencia.execute();
            gastos.add(gasto);
        }catch(SQLException ex){
            Logger.getLogger(ControladorVentaLocal.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //funcion que sirve para actualizar a un registro
    public boolean actualizaGasto(Gastos gasto) {
        boolean resultado = false;
        try{
            conectar = Conexion.getConexion().getConector();
            String actualizar = "UPDATE gastos "
                    + "SET Nombre = ?,"
                    + "Monto = ?,"
                    + "Fecha = ?,"
                    + "Tipo = ? "
                    + "WHERE IDGasto = ?;";
            PreparedStatement sentencia = conectar.prepareStatement(actualizar);
            sentencia.setString(1, gasto.getNombre());
            sentencia.setFloat(2, gasto.getMonto());
            sentencia.setString(3, gasto.getFecha());
            sentencia.setString(4, gasto.getTipo());
            sentencia.setInt(5, gasto.getId_gasto());
            //System.out.println(sentencia);
            resultado = (1 == sentencia.executeUpdate());
        }catch(SQLException ex){
            Logger.getLogger(ControladorProductos.class.getName()).log(Level.SEVERE,null, ex);
        }
        return resultado;
    }
    
    //función para buscar a un registro por su ID
    public Gastos buscarGastoPorID(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        Gastos gastoEncontrado = null;
        
        for(Gastos gastoTemp : gastos){
            if(gastoTemp.getId_gasto() == ID){
                gastoEncontrado = gastoTemp;
                break;
            }
        }
        return gastoEncontrado;
    }
    
}
