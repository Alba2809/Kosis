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
import java.util.ArrayList;
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
public class AreaCompras extends javax.swing.JFrame implements Runnable{
    
    //Variables para el reloj y fecha
    String hora,minutos;
    Calendar callendario;
    Thread reloj;
    
    //Variables que serviran para obtener las coordenadas del mouse (esta se aplicará para mover las ventanas)
    int xMouse, yMouse;
    
    ControladorProductos controladorProductos = new ControladorProductos("ID");
    ControladorProducElim controladorPEliminados = new ControladorProducElim("ID");
    ControladorProveedores controladorProveedor = new ControladorProveedores("ID");
    ControladorAlerta controladorAlertas = new ControladorAlerta(0);
    ControladorAlertaPedido controladorAlertaPedido = new ControladorAlertaPedido(0);
    ControladorCompras controladorCompras = new ControladorCompras("ID");
    ControladorPedidos controladorPedidos = new ControladorPedidos();
    
    
    //Variables para obtener el renglon/fila seleccionado de las tablas de Nueva Venta
    private int renglonAlerta = 0;
    private int columnAlerta = 0;
    private int renglonNotif = 0;
    private int columnNotif = 0;
    private int renglonCompraSelec = 0;
    private int columnCompraSelec = 0;
    private int renglonProdCSelec = 0;
    private int columnProdCSelec = 0;
    private int renglonProdNC = 0;
    private int columnProdNC = 0;
    private int renglonProvSelec = 0;
    private int columnProvSelec = 0;
    
