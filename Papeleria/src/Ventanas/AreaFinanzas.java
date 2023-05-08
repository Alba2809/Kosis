/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventanas;

import Controlador.*;
import Modelo.*;
import TablaEstilo.*;
import ComboBoxEstilo.*;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author josei
 */
public class AreaFinanzas extends javax.swing.JFrame implements Runnable{
    
    //Variables para el reloj y fecha
    String hora,minutos;
    Calendar callendario;
    Thread reloj;
    
    //Variables que serviran para obtener las coordenadas del mouse (esta se aplicará para mover las ventanas)
    int xMouse, yMouse;
    
    ControladorGanancias controladorGanancias = new ControladorGanancias(0,"Folio","");
    ControladorGastos controladorGastos = new ControladorGastos(0,"Folio","");
    
    //Variables para obtener el renglon/fila seleccionado de las tablas de Nueva Venta
    
    private int renglonGanancia = 0;
    private int columnGanancia = 0;
    
    /**
     * Creates new form AreaFinanzas
     */
    public AreaFinanzas() {
        initComponents();
        setLocationRelativeTo(null); //ventana en medio de la pantalla
        
        //----------------Fecha------------------------------------------
        Calendar calendario = Calendar.getInstance();
        String dia = "" + calendario.get(Calendar.DATE);
        String mes = "" + (calendario.get(Calendar.MONTH)+1);
        String anio = "" + calendario.get(Calendar.YEAR);
        if(dia.length() == 1)
            dia = "0" + dia;
        if(mes.length() == 1)
            mes = "0" + mes;
        String date = anio + "-" + mes + "-" + dia;
        LocalDate currentDate = LocalDate.parse(date);
        Month mesPalabra = currentDate.getMonth();     
        String mesEspañol = mesPalabra.getDisplayName(TextStyle.FULL,new Locale("es","Es"));
        this.fechaSistema.setText(dia + " de " + mesEspañol.toLowerCase() + " del " + anio);
        
        reloj = new Thread(this);
        reloj.start();
        //----------------Apartado ganancias------------------------------------------
        JTableHeader tHeaderGanancia = tablaGanancias.getTableHeader();
        tHeaderGanancia.setDefaultRenderer(new Encabezado());
        tablaGanancias.setTableHeader(tHeaderGanancia);
        
        scrollGanancias.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGanancias.setBackground(new Color(250,226,211));
        tablaGanancias.setSelectionBackground(new Color( 236,200,160));
        //-----------------------------------------------------------------------------
        JTableHeader tHeaderProdG = tablaProdGanancia.getTableHeader();
        tHeaderProdG.setDefaultRenderer(new Encabezado());
        tablaProdGanancia.setTableHeader(tHeaderProdG);
        
        scrollProdGanancia.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdGanancia.setBackground(new Color(250,226,211));
        tablaProdGanancia.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        comboOrdenarGanan.setUI(new CustomUI());
        comboOrdenarGanan.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderVD = tablaGananciaDia.getTableHeader();
        tHeaderVD.setDefaultRenderer(new Encabezado());
        tablaGananciaDia.setTableHeader(tHeaderVD);
        
        scrollGananciaDia.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGananciaDia.setBackground(new Color(250,226,211));
        tablaGananciaDia.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        comboMesGM.setUI(new CustomUI());
        comboMesGM.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderVS = tablaGananciaSemana.getTableHeader();
        tHeaderVS.setDefaultRenderer(new Encabezado());
        tablaGananciaSemana.setTableHeader(tHeaderVS);
        
        scrollGananciaSemana.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGananciaSemana.setBackground(new Color(250,226,211));
        tablaGananciaSemana.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderVM = tablaGananciaMes.getTableHeader();
        tHeaderVM.setDefaultRenderer(new Encabezado());
        tablaGananciaMes.setTableHeader(tHeaderVM);
        
        scrollGananciaMes.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGananciaMes.setBackground(new Color(250,226,211));
        tablaGananciaMes.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        campoVentasGD.setBackground(new Color(255,255,255,0));
        campoGanDTotal.setBackground(new Color(255,255,255,0));
        campoVentasGM.setBackground(new Color(255,255,255,0));
        campoGanMTotal.setBackground(new Color(255,255,255,0));
        campoVentasGS.setBackground(new Color(255,255,255,0));
        campoGanSTotal.setBackground(new Color(255,255,255,0));
        //----------------Apartado gastos------------------------------------------
        JTableHeader tHeaderG = tablaGastos.getTableHeader();
        tHeaderG.setDefaultRenderer(new Encabezado());
        tablaGastos.setTableHeader(tHeaderG);
        
        scrollGastos.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGastos.setBackground(new Color(250,226,211));
        tablaGastos.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderGD = tablaGastosDia.getTableHeader();
        tHeaderGD.setDefaultRenderer(new Encabezado());
        tablaGastosDia.setTableHeader(tHeaderGD);
        
        scrollGastosDia.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGastosDia.setBackground(new Color(250,226,211));
        tablaGastosDia.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderGS = tablaGastosSemana.getTableHeader();
        tHeaderGS.setDefaultRenderer(new Encabezado());
        tablaGastosSemana.setTableHeader(tHeaderGS);
        
        scrollGastosSemana.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGastosSemana.setBackground(new Color(250,226,211));
        tablaGastosSemana.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderGM = tablaGastosMes.getTableHeader();
        tHeaderGM.setDefaultRenderer(new Encabezado());
        tablaGastosMes.setTableHeader(tHeaderGM);
        
        scrollGastosMes.getVerticalScrollBar().setUI(new Scroll());
        
        tablaGastosMes.setBackground(new Color(250,226,211));
        tablaGastosMes.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        comboOrdenarGasto.setUI(new CustomUI());
        comboOrdenarGasto.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboTipoGastoN.setUI(new CustomUI());
        comboTipoGastoN.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboMesGsM.setUI(new CustomUI());
        comboMesGsM.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboTipoGastoM.setUI(new CustomUI());
        comboTipoGastoM.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        campoIDGastoN.setBackground(new Color(255,255,255,0));
        campoVentasGsD.setBackground(new Color(255,255,255,0));
        campoGasDTotal.setBackground(new Color(255,255,255,0));
        campoVentasGsM.setBackground(new Color(255,255,255,0));
        campoGasMTotal.setBackground(new Color(255,255,255,0));
        campoVentasGsS.setBackground(new Color(255,255,255,0));
        campoGasSTotal.setBackground(new Color(255,255,255,0));
        campoIDGastoEn.setVisible(false);
        //------------------------------------------------------------------------------
        
        //solo me muestra la pantalla o ventana principal al iniciar sesión
        ocultarTodo();
        
        //se inhabilita la opción de pegar (Ctrl + v)
        inhabilitarPegar(panelRaiz);
        
        //se inhabilita la opción de tabular (Tab)
        this.getContentPane().setFocusTraversalPolicyProvider(true);
        this.getContentPane().setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getLastComponent(Container aContainer) {
                    // TODO Auto-generated method stub
                    return null;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                    return null;
            }
            @Override
            public Component getDefaultComponent(Container aContainer) {
                    return null;
            }

            @Override
            public Component getComponentBefore(Container aContainer,
                            Component aComponent) {
                    // TODO Auto-generated method stub
                    return null;
            }

            @Override
            public Component getComponentAfter(Container aContainer,
                            Component aComponent) {
                    // TODO Auto-generated method stub
                    return null;
            }
        });
    }
    
    public void ocultarTodo(){
        panelGanancias.setVisible(false);
        panelGanancias.setEnabled(false);
        
        panelCalcularGan.setVisible(false);
        panelCalcularGan.setEnabled(false);
        
        panelGastos.setVisible(false);
        panelGastos.setEnabled(false);
        
        panelNuevoGasto.setVisible(false);
        panelNuevoGasto.setEnabled(false);
        
        panelModiGasto.setVisible(false);
        panelModiGasto.setEnabled(false);
        
        panelCalcularGas.setVisible(false);
        panelCalcularGas.setEnabled(false);
    }
    
    private void inhabilitarPegar(Container contenedor){
        Component [] componentes = contenedor.getComponents();
        for (int i = 0; i < componentes.length; i++) {
            if (componentes [i] instanceof JTextField){
                InputMap map = ((JTextField)componentes[i]).getInputMap();
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), "null");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, Event.SHIFT_MASK), "null");
            }
            else
                if (componentes [i] instanceof Container)
                    inhabilitarPegar((Container)componentes [i]);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRaiz = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        panelCerrar = new javax.swing.JPanel();
        cerrarAreaVentas = new javax.swing.JLabel();
        panelMenu = new javax.swing.JPanel();
        btnGanancias = new javax.swing.JButton();
        btnGastos = new javax.swing.JButton();
        panelFechaHeader = new javax.swing.JPanel();
        fechaSistema = new javax.swing.JLabel();
        horaSistema = new javax.swing.JLabel();
        panelGanancias = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        scrollGanancias = new javax.swing.JScrollPane();
        tablaGanancias = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scrollProdGanancia = new javax.swing.JScrollPane();
        tablaProdGanancia = new javax.swing.JTable();
        btnMostrarGan = new javax.swing.JButton();
        campoFolioGanancia = new javax.swing.JTextField();
        iconBuscarGanancia = new javax.swing.JLabel();
        comboOrdenarGanan = new javax.swing.JComboBox<>();
        panelBtnCG = new javax.swing.JPanel();
        btnCalcularGan = new javax.swing.JButton();
        fondoPedidos = new javax.swing.JLabel();
        panelCalcularGan = new javax.swing.JPanel();
        scrollGananciaDia = new javax.swing.JScrollPane();
        tablaGananciaDia = new javax.swing.JTable();
        scrollGananciaSemana = new javax.swing.JScrollPane();
        tablaGananciaSemana = new javax.swing.JTable();
        scrollGananciaMes = new javax.swing.JScrollPane();
        tablaGananciaMes = new javax.swing.JTable();
        campoDiaGD = new javax.swing.JTextField();
        campoMesGD = new javax.swing.JTextField();
        campoAnioGD = new javax.swing.JTextField();
        campoVentasGD = new javax.swing.JTextField();
        campoGanDTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        campoDiaGS = new javax.swing.JTextField();
        campoMesGS = new javax.swing.JTextField();
        campoAnioGS = new javax.swing.JTextField();
        campoVentasGS = new javax.swing.JTextField();
        campoGanSTotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        comboMesGM = new javax.swing.JComboBox<>();
        campoAnioGM = new javax.swing.JTextField();
        campoVentasGM = new javax.swing.JTextField();
        campoGanMTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnCalcularGM = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnCalcularGS = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCalcularGD = new javax.swing.JButton();
        fondoNuevaCompra = new javax.swing.JLabel();
        panelGastos = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        scrollGastos = new javax.swing.JScrollPane();
        tablaGastos = new javax.swing.JTable();
        campoIDGasto = new javax.swing.JTextField();
        iconBuscarGasto = new javax.swing.JLabel();
        comboOrdenarGasto = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        btnCalcularGastos = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnNuevoGasto = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnModifGasto = new javax.swing.JButton();
        fondoInventario = new javax.swing.JLabel();
        panelNuevoGasto = new javax.swing.JPanel();
        campoIDGastoN = new javax.swing.JTextField();
        campoNombreGastoN = new javax.swing.JTextField();
        campoMontoGastoN = new javax.swing.JTextField();
        campoDiaGastoN = new javax.swing.JTextField();
        campoMesGastoN = new javax.swing.JTextField();
        campoAnioGastoN = new javax.swing.JTextField();
        comboTipoGastoN = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        btnCancelarGastoN = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnAceptarGastoN = new javax.swing.JButton();
        fondoNuevProducto = new javax.swing.JLabel();
        panelModiGasto = new javax.swing.JPanel();
        campoIDGastoM = new javax.swing.JTextField();
        campoIDGastoEn = new javax.swing.JTextField();
        campoNombreGastoM = new javax.swing.JTextField();
        campoMontoGastoM = new javax.swing.JTextField();
        comboTipoGastoM = new javax.swing.JComboBox<>();
        campoDiaGastoM = new javax.swing.JTextField();
        campoMesGastoM = new javax.swing.JTextField();
        campoAnioGastoM = new javax.swing.JTextField();
        panelBtnBuscRecGastModi = new javax.swing.JPanel();
        btnBuscarGastoM = new javax.swing.JButton();
        btnRecarGastoM = new javax.swing.JButton();
        panelBtnModiGast = new javax.swing.JPanel();
        btnModiGastoBD = new javax.swing.JButton();
        btnCancelarGastoM = new javax.swing.JButton();
        fondoElimProducto = new javax.swing.JLabel();
        panelCalcularGas = new javax.swing.JPanel();
        campoDiaGsD = new javax.swing.JTextField();
        campoMesGsD = new javax.swing.JTextField();
        campoAnioGsD = new javax.swing.JTextField();
        campoDiaGsS = new javax.swing.JTextField();
        campoMesGsS = new javax.swing.JTextField();
        campoAnioGsS = new javax.swing.JTextField();
        comboMesGsM = new javax.swing.JComboBox<>();
        campoAnioGsM = new javax.swing.JTextField();
        campoVentasGsS = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoGasDTotal = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        btnCalcularGsD = new javax.swing.JButton();
        scrollGastosSemana = new javax.swing.JScrollPane();
        tablaGastosSemana = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnCalcularGsS = new javax.swing.JButton();
        campoGasSTotal = new javax.swing.JTextField();
        scrollGastosDia = new javax.swing.JScrollPane();
        tablaGastosDia = new javax.swing.JTable();
        campoVentasGsD = new javax.swing.JTextField();
        campoGasMTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        scrollGastosMes = new javax.swing.JScrollPane();
        tablaGastosMes = new javax.swing.JTable();
        campoVentasGsM = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        btnCalcularGsM = new javax.swing.JButton();
        fondoRegCompras = new javax.swing.JLabel();
        fondoFinanzas = new javax.swing.JLabel();
        headerFinanzas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        panelRaiz.setBackground(new java.awt.Color(255, 255, 255));
        panelRaiz.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setEnabled(false);
        jLabel6.setOpaque(true);
        panelRaiz.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 10, 30, 50));

        panelCerrar.setBackground(new java.awt.Color(255, 255, 255));

        cerrarAreaVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/IconCerrar.png"))); // NOI18N
        cerrarAreaVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cerrarAreaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cerrarAreaVentasMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelCerrarLayout = new javax.swing.GroupLayout(panelCerrar);
        panelCerrar.setLayout(panelCerrarLayout);
        panelCerrarLayout.setHorizontalGroup(
            panelCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCerrarLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(cerrarAreaVentas)
                .addContainerGap())
        );
        panelCerrarLayout.setVerticalGroup(
            panelCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCerrarLayout.createSequentialGroup()
                .addComponent(cerrarAreaVentas)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelRaiz.add(panelCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 20, 70, 40));

        panelMenu.setBackground(new java.awt.Color(45, 49, 66));

        btnGanancias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuGananciasNS.png"))); // NOI18N
        btnGanancias.setBorder(null);
        btnGanancias.setBorderPainted(false);
        btnGanancias.setContentAreaFilled(false);
        btnGanancias.setFocusPainted(false);
        btnGanancias.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuGananciasS.png"))); // NOI18N
        btnGanancias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGananciasActionPerformed(evt);
            }
        });

        btnGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuGastosNS.png"))); // NOI18N
        btnGastos.setBorder(null);
        btnGastos.setBorderPainted(false);
        btnGastos.setContentAreaFilled(false);
        btnGastos.setFocusPainted(false);
        btnGastos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuGastosS.png"))); // NOI18N
        btnGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGastosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGanancias, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGanancias)
                .addGap(18, 18, 18)
                .addComponent(btnGastos)
                .addContainerGap(403, Short.MAX_VALUE))
        );

        panelRaiz.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 190, 520));

        panelFechaHeader.setBackground(new java.awt.Color(255, 255, 255));

        fechaSistema.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        fechaSistema.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fechaSistema.setText("fecha");

        horaSistema.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        horaSistema.setText("hora");

        javax.swing.GroupLayout panelFechaHeaderLayout = new javax.swing.GroupLayout(panelFechaHeader);
        panelFechaHeader.setLayout(panelFechaHeaderLayout);
        panelFechaHeaderLayout.setHorizontalGroup(
            panelFechaHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFechaHeaderLayout.createSequentialGroup()
                .addComponent(fechaSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(horaSistema, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );
        panelFechaHeaderLayout.setVerticalGroup(
            panelFechaHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fechaSistema, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(horaSistema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelRaiz.add(panelFechaHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 470, -1));

        panelGanancias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 237, 225));

        tablaGanancias = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGanancias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Cliente", "Cantidad Productos", "Fecha", "Total Venta", "Ganancia"
            }
        ));
        tablaGanancias.getTableHeader().setReorderingAllowed(false);
        tablaGanancias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaGananciasMouseClicked(evt);
            }
        });
        scrollGanancias.setViewportView(tablaGanancias);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollGanancias, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollGanancias, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelGanancias.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 660, 270));

        jPanel3.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdGanancia = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdGanancia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Venta", "Precio Compra", "Estado"
            }
        ));
        tablaProdGanancia.getTableHeader().setReorderingAllowed(false);
        scrollProdGanancia.setViewportView(tablaProdGanancia);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdGanancia, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdGanancia, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelGanancias.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 660, 200));

        btnMostrarGan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecNS.png"))); // NOI18N
        btnMostrarGan.setBorder(null);
        btnMostrarGan.setBorderPainted(false);
        btnMostrarGan.setContentAreaFilled(false);
        btnMostrarGan.setFocusPainted(false);
        btnMostrarGan.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecS.png"))); // NOI18N
        btnMostrarGan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarGanActionPerformed(evt);
            }
        });
        panelGanancias.add(btnMostrarGan, new org.netbeans.lib.awtextra.AbsoluteConstraints(761, 338, 208, 68));

        campoFolioGanancia.setBorder(null);
        campoFolioGanancia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoFolioGananciaKeyTyped(evt);
            }
        });
        panelGanancias.add(campoFolioGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 224, 220, 20));

        iconBuscarGanancia.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarGanancia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarGanancia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarGanancia.setOpaque(true);
        iconBuscarGanancia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarGananciaMouseClicked(evt);
            }
        });
        panelGanancias.add(iconBuscarGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 224, 22, 22));

        comboOrdenarGanan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Folio", "Fecha ascendente", "Fecha descendente" }));
        comboOrdenarGanan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarGananItemStateChanged(evt);
            }
        });
        panelGanancias.add(comboOrdenarGanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(731, 285, 180, 25));

        panelBtnCG.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGan.setBorder(null);
        btnCalcularGan.setBorderPainted(false);
        btnCalcularGan.setContentAreaFilled(false);
        btnCalcularGan.setFocusPainted(false);
        btnCalcularGan.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnCGLayout = new javax.swing.GroupLayout(panelBtnCG);
        panelBtnCG.setLayout(panelBtnCGLayout);
        panelBtnCGLayout.setHorizontalGroup(
            panelBtnCGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnCGLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCalcularGan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBtnCGLayout.setVerticalGroup(
            panelBtnCGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnCGLayout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(btnCalcularGan))
        );

        panelGanancias.add(panelBtnCG, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 470, 220, 66));

        fondoPedidos.setBackground(new java.awt.Color(255, 255, 255));
        fondoPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoGanancias.png"))); // NOI18N
        panelGanancias.add(fondoPedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelCalcularGan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaGananciaDia = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGananciaDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Ganancia"
            }
        ));
        tablaGananciaDia.getTableHeader().setReorderingAllowed(false);
        scrollGananciaDia.setViewportView(tablaGananciaDia);

        panelCalcularGan.add(scrollGananciaDia, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 254, 110));

        tablaGananciaSemana = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGananciaSemana.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Ganancia"
            }
        ));
        tablaGananciaSemana.getTableHeader().setReorderingAllowed(false);
        scrollGananciaSemana.setViewportView(tablaGananciaSemana);

        panelCalcularGan.add(scrollGananciaSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(394, 385, 254, 110));

        tablaGananciaMes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGananciaMes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Ganancia"
            }
        ));
        tablaGananciaMes.getTableHeader().setReorderingAllowed(false);
        scrollGananciaMes.setViewportView(tablaGananciaMes);

        panelCalcularGan.add(scrollGananciaMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(734, 385, 254, 110));

        campoDiaGD.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGD.setText("Día");
        campoDiaGD.setBorder(null);
        campoDiaGD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGDMousePressed(evt);
            }
        });
        campoDiaGD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGDKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoDiaGD, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 208, 60, 20));

        campoMesGD.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGD.setText("Mes");
        campoMesGD.setBorder(null);
        campoMesGD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGDMousePressed(evt);
            }
        });
        campoMesGD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGDKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoMesGD, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 208, 60, 20));

        campoAnioGD.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGD.setText("Año");
        campoAnioGD.setBorder(null);
        campoAnioGD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGDMousePressed(evt);
            }
        });
        campoAnioGD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGDKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoAnioGD, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 208, 60, 20));

        campoVentasGD.setEditable(false);
        campoVentasGD.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGD.setBorder(null);
        campoVentasGD.setEnabled(false);
        panelCalcularGan.add(campoVentasGD, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 352, 256, 20));

        campoGanDTotal.setEditable(false);
        campoGanDTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGanDTotal.setBorder(null);
        campoGanDTotal.setEnabled(false);
        panelCalcularGan.add(campoGanDTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 529, 245, 20));

        jLabel1.setText("$");
        panelCalcularGan.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 532, -1, -1));

        campoDiaGS.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGS.setText("Día");
        campoDiaGS.setBorder(null);
        campoDiaGS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGSMousePressed(evt);
            }
        });
        campoDiaGS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGSKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoDiaGS, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 207, 60, 20));

        campoMesGS.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGS.setText("Mes");
        campoMesGS.setBorder(null);
        campoMesGS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGSMousePressed(evt);
            }
        });
        campoMesGS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGSKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoMesGS, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 207, 60, 20));

        campoAnioGS.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGS.setText("Año");
        campoAnioGS.setBorder(null);
        campoAnioGS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGSMousePressed(evt);
            }
        });
        campoAnioGS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGSKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoAnioGS, new org.netbeans.lib.awtextra.AbsoluteConstraints(592, 207, 60, 20));

        campoVentasGS.setEditable(false);
        campoVentasGS.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGS.setBorder(null);
        campoVentasGS.setEnabled(false);
        panelCalcularGan.add(campoVentasGS, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 348, 256, 20));

        campoGanSTotal.setEditable(false);
        campoGanSTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGanSTotal.setBorder(null);
        campoGanSTotal.setEnabled(false);
        panelCalcularGan.add(campoGanSTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 525, 245, 20));

        jLabel2.setText("$");
        panelCalcularGan.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 527, -1, -1));

        comboMesGM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        comboMesGM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                comboMesGMMousePressed(evt);
            }
        });
        panelCalcularGan.add(comboMesGM, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 214, 110, 20));

        campoAnioGM.setBorder(null);
        campoAnioGM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGMMousePressed(evt);
            }
        });
        campoAnioGM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGMKeyTyped(evt);
            }
        });
        panelCalcularGan.add(campoAnioGM, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 214, 80, 20));

        campoVentasGM.setEditable(false);
        campoVentasGM.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGM.setBorder(null);
        campoVentasGM.setEnabled(false);
        panelCalcularGan.add(campoVentasGM, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 348, 256, 20));

        campoGanMTotal.setEditable(false);
        campoGanMTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGanMTotal.setBorder(null);
        campoGanMTotal.setEnabled(false);
        panelCalcularGan.add(campoGanMTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(744, 525, 245, 20));

        jLabel3.setText("$");
        panelCalcularGan.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 527, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGM.setBorder(null);
        btnCalcularGM.setBorderPainted(false);
        btnCalcularGM.setContentAreaFilled(false);
        btnCalcularGM.setFocusPainted(false);
        btnCalcularGM.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(btnCalcularGM, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(btnCalcularGM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGan.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 210, 70));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGS.setBorder(null);
        btnCalcularGS.setBorderPainted(false);
        btnCalcularGS.setContentAreaFilled(false);
        btnCalcularGS.setFocusPainted(false);
        btnCalcularGS.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnCalcularGS, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnCalcularGS, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGan.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 250, 210, 70));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGD.setBorder(null);
        btnCalcularGD.setBorderPainted(false);
        btnCalcularGD.setContentAreaFilled(false);
        btnCalcularGD.setFocusPainted(false);
        btnCalcularGD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnCalcularGD, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnCalcularGD, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGan.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 250, 210, 70));

        fondoNuevaCompra.setBackground(new java.awt.Color(255, 255, 255));
        fondoNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoCalcularGan.png"))); // NOI18N
        panelCalcularGan.add(fondoNuevaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelCalcularGan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelGastos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 237, 225));

        tablaGastos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Monto", "Fecha", "Tipo Gasto"
            }
        ));
        tablaGastos.getTableHeader().setReorderingAllowed(false);
        scrollGastos.setViewportView(tablaGastos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollGastos, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollGastos, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelGastos.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 670, 400));

        campoIDGasto.setBorder(null);
        campoIDGasto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDGastoKeyTyped(evt);
            }
        });
        panelGastos.add(campoIDGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(743, 189, 220, 20));

        iconBuscarGasto.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarGasto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarGasto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarGasto.setOpaque(true);
        iconBuscarGasto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarGastoMouseClicked(evt);
            }
        });
        panelGastos.add(iconBuscarGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(974, 188, 22, 22));

        comboOrdenarGasto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Fecha ascendente", "Fecha descendente" }));
        comboOrdenarGasto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarGastoItemStateChanged(evt);
            }
        });
        panelGastos.add(comboOrdenarGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(736, 252, 180, 25));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGastos.setBorder(null);
        btnCalcularGastos.setBorderPainted(false);
        btnCalcularGastos.setContentAreaFilled(false);
        btnCalcularGastos.setFocusPainted(false);
        btnCalcularGastos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGastosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btnCalcularGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btnCalcularGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        panelGastos.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 401, 205, 66));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        btnNuevoGasto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnNuevoGastoNS.png"))); // NOI18N
        btnNuevoGasto.setBorder(null);
        btnNuevoGasto.setBorderPainted(false);
        btnNuevoGasto.setContentAreaFilled(false);
        btnNuevoGasto.setFocusPainted(false);
        btnNuevoGasto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnNuevoGastoS.png"))); // NOI18N
        btnNuevoGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoGastoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(btnNuevoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(btnNuevoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelGastos.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 575, 210, 70));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        btnModifGasto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModifGastoNS.png"))); // NOI18N
        btnModifGasto.setBorder(null);
        btnModifGasto.setBorderPainted(false);
        btnModifGasto.setContentAreaFilled(false);
        btnModifGasto.setFocusPainted(false);
        btnModifGasto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModifGastoS.png"))); // NOI18N
        btnModifGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifGastoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btnModifGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btnModifGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelGastos.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 575, 210, 70));

        fondoInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoGastos.png"))); // NOI18N
        panelGastos.add(fondoInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNuevoGasto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDGastoN.setEditable(false);
        campoIDGastoN.setBackground(new java.awt.Color(255, 255, 255));
        campoIDGastoN.setBorder(null);
        campoIDGastoN.setEnabled(false);
        panelNuevoGasto.add(campoIDGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 260, 220, 20));

        campoNombreGastoN.setBorder(null);
        campoNombreGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoNombreGastoNMousePressed(evt);
            }
        });
        campoNombreGastoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNombreGastoNKeyTyped(evt);
            }
        });
        panelNuevoGasto.add(campoNombreGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 357, 220, 20));

        campoMontoGastoN.setBorder(null);
        campoMontoGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMontoGastoNMousePressed(evt);
            }
        });
        campoMontoGastoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMontoGastoNKeyTyped(evt);
            }
        });
        panelNuevoGasto.add(campoMontoGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 451, 220, 20));

        campoDiaGastoN.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGastoN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGastoN.setText("Día");
        campoDiaGastoN.setBorder(null);
        campoDiaGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGastoNMousePressed(evt);
            }
        });
        campoDiaGastoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGastoNKeyTyped(evt);
            }
        });
        panelNuevoGasto.add(campoDiaGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 310, 52, 20));

        campoMesGastoN.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGastoN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGastoN.setText("Mes");
        campoMesGastoN.setBorder(null);
        campoMesGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGastoNMousePressed(evt);
            }
        });
        campoMesGastoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGastoNKeyTyped(evt);
            }
        });
        panelNuevoGasto.add(campoMesGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(709, 310, 52, 20));

        campoAnioGastoN.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGastoN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGastoN.setText("Año");
        campoAnioGastoN.setBorder(null);
        campoAnioGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGastoNMousePressed(evt);
            }
        });
        campoAnioGastoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGastoNKeyTyped(evt);
            }
        });
        panelNuevoGasto.add(campoAnioGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(809, 310, 52, 20));

        comboTipoGastoN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pago de servicio", "Pago de nómina" }));
        comboTipoGastoN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                comboTipoGastoNMousePressed(evt);
            }
        });
        panelNuevoGasto.add(comboTipoGastoN, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 410, 170, -1));

        jPanel11.setBackground(new java.awt.Color(248, 249, 255));

        btnCancelarGastoN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarGastoN.setBorder(null);
        btnCancelarGastoN.setBorderPainted(false);
        btnCancelarGastoN.setContentAreaFilled(false);
        btnCancelarGastoN.setFocusPainted(false);
        btnCancelarGastoN.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarGastoN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarGastoNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelarGastoN, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelarGastoN, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNuevoGasto.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, 220, 80));

        jPanel10.setBackground(new java.awt.Color(248, 249, 255));

        btnAceptarGastoN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarNS.png"))); // NOI18N
        btnAceptarGastoN.setBorder(null);
        btnAceptarGastoN.setBorderPainted(false);
        btnAceptarGastoN.setContentAreaFilled(false);
        btnAceptarGastoN.setFocusPainted(false);
        btnAceptarGastoN.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarS.png"))); // NOI18N
        btnAceptarGastoN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarGastoNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAceptarGastoN, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAceptarGastoN, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNuevoGasto.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 570, 220, 80));

        fondoNuevProducto.setBackground(new java.awt.Color(255, 255, 255));
        fondoNuevProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevoGasto.png"))); // NOI18N
        panelNuevoGasto.add(fondoNuevProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevoGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelModiGasto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDGastoM.setBorder(null);
        campoIDGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoIDGastoMMousePressed(evt);
            }
        });
        campoIDGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoIDGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 310, 146, 20));

        campoIDGastoEn.setEditable(false);
        campoIDGastoEn.setEnabled(false);
        panelModiGasto.add(campoIDGastoEn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 40, -1));

        campoNombreGastoM.setBorder(null);
        campoNombreGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoNombreGastoMMousePressed(evt);
            }
        });
        campoNombreGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNombreGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoNombreGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 196, 222, 20));

        campoMontoGastoM.setBorder(null);
        campoMontoGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMontoGastoMMousePressed(evt);
            }
        });
        campoMontoGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMontoGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoMontoGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 317, 222, 20));

        comboTipoGastoM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pago de servicio", "Pago de nómina" }));
        comboTipoGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                comboTipoGastoMMousePressed(evt);
            }
        });
        panelModiGasto.add(comboTipoGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(441, 549, 170, -1));

        campoDiaGastoM.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGastoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGastoM.setText("Día");
        campoDiaGastoM.setBorder(null);
        campoDiaGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGastoMMousePressed(evt);
            }
        });
        campoDiaGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoDiaGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 434, 52, 20));

        campoMesGastoM.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGastoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGastoM.setText("Mes");
        campoMesGastoM.setBorder(null);
        campoMesGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGastoMMousePressed(evt);
            }
        });
        campoMesGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoMesGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 434, 52, 20));

        campoAnioGastoM.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGastoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGastoM.setText("Año");
        campoAnioGastoM.setBorder(null);
        campoAnioGastoM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGastoMMousePressed(evt);
            }
        });
        campoAnioGastoM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGastoMKeyTyped(evt);
            }
        });
        panelModiGasto.add(campoAnioGastoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 434, 52, 20));

        panelBtnBuscRecGastModi.setBackground(new java.awt.Color(255, 243, 236));

        btnBuscarGastoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnBuscarGastoM.setToolTipText("Buscar");
        btnBuscarGastoM.setBorder(null);
        btnBuscarGastoM.setBorderPainted(false);
        btnBuscarGastoM.setContentAreaFilled(false);
        btnBuscarGastoM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarGastoM.setFocusPainted(false);
        btnBuscarGastoM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarGastoMActionPerformed(evt);
            }
        });

        btnRecarGastoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconRecargar.png"))); // NOI18N
        btnRecarGastoM.setToolTipText("Limpiar Campos");
        btnRecarGastoM.setBorder(null);
        btnRecarGastoM.setBorderPainted(false);
        btnRecarGastoM.setContentAreaFilled(false);
        btnRecarGastoM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecarGastoM.setFocusPainted(false);
        btnRecarGastoM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecarGastoMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnBuscRecGastModiLayout = new javax.swing.GroupLayout(panelBtnBuscRecGastModi);
        panelBtnBuscRecGastModi.setLayout(panelBtnBuscRecGastModiLayout);
        panelBtnBuscRecGastModiLayout.setHorizontalGroup(
            panelBtnBuscRecGastModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnBuscRecGastModiLayout.createSequentialGroup()
                .addComponent(btnRecarGastoM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(btnBuscarGastoM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBtnBuscRecGastModiLayout.setVerticalGroup(
            panelBtnBuscRecGastModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnBuscRecGastModiLayout.createSequentialGroup()
                .addGroup(panelBtnBuscRecGastModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarGastoM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecarGastoM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModiGasto.add(panelBtnBuscRecGastModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 190, 60));

        panelBtnModiGast.setBackground(new java.awt.Color(248, 249, 255));

        btnModiGastoBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarNS.png"))); // NOI18N
        btnModiGastoBD.setBorder(null);
        btnModiGastoBD.setBorderPainted(false);
        btnModiGastoBD.setContentAreaFilled(false);
        btnModiGastoBD.setFocusPainted(false);
        btnModiGastoBD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarS.png"))); // NOI18N
        btnModiGastoBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiGastoBDActionPerformed(evt);
            }
        });

        btnCancelarGastoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarGastoM.setBorder(null);
        btnCancelarGastoM.setBorderPainted(false);
        btnCancelarGastoM.setContentAreaFilled(false);
        btnCancelarGastoM.setFocusPainted(false);
        btnCancelarGastoM.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarGastoM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarGastoMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnModiGastLayout = new javax.swing.GroupLayout(panelBtnModiGast);
        panelBtnModiGast.setLayout(panelBtnModiGastLayout);
        panelBtnModiGastLayout.setHorizontalGroup(
            panelBtnModiGastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnModiGastoBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelarGastoM, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        panelBtnModiGastLayout.setVerticalGroup(
            panelBtnModiGastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnModiGastLayout.createSequentialGroup()
                .addComponent(btnModiGastoBD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(btnCancelarGastoM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        panelModiGasto.add(panelBtnModiGast, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 240, 220, 260));

        fondoElimProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoModifGasto.png"))); // NOI18N
        panelModiGasto.add(fondoElimProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelModiGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelCalcularGas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoDiaGsD.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGsD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGsD.setText("Día");
        campoDiaGsD.setBorder(null);
        campoDiaGsD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGsDMousePressed(evt);
            }
        });
        campoDiaGsD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGsDKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoDiaGsD, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 208, 60, 20));

        campoMesGsD.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGsD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGsD.setText("Mes");
        campoMesGsD.setBorder(null);
        campoMesGsD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGsDMousePressed(evt);
            }
        });
        campoMesGsD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGsDKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoMesGsD, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 208, 60, 20));

        campoAnioGsD.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGsD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGsD.setText("Año");
        campoAnioGsD.setBorder(null);
        campoAnioGsD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGsDMousePressed(evt);
            }
        });
        campoAnioGsD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGsDKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoAnioGsD, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 208, 60, 20));

        campoDiaGsS.setForeground(new java.awt.Color(204, 204, 204));
        campoDiaGsS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoDiaGsS.setText("Día");
        campoDiaGsS.setBorder(null);
        campoDiaGsS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoDiaGsSMousePressed(evt);
            }
        });
        campoDiaGsS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDiaGsSKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoDiaGsS, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 207, 60, 20));

        campoMesGsS.setForeground(new java.awt.Color(204, 204, 204));
        campoMesGsS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMesGsS.setText("Mes");
        campoMesGsS.setBorder(null);
        campoMesGsS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoMesGsSMousePressed(evt);
            }
        });
        campoMesGsS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMesGsSKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoMesGsS, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 207, 60, 20));

        campoAnioGsS.setForeground(new java.awt.Color(204, 204, 204));
        campoAnioGsS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoAnioGsS.setText("Año");
        campoAnioGsS.setBorder(null);
        campoAnioGsS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGsSMousePressed(evt);
            }
        });
        campoAnioGsS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGsSKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoAnioGsS, new org.netbeans.lib.awtextra.AbsoluteConstraints(592, 207, 60, 20));

        comboMesGsM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        comboMesGsM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                comboMesGsMMousePressed(evt);
            }
        });
        panelCalcularGas.add(comboMesGsM, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 214, 110, 20));

        campoAnioGsM.setBorder(null);
        campoAnioGsM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                campoAnioGsMMousePressed(evt);
            }
        });
        campoAnioGsM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoAnioGsMKeyTyped(evt);
            }
        });
        panelCalcularGas.add(campoAnioGsM, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 214, 80, 20));

        campoVentasGsS.setEditable(false);
        campoVentasGsS.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGsS.setBorder(null);
        campoVentasGsS.setEnabled(false);
        panelCalcularGas.add(campoVentasGsS, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 348, 256, 20));

        jLabel4.setText("$");
        panelCalcularGas.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 532, -1, -1));

        campoGasDTotal.setEditable(false);
        campoGasDTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGasDTotal.setBorder(null);
        campoGasDTotal.setEnabled(false);
        panelCalcularGas.add(campoGasDTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 529, 245, 20));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGsD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGsD.setBorder(null);
        btnCalcularGsD.setBorderPainted(false);
        btnCalcularGsD.setContentAreaFilled(false);
        btnCalcularGsD.setFocusPainted(false);
        btnCalcularGsD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGsD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGsDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(btnCalcularGsD, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(btnCalcularGsD, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGas.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 250, 210, 70));

        tablaGastosSemana = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGastosSemana.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Monto"
            }
        ));
        tablaGastosSemana.getTableHeader().setReorderingAllowed(false);
        scrollGastosSemana.setViewportView(tablaGastosSemana);

        panelCalcularGas.add(scrollGastosSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(394, 385, 254, 110));

        jLabel5.setText("$");
        panelCalcularGas.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 527, -1, -1));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGsS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGsS.setBorder(null);
        btnCalcularGsS.setBorderPainted(false);
        btnCalcularGsS.setContentAreaFilled(false);
        btnCalcularGsS.setFocusPainted(false);
        btnCalcularGsS.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGsS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGsSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(btnCalcularGsS, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(btnCalcularGsS, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGas.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 250, 210, 70));

        campoGasSTotal.setEditable(false);
        campoGasSTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGasSTotal.setBorder(null);
        campoGasSTotal.setEnabled(false);
        panelCalcularGas.add(campoGasSTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 525, 245, 20));

        tablaGastosDia = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGastosDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Monto"
            }
        ));
        tablaGastosDia.getTableHeader().setReorderingAllowed(false);
        scrollGastosDia.setViewportView(tablaGastosDia);

        panelCalcularGas.add(scrollGastosDia, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 254, 110));

        campoVentasGsD.setEditable(false);
        campoVentasGsD.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGsD.setBorder(null);
        campoVentasGsD.setEnabled(false);
        panelCalcularGas.add(campoVentasGsD, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 352, 256, 20));

        campoGasMTotal.setEditable(false);
        campoGasMTotal.setBackground(new java.awt.Color(255, 255, 255));
        campoGasMTotal.setBorder(null);
        campoGasMTotal.setEnabled(false);
        panelCalcularGas.add(campoGasMTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(744, 525, 245, 20));

        jLabel7.setText("$");
        panelCalcularGas.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 527, -1, -1));

        tablaGastosMes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaGastosMes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Monto"
            }
        ));
        tablaGastosMes.getTableHeader().setReorderingAllowed(false);
        scrollGastosMes.setViewportView(tablaGastosMes);

        panelCalcularGas.add(scrollGastosMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(734, 385, 254, 110));

        campoVentasGsM.setEditable(false);
        campoVentasGsM.setBackground(new java.awt.Color(255, 255, 255));
        campoVentasGsM.setBorder(null);
        campoVentasGsM.setEnabled(false);
        panelCalcularGas.add(campoVentasGsM, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 348, 256, 20));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        btnCalcularGsM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularNS.png"))); // NOI18N
        btnCalcularGsM.setBorder(null);
        btnCalcularGsM.setBorderPainted(false);
        btnCalcularGsM.setContentAreaFilled(false);
        btnCalcularGsM.setFocusPainted(false);
        btnCalcularGsM.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCalcularS.png"))); // NOI18N
        btnCalcularGsM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularGsMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(btnCalcularGsM, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(btnCalcularGsM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        panelCalcularGas.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 210, 70));

        fondoRegCompras.setBackground(new java.awt.Color(255, 255, 255));
        fondoRegCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoCalcularGas.png"))); // NOI18N
        panelCalcularGas.add(fondoRegCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelCalcularGas, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        fondoFinanzas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/Fondo.png"))); // NOI18N
        panelRaiz.add(fondoFinanzas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        headerFinanzas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerFinanzasMouseDragged(evt);
            }
        });
        headerFinanzas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerFinanzasMousePressed(evt);
            }
        });

        javax.swing.GroupLayout headerFinanzasLayout = new javax.swing.GroupLayout(headerFinanzas);
        headerFinanzas.setLayout(headerFinanzasLayout);
        headerFinanzasLayout.setHorizontalGroup(
            headerFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        headerFinanzasLayout.setVerticalGroup(
            headerFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelRaiz.add(headerFinanzas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelRaiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelRaiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //hilo para el reloj
    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while(ct == reloj){
            calcula();
            horaSistema.setText("   |   " + hora + ":" + minutos + " hrs");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                
            }
        }
    }
    
    private void calcula() {
        Calendar calendario = new GregorianCalendar();
        Date fechaHoraactual = new Date();
        calendario.setTime(fechaHoraactual);
        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        
    }
    
    private void cerrarAreaVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cerrarAreaVentasMouseClicked
        System.exit(0);
    }//GEN-LAST:event_cerrarAreaVentasMouseClicked
    
    private void btnGananciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGananciasActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelGanancias.setVisible(true);
        panelGanancias.setEnabled(true);
        
        controladorGanancias = new ControladorGanancias(0,"Folio","");
        tablaGanancias.setModel(controladorGanancias.getModelo());
        
        boolean seleccionado = false;
        for(int i = 0;i < tablaGanancias.getRowCount(); i++){
            if(tablaGanancias.isRowSelected(i))
                seleccionado = true;                
        }
        if(seleccionado != false)
            tablaGanancias.changeSelection(renglonGanancia, columnGanancia, true, false);
        
        if(tablaProdGanancia.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdGanancia.getModel();
            modeloProd.setRowCount(0);
        }
        
        renglonGanancia = 0;
        columnGanancia = 0;
    }//GEN-LAST:event_btnGananciasActionPerformed

    private void btnGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastosActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelGastos.setVisible(true);
        panelGastos.setEnabled(true);
        
        controladorGastos = new ControladorGastos(0,"Folio","");
        tablaGastos.setModel(controladorGastos.getModelo());
        
    }//GEN-LAST:event_btnGastosActionPerformed

    private void headerFinanzasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerFinanzasMouseDragged
        //se asignan las coordenadas del mouse en pantalla
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        //se restan los valores para obtener el resultado de mover la ventana
        this.setLocation(x - xMouse,y - yMouse);
    }//GEN-LAST:event_headerFinanzasMouseDragged

    private void headerFinanzasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerFinanzasMousePressed
        //se asignan las coordenadas (del mouse en la ventana)
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerFinanzasMousePressed

    private void tablaGananciasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaGananciasMouseClicked
        if(tablaGanancias.getRowCount() != 0){
            renglonGanancia = tablaGanancias.rowAtPoint(evt.getPoint());
            columnGanancia = tablaGanancias.columnAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaGananciasMouseClicked

    private void btnMostrarGanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarGanActionPerformed
        boolean seleccionado = false;
        int filas = tablaGanancias.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaGanancias.isRowSelected(i))
                seleccionado = true;                
        }
        
        if((filas != 0) && (seleccionado != false)){
            ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(tablaGanancias.getValueAt(renglonGanancia,0)),1);
            tablaProdGanancia.setModel(controladorProdVenta.getModelo());
        }else{
            String m = "No hay una venta seleccionada.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        
    }//GEN-LAST:event_btnMostrarGanActionPerformed

    private void iconBuscarGananciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarGananciaMouseClicked
        if(!(this.campoFolioGanancia.getText().equals(""))){
            controladorGanancias = new ControladorGanancias(0,"Folio","");
            if(controladorGanancias.buscarGananciaAct(this.campoFolioGanancia.getText())){
                tablaGanancias.setModel(controladorGanancias.getModelo());
                if(tablaProdGanancia.getRowCount() != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaProdGanancia.getModel();
                    for(int i = 0 ; i < tablaProdGanancia.getRowCount() ; i++)
                    modeloProd.removeRow(0);
                }
            }
            else{
                String m = "Venta no encontrada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoFolioGanancia.setText("");
            }
        }else{
            controladorGanancias = new ControladorGanancias(0,"Folio","");
            tablaGanancias.setModel(controladorGanancias.getModelo());

            if(tablaProdGanancia.getRowCount() != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdGanancia.getModel();
                for(int i = 0 ; i < tablaProdGanancia.getRowCount() ; i++)
                modeloProd.removeRow(0);
            }
        }
    }//GEN-LAST:event_iconBuscarGananciaMouseClicked

    private void comboOrdenarGananItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarGananItemStateChanged
        if(comboOrdenarGanan.getSelectedItem().equals("Folio")){
            controladorGanancias = new ControladorGanancias(0,"Folio","");
            tablaGanancias.setModel(controladorGanancias.getModelo());
        }
        if(comboOrdenarGanan.getSelectedItem().equals("Fecha ascendente")){
            controladorGanancias = new ControladorGanancias(0,"FechaASC","");
            tablaGanancias.setModel(controladorGanancias.getModelo());
        }
        if(comboOrdenarGanan.getSelectedItem().equals("Fecha descendente")){
            controladorGanancias = new ControladorGanancias(0,"FechaDESC","");
            tablaGanancias.setModel(controladorGanancias.getModelo());
        }
        
        if(tablaProdGanancia.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdGanancia.getModel();
            for(int i = 0 ; i < tablaProdGanancia.getRowCount() ; i++)
            modeloProd.removeRow(0);
        }
    }//GEN-LAST:event_comboOrdenarGananItemStateChanged

    private void btnCalcularGanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGanActionPerformed
        //se oculta todo
        ocultarTodo();
        //se habilitan los paneles correspondientes
        panelCalcularGan.setVisible(true);
        panelCalcularGan.setEnabled(true);
        
        //se vacian las tablas y campos
        int filas = tablaGananciaDia.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaDia.getModel();
            modeloProd.setRowCount(0);
        }
        
        filas = tablaGananciaSemana.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaSemana.getModel();
            modeloProd.setRowCount(0);
        }
        
        filas = tablaGananciaMes.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaMes.getModel();
            modeloProd.setRowCount(0);
        }
        
        this.campoDiaGD.setText("Día");
        this.campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGD.setText("Mes");
        this.campoMesGD.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGD.setText("Año");
        this.campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        this.campoVentasGD.setText("");
        this.campoGanDTotal.setText("");
        
        this.campoDiaGS.setText("Día");
        this.campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGS.setText("Mes");
        this.campoMesGS.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGS.setText("Año");
        this.campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        this.campoVentasGS.setText("");
        this.campoGanSTotal.setText("");
        
        this.campoAnioGM.setText("");
        this.campoVentasGM.setText("");
        this.campoGanMTotal.setText("");
    }//GEN-LAST:event_btnCalcularGanActionPerformed

    private void btnCalcularGDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGDActionPerformed
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if((this.campoDiaGD.getText().equals("Día")) || (this.campoMesGD.getText().equals("Mes")) || (this.campoAnioGD.getText().equals("Año"))){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((Integer.parseInt(this.campoDiaGD.getText()) > 31) || (Integer.parseInt(this.campoDiaGD.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGD.getText()) > 12) || (Integer.parseInt(this.campoMesGD.getText()) < 1)
                    || (this.campoAnioGD.getText().length() != 4) || (Integer.parseInt(this.campoAnioGD.getText()) < 2000)){
                String m = "Fecha inválida.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                campoDiaGD.setText("Día");
                campoDiaGD.setForeground(new java.awt.Color(204,204,204));
                
                campoMesGD.setText("Mes");
                campoMesGD.setForeground(new java.awt.Color(204,204,204));
                
                campoAnioGD.setText("Año");
                campoAnioGD.setForeground(new java.awt.Color(204,204,204));
                
                int filas = tablaGananciaDia.getRowCount(); 
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaDia.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoVentasGD.setText("");
                this.campoGanDTotal.setText("");
            }
            else{
                int filas = tablaGananciaDia.getRowCount(); 
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaDia.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                int anio = Integer.parseInt(this.campoAnioGD.getText());
                boolean bisiesto = false;
                //se evalua si el año es bisiesto
                if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                    bisiesto = true;
                //se valida el mes de febrero si el año es bisiesto
                if(((Integer.parseInt(this.campoMesGD.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGD.getText()) > 28) &&
                        (bisiesto != true)) ||
                        ((Integer.parseInt(this.campoMesGD.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGD.getText()) > 29) &&
                        (bisiesto != false))){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
                else{
                    String fecha = this.campoAnioGD.getText()+"-"+this.campoMesGD.getText()+"-"+this.campoDiaGD.getText();
                    controladorGanancias = new ControladorGanancias(1,fecha,"");
                    tablaGananciaDia.setModel(controladorGanancias.getModelo());
                    float total = 0;
                    if(tablaGananciaDia.getRowCount() != 0){
                        for(int i = 0 ; i < tablaGananciaDia.getRowCount() ; i++)
                            total += Float.parseFloat(tablaGananciaDia.getValueAt(i, 1).toString());
                    }
                    this.campoVentasGD.setText(""+tablaGananciaDia.getRowCount());
                    this.campoGanDTotal.setText(""+total);
                }
            }
        }
    }//GEN-LAST:event_btnCalcularGDActionPerformed

    private void campoDiaGDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGDKeyTyped
        if(!(this.campoDiaGD.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGDKeyTyped

    private void campoMesGDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGDKeyTyped
        if(!(this.campoMesGD.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGDKeyTyped

    private void campoAnioGDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGDKeyTyped
        if(!(this.campoAnioGD.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGDKeyTyped

    private void campoDiaGDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGDMousePressed
        if(campoDiaGD.getText().equals("Día")){
            campoDiaGD.setText("");
            campoDiaGD.setForeground(Color.black);
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGDMousePressed

    private void campoMesGDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGDMousePressed
        if(campoMesGD.getText().equals("Mes")){
            campoMesGD.setText("");
            campoMesGD.setForeground(Color.BLACK);
        }
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGDMousePressed

    private void campoAnioGDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGDMousePressed
        if(campoAnioGD.getText().equals("Año")){
            campoAnioGD.setText("");
            campoAnioGD.setForeground(Color.BLACK);
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGDMousePressed

    private void btnCalcularGSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGSActionPerformed
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
        if((this.campoDiaGS.getText().equals("Día")) || (this.campoMesGS.getText().equals("Mes")) || (this.campoAnioGS.getText().equals("Año"))){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((Integer.parseInt(this.campoDiaGS.getText()) > 31) || (Integer.parseInt(this.campoDiaGS.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGS.getText()) > 12) || (Integer.parseInt(this.campoMesGS.getText()) < 1)
                    || (this.campoAnioGS.getText().length() != 4) || (Integer.parseInt(this.campoAnioGS.getText()) < 2000)){
                String m = "Fecha inválida.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                campoDiaGS.setText("Día");
                campoDiaGS.setForeground(new java.awt.Color(204,204,204));


                campoMesGS.setText("Mes");
                campoMesGS.setForeground(new java.awt.Color(204,204,204));


                campoAnioGS.setText("Año");
                campoAnioGS.setForeground(new java.awt.Color(204,204,204));
                
                int filas = tablaGananciaSemana.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaSemana.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoVentasGS.setText("");
                this.campoGanSTotal.setText("");
            }
            else{
                int filas = tablaGananciaSemana.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaSemana.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                int anio = Integer.parseInt(this.campoAnioGS.getText());
                boolean bisiesto = false;
                if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                    bisiesto = true;

                if(((Integer.parseInt(this.campoMesGS.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGS.getText()) > 28) &&
                        (bisiesto != true)) ||
                        ((Integer.parseInt(this.campoMesGS.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGS.getText()) > 29) &&
                        (bisiesto != false))){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
                else{
                    String fecha = this.campoAnioGS.getText()+"-"+this.campoMesGS.getText()+"-"+this.campoDiaGS.getText();
                    controladorGanancias = new ControladorGanancias(2,fecha,"");
                    tablaGananciaSemana.setModel(controladorGanancias.getModelo());
                    float total = 0;
                    if(tablaGananciaSemana.getRowCount() != 0){
                        for(int i = 0 ; i < tablaGananciaSemana.getRowCount() ; i++)
                            total += Float.parseFloat(tablaGananciaSemana.getValueAt(i, 1).toString());
                    }
                    this.campoVentasGS.setText(""+tablaGananciaSemana.getRowCount());
                    this.campoGanSTotal.setText(""+total);
                }
            }
        }
    }//GEN-LAST:event_btnCalcularGSActionPerformed

    private void campoDiaGSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGSKeyTyped
        if(!(this.campoDiaGS.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGSKeyTyped

    private void campoMesGSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGSKeyTyped
        if(!(this.campoMesGS.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGSKeyTyped

    private void campoAnioGSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGSKeyTyped
        if(!(this.campoAnioGS.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGSKeyTyped

    private void campoDiaGSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGSMousePressed
        if(campoDiaGS.getText().equals("Día")){
            campoDiaGS.setText("");
            campoDiaGS.setForeground(Color.black);
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGSMousePressed

    private void campoMesGSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGSMousePressed
        if(campoMesGS.getText().equals("Mes")){
            campoMesGS.setText("");
            campoMesGS.setForeground(Color.BLACK);
        }
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGSMousePressed

    private void campoAnioGSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGSMousePressed
        if(campoAnioGS.getText().equals("Año")){
            campoAnioGS.setText("");
            campoAnioGS.setForeground(Color.BLACK);
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGSMousePressed

    private void btnCalcularGMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGMActionPerformed
        if(this.campoAnioGM.getText().equals("")){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((this.campoAnioGM.getText().length() != 4) || (Integer.parseInt(this.campoAnioGM.getText()) < 2000)){
                String m = "Año inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                int filas = tablaGananciaMes.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaMes.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoAnioGM.setText("");
                this.campoVentasGM.setText("");
                this.campoGanMTotal.setText("");
            }
            else{
                int filas = tablaGananciaMes.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGananciaMes.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                String mes = comboMesGM.getSelectedItem().toString();
                String anio = this.campoAnioGM.getText();
                controladorGanancias = new ControladorGanancias(3,mes,anio);
                tablaGananciaMes.setModel(controladorGanancias.getModelo());
                float total = 0;
                if(tablaGananciaMes.getRowCount() != 0){
                    for(int i = 0 ; i < tablaGananciaMes.getRowCount() ; i++)
                        total += Float.parseFloat(tablaGananciaMes.getValueAt(i, 1).toString());
                }
                this.campoVentasGM.setText(""+tablaGananciaMes.getRowCount());
                this.campoGanMTotal.setText(""+total);
            }
        }
    }//GEN-LAST:event_btnCalcularGMActionPerformed

    private void campoAnioGMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGMKeyTyped
        if(!(this.campoAnioGM.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGMKeyTyped

    private void campoAnioGMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGMMousePressed
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGMMousePressed

    private void iconBuscarGastoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarGastoMouseClicked
        if(!(this.campoIDGasto.getText().equals(""))){
            controladorGastos = new ControladorGastos(0,"Folio","");
            if(controladorGastos.buscarGastoAct(this.campoIDGasto.getText()))
                tablaGastos.setModel(controladorGastos.getModelo());
            else{
                String m = "Gasto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDGasto.setText("");
            }
        }else{
            controladorGastos = new ControladorGastos(0,"Folio","");
            tablaGastos.setModel(controladorGastos.getModelo());
        }
    }//GEN-LAST:event_iconBuscarGastoMouseClicked

    private void comboOrdenarGastoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarGastoItemStateChanged
        if(comboOrdenarGasto.getSelectedItem().equals("ID")){
            controladorGastos = new ControladorGastos(0,"Folio","");
            tablaGastos.setModel(controladorGastos.getModelo());
        }
        if(comboOrdenarGasto.getSelectedItem().equals("Fecha ascendente")){
            controladorGastos = new ControladorGastos(0,"FechaASC","");
            tablaGastos.setModel(controladorGastos.getModelo());
        }
        if(comboOrdenarGasto.getSelectedItem().equals("Fecha descendente")){
            controladorGastos = new ControladorGastos(0,"FechaDESC","");
            tablaGastos.setModel(controladorGastos.getModelo());
        }
    }//GEN-LAST:event_comboOrdenarGastoItemStateChanged

    private void btnCalcularGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGastosActionPerformed
        //se oculta todo
        ocultarTodo();
        //se habilitan los paneles correspondientes
        panelCalcularGas.setVisible(true);
        panelCalcularGas.setEnabled(true);
        
        //se vacían tablas y campos
        System.out.println(tablaGastosDia.getRowCount());
        int filas = tablaGastosDia.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosDia.getModel();
            modeloProd.setRowCount(0);
        }
        
        filas = tablaGastosSemana.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosSemana.getModel();
            modeloProd.setRowCount(0);
        }
        
        filas = tablaGastosMes.getRowCount();
        if(filas != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosMes.getModel();
            modeloProd.setRowCount(0);
        }
        
        this.campoDiaGsD.setText("Día");
        this.campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGsD.setText("Mes");
        this.campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGsD.setText("Año");
        this.campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        this.campoVentasGsD.setText("");
        this.campoGasDTotal.setText("");
        
        this.campoDiaGsS.setText("Día");
        this.campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGsS.setText("Mes");
        this.campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGsS.setText("Año");
        this.campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        this.campoVentasGsS.setText("");
        this.campoGasSTotal.setText("");
        
        this.campoAnioGsM.setText("");
        this.campoVentasGsM.setText("");
        this.campoGasMTotal.setText("");
        
    }//GEN-LAST:event_btnCalcularGastosActionPerformed

    private void btnNuevoGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoGastoActionPerformed
        //se oculta todo
        ocultarTodo();
        //se habilitan los paneles correspondientes
        panelNuevoGasto.setVisible(true);
        panelNuevoGasto.setEnabled(true);
        //
        ControladorMetodos controladorMetodos = new ControladorMetodos();
        int idNuevo = controladorMetodos.ultimoGasto() + 2;
        this.campoIDGastoN.setText("" + idNuevo);
        this.campoNombreGastoN.setText("");
        this.campoMontoGastoN.setText("");
        this.campoDiaGastoN.setText("Día");
        this.campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGastoN.setText("Mes");
        this.campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGastoN.setText("Año");
        this.campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
    }//GEN-LAST:event_btnNuevoGastoActionPerformed

    private void btnModifGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifGastoActionPerformed
        //se oculta todo
        ocultarTodo();
        //se habilitan los paneles correspondientes
        panelModiGasto.setVisible(true);
        panelModiGasto.setEnabled(true);
        
        this.campoIDGastoM.setText("");
        this.campoIDGastoEn.setText("");
        this.campoNombreGastoM.setText("");
        this.campoMontoGastoM.setText("");
        this.campoDiaGastoM.setText("Día");
        this.campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGastoM.setText("Mes");
        this.campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGastoM.setText("Año");
        this.campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
    }//GEN-LAST:event_btnModifGastoActionPerformed

    private void btnCancelarGastoNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarGastoNActionPerformed
        //se oculta todo
        ocultarTodo();
        //se habilitan los paneles correspondientes
        panelGastos.setVisible(true);
        panelGastos.setEnabled(true);
        //se borran campos
        this.campoIDGastoN.setText("");
        this.campoNombreGastoN.setText("");
        this.campoMontoGastoN.setText("");
        this.campoDiaGastoN.setText("Día");
        this.campoMesGastoN.setText("Mes");
        this.campoAnioGastoN.setText("Año");
    }//GEN-LAST:event_btnCancelarGastoNActionPerformed

    private void campoNombreGastoNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNombreGastoNKeyTyped
        if(this.campoNombreGastoN.getText().length() >= 50)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
    }//GEN-LAST:event_campoNombreGastoNKeyTyped

    private void campoMontoGastoNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMontoGastoNKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoMontoGastoN.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoMontoGastoNKeyTyped

    private void campoDiaGastoNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGastoNKeyTyped
        if(!(this.campoDiaGastoN.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGastoNKeyTyped

    private void campoMesGastoNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGastoNKeyTyped
        if(!(this.campoMesGastoN.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGastoNKeyTyped

    private void campoAnioGastoNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGastoNKeyTyped
        if(!(this.campoAnioGastoN.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGastoNKeyTyped

    private void campoAnioGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGastoNMousePressed
        if(campoAnioGastoN.getText().equals("Año")){
            campoAnioGastoN.setText("");
            campoAnioGastoN.setForeground(Color.BLACK);
        }
        if(campoMesGastoN.getText().isEmpty()){
            campoMesGastoN.setText("Mes");
            campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGastoN.getText().isEmpty()){
            campoDiaGastoN.setText("Día");
            campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGastoNMousePressed

    private void campoMesGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGastoNMousePressed
        if(campoMesGastoN.getText().equals("Mes")){
            campoMesGastoN.setText("");
            campoMesGastoN.setForeground(Color.BLACK);
        }
        if(campoDiaGastoN.getText().isEmpty()){
            campoDiaGastoN.setText("Día");
            campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoN.getText().isEmpty()){
            campoAnioGastoN.setText("Año");
            campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGastoNMousePressed

    private void campoDiaGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGastoNMousePressed
        if(campoDiaGastoN.getText().equals("Día")){
            campoDiaGastoN.setText("");
            campoDiaGastoN.setForeground(Color.black);
        }
        if(campoMesGastoN.getText().isEmpty()){
            campoMesGastoN.setText("Mes");
            campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoN.getText().isEmpty()){
            campoAnioGastoN.setText("Año");
            campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGastoNMousePressed

    private void campoNombreGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoNombreGastoNMousePressed
        if(campoDiaGastoN.getText().isEmpty()){
            campoDiaGastoN.setText("Día");
            campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoN.getText().isEmpty()){
            campoMesGastoN.setText("Mes");
            campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoN.getText().isEmpty()){
            campoAnioGastoN.setText("Año");
            campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoNombreGastoNMousePressed

    private void campoMontoGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMontoGastoNMousePressed
        if(campoDiaGastoN.getText().isEmpty()){
            campoDiaGastoN.setText("Día");
            campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoN.getText().isEmpty()){
            campoMesGastoN.setText("Mes");
            campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoN.getText().isEmpty()){
            campoAnioGastoN.setText("Año");
            campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMontoGastoNMousePressed

    private void comboTipoGastoNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboTipoGastoNMousePressed
        if(campoDiaGastoN.getText().isEmpty()){
            campoDiaGastoN.setText("Día");
            campoDiaGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoN.getText().isEmpty()){
            campoMesGastoN.setText("Mes");
            campoMesGastoN.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoN.getText().isEmpty()){
            campoAnioGastoN.setText("Año");
            campoAnioGastoN.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_comboTipoGastoNMousePressed

    private void btnAceptarGastoNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarGastoNActionPerformed
        if((this.campoNombreGastoN.getText().equals("")) || (this.campoMontoGastoN.getText().equals("")) ||
                (this.campoDiaGastoN.getText().equals("Día")) || (this.campoMesGastoN.getText().equals("Mes")) ||
                (this.campoAnioGastoN.getText().equals("Año"))){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((Integer.parseInt(this.campoDiaGastoN.getText()) > 31) || (Integer.parseInt(this.campoDiaGastoN.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGastoN.getText()) > 12) || (Integer.parseInt(this.campoMesGastoN.getText()) < 1)
                    || (this.campoAnioGastoN.getText().length() != 4) || (Integer.parseInt(this.campoAnioGastoN.getText()) < 1000)){
                String m = "Fecha inválida.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
            else{
                int anio = Integer.parseInt(this.campoAnioGastoN.getText());
                boolean bisiesto = false;
                if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                    bisiesto = true;
                
                if(((Integer.parseInt(this.campoMesGastoN.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGastoN.getText()) > 28) &&
                        (bisiesto != true)) ||
                        ((Integer.parseInt(this.campoMesGastoN.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGastoN.getText()) > 29) &&
                        (bisiesto != false))){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
                else{
                    Gastos gasto = new Gastos();
                    String fecha = this.campoAnioGastoN.getText()+"-"+this.campoMesGastoN.getText()+"-"+this.campoDiaGastoN.getText();
                    gasto.setId_gasto(Integer.parseInt(this.campoIDGastoN.getText()));
                    gasto.setNombre(this.campoNombreGastoN.getText());
                    gasto.setFecha(fecha);
                    gasto.setMonto(Float.parseFloat(this.campoMontoGastoN.getText()));
                    gasto.setTipo(this.comboTipoGastoN.getSelectedItem().toString());
                    if(controladorGastos.agregarGasto(gasto)){
                        controladorGastos = new ControladorGastos(0,"Folio","");
                        tablaGastos.setModel(controladorGastos.getModelo());
                        
                        String m = "Registro realizado.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                        
                        ocultarTodo();
                        panelGastos.setVisible(true);
                        panelGastos.setEnabled(true);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnAceptarGastoNActionPerformed

    private void campoFolioGananciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoFolioGananciaKeyTyped
        if(!(this.campoFolioGanancia.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoFolioGananciaKeyTyped

    private void campoIDGastoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDGastoKeyTyped
        if(!(this.campoIDGasto.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDGastoKeyTyped

    private void comboTipoGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboTipoGastoMMousePressed
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_comboTipoGastoMMousePressed

    private void campoDiaGsDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGsDMousePressed
        if(campoDiaGsD.getText().equals("Día")){
            campoDiaGsD.setText("");
            campoDiaGsD.setForeground(Color.black);
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGsDMousePressed

    private void campoDiaGsDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGsDKeyTyped
        if(!(this.campoDiaGsD.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGsDKeyTyped

    private void campoMesGsDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGsDMousePressed
        if(campoMesGsD.getText().equals("Mes")){
            campoMesGsD.setText("");
            campoMesGsD.setForeground(Color.BLACK);
        }
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGsDMousePressed

    private void campoMesGsDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGsDKeyTyped
        if(!(this.campoMesGsD.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGsDKeyTyped

    private void campoAnioGsDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGsDMousePressed
        if(campoAnioGsD.getText().equals("Año")){
            campoAnioGsD.setText("");
            campoAnioGsD.setForeground(Color.BLACK);
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGsDMousePressed

    private void campoAnioGsDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGsDKeyTyped
        if(!(this.campoAnioGsD.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGsDKeyTyped

    private void campoDiaGsSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGsSMousePressed
        if(campoDiaGsS.getText().equals("Día")){
            campoDiaGsS.setText("");
            campoDiaGsS.setForeground(Color.black);
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGsSMousePressed

    private void campoDiaGsSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGsSKeyTyped
        if(!(this.campoDiaGsS.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGsSKeyTyped

    private void campoMesGsSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGsSMousePressed
        if(campoMesGsS.getText().equals("Mes")){
            campoMesGsS.setText("");
            campoMesGsS.setForeground(Color.BLACK);
        }
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGsSMousePressed

    private void campoMesGsSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGsSKeyTyped
        if(!(this.campoMesGsS.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGsSKeyTyped

    private void campoAnioGsSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGsSMousePressed
        if(campoAnioGsS.getText().equals("Año")){
            campoAnioGsS.setText("");
            campoAnioGsS.setForeground(Color.BLACK);
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGsSMousePressed

    private void campoAnioGsSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGsSKeyTyped
        if(!(this.campoAnioGsS.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGsSKeyTyped

    private void campoAnioGsMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGsMMousePressed
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGsMMousePressed

    private void campoAnioGsMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGsMKeyTyped
        if(!(this.campoAnioGsM.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGsMKeyTyped

    private void btnCalcularGsDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGsDActionPerformed
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if((this.campoDiaGsD.getText().equals("Día")) || (this.campoMesGsD.getText().equals("Mes")) || (this.campoAnioGsD.getText().equals("Año"))){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((Integer.parseInt(this.campoDiaGsD.getText()) > 31) || (Integer.parseInt(this.campoDiaGsD.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGsD.getText()) > 12) || (Integer.parseInt(this.campoMesGsD.getText()) < 1)
                    || (this.campoAnioGsD.getText().length() != 4) || (Integer.parseInt(this.campoAnioGsD.getText()) < 2000)){
                String m = "Fecha inválida.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                campoDiaGsD.setText("Día");
                campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
                
                campoMesGsD.setText("Mes");
                campoMesGsD.setForeground(new java.awt.Color(204,204,204));
                
                campoAnioGsD.setText("Año");
                campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
                
                int filas = tablaGastosDia.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosDia.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoVentasGsD.setText("");
                this.campoGasDTotal.setText("");
            }
            else{
                int filas = tablaGastosDia.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosDia.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                int anio = Integer.parseInt(this.campoAnioGsD.getText());
                boolean bisiesto = false;
                if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                    bisiesto = true;

                if(((Integer.parseInt(this.campoMesGsD.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGsD.getText()) > 28) &&
                        (bisiesto != true)) ||
                        ((Integer.parseInt(this.campoMesGsD.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGsD.getText()) > 29) &&
                        (bisiesto != false))){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }else{
                    String fecha = this.campoAnioGsD.getText()+"-"+this.campoMesGsD.getText()+"-"+this.campoDiaGsD.getText();
                    controladorGastos = new ControladorGastos(1,fecha,"");
                    tablaGastosDia.setModel(controladorGastos.getModelo());

                    float total = 0;
                    if(tablaGastosDia.getRowCount() != 0){
                        for(int i = 0 ; i < tablaGastosDia.getRowCount() ; i++)
                            total += Float.parseFloat(tablaGastosDia.getValueAt(i, 1).toString());
                    }

                    this.campoVentasGsD.setText(""+tablaGastosDia.getRowCount());
                    this.campoGasDTotal.setText(""+total);
                }
            }
        }
    }//GEN-LAST:event_btnCalcularGsDActionPerformed

    private void btnCalcularGsSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGsSActionPerformed
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if((this.campoDiaGsS.getText().equals("Día")) || (this.campoMesGsS.getText().equals("Mes")) || (this.campoAnioGsS.getText().equals("Año"))){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((Integer.parseInt(this.campoDiaGsS.getText()) > 31) || (Integer.parseInt(this.campoDiaGsS.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGsS.getText()) > 12) || (Integer.parseInt(this.campoMesGsS.getText()) < 1)
                    || (this.campoAnioGsS.getText().length() != 4) || (Integer.parseInt(this.campoAnioGsS.getText()) < 2000)){
                String m = "Fecha inválida.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                campoDiaGsS.setText("Día");
                campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
                
                campoMesGsS.setText("Mes");
                campoMesGsS.setForeground(new java.awt.Color(204,204,204));
                
                campoAnioGsS.setText("Año");
                campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
                
                int filas = tablaGastosSemana.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosSemana.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoVentasGsS.setText("");
                this.campoGasSTotal.setText("");
            }
            else{
                int filas = tablaGastosSemana.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosSemana.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                int anio = Integer.parseInt(this.campoAnioGsS.getText());
                boolean bisiesto = false;
                if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                    bisiesto = true;

                if(((Integer.parseInt(this.campoMesGsS.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGsS.getText()) > 28) &&
                        (bisiesto != true)) ||
                        ((Integer.parseInt(this.campoMesGsS.getText()) == 2) && 
                        (Integer.parseInt(this.campoDiaGsS.getText()) > 29) &&
                        (bisiesto != false))){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
                else{
                    String fecha = this.campoAnioGsS.getText()+"-"+this.campoMesGsS.getText()+"-"+this.campoDiaGsS.getText();
                    controladorGastos = new ControladorGastos(2,fecha,"");
                    tablaGastosSemana.setModel(controladorGastos.getModelo());

                    float total = 0;
                    if(tablaGastosSemana.getRowCount() != 0){
                        for(int i = 0 ; i < tablaGastosSemana.getRowCount() ; i++)
                            total += Float.parseFloat(tablaGastosSemana.getValueAt(i, 1).toString());
                    }

                    this.campoVentasGsS.setText(""+tablaGastosSemana.getRowCount());
                    this.campoGasSTotal.setText(""+total);
                }
            }
        }
    }//GEN-LAST:event_btnCalcularGsSActionPerformed

    private void btnCalcularGsMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularGsMActionPerformed
        if(this.campoAnioGsM.getText().equals("")){
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        else{
            if((this.campoAnioGsM.getText().length() != 4) || (Integer.parseInt(this.campoAnioGsM.getText()) < 2000)){
                String m = "Año inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                int filas = tablaGastosMes.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosMes.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoAnioGsM.setText("");
            }
            else{
                int filas = tablaGastosMes.getRowCount();
                if(filas != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaGastosMes.getModel();
                    for(int i = 0 ; i < filas ; i++)
                        modeloProd.removeRow(0);
                }
                
                String mes = comboMesGsM.getSelectedItem().toString();
                String anio = this.campoAnioGsM.getText();
                controladorGastos = new ControladorGastos(3,mes,anio);
                tablaGastosMes.setModel(controladorGastos.getModelo());
                
                float total = 0;
                if(tablaGastosMes.getRowCount() != 0){
                    for(int i = 0 ; i < tablaGastosMes.getRowCount() ; i++)
                        total += Float.parseFloat(tablaGastosMes.getValueAt(i, 1).toString());
                }
                
                this.campoVentasGsM.setText(""+tablaGastosMes.getRowCount());
                this.campoGasMTotal.setText(""+total);
            }
        }
    }//GEN-LAST:event_btnCalcularGsMActionPerformed

    private void campoDiaGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoDiaGastoMMousePressed
        if(campoDiaGastoM.getText().equals("Día")){
            campoDiaGastoM.setText("");
            campoDiaGastoM.setForeground(Color.black);
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoDiaGastoMMousePressed

    private void campoDiaGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaGastoMKeyTyped
        if(!(this.campoDiaGastoM.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoDiaGastoMKeyTyped

    private void campoMesGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMesGastoMMousePressed
        if(campoMesGastoM.getText().equals("Mes")){
            campoMesGastoM.setText("");
            campoMesGastoM.setForeground(Color.BLACK);
        }
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMesGastoMMousePressed

    private void campoMesGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesGastoMKeyTyped
        if(!(this.campoMesGastoM.getText().length() >= 2)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoMesGastoMKeyTyped

    private void campoAnioGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoAnioGastoMMousePressed
        if(campoAnioGastoM.getText().equals("Año")){
            campoAnioGastoM.setText("");
            campoAnioGastoM.setForeground(Color.BLACK);
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoAnioGastoMMousePressed

    private void campoAnioGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnioGastoMKeyTyped
        if(!(this.campoAnioGastoM.getText().length() >= 4)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoAnioGastoMKeyTyped

    private void btnBuscarGastoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarGastoMActionPerformed
        if(!(this.campoIDGastoM.getText().equals(""))){
            controladorGastos = new ControladorGastos(0,"Folio","");
            Gastos gasto = controladorGastos.buscarGastoPorID(this.campoIDGastoM.getText());
            if(gasto != null){
                if(gasto.getId_gasto() % 2 == 0){
                    String m = "No se puede modificar una compra.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    this.campoIDGastoM.setText("");
                }
                else{
                    this.campoIDGastoEn.setText(""+gasto.getId_gasto());
                    this.campoNombreGastoM.setText(gasto.getNombre());
                    this.campoMontoGastoM.setText(""+gasto.getMonto());
                    String fecha = gasto.getFecha();
                    String[] div = fecha.split("-");
                    String anio = div[0];
                    String mes = div[1];
                    String dia = div[2];
                    this.campoDiaGastoM.setText(dia);
                    this.campoDiaGastoM.setForeground(Color.BLACK);
                    this.campoMesGastoM.setText(mes);
                    this.campoMesGastoM.setForeground(Color.BLACK);
                    this.campoAnioGastoM.setText(anio);
                    this.campoAnioGastoM.setForeground(Color.BLACK);
                    this.comboTipoGastoM.setSelectedItem(gasto.getTipo());
                }
            }else{
                String m = "Gasto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDGastoM.setText("");
            }

        }
    }//GEN-LAST:event_btnBuscarGastoMActionPerformed

    private void btnRecarGastoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecarGastoMActionPerformed
        this.campoIDGastoM.setText("");
        this.campoIDGastoEn.setText("");
        this.campoNombreGastoM.setText("");
        this.campoMontoGastoM.setText("");
        this.campoDiaGastoM.setText("Día");
        this.campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGastoM.setText("Mes");
        this.campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGastoM.setText("Año");
        this.campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
    }//GEN-LAST:event_btnRecarGastoMActionPerformed

    private void campoNombreGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoNombreGastoMMousePressed
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoNombreGastoMMousePressed

    private void campoMontoGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoMontoGastoMMousePressed
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoMontoGastoMMousePressed

    private void campoIDGastoMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoIDGastoMMousePressed
        if(campoDiaGastoM.getText().isEmpty()){
            campoDiaGastoM.setText("Día");
            campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGastoM.getText().isEmpty()){
            campoMesGastoM.setText("Mes");
            campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGastoM.getText().isEmpty()){
            campoAnioGastoM.setText("Año");
            campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_campoIDGastoMMousePressed

    private void campoIDGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDGastoMKeyTyped
        if(this.campoIDGastoM.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDGastoMKeyTyped

    private void campoNombreGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNombreGastoMKeyTyped
        if(this.campoNombreGastoM.getText().length() >= 50)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
    }//GEN-LAST:event_campoNombreGastoMKeyTyped

    private void campoMontoGastoMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMontoGastoMKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoMontoGastoM.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoMontoGastoMKeyTyped

    private void btnModiGastoBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiGastoBDActionPerformed
        controladorGastos = new ControladorGastos(0,"Folio","");
        
        if(!(this.campoIDGastoEn.getText().equals(""))){
            if((this.campoNombreGastoM.getText().equals("")) || (this.campoMontoGastoM.getText().equals("")) || 
                    (this.campoDiaGastoM.getText().equals("Día")) || (this.campoMesGastoM.getText().equals("Mes")) ||
                (this.campoAnioGastoM.getText().equals("Año"))){
                String m = "Hay campos vacíos.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
            else{
                if((Integer.parseInt(this.campoDiaGastoM.getText()) > 31) || (Integer.parseInt(this.campoDiaGastoM.getText()) < 1)
                    || (Integer.parseInt(this.campoMesGastoM.getText()) > 12) || (Integer.parseInt(this.campoMesGastoM.getText()) < 1)
                    || (this.campoAnioGastoM.getText().length() != 4) || (Integer.parseInt(this.campoAnioGastoM.getText()) < 1000)){
                    String m = "Fecha inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
                else{
                    int anio = Integer.parseInt(this.campoAnioGastoM.getText());
                    boolean bisiesto = false;
                    if(((anio % 4 == 0) && (anio % 100 != 0)) || (anio % 400 == 0))
                        bisiesto = true;
                    
                    if(((Integer.parseInt(this.campoMesGastoM.getText()) == 2) && 
                            (Integer.parseInt(this.campoDiaGastoM.getText()) > 28) &&
                            (bisiesto != true)) ||
                            ((Integer.parseInt(this.campoMesGastoM.getText()) == 2) && 
                            (Integer.parseInt(this.campoDiaGastoM.getText()) > 29) &&
                            (bisiesto != false))){
                        String m = "Fecha inválida.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                    }
                    else{
                        Gastos gasto = new Gastos();
                        gasto.setId_gasto(Integer.parseInt(this.campoIDGastoEn.getText()));
                        gasto.setNombre(this.campoNombreGastoM.getText());
                        gasto.setMonto(Float.parseFloat(this.campoMontoGastoM.getText()));
                        gasto.setFecha(this.campoAnioGastoM.getText()+"-"+this.campoMesGastoM.getText()+"-"+this.campoDiaGastoM.getText());
                        gasto.setTipo(this.comboTipoGastoM.getSelectedItem().toString());
                        if(controladorGastos.actualizaGasto(gasto)){
                            controladorGastos = new ControladorGastos(0,"Folio","");
                            tablaGastos.setModel(controladorGastos.getModelo());
                            
                            String m = "Actualización realizada.";
                            AlertaWarning salir = new AlertaWarning(this, true);
                            salir.mensaje.setText(m);
                            salir.setVisible(true);
                            
                            ocultarTodo();
                            panelGastos.setEnabled(true);
                            panelGastos.setVisible(true);
                        }
                    }
                }
            }
        }

    }//GEN-LAST:event_btnModiGastoBDActionPerformed

    private void btnCancelarGastoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarGastoMActionPerformed
        this.campoIDGastoM.setText("");
        this.campoIDGastoEn.setText("");
        this.campoNombreGastoM.setText("");
        this.campoMontoGastoM.setText("");
        this.campoDiaGastoM.setText("Día");
        this.campoDiaGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoMesGastoM.setText("Mes");
        this.campoMesGastoM.setForeground(new java.awt.Color(204,204,204));
        this.campoAnioGastoM.setText("Año");
        this.campoAnioGastoM.setForeground(new java.awt.Color(204,204,204));
        ocultarTodo();
        panelGastos.setEnabled(true);
        panelGastos.setVisible(true);
    }//GEN-LAST:event_btnCancelarGastoMActionPerformed

    private void comboMesGMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboMesGMMousePressed
        if(campoDiaGD.getText().isEmpty()){
            campoDiaGD.setText("Día");
            campoDiaGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGD.getText().isEmpty()){
            campoMesGD.setText("Mes");
            campoMesGD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGD.getText().isEmpty()){
            campoAnioGD.setText("Año");
            campoAnioGD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGS.getText().isEmpty()){
            campoDiaGS.setText("Día");
            campoDiaGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGS.getText().isEmpty()){
            campoMesGS.setText("Mes");
            campoMesGS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGS.getText().isEmpty()){
            campoAnioGS.setText("Año");
            campoAnioGS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_comboMesGMMousePressed

    private void comboMesGsMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboMesGsMMousePressed
        if(campoDiaGsD.getText().isEmpty()){
            campoDiaGsD.setText("Día");
            campoDiaGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsD.getText().isEmpty()){
            campoMesGsD.setText("Mes");
            campoMesGsD.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsD.getText().isEmpty()){
            campoAnioGsD.setText("Año");
            campoAnioGsD.setForeground(new java.awt.Color(204,204,204));
        }
        
        if(campoDiaGsS.getText().isEmpty()){
            campoDiaGsS.setText("Día");
            campoDiaGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoMesGsS.getText().isEmpty()){
            campoMesGsS.setText("Mes");
            campoMesGsS.setForeground(new java.awt.Color(204,204,204));
        }
        if(campoAnioGsS.getText().isEmpty()){
            campoAnioGsS.setText("Año");
            campoAnioGsS.setForeground(new java.awt.Color(204,204,204));
        }
    }//GEN-LAST:event_comboMesGsMMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AreaFinanzas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AreaFinanzas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AreaFinanzas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AreaFinanzas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AreaFinanzas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptarGastoN;
    private javax.swing.JButton btnBuscarGastoM;
    private javax.swing.JButton btnCalcularGD;
    private javax.swing.JButton btnCalcularGM;
    private javax.swing.JButton btnCalcularGS;
    private javax.swing.JButton btnCalcularGan;
    private javax.swing.JButton btnCalcularGastos;
    private javax.swing.JButton btnCalcularGsD;
    private javax.swing.JButton btnCalcularGsM;
    private javax.swing.JButton btnCalcularGsS;
    private javax.swing.JButton btnCancelarGastoM;
    private javax.swing.JButton btnCancelarGastoN;
    private javax.swing.JButton btnGanancias;
    private javax.swing.JButton btnGastos;
    private javax.swing.JButton btnModiGastoBD;
    private javax.swing.JButton btnModifGasto;
    private javax.swing.JButton btnMostrarGan;
    private javax.swing.JButton btnNuevoGasto;
    private javax.swing.JButton btnRecarGastoM;
    private javax.swing.JTextField campoAnioGD;
    private javax.swing.JTextField campoAnioGM;
    private javax.swing.JTextField campoAnioGS;
    private javax.swing.JTextField campoAnioGastoM;
    private javax.swing.JTextField campoAnioGastoN;
    private javax.swing.JTextField campoAnioGsD;
    private javax.swing.JTextField campoAnioGsM;
    private javax.swing.JTextField campoAnioGsS;
    private javax.swing.JTextField campoDiaGD;
    private javax.swing.JTextField campoDiaGS;
    private javax.swing.JTextField campoDiaGastoM;
    private javax.swing.JTextField campoDiaGastoN;
    private javax.swing.JTextField campoDiaGsD;
    private javax.swing.JTextField campoDiaGsS;
    private javax.swing.JTextField campoFolioGanancia;
    private javax.swing.JTextField campoGanDTotal;
    private javax.swing.JTextField campoGanMTotal;
    private javax.swing.JTextField campoGanSTotal;
    private javax.swing.JTextField campoGasDTotal;
    private javax.swing.JTextField campoGasMTotal;
    private javax.swing.JTextField campoGasSTotal;
    private javax.swing.JTextField campoIDGasto;
    private javax.swing.JTextField campoIDGastoEn;
    private javax.swing.JTextField campoIDGastoM;
    private javax.swing.JTextField campoIDGastoN;
    private javax.swing.JTextField campoMesGD;
    private javax.swing.JTextField campoMesGS;
    private javax.swing.JTextField campoMesGastoM;
    private javax.swing.JTextField campoMesGastoN;
    private javax.swing.JTextField campoMesGsD;
    private javax.swing.JTextField campoMesGsS;
    private javax.swing.JTextField campoMontoGastoM;
    private javax.swing.JTextField campoMontoGastoN;
    private javax.swing.JTextField campoNombreGastoM;
    private javax.swing.JTextField campoNombreGastoN;
    private javax.swing.JTextField campoVentasGD;
    private javax.swing.JTextField campoVentasGM;
    private javax.swing.JTextField campoVentasGS;
    private javax.swing.JTextField campoVentasGsD;
    private javax.swing.JTextField campoVentasGsM;
    private javax.swing.JTextField campoVentasGsS;
    private javax.swing.JLabel cerrarAreaVentas;
    private javax.swing.JComboBox<String> comboMesGM;
    private javax.swing.JComboBox<String> comboMesGsM;
    private javax.swing.JComboBox<String> comboOrdenarGanan;
    private javax.swing.JComboBox<String> comboOrdenarGasto;
    private javax.swing.JComboBox<String> comboTipoGastoM;
    private javax.swing.JComboBox<String> comboTipoGastoN;
    private javax.swing.JLabel fechaSistema;
    private javax.swing.JLabel fondoElimProducto;
    private javax.swing.JLabel fondoFinanzas;
    private javax.swing.JLabel fondoInventario;
    private javax.swing.JLabel fondoNuevProducto;
    private javax.swing.JLabel fondoNuevaCompra;
    private javax.swing.JLabel fondoPedidos;
    private javax.swing.JLabel fondoRegCompras;
    private javax.swing.JPanel headerFinanzas;
    private javax.swing.JLabel horaSistema;
    private javax.swing.JLabel iconBuscarGanancia;
    private javax.swing.JLabel iconBuscarGasto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel panelBtnBuscRecGastModi;
    private javax.swing.JPanel panelBtnCG;
    private javax.swing.JPanel panelBtnModiGast;
    private javax.swing.JPanel panelCalcularGan;
    private javax.swing.JPanel panelCalcularGas;
    private javax.swing.JPanel panelCerrar;
    private javax.swing.JPanel panelFechaHeader;
    private javax.swing.JPanel panelGanancias;
    private javax.swing.JPanel panelGastos;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelModiGasto;
    private javax.swing.JPanel panelNuevoGasto;
    private javax.swing.JPanel panelRaiz;
    private javax.swing.JScrollPane scrollGananciaDia;
    private javax.swing.JScrollPane scrollGananciaMes;
    private javax.swing.JScrollPane scrollGananciaSemana;
    private javax.swing.JScrollPane scrollGanancias;
    private javax.swing.JScrollPane scrollGastos;
    private javax.swing.JScrollPane scrollGastosDia;
    private javax.swing.JScrollPane scrollGastosMes;
    private javax.swing.JScrollPane scrollGastosSemana;
    private javax.swing.JScrollPane scrollProdGanancia;
    private javax.swing.JTable tablaGananciaDia;
    private javax.swing.JTable tablaGananciaMes;
    private javax.swing.JTable tablaGananciaSemana;
    private javax.swing.JTable tablaGanancias;
    private javax.swing.JTable tablaGastos;
    private javax.swing.JTable tablaGastosDia;
    private javax.swing.JTable tablaGastosMes;
    private javax.swing.JTable tablaGastosSemana;
    private javax.swing.JTable tablaProdGanancia;
    // End of variables declaration//GEN-END:variables
}
