/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComponentePDF;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josei
 */
public class EventoPDF implements IEventHandler {

    private final Document documento; 
    private String nombre;
    private String folio;
    
    //Ruta relativa de imagen o logotipo
    public static final String IMG = "..\\Resources\\papalote.png";
    //Constructor que recibe el documento, titulo del pdf y el folio de la venta
    public EventoPDF(Document doc, String n,String folio) {
        documento = doc;
        this.nombre = n;
        this.folio = folio;
    }

    /**
     * Se crea el rectangulo donde irá el encabezado
     *
     * @param docEvent Evento de documento
     * @return Area donde irá el encabezado
     */
    private Rectangle crearRectanguloEncabezado(PdfDocumentEvent docEvent) {
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        
        //Dimensiones del encabezado
        float xEncabezado = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yEncabezado = pdfDoc.getDefaultPageSize().getTop() - documento.getTopMargin();
        float anchoEncabezado = page.getPageSize().getWidth() - 301;
        float altoEncabezado = 50F;

        Rectangle rectanguloEncabezado = new Rectangle(xEncabezado, yEncabezado, anchoEncabezado, altoEncabezado);
        
        return rectanguloEncabezado;
    }

    /**
     * Se crea el rectangulo donde irá el pie de página
     *
     * @param docEvent Evento del documento
     * @return Area donde irá el pie de página
     */
    private Rectangle crearRectanguloPie(PdfDocumentEvent docEvent) {
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        
        //Dimensiones del pie de página
        float xPie = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yPie = pdfDoc.getDefaultPageSize().getBottom();
        float anchoPie = page.getPageSize().getWidth() - 301;
        float altoPie = 50F;

        Rectangle rectanguloPie = new Rectangle(xPie, yPie, anchoPie, altoPie);

        return rectanguloPie;
    }

    /**
     * Se crea la tabla que contendra el mensaje e imagen del encabezado (título)
     *
     * @param mensaje Mensaje que desplegaremos
     * @return Tabla con el mensaje de encabezado
     */
    private Table crearTablaEncabezado(String mensaje) throws IOException {
        //Se crea la tabla donde la primera columna ocupará el 10% del ancho total
        //y la segunda columna ocupará el 90%
        Table tablaEncabezado = new Table(UnitValue.createPercentArray(new float[]{10, 90}));
        tablaEncabezado.setWidth(527F);
        Cell cell1, cell2;
        
        //Se inserta la imagen en la primera celda
        Image img = new Image(ImageDataFactory.create(IMG));
        cell2 = new Cell();
        cell2.add(img.setAutoScale(true));
        cell2.setBorder(Border.NO_BORDER);
        tablaEncabezado.addCell(cell2);
        
        //Se inserta el título en la segundo celda
        cell1 = new Cell();
        cell1.add(new Paragraph(mensaje));
        cell1.setBorder(Border.NO_BORDER);
        tablaEncabezado.addCell(cell1);
        
        //Se establece el estilo del encabezado
        Color fondo = new DeviceRgb(137, 168, 252);
        tablaEncabezado.setBackgroundColor(fondo);
        tablaEncabezado.setTextAlignment(TextAlignment.CENTER);
        tablaEncabezado.setWidth(UnitValue.createPercentValue(100));
        tablaEncabezado.setFontSize(25);
        PdfFont fuente = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        tablaEncabezado.setFont(fuente);
        tablaEncabezado.setUnderline();
        return tablaEncabezado;
    }

    /**
     * Se crea la tabla de pie de pagina, con el folio de la venta
     *
     * @param docEvent Evento del documento
     * @return Pie de pagina con el numero de pagina
     */
    private Table crearTablaPie(PdfDocumentEvent docEvent) {
        //se crea la tabla
        float[] anchos = {4F, 4F};
        Table tablaPie = new Table(anchos);
        tablaPie.setWidth(527F);
        
        //Se establece el color
        Color fondo = new DeviceRgb(137, 168, 252);
        tablaPie.setBackgroundColor(fondo);
        
        //Se obtiene la fecha del sistema
        Calendar g = new GregorianCalendar();
        int ann = g.get(Calendar.YEAR);
        int mes = g.get(Calendar.MONTH) + 1;
        int dia = g.get(Calendar.DATE);
        String fecha = String.valueOf(dia + "/" + mes + "/" + ann);

        Cell cell;
        
        //Se inserta la fecha a la izquierda de la celda
        cell = new Cell();
        cell.add(new Paragraph("Fecha: " + fecha).setTextAlignment(TextAlignment.LEFT));
        cell.setBorder(Border.NO_BORDER);
        tablaPie.addFooterCell(cell);
        
        //Se inserta el folio a la derecha de la celda
        cell = new Cell();
        cell.add(new Paragraph("Folio: " + folio).setTextAlignment(TextAlignment.RIGHT).setMarginRight(20));
        cell.setBorder(Border.NO_BORDER);
        tablaPie.addFooterCell(cell);
        
        //se indica que la tabla ocupará el 100% del pdf (respetando margenes)
        tablaPie.setWidth(UnitValue.createPercentValue(100));

        return tablaPie;
    }

    /**
     * Manejador del evento de cambio de pagina, agrega el encabezado y pie de
     * pagina
     *
     * @param event Evento de pagina
     */
    @Override
    public void handleEvent(Event event) {
        try {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();

            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            Table tablaEncabezado = this.crearTablaEncabezado(nombre);
            Rectangle rectanguloEncabezado = this.crearRectanguloEncabezado(docEvent);
            Canvas canvasEncabezado = new Canvas(canvas, pdfDoc, rectanguloEncabezado);
            canvasEncabezado.add(tablaEncabezado);

            Table tablaNumeracion = this.crearTablaPie(docEvent);
            Rectangle rectanguloPie = this.crearRectanguloPie(docEvent);
            Canvas canvasPie = new Canvas(canvas, pdfDoc, rectanguloPie);
            canvasPie.add(tablaNumeracion);
        } catch (IOException ex) {
            Logger.getLogger(EventoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
