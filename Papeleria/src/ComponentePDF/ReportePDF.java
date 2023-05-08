/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComponentePDF;

import Modelo.Conexion;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.StringTokenizer;

/**
 *
 * @author josei
 */
public class ReportePDF implements Reporte {

    //Crea el objeto Connection
    private Connection con;
    //Script sql para hacer la consulta (ejemplo)
    private String SQL_CONSULTA = "Select IDClienteLocal,Nombre,Apellidos,Edad,Correo,Telefono from clientelocal";
    private String folio;
    //Constructor con un parámetro tipo String, el cúal será la consulta para obtener los
    //productos de la venta
    public ReportePDF(String SQL_CONSULTA,String folio){
        this.SQL_CONSULTA = SQL_CONSULTA;
        this.folio = folio;
    }
    
    //Constructor que recibe la conexión a la BD
    public ReportePDF(Connection c){
        this.con = c;
    }
    //Hilo que se encarga de obtener los datos que tendrá el PDF
    @Override
    public void generarReporte(String DES, String titulo) throws SQLException, FileNotFoundException, IOException {
        //Objeto que escribe en el PDF
        PdfWriter writer = new PdfWriter(DES);
        PdfDocument pdf = new PdfDocument(writer);
        //El PDF tendrá el formato de A4 en forma horizontal
        Document document = new Document(pdf,PageSize.A4.rotate());
        
        //Se agrega encabezado y pie de pagina que esta estructurado en la clase EventoPDF
        EventoPDF evento = new EventoPDF(document,titulo,folio);
        //Se indica que el manejador se encargará del evento END_PAGE
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
        
        //Se agregan margenes a los cuatro lados del pdf
        document.setMargins(75, 150, 75, 150);
        //Se crean dos fuentes para texto, uno para el contenido de la tabla (font)
        //y bold para el titulo y último registro de la tabla
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        
        //Se agregan un array que contiene los titulos de la tabla
        String tituloTabla[] = {"Producto","Cantidad","Precio"};
        //Se crean columnas para la tabla, con un determinado ancho
        float[] anchoColumn = {3,2,2};
        
        //Se crea el objeto tabla
        Table table = new Table(anchoColumn);
        //Se indica que la tabla ocupe el 100% del espacio (respetando margenes)
        table.setWidth(UnitValue.createPercentValue(100));
        
        //Se inicia la conexión con la BD
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            conn = (this.con != null) ? this.con : Conexion.getConector();
            ps = conn.prepareStatement(SQL_CONSULTA);
            rs = ps.executeQuery();
            //Variable que guardará el total de la venta
            float total = 0;
            //Variable que guardará el total de productos comprados
            int productos = 0;
            //Antes de llenar la tabla, primero se agregan los titulos para las
            //columnas de la tabla
            for(String encabezadoTabla : tituloTabla){
                process(table,encabezadoTabla,bold,true);
            }
            
            //Se recorre la consulta de la base de datos
            while(rs.next()){
                /**
                 * Este proceso guarda la consulta de la base de datos linea por
                 * linea, es decir, primero lee la primera linea, comprueba si hay
                 * campos vacios y lo reemplaza por guiones, los campos en cada
                 * linea están separados por ;
                 * También se va sumando los precios y cantidad de cada producto 
                 * que se encuentran en la columna 3 y 2 respectivamente.
                 */
                String line = "";
                
                for(int i = 1; i <= 3; i++){
                    String valor = rs.getString(i);
                    if(i == 3)
                        total += Float.parseFloat(valor);
                    if(i == 2)
                        productos += Integer.parseInt(valor);
                    if(i == 3){
                        if(valor == null || valor.trim().length() == 0)
                            line += "-";
                        else
                            line += valor;
                    }else{
                        if(valor == null || valor.trim().length() == 0)
                            line += "-;";
                        else
                            line += valor + ";";
                    }
                }
                //Aqui se pasa la linea, por ejemplo:
                //linea = campo1; campo2; -; campo#
                process(table,line,font,false);
            }
            //Se agrega la última línea que es la del total
            String ultimaLinea = "Total;"+ productos + ";"+ total;
            process(table,ultimaLinea,bold,false);
            
            //Por ultimo se agrega la tabla al documento
            document.add(table);
            //Se cierra el documento
            document.close();
        } finally{}
        
    }

    /**
     * Este ciclo se encarga de procesar el contenido de la tabla
     * 
     * @param table para la tabla creada
     * @param line la linea de la consulta
     * @param font una fuente de texto
     * @param isHeader Si es true se asume que line es el titulo de la tabla
     */
    
    public void process(Table table,String line,PdfFont font,boolean isHeader){
        //Se usa StringTokenizer para leer la linea que contiene los campos separados por ;
        StringTokenizer tokenizer = new StringTokenizer(line,";");
        while(tokenizer.hasMoreTokens()){
            tokenizer.countTokens();
            if(isHeader) //Si es el encabezado de la tabla
                table.addHeaderCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            else //Cuando es el contenido de la tabla
                table.addCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
        }
    }
    
    
    
    

}
