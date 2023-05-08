/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaEstilo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author josei
 */
public class Celdas extends DefaultTableCellRenderer{
    //se definen por defecto los tipos de datos a usar
    private Font normal = new Font( "Verdana",Font.PLAIN ,12 );
    private Font bold = new Font( "Verdana",Font.BOLD ,12 );
    //etiqueta que almacenará el icono a mostrar
    private JLabel label = new JLabel();
    private String tipo="text";
    
    public Celdas(String tipo){
        this.tipo=tipo;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        Color colorFondo = null;
        Color colorFondoPorDefecto=new Color( 255,213,164);
        Color colorFondoSeleccion=new Color( 236,200,160);

        //Si la celda del evento es la seleccionada se asigna el fondo por defecto para la selección
        if (selected) this.setBackground(colorFondoPorDefecto );
        //Para las que no están seleccionadas se pinta el fondo de las celdas de colorFondo
        else this.setBackground(colorFondo);
        
                
        /*
        * Se definen los tipos de datos que contendrán las celdas basado en la instancia que
        * se hace en la ventana de la tabla al momento de construirla
        */
        if( tipo.equals("texto"))
        {
            //si es tipo texto define el color de fondo del texto y de la celda así como la alineación
            if (focused) {
                colorFondo=colorFondoSeleccion;
            }else{
                colorFondo= colorFondoPorDefecto;
            }
            this.setHorizontalAlignment( JLabel.LEFT );
            this.setText( (String) value );
            this.setBackground( (selected)? colorFondo :new Color(255,228,201)); 
            this.setFont(normal);   
            this.setEnabled(false);
            return this;
        }
           
        //definie si el tipo de dato el numerico para personalizarlo
        if( tipo.equals("numerico"))
        {           
            if (focused) colorFondo=colorFondoSeleccion;
            else colorFondo=colorFondoPorDefecto;
            
            this.setHorizontalAlignment( JLabel.CENTER );
            this.setText( (String) value );            
            this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );    
            this.setBackground( (selected)? colorFondo :new Color(255,228,201));
            this.setFont(bold);        
            return this;   
        }
        
        return this; 
    }
}

