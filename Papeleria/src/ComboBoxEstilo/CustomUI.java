/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComboBoxEstilo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import TablaEstilo.Scroll;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author josei
 */
public class CustomUI extends BasicComboBoxUI{
    
    private Color colorPrincipal = new Color(180,180,180);
    
    public static ComboBoxUI createUI(JComponent c) {
        return new CustomUI();
    }

    @Override 
    protected JButton createArrowButton() {        
        BasicArrowButton basicArrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, //Direccion de la flecha
                colorPrincipal, //Color de fondo
                Color.BLACK,//sombra
                Color.BLACK,//darkShadow
                Color.WHITE //highlight
                );         
        //se quita el efecto 3d del boton, sombra y darkShadow no se aplican 
        basicArrowButton.setBorder(BorderFactory.createLineBorder(colorPrincipal,2));
        basicArrowButton.setContentAreaFilled(false);        
        return basicArrowButton;
    }
    //
    @Override
    public void paintCurrentValueBackground(Graphics g,
                               Rectangle bounds,
                               boolean hasFocus){
        g.setColor(colorPrincipal );            
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
      
    //se modifica el color de los items del combo
    @Override
    protected ListCellRenderer createRenderer(){
        
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,Object value,int index,
              boolean isSelected,boolean cellHasFocus) {
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                list.setSelectionBackground(colorPrincipal);
                
                if (isSelected){
                    setBackground(colorPrincipal );
                    setForeground(Color.WHITE);
                }
                else{
                    setBackground( Color.WHITE );            
                    setForeground( new Color(70,70,70));
                }
                return this;
            }
        };
    }
    //se modifica el scroll que tienen los JComboBox
    @Override
    protected ComboPopup createPopup() {
        return new BasicComboPopup(comboBox) {
            @Override
            protected JScrollPane createScroller() {
                JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                /*scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    public Dimension getPreferredSize(JComponent c) {
                        return new Dimension(10, super.getPreferredSize(c).height);
                    }
                    
                    private JButton createZeroButton() {
                        return new JButton() {
                            @Override
                            public Dimension getMinimumSize() {
                                return new Dimension(new Dimension(0, 0));
                            }

                            @Override
                            public Dimension getPreferredSize() {
                                return new Dimension(new Dimension(0, 0));
                            }

                            @Override
                            public Dimension getMaximumSize() {
                                return new Dimension(new Dimension(0, 0));
                            }
                        };
                    }
                });*/
                scroller.getVerticalScrollBar().setUI(new Scroll());
                return scroller;
            }
        };
    }
}
