/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Ganancias;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josei
 */
public class ControladorGanancias {
    ArrayList<Ganancias> ganancias = new ArrayList<Ganancias>();
    private DefaultTableModel modelo;
    //private Ganancias gananciaActual;
    protected Connection conectar = null;
    private Statement sentencia;
    private ResultSet resultados;
    
    public ControladorGanancias(int modo,String orden,String auxiliar){        
        try{
            conectar = Conexion.getConexion().getConector();
            sentencia = conectar.createStatement();
            //se realiza una consulta
            if((orden.equals("Folio")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta GROUP BY pv.IDVenta) ORDER BY IDVentaLocal;");
            else if((orden.equals("FechaASC")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta GROUP BY pv.IDVenta) ORDER BY Fecha ASC;");
            else if((orden.equals("FechaDESC")) && (modo == 0))
                resultados = sentencia.executeQuery("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta GROUP BY pv.IDVenta) ORDER BY Fecha DESC;");
            else if(modo == 1){
                //esta consulta es para obtener las ganancias de un día
                //orden = Fecha del día
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta WHERE vl.Fecha = ? GROUP BY pv.IDVenta) ORDER BY IDVentaLocal;");
                miSentencia.setString(1, orden);
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else if(modo == 2){
                //esta consulta es para obtener las ganancias de una semama
                //orden = Fecha inicial de la semana
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta WHERE vl.Fecha BETWEEN ? AND DATE_ADD(?, INTERVAL 6 DAY) GROUP BY pv.IDVenta) ORDER BY IDVentaLocal;");
                miSentencia.setString(1, orden);
                miSentencia.setString(2, orden);
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else if(modo == 3){
                //esta consulta es para buscar las ganancias por mes
                //orden = Mes
                //auxiliar = Año
                PreparedStatement miSentencia = conectar.prepareStatement("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta WHERE MONTH(vl.Fecha) = ? AND YEAR(vl.Fecha) = ? GROUP BY pv.IDVenta) ORDER BY IDVentaLocal;");
                miSentencia.setInt(1, Integer.parseInt(orden));
                miSentencia.setInt(2, Integer.parseInt(auxiliar));
                //se realiza una consulta
                resultados = miSentencia.executeQuery();
            }
            else
                resultados = sentencia.executeQuery("(SELECT vl.IDVentaLocal,vl.IDClienteLocal,vl.CantProductos,vl.Fecha,vl.PrecioTotal,SUM(if(pv.EstadoP = 'Funcional', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), if(pv.EstadoP = 'Funcional_Cambiado', (pv.PrecioTotal - (pv.CantProducto * pv.PrecioCompra)), 0))) AS Ganancia FROM ventalocal vl JOIN productosventa pv ON vl.IDVentaLocal = pv.IDVenta GROUP BY pv.IDVenta) ORDER BY IDVentaLocal;");
            while(resultados.next()){
                ganancias.add(
                    new Ganancias(
                            resultados.getInt("IDVentaLocal"),
                            resultados.getInt("IDClienteLocal"),
                            resultados.getInt("CantProductos"),
                            resultados.getString("Fecha"),
                            resultados.getFloat("PrecioTotal"),
                            resultados.getFloat("Ganancia")
                    ));
            };
        } catch(SQLException ex){
            System.out.println("Error en la conexión: " + ex.getMessage());
        }
        if(modo == 0)
            actualizarModelo();
        else
            buscarGanancias();
    }
    
    //funcion que sirve para actualizar el modelo
    public void actualizarModelo(){
        modelo = new DefaultTableModel();
        modelo.addColumn("Folio");
        modelo.addColumn("Cliente");
        modelo.addColumn("Cantidad Productos");
        modelo.addColumn("Fecha");
        modelo.addColumn("Total Venta");
        modelo.addColumn("Ganancia");
        
        Object[] ganancia;
        for(Ganancias gananciaTemp : ganancias){
            ganancia = new Object[]{
                gananciaTemp.getFolio(),gananciaTemp.getCliente(),gananciaTemp.getCant_productos(),gananciaTemp.getFecha(),
                gananciaTemp.getTotal_venta(),gananciaTemp.getGanancia()
            };
            
            getModelo().addRow(ganancia);
        }
    }
    //este mdelo se utilizará para las tablas de calculo de ganancias
    public void buscarGanancias(){
        modelo = new DefaultTableModel();
        getModelo().addColumn("Código");
        getModelo().addColumn("Ganancia");
        
        Object[] ganancia;
        for(Ganancias gananciaTemp : ganancias){
            ganancia = new Object[]{
                gananciaTemp.getFolio(),gananciaTemp.getGanancia()
            };
            
            getModelo().addRow(ganancia);
        }
    }

    /**
     * @return the modelo
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }
    
    //controlador que sirve para buscar una ganancia(s) y mostralo en una tabla
    public boolean buscarGananciaAct(String cadenaBusqueda){
        int ID = Integer.parseInt(cadenaBusqueda);
        modelo = new DefaultTableModel();
        modelo.addColumn("Folio");
        modelo.addColumn("Cliente");
        modelo.addColumn("Cantidad Productos");
        modelo.addColumn("Fecha");
        modelo.addColumn("Total Venta");
        modelo.addColumn("Ganancia");
        
        ArrayList<Ganancias> gananciaCoincidentes = new ArrayList<Ganancias>();
        Object[] ganancia;
        for(Ganancias gananciaTemp : ganancias){
            if(gananciaTemp.getFolio() == ID){
                ganancia = new Object[]{
                    gananciaTemp.getFolio(),gananciaTemp.getCliente(),gananciaTemp.getCant_productos(),gananciaTemp.getFecha(),
                    gananciaTemp.getTotal_venta(),gananciaTemp.getGanancia()
                };
                modelo.addRow(ganancia);
                gananciaCoincidentes.add(gananciaTemp);
            }
        }
        return !gananciaCoincidentes.isEmpty();
    }
}
