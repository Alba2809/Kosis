/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComponentePDF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author josei
 */
public interface Reporte {
    
    //método que genera el PDF
    //DES Ruta donde se guardará el PDF
    //titulo Es el título que aparecerá dentro del PDF
    
    public abstract void generarReporte(String DES, String titulo) throws SQLException, FileNotFoundException, IOException;

}