    /**
     * Creates new form AreaCompras
     */
    public AreaCompras() {
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
        //----------------Apartado pedidos-nueva compra------------------------------------------
        JTableHeader tHeaderPedido = tablaPedidos.getTableHeader();
        tHeaderPedido.setDefaultRenderer(new Encabezado());
        tablaPedidos.setTableHeader(tHeaderPedido);
        
        scrollPedidos.getVerticalScrollBar().setUI(new Scroll());
        
        tablaPedidos.setBackground(new Color(250,226,211));
        tablaPedidos.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdNC = tablaProdNC.getTableHeader();
        tHeaderProdNC.setDefaultRenderer(new Encabezado());
        tablaProdNC.setTableHeader(tHeaderProdNC);
        
        scrollProdNC.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdNC.setBackground(new Color(250,226,211));
        tablaProdNC.setSelectionBackground(new Color( 236,200,160));
        //----------------Apartado proveedores----------------------------------------
        JTableHeader tHeaderProveedor = tablaProveedores.getTableHeader();
        tHeaderProveedor.setDefaultRenderer(new Encabezado());
        tablaProveedores.setTableHeader(tHeaderProveedor);
        
        scrollProveedores.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProveedores.setBackground(new Color(250,226,211));
        tablaProveedores.setSelectionBackground(new Color( 236,200,160));
        
        campoIDProvMEnc.setVisible(false);
        //----------------Apartado inventario----------------------------------------
        JTableHeader tHeaderProductos = tablaInventario.getTableHeader();
        tHeaderProductos.setDefaultRenderer(new Encabezado());
        tablaInventario.setTableHeader(tHeaderProductos);
        
        scrollInventario.getVerticalScrollBar().setUI(new Scroll());
        
        tablaInventario.setBackground(new Color(250,226,211));
        tablaInventario.setSelectionBackground(new Color( 236,200,160));
        
        campoIDProdModiEncon.setVisible(false);
        //------------------------------------------------------------------------------
        JTableHeader tHeaderEliminados = tablaEliminados.getTableHeader();
        tHeaderEliminados.setDefaultRenderer(new Encabezado());
        tablaEliminados.setTableHeader(tHeaderEliminados);
        
        scrollEliminados.getVerticalScrollBar().setUI(new Scroll());
        
        tablaEliminados.setBackground(new Color(250,226,211));
        tablaEliminados.setSelectionBackground(new Color( 236,200,160));
        //---------------------Apartado Alertas-Notificaciones------------------------------
        
        JTableHeader tHeaderAlertas = tablaAlertas.getTableHeader();
        tHeaderAlertas.setDefaultRenderer(new Encabezado());
        tablaAlertas.setTableHeader(tHeaderAlertas);
        
        scrollAlertas.getVerticalScrollBar().setUI(new Scroll());
        
        tablaAlertas.setBackground(new Color(250,226,211));
        tablaAlertas.setSelectionBackground(new Color( 236,200,160));
        tablaAlertas.setRowHeight(40);
        
        //------------------------------------------------------------------------------
        JTableHeader tHeaderNotif = tablaNotificacionC.getTableHeader();
        tHeaderNotif.setDefaultRenderer(new Encabezado());
        tablaNotificacionC.setTableHeader(tHeaderNotif);
        
        scrollNotificacionC.getVerticalScrollBar().setUI(new Scroll());
        
        tablaNotificacionC.setBackground(new Color(250,226,211));
        tablaNotificacionC.setSelectionBackground(new Color( 236,200,160));
        tablaNotificacionC.setRowHeight(40);
        
        //---------------------Apartado Compras------------------------------
        JTableHeader tHeaderCompras = tablaCompras.getTableHeader();
        tHeaderCompras.setDefaultRenderer(new Encabezado());
        tablaCompras.setTableHeader(tHeaderCompras);
        
        scrollCompras.getVerticalScrollBar().setUI(new Scroll());
        
        tablaCompras.setBackground(new Color(250,226,211));
        tablaCompras.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdCompra = tablaProdCompra.getTableHeader();
        tHeaderProdCompra.setDefaultRenderer(new Encabezado());
        tablaProdCompra.setTableHeader(tHeaderProdCompra);
        
        scrollProdCompra.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdCompra.setBackground(new Color(250,226,211));
        tablaProdCompra.setSelectionBackground(new Color( 236,200,160));
        //----------------Apartado proveedor------------------------------------------
        comboOrdenarProveedor.setUI(new CustomUI());
        comboOrdenarProveedor.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        
        campoCompReaMP.setBackground(new Color(255,255,255,0));
        //----------------Apartado inventario------------------------------------------
        comboOrdenarInventario.setUI(new CustomUI());
        comboOrdenarInventario.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboCatProdModi.setUI(new CustomUI());
        comboCatProdModi.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));     
        //------------------------------------------------------------------------------
        comboDisProdModi.setUI(new CustomUI());
        comboDisProdModi.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboMotElimProd.setUI(new CustomUI());
        comboMotElimProd.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboOrdenarElim.setUI(new CustomUI());
        comboOrdenarElim.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboCatProdN.setUI(new CustomUI());
        comboCatProdN.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //---------------------Apartado Compras------------------------------
        comboOrdenarCompras.setUI(new CustomUI());
        comboOrdenarCompras.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        
        campoNomProvProdSelec.setBackground(new Color(255,255,255,0));
        campoTelProvProdSelec.setBackground(new Color(255,255,255,0));
        campoComProvProdSelec.setBackground(new Color(255,255,255,0));
        
        campoIDProvNC.setBackground(new Color(255,255,255,0));
        campoNomProvNC.setBackground(new Color(255,255,255,0));
        campoIDCompraN.setBackground(new Color(255,255,255,0));
        campoFechaNC.setBackground(new Color(255,255,255,0));
        CampoTotalNC.setBackground(new Color(255,255,255,0));
        campoIDPAuxliar.setVisible(false);
        //-------------------Apartado inventario--------------------------------------------
        campoNomProdElim.setBackground(new Color(255,255,255,0));
        campoCatProdElim.setBackground(new Color(255,255,255,0));
        campoMarcaProdElim.setBackground(new Color(255,255,255,0));
        campoPrecioCProdElim.setBackground(new Color(255,255,255,0));
        campoPrecioVProdElim.setBackground(new Color(255,255,255,0));
        campoDispProdElim.setBackground(new Color(255,255,255,0));
        campoStockProdElim.setBackground(new Color(255,255,255,0));
        campoIDProElimEnc.setVisible(false);
        campoNomProvNP.setBackground(new Color(255,255,255,0));
        
        //Se ocultan e innabilitan los apartados al iniciar la ventana
        ocultarTodo();
        //se inhabilita la opcion de pegar (ctrl + v) contenido en los campos de texto, esto se 
        //realiza para evitar errores de inserción a la base de datos
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
    
    //Funcion que oculta e innabilita todos los apartados
    public void ocultarTodo(){
        panelNuevaCompra.setVisible(false);
        panelNuevaCompra.setEnabled(false);
        
        panelPedidos.setVisible(false);
        panelPedidos.setEnabled(false);
        //-------------------------------------
        panelProveedor.setVisible(false);
        panelProveedor.setEnabled(false);
        //-------------------------------------
        panelInventario.setVisible(false);
        panelInventario.setEnabled(false);
        
        panelElimProducto.setVisible(false);
        panelElimProducto.setEnabled(false);
        
        panelModiProducto.setVisible(false);
        panelModiProducto.setEnabled(false);
        
        panelRegistroElim.setVisible(false);
        panelRegistroElim.setEnabled(false);
        
        panelNuevProducto.setVisible(false);
        panelNuevProducto.setEnabled(false);
        //-------------------------------------
        panelCompras.setVisible(false);
        panelCompras.setEnabled(false);
        //-------------------------------------
        panelNotificaciones.setVisible(false);
        panelNotificaciones.setEnabled(false);
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
        btnNuevaCompra = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnInventario = new javax.swing.JButton();
        btnCompras = new javax.swing.JButton();
        btnNotificaciones = new javax.swing.JButton();
        panelFechaHeader = new javax.swing.JPanel();
        fechaSistema = new javax.swing.JLabel();
        horaSistema = new javax.swing.JLabel();
        panelPedidos = new javax.swing.JPanel();
        btnCrearCompra = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        scrollPedidos = new javax.swing.JScrollPane();
        tablaPedidos = new javax.swing.JTable();
        campoIDPedidoBuscarTabla = new javax.swing.JTextField();
        iconBuscarProdEliminado1 = new javax.swing.JLabel();
        fondoPedidos = new javax.swing.JLabel();
        panelNuevaCompra = new javax.swing.JPanel();
        campoIDPedidoBuscar = new javax.swing.JTextField();
        iconBuscarPedido = new javax.swing.JLabel();
        campoCantProdNC = new javax.swing.JTextField();
        campoIDProvNC = new javax.swing.JTextField();
        campoNomProvNC = new javax.swing.JTextField();
        campoIDCompraN = new javax.swing.JTextField();
        campoFechaNC = new javax.swing.JTextField();
        CampoTotalNC = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnModiCantProdNC = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        scrollProdNC = new javax.swing.JScrollPane();
        tablaProdNC = new javax.swing.JTable();
        campoIDPAuxliar = new javax.swing.JTextField();
        panelAcCanFac = new javax.swing.JPanel();
        btnAcepNuevaCompra = new javax.swing.JButton();
        btnCanNuevaCompra = new javax.swing.JButton();
        fondoNuevaCompra = new javax.swing.JLabel();
        panelProveedor = new javax.swing.JPanel();
        panelTabProveedores = new javax.swing.JPanel();
        scrollProveedores = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();
        iconBuscarProveedor = new javax.swing.JLabel();
        campoIDProvBuscar = new javax.swing.JTextField();
        comboOrdenarProveedor = new javax.swing.JComboBox<>();
        campoNombreNP = new javax.swing.JTextField();
        campoTelNP = new javax.swing.JTextField();
        campoIDProvBuscarM = new javax.swing.JTextField();
        iconBuscarPM = new javax.swing.JLabel();
        campoNombreMP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoTelMP = new javax.swing.JTextField();
        campoDireccMP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        campoCompReaMP = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevoProveedor = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnModifProveedor = new javax.swing.JButton();
        campoIDProvMEnc = new javax.swing.JTextField();
        campoDireccNP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnEliminarProv = new javax.swing.JButton();
        fondoProveedor = new javax.swing.JLabel();
        panelInventario = new javax.swing.JPanel();
        panelBtnProduc = new javax.swing.JPanel();
        btnModiProducto = new javax.swing.JButton();
        btnElimProducto = new javax.swing.JButton();
        btnMostrarElim = new javax.swing.JButton();
        btnRegProducto = new javax.swing.JButton();
        iconBuscarProdInven = new javax.swing.JLabel();
        comboOrdenarInventario = new javax.swing.JComboBox<>();
        panelTablaInventario = new javax.swing.JPanel();
        scrollInventario = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        campoIDProdInventario = new javax.swing.JTextField();
        fondoInventario = new javax.swing.JLabel();
        panelNuevProducto = new javax.swing.JPanel();
        campoIDProvBuscarNP = new javax.swing.JTextField();
        iconBuscarProvNP = new javax.swing.JLabel();
        campoNomProvNP = new javax.swing.JTextField();
        campoNombreProdN = new javax.swing.JTextField();
        campoMarcaProdN = new javax.swing.JTextField();
        comboCatProdN = new javax.swing.JComboBox<>();
        campoPCProdN = new javax.swing.JTextField();
        campoPVProdN = new javax.swing.JTextField();
        campoLimProdN = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCancelarNP = new javax.swing.JButton();
        fondoNuevProducto = new javax.swing.JLabel();
        panelModiProducto = new javax.swing.JPanel();
        campoIDProdModi = new javax.swing.JTextField();
        campoNomProdModi = new javax.swing.JTextField();
        comboCatProdModi = new javax.swing.JComboBox<>();
        campoPrCProdModi = new javax.swing.JTextField();
        campoPrVProdModi = new javax.swing.JTextField();
        comboDisProdModi = new javax.swing.JComboBox<>();
        campoStockProdModi = new javax.swing.JTextField();
        campoLimiteProdModi = new javax.swing.JTextField();
        panelBtnModiProd = new javax.swing.JPanel();
        btnModiProductoBD = new javax.swing.JButton();
        btnCancelarProdModi = new javax.swing.JButton();
        panelBtnBuscRecProdModi = new javax.swing.JPanel();
        btnBuscarProdModi = new javax.swing.JButton();
        btnRecarProdModi = new javax.swing.JButton();
        campoIDProdModiEncon = new javax.swing.JTextField();
        campoMarcaProdModi = new javax.swing.JTextField();
        fondoElimProducto = new javax.swing.JLabel();
        panelElimProducto = new javax.swing.JPanel();
        btnElimProductoBD = new javax.swing.JButton();
        panelBtnCanProdElim = new javax.swing.JPanel();
        btnCancelarProdElim = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnElimProdCant = new javax.swing.JButton();
        campoIDProdElim = new javax.swing.JTextField();
        campoNomProdElim = new javax.swing.JTextField();
        campoCatProdElim = new javax.swing.JTextField();
        campoMarcaProdElim = new javax.swing.JTextField();
        campoPrecioCProdElim = new javax.swing.JTextField();
        campoPrecioVProdElim = new javax.swing.JTextField();
        campoDispProdElim = new javax.swing.JTextField();
        campoStockProdElim = new javax.swing.JTextField();
        campoCantElimProdElim = new javax.swing.JTextField();
        panelBtnBuscRecProdElim = new javax.swing.JPanel();
        btnBuscarProdElim = new javax.swing.JButton();
        btnRecarProdElim = new javax.swing.JButton();
        campoIDProElimEnc = new javax.swing.JTextField();
        comboMotElimProd = new javax.swing.JComboBox<>();
        fondoModiProducto = new javax.swing.JLabel();
        panelRegistroElim = new javax.swing.JPanel();
        panelBtnAcepElim = new javax.swing.JPanel();
        btnAceptarRegElim = new javax.swing.JButton();
        iconBuscarProdEliminado = new javax.swing.JLabel();
        comboOrdenarElim = new javax.swing.JComboBox<>();
        panelTablaEliminados = new javax.swing.JPanel();
        scrollEliminados = new javax.swing.JScrollPane();
        tablaEliminados = new javax.swing.JTable();
        campoIDProdIEliminado = new javax.swing.JTextField();
        fondoInventario1 = new javax.swing.JLabel();
        panelCompras = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        scrollCompras = new javax.swing.JScrollPane();
        tablaCompras = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        scrollProdCompra = new javax.swing.JScrollPane();
        tablaProdCompra = new javax.swing.JTable();
        btnMostrarCompra = new javax.swing.JButton();
        campoIDCompraBuscar = new javax.swing.JTextField();
        iconBuscarCompra = new javax.swing.JLabel();
        campoNomProvProdSelec = new javax.swing.JTextField();
        campoTelProvProdSelec = new javax.swing.JTextField();
        campoComProvProdSelec = new javax.swing.JTextField();
        comboOrdenarCompras = new javax.swing.JComboBox<>();
        fondoRegCompras = new javax.swing.JLabel();
        panelNotificaciones = new javax.swing.JPanel();
        panelTablaAlertas = new javax.swing.JPanel();
        scrollAlertas = new javax.swing.JScrollPane();
        tablaAlertas = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        btnGenReporteAlerta = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        panelTablaNotificaciones = new javax.swing.JPanel();
        scrollNotificacionC = new javax.swing.JScrollPane();
        tablaNotificacionC = new javax.swing.JTable();
        fondoNotificaciones = new javax.swing.JLabel();
        fondoCompras = new javax.swing.JLabel();
        headerCompras = new javax.swing.JPanel();

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

        btnNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNuevaCompraNS.png"))); // NOI18N
        btnNuevaCompra.setBorder(null);
        btnNuevaCompra.setBorderPainted(false);
        btnNuevaCompra.setContentAreaFilled(false);
        btnNuevaCompra.setFocusPainted(false);
        btnNuevaCompra.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNuevaCompraS.png"))); // NOI18N
        btnNuevaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaCompraActionPerformed(evt);
            }
        });

        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuProveedorNS.png"))); // NOI18N
        btnProveedor.setBorder(null);
        btnProveedor.setBorderPainted(false);
        btnProveedor.setContentAreaFilled(false);
        btnProveedor.setFocusPainted(false);
        btnProveedor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuProveedorS.png"))); // NOI18N
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuInventarioNS.png"))); // NOI18N
        btnInventario.setBorder(null);
        btnInventario.setBorderPainted(false);
        btnInventario.setContentAreaFilled(false);
        btnInventario.setFocusPainted(false);
        btnInventario.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuInventarioS.png"))); // NOI18N
        btnInventario.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuInventarioNS.png"))); // NOI18N
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });

        btnCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuComprasNS.png"))); // NOI18N
        btnCompras.setBorder(null);
        btnCompras.setBorderPainted(false);
        btnCompras.setContentAreaFilled(false);
        btnCompras.setFocusPainted(false);
        btnCompras.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuComprasS.png"))); // NOI18N
        btnCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprasActionPerformed(evt);
            }
        });

        btnNotificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNotifiNS.png"))); // NOI18N
        btnNotificaciones.setBorder(null);
        btnNotificaciones.setBorderPainted(false);
        btnNotificaciones.setContentAreaFilled(false);
        btnNotificaciones.setFocusPainted(false);
        btnNotificaciones.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNotifiS.png"))); // NOI18N
        btnNotificaciones.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNotifiNS.png"))); // NOI18N
        btnNotificaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificacionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevaCompra)
                .addGap(18, 18, 18)
                .addComponent(btnProveedor)
                .addGap(18, 18, 18)
                .addComponent(btnInventario)
                .addGap(18, 18, 18)
                .addComponent(btnCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNotificaciones)
                .addContainerGap(217, Short.MAX_VALUE))
        );

        panelRaiz.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 190, 520));

        panelFechaHeader.setBackground(new java.awt.Color(255, 255, 255));

        fechaSistema.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        fechaSistema.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fechaSistema.setText("fecha");

        horaSistema.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        horaSistema.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
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

        panelPedidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCrearCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCrearCompraNS.png"))); // NOI18N
        btnCrearCompra.setBorder(null);
        btnCrearCompra.setBorderPainted(false);
        btnCrearCompra.setContentAreaFilled(false);
        btnCrearCompra.setFocusPainted(false);
        btnCrearCompra.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCrearCompraS.png"))); // NOI18N
        btnCrearCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearCompraActionPerformed(evt);
            }
        });
        panelPedidos.add(btnCrearCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(422, 594, 210, 60));

        jPanel9.setBackground(new java.awt.Color(255, 237, 225));

        tablaPedidos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Pedido", "ID Producto", "Nombre", "Categoría", "Marca", "Stock", "Limite", "Fecha"
            }
        ));
        tablaPedidos.getTableHeader().setReorderingAllowed(false);
        scrollPedidos.setViewportView(tablaPedidos);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelPedidos.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 980, 400));

        campoIDPedidoBuscarTabla.setBorder(null);
        campoIDPedidoBuscarTabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDPedidoBuscarTablaKeyTyped(evt);
            }
        });
        panelPedidos.add(campoIDPedidoBuscarTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 111, 145, 20));

        iconBuscarProdEliminado1.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarProdEliminado1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProdEliminado1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProdEliminado1.setOpaque(true);
        iconBuscarProdEliminado1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProdEliminado1MouseClicked(evt);
            }
        });
        panelPedidos.add(iconBuscarProdEliminado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(823, 110, 22, 22));

        fondoPedidos.setBackground(new java.awt.Color(255, 255, 255));
        fondoPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoPedidos.png"))); // NOI18N
        panelPedidos.add(fondoPedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelPedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNuevaCompra.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDPedidoBuscar.setBorder(null);
        campoIDPedidoBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDPedidoBuscarKeyTyped(evt);
            }
        });
        panelNuevaCompra.add(campoIDPedidoBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 214, 224, 20));

        iconBuscarPedido.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarPedido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarPedido.setOpaque(true);
        iconBuscarPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarPedidoMouseClicked(evt);
            }
        });
        panelNuevaCompra.add(iconBuscarPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 214, 22, 22));

        campoCantProdNC.setBorder(null);
        campoCantProdNC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoCantProdNCKeyTyped(evt);
            }
        });
        panelNuevaCompra.add(campoCantProdNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 302, 256, 20));

        campoIDProvNC.setEditable(false);
        campoIDProvNC.setBorder(null);
        panelNuevaCompra.add(campoIDProvNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 512, 254, 20));

        campoNomProvNC.setEditable(false);
        campoNomProvNC.setBorder(null);
        panelNuevaCompra.add(campoNomProvNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 565, 254, 20));

        campoIDCompraN.setEditable(false);
        campoIDCompraN.setBorder(null);
        panelNuevaCompra.add(campoIDCompraN, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 424, 220, 20));

        campoFechaNC.setEditable(false);
        campoFechaNC.setBorder(null);
        panelNuevaCompra.add(campoFechaNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 500, 220, 20));

        CampoTotalNC.setEditable(false);
        CampoTotalNC.setBorder(null);
        panelNuevaCompra.add(CampoTotalNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 574, 210, 20));

        jLabel1.setText("$");
        panelNuevaCompra.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 577, -1, -1));

        btnModiCantProdNC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconEditar.png"))); // NOI18N
        btnModiCantProdNC.setToolTipText("Editar Cantidad");
        btnModiCantProdNC.setBorder(null);
        btnModiCantProdNC.setBorderPainted(false);
        btnModiCantProdNC.setContentAreaFilled(false);
        btnModiCantProdNC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModiCantProdNC.setFocusPainted(false);
        btnModiCantProdNC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiCantProdNCActionPerformed(evt);
            }
        });
        panelNuevaCompra.add(btnModiCantProdNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, -1, -1));

        jPanel10.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdNC = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdNC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Total"
            }
        ));
        tablaProdNC.getTableHeader().setReorderingAllowed(false);
        tablaProdNC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProdNCMouseClicked(evt);
            }
        });
        scrollProdNC.setViewportView(tablaProdNC);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNC, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNC, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNuevaCompra.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 630, 250));

        campoIDPAuxliar.setEditable(false);
        campoIDPAuxliar.setEnabled(false);
        panelNuevaCompra.add(campoIDPAuxliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 70, -1));

        panelAcCanFac.setBackground(new java.awt.Color(249, 251, 242));

        btnAcepNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnAcepNuevaCompra.setToolTipText("Agregar Compra");
        btnAcepNuevaCompra.setBorder(null);
        btnAcepNuevaCompra.setBorderPainted(false);
        btnAcepNuevaCompra.setContentAreaFilled(false);
        btnAcepNuevaCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAcepNuevaCompra.setFocusPainted(false);
        btnAcepNuevaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcepNuevaCompraActionPerformed(evt);
            }
        });

        btnCanNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnXCancelar.png"))); // NOI18N
        btnCanNuevaCompra.setToolTipText("Cancelar Compra");
        btnCanNuevaCompra.setBorder(null);
        btnCanNuevaCompra.setBorderPainted(false);
        btnCanNuevaCompra.setContentAreaFilled(false);
        btnCanNuevaCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanNuevaCompra.setFocusPainted(false);
        btnCanNuevaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanNuevaCompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcCanFacLayout = new javax.swing.GroupLayout(panelAcCanFac);
        panelAcCanFac.setLayout(panelAcCanFacLayout);
        panelAcCanFacLayout.setHorizontalGroup(
            panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanFacLayout.createSequentialGroup()
                .addGroup(panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAcepNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCanNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAcCanFacLayout.setVerticalGroup(
            panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanFacLayout.createSequentialGroup()
                .addComponent(btnAcepNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCanNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNuevaCompra.add(panelAcCanFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 440, 60, 140));

        fondoNuevaCompra.setBackground(new java.awt.Color(255, 255, 255));
        fondoNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevaCompra.png"))); // NOI18N
        panelNuevaCompra.add(fondoNuevaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelProveedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTabProveedores.setBackground(new java.awt.Color(255, 237, 225));

        tablaProveedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Teléfono", "Dirección", "Compras realizadas"
            }
        ));
        tablaProveedores.getTableHeader().setReorderingAllowed(false);
        tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProveedoresMouseClicked(evt);
            }
        });
        scrollProveedores.setViewportView(tablaProveedores);

        javax.swing.GroupLayout panelTabProveedoresLayout = new javax.swing.GroupLayout(panelTabProveedores);
        panelTabProveedores.setLayout(panelTabProveedoresLayout);
        panelTabProveedoresLayout.setHorizontalGroup(
            panelTabProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTabProveedoresLayout.setVerticalGroup(
            panelTabProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelProveedor.add(panelTabProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 640, 220));

        iconBuscarProveedor.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProveedor.setOpaque(true);
        iconBuscarProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProveedorMouseClicked(evt);
            }
        });
        panelProveedor.add(iconBuscarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 151, 22, 22));

        campoIDProvBuscar.setToolTipText("");
        campoIDProvBuscar.setBorder(null);
        campoIDProvBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProvBuscarKeyTyped(evt);
            }
        });
        panelProveedor.add(campoIDProvBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 152, 220, 20));

        comboOrdenarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        comboOrdenarProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nombre" }));
        comboOrdenarProveedor.setBorder(null);
        comboOrdenarProveedor.setPreferredSize(new java.awt.Dimension(200, 25));
        comboOrdenarProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarProveedorItemStateChanged(evt);
            }
        });
        panelProveedor.add(comboOrdenarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 220, 170, -1));

        campoNombreNP.setBorder(null);
        campoNombreNP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNombreNPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoNombreNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 433, 280, 20));

        campoTelNP.setBorder(null);
        campoTelNP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoTelNPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoTelNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 498, 108, 20));

        campoIDProvBuscarM.setToolTipText("");
        campoIDProvBuscarM.setBorder(null);
        campoIDProvBuscarM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProvBuscarMKeyTyped(evt);
            }
        });
        panelProveedor.add(campoIDProvBuscarM, new org.netbeans.lib.awtextra.AbsoluteConstraints(604, 375, 140, 20));

        iconBuscarPM.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarPM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarPM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarPM.setOpaque(true);
        iconBuscarPM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarPMMouseClicked(evt);
            }
        });
        panelProveedor.add(iconBuscarPM, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 370, 30, 30));

        campoNombreMP.setBorder(null);
        campoNombreMP.setOpaque(false);
        campoNombreMP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNombreMPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoNombreMP, new org.netbeans.lib.awtextra.AbsoluteConstraints(602, 441, 140, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/CampoDireccion.png"))); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(158, 30));
        panelProveedor.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 432, 160, 40));

        campoTelMP.setBorder(null);
        campoTelMP.setOpaque(false);
        campoTelMP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoTelMPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoTelMP, new org.netbeans.lib.awtextra.AbsoluteConstraints(604, 505, 108, 20));

        campoDireccMP.setBorder(null);
        campoDireccMP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDireccMPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoDireccMP, new org.netbeans.lib.awtextra.AbsoluteConstraints(759, 441, 140, 20));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/CampoDireccion.png"))); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(158, 30));
        panelProveedor.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 432, 160, 40));

        campoCompReaMP.setEditable(false);
        campoCompReaMP.setBorder(null);
        campoCompReaMP.setOpaque(false);
        panelProveedor.add(campoCompReaMP, new org.netbeans.lib.awtextra.AbsoluteConstraints(747, 504, 135, 20));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAgregarProvNS.png"))); // NOI18N
        btnNuevoProveedor.setBorder(null);
        btnNuevoProveedor.setBorderPainted(false);
        btnNuevoProveedor.setContentAreaFilled(false);
        btnNuevoProveedor.setFocusPainted(false);
        btnNuevoProveedor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAgregarProvS.png"))); // NOI18N
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnNuevoProveedor)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addComponent(btnNuevoProveedor))
        );

        panelProveedor.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 560, 210, 64));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnModifProveedor.setBackground(new java.awt.Color(255, 255, 255));
        btnModifProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModifProvNS.png"))); // NOI18N
        btnModifProveedor.setBorder(null);
        btnModifProveedor.setBorderPainted(false);
        btnModifProveedor.setContentAreaFilled(false);
        btnModifProveedor.setFocusPainted(false);
        btnModifProveedor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModifProvS.png"))); // NOI18N
        btnModifProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnModifProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(btnModifProveedor))
        );

        panelProveedor.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 550, -1, 70));

        campoIDProvMEnc.setEditable(false);
        panelProveedor.add(campoIDProvMEnc, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 380, 70, -1));

        campoDireccNP.setBorder(null);
        campoDireccNP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDireccNPKeyTyped(evt);
            }
        });
        panelProveedor.add(campoDireccNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 498, 140, 20));

        jLabel3.setFont(new java.awt.Font("Roboto Black", 0, 15)); // NOI18N
        jLabel3.setText("Dirección:");
        panelProveedor.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(753, 418, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/CampoDireccion.png"))); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(158, 30));
        panelProveedor.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 489, 160, 40));

        jLabel7.setFont(new java.awt.Font("Roboto Black", 0, 15)); // NOI18N
        jLabel7.setText("Dirección");
        panelProveedor.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 476, -1, -1));

        jPanel11.setBackground(new java.awt.Color(248, 249, 255));

        btnEliminarProv.setBackground(new java.awt.Color(255, 255, 255));
        btnEliminarProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnEliminarNS.png"))); // NOI18N
        btnEliminarProv.setBorder(null);
        btnEliminarProv.setBorderPainted(false);
        btnEliminarProv.setContentAreaFilled(false);
        btnEliminarProv.setFocusPainted(false);
        btnEliminarProv.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnEliminarS.png"))); // NOI18N
        btnEliminarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminarProv)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(btnEliminarProv)
                .addContainerGap())
        );

        panelProveedor.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 269, 170, 58));

        fondoProveedor.setBackground(new java.awt.Color(248, 249, 255));
        fondoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoProveedor.png"))); // NOI18N
        panelProveedor.add(fondoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnProduc.setBackground(new java.awt.Color(248, 249, 255));

        btnModiProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiProductoNS.png"))); // NOI18N
        btnModiProducto.setBorder(null);
        btnModiProducto.setBorderPainted(false);
        btnModiProducto.setContentAreaFilled(false);
        btnModiProducto.setFocusPainted(false);
        btnModiProducto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiProductoS.png"))); // NOI18N
        btnModiProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiProductoActionPerformed(evt);
            }
        });

        btnElimProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimProductoNS.png"))); // NOI18N
        btnElimProducto.setBorder(null);
        btnElimProducto.setBorderPainted(false);
        btnElimProducto.setContentAreaFilled(false);
        btnElimProducto.setFocusPainted(false);
        btnElimProducto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimProductoS.png"))); // NOI18N
        btnElimProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimProductoActionPerformed(evt);
            }
        });

        btnMostrarElim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarElimNS.png"))); // NOI18N
        btnMostrarElim.setBorder(null);
        btnMostrarElim.setBorderPainted(false);
        btnMostrarElim.setContentAreaFilled(false);
        btnMostrarElim.setFocusPainted(false);
        btnMostrarElim.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarElimS.png"))); // NOI18N
        btnMostrarElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarElimActionPerformed(evt);
            }
        });

        btnRegProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnRegProductoNS.png"))); // NOI18N
        btnRegProducto.setBorder(null);
        btnRegProducto.setBorderPainted(false);
        btnRegProducto.setContentAreaFilled(false);
        btnRegProducto.setFocusPainted(false);
        btnRegProducto.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnRegProductoS.png"))); // NOI18N
        btnRegProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnProducLayout = new javax.swing.GroupLayout(panelBtnProduc);
        panelBtnProduc.setLayout(panelBtnProducLayout);
        panelBtnProducLayout.setHorizontalGroup(
            panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnProducLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnRegProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnModiProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnMostrarElim, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btnElimProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBtnProducLayout.setVerticalGroup(
            panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnProducLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnElimProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModiProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarElim)
                    .addComponent(btnRegProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInventario.add(panelBtnProduc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 1000, 70));

        iconBuscarProdInven.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarProdInven.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProdInven.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProdInven.setOpaque(true);
        iconBuscarProdInven.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProdInvenMouseClicked(evt);
            }
        });
        panelInventario.add(iconBuscarProdInven, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 22, 22));

        comboOrdenarInventario.setForeground(new java.awt.Color(255, 255, 255));
        comboOrdenarInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Categoría", "Disponibilidad" }));
        comboOrdenarInventario.setBorder(null);
        comboOrdenarInventario.setPreferredSize(new java.awt.Dimension(200, 25));
        comboOrdenarInventario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarInventarioItemStateChanged(evt);
            }
        });
        panelInventario.add(comboOrdenarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 120, 170, -1));

        panelTablaInventario.setBackground(new java.awt.Color(255, 237, 225));

        tablaInventario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Categoría", "Tamaño", "Precio_Compra", "Precio_Venta", "Disponibilidad", "Stock", "Límite"
            }
        ));
        tablaInventario.setFillsViewportHeight(true);
        tablaInventario.getTableHeader().setReorderingAllowed(false);
        scrollInventario.setViewportView(tablaInventario);

        javax.swing.GroupLayout panelTablaInventarioLayout = new javax.swing.GroupLayout(panelTablaInventario);
        panelTablaInventario.setLayout(panelTablaInventarioLayout);
        panelTablaInventarioLayout.setHorizontalGroup(
            panelTablaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaInventarioLayout.setVerticalGroup(
            panelTablaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaInventarioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        panelInventario.add(panelTablaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 986, 400));

        campoIDProdInventario.setBorder(null);
        campoIDProdInventario.setOpaque(false);
        campoIDProdInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdInventarioKeyTyped(evt);
            }
        });
        panelInventario.add(campoIDProdInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 120, 144, 20));

        fondoInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoInventario.png"))); // NOI18N
        panelInventario.add(fondoInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNuevProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDProvBuscarNP.setBorder(null);
        campoIDProvBuscarNP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProvBuscarNPKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoIDProvBuscarNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 255, 193, 20));

        iconBuscarProvNP.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarProvNP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProvNP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProvNP.setOpaque(true);
        iconBuscarProvNP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProvNPMouseClicked(evt);
            }
        });
        panelNuevProducto.add(iconBuscarProvNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 254, 22, 22));

        campoNomProvNP.setEditable(false);
        campoNomProvNP.setBorder(null);
        panelNuevProducto.add(campoNomProvNP, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 328, 220, 20));

        campoNombreProdN.setBorder(null);
        campoNombreProdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNombreProdNKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoNombreProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 402, 220, 20));

        campoMarcaProdN.setBorder(null);
        campoMarcaProdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMarcaProdNKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoMarcaProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 468, 220, 20));

        comboCatProdN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Artículos de sobre mesa", "Artículos de escritura", "Papeles y etiquetas", "Material de embalaje", "Organización personal", "Manipulados de papel", "Manualidades y bellas artes", "Material escolar especializado" }));
        panelNuevProducto.add(comboCatProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(611, 260, 236, -1));

        campoPCProdN.setBorder(null);
        campoPCProdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPCProdNKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoPCProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 326, 220, 20));

        campoPVProdN.setBorder(null);
        campoPVProdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPVProdNKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoPVProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 395, 220, 20));

        campoLimProdN.setBorder(null);
        campoLimProdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoLimProdNKeyTyped(evt);
            }
        });
        panelNuevProducto.add(campoLimProdN, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 467, 220, 20));

        jPanel5.setBackground(new java.awt.Color(248, 249, 255));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarNS.png"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarS.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(jButton3))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNuevProducto.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 580, 230, 80));

        jPanel4.setBackground(new java.awt.Color(248, 249, 255));

        btnCancelarNP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarNP.setBorder(null);
        btnCancelarNP.setBorderPainted(false);
        btnCancelarNP.setContentAreaFilled(false);
        btnCancelarNP.setFocusPainted(false);
        btnCancelarNP.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarNP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarNPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(btnCancelarNP))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelarNP, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNuevProducto.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 580, 230, 80));

        fondoNuevProducto.setBackground(new java.awt.Color(255, 255, 255));
        fondoNuevProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevoProd.png"))); // NOI18N
        panelNuevProducto.add(fondoNuevProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelModiProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDProdModi.setBorder(null);
        campoIDProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoIDProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 310, 148, 20));

        campoNomProdModi.setBorder(null);
        campoNomProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNomProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoNomProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 154, 226, 20));

        comboCatProdModi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Artículos de sobre mesa", "Artículos de escritura", "Papeles y etiquetas", "Material de embalaje", "Organización personal", "Manipulados de papel", "Manualidades y bellas artes", "Material escolar especializado" }));
        panelModiProducto.add(comboCatProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 230, -1));

        campoPrCProdModi.setBorder(null);
        campoPrCProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPrCProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoPrCProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 367, 226, 20));

        campoPrVProdModi.setBorder(null);
        campoPrVProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPrVProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoPrVProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 445, 226, 20));

        comboDisProdModi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "D", "ND" }));
        panelModiProducto.add(comboDisProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 520, 110, -1));

        campoStockProdModi.setEditable(false);
        campoStockProdModi.setBackground(new java.awt.Color(255, 255, 255));
        campoStockProdModi.setBorder(null);
        panelModiProducto.add(campoStockProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 581, 79, 20));

        campoLimiteProdModi.setBorder(null);
        campoLimiteProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoLimiteProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoLimiteProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 581, 78, 20));

        panelBtnModiProd.setBackground(new java.awt.Color(248, 249, 255));

        btnModiProductoBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiProductoNS.png"))); // NOI18N
        btnModiProductoBD.setBorder(null);
        btnModiProductoBD.setBorderPainted(false);
        btnModiProductoBD.setContentAreaFilled(false);
        btnModiProductoBD.setFocusPainted(false);
        btnModiProductoBD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiProductoS.png"))); // NOI18N
        btnModiProductoBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiProductoBDActionPerformed(evt);
            }
        });

        btnCancelarProdModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarProdModi.setBorder(null);
        btnCancelarProdModi.setBorderPainted(false);
        btnCancelarProdModi.setContentAreaFilled(false);
        btnCancelarProdModi.setFocusPainted(false);
        btnCancelarProdModi.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarProdModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarProdModiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnModiProdLayout = new javax.swing.GroupLayout(panelBtnModiProd);
        panelBtnModiProd.setLayout(panelBtnModiProdLayout);
        panelBtnModiProdLayout.setHorizontalGroup(
            panelBtnModiProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnModiProductoBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelarProdModi, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        panelBtnModiProdLayout.setVerticalGroup(
            panelBtnModiProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnModiProdLayout.createSequentialGroup()
                .addComponent(btnModiProductoBD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btnCancelarProdModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelModiProducto.add(panelBtnModiProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 240, 220, 210));

        panelBtnBuscRecProdModi.setBackground(new java.awt.Color(242, 253, 252));

        btnBuscarProdModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnBuscarProdModi.setToolTipText("Buscar");
        btnBuscarProdModi.setBorder(null);
        btnBuscarProdModi.setBorderPainted(false);
        btnBuscarProdModi.setContentAreaFilled(false);
        btnBuscarProdModi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProdModi.setFocusPainted(false);
        btnBuscarProdModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProdModiActionPerformed(evt);
            }
        });

        btnRecarProdModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconRecargar.png"))); // NOI18N
        btnRecarProdModi.setToolTipText("Limpiar Campos");
        btnRecarProdModi.setBorder(null);
        btnRecarProdModi.setBorderPainted(false);
        btnRecarProdModi.setContentAreaFilled(false);
        btnRecarProdModi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecarProdModi.setFocusPainted(false);
        btnRecarProdModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecarProdModiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnBuscRecProdModiLayout = new javax.swing.GroupLayout(panelBtnBuscRecProdModi);
        panelBtnBuscRecProdModi.setLayout(panelBtnBuscRecProdModiLayout);
        panelBtnBuscRecProdModiLayout.setHorizontalGroup(
            panelBtnBuscRecProdModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnBuscRecProdModiLayout.createSequentialGroup()
                .addComponent(btnRecarProdModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(btnBuscarProdModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBtnBuscRecProdModiLayout.setVerticalGroup(
            panelBtnBuscRecProdModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnBuscRecProdModiLayout.createSequentialGroup()
                .addGroup(panelBtnBuscRecProdModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarProdModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecarProdModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModiProducto.add(panelBtnBuscRecProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 190, 60));

        campoIDProdModiEncon.setEditable(false);
        campoIDProdModiEncon.setEnabled(false);
        panelModiProducto.add(campoIDProdModiEncon, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 40, -1));

        campoMarcaProdModi.setBorder(null);
        campoMarcaProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMarcaProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoMarcaProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 303, 226, 20));

        fondoElimProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoModiProducto.png"))); // NOI18N
        panelModiProducto.add(fondoElimProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelModiProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelElimProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnElimProductoBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimProductoNS.png"))); // NOI18N
        btnElimProductoBD.setBorder(null);
        btnElimProductoBD.setBorderPainted(false);
        btnElimProductoBD.setContentAreaFilled(false);
        btnElimProductoBD.setFocusPainted(false);
        btnElimProductoBD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimProductoS.png"))); // NOI18N
        btnElimProductoBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimProductoBDActionPerformed(evt);
            }
        });
        panelElimProducto.add(btnElimProductoBD, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 580, -1, -1));

        panelBtnCanProdElim.setBackground(new java.awt.Color(248, 249, 255));

        btnCancelarProdElim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarProdElim.setBorder(null);
        btnCancelarProdElim.setBorderPainted(false);
        btnCancelarProdElim.setContentAreaFilled(false);
        btnCancelarProdElim.setFocusPainted(false);
        btnCancelarProdElim.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarProdElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarProdElimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnCanProdElimLayout = new javax.swing.GroupLayout(panelBtnCanProdElim);
        panelBtnCanProdElim.setLayout(panelBtnCanProdElimLayout);
        panelBtnCanProdElimLayout.setHorizontalGroup(
            panelBtnCanProdElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnCanProdElimLayout.createSequentialGroup()
                .addComponent(btnCancelarProdElim)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelBtnCanProdElimLayout.setVerticalGroup(
            panelBtnCanProdElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnCanProdElimLayout.createSequentialGroup()
                .addComponent(btnCancelarProdElim)
                .addGap(0, 21, Short.MAX_VALUE))
        );

        panelElimProducto.add(panelBtnCanProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 560, 210, 80));

        jPanel1.setBackground(new java.awt.Color(248, 249, 255));

        btnElimProdCant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnEliminarNS.png"))); // NOI18N
        btnElimProdCant.setBorder(null);
        btnElimProdCant.setBorderPainted(false);
        btnElimProdCant.setContentAreaFilled(false);
        btnElimProdCant.setFocusPainted(false);
        btnElimProdCant.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnEliminarS.png"))); // NOI18N
        btnElimProdCant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimProdCantActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnElimProdCant, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(btnElimProdCant, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelElimProducto.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 290, 150, 70));

        campoIDProdElim.setBorder(null);
        campoIDProdElim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdElimKeyTyped(evt);
            }
        });
        panelElimProducto.add(campoIDProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 310, 144, 20));

        campoNomProdElim.setEditable(false);
        campoNomProdElim.setBorder(null);
        campoNomProdElim.setOpaque(false);
        panelElimProducto.add(campoNomProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 154, 225, 20));

        campoCatProdElim.setEditable(false);
        campoCatProdElim.setBorder(null);
        campoCatProdElim.setOpaque(false);
        panelElimProducto.add(campoCatProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 224, 225, 20));

        campoMarcaProdElim.setEditable(false);
        campoMarcaProdElim.setBorder(null);
        campoMarcaProdElim.setOpaque(false);
        panelElimProducto.add(campoMarcaProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 290, 225, 30));

        campoPrecioCProdElim.setEditable(false);
        campoPrecioCProdElim.setBorder(null);
        campoPrecioCProdElim.setOpaque(false);
        panelElimProducto.add(campoPrecioCProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 367, 225, 20));

        campoPrecioVProdElim.setEditable(false);
        campoPrecioVProdElim.setBorder(null);
        campoPrecioVProdElim.setOpaque(false);
        panelElimProducto.add(campoPrecioVProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 440, 225, 30));

        campoDispProdElim.setEditable(false);
        campoDispProdElim.setBorder(null);
        campoDispProdElim.setOpaque(false);
        panelElimProducto.add(campoDispProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 517, 225, 20));

        campoStockProdElim.setEditable(false);
        campoStockProdElim.setBorder(null);
        campoStockProdElim.setOpaque(false);
        panelElimProducto.add(campoStockProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 581, 225, 20));

        campoCantElimProdElim.setBorder(null);
        campoCantElimProdElim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoCantElimProdElimKeyTyped(evt);
            }
        });
        panelElimProducto.add(campoCantElimProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 154, 210, 21));

        panelBtnBuscRecProdElim.setBackground(new java.awt.Color(242, 253, 252));

        btnBuscarProdElim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnBuscarProdElim.setToolTipText("Buscar");
        btnBuscarProdElim.setBorder(null);
        btnBuscarProdElim.setBorderPainted(false);
        btnBuscarProdElim.setContentAreaFilled(false);
        btnBuscarProdElim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProdElim.setFocusPainted(false);
        btnBuscarProdElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProdElimActionPerformed(evt);
            }
        });

        btnRecarProdElim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconRecargar.png"))); // NOI18N
        btnRecarProdElim.setToolTipText("Limpiar Campos");
        btnRecarProdElim.setBorder(null);
        btnRecarProdElim.setBorderPainted(false);
        btnRecarProdElim.setContentAreaFilled(false);
        btnRecarProdElim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecarProdElim.setFocusPainted(false);
        btnRecarProdElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecarProdElimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnBuscRecProdElimLayout = new javax.swing.GroupLayout(panelBtnBuscRecProdElim);
        panelBtnBuscRecProdElim.setLayout(panelBtnBuscRecProdElimLayout);
        panelBtnBuscRecProdElimLayout.setHorizontalGroup(
            panelBtnBuscRecProdElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnBuscRecProdElimLayout.createSequentialGroup()
                .addComponent(btnRecarProdElim, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(btnBuscarProdElim, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBtnBuscRecProdElimLayout.setVerticalGroup(
            panelBtnBuscRecProdElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnBuscRecProdElimLayout.createSequentialGroup()
                .addGroup(panelBtnBuscRecProdElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarProdElim, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecarProdElim, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelElimProducto.add(panelBtnBuscRecProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, 190, 60));

        campoIDProElimEnc.setEditable(false);
        panelElimProducto.add(campoIDProElimEnc, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 70, -1));

        comboMotElimProd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Producto descontinuado", "Producto dañado" }));
        panelElimProducto.add(comboMotElimProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 220, 233, -1));

        fondoModiProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoElimProducto.png"))); // NOI18N
        panelElimProducto.add(fondoModiProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelElimProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelRegistroElim.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnAcepElim.setBackground(new java.awt.Color(248, 249, 255));

        btnAceptarRegElim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarNS.png"))); // NOI18N
        btnAceptarRegElim.setBorder(null);
        btnAceptarRegElim.setBorderPainted(false);
        btnAceptarRegElim.setContentAreaFilled(false);
        btnAceptarRegElim.setFocusPainted(false);
        btnAceptarRegElim.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnAceptarS.png"))); // NOI18N
        btnAceptarRegElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarRegElimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnAcepElimLayout = new javax.swing.GroupLayout(panelBtnAcepElim);
        panelBtnAcepElim.setLayout(panelBtnAcepElimLayout);
        panelBtnAcepElimLayout.setHorizontalGroup(
            panelBtnAcepElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnAcepElimLayout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(btnAceptarRegElim, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(396, Short.MAX_VALUE))
        );
        panelBtnAcepElimLayout.setVerticalGroup(
            panelBtnAcepElimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnAcepElimLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAceptarRegElim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelRegistroElim.add(panelBtnAcepElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 1000, 70));

        iconBuscarProdEliminado.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarProdEliminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProdEliminado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProdEliminado.setOpaque(true);
        iconBuscarProdEliminado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProdEliminadoMouseClicked(evt);
            }
        });
        panelRegistroElim.add(iconBuscarProdEliminado, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 22, 22));

        comboOrdenarElim.setForeground(new java.awt.Color(255, 255, 255));
        comboOrdenarElim.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Fecha descendente", "Fecha ascendente" }));
        comboOrdenarElim.setBorder(null);
        comboOrdenarElim.setPreferredSize(new java.awt.Dimension(200, 25));
        comboOrdenarElim.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarElimItemStateChanged(evt);
            }
        });
        panelRegistroElim.add(comboOrdenarElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 120, 170, -1));

        panelTablaEliminados.setBackground(new java.awt.Color(255, 237, 225));

        tablaEliminados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaEliminados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Cantidad", "Motivo", "Fecha"
            }
        ));
        tablaEliminados.setFillsViewportHeight(true);
        tablaEliminados.getTableHeader().setReorderingAllowed(false);
        scrollEliminados.setViewportView(tablaEliminados);

        javax.swing.GroupLayout panelTablaEliminadosLayout = new javax.swing.GroupLayout(panelTablaEliminados);
        panelTablaEliminados.setLayout(panelTablaEliminadosLayout);
        panelTablaEliminadosLayout.setHorizontalGroup(
            panelTablaEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaEliminadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollEliminados, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaEliminadosLayout.setVerticalGroup(
            panelTablaEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaEliminadosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollEliminados, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        panelRegistroElim.add(panelTablaEliminados, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 986, 400));

        campoIDProdIEliminado.setBorder(null);
        campoIDProdIEliminado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdIEliminadoKeyTyped(evt);
            }
        });
        panelRegistroElim.add(campoIDProdIEliminado, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 120, 144, 20));

        fondoInventario1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoRegistroElim.png"))); // NOI18N
        panelRegistroElim.add(fondoInventario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelRegistroElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelCompras.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 237, 225));

        tablaCompras = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Fecha", "Cantidad Productos", "Precio Total"
            }
        ));
        tablaCompras.getTableHeader().setReorderingAllowed(false);
        tablaCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaComprasMouseClicked(evt);
            }
        });
        scrollCompras.setViewportView(tablaCompras);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollCompras, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollCompras, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelCompras.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 90, 660, 230));

        jPanel7.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdCompra = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario"
            }
        ));
        tablaProdCompra.getTableHeader().setReorderingAllowed(false);
        tablaProdCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProdCompraMouseClicked(evt);
            }
        });
        scrollProdCompra.setViewportView(tablaProdCompra);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelCompras.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 650, 240));

        btnMostrarCompra.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecNS.png"))); // NOI18N
        btnMostrarCompra.setBorder(null);
        btnMostrarCompra.setBorderPainted(false);
        btnMostrarCompra.setContentAreaFilled(false);
        btnMostrarCompra.setFocusPainted(false);
        btnMostrarCompra.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecS.png"))); // NOI18N
        btnMostrarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarCompraActionPerformed(evt);
            }
        });
        panelCompras.add(btnMostrarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 218, -1, 70));

        campoIDCompraBuscar.setBorder(null);
        campoIDCompraBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDCompraBuscarKeyTyped(evt);
            }
        });
        panelCompras.add(campoIDCompraBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 127, 220, 20));

        iconBuscarCompra.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarCompra.setOpaque(true);
        iconBuscarCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarCompraMouseClicked(evt);
            }
        });
        panelCompras.add(iconBuscarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(964, 127, 22, 22));

        campoNomProvProdSelec.setEditable(false);
        campoNomProvProdSelec.setBorder(null);
        panelCompras.add(campoNomProvProdSelec, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 420, 180, 20));

        campoTelProvProdSelec.setEditable(false);
        campoTelProvProdSelec.setBorder(null);
        panelCompras.add(campoTelProvProdSelec, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 485, 180, 20));

        campoComProvProdSelec.setEditable(false);
        campoComProvProdSelec.setBorder(null);
        panelCompras.add(campoComProvProdSelec, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 549, 180, 20));

        comboOrdenarCompras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Fecha ascendente", "Fecha descendente" }));
        comboOrdenarCompras.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarComprasItemStateChanged(evt);
            }
        });
        panelCompras.add(comboOrdenarCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(724, 180, 180, 25));

        fondoRegCompras.setBackground(new java.awt.Color(255, 255, 255));
        fondoRegCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoCompras.png"))); // NOI18N
        panelCompras.add(fondoRegCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNotificaciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTablaAlertas.setBackground(new java.awt.Color(255, 237, 225));

        tablaAlertas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaAlertas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Aviso", "Fecha", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaAlertas.getTableHeader().setReorderingAllowed(false);
        tablaAlertas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAlertasMouseClicked(evt);
            }
        });
        scrollAlertas.setViewportView(tablaAlertas);
        if (tablaAlertas.getColumnModel().getColumnCount() > 0) {
            tablaAlertas.getColumnModel().getColumn(1).setMinWidth(200);
            tablaAlertas.getColumnModel().getColumn(1).setPreferredWidth(200);
            tablaAlertas.getColumnModel().getColumn(1).setMaxWidth(200);
        }

        javax.swing.GroupLayout panelTablaAlertasLayout = new javax.swing.GroupLayout(panelTablaAlertas);
        panelTablaAlertas.setLayout(panelTablaAlertasLayout);
        panelTablaAlertasLayout.setHorizontalGroup(
            panelTablaAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaAlertasLayout.setVerticalGroup(
            panelTablaAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNotificaciones.add(panelTablaAlertas, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 175, 455, 370));

        jPanel8.setBackground(new java.awt.Color(248, 249, 255));

        btnGenReporteAlerta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnGenReporteNS.png"))); // NOI18N
        btnGenReporteAlerta.setBorder(null);
        btnGenReporteAlerta.setBorderPainted(false);
        btnGenReporteAlerta.setContentAreaFilled(false);
        btnGenReporteAlerta.setFocusPainted(false);
        btnGenReporteAlerta.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnGenReporteS.png"))); // NOI18N
        btnGenReporteAlerta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenReporteAlertaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(356, Short.MAX_VALUE)
                .addComponent(btnGenReporteAlerta)
                .addGap(354, 354, 354))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addComponent(btnGenReporteAlerta))
        );

        panelNotificaciones.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 570, 860, 80));

        jPanel12.setBackground(new java.awt.Color(253, 238, 229));

        jLabel8.setBackground(new java.awt.Color(253, 238, 229));
        jLabel8.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jLabel8.setText("Pedidos adicionales de productos:");
        jLabel8.setToolTipText("");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNotificaciones.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, 300, 20));

        panelTablaNotificaciones.setBackground(new java.awt.Color(255, 237, 225));

        tablaNotificacionC = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaNotificacionC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Mensaje", "Fecha", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaNotificacionC.getTableHeader().setReorderingAllowed(false);
        tablaNotificacionC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaNotificacionCMouseClicked(evt);
            }
        });
        scrollNotificacionC.setViewportView(tablaNotificacionC);

        javax.swing.GroupLayout panelTablaNotificacionesLayout = new javax.swing.GroupLayout(panelTablaNotificaciones);
        panelTablaNotificaciones.setLayout(panelTablaNotificacionesLayout);
        panelTablaNotificacionesLayout.setHorizontalGroup(
            panelTablaNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollNotificacionC, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaNotificacionesLayout.setVerticalGroup(
            panelTablaNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollNotificacionC, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNotificaciones.add(panelTablaNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 170, 460, 380));

        fondoNotificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNotificaciones.png"))); // NOI18N
        panelNotificaciones.add(fondoNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        fondoCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/Fondo.png"))); // NOI18N
        panelRaiz.add(fondoCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        headerCompras.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerComprasMouseDragged(evt);
            }
        });
        headerCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerComprasMousePressed(evt);
            }
        });

        javax.swing.GroupLayout headerComprasLayout = new javax.swing.GroupLayout(headerCompras);
        headerCompras.setLayout(headerComprasLayout);
        headerComprasLayout.setHorizontalGroup(
            headerComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        headerComprasLayout.setVerticalGroup(
            headerComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelRaiz.add(headerCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 40));

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

    private void btnNuevaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaCompraActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelPedidos.setVisible(true);
        panelPedidos.setEnabled(true);
        
        //se vuelve a asignar el constructor para que tenga los datos actualizados
        controladorPedidos = new ControladorPedidos();
        tablaPedidos.setModel(controladorPedidos.getModelo());
    }//GEN-LAST:event_btnNuevaCompraActionPerformed

    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelProveedor.setVisible(true);
        panelProveedor.setEnabled(true);
        //se vuelve a asignar el constructor para que tenga los datos actualizados
        controladorProveedor = new ControladorProveedores("ID");
        tablaProveedores.setModel(controladorProveedor.getModelo());
    }//GEN-LAST:event_btnProveedorActionPerformed

    private void btnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelInventario.setVisible(true);
        panelInventario.setEnabled(true);
        //se vuelve a asignar el constructor para que tenga los datos actualizados
        controladorProductos = new ControladorProductos("ID");
        //controladorProductos.actualizarModelo();
        tablaInventario.setModel(controladorProductos.getModelo());
    }//GEN-LAST:event_btnInventarioActionPerformed

    private void btnComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprasActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelCompras.setVisible(true);
        panelCompras.setEnabled(true);
        
        controladorCompras = new ControladorCompras("ID");
        tablaCompras.setModel(controladorCompras.getModelo());

        boolean seleccionado = false;
        for(int i = 0;i < tablaCompras.getRowCount(); i++){
            if(tablaCompras.isRowSelected(i))
                seleccionado = true;
        }
        if(seleccionado != false)
            tablaCompras.changeSelection(renglonCompraSelec, columnCompraSelec, true, false);
        
        int row = tablaProdCompra.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdCompra.getModel();
            modeloProd.setRowCount(0);
        }
        
        this.campoNomProvProdSelec.setText("");
        this.campoTelProvProdSelec.setText("");
        this.campoComProvProdSelec.setText("");
    }//GEN-LAST:event_btnComprasActionPerformed

    private void btnNotificacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificacionesActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelNotificaciones.setVisible(true);
        panelNotificaciones.setEnabled(true);

        controladorAlertas = new ControladorAlerta(0);
        tablaAlertas.setModel(controladorAlertas.getModelo());
        
        controladorAlertaPedido = new ControladorAlertaPedido(0);
        tablaNotificacionC.setModel(controladorAlertaPedido.getModelo());
    }//GEN-LAST:event_btnNotificacionesActionPerformed

    private void btnModiProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiProductoActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelModiProducto.setVisible(true);
        panelModiProducto.setEnabled(true);
        this.campoIDProdModi.setText("");
        this.campoNomProdModi.setText("");
        this.comboCatProdModi.setSelectedIndex(0);
        this.campoMarcaProdModi.setText("");
        this.campoPrCProdModi.setText("");
        this.campoPrVProdModi.setText("");
        this.comboDisProdModi.setSelectedIndex(0);
        this.campoStockProdModi.setText("");
        this.campoLimiteProdModi.setText("");
    }//GEN-LAST:event_btnModiProductoActionPerformed

    private void btnElimProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimProductoActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelElimProducto.setVisible(true);
        panelElimProducto.setEnabled(true);
        this.campoIDProdElim.setText("");
        this.campoNomProdElim.setText("");
        this.campoCatProdElim.setText("");
        this.campoMarcaProdElim.setText("");
        this.campoPrecioCProdElim.setText("");
        this.campoPrecioVProdElim.setText("");
        this.campoDispProdElim.setText("");
        this.campoStockProdElim.setText("");
        this.campoCantElimProdElim.setText("");
    }//GEN-LAST:event_btnElimProductoActionPerformed

    private void btnMostrarElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarElimActionPerformed
        ocultarTodo();
        panelRegistroElim.setVisible(true);
        panelRegistroElim.setEnabled(true);
        controladorPEliminados = new ControladorProducElim("ID");
        tablaEliminados.setModel(controladorPEliminados.getModelo());
    }//GEN-LAST:event_btnMostrarElimActionPerformed

    private void iconBuscarProdInvenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProdInvenMouseClicked
        if(!(this.campoIDProdInventario.getText().equals(""))){
            controladorProductos = new ControladorProductos("ID");
            if(controladorProductos.buscarProductoAct(this.campoIDProdInventario.getText()))
            tablaInventario.setModel(controladorProductos.getModelo());
            else{
                String m = "Producto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProdInventario.setText("");
            }

        }else{
            controladorProductos = new ControladorProductos("ID");
            tablaInventario.setModel(controladorProductos.getModelo());
        }
    }//GEN-LAST:event_iconBuscarProdInvenMouseClicked

    private void comboOrdenarInventarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarInventarioItemStateChanged
        if(comboOrdenarInventario.getSelectedItem().equals("ID")){
            controladorProductos = new ControladorProductos("ID");
            tablaInventario.setModel(controladorProductos.getModelo());
        }
        if(comboOrdenarInventario.getSelectedItem().equals("Nombre")){
            controladorProductos = new ControladorProductos("Nombre");
            tablaInventario.setModel(controladorProductos.getModelo());
        }
        if(comboOrdenarInventario.getSelectedItem().equals("Categoría")){
            controladorProductos = new ControladorProductos("Cat");
            tablaInventario.setModel(controladorProductos.getModelo());
        }
        if(comboOrdenarInventario.getSelectedItem().equals("Disponibilidad")){
            controladorProductos = new ControladorProductos("Disp");
            tablaInventario.setModel(controladorProductos.getModelo());
        }
    }//GEN-LAST:event_comboOrdenarInventarioItemStateChanged

    private void campoIDProdInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdInventarioKeyTyped
        if(this.campoIDProdInventario.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProdInventarioKeyTyped

    private void campoIDProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdModiKeyTyped
        if(this.campoIDProdModi.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProdModiKeyTyped

    private void campoPrCProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPrCProdModiKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoPrCProdModi.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoPrCProdModiKeyTyped

    private void campoPrVProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPrVProdModiKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoPrVProdModi.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoPrVProdModiKeyTyped

    private void btnModiProductoBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiProductoBDActionPerformed
        controladorProductos = new ControladorProductos("ID");
        String nombre = this.campoNomProdModi.getText();
        String categoria = this.comboCatProdModi.getSelectedItem().toString();
        String marca = this.campoMarcaProdModi.getText();
        String precioc = this.campoPrCProdModi.getText();
        String preciov = this.campoPrVProdModi.getText();
        String disp = this.comboDisProdModi.getSelectedItem().toString();
        String limite = this.campoLimiteProdModi.getText();

        if(!(this.campoIDProdModiEncon.getText().equals(""))){
            if((nombre.equals("")) || (marca.equals("")) || (categoria.equals("")) || (precioc.equals("")) || (preciov.equals(""))
                || (disp.equals("")) || (limite.equals(""))){
                String m = "Hay campos vacíos.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
            else{
                controladorProductos.setProductoActual(controladorProductos.buscarProductoPorID(this.campoIDProdModiEncon.getText()));
                Productos producto = controladorProductos.getProductoActual();
                producto.setNombre(nombre);
                producto.setCat_producto(categoria);
                producto.setMarca(marca);
                producto.setPrecioC(Float.parseFloat(precioc));
                producto.setPrecioV(Float.parseFloat(preciov));
                producto.setDisponibilidad(disp);
                producto.setLimite(Integer.parseInt(limite));
                if(controladorProductos.actualizaProducto()){
                    controladorProductos = new ControladorProductos("ID");
                    tablaInventario.setModel(controladorProductos.getModelo());
                    
                    String m = "Modificación realizada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    ocultarTodo();
                    panelInventario.setEnabled(true);
                    panelInventario.setVisible(true);
                }
            }
        }

    }//GEN-LAST:event_btnModiProductoBDActionPerformed

    private void btnCancelarProdModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarProdModiActionPerformed
        this.campoIDProdModi.setText("");
        this.campoNomProdModi.setText("");
        this.comboCatProdModi.setSelectedIndex(0);
        this.campoMarcaProdModi.setText("");
        this.campoPrCProdModi.setText("");
        this.campoPrVProdModi.setText("");
        this.comboDisProdModi.setSelectedIndex(0);
        this.campoStockProdModi.setText("");
        this.campoLimiteProdModi.setText("");
        this.campoIDProdModiEncon.setText("");
        ocultarTodo();
        panelInventario.setEnabled(true);
        panelInventario.setVisible(true);
    }//GEN-LAST:event_btnCancelarProdModiActionPerformed

    private void btnBuscarProdModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProdModiActionPerformed
        if(!(this.campoIDProdModi.getText().equals(""))){
            controladorProductos = new ControladorProductos("ID");
            Productos producto = controladorProductos.buscarProductoPorID(this.campoIDProdModi.getText());
            if(producto != null){
                this.campoNomProdModi.setText(producto.getNombre());
                this.comboCatProdModi.setSelectedItem(producto.getCat_producto());
                this.campoMarcaProdModi.setText(producto.getMarca());
                this.campoPrCProdModi.setText(""+producto.getPrecioC());
                this.campoPrVProdModi.setText(""+producto.getPrecioV());
                this.comboDisProdModi.setSelectedItem(producto.getDisponibilidad());
                this.campoStockProdModi.setText(""+producto.getStock());
                this.campoLimiteProdModi.setText(""+producto.getLimite());
                this.campoIDProdModiEncon.setText(""+producto.getId_producto());
            }else{
                String m = "Producto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProdModi.setText("");
            }
        }
    }//GEN-LAST:event_btnBuscarProdModiActionPerformed

    private void btnRecarProdModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecarProdModiActionPerformed
        this.campoIDProdModi.setText("");
        this.campoNomProdModi.setText("");
        this.comboCatProdModi.setSelectedIndex(0);
        this.campoMarcaProdModi.setText("");
        this.campoPrCProdModi.setText("");
        this.campoPrVProdModi.setText("");
        this.comboDisProdModi.setSelectedIndex(0);
        this.campoStockProdModi.setText("");
        this.campoLimiteProdModi.setText("");
        this.campoIDProdModiEncon.setText("");
    }//GEN-LAST:event_btnRecarProdModiActionPerformed

    private void btnElimProductoBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimProductoBDActionPerformed
        if(!(this.campoIDProElimEnc.getText().equals(""))){
            if(Integer.parseInt(this.campoStockProdElim.getText()) == 0){
                AlertaConfirm pregunta = new AlertaConfirm(this, true);
                pregunta.mensaje.setText("¿Desea eliminar?.");
                pregunta.setVisible(true);

                boolean respuesta = pregunta.respuesta;
                
                if(respuesta){
                    controladorProductos.eliminarProducto(Integer.parseInt(this.campoIDProElimEnc.getText()));
                    controladorProductos = new ControladorProductos("ID");
                    tablaInventario.setModel(controladorProductos.getModelo());
                    
                    String m = "Eliminación realizada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    ocultarTodo();
                    panelInventario.setEnabled(true);
                    panelInventario.setVisible(true);
                }
            }
            else{
                String m = "Hay productos en Stock.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }else{
            String m = "Primero busque el producto.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnElimProductoBDActionPerformed

    private void btnCancelarProdElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarProdElimActionPerformed
        this.campoIDProdElim.setText("");
        this.campoIDProElimEnc.setText("");
        this.campoNomProdElim.setText("");
        this.campoCatProdElim.setText("");
        this.campoMarcaProdElim.setText("");
        this.campoPrecioCProdElim.setText("");
        this.campoPrecioVProdElim.setText("");
        this.campoDispProdElim.setText("");
        this.campoStockProdElim.setText("");
        this.campoCantElimProdElim.setText("");
        ocultarTodo();
        panelInventario.setEnabled(true);
        panelInventario.setVisible(true);
    }//GEN-LAST:event_btnCancelarProdElimActionPerformed

    private void btnElimProdCantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimProdCantActionPerformed
        if(!(this.campoIDProElimEnc.getText().equals(""))){
            int cantidad = 0;
            if(!(campoCantElimProdElim.getText().equals(""))){
                cantidad = Integer.parseInt(campoCantElimProdElim.getText());
                if(cantidad > 0){
                    controladorProductos.setProductoActual(controladorProductos.buscarProductoPorID(this.campoIDProElimEnc.getText()));
                    Productos producto = controladorProductos.getProductoActual();
                    if(producto.getStock() >= cantidad){
                        producto.setStock(producto.getStock()-cantidad);
                        if(controladorProductos.actualizaProducto()){
                            controladorProductos.actualizarModelo();
                            tablaInventario.setModel(controladorProductos.getModelo());
                            //se registra la eliminacion
                            ProductosElim productoElim = new ProductosElim();
                            productoElim.setId_producto(Integer.parseInt(this.campoIDProElimEnc.getText()));
                            productoElim.setNombre(this.campoNomProdElim.getText());
                            productoElim.setCantidad(cantidad);
                            productoElim.setMotivo(this.comboMotElimProd.getSelectedItem().toString());
                            Calendar calendario = Calendar.getInstance();
                            String dia = "" + calendario.get(Calendar.DATE);
                            String mes = "" + (calendario.get(Calendar.MONTH)+1);
                            String anio = "" + calendario.get(Calendar.YEAR);
                            productoElim.setFecha(anio+"-"+mes+"-"+dia);

                            controladorPEliminados.agregarEliminacion(productoElim);
                            
                            String m = "Eliminación realizada.";
                            AlertaWarning salir = new AlertaWarning(this, true);
                            salir.mensaje.setText(m);
                            salir.setVisible(true);
                            
                            controladorProductos.actualizarDisponibiilidad();
                            
                            ocultarTodo();
                            panelInventario.setEnabled(true);
                            panelInventario.setVisible(true);
                        }
                    }else{
                        String m = "Cantidad mayor al Stock.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                    }
                }else{
                    String m = "Cantidad inválida.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
            }else{
                String m = "Cantidad no ingresada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnElimProdCantActionPerformed

    private void campoCantElimProdElimKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoCantElimProdElimKeyTyped
        if(this.campoCantElimProdElim.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoCantElimProdElimKeyTyped

    private void btnBuscarProdElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProdElimActionPerformed
        if(!(this.campoIDProdElim.getText().equals(""))){
            controladorProductos = new ControladorProductos("ID");
            Productos producto = controladorProductos.buscarProductoPorID(this.campoIDProdElim.getText());
            if(producto != null){
                this.campoIDProElimEnc.setText(""+producto.getId_producto());
                this.campoNomProdElim.setText(producto.getNombre());
                this.campoCatProdElim.setText(producto.getCat_producto());
                this.campoMarcaProdElim.setText(producto.getMarca());
                this.campoPrecioCProdElim.setText(""+producto.getPrecioC());
                this.campoPrecioVProdElim.setText(""+producto.getPrecioV());
                this.campoDispProdElim.setText(producto.getDisponibilidad());
                this.campoStockProdElim.setText(""+producto.getStock());
            }else{
                String m = "Producto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProdElim.setText("");
            }
        }
    }//GEN-LAST:event_btnBuscarProdElimActionPerformed

    private void btnRecarProdElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecarProdElimActionPerformed
        this.campoIDProdElim.setText("");
        this.campoIDProElimEnc.setText("");
        this.campoNomProdElim.setText("");
        this.campoCatProdElim.setText("");
        this.campoMarcaProdElim.setText("");
        this.campoPrecioCProdElim.setText("");
        this.campoPrecioVProdElim.setText("");
        this.campoDispProdElim.setText("");
        this.campoStockProdElim.setText("");
        this.campoCantElimProdElim.setText("");
    }//GEN-LAST:event_btnRecarProdElimActionPerformed

    private void btnAceptarRegElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarRegElimActionPerformed
        ocultarTodo();
        panelInventario.setVisible(true);
        panelInventario.setEnabled(true);
    }//GEN-LAST:event_btnAceptarRegElimActionPerformed

    private void iconBuscarProdEliminadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProdEliminadoMouseClicked
        if(!(this.campoIDProdIEliminado.getText().equals(""))){
            controladorPEliminados = new ControladorProducElim("ID");
            if(controladorPEliminados.buscarProductoAct(this.campoIDProdIEliminado.getText()))
                tablaEliminados.setModel(controladorPEliminados.getModelo());
            else{
                String m = "Producto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProdIEliminado.setText("");
            }

        }else{
            controladorPEliminados = new ControladorProducElim("ID");
            tablaEliminados.setModel(controladorPEliminados.getModelo());
        }
    }//GEN-LAST:event_iconBuscarProdEliminadoMouseClicked

    private void comboOrdenarElimItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarElimItemStateChanged
        if(comboOrdenarElim.getSelectedItem().equals("ID")){
            controladorPEliminados = new ControladorProducElim("ID");
            tablaEliminados.setModel(controladorPEliminados.getModelo());
        }
        if(comboOrdenarElim.getSelectedItem().equals("Nombre")){
            controladorPEliminados = new ControladorProducElim("Nombre");
            tablaEliminados.setModel(controladorPEliminados.getModelo());
        }
        if(comboOrdenarElim.getSelectedItem().equals("Fecha descendente")){
            controladorPEliminados = new ControladorProducElim("FechaDESC");
            tablaEliminados.setModel(controladorPEliminados.getModelo());
        }
        if(comboOrdenarElim.getSelectedItem().equals("Fecha ascendente")){
            controladorPEliminados = new ControladorProducElim("FechaASC");
            tablaEliminados.setModel(controladorPEliminados.getModelo());
        }
    }//GEN-LAST:event_comboOrdenarElimItemStateChanged

    private void campoIDProdIEliminadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdIEliminadoKeyTyped
        if(this.campoIDProdIEliminado.getText().length() >= 10)
           evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProdIEliminadoKeyTyped

    private void headerComprasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerComprasMouseDragged
        //se asignan las coordenadas del mouse en pantalla
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        //se restan los valores para obtener el resultado de mover la ventana
        this.setLocation(x - xMouse,y - yMouse);
    }//GEN-LAST:event_headerComprasMouseDragged

    private void headerComprasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerComprasMousePressed
        //se asignan las coordenadas (del mouse en la ventana)
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerComprasMousePressed

    private void tablaAlertasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAlertasMouseClicked
        if(tablaAlertas.getRowCount() != 0){
            renglonAlerta = tablaAlertas.rowAtPoint(evt.getPoint());
            columnAlerta = tablaAlertas.columnAtPoint(evt.getPoint());

            //se realiza el evento de selecciona de renglones (marcar las casillas de cada fila)
            if (tablaAlertas.getValueAt(renglonAlerta, 3).equals(true))
            tablaAlertas.setValueAt(false, renglonAlerta, 3);
            else
            tablaAlertas.setValueAt(true, renglonAlerta, 3);
        }
    }//GEN-LAST:event_tablaAlertasMouseClicked

    private void btnCrearCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearCompraActionPerformed
        ocultarTodo();
        panelNuevaCompra.setVisible(true);
        panelNuevaCompra.setEnabled(true);
        
        Calendar calendario = Calendar.getInstance();
        String dia = "" + calendario.get(Calendar.DATE);
        String mes = "" + (calendario.get(Calendar.MONTH)+1);
        String anio = "" + calendario.get(Calendar.YEAR);
        this.campoFechaNC.setText(anio+"-"+mes+"-"+dia);
        
        ControladorMetodos controladorMetodos = new ControladorMetodos();
        int id = controladorMetodos.ultimaCompra() + 2;
        this.campoIDCompraN.setText(String.valueOf(id));
        
        int row = tablaProdNC.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNC.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }
        
        this.campoIDPedidoBuscar.setText("");
        this.campoIDPAuxliar.setText("");
        this.CampoTotalNC.setText("");
        this.campoIDProvNC.setText("");
        this.campoNomProvNC.setText("");
        this.campoCantProdNC.setText("");
    }//GEN-LAST:event_btnCrearCompraActionPerformed

    private void btnRegProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegProductoActionPerformed
        ocultarTodo();
        panelNuevProducto.setVisible(true);
        panelNuevProducto.setEnabled(true);
        this.campoIDProvBuscarNP.setText("");
        this.campoNomProvNP.setText("");
        this.campoNombreProdN.setText("");
        this.campoMarcaProdN.setText("");
        this.campoPCProdN.setText("");
        this.campoPVProdN.setText("");
        this.campoLimProdN.setText("");
    }//GEN-LAST:event_btnRegProductoActionPerformed

    private void iconBuscarProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProveedorMouseClicked
        if(!(this.campoIDProvBuscar.getText().equals(""))){
            controladorProveedor = new ControladorProveedores("ID");
            if(controladorProveedor.buscarProveedorAct(this.campoIDProvBuscar.getText()))
                tablaProveedores.setModel(controladorProveedor.getModelo());
            else{
                String m = "Proveedor no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProvBuscar.setText("");
            }

        }else{
            controladorProveedor = new ControladorProveedores("ID");
            tablaProveedores.setModel(controladorProveedor.getModelo());
        }
    }//GEN-LAST:event_iconBuscarProveedorMouseClicked

    private void comboOrdenarProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarProveedorItemStateChanged
        if(comboOrdenarProveedor.getSelectedItem().equals("Código")){
            controladorProveedor = new ControladorProveedores("ID");
            tablaProveedores.setModel(controladorProveedor.getModelo());
        }
        if(comboOrdenarProveedor.getSelectedItem().equals("Nombre")){
            controladorProveedor = new ControladorProveedores("Nombre");
            tablaProveedores.setModel(controladorProveedor.getModelo());
        }
    }//GEN-LAST:event_comboOrdenarProveedorItemStateChanged

    private void iconBuscarPMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarPMMouseClicked
        if(!(this.campoIDProvBuscarM.getText().equals(""))){
            controladorProveedor = new ControladorProveedores("ID");
            Proveedor proveedor = controladorProveedor.buscarProveedorPorID(this.campoIDProvBuscarM.getText());
            if(proveedor != null){
                this.campoIDProvMEnc.setText(""+proveedor.getId_proveedor());
                this.campoNombreMP.setText(proveedor.getNombre());
                this.campoTelMP.setText(proveedor.getTelefono());
                this.campoDireccMP.setText(proveedor.getDireccion());
                this.campoCompReaMP.setText(""+proveedor.getNum_pedidos());
            }
            else{
                String m = "Proveedor no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProvBuscarM.setText("");
                this.campoIDProvMEnc.setText("");
                this.campoNombreMP.setText("");
                this.campoDireccMP.setText("");
                this.campoTelMP.setText("");
                this.campoCompReaMP.setText("");
            }
        }
        else{
            this.campoIDProvMEnc.setText("");
            this.campoNombreMP.setText("");
            this.campoTelMP.setText("");
            this.campoCompReaMP.setText("");
        }
    }//GEN-LAST:event_iconBuscarPMMouseClicked

    private void campoIDProvBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProvBuscarKeyTyped
        if(this.campoIDProvBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProvBuscarKeyTyped

    private void campoTelNPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTelNPKeyTyped
        if(this.campoTelNP.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoTelNPKeyTyped

    private void campoIDProvBuscarMKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProvBuscarMKeyTyped
        if(this.campoIDProvBuscarM.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProvBuscarMKeyTyped

    private void campoTelMPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTelMPKeyTyped
        if(this.campoTelMP.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoTelMPKeyTyped

    private void btnModifProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifProveedorActionPerformed
        if(!(this.campoIDProvMEnc.getText().equals("")) && !(this.campoNombreMP.getText().equals(""))
                && !(this.campoTelMP.getText().equals("")) && !(this.campoDireccMP.getText().equals(""))){
            if(this.campoTelMP.getText().length() == 10){
                Proveedor proveedor = new Proveedor();
                proveedor.setId_proveedor(Integer.parseInt(this.campoIDProvMEnc.getText()));
                proveedor.setNombre(this.campoNombreMP.getText());
                proveedor.setTelefono(this.campoTelMP.getText());
                proveedor.setDireccion(this.campoDireccMP.getText());
                if(controladorProveedor.actualizaProveedor(proveedor)){
                    controladorProveedor = new ControladorProveedores("ID");
                    tablaProveedores.setModel(controladorProveedor.getModelo());

                    String m = "Modificación realizada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);

                    this.campoIDProvBuscarM.setText("");
                    this.campoIDProvMEnc.setText("");
                    this.campoCompReaMP.setText("");
                    this.campoDireccMP.setText("");
                    this.campoNombreMP.setText("");
                    this.campoTelMP.setText("");   
                }
            }else{
                String m = "Teléfono inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
        else{
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnModifProveedorActionPerformed

    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorActionPerformed
        if(!(this.campoNombreNP.getText().equals("")) && !(this.campoTelNP.getText().equals("")) && !(this.campoDireccNP.getText().equals(""))){
            if(this.campoTelNP.getText().length() == 10){
                Proveedor proveedor = new Proveedor();
                proveedor.setId_proveedor(0);
                proveedor.setNombre(this.campoNombreNP.getText());
                proveedor.setTelefono(this.campoTelNP.getText());
                proveedor.setDireccion(this.campoDireccNP.getText());
                proveedor.setNum_pedidos(0);
                if(controladorProveedor.agregarProveedor(proveedor)){
                    controladorProveedor = new ControladorProveedores("ID");
                    tablaProveedores.setModel(controladorProveedor.getModelo());

                    String m = "Registro realizado.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);

                    this.campoNombreNP.setText("");
                    this.campoTelNP.setText("");
                    this.campoDireccNP.setText("");
                }
            }else{
                String m = "Teléfono inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
        else{
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnNuevoProveedorActionPerformed

    private void iconBuscarProvNPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProvNPMouseClicked
        if(!(this.campoIDProvBuscarNP.getText().equals(""))){
            controladorProveedor = new ControladorProveedores("ID");
            Proveedor proveedor = controladorProveedor.buscarProveedorPorID(this.campoIDProvBuscarNP.getText());
            if(proveedor != null)
                this.campoNomProvNP.setText(proveedor.getNombre());
            else{
                String m = "Proveedor no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDProvBuscarNP.setText("");
                this.campoNomProvNP.setText("");
            }
        }
        else
            this.campoNomProvNP.setText("");
    }//GEN-LAST:event_iconBuscarProvNPMouseClicked

    private void btnCancelarNPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarNPActionPerformed
        this.campoIDProvBuscarNP.setText("");
        this.campoNomProvNP.setText("");
        this.campoNombreProdN.setText("");
        this.campoMarcaProdN.setText("");
        this.campoPCProdN.setText("");
        this.campoPVProdN.setText("");
        this.campoLimProdN.setText("");
        ocultarTodo();
        panelInventario.setVisible(true);
        panelInventario.setEnabled(true);
    }//GEN-LAST:event_btnCancelarNPActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(!(this.campoIDProvBuscarNP.getText().equals("")) && !(this.campoNomProvNP.getText().equals(""))
                && !(this.campoNombreProdN.getText().equals("")) && !(this.campoMarcaProdN.getText().equals(""))
                && !(this.campoPCProdN.getText().equals("")) && !(this.campoPVProdN.getText().equals(""))
                && !(this.campoLimProdN.getText().equals(""))){
            controladorProveedor = new ControladorProveedores("ID");
            Proveedor proveedor = controladorProveedor.buscarProveedorPorID(this.campoIDProvBuscarNP.getText());
            if(proveedor != null){
                Productos producto = new Productos();
                producto.setId_producto(0);
                producto.setId_proveedor(Integer.parseInt(this.campoIDProvBuscarNP.getText()));
                producto.setPrecioC(Float.parseFloat(this.campoPCProdN.getText()));
                producto.setPrecioV(Float.parseFloat(this.campoPVProdN.getText()));
                producto.setMarca(this.campoMarcaProdN.getText());
                producto.setNombre(this.campoNombreProdN.getText());
                producto.setCat_producto(this.comboCatProdN.getSelectedItem().toString());
                producto.setDisponibilidad("ND");
                producto.setStock(0);
                producto.setLimite(Integer.parseInt(this.campoLimProdN.getText()));
                if(controladorProductos.agregarProducto(producto)){
                    controladorProductos = new ControladorProductos("ID");
                    tablaInventario.setModel(controladorProductos.getModelo());
                    String m = "<html><center>" + "Registro realizado.<p>Se agregó la alerta del producto." + "<center></html>";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    this.campoIDProvBuscarNP.setText("");
                    this.campoNomProvNP.setText("");
                    this.campoNombreProdN.setText("");
                    this.campoMarcaProdN.setText("");
                    this.campoPCProdN.setText("");
                    this.campoPVProdN.setText("");
                    this.campoLimProdN.setText("");
                    ocultarTodo();
                    panelInventario.setVisible(true);
                    panelInventario.setEnabled(true);
                }
            }
            else{
                String m = "Proveedor no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
        else{
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void campoIDProvBuscarNPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProvBuscarNPKeyTyped
        if(this.campoIDProvBuscarNP.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProvBuscarNPKeyTyped

    private void campoPCProdNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPCProdNKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoPCProdN.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoPCProdNKeyTyped

    private void campoPVProdNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPVProdNKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoPVProdN.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoPVProdNKeyTyped

    private void campoLimProdNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoLimProdNKeyTyped
        if(this.campoLimProdN.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoLimProdNKeyTyped

    private void tablaComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaComprasMouseClicked
        if(tablaCompras.getRowCount() != 0){
            renglonCompraSelec = tablaCompras.rowAtPoint(evt.getPoint());
            columnCompraSelec = tablaCompras.columnAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaComprasMouseClicked

    private void btnMostrarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarCompraActionPerformed
        boolean seleccionado = false;
        int filas = tablaCompras.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaCompras.isRowSelected(i))
                seleccionado = true;
        }

        if((filas != 0) && (seleccionado != false)){
            ControladorProductosCompra controladorProdCompra = new ControladorProductosCompra(String.valueOf(tablaCompras.getValueAt(renglonCompraSelec,0)));
            tablaProdCompra.setModel(controladorProdCompra.getModelo());
            this.campoNomProvProdSelec.setText("");
            this.campoTelProvProdSelec.setText("");
            this.campoComProvProdSelec.setText("");
        }else{
            String m = "No hay una compra seleccionada.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnMostrarCompraActionPerformed

    private void comboOrdenarComprasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarComprasItemStateChanged
        if(comboOrdenarCompras.getSelectedItem().equals("Código")){
            controladorCompras = new ControladorCompras("ID");
            tablaCompras.setModel(controladorCompras.getModelo());
        }
        if(comboOrdenarCompras.getSelectedItem().equals("Fecha ascendente")){
            controladorCompras = new ControladorCompras("FechaASC");
            tablaCompras.setModel(controladorCompras.getModelo());
        }
        if(comboOrdenarCompras.getSelectedItem().equals("Fecha descendente")){
            controladorCompras = new ControladorCompras("FechaDESC");
            tablaCompras.setModel(controladorCompras.getModelo());
        }
        int row = tablaProdCompra.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdCompra.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }
        
        boolean seleccionado = false;
        for(int i = 0;i < tablaCompras.getRowCount(); i++){
            if(tablaCompras.isRowSelected(i))
                seleccionado = true;
        }
        if(seleccionado != false)
            tablaCompras.changeSelection(renglonCompraSelec, columnCompraSelec, true, false);
        
        this.campoNomProvProdSelec.setText("");
        this.campoTelProvProdSelec.setText("");
        this.campoComProvProdSelec.setText("");
    }//GEN-LAST:event_comboOrdenarComprasItemStateChanged

    private void iconBuscarCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarCompraMouseClicked
        if(!(this.campoIDCompraBuscar.getText().equals(""))){
            controladorCompras = new ControladorCompras("ID");
            if(controladorCompras.buscarCompraAct(this.campoIDCompraBuscar.getText())){
                tablaCompras.setModel(controladorCompras.getModelo());
                
                int row = tablaProdCompra.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaProdCompra.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProd.removeRow(0);
                }
                
                this.campoNomProvProdSelec.setText("");
                this.campoTelProvProdSelec.setText("");
                this.campoComProvProdSelec.setText("");
            }
            else{
                String m = "Compra no encontrada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDCompraBuscar.setText("");
            }
        }else{
            controladorCompras = new ControladorCompras("ID");
            tablaCompras.setModel(controladorCompras.getModelo());

            int row = tablaProdCompra.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdCompra.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            
            this.campoNomProvProdSelec.setText("");
            this.campoTelProvProdSelec.setText("");
            this.campoComProvProdSelec.setText("");
        }
    }//GEN-LAST:event_iconBuscarCompraMouseClicked

    private void campoIDCompraBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDCompraBuscarKeyTyped
        if(this.campoIDCompraBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDCompraBuscarKeyTyped

    private void tablaProdCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProdCompraMouseClicked
        if(tablaProdCompra.getRowCount() != 0){
            renglonProdCSelec = tablaProdCompra.rowAtPoint(evt.getPoint());
            columnProdCSelec = tablaProdCompra.columnAtPoint(evt.getPoint());
            controladorProductos = new ControladorProductos("ID");
            Productos producto = controladorProductos.buscarProductoPorID(tablaProdCompra.getValueAt(renglonProdCSelec, 0).toString());
            if(producto != null){
                controladorProveedor = new ControladorProveedores("ID");
                Proveedor proveedor = controladorProveedor.buscarProveedorPorID(String.valueOf(producto.getId_proveedor()));
                if(proveedor != null){
                    this.campoNomProvProdSelec.setText(proveedor.getNombre());
                    this.campoTelProvProdSelec.setText(proveedor.getTelefono());
                    this.campoComProvProdSelec.setText(""+proveedor.getNum_pedidos());
                }
                else{
                    String m = "Proveedor no encontrado.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
            }
            else{
                String m = "Producto no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_tablaProdCompraMouseClicked

    private void btnGenReporteAlertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenReporteAlertaActionPerformed
        int row = tablaAlertas.getRowCount(), seleccionados = 0, rowP = tablaNotificacionC.getRowCount();
        if(row != 0){
            for(int i = 0 ; i < row ; i++){
                if(tablaAlertas.getValueAt(i,3).equals(true))
                    seleccionados += 1;
            }
        }
        
        if(rowP != 0){
            for(int i = 0 ; i < rowP ; i++){
                if(tablaNotificacionC.getValueAt(i,3).equals(true))
                    seleccionados += 1;
            }
        }
        
        if(seleccionados != 0){
            ControladorMetodos controladorMetodos = new ControladorMetodos();
            int idp = controladorMetodos.ultimoPedido();
            boolean existe = false;
            idp += 1;
            Calendar calendario = Calendar.getInstance();
            String dia = "" + calendario.get(Calendar.DATE);
            String mes = "" + (calendario.get(Calendar.MONTH)+1);
            String anio = "" + calendario.get(Calendar.YEAR);
            
            ArrayList prodSelec = new ArrayList();
            ArrayList prodExiste = new ArrayList();
            
            for(int i = 0 ; i < row ; i++){
                if(tablaAlertas.getValueAt(i,3).equals(true)){
                    if(!prodSelec.contains(tablaAlertas.getValueAt(i,0).toString())){
                        prodSelec.add(tablaAlertas.getValueAt(i,0).toString());
                    }
                }
            }
            
            for(int i = 0 ; i < rowP ; i++){
                if(tablaNotificacionC.getValueAt(i,3).equals(true)){
                    if(!prodSelec.contains(tablaNotificacionC.getValueAt(i,0).toString())){
                        prodSelec.add(tablaNotificacionC.getValueAt(i,0).toString());
                    }
                }
            }
            
            for(int i = 0; i < prodSelec.size(); i++){
                if(controladorMetodos.existePedido(Integer.valueOf(prodSelec.get(i).toString())) != 0){
                    prodExiste.add(prodSelec.get(i).toString());
                }
            }
            
            if(prodExiste.isEmpty()){
                for(int i = 0; i < prodSelec.size(); i++){
                    controladorProductos = new ControladorProductos("ID");
                    Pedido pedido = new Pedido();
                    Productos producto = controladorProductos.buscarProductoPorID(prodSelec.get(i).toString());

                    pedido.setId_pedido(idp);
                    pedido.setId_producto(producto.getId_producto());
                    pedido.setPrecioC(producto.getPrecioC());
                    pedido.setFecha(anio+"-"+mes+"-"+dia);
                    controladorPedidos.agregarPedido(pedido);
                    for(int j = 0 ; j < row ; j++){
                        if(String.valueOf(tablaAlertas.getValueAt(j,0)).equals(prodSelec.get(i).toString())){
                            controladorAlertas.eliminarAlerta(Integer.parseInt(String.valueOf(tablaAlertas.getValueAt(j,0))), String.valueOf(tablaAlertas.getValueAt(j, 2)));
                        }
                    }
                    for(int j = 0 ; j < rowP ; j++){
                        if(String.valueOf(tablaNotificacionC.getValueAt(j,0)).equals(prodSelec.get(i).toString())){
                            controladorAlertaPedido.eliminarAlerta(Integer.parseInt(String.valueOf(tablaNotificacionC.getValueAt(j,0))), String.valueOf(tablaNotificacionC.getValueAt(j, 2)));
                        }
                    }
                }
                
                String m = "Pedido registrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);

                controladorAlertas = new ControladorAlerta(0);
                tablaAlertas.setModel(controladorAlertas.getModelo());

                controladorAlertaPedido = new ControladorAlertaPedido(0);
                tablaNotificacionC.setModel(controladorAlertaPedido.getModelo());
            }
            else{
                String m = "<html><center>" + "Ya existe un pedido pendiente para los productos <p>";
                //String m = "Ya existe un pedido pendiente para los productos ";
                for(int i = 0; i < prodExiste.size(); i++){
                    if(i != (prodExiste.size()-1))
                        m += prodExiste.get(i) + ", ";
                    else
                        m += prodExiste.get(i) + ".<center></html>";
                }
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                String m2 = "Se eliminarán las alertas correspondientes.";
                AlertaWarning salir2 = new AlertaWarning(this, true);
                salir2.mensaje.setText(m2);
                salir2.setVisible(true);
                
                for(int i = 0; i < prodExiste.size(); i++){
                    for(int j = 0 ; j < rowP ; j++){
                        if(String.valueOf(tablaNotificacionC.getValueAt(j,0)).equals(prodExiste.get(i).toString())){
                            controladorAlertaPedido.eliminarAlerta(Integer.parseInt(String.valueOf(tablaNotificacionC.getValueAt(j,0))), String.valueOf(tablaNotificacionC.getValueAt(j, 2)));
                        }
                    }
                }
                
                String m3 = "Se han eliminado las alertas correspondientes.";
                AlertaWarning salir3 = new AlertaWarning(this, true);
                salir3.mensaje.setText(m3);
                salir3.setVisible(true);
                
                controladorAlertaPedido = new ControladorAlertaPedido(0);
                tablaNotificacionC.setModel(controladorAlertaPedido.getModelo());
            }
            
            
        }else{
            String m = "No hay productos seleccionados.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnGenReporteAlertaActionPerformed

    private void iconBuscarPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarPedidoMouseClicked
        if(!(this.campoIDPedidoBuscar.getText().equals(""))){
            controladorPedidos = new ControladorPedidos();
            Pedido pedido = controladorPedidos.buscarProd(this.campoIDPedidoBuscar.getText());
                    
            if(pedido != null){
                if(pedido.getEstado().equals("Pendiente")){
                    controladorPedidos = new ControladorPedidos(this.campoIDPedidoBuscar.getText());
                    tablaProdNC.setModel(controladorPedidos.getModelo());
                    this.campoIDPAuxliar.setText(""+pedido.getId_pedido());
                    this.CampoTotalNC.setText("");
                    this.campoIDProvNC.setText("");
                    this.campoNomProvNC.setText("");
                    this.campoCantProdNC.setText("");
                }
                else{
                    String m = "Este pedido ya ha sido comprado.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    this.campoIDPedidoBuscar.setText("");
                }
            }
            else{
                String m = "Pedido no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDPedidoBuscar.setText("");
            }
        }else{
            int row = tablaProdNC.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNC.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            this.campoIDPAuxliar.setText("");
            this.CampoTotalNC.setText("");
            this.campoIDProvNC.setText("");
            this.campoNomProvNC.setText("");
            this.campoCantProdNC.setText("");
        }
    }//GEN-LAST:event_iconBuscarPedidoMouseClicked

    private void campoIDPedidoBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDPedidoBuscarKeyTyped
        if(this.campoIDPedidoBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDPedidoBuscarKeyTyped

    private void campoCantProdNCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoCantProdNCKeyTyped
        if(this.campoCantProdNC.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoCantProdNCKeyTyped

    private void btnModiCantProdNCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiCantProdNCActionPerformed
        boolean seleccionado = false;
        int filas = tablaProdNC.getRowCount();
        float suma = 0;
        for(int i = 0;i < filas; i++){
            if(tablaProdNC.isRowSelected(i))
                seleccionado = true;
        }
        
        if((filas != 0) && (seleccionado!=false)){
            String codigo = (String.valueOf(tablaProdNC.getValueAt(renglonProdNC, 0)));
            Productos producto = controladorProductos.buscarProductoPorID(codigo);

            //se agrega el producto, se realiza de nuevo la validación de la cantidad ingresada, en caso de que
            //el usuario no hay confirmado con la tecla ENTER
            if((Integer.parseInt(this.campoCantProdNC.getText()) <= 0) || 
                    (this.campoCantProdNC.getText().equals(""))){
                String m = "Valor inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoCantProdNC.setText("");
            }
            else{                
                //se agrega fila si la cantidad es diferente a 0 y que no se encuentra ya agregado el producto
                DefaultTableModel modeloProdNC = (DefaultTableModel) tablaProdNC.getModel();
                modeloProdNC.setValueAt(this.campoCantProdNC.getText(), renglonProdNC, 2);
                modeloProdNC.setValueAt(Float.parseFloat(this.campoCantProdNC.getText()) * producto.getPrecioC(), renglonProdNC, 3);

                tablaProdNC.changeSelection(renglonProdNC, columnProdNC, true, false);

                for(int i = 0;i < filas; i++)
                    suma += Float.parseFloat(String.valueOf(tablaProdNC.getValueAt(i, 3)));
                if(suma != 0)
                    this.CampoTotalNC.setText(""+suma);
            }
        }else{
            String m = "No hay un producto seleccionado.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }

    }//GEN-LAST:event_btnModiCantProdNCActionPerformed

    private void tablaProdNCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProdNCMouseClicked
        if(tablaProdNC.getRowCount() != 0){
            renglonProdNC = tablaProdNC.rowAtPoint(evt.getPoint());
            columnProdNC = tablaProdNC.columnAtPoint(evt.getPoint());
            
            this.campoCantProdNC.setText(tablaProdNC.getValueAt(renglonProdNC, 2).toString());
            
            String codigo = (String.valueOf(tablaProdNC.getValueAt(renglonProdNC, 0)));
            Productos producto = controladorProductos.buscarProductoPorID(codigo);
            
            controladorProveedor = new ControladorProveedores("ID");
            Proveedor proveedor = controladorProveedor.buscarProveedorPorID(String.valueOf(producto.getId_proveedor()));
            if(proveedor != null){
                this.campoIDProvNC.setText(""+proveedor.getId_proveedor());
                this.campoNomProvNC.setText(proveedor.getNombre());
            }
        }
    }//GEN-LAST:event_tablaProdNCMouseClicked

    private void btnAcepNuevaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcepNuevaCompraActionPerformed
        int row = tablaProdNC.getRowCount();
        ControladorMetodos controladorMetodos = new ControladorMetodos();
        int idNueva = controladorMetodos.ultimaCompra() + 2;
        this.campoIDCompraN.setText(String.valueOf(idNueva));
        
        if(row != 0 || !(this.CampoTotalNC.getText().equals(""))){
            int ceros = 0;
            int cant_prod = 0;
            
            for(int i = 0 ; i < row ; i++){
                if(tablaProdNC.getValueAt(i,2).equals(0))
                    ceros += 1;
            }
            
            if(ceros == 0){
                for(int i = 0 ; i < row ; i++)
                    cant_prod += Integer.parseInt(tablaProdNC.getValueAt(i,2).toString());
                boolean resultado = false;
                Compra compra = new Compra();
                ControladorProductosCompra controladorProdCompra = new ControladorProductosCompra();
                
                compra.setId_compra(Integer.parseInt(this.campoIDCompraN.getText()));
                compra.setPrecio_total(Float.parseFloat(this.CampoTotalNC.getText()));
                compra.setCant_productos(cant_prod);
                compra.setFecha(this.campoFechaNC.getText());
                if(controladorCompras.agregarCompra(compra)){
                    for(int i = 0 ; i < row ; i++){
                        ProductosCompra producto = new ProductosCompra();
                        producto.setId_compra(compra.getId_compra());
                        producto.setId_producto(Integer.parseInt(this.tablaProdNC.getValueAt(i,0).toString()));
                        producto.setCant_producto(Integer.parseInt(this.tablaProdNC.getValueAt(i,2).toString()));
                        producto.setPrecio_compra(Float.parseFloat(this.tablaProdNC.getValueAt(i,3).toString()) / 
                                Float.parseFloat(this.tablaProdNC.getValueAt(i,2).toString()));
                        if(controladorProdCompra.agregarProdCompra(producto))
                            resultado = true;
                    }
                }
                if(resultado == true){
                    controladorPedidos.actualizaPedido(Integer.parseInt(this.campoIDPAuxliar.getText()));
                    controladorProveedor.actualizaProveedorP(compra.getId_compra());
                    
                    String m = "Compra registrada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    controladorProductos.actualizarDisponibiilidad();
                    
                    DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdNC.getModel();
                    for(int i=0;i < row; i++)
                        modeloProdAgregados.removeRow(0);
                    
                    this.CampoTotalNC.setText("");
                    this.campoIDProvNC.setText("");
                    this.campoIDCompraN.setText("");
                    this.campoNomProvNC.setText("");
                    this.campoIDPAuxliar.setText("");
                    this.campoCantProdNC.setText("");
                    this.campoIDPedidoBuscar.setText("");
                    
                    controladorPedidos = new ControladorPedidos();
                    tablaPedidos.setModel(controladorPedidos.getModelo());
                    
                    ocultarTodo();
                    panelPedidos.setVisible(true);
                    panelPedidos.setEnabled(true);
                    
                }
            }
            else{
                String m = "Hay productos con una cantidad de 0.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
        else{
            String m = "Hay campos vacíos.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnAcepNuevaCompraActionPerformed

    private void btnCanNuevaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanNuevaCompraActionPerformed
        int filasPA = tablaProdNC.getRowCount();
        if(filasPA != 0){
            DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdNC.getModel();
            for(int i=0;i < filasPA; i++)
                modeloProdAgregados.removeRow(0);
        }
        this.CampoTotalNC.setText("");
        this.campoIDProvNC.setText("");
        this.campoNomProvNC.setText("");
        this.campoIDPAuxliar.setText("");
        this.campoCantProdNC.setText("");
        this.campoIDPedidoBuscar.setText("");
        ocultarTodo();
        panelPedidos.setVisible(true);
        panelPedidos.setEnabled(true);
    }//GEN-LAST:event_btnCanNuevaCompraActionPerformed

    private void campoNombreNPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNombreNPKeyTyped
        if(this.campoNombreNP.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoNombreNPKeyTyped

    private void campoNombreMPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNombreMPKeyTyped
        if(this.campoNombreMP.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoNombreMPKeyTyped

    private void campoNombreProdNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNombreProdNKeyTyped
        if(this.campoNombreProdN.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoNombreProdNKeyTyped

    private void campoMarcaProdNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMarcaProdNKeyTyped
        if(this.campoMarcaProdN.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoMarcaProdNKeyTyped

    private void campoNomProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomProdModiKeyTyped
        if(this.campoNomProdModi.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoNomProdModiKeyTyped

    private void campoMarcaProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMarcaProdModiKeyTyped
        if(this.campoMarcaProdModi.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoMarcaProdModiKeyTyped

    private void campoLimiteProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoLimiteProdModiKeyTyped
        if(!(this.campoLimiteProdModi.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }else
            evt.consume();
    }//GEN-LAST:event_campoLimiteProdModiKeyTyped

    private void campoIDProdElimKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdElimKeyTyped
        if(this.campoIDProdElim.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProdElimKeyTyped

    private void iconBuscarProdEliminado1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProdEliminado1MouseClicked
        if(!(this.campoIDPedidoBuscarTabla.getText().equals(""))){
            controladorPedidos = new ControladorPedidos();
            if(controladorPedidos.buscarPedidoAct(this.campoIDPedidoBuscarTabla.getText()))
                tablaPedidos.setModel(controladorPedidos.getModelo());
            else{
                String m = "Pedido no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDPedidoBuscarTabla.setText("");
            }

        }else{
            controladorPedidos = new ControladorPedidos();
            tablaPedidos.setModel(controladorPedidos.getModelo());
        }
    }//GEN-LAST:event_iconBuscarProdEliminado1MouseClicked

    private void campoIDPedidoBuscarTablaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDPedidoBuscarTablaKeyTyped
        if(this.campoIDPedidoBuscarTabla.getText().length() >= 11)
           evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDPedidoBuscarTablaKeyTyped

    private void campoDireccNPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDireccNPKeyTyped
        if(this.campoDireccNP.getText().length() >= 60)
            evt.consume();
    }//GEN-LAST:event_campoDireccNPKeyTyped

    private void campoDireccMPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDireccMPKeyTyped
        if(this.campoDireccMP.getText().length() >= 60)
            evt.consume();
    }//GEN-LAST:event_campoDireccMPKeyTyped

    private void btnEliminarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProvActionPerformed
        boolean seleccionado = false;
        int filas = tablaProveedores.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaProveedores.isRowSelected(i))
                seleccionado = true;
        }

        if((filas != 0) && (seleccionado != false)){
            int id = Integer.parseInt(String.valueOf(tablaProveedores.getValueAt(renglonProvSelec,0)));
            
            AlertaConfirm pregunta = new AlertaConfirm(this, true);
            pregunta.mensaje.setText("¿Desea eliminar?.");
            pregunta.setVisible(true);

            boolean respuesta = pregunta.respuesta;
            
            if(respuesta){
                if(controladorProveedor.eliminarProveedor(id)){
                    String m = "Se ha eliminado el proveedor.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }    
            }
            
            controladorProveedor = new ControladorProveedores("ID");
            tablaProveedores.setModel(controladorProveedor.getModelo());
            
        }else{
            String m = "No hay un proveedor seleccionado.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnEliminarProvActionPerformed

    private void tablaProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedoresMouseClicked
        if(tablaProveedores.getRowCount() != 0){
            renglonProvSelec = tablaProveedores.rowAtPoint(evt.getPoint());
            columnProvSelec = tablaProveedores.columnAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaProveedoresMouseClicked

    private void tablaNotificacionCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaNotificacionCMouseClicked
        if(tablaNotificacionC.getRowCount() != 0){
            renglonNotif = tablaNotificacionC.rowAtPoint(evt.getPoint());
            columnNotif = tablaNotificacionC.columnAtPoint(evt.getPoint());

            //se realiza el evento de selecciona de renglones (marcar las casillas de cada fila)
            if (tablaNotificacionC.getValueAt(renglonNotif, 3).equals(true))
                tablaNotificacionC.setValueAt(false, renglonNotif, 3);
            else
                tablaNotificacionC.setValueAt(true, renglonNotif, 3);
        }
    }//GEN-LAST:event_tablaNotificacionCMouseClicked

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
            java.util.logging.Logger.getLogger(AreaCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AreaCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AreaCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AreaCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AreaCompras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CampoTotalNC;
    private javax.swing.JButton btnAcepNuevaCompra;
    private javax.swing.JButton btnAceptarRegElim;
    private javax.swing.JButton btnBuscarProdElim;
    private javax.swing.JButton btnBuscarProdModi;
    private javax.swing.JButton btnCanNuevaCompra;
    private javax.swing.JButton btnCancelarNP;
    private javax.swing.JButton btnCancelarProdElim;
    private javax.swing.JButton btnCancelarProdModi;
    private javax.swing.JButton btnCompras;
    private javax.swing.JButton btnCrearCompra;
    private javax.swing.JButton btnElimProdCant;
    private javax.swing.JButton btnElimProducto;
    private javax.swing.JButton btnElimProductoBD;
    private javax.swing.JButton btnEliminarProv;
    private javax.swing.JButton btnGenReporteAlerta;
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnModiCantProdNC;
    private javax.swing.JButton btnModiProducto;
    private javax.swing.JButton btnModiProductoBD;
    private javax.swing.JButton btnModifProveedor;
    private javax.swing.JButton btnMostrarCompra;
    private javax.swing.JButton btnMostrarElim;
    private javax.swing.JButton btnNotificaciones;
    private javax.swing.JButton btnNuevaCompra;
    private javax.swing.JButton btnNuevoProveedor;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnRecarProdElim;
    private javax.swing.JButton btnRecarProdModi;
    private javax.swing.JButton btnRegProducto;
    private javax.swing.JTextField campoCantElimProdElim;
    private javax.swing.JTextField campoCantProdNC;
    private javax.swing.JTextField campoCatProdElim;
    private javax.swing.JTextField campoComProvProdSelec;
    private javax.swing.JTextField campoCompReaMP;
    private javax.swing.JTextField campoDireccMP;
    private javax.swing.JTextField campoDireccNP;
    private javax.swing.JTextField campoDispProdElim;
    private javax.swing.JTextField campoFechaNC;
    private javax.swing.JTextField campoIDCompraBuscar;
    private javax.swing.JTextField campoIDCompraN;
    private javax.swing.JTextField campoIDPAuxliar;
    private javax.swing.JTextField campoIDPedidoBuscar;
    private javax.swing.JTextField campoIDPedidoBuscarTabla;
    private javax.swing.JTextField campoIDProElimEnc;
    private javax.swing.JTextField campoIDProdElim;
    private javax.swing.JTextField campoIDProdIEliminado;
    private javax.swing.JTextField campoIDProdInventario;
    private javax.swing.JTextField campoIDProdModi;
    private javax.swing.JTextField campoIDProdModiEncon;
    private javax.swing.JTextField campoIDProvBuscar;
    private javax.swing.JTextField campoIDProvBuscarM;
    private javax.swing.JTextField campoIDProvBuscarNP;
    private javax.swing.JTextField campoIDProvMEnc;
    private javax.swing.JTextField campoIDProvNC;
    private javax.swing.JTextField campoLimProdN;
    private javax.swing.JTextField campoLimiteProdModi;
    private javax.swing.JTextField campoMarcaProdElim;
    private javax.swing.JTextField campoMarcaProdModi;
    private javax.swing.JTextField campoMarcaProdN;
    private javax.swing.JTextField campoNomProdElim;
    private javax.swing.JTextField campoNomProdModi;
    private javax.swing.JTextField campoNomProvNC;
    private javax.swing.JTextField campoNomProvNP;
    private javax.swing.JTextField campoNomProvProdSelec;
    private javax.swing.JTextField campoNombreMP;
    private javax.swing.JTextField campoNombreNP;
    private javax.swing.JTextField campoNombreProdN;
    private javax.swing.JTextField campoPCProdN;
    private javax.swing.JTextField campoPVProdN;
    private javax.swing.JTextField campoPrCProdModi;
    private javax.swing.JTextField campoPrVProdModi;
    private javax.swing.JTextField campoPrecioCProdElim;
    private javax.swing.JTextField campoPrecioVProdElim;
    private javax.swing.JTextField campoStockProdElim;
    private javax.swing.JTextField campoStockProdModi;
    private javax.swing.JTextField campoTelMP;
    private javax.swing.JTextField campoTelNP;
    private javax.swing.JTextField campoTelProvProdSelec;
    private javax.swing.JLabel cerrarAreaVentas;
    private javax.swing.JComboBox<String> comboCatProdModi;
    private javax.swing.JComboBox<String> comboCatProdN;
    private javax.swing.JComboBox<String> comboDisProdModi;
    private javax.swing.JComboBox<String> comboMotElimProd;
    private javax.swing.JComboBox<String> comboOrdenarCompras;
    private javax.swing.JComboBox<String> comboOrdenarElim;
    private javax.swing.JComboBox<String> comboOrdenarInventario;
    private javax.swing.JComboBox<String> comboOrdenarProveedor;
    private javax.swing.JLabel fechaSistema;
    private javax.swing.JLabel fondoCompras;
    private javax.swing.JLabel fondoElimProducto;
    private javax.swing.JLabel fondoInventario;
    private javax.swing.JLabel fondoInventario1;
    private javax.swing.JLabel fondoModiProducto;
    private javax.swing.JLabel fondoNotificaciones;
    private javax.swing.JLabel fondoNuevProducto;
    private javax.swing.JLabel fondoNuevaCompra;
    private javax.swing.JLabel fondoPedidos;
    private javax.swing.JLabel fondoProveedor;
    private javax.swing.JLabel fondoRegCompras;
    private javax.swing.JPanel headerCompras;
    private javax.swing.JLabel horaSistema;
    private javax.swing.JLabel iconBuscarCompra;
    private javax.swing.JLabel iconBuscarPM;
    private javax.swing.JLabel iconBuscarPedido;
    private javax.swing.JLabel iconBuscarProdEliminado;
    private javax.swing.JLabel iconBuscarProdEliminado1;
    private javax.swing.JLabel iconBuscarProdInven;
    private javax.swing.JLabel iconBuscarProvNP;
    private javax.swing.JLabel iconBuscarProveedor;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel panelAcCanFac;
    private javax.swing.JPanel panelBtnAcepElim;
    private javax.swing.JPanel panelBtnBuscRecProdElim;
    private javax.swing.JPanel panelBtnBuscRecProdModi;
    private javax.swing.JPanel panelBtnCanProdElim;
    private javax.swing.JPanel panelBtnModiProd;
    private javax.swing.JPanel panelBtnProduc;
    private javax.swing.JPanel panelCerrar;
    private javax.swing.JPanel panelCompras;
    private javax.swing.JPanel panelElimProducto;
    private javax.swing.JPanel panelFechaHeader;
    private javax.swing.JPanel panelInventario;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelModiProducto;
    private javax.swing.JPanel panelNotificaciones;
    private javax.swing.JPanel panelNuevProducto;
    private javax.swing.JPanel panelNuevaCompra;
    private javax.swing.JPanel panelPedidos;
    private javax.swing.JPanel panelProveedor;
    private javax.swing.JPanel panelRaiz;
    private javax.swing.JPanel panelRegistroElim;
    private javax.swing.JPanel panelTabProveedores;
    private javax.swing.JPanel panelTablaAlertas;
    private javax.swing.JPanel panelTablaEliminados;
    private javax.swing.JPanel panelTablaInventario;
    private javax.swing.JPanel panelTablaNotificaciones;
    private javax.swing.JScrollPane scrollAlertas;
    private javax.swing.JScrollPane scrollCompras;
    private javax.swing.JScrollPane scrollEliminados;
    private javax.swing.JScrollPane scrollInventario;
    private javax.swing.JScrollPane scrollNotificacionC;
    private javax.swing.JScrollPane scrollPedidos;
    private javax.swing.JScrollPane scrollProdCompra;
    private javax.swing.JScrollPane scrollProdNC;
    private javax.swing.JScrollPane scrollProveedores;
    private javax.swing.JTable tablaAlertas;
    private javax.swing.JTable tablaCompras;
    private javax.swing.JTable tablaEliminados;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaNotificacionC;
    private javax.swing.JTable tablaPedidos;
    private javax.swing.JTable tablaProdCompra;
    private javax.swing.JTable tablaProdNC;
    private javax.swing.JTable tablaProveedores;
    // End of variables declaration//GEN-END:variables
}
