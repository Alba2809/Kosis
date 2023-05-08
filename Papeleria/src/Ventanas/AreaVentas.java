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
import ComponentePDF.Reporte;
import ComponentePDF.ReportePDF;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author josei
 */
public class AreaVentas extends javax.swing.JFrame implements Runnable{
    //Variable que indicará la ruta relativa donde se guardarán los tickets
    String ruta  ="..\\Tickets\\";
    //Variables para el reloj y fecha
    String hora,minutos;
    Calendar callendario;
    Thread reloj;
    
    //Variables que serviran para obtener las coordenadas del mouse (esta se aplicará para mover las ventanas)
    int xMouse, yMouse;
    
    //Controladores con los que se obtendrá los datos de la BD
    ControladorProductos controladorProductos = new ControladorProductos("ID");
    ControladorProducElim controladorPEliminados = new ControladorProducElim("ID");
    ControladorVentaLocal controladorVentasL = new ControladorVentaLocal("Folio");
    ControladorFacturacion controladorFacturas = null;
    ControladorClienteLocal controladorClientesL = new ControladorClienteLocal();
    ControladorClienteWeb controladorClientesW = new ControladorClienteWeb();
    ControladorVentaWeb controladorVentasW = new ControladorVentaWeb();
    ControladorClienteGeneral controladorClienteG = new ControladorClienteGeneral("ID");
    ControladorDevoluciones controladorDev = new ControladorDevoluciones("ID");
    ControladorProveedores controladorProveedor = new ControladorProveedores("ID");
    ControladorAlerta controladorAlertas = new ControladorAlerta(1);
    ControladorAlertaPedido controladorAlertasPedido = new ControladorAlertaPedido(1);
    ControladorNotificaciones controladorNotificaciones = new ControladorNotificaciones();
    
    //Variables para obtener el renglon/fila seleccionado de las tablas de Nueva Venta
    private int renglonProdBuscado;
    private int renglonProdAgregado = 0;
    private int columnProdAgregado = 0;
    private int renglonVentaLSeleccionado = 0;
    private int columnVentaLSeleccionado = 0;
    private int renglonVentaWSeleccionado = 0;
    private int columnVentaWSeleccionado = 0;
    private int renglonClienteSeleccionado = 0;
    private int columnClienteSeleccionado = 0;
    private int renglonProdDevSeleccionado = 0;
    private int columnProdDevSeleccionado = 0;
    private int renglonAlerta = 0;
    private int columnAlerta = 0;
    private int renglonNotif = 0;
    private int columnNotif = 0;
    
    /**
     * Creates new form AreaVentas
     */
    public AreaVentas() {
        initComponents();
        
        setLocationRelativeTo(null); //ventana en medio de la pantalla
        
        btnVentaWeb.setVisible(false);

        //----------------Fecha y hora actual------------------------------------------
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
        //----------------Apartado nueva ventaL------------------------------------------
        JTableHeader tHeaderProdBuscadosNV = tablaProdBuscadosNV.getTableHeader();
        tHeaderProdBuscadosNV.setDefaultRenderer(new Encabezado());
        tablaProdBuscadosNV.setTableHeader(tHeaderProdBuscadosNV);
        
        scrollProdBuscadosNV.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdBuscadosNV.setBackground(new Color(250,226,211));
        tablaProdBuscadosNV.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdAgregadosNV = tablaProdAgregadosNV.getTableHeader();
        tHeaderProdAgregadosNV.setDefaultRenderer(new Encabezado());
        tablaProdAgregadosNV.setTableHeader(tHeaderProdAgregadosNV);
        
        scrollProdAgregados.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdAgregadosNV.setBackground(new Color(250,226,211));
        tablaProdAgregadosNV.setSelectionBackground(new Color( 236,200,160));
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
        //--------------Apartado ventas locales--------------------------------------------------
        JTableHeader tHeaderVentaLocal = tablaVentasL.getTableHeader();
        tHeaderVentaLocal.setDefaultRenderer(new Encabezado());
        tablaVentasL.setTableHeader(tHeaderVentaLocal);
        
        scrollVentasL.getVerticalScrollBar().setUI(new Scroll());
        
        tablaVentasL.setBackground(new Color(250,226,211));
        tablaVentasL.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdVentaLocal = tablaProdVentasL.getTableHeader();
        tHeaderProdVentaLocal.setDefaultRenderer(new Encabezado());
        tablaProdVentasL.setTableHeader(tHeaderProdVentaLocal);
        
        scrollProdVentasL.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdVentasL.setBackground(new Color(250,226,211));
        tablaProdVentasL.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdFactura = tablaProdFacturaL.getTableHeader();
        tHeaderProdFactura.setDefaultRenderer(new Encabezado());
        tablaProdFacturaL.setTableHeader(tHeaderProdFactura);
        
        scrollProdFactura.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdFacturaL.setBackground(new Color(250,226,211));
        tablaProdFacturaL.setSelectionBackground(new Color( 236,200,160));
        //-----------Apartado ventas web---------------------------------------
        JTableHeader tHeaderVentaW = tablaVentasW.getTableHeader();
        tHeaderVentaW.setDefaultRenderer(new Encabezado());
        tablaVentasW.setTableHeader(tHeaderVentaW);
        
        scrollVentasW.getVerticalScrollBar().setUI(new Scroll());
        
        tablaVentasW.setBackground(new Color(250,226,211));
        tablaVentasW.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdVentaWeb = tablaProdVentaW.getTableHeader();
        tHeaderProdVentaWeb.setDefaultRenderer(new Encabezado());
        tablaProdVentaW.setTableHeader(tHeaderProdVentaWeb);
        
        scrollProdVentaW.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdVentaW.setBackground(new Color(250,226,211));
        tablaProdVentaW.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderClienteVentaW = tablaClienteVentaW.getTableHeader();
        tHeaderClienteVentaW.setDefaultRenderer(new Encabezado());
        tablaClienteVentaW.setTableHeader(tHeaderClienteVentaW);
        
        scrollClienteVentaW.getVerticalScrollBar().setUI(new Scroll());
        
        tablaClienteVentaW.setBackground(new Color(250,226,211));
        tablaClienteVentaW.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderClienteProdFacW = tablaProdFacW.getTableHeader();
        tHeaderClienteProdFacW.setDefaultRenderer(new Encabezado());
        tablaProdFacW.setTableHeader(tHeaderClienteProdFacW);
        
        scrollProdFacW.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdFacW.setBackground(new Color(250,226,211));
        tablaProdFacW.setSelectionBackground(new Color( 236,200,160));
        //-------------------Apartado clientes general-------------------------------------
        JTableHeader tHeaderClienteG = tablaClientes.getTableHeader();
        tHeaderClienteG.setDefaultRenderer(new Encabezado());
        tablaClientes.setTableHeader(tHeaderClienteG);
        
        scrollClientes.getVerticalScrollBar().setUI(new Scroll());
        
        tablaClientes.setBackground(new Color(250,226,211));
        tablaClientes.setSelectionBackground(new Color( 236,200,160));
        campoIDClienEncon.setVisible(false);
        //-----------Apartado nueva factura---------------------------------------
        JTableHeader tHeaderNFac = tablaProdNuevFacL.getTableHeader();
        tHeaderNFac.setDefaultRenderer(new Encabezado());
        tablaProdNuevFacL.setTableHeader(tHeaderNFac);
        
        scrollProdNuevFacL.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdNuevFacL.setBackground(new Color(250,226,211));
        tablaProdNuevFacL.setSelectionBackground(new Color( 236,200,160));
        iconBuscarClienFac.setBackground(new Color(225,225,225,0));
        iconBuscarVentaFac.setBackground(new Color(225,225,225,0));
        campoTotalNFactura.setBackground(new Color(225,225,225,0));
        campoIDVentaFacEncon.setVisible(false);
        campoIDClienFacEncon.setVisible(false);
        //-----------Apartado devoluciones---------------------------------------
        JTableHeader tHeaderDev = tablaDev.getTableHeader();
        tHeaderDev.setDefaultRenderer(new Encabezado());
        tablaDev.setTableHeader(tHeaderDev);
        
        scrollDev.getVerticalScrollBar().setUI(new Scroll());
        
        tablaDev.setBackground(new Color(250,226,211));
        tablaDev.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderDevS = tablaProdSelecDev.getTableHeader();
        tHeaderDevS.setDefaultRenderer(new Encabezado());
        tablaProdSelecDev.setTableHeader(tHeaderDevS);
        
        scrollProdSelecDev.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdSelecDev.setBackground(new Color(250,226,211));
        tablaProdSelecDev.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdDev = tablaProdNDev.getTableHeader();
        tHeaderProdDev.setDefaultRenderer(new Encabezado());
        tablaProdNDev.setTableHeader(tHeaderProdDev);
        
        scrollProdNDev.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdNDev.setBackground(new Color(250,226,211));
        tablaProdNDev.setSelectionBackground(new Color( 236,200,160));
        //------------------------------------------------------------------------------
        JTableHeader tHeaderProdSDev = tablaProdSelecNDev.getTableHeader();
        tHeaderProdSDev.setDefaultRenderer(new Encabezado());
        tablaProdSelecNDev.setTableHeader(tHeaderProdSDev);
        
        scrollProdSelecNDev.getVerticalScrollBar().setUI(new Scroll());
        
        tablaProdSelecNDev.setBackground(new Color(250,226,211));
        tablaProdSelecNDev.setSelectionBackground(new Color( 236,200,160));
        
        //---------------------Apartado Alertas-Notificaciones------------------------------
        JTableHeader tHeaderAlertas = tablaAlertas.getTableHeader();
        tHeaderAlertas.setDefaultRenderer(new Encabezado());
        tablaAlertas.setTableHeader(tHeaderAlertas);
        
        scrollAlertas.getVerticalScrollBar().setUI(new Scroll());
        
        tablaAlertas.setBackground(new Color(250,226,211));
        tablaAlertas.setSelectionBackground(new Color( 236,200,160));
        tablaAlertas.setRowHeight(40);
        
        //------------------------------------------------------------------------------
        JTableHeader tHeaderNotif = tablaNotificacion.getTableHeader();
        tHeaderNotif.setDefaultRenderer(new Encabezado());
        tablaNotificacion.setTableHeader(tHeaderNotif);
        
        scrollNotificacion.getVerticalScrollBar().setUI(new Scroll());
        
        tablaNotificacion.setBackground(new Color(250,226,211));
        tablaNotificacion.setSelectionBackground(new Color( 236,200,160));
        
        //------------------------------------------------------------------------------
        comboFormaNFactura.setUI(new CustomUI());
        comboFormaNFactura.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
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
        //---------------------Apartado ventas locales----------------------------------------
        comboOrdenarVentasL.setUI(new CustomUI());
        comboOrdenarVentasL.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboFormaFacL.setUI(new CustomUI());
        comboFormaFacL.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboFormaNVenta.setUI(new CustomUI());
        comboFormaNVenta.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //---------------------Apartado ventas web----------------------------------------
        comboOrdenarVentasW.setUI(new CustomUI());
        comboOrdenarVentasW.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        iconBuscarVentaW.setBackground(new Color(225,225,225,0));
        //------------------------------------------------------------------------------
        comboFormaFacW.setUI(new CustomUI());
        comboFormaFacW.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboEstadoFacW.setUI(new CustomUI());
        comboEstadoFacW.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------Apartado clientes general----------------------------------
        comboOrdenarClientes.setUI(new CustomUI());
        comboOrdenarClientes.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------Apartado devoluciones----------------------------------
        comboOrdenarDev.setUI(new CustomUI());
        comboOrdenarDev.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboTipoNDev.setUI(new CustomUI());
        comboTipoNDev.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //------------------------------------------------------------------------------
        comboRazonDev.setUI(new CustomUI());
        comboRazonDev.setBorder(BorderFactory.createLineBorder(new Color(71, 71, 71), 1));
        //-------------------Apartado inventario--------------------------------------------
        campoNomProdElim.setBackground(new Color(255,255,255,0));
        campoCatProdElim.setBackground(new Color(255,255,255,0));
        campoMarcaProdElim.setBackground(new Color(255,255,255,0));
        campoPrecioCProdElim.setBackground(new Color(255,255,255,0));
        campoPrecioVProdElim.setBackground(new Color(255,255,255,0));
        campoDispProdElim.setBackground(new Color(255,255,255,0));
        campoStockProdElim.setBackground(new Color(255,255,255,0));
        campoIDProElimEnc.setVisible(false);
        //-------------------Apartado ventas locales-----------------------------------------
        campoIDClienteFacL.setBackground(new Color(255,255,255,0));
        campoNomClienteFacL.setBackground(new Color(255,255,255,0));
        campoTelClienteFacL.setBackground(new Color(255,255,255,0));
        campoEdadClienteFacL.setBackground(new Color(255,255,255,0));
        campoRfcClienteFacL.setBackground(new Color(255,255,255,0));
        campoEmpClienteFacL.setBackground(new Color(255,255,255,0));
        campoDomClienteFacL.setBackground(new Color(255,255,255,0));
        campoMailClienteFacL.setBackground(new Color(255,255,255,0));
        campoFechaFacL.setBackground(new Color(255,255,255,0));
        campoTotalFacL.setBackground(new Color(255,255,255,0));
        campoIDFacturaL.setVisible(false);
        
        campoIDClienFacEncon.setBackground(new Color(255,255,255,0));
        campoNomClienNFac.setBackground(new Color(255,255,255,0));
        campoApeClienNFac.setBackground(new Color(255,255,255,0));
        campoMailClienNFac.setBackground(new Color(255,255,255,0));
        campoEdadClienNFac.setBackground(new Color(255,255,255,0));
        campoTelClienNFac.setBackground(new Color(255,255,255,0));
        campoRfcClienNFac.setBackground(new Color(255,255,255,0));
        campoEmpClienNFac.setBackground(new Color(255,255,255,0));
        campoDomClienNFac.setBackground(new Color(255,255,255,0));
        //-------------------Apartado ventas web-----------------------------------------
        campoIDClienteW.setBackground(new Color(255,255,255,0));
        campoNomClienteW.setBackground(new Color(255,255,255,0));
        campoTelClienteW.setBackground(new Color(255,255,255,0));
        campoEdadClienteW.setBackground(new Color(255,255,255,0));
        campoRfcClienteW.setBackground(new Color(255,255,255,0));
        campoEmClienteW.setBackground(new Color(255,255,255,0));
        campoDomClienteW.setBackground(new Color(255,255,255,0));
        campoMailClienteW.setBackground(new Color(255,255,255,0));
        campoTotalFacW.setBackground(new Color(255,255,255,0));
        campoIDFacWBuscado.setVisible(false);
        //-------------------Apartado devoluciones-----------------------------------------
        campoProvNDev.setBackground(new Color(255,255,255,0));
        campoFechaNDev.setBackground(new Color(255,255,255,0));
        campoFechaVenNDev.setBackground(new Color(255,255,255,0));
        campoDefectoNDev.setBackground(new Color(255,255,255,0));
        campoTotalVenNDev.setBackground(new Color(255,255,255,0));
        campoIDProvEn.setBackground(new Color(255,255,255,0));
        campoIDProvEn.setVisible(false);
        campoIDVentaEnDev.setBackground(new Color(255,255,255,0));
        campoIDVentaEnDev.setVisible(false);
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
        panelNuevaVenta.setVisible(false);
        panelNuevaVenta.setEnabled(false);
        
        panelNuevaFactura.setVisible(false);
        panelNuevaFactura.setEnabled(false);
        //-------------------------------------
        panelDevoluciones.setVisible(false);
        panelDevoluciones.setEnabled(false);
        
        panelNuevaDev.setVisible(false);
        panelNuevaDev.setEnabled(false);
        //-------------------------------------
        panelInventario.setVisible(false);
        panelInventario.setEnabled(false);
        
        panelElimProducto.setVisible(false);
        panelElimProducto.setEnabled(false);
        
        panelModiProducto.setVisible(false);
        panelModiProducto.setEnabled(false);
        
        panelRegistroElim.setVisible(false);
        panelRegistroElim.setEnabled(false);
        //-------------------------------------
        panelVentasL.setVisible(false);
        panelVentasL.setEnabled(false);
        
        panelFacturaL.setVisible(false);
        panelFacturaL.setEnabled(false);
        //-------------------------------------
        panelVentasW.setVisible(false);
        panelVentasW.setEnabled(false);
        
        panelFacturaW.setVisible(false);
        panelFacturaW.setEnabled(false);
        //-------------------------------------
        panelClientes.setVisible(false);
        panelClientes.setEnabled(false);
                
        panelModiCliente.setVisible(false);
        panelModiCliente.setEnabled(false);
        
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
        btnNuevaVenta = new javax.swing.JButton();
        btnDevolucion = new javax.swing.JButton();
        btnInventario = new javax.swing.JButton();
        btnVentaLocal = new javax.swing.JButton();
        btnVentaWeb = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnNotificaciones = new javax.swing.JButton();
        panelFechaHeader = new javax.swing.JPanel();
        fechaSistema = new javax.swing.JLabel();
        horaSistema = new javax.swing.JLabel();
        panelNuevaVenta = new javax.swing.JPanel();
        panelBtnProductos = new javax.swing.JPanel();
        btnModiCantAgregado = new javax.swing.JButton();
        btnElimProdAgregado = new javax.swing.JButton();
        btnAgregarProd = new javax.swing.JButton();
        panelAcCanFac = new javax.swing.JPanel();
        btnAcepNuevaVenta = new javax.swing.JButton();
        btnCanNuevaVenta = new javax.swing.JButton();
        btnNuevaFactura = new javax.swing.JButton();
        panelTablaDerecha = new javax.swing.JPanel();
        scrollProdAgregados = new javax.swing.JScrollPane();
        tablaProdAgregadosNV = new javax.swing.JTable();
        scrollProdBuscadosNV = new javax.swing.JScrollPane();
        tablaProdBuscadosNV = new javax.swing.JTable();
        iconBuscarProd = new javax.swing.JLabel();
        campoIDProdBuscado = new javax.swing.JTextField();
        campoCantProdBuscado = new javax.swing.JTextField();
        campoPrecioProd = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoTotalNVenta = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        campoPagaConNV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoCambioNV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboFormaNVenta = new javax.swing.JComboBox<>();
        fondoNuevaVenta = new javax.swing.JLabel();
        panelNuevaFactura = new javax.swing.JPanel();
        panelTablaProdNuevFacL = new javax.swing.JPanel();
        scrollProdNuevFacL = new javax.swing.JScrollPane();
        tablaProdNuevFacL = new javax.swing.JTable();
        campoIDClienFacBuscar = new javax.swing.JTextField();
        campoIDClienFacEncon = new javax.swing.JTextField();
        iconBuscarClienFac = new javax.swing.JLabel();
        campoNomClienNFac = new javax.swing.JTextField();
        campoApeClienNFac = new javax.swing.JTextField();
        campoMailClienNFac = new javax.swing.JTextField();
        campoTelClienNFac = new javax.swing.JTextField();
        campoRfcClienNFac = new javax.swing.JTextField();
        campoEmpClienNFac = new javax.swing.JTextField();
        campoDomClienNFac = new javax.swing.JTextField();
        campoFechaNFactura = new javax.swing.JTextField();
        iconBuscarVentaFac = new javax.swing.JLabel();
        campoIDVentaFacBuscar = new javax.swing.JTextField();
        campoTotalNFactura = new javax.swing.JTextField();
        comboFormaNFactura = new javax.swing.JComboBox<>();
        campoEdadClienNFac = new javax.swing.JTextField();
        campoIDVentaFacEncon = new javax.swing.JTextField();
        panelAcCanNueFactura = new javax.swing.JPanel();
        btnAcepNuevaFactura = new javax.swing.JButton();
        btnCanNuevaFactura = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        fondoNuevaFactura = new javax.swing.JLabel();
        panelDevoluciones = new javax.swing.JPanel();
        panelBtnNueDev = new javax.swing.JPanel();
        btnNuevaDev = new javax.swing.JButton();
        panelTablaDev = new javax.swing.JPanel();
        scrollDev = new javax.swing.JScrollPane();
        tablaDev = new javax.swing.JTable();
        campoIDDevBuscar = new javax.swing.JTextField();
        iconBuscarDev = new javax.swing.JLabel();
        comboOrdenarDev = new javax.swing.JComboBox<>();
        panelTablaProdSelecNDev1 = new javax.swing.JPanel();
        scrollProdSelecDev = new javax.swing.JScrollPane();
        tablaProdSelecDev = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        fondoDevoluciones = new javax.swing.JLabel();
        panelNuevaDev = new javax.swing.JPanel();
        panelTablaProdNDev = new javax.swing.JPanel();
        scrollProdNDev = new javax.swing.JScrollPane();
        tablaProdNDev = new javax.swing.JTable();
        campoIDVenDevBuscar = new javax.swing.JTextField();
        panelBtnBuscRecProdModi1 = new javax.swing.JPanel();
        btnBuscarNuevDev = new javax.swing.JButton();
        btnRecarNuevDev = new javax.swing.JButton();
        campoProvNDev = new javax.swing.JTextField();
        campoFechaNDev = new javax.swing.JTextField();
        comboRazonDev = new javax.swing.JComboBox<>();
        comboTipoNDev = new javax.swing.JComboBox<>();
        campoFechaVenNDev = new javax.swing.JTextField();
        campoTotalVenNDev = new javax.swing.JTextField();
        panelTablaProdSelecNDev = new javax.swing.JPanel();
        scrollProdSelecNDev = new javax.swing.JScrollPane();
        tablaProdSelecNDev = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        btnDevolverNDev = new javax.swing.JButton();
        campoDefectoNDev = new javax.swing.JTextField();
        campoCantDevolver = new javax.swing.JTextField();
        campoIDVentaEnDev = new javax.swing.JTextField();
        campoIDProvEn = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        fondoNuevaDev = new javax.swing.JLabel();
        panelInventario = new javax.swing.JPanel();
        panelBtnProduc = new javax.swing.JPanel();
        btnModiProducto = new javax.swing.JButton();
        btnElimProducto = new javax.swing.JButton();
        btnMostrarElim = new javax.swing.JButton();
        iconBuscarProdInven = new javax.swing.JLabel();
        comboOrdenarInventario = new javax.swing.JComboBox<>();
        panelTablaInventario = new javax.swing.JPanel();
        scrollInventario = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        campoIDProdInventario = new javax.swing.JTextField();
        fondoInventario = new javax.swing.JLabel();
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
        panelVentasL = new javax.swing.JPanel();
        panelBtnVerFacturaL = new javax.swing.JPanel();
        btnVerFacturaL = new javax.swing.JButton();
        panelTablaVentasL = new javax.swing.JPanel();
        scrollVentasL = new javax.swing.JScrollPane();
        tablaVentasL = new javax.swing.JTable();
        panelTablaProdVentaL = new javax.swing.JPanel();
        scrollProdVentasL = new javax.swing.JScrollPane();
        tablaProdVentasL = new javax.swing.JTable();
        campoIDVentaLlBuscar = new javax.swing.JTextField();
        iconBuscarVentaL = new javax.swing.JLabel();
        comboOrdenarVentasL = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btnMostrarVentaL = new javax.swing.JButton();
        fondoVentasL = new javax.swing.JLabel();
        panelFacturaL = new javax.swing.JPanel();
        campoIDClienteFacL = new javax.swing.JTextField();
        campoNomClienteFacL = new javax.swing.JTextField();
        campoTelClienteFacL = new javax.swing.JTextField();
        campoEdadClienteFacL = new javax.swing.JTextField();
        campoRfcClienteFacL = new javax.swing.JTextField();
        campoEmpClienteFacL = new javax.swing.JTextField();
        campoDomClienteFacL = new javax.swing.JTextField();
        campoMailClienteFacL = new javax.swing.JTextField();
        panelTablaProdFactura = new javax.swing.JPanel();
        scrollProdFactura = new javax.swing.JScrollPane();
        tablaProdFacturaL = new javax.swing.JTable();
        comboFormaFacL = new javax.swing.JComboBox<>();
        campoFechaFacL = new javax.swing.JTextField();
        campoTotalFacL = new javax.swing.JTextField();
        panelBtnElimFacL = new javax.swing.JPanel();
        btnElimFacturaL = new javax.swing.JButton();
        panelAcCanFacFacL = new javax.swing.JPanel();
        btnAceptarFacL = new javax.swing.JButton();
        campoIDFacturaL = new javax.swing.JTextField();
        fondoFacturaL = new javax.swing.JLabel();
        panelVentasW = new javax.swing.JPanel();
        panelVerFacturaW = new javax.swing.JPanel();
        btnVerFacturaW = new javax.swing.JButton();
        panelBtnMostrarVentaW = new javax.swing.JPanel();
        btnMostrarVentaW = new javax.swing.JButton();
        panelBtnMarcarVentaW = new javax.swing.JPanel();
        btnMarcarVentaW = new javax.swing.JButton();
        panelTablaVentasW = new javax.swing.JPanel();
        scrollVentasW = new javax.swing.JScrollPane();
        tablaVentasW = new javax.swing.JTable();
        panelTablaClienteVentaW = new javax.swing.JPanel();
        scrollClienteVentaW = new javax.swing.JScrollPane();
        tablaClienteVentaW = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scrollProdVentaW = new javax.swing.JScrollPane();
        tablaProdVentaW = new javax.swing.JTable();
        campoIDVentaWBuscar = new javax.swing.JTextField();
        comboOrdenarVentasW = new javax.swing.JComboBox<>();
        iconBuscarVentaW = new javax.swing.JLabel();
        fondoVentasW = new javax.swing.JLabel();
        panelFacturaW = new javax.swing.JPanel();
        panelTabProdFacW = new javax.swing.JPanel();
        scrollProdFacW = new javax.swing.JScrollPane();
        tablaProdFacW = new javax.swing.JTable();
        campoIDClienteW = new javax.swing.JTextField();
        campoNomClienteW = new javax.swing.JTextField();
        campoTelClienteW = new javax.swing.JTextField();
        campoEdadClienteW = new javax.swing.JTextField();
        campoRfcClienteW = new javax.swing.JTextField();
        campoEmClienteW = new javax.swing.JTextField();
        campoDomClienteW = new javax.swing.JTextField();
        campoMailClienteW = new javax.swing.JTextField();
        campoEntregaFacW = new javax.swing.JTextField();
        campoTotalFacW = new javax.swing.JTextField();
        comboEstadoFacW = new javax.swing.JComboBox<>();
        comboFormaFacW = new javax.swing.JComboBox<>();
        campoIDFacWBuscado = new javax.swing.JTextField();
        panelAcFacW = new javax.swing.JPanel();
        btnAceptarFacW = new javax.swing.JButton();
        panelBtnElimFacW = new javax.swing.JPanel();
        btnElimFacturaW = new javax.swing.JButton();
        fondoFacturaW = new javax.swing.JLabel();
        panelClientes = new javax.swing.JPanel();
        panelBtnClientes = new javax.swing.JPanel();
        btnElimCliente = new javax.swing.JButton();
        btnModiCliente = new javax.swing.JButton();
        panelTablaClientes = new javax.swing.JPanel();
        scrollClientes = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        campoIDClienteBuscar = new javax.swing.JTextField();
        iconBuscarCliente = new javax.swing.JLabel();
        comboOrdenarClientes = new javax.swing.JComboBox<>();
        fondoClientes = new javax.swing.JLabel();
        panelModiCliente = new javax.swing.JPanel();
        panelBtnModiClien = new javax.swing.JPanel();
        btnModiClienteBD = new javax.swing.JButton();
        btnCancelarClienModi = new javax.swing.JButton();
        panelBtnBuscRecClienModi = new javax.swing.JPanel();
        btnBuscarClienModi = new javax.swing.JButton();
        btnRecarClienModi = new javax.swing.JButton();
        campoIDClienteModi = new javax.swing.JTextField();
        campoIDClienEncon = new javax.swing.JTextField();
        campoNomClienteModi = new javax.swing.JTextField();
        campoApeClienteModi = new javax.swing.JTextField();
        campoEdadClienteModi = new javax.swing.JTextField();
        campoTelClienteModi = new javax.swing.JTextField();
        campoRfcClienteModi = new javax.swing.JTextField();
        campoDomClienteModi = new javax.swing.JTextField();
        campoEmClienteModi = new javax.swing.JTextField();
        campoMailClienteModi = new javax.swing.JTextField();
        fondoModiCliente = new javax.swing.JLabel();
        panelNotificaciones = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        panelTablaNotificaciones = new javax.swing.JPanel();
        scrollNotificacion = new javax.swing.JScrollPane();
        tablaNotificacion = new javax.swing.JTable();
        panelTablaAlertas = new javax.swing.JPanel();
        scrollAlertas = new javax.swing.JScrollPane();
        tablaAlertas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        fondoNotificaciones = new javax.swing.JLabel();
        fondoVentas = new javax.swing.JLabel();
        headerVentas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        panelRaiz.setBackground(new java.awt.Color(255, 255, 255));
        panelRaiz.setPreferredSize(new java.awt.Dimension(1230, 675));
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

        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNuevaVentaNS.png"))); // NOI18N
        btnNuevaVenta.setBorder(null);
        btnNuevaVenta.setBorderPainted(false);
        btnNuevaVenta.setContentAreaFilled(false);
        btnNuevaVenta.setFocusPainted(false);
        btnNuevaVenta.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNuevaVentaS.png"))); // NOI18N
        btnNuevaVenta.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuNuevaVentaNS.png"))); // NOI18N
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        btnDevolucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuDevNS.png"))); // NOI18N
        btnDevolucion.setBorder(null);
        btnDevolucion.setBorderPainted(false);
        btnDevolucion.setContentAreaFilled(false);
        btnDevolucion.setFocusPainted(false);
        btnDevolucion.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuDevS.png"))); // NOI18N
        btnDevolucion.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuDevNS.png"))); // NOI18N
        btnDevolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolucionActionPerformed(evt);
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

        btnVentaLocal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasLNS.png"))); // NOI18N
        btnVentaLocal.setBorder(null);
        btnVentaLocal.setBorderPainted(false);
        btnVentaLocal.setContentAreaFilled(false);
        btnVentaLocal.setFocusPainted(false);
        btnVentaLocal.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasLS.png"))); // NOI18N
        btnVentaLocal.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasLNS.png"))); // NOI18N
        btnVentaLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaLocalActionPerformed(evt);
            }
        });

        btnVentaWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasWNS.png"))); // NOI18N
        btnVentaWeb.setBorder(null);
        btnVentaWeb.setBorderPainted(false);
        btnVentaWeb.setContentAreaFilled(false);
        btnVentaWeb.setEnabled(false);
        btnVentaWeb.setFocusPainted(false);
        btnVentaWeb.setFocusable(false);
        btnVentaWeb.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasWS.png"))); // NOI18N
        btnVentaWeb.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuVentasWNS.png"))); // NOI18N
        btnVentaWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaWebActionPerformed(evt);
            }
        });

        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuClientesNS.png"))); // NOI18N
        btnClientes.setBorder(null);
        btnClientes.setBorderPainted(false);
        btnClientes.setContentAreaFilled(false);
        btnClientes.setFocusPainted(false);
        btnClientes.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuClientesS.png"))); // NOI18N
        btnClientes.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/menuClientesNS.png"))); // NOI18N
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
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
                    .addComponent(btnNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVentaLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVentaWeb, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevaVenta)
                .addGap(18, 18, 18)
                .addComponent(btnDevolucion)
                .addGap(18, 18, 18)
                .addComponent(btnInventario)
                .addGap(18, 18, 18)
                .addComponent(btnVentaLocal)
                .addGap(18, 18, 18)
                .addComponent(btnClientes)
                .addGap(18, 18, 18)
                .addComponent(btnNotificaciones)
                .addGap(73, 73, 73)
                .addComponent(btnVentaWeb)
                .addContainerGap(38, Short.MAX_VALUE))
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

        panelNuevaVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnProductos.setBackground(new java.awt.Color(255, 255, 255));

        btnModiCantAgregado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconEditar.png"))); // NOI18N
        btnModiCantAgregado.setToolTipText("Cambiar Cantidad");
        btnModiCantAgregado.setBorder(null);
        btnModiCantAgregado.setBorderPainted(false);
        btnModiCantAgregado.setContentAreaFilled(false);
        btnModiCantAgregado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModiCantAgregado.setFocusPainted(false);
        btnModiCantAgregado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiCantAgregadoActionPerformed(evt);
            }
        });

        btnElimProdAgregado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBasura.png"))); // NOI18N
        btnElimProdAgregado.setToolTipText("Eliminar Producto");
        btnElimProdAgregado.setBorder(null);
        btnElimProdAgregado.setBorderPainted(false);
        btnElimProdAgregado.setContentAreaFilled(false);
        btnElimProdAgregado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnElimProdAgregado.setFocusPainted(false);
        btnElimProdAgregado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimProdAgregadoActionPerformed(evt);
            }
        });

        btnAgregarProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconAgregar.png"))); // NOI18N
        btnAgregarProd.setToolTipText("Agregar Producto");
        btnAgregarProd.setBorder(null);
        btnAgregarProd.setBorderPainted(false);
        btnAgregarProd.setContentAreaFilled(false);
        btnAgregarProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProd.setFocusPainted(false);
        btnAgregarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnProductosLayout = new javax.swing.GroupLayout(panelBtnProductos);
        panelBtnProductos.setLayout(panelBtnProductosLayout);
        panelBtnProductosLayout.setHorizontalGroup(
            panelBtnProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnModiCantAgregado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnElimProdAgregado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnAgregarProd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBtnProductosLayout.setVerticalGroup(
            panelBtnProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnProductosLayout.createSequentialGroup()
                .addGroup(panelBtnProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnModiCantAgregado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnElimProdAgregado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarProd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelNuevaVenta.add(panelBtnProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 550, 260, 60));

        panelAcCanFac.setBackground(new java.awt.Color(249, 251, 242));

        btnAcepNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnAcepNuevaVenta.setToolTipText("Confirmar Venta");
        btnAcepNuevaVenta.setBorder(null);
        btnAcepNuevaVenta.setBorderPainted(false);
        btnAcepNuevaVenta.setContentAreaFilled(false);
        btnAcepNuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAcepNuevaVenta.setFocusPainted(false);
        btnAcepNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcepNuevaVentaActionPerformed(evt);
            }
        });

        btnCanNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnXCancelar.png"))); // NOI18N
        btnCanNuevaVenta.setToolTipText("Cancelar Venta");
        btnCanNuevaVenta.setBorder(null);
        btnCanNuevaVenta.setBorderPainted(false);
        btnCanNuevaVenta.setContentAreaFilled(false);
        btnCanNuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanNuevaVenta.setFocusPainted(false);
        btnCanNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanNuevaVentaActionPerformed(evt);
            }
        });

        btnNuevaFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconFacturar.png"))); // NOI18N
        btnNuevaFactura.setToolTipText("Nueva Factura");
        btnNuevaFactura.setBorder(null);
        btnNuevaFactura.setBorderPainted(false);
        btnNuevaFactura.setContentAreaFilled(false);
        btnNuevaFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevaFactura.setFocusPainted(false);
        btnNuevaFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcCanFacLayout = new javax.swing.GroupLayout(panelAcCanFac);
        panelAcCanFac.setLayout(panelAcCanFacLayout);
        panelAcCanFacLayout.setHorizontalGroup(
            panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanFacLayout.createSequentialGroup()
                .addGroup(panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAcepNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCanNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAcCanFacLayout.setVerticalGroup(
            panelAcCanFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanFacLayout.createSequentialGroup()
                .addComponent(btnAcepNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCanNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(btnNuevaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNuevaVenta.add(panelAcCanFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 400, 60, 210));

        panelTablaDerecha.setBackground(new java.awt.Color(255, 237, 225));

        scrollProdAgregados.setBorder(null);

        tablaProdAgregadosNV = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdAgregadosNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio"
            }
        ));
        tablaProdAgregadosNV.getTableHeader().setReorderingAllowed(false);
        tablaProdAgregadosNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProdAgregadosNVMouseClicked(evt);
            }
        });
        scrollProdAgregados.setViewportView(tablaProdAgregadosNV);

        javax.swing.GroupLayout panelTablaDerechaLayout = new javax.swing.GroupLayout(panelTablaDerecha);
        panelTablaDerecha.setLayout(panelTablaDerechaLayout);
        panelTablaDerechaLayout.setHorizontalGroup(
            panelTablaDerechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaDerechaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdAgregados, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaDerechaLayout.setVerticalGroup(
            panelTablaDerechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaDerechaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdAgregados, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNuevaVenta.add(panelTablaDerecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 140, 630, 200));

        scrollProdBuscadosNV.setBackground(new java.awt.Color(255, 237, 225));
        scrollProdBuscadosNV.setBorder(null);

        tablaProdBuscadosNV = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdBuscadosNV.setBackground(new java.awt.Color(255, 237, 225));
        tablaProdBuscadosNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Precio", "Stock"
            }
        ));
        tablaProdBuscadosNV.setGridColor(new java.awt.Color(102, 102, 102));
        tablaProdBuscadosNV.getTableHeader().setReorderingAllowed(false);
        tablaProdBuscadosNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProdBuscadosNVMouseClicked(evt);
            }
        });
        scrollProdBuscadosNV.setViewportView(tablaProdBuscadosNV);

        panelNuevaVenta.add(scrollProdBuscadosNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 250, 120));

        iconBuscarProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarProdMouseClicked(evt);
            }
        });
        panelNuevaVenta.add(iconBuscarProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 22, 22));

        campoIDProdBuscado.setBorder(null);
        campoIDProdBuscado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdBuscadoKeyTyped(evt);
            }
        });
        panelNuevaVenta.add(campoIDProdBuscado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 220, 22));

        campoCantProdBuscado.setText("0");
        campoCantProdBuscado.setBorder(null);
        campoCantProdBuscado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoCantProdBuscadoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoCantProdBuscadoKeyTyped(evt);
            }
        });
        panelNuevaVenta.add(campoCantProdBuscado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, 250, -1));

        campoPrecioProd.setEditable(false);
        campoPrecioProd.setBackground(new java.awt.Color(255, 255, 255));
        campoPrecioProd.setText("0.0");
        campoPrecioProd.setBorder(null);
        panelNuevaVenta.add(campoPrecioProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 240, -1));

        jLabel4.setText("$");
        panelNuevaVenta.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 490, 10, -1));

        campoTotalNVenta.setEditable(false);
        campoTotalNVenta.setBackground(new java.awt.Color(255, 255, 255));
        campoTotalNVenta.setText("0.0");
        campoTotalNVenta.setBorder(null);
        panelNuevaVenta.add(campoTotalNVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 440, 210, -1));

        jLabel1.setText("$");
        panelNuevaVenta.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 440, 10, -1));

        campoPagaConNV.setBorder(null);
        campoPagaConNV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPagaConNVKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPagaConNVKeyTyped(evt);
            }
        });
        panelNuevaVenta.add(campoPagaConNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 520, 80, -1));

        jLabel2.setText("$");
        panelNuevaVenta.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 520, 10, -1));

        campoCambioNV.setEditable(false);
        campoCambioNV.setBackground(new java.awt.Color(255, 255, 255));
        campoCambioNV.setText("0.0");
        campoCambioNV.setBorder(null);
        panelNuevaVenta.add(campoCambioNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 520, 80, -1));

        jLabel3.setText("$");
        panelNuevaVenta.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 520, 10, -1));

        comboFormaNVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "PayPal" }));
        panelNuevaVenta.add(comboFormaNVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(639, 570, 110, 30));

        fondoNuevaVenta.setBackground(new java.awt.Color(255, 255, 255));
        fondoNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevaVenta.png"))); // NOI18N
        panelNuevaVenta.add(fondoNuevaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNuevaFactura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTablaProdNuevFacL.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdNuevFacL = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdNuevFacL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario"
            }
        ));
        tablaProdNuevFacL.getTableHeader().setReorderingAllowed(false);
        scrollProdNuevFacL.setViewportView(tablaProdNuevFacL);

        javax.swing.GroupLayout panelTablaProdNuevFacLLayout = new javax.swing.GroupLayout(panelTablaProdNuevFacL);
        panelTablaProdNuevFacL.setLayout(panelTablaProdNuevFacLLayout);
        panelTablaProdNuevFacLLayout.setHorizontalGroup(
            panelTablaProdNuevFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdNuevFacLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNuevFacL, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdNuevFacLLayout.setVerticalGroup(
            panelTablaProdNuevFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdNuevFacLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNuevFacL, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNuevaFactura.add(panelTablaProdNuevFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, 580, 200));

        campoIDClienFacBuscar.setBorder(null);
        campoIDClienFacBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDClienFacBuscarKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoIDClienFacBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 220, -1));

        campoIDClienFacEncon.setEditable(false);
        campoIDClienFacEncon.setEnabled(false);
        panelNuevaFactura.add(campoIDClienFacEncon, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 60, -1));

        iconBuscarClienFac.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarClienFac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarClienFac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarClienFac.setOpaque(true);
        iconBuscarClienFac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarClienFacMouseClicked(evt);
            }
        });
        panelNuevaFactura.add(iconBuscarClienFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 152, 20, 30));

        campoNomClienNFac.setBorder(null);
        campoNomClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNomClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoNomClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 260, -1));

        campoApeClienNFac.setBorder(null);
        campoApeClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoApeClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoApeClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 281, 260, 20));

        campoMailClienNFac.setBorder(null);
        campoMailClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMailClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoMailClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 260, 20));

        campoTelClienNFac.setBorder(null);
        campoTelClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoTelClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoTelClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 398, 143, 20));

        campoRfcClienNFac.setBorder(null);
        campoRfcClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoRfcClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoRfcClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 453, 260, 20));

        campoEmpClienNFac.setBorder(null);
        campoEmpClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoEmpClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoEmpClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 521, 260, 20));

        campoDomClienNFac.setBorder(null);
        campoDomClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDomClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoDomClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 588, 260, 20));

        campoFechaNFactura.setEditable(false);
        campoFechaNFactura.setBackground(new java.awt.Color(255, 255, 255));
        campoFechaNFactura.setBorder(null);
        panelNuevaFactura.add(campoFechaNFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 503, 220, 20));

        iconBuscarVentaFac.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarVentaFac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarVentaFac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarVentaFac.setOpaque(true);
        iconBuscarVentaFac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarVentaFacMouseClicked(evt);
            }
        });
        panelNuevaFactura.add(iconBuscarVentaFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(846, 110, 22, 30));

        campoIDVentaFacBuscar.setBorder(null);
        campoIDVentaFacBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDVentaFacBuscarKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoIDVentaFacBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 114, 130, 20));

        campoTotalNFactura.setEditable(false);
        campoTotalNFactura.setBorder(null);
        panelNuevaFactura.add(campoTotalNFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 574, 120, 20));

        comboFormaNFactura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "PayPal" }));
        panelNuevaFactura.add(comboFormaNFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 430, 110, 30));

        campoEdadClienNFac.setBorder(null);
        campoEdadClienNFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoEdadClienNFacKeyTyped(evt);
            }
        });
        panelNuevaFactura.add(campoEdadClienNFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 398, 60, 20));

        campoIDVentaFacEncon.setEditable(false);
        campoIDVentaFacEncon.setEnabled(false);
        panelNuevaFactura.add(campoIDVentaFacEncon, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 70, -1));

        panelAcCanNueFactura.setBackground(new java.awt.Color(249, 251, 242));

        btnAcepNuevaFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnAcepNuevaFactura.setToolTipText("Agregar Factura");
        btnAcepNuevaFactura.setBorder(null);
        btnAcepNuevaFactura.setBorderPainted(false);
        btnAcepNuevaFactura.setContentAreaFilled(false);
        btnAcepNuevaFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAcepNuevaFactura.setFocusPainted(false);
        btnAcepNuevaFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcepNuevaFacturaActionPerformed(evt);
            }
        });

        btnCanNuevaFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnXCancelar.png"))); // NOI18N
        btnCanNuevaFactura.setToolTipText("Cancelar Factura");
        btnCanNuevaFactura.setBorder(null);
        btnCanNuevaFactura.setBorderPainted(false);
        btnCanNuevaFactura.setContentAreaFilled(false);
        btnCanNuevaFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanNuevaFactura.setFocusPainted(false);
        btnCanNuevaFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanNuevaFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcCanNueFacturaLayout = new javax.swing.GroupLayout(panelAcCanNueFactura);
        panelAcCanNueFactura.setLayout(panelAcCanNueFacturaLayout);
        panelAcCanNueFacturaLayout.setHorizontalGroup(
            panelAcCanNueFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAcCanNueFacturaLayout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(panelAcCanNueFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCanNuevaFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAcepNuevaFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelAcCanNueFacturaLayout.setVerticalGroup(
            panelAcCanNueFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanNueFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAcepNuevaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnCanNuevaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelNuevaFactura.add(panelAcCanNueFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 440, 65, 160));

        jLabel5.setText("$");
        panelNuevaFactura.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 574, 10, 20));

        fondoNuevaFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevaFactura.png"))); // NOI18N
        panelNuevaFactura.add(fondoNuevaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelDevoluciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnNueDev.setBackground(new java.awt.Color(255, 255, 255));

        btnNuevaDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnNuevaDevNS.png"))); // NOI18N
        btnNuevaDev.setBorder(null);
        btnNuevaDev.setBorderPainted(false);
        btnNuevaDev.setContentAreaFilled(false);
        btnNuevaDev.setFocusPainted(false);
        btnNuevaDev.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnNuevaDevS.png"))); // NOI18N
        btnNuevaDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaDevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnNueDevLayout = new javax.swing.GroupLayout(panelBtnNueDev);
        panelBtnNueDev.setLayout(panelBtnNueDevLayout);
        panelBtnNueDevLayout.setHorizontalGroup(
            panelBtnNueDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevaDev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBtnNueDevLayout.setVerticalGroup(
            panelBtnNueDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevaDev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelDevoluciones.add(panelBtnNueDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 440, 207, 58));

        panelTablaDev.setBackground(new java.awt.Color(255, 237, 225));

        tablaDev = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaDev.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Devolucion", "Venta", "Producto", "Proveedor", "Fecha", "Defecto", "Tipo"
            }
        ));
        tablaDev.getTableHeader().setReorderingAllowed(false);
        tablaDev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDevMouseClicked(evt);
            }
        });
        scrollDev.setViewportView(tablaDev);

        javax.swing.GroupLayout panelTablaDevLayout = new javax.swing.GroupLayout(panelTablaDev);
        panelTablaDev.setLayout(panelTablaDevLayout);
        panelTablaDevLayout.setHorizontalGroup(
            panelTablaDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollDev, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaDevLayout.setVerticalGroup(
            panelTablaDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollDev, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelDevoluciones.add(panelTablaDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 660, 350));

        campoIDDevBuscar.setBorder(null);
        campoIDDevBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDDevBuscarKeyTyped(evt);
            }
        });
        panelDevoluciones.add(campoIDDevBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 190, 220, 20));

        iconBuscarDev.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarDev.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarDev.setOpaque(true);
        iconBuscarDev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarDevMouseClicked(evt);
            }
        });
        panelDevoluciones.add(iconBuscarDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(972, 190, 24, 20));

        comboOrdenarDev.setForeground(new java.awt.Color(255, 255, 255));
        comboOrdenarDev.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Devolucion", "Fecha descendente", "Fecha ascendente" }));
        comboOrdenarDev.setBorder(null);
        comboOrdenarDev.setPreferredSize(new java.awt.Dimension(200, 25));
        comboOrdenarDev.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarDevItemStateChanged(evt);
            }
        });
        panelDevoluciones.add(comboOrdenarDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 250, 170, -1));

        panelTablaProdSelecNDev1.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdSelecDev = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdSelecDev.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Proveedor", "Producto", "Cantidad", "Precio Unitario"
            }
        ));
        tablaProdSelecDev.getTableHeader().setReorderingAllowed(false);
        scrollProdSelecDev.setViewportView(tablaProdSelecDev);

        javax.swing.GroupLayout panelTablaProdSelecNDev1Layout = new javax.swing.GroupLayout(panelTablaProdSelecNDev1);
        panelTablaProdSelecNDev1.setLayout(panelTablaProdSelecNDev1Layout);
        panelTablaProdSelecNDev1Layout.setHorizontalGroup(
            panelTablaProdSelecNDev1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdSelecNDev1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdSelecDev, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdSelecNDev1Layout.setVerticalGroup(
            panelTablaProdSelecNDev1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaProdSelecNDev1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdSelecDev, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelDevoluciones.add(panelTablaProdSelecNDev1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 660, 70));

        jLabel9.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(36, 36, 37));
        jLabel9.setText("Producto seleccionado:");
        panelDevoluciones.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 510, -1, -1));

        fondoDevoluciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoDevoluciones.png"))); // NOI18N
        panelDevoluciones.add(fondoDevoluciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelDevoluciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNuevaDev.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTablaProdNDev.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdNDev = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdNDev.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario", "Estado"
            }
        ));
        tablaProdNDev.getTableHeader().setReorderingAllowed(false);
        tablaProdNDev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProdNDevMouseClicked(evt);
            }
        });
        scrollProdNDev.setViewportView(tablaProdNDev);

        javax.swing.GroupLayout panelTablaProdNDevLayout = new javax.swing.GroupLayout(panelTablaProdNDev);
        panelTablaProdNDev.setLayout(panelTablaProdNDevLayout);
        panelTablaProdNDevLayout.setHorizontalGroup(
            panelTablaProdNDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdNDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNDev, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdNDevLayout.setVerticalGroup(
            panelTablaProdNDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaProdNDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdNDev, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNuevaDev.add(panelTablaProdNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 565, 160));

        campoIDVenDevBuscar.setBorder(null);
        campoIDVenDevBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDVenDevBuscarKeyTyped(evt);
            }
        });
        panelNuevaDev.add(campoIDVenDevBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 212, 230, 20));

        panelBtnBuscRecProdModi1.setBackground(new java.awt.Color(255, 255, 255));

        btnBuscarNuevDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnBuscarNuevDev.setToolTipText("Buscar");
        btnBuscarNuevDev.setBorder(null);
        btnBuscarNuevDev.setBorderPainted(false);
        btnBuscarNuevDev.setContentAreaFilled(false);
        btnBuscarNuevDev.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarNuevDev.setFocusPainted(false);
        btnBuscarNuevDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarNuevDevActionPerformed(evt);
            }
        });

        btnRecarNuevDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconRecargar.png"))); // NOI18N
        btnRecarNuevDev.setToolTipText("Limpiar Campos");
        btnRecarNuevDev.setBorder(null);
        btnRecarNuevDev.setBorderPainted(false);
        btnRecarNuevDev.setContentAreaFilled(false);
        btnRecarNuevDev.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecarNuevDev.setFocusPainted(false);
        btnRecarNuevDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecarNuevDevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnBuscRecProdModi1Layout = new javax.swing.GroupLayout(panelBtnBuscRecProdModi1);
        panelBtnBuscRecProdModi1.setLayout(panelBtnBuscRecProdModi1Layout);
        panelBtnBuscRecProdModi1Layout.setHorizontalGroup(
            panelBtnBuscRecProdModi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnBuscRecProdModi1Layout.createSequentialGroup()
                .addComponent(btnRecarNuevDev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(btnBuscarNuevDev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBtnBuscRecProdModi1Layout.setVerticalGroup(
            panelBtnBuscRecProdModi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnBuscRecProdModi1Layout.createSequentialGroup()
                .addGroup(panelBtnBuscRecProdModi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarNuevDev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecarNuevDev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelNuevaDev.add(panelBtnBuscRecProdModi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 242, 60));

        campoProvNDev.setEditable(false);
        campoProvNDev.setBorder(null);
        panelNuevaDev.add(campoProvNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 451, 220, 20));

        campoFechaNDev.setEditable(false);
        campoFechaNDev.setBorder(null);
        panelNuevaDev.add(campoFechaNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 560, 220, -1));

        comboRazonDev.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dañado", "No deseado" }));
        panelNuevaDev.add(comboRazonDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 620, 80, 25));

        comboTipoNDev.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Cambio" }));
        panelNuevaDev.add(comboTipoNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 600, 90, 25));

        campoFechaVenNDev.setBorder(null);
        campoFechaVenNDev.setEnabled(false);
        panelNuevaDev.add(campoFechaVenNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 358, 120, 20));

        campoTotalVenNDev.setBorder(null);
        campoTotalVenNDev.setEnabled(false);
        panelNuevaDev.add(campoTotalVenNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(728, 358, 120, 20));

        panelTablaProdSelecNDev.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdSelecNDev.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario", "Estado"
            }
        ));
        tablaProdSelecNDev.getTableHeader().setReorderingAllowed(false);
        scrollProdSelecNDev.setViewportView(tablaProdSelecNDev);
        if (tablaProdSelecNDev.getColumnModel().getColumnCount() > 0) {
            tablaProdSelecNDev.getColumnModel().getColumn(4).setHeaderValue("Estado");
        }

        javax.swing.GroupLayout panelTablaProdSelecNDevLayout = new javax.swing.GroupLayout(panelTablaProdSelecNDev);
        panelTablaProdSelecNDev.setLayout(panelTablaProdSelecNDevLayout);
        panelTablaProdSelecNDevLayout.setHorizontalGroup(
            panelTablaProdSelecNDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdSelecNDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdSelecNDev, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdSelecNDevLayout.setVerticalGroup(
            panelTablaProdSelecNDevLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaProdSelecNDevLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdSelecNDev, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNuevaDev.add(panelTablaProdSelecNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 480, 580, 70));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        btnDevolverNDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnDevolverPNS.png"))); // NOI18N
        btnDevolverNDev.setBorder(null);
        btnDevolverNDev.setBorderPainted(false);
        btnDevolverNDev.setContentAreaFilled(false);
        btnDevolverNDev.setFocusPainted(false);
        btnDevolverNDev.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnDevolverPS.png"))); // NOI18N
        btnDevolverNDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverNDevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnDevolverNDev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnDevolverNDev, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );

        panelNuevaDev.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(778, 588, 210, 57));

        campoDefectoNDev.setEditable(false);
        campoDefectoNDev.setBorder(null);
        panelNuevaDev.add(campoDefectoNDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 503, 220, 20));

        campoCantDevolver.setBorder(null);
        campoCantDevolver.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoCantDevolverKeyTyped(evt);
            }
        });
        panelNuevaDev.add(campoCantDevolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 610, 240, 20));

        campoIDVentaEnDev.setEnabled(false);
        panelNuevaDev.add(campoIDVentaEnDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, -1, -1));

        campoIDProvEn.setEditable(false);
        campoIDProvEn.setEnabled(false);
        panelNuevaDev.add(campoIDProvEn, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 450, -1, -1));

        jLabel8.setBackground(new java.awt.Color(231, 231, 231));
        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(171, 171, 171));
        jLabel8.setText("Razón");
        panelNuevaDev.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 630, -1, -1));

        fondoNuevaDev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNuevaDev.png"))); // NOI18N
        panelNuevaDev.add(fondoNuevaDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNuevaDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

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

        javax.swing.GroupLayout panelBtnProducLayout = new javax.swing.GroupLayout(panelBtnProduc);
        panelBtnProduc.setLayout(panelBtnProducLayout);
        panelBtnProducLayout.setHorizontalGroup(
            panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnProducLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnModiProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(btnMostrarElim, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(btnElimProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        panelBtnProducLayout.setVerticalGroup(
            panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnProducLayout.createSequentialGroup()
                .addGroup(panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBtnProducLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBtnProducLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnModiProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnElimProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBtnProducLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnMostrarElim)))
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
        if (tablaInventario.getColumnModel().getColumnCount() > 0) {
            tablaInventario.getColumnModel().getColumn(4).setHeaderValue("Precio_Compra");
            tablaInventario.getColumnModel().getColumn(5).setHeaderValue("Precio_Venta");
            tablaInventario.getColumnModel().getColumn(6).setHeaderValue("Disponibilidad");
            tablaInventario.getColumnModel().getColumn(7).setHeaderValue("Stock");
            tablaInventario.getColumnModel().getColumn(8).setHeaderValue("Límite");
        }

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
        campoIDProdInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdInventarioKeyTyped(evt);
            }
        });
        panelInventario.add(campoIDProdInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 120, 144, 20));

        fondoInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoInventario.png"))); // NOI18N
        panelInventario.add(fondoInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelModiProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDProdModi.setBorder(null);
        campoIDProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoIDProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 310, 143, 20));

        campoNomProdModi.setBorder(null);
        campoNomProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNomProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoNomProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 154, 220, 20));

        comboCatProdModi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Artículos de sobre mesa", "Artículos de escritura", "Papeles y etiquetas", "Material de embalaje", "Organización personal", "Manipulados de papel", "Manualidades y bellas artes", "Material escolar especializado" }));
        panelModiProducto.add(comboCatProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 230, -1));

        campoPrCProdModi.setBorder(null);
        campoPrCProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPrCProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoPrCProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 367, 220, 20));

        campoPrVProdModi.setBorder(null);
        campoPrVProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPrVProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoPrVProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 445, 220, 20));

        comboDisProdModi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "D", "ND" }));
        panelModiProducto.add(comboDisProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 520, 110, -1));

        campoStockProdModi.setEditable(false);
        campoStockProdModi.setBackground(new java.awt.Color(255, 255, 255));
        campoStockProdModi.setBorder(null);
        panelModiProducto.add(campoStockProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 581, 73, 20));

        campoLimiteProdModi.setBorder(null);
        campoLimiteProdModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoLimiteProdModiKeyTyped(evt);
            }
        });
        panelModiProducto.add(campoLimiteProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(599, 581, 80, 20));

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
        panelModiProducto.add(campoMarcaProdModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 303, 220, 20));

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
        panelElimProducto.add(campoIDProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 310, 140, 20));

        campoNomProdElim.setEditable(false);
        campoNomProdElim.setBorder(null);
        panelElimProducto.add(campoNomProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 154, 230, 20));

        campoCatProdElim.setEditable(false);
        campoCatProdElim.setBorder(null);
        panelElimProducto.add(campoCatProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 224, 220, 20));

        campoMarcaProdElim.setEditable(false);
        campoMarcaProdElim.setBorder(null);
        panelElimProducto.add(campoMarcaProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 220, 30));

        campoPrecioCProdElim.setEditable(false);
        campoPrecioCProdElim.setBorder(null);
        panelElimProducto.add(campoPrecioCProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 367, 220, 20));

        campoPrecioVProdElim.setEditable(false);
        campoPrecioVProdElim.setBorder(null);
        panelElimProducto.add(campoPrecioVProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 440, 220, 30));

        campoDispProdElim.setEditable(false);
        campoDispProdElim.setBorder(null);
        panelElimProducto.add(campoDispProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 517, 220, 20));

        campoStockProdElim.setEditable(false);
        campoStockProdElim.setBorder(null);
        panelElimProducto.add(campoStockProdElim, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 581, 220, 20));

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

        panelVentasL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnVerFacturaL.setBackground(new java.awt.Color(255, 255, 255));

        btnVerFacturaL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnVerFacturaLNS.png"))); // NOI18N
        btnVerFacturaL.setBorder(null);
        btnVerFacturaL.setBorderPainted(false);
        btnVerFacturaL.setContentAreaFilled(false);
        btnVerFacturaL.setFocusPainted(false);
        btnVerFacturaL.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnVerFacturaLS.png"))); // NOI18N
        btnVerFacturaL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerFacturaLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnVerFacturaLLayout = new javax.swing.GroupLayout(panelBtnVerFacturaL);
        panelBtnVerFacturaL.setLayout(panelBtnVerFacturaLLayout);
        panelBtnVerFacturaLLayout.setHorizontalGroup(
            panelBtnVerFacturaLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnVerFacturaLLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnVerFacturaL))
        );
        panelBtnVerFacturaLLayout.setVerticalGroup(
            panelBtnVerFacturaLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnVerFacturaLLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(btnVerFacturaL))
        );

        panelVentasL.add(panelBtnVerFacturaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 510, 210, 70));

        panelTablaVentasL.setBackground(new java.awt.Color(255, 237, 225));

        tablaVentasL = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaVentasL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Cliente", "Cantidad Productos", "Fecha", "Precio Total", "Factura"
            }
        ));
        tablaVentasL.setFillsViewportHeight(true);
        tablaVentasL.getTableHeader().setReorderingAllowed(false);
        tablaVentasL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasLMouseClicked(evt);
            }
        });
        scrollVentasL.setViewportView(tablaVentasL);

        javax.swing.GroupLayout panelTablaVentasLLayout = new javax.swing.GroupLayout(panelTablaVentasL);
        panelTablaVentasL.setLayout(panelTablaVentasLLayout);
        panelTablaVentasLLayout.setHorizontalGroup(
            panelTablaVentasLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaVentasLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollVentasL, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaVentasLLayout.setVerticalGroup(
            panelTablaVentasLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaVentasLLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollVentasL, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139))
        );

        panelVentasL.add(panelTablaVentasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 660, 310));

        panelTablaProdVentaL.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdVentasL = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdVentasL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario", "Estado"
            }
        ));
        tablaProdVentasL.setFillsViewportHeight(true);
        tablaProdVentasL.getTableHeader().setReorderingAllowed(false);
        scrollProdVentasL.setViewportView(tablaProdVentasL);

        javax.swing.GroupLayout panelTablaProdVentaLLayout = new javax.swing.GroupLayout(panelTablaProdVentaL);
        panelTablaProdVentaL.setLayout(panelTablaProdVentaLLayout);
        panelTablaProdVentaLLayout.setHorizontalGroup(
            panelTablaProdVentaLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdVentaLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdVentasL, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdVentaLLayout.setVerticalGroup(
            panelTablaProdVentaLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdVentaLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdVentasL, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelVentasL.add(panelTablaProdVentaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 660, 200));

        campoIDVentaLlBuscar.setBorder(null);
        campoIDVentaLlBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDVentaLlBuscarKeyTyped(evt);
            }
        });
        panelVentasL.add(campoIDVentaLlBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(726, 170, 225, 20));

        iconBuscarVentaL.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarVentaL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarVentaL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarVentaL.setOpaque(true);
        iconBuscarVentaL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarVentaLMouseClicked(evt);
            }
        });
        panelVentasL.add(iconBuscarVentaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 170, 22, 22));

        comboOrdenarVentasL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Folio", "Fecha ascendente", "Fecha descendente" }));
        comboOrdenarVentasL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarVentasLItemStateChanged(evt);
            }
        });
        panelVentasL.add(comboOrdenarVentasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(722, 230, 180, 25));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnMostrarVentaL.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarVentaL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecNS.png"))); // NOI18N
        btnMostrarVentaL.setBorder(null);
        btnMostrarVentaL.setBorderPainted(false);
        btnMostrarVentaL.setContentAreaFilled(false);
        btnMostrarVentaL.setFocusPainted(false);
        btnMostrarVentaL.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecS.png"))); // NOI18N
        btnMostrarVentaL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarVentaLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMostrarVentaL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMostrarVentaL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelVentasL.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 280, 220, 70));

        fondoVentasL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoVentaL.png"))); // NOI18N
        panelVentasL.add(fondoVentasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelVentasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelFacturaL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        campoIDClienteFacL.setEditable(false);
        campoIDClienteFacL.setBorder(null);
        panelFacturaL.add(campoIDClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 154, 250, 20));

        campoNomClienteFacL.setEditable(false);
        campoNomClienteFacL.setBorder(null);
        panelFacturaL.add(campoNomClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 221, 250, 20));

        campoTelClienteFacL.setEditable(false);
        campoTelClienteFacL.setBorder(null);
        panelFacturaL.add(campoTelClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 287, 110, 20));

        campoEdadClienteFacL.setEditable(false);
        campoEdadClienteFacL.setBorder(null);
        panelFacturaL.add(campoEdadClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 287, 100, 20));

        campoRfcClienteFacL.setEditable(false);
        campoRfcClienteFacL.setBorder(null);
        panelFacturaL.add(campoRfcClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 353, 250, 20));

        campoEmpClienteFacL.setEditable(false);
        campoEmpClienteFacL.setBorder(null);
        panelFacturaL.add(campoEmpClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 426, 250, 20));

        campoDomClienteFacL.setEditable(false);
        campoDomClienteFacL.setBorder(null);
        panelFacturaL.add(campoDomClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 492, 250, 20));

        campoMailClienteFacL.setEditable(false);
        campoMailClienteFacL.setBorder(null);
        panelFacturaL.add(campoMailClienteFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 556, 250, 20));

        panelTablaProdFactura.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdFacturaL = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdFacturaL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio Unitario"
            }
        ));
        tablaProdFacturaL.setFillsViewportHeight(true);
        tablaProdFacturaL.getTableHeader().setReorderingAllowed(false);
        scrollProdFactura.setViewportView(tablaProdFacturaL);

        javax.swing.GroupLayout panelTablaProdFacturaLayout = new javax.swing.GroupLayout(panelTablaProdFactura);
        panelTablaProdFactura.setLayout(panelTablaProdFacturaLayout);
        panelTablaProdFacturaLayout.setHorizontalGroup(
            panelTablaProdFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaProdFacturaLayout.setVerticalGroup(
            panelTablaProdFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaProdFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelFacturaL.add(panelTablaProdFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, 600, 230));

        comboFormaFacL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "PayPal" }));
        comboFormaFacL.setEnabled(false);
        panelFacturaL.add(comboFormaFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, 110, -1));

        campoFechaFacL.setEditable(false);
        campoFechaFacL.setBorder(null);
        panelFacturaL.add(campoFechaFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 467, 210, 20));

        campoTotalFacL.setEditable(false);
        campoTotalFacL.setBorder(null);
        panelFacturaL.add(campoTotalFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 544, 130, 20));

        panelBtnElimFacL.setBackground(new java.awt.Color(255, 255, 255));

        btnElimFacturaL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimFacturaNS.png"))); // NOI18N
        btnElimFacturaL.setBorder(null);
        btnElimFacturaL.setBorderPainted(false);
        btnElimFacturaL.setContentAreaFilled(false);
        btnElimFacturaL.setFocusPainted(false);
        btnElimFacturaL.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimFacturaS.png"))); // NOI18N
        btnElimFacturaL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimFacturaLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnElimFacLLayout = new javax.swing.GroupLayout(panelBtnElimFacL);
        panelBtnElimFacL.setLayout(panelBtnElimFacLLayout);
        panelBtnElimFacLLayout.setHorizontalGroup(
            panelBtnElimFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnElimFacLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnElimFacturaL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBtnElimFacLLayout.setVerticalGroup(
            panelBtnElimFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnElimFacturaL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelFacturaL.add(panelBtnElimFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 600, 220, 60));

        panelAcCanFacFacL.setBackground(new java.awt.Color(249, 251, 242));

        btnAceptarFacL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnAceptarFacL.setToolTipText("Regresar");
        btnAceptarFacL.setBorder(null);
        btnAceptarFacL.setBorderPainted(false);
        btnAceptarFacL.setContentAreaFilled(false);
        btnAceptarFacL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAceptarFacL.setFocusPainted(false);
        btnAceptarFacL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarFacLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcCanFacFacLLayout = new javax.swing.GroupLayout(panelAcCanFacFacL);
        panelAcCanFacFacL.setLayout(panelAcCanFacFacLLayout);
        panelAcCanFacFacLLayout.setHorizontalGroup(
            panelAcCanFacFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAcCanFacFacLLayout.createSequentialGroup()
                .addComponent(btnAceptarFacL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAcCanFacFacLLayout.setVerticalGroup(
            panelAcCanFacFacLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcCanFacFacLLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnAceptarFacL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        panelFacturaL.add(panelAcCanFacFacL, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 420, 60, 150));

        campoIDFacturaL.setEditable(false);
        panelFacturaL.add(campoIDFacturaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 110, -1));

        fondoFacturaL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoVerFacturaL.png"))); // NOI18N
        panelFacturaL.add(fondoFacturaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelFacturaL, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelVentasW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelVerFacturaW.setBackground(new java.awt.Color(255, 255, 255));

        btnVerFacturaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnVerFacturaLNS.png"))); // NOI18N
        btnVerFacturaW.setBorder(null);
        btnVerFacturaW.setBorderPainted(false);
        btnVerFacturaW.setContentAreaFilled(false);
        btnVerFacturaW.setFocusPainted(false);
        btnVerFacturaW.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnVerFacturaLS.png"))); // NOI18N
        btnVerFacturaW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerFacturaWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVerFacturaWLayout = new javax.swing.GroupLayout(panelVerFacturaW);
        panelVerFacturaW.setLayout(panelVerFacturaWLayout);
        panelVerFacturaWLayout.setHorizontalGroup(
            panelVerFacturaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVerFacturaWLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnVerFacturaW))
        );
        panelVerFacturaWLayout.setVerticalGroup(
            panelVerFacturaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVerFacturaWLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(btnVerFacturaW))
        );

        panelVentasW.add(panelVerFacturaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 550, 210, 70));

        panelBtnMostrarVentaW.setBackground(new java.awt.Color(255, 255, 255));

        btnMostrarVentaW.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarVentaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecNS.png"))); // NOI18N
        btnMostrarVentaW.setBorder(null);
        btnMostrarVentaW.setBorderPainted(false);
        btnMostrarVentaW.setContentAreaFilled(false);
        btnMostrarVentaW.setFocusPainted(false);
        btnMostrarVentaW.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMostrarSelecS.png"))); // NOI18N
        btnMostrarVentaW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarVentaWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnMostrarVentaWLayout = new javax.swing.GroupLayout(panelBtnMostrarVentaW);
        panelBtnMostrarVentaW.setLayout(panelBtnMostrarVentaWLayout);
        panelBtnMostrarVentaWLayout.setHorizontalGroup(
            panelBtnMostrarVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnMostrarVentaWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMostrarVentaW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBtnMostrarVentaWLayout.setVerticalGroup(
            panelBtnMostrarVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMostrarVentaW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelVentasW.add(panelBtnMostrarVentaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 240, 220, 70));

        panelBtnMarcarVentaW.setBackground(new java.awt.Color(255, 255, 255));

        btnMarcarVentaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMarcarVentaWNS.png"))); // NOI18N
        btnMarcarVentaW.setBorder(null);
        btnMarcarVentaW.setBorderPainted(false);
        btnMarcarVentaW.setContentAreaFilled(false);
        btnMarcarVentaW.setFocusPainted(false);
        btnMarcarVentaW.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnMarcarVentaWS.png"))); // NOI18N
        btnMarcarVentaW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarcarVentaWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnMarcarVentaWLayout = new javax.swing.GroupLayout(panelBtnMarcarVentaW);
        panelBtnMarcarVentaW.setLayout(panelBtnMarcarVentaWLayout);
        panelBtnMarcarVentaWLayout.setHorizontalGroup(
            panelBtnMarcarVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMarcarVentaW, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelBtnMarcarVentaWLayout.setVerticalGroup(
            panelBtnMarcarVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnMarcarVentaWLayout.createSequentialGroup()
                .addComponent(btnMarcarVentaW, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        panelVentasW.add(panelBtnMarcarVentaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 390, 210, 70));

        panelTablaVentasW.setBackground(new java.awt.Color(255, 237, 225));

        tablaVentasW = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaVentasW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Cliente", "Fecha_Compra", "Fecha_Entrega", "Precio_Total", "Estado", "Factura"
            }
        ));
        tablaVentasW.setFillsViewportHeight(true);
        tablaVentasW.getTableHeader().setReorderingAllowed(false);
        tablaVentasW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasWMouseClicked(evt);
            }
        });
        scrollVentasW.setViewportView(tablaVentasW);

        javax.swing.GroupLayout panelTablaVentasWLayout = new javax.swing.GroupLayout(panelTablaVentasW);
        panelTablaVentasW.setLayout(panelTablaVentasWLayout);
        panelTablaVentasWLayout.setHorizontalGroup(
            panelTablaVentasWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaVentasWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollVentasW, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaVentasWLayout.setVerticalGroup(
            panelTablaVentasWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaVentasWLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollVentasW, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(280, 280, 280))
        );

        panelVentasW.add(panelTablaVentasW, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 670, 180));

        panelTablaClienteVentaW.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdVentaW = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaClienteVentaW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Apellidos", "Teléfono", "Domicilio"
            }
        ));
        tablaClienteVentaW.setFillsViewportHeight(true);
        tablaClienteVentaW.getTableHeader().setReorderingAllowed(false);
        scrollClienteVentaW.setViewportView(tablaClienteVentaW);

        javax.swing.GroupLayout panelTablaClienteVentaWLayout = new javax.swing.GroupLayout(panelTablaClienteVentaW);
        panelTablaClienteVentaW.setLayout(panelTablaClienteVentaWLayout);
        panelTablaClienteVentaWLayout.setHorizontalGroup(
            panelTablaClienteVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaClienteVentaWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollClienteVentaW, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaClienteVentaWLayout.setVerticalGroup(
            panelTablaClienteVentaWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaClienteVentaWLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollClienteVentaW, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(325, 325, 325))
        );

        panelVentasW.add(panelTablaClienteVentaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 670, 130));

        jPanel3.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdVentaW = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdVentaW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Producto", "Cantidad", "Precio_Unitario", "Estado"
            }
        ));
        tablaProdVentaW.setFillsViewportHeight(true);
        tablaProdVentaW.getTableHeader().setReorderingAllowed(false);
        scrollProdVentaW.setViewportView(tablaProdVentaW);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdVentaW, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdVentaW, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelVentasW.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 670, 160));

        campoIDVentaWBuscar.setBorder(null);
        campoIDVentaWBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDVentaWBuscarKeyTyped(evt);
            }
        });
        panelVentasW.add(campoIDVentaWBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(726, 124, 225, 20));

        comboOrdenarVentasW.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Folio", "Fecha Compra Descendente", "Fecha Entrega Descendente", "Estado" }));
        comboOrdenarVentasW.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarVentasWItemStateChanged(evt);
            }
        });
        panelVentasW.add(comboOrdenarVentasW, new org.netbeans.lib.awtextra.AbsoluteConstraints(722, 190, 200, -1));

        iconBuscarVentaW.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarVentaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarVentaW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarVentaW.setOpaque(true);
        iconBuscarVentaW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarVentaWMouseClicked(evt);
            }
        });
        panelVentasW.add(iconBuscarVentaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 120, 22, 30));

        fondoVentasW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoVentaW.png"))); // NOI18N
        panelVentasW.add(fondoVentasW, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelVentasW, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelFacturaW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTabProdFacW.setBackground(new java.awt.Color(255, 237, 225));

        tablaProdFacW = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaProdFacW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Producto", "Cantidad", "Precio_Unitario"
            }
        ));
        tablaProdFacW.setFillsViewportHeight(true);
        tablaProdFacW.getTableHeader().setReorderingAllowed(false);
        scrollProdFacW.setViewportView(tablaProdFacW);

        javax.swing.GroupLayout panelTabProdFacWLayout = new javax.swing.GroupLayout(panelTabProdFacW);
        panelTabProdFacW.setLayout(panelTabProdFacWLayout);
        panelTabProdFacWLayout.setHorizontalGroup(
            panelTabProdFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabProdFacWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollProdFacW, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTabProdFacWLayout.setVerticalGroup(
            panelTabProdFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabProdFacWLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollProdFacW, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(249, 249, 249))
        );

        panelFacturaW.add(panelTabProdFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 600, 210));

        campoIDClienteW.setEditable(false);
        campoIDClienteW.setBorder(null);
        panelFacturaW.add(campoIDClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 155, 250, 20));

        campoNomClienteW.setEditable(false);
        campoNomClienteW.setBorder(null);
        panelFacturaW.add(campoNomClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 223, 250, 20));

        campoTelClienteW.setEditable(false);
        campoTelClienteW.setBorder(null);
        panelFacturaW.add(campoTelClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 110, 20));

        campoEdadClienteW.setEditable(false);
        campoEdadClienteW.setBorder(null);
        panelFacturaW.add(campoEdadClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 100, 20));

        campoRfcClienteW.setEditable(false);
        campoRfcClienteW.setBorder(null);
        panelFacturaW.add(campoRfcClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 355, 250, 20));

        campoEmClienteW.setEditable(false);
        campoEmClienteW.setBorder(null);
        panelFacturaW.add(campoEmClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 430, 250, 20));

        campoDomClienteW.setEditable(false);
        campoDomClienteW.setBorder(null);
        panelFacturaW.add(campoDomClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 495, 250, 20));

        campoMailClienteW.setEditable(false);
        campoMailClienteW.setBorder(null);
        panelFacturaW.add(campoMailClienteW, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, 250, 20));

        campoEntregaFacW.setEditable(false);
        campoEntregaFacW.setBackground(new java.awt.Color(255, 255, 255));
        campoEntregaFacW.setBorder(null);
        panelFacturaW.add(campoEntregaFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 450, 220, 16));

        campoTotalFacW.setEditable(false);
        campoTotalFacW.setBorder(null);
        panelFacturaW.add(campoTotalFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 530, 110, -1));

        comboEstadoFacW.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pendiente", "Entregado" }));
        comboEstadoFacW.setEnabled(false);
        panelFacturaW.add(comboEstadoFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 570, 140, -1));

        comboFormaFacW.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "PayPal" }));
        comboFormaFacW.setEnabled(false);
        panelFacturaW.add(comboFormaFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(656, 380, 110, -1));

        campoIDFacWBuscado.setEditable(false);
        campoIDFacWBuscado.setEnabled(false);
        panelFacturaW.add(campoIDFacWBuscado, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 80, -1));

        panelAcFacW.setBackground(new java.awt.Color(249, 251, 242));

        btnAceptarFacW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnAceptarFacW.setToolTipText("Regresar");
        btnAceptarFacW.setBorder(null);
        btnAceptarFacW.setBorderPainted(false);
        btnAceptarFacW.setContentAreaFilled(false);
        btnAceptarFacW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAceptarFacW.setFocusPainted(false);
        btnAceptarFacW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarFacWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcFacWLayout = new javax.swing.GroupLayout(panelAcFacW);
        panelAcFacW.setLayout(panelAcFacWLayout);
        panelAcFacWLayout.setHorizontalGroup(
            panelAcFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAcFacWLayout.createSequentialGroup()
                .addComponent(btnAceptarFacW, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAcFacWLayout.setVerticalGroup(
            panelAcFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcFacWLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnAceptarFacW, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        panelFacturaW.add(panelAcFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 420, 60, 150));

        panelBtnElimFacW.setBackground(new java.awt.Color(255, 255, 255));

        btnElimFacturaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimFacturaNS.png"))); // NOI18N
        btnElimFacturaW.setBorder(null);
        btnElimFacturaW.setBorderPainted(false);
        btnElimFacturaW.setContentAreaFilled(false);
        btnElimFacturaW.setFocusPainted(false);
        btnElimFacturaW.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimFacturaS.png"))); // NOI18N
        btnElimFacturaW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimFacturaWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnElimFacWLayout = new javax.swing.GroupLayout(panelBtnElimFacW);
        panelBtnElimFacW.setLayout(panelBtnElimFacWLayout);
        panelBtnElimFacWLayout.setHorizontalGroup(
            panelBtnElimFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnElimFacWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnElimFacturaW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBtnElimFacWLayout.setVerticalGroup(
            panelBtnElimFacWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnElimFacturaW, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelFacturaW.add(panelBtnElimFacW, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 605, 220, 60));

        fondoFacturaW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoVerFacturaW.png"))); // NOI18N
        panelFacturaW.add(fondoFacturaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelFacturaW, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelClientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnClientes.setBackground(new java.awt.Color(248, 249, 255));

        btnElimCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimClienteNS.png"))); // NOI18N
        btnElimCliente.setBorder(null);
        btnElimCliente.setBorderPainted(false);
        btnElimCliente.setContentAreaFilled(false);
        btnElimCliente.setFocusPainted(false);
        btnElimCliente.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnElimClienteS.png"))); // NOI18N
        btnElimCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElimClienteActionPerformed(evt);
            }
        });

        btnModiCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiClienteNS.png"))); // NOI18N
        btnModiCliente.setBorder(null);
        btnModiCliente.setBorderPainted(false);
        btnModiCliente.setContentAreaFilled(false);
        btnModiCliente.setFocusPainted(false);
        btnModiCliente.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiClienteS.png"))); // NOI18N
        btnModiCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnClientesLayout = new javax.swing.GroupLayout(panelBtnClientes);
        panelBtnClientes.setLayout(panelBtnClientesLayout);
        panelBtnClientesLayout.setHorizontalGroup(
            panelBtnClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnClientesLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(btnModiCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 284, Short.MAX_VALUE)
                .addComponent(btnElimCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148))
        );
        panelBtnClientesLayout.setVerticalGroup(
            panelBtnClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBtnClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnElimCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModiCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelClientes.add(panelBtnClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 1000, 70));

        panelTablaClientes.setBackground(new java.awt.Color(255, 237, 225));

        tablaClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre (s)", "Apellidos", "Edad", "Teléfono", "RFC", "Domicilio", "Empresa", "Correo"
            }
        ));
        tablaClientes.setFillsViewportHeight(true);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        scrollClientes.setViewportView(tablaClientes);

        javax.swing.GroupLayout panelTablaClientesLayout = new javax.swing.GroupLayout(panelTablaClientes);
        panelTablaClientes.setLayout(panelTablaClientesLayout);
        panelTablaClientesLayout.setHorizontalGroup(
            panelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaClientesLayout.setVerticalGroup(
            panelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelClientes.add(panelTablaClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 985, 400));

        campoIDClienteBuscar.setBorder(null);
        campoIDClienteBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDClienteBuscarKeyTyped(evt);
            }
        });
        panelClientes.add(campoIDClienteBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 120, 144, 20));

        iconBuscarCliente.setBackground(new java.awt.Color(255, 255, 255));
        iconBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconBuscar.png"))); // NOI18N
        iconBuscarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBuscarCliente.setOpaque(true);
        iconBuscarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBuscarClienteMouseClicked(evt);
            }
        });
        panelClientes.add(iconBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 22, 20));

        comboOrdenarClientes.setForeground(new java.awt.Color(255, 255, 255));
        comboOrdenarClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Apellidos" }));
        comboOrdenarClientes.setBorder(null);
        comboOrdenarClientes.setPreferredSize(new java.awt.Dimension(200, 25));
        comboOrdenarClientes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarClientesItemStateChanged(evt);
            }
        });
        panelClientes.add(comboOrdenarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 120, 170, -1));

        fondoClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoClientes.png"))); // NOI18N
        panelClientes.add(fondoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelModiCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBtnModiClien.setBackground(new java.awt.Color(248, 249, 255));

        btnModiClienteBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiClienteNS.png"))); // NOI18N
        btnModiClienteBD.setBorder(null);
        btnModiClienteBD.setBorderPainted(false);
        btnModiClienteBD.setContentAreaFilled(false);
        btnModiClienteBD.setFocusPainted(false);
        btnModiClienteBD.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnModiClienteS.png"))); // NOI18N
        btnModiClienteBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModiClienteBDActionPerformed(evt);
            }
        });

        btnCancelarClienModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarNS.png"))); // NOI18N
        btnCancelarClienModi.setBorder(null);
        btnCancelarClienModi.setBorderPainted(false);
        btnCancelarClienModi.setContentAreaFilled(false);
        btnCancelarClienModi.setFocusPainted(false);
        btnCancelarClienModi.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Btn/btnCancelarS.png"))); // NOI18N
        btnCancelarClienModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarClienModiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnModiClienLayout = new javax.swing.GroupLayout(panelBtnModiClien);
        panelBtnModiClien.setLayout(panelBtnModiClienLayout);
        panelBtnModiClienLayout.setHorizontalGroup(
            panelBtnModiClienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnModiClienteBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelarClienModi, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        panelBtnModiClienLayout.setVerticalGroup(
            panelBtnModiClienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnModiClienLayout.createSequentialGroup()
                .addComponent(btnModiClienteBD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(btnCancelarClienModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        panelModiCliente.add(panelBtnModiClien, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 240, 220, 240));

        panelBtnBuscRecClienModi.setBackground(new java.awt.Color(255, 243, 236));

        btnBuscarClienModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/btnPalomitaS.png"))); // NOI18N
        btnBuscarClienModi.setToolTipText("Buscar");
        btnBuscarClienModi.setBorder(null);
        btnBuscarClienModi.setBorderPainted(false);
        btnBuscarClienModi.setContentAreaFilled(false);
        btnBuscarClienModi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarClienModi.setFocusPainted(false);
        btnBuscarClienModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienModiActionPerformed(evt);
            }
        });

        btnRecarClienModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Iconos/iconRecargar.png"))); // NOI18N
        btnRecarClienModi.setToolTipText("Limpiar Campos");
        btnRecarClienModi.setBorder(null);
        btnRecarClienModi.setBorderPainted(false);
        btnRecarClienModi.setContentAreaFilled(false);
        btnRecarClienModi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecarClienModi.setFocusPainted(false);
        btnRecarClienModi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecarClienModiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnBuscRecClienModiLayout = new javax.swing.GroupLayout(panelBtnBuscRecClienModi);
        panelBtnBuscRecClienModi.setLayout(panelBtnBuscRecClienModiLayout);
        panelBtnBuscRecClienModiLayout.setHorizontalGroup(
            panelBtnBuscRecClienModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnBuscRecClienModiLayout.createSequentialGroup()
                .addComponent(btnRecarClienModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(btnBuscarClienModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBtnBuscRecClienModiLayout.setVerticalGroup(
            panelBtnBuscRecClienModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnBuscRecClienModiLayout.createSequentialGroup()
                .addGroup(panelBtnBuscRecClienModiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarClienModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecarClienModi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModiCliente.add(panelBtnBuscRecClienModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 190, 60));

        campoIDClienteModi.setBorder(null);
        campoIDClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoIDClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 310, 145, 20));

        campoIDClienEncon.setEditable(false);
        campoIDClienEncon.setEnabled(false);
        panelModiCliente.add(campoIDClienEncon, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, 40, -1));

        campoNomClienteModi.setBorder(null);
        campoNomClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNomClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoNomClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, 210, -1));

        campoApeClienteModi.setBorder(null);
        campoApeClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoApeClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoApeClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, 210, 20));

        campoEdadClienteModi.setBorder(null);
        campoEdadClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoEdadClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoEdadClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, 210, 20));

        campoTelClienteModi.setBorder(null);
        campoTelClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoTelClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoTelClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 334, 210, 16));

        campoRfcClienteModi.setBorder(null);
        campoRfcClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoRfcClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoRfcClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 394, 210, 20));

        campoDomClienteModi.setBorder(null);
        campoDomClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoDomClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoDomClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 460, 210, -1));

        campoEmClienteModi.setBorder(null);
        campoEmClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoEmClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoEmClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 520, 210, 20));

        campoMailClienteModi.setBorder(null);
        campoMailClienteModi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoMailClienteModiKeyTyped(evt);
            }
        });
        panelModiCliente.add(campoMailClienteModi, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 581, 210, 20));

        fondoModiCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoModiCliente.png"))); // NOI18N
        panelModiCliente.add(fondoModiCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelModiCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        panelNotificaciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(253, 238, 229));

        jLabel7.setBackground(new java.awt.Color(253, 238, 229));
        jLabel7.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        jLabel7.setText("Pedidos adicionales de productos:");
        jLabel7.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNotificaciones.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, 300, 20));

        panelTablaNotificaciones.setBackground(new java.awt.Color(255, 237, 225));

        tablaNotificacion = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tablaNotificacion.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaNotificacion.getTableHeader().setReorderingAllowed(false);
        tablaNotificacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaNotificacionMouseClicked(evt);
            }
        });
        scrollNotificacion.setViewportView(tablaNotificacion);

        javax.swing.GroupLayout panelTablaNotificacionesLayout = new javax.swing.GroupLayout(panelTablaNotificaciones);
        panelTablaNotificaciones.setLayout(panelTablaNotificacionesLayout);
        panelTablaNotificacionesLayout.setHorizontalGroup(
            panelTablaNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollNotificacion, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaNotificacionesLayout.setVerticalGroup(
            panelTablaNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollNotificacion, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNotificaciones.add(panelTablaNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 170, 460, 380));

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
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
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

        javax.swing.GroupLayout panelTablaAlertasLayout = new javax.swing.GroupLayout(panelTablaAlertas);
        panelTablaAlertas.setLayout(panelTablaAlertasLayout);
        panelTablaAlertasLayout.setHorizontalGroup(
            panelTablaAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTablaAlertasLayout.setVerticalGroup(
            panelTablaAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNotificaciones.add(panelTablaAlertas, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 170, 460, 380));

        jPanel4.setBackground(new java.awt.Color(248, 249, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        panelNotificaciones.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 570, 690, 90));

        fondoNotificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/fondoNotificaciones.png"))); // NOI18N
        panelNotificaciones.add(fondoNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelRaiz.add(panelNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1040, 675));

        fondoVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen_Fondos/Fondo.png"))); // NOI18N
        panelRaiz.add(fondoVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        headerVentas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerVentasMouseDragged(evt);
            }
        });
        headerVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerVentasMousePressed(evt);
            }
        });

        javax.swing.GroupLayout headerVentasLayout = new javax.swing.GroupLayout(headerVentas);
        headerVentas.setLayout(headerVentasLayout);
        headerVentasLayout.setHorizontalGroup(
            headerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        headerVentasLayout.setVerticalGroup(
            headerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelRaiz.add(headerVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 40));

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
            hora = "" + (Integer.parseInt(hora) - 1);
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
    
    
    private void headerVentasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerVentasMousePressed
        //se asignan las coordenadas (del mouse en la ventana)
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerVentasMousePressed

    private void headerVentasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerVentasMouseDragged
        //se asignan las coordenadas del mouse en pantalla
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        //se restan los valores para obtener el resultado de mover la ventana
        this.setLocation(x - xMouse,y - yMouse);
    }//GEN-LAST:event_headerVentasMouseDragged

    private void cerrarAreaVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cerrarAreaVentasMouseClicked
        System.exit(0);
    }//GEN-LAST:event_cerrarAreaVentasMouseClicked
    
    //Eventos de los botones del menu de apartados, donde al hcer click en alguno, se mostrará el apartado
    //correspondiente, y en caso de ser así, oculta el apartado que había antes
    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelNuevaVenta.setVisible(true);
        panelNuevaVenta.setEnabled(true);
        //se vacían tablas y campos
        this.campoIDProdBuscado.setText("");
        this.campoCantProdBuscado.setText("0");
        this.campoPagaConNV.setText("");
        if(tablaProdBuscadosNV.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdBuscadosNV.getModel();
            modeloProd.setRowCount(0);
        }
        
        if(tablaProdAgregadosNV.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdAgregadosNV.getModel();
            modeloProd.setRowCount(0);
        }
    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    private void btnDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolucionActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelDevoluciones.setVisible(true);
        panelDevoluciones.setEnabled(true);
        //se vuelve a asignar el constructor para que tenga los datos actualizados
        controladorDev = new ControladorDevoluciones("ID");
        tablaDev.setModel(controladorDev.getModelo());
        if(tablaProdSelecDev.getRowCount() != 0){
            DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecDev.getModel();
            modeloProdS.setRowCount(0);
        }
    }//GEN-LAST:event_btnDevolucionActionPerformed

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

    private void btnVentaLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaLocalActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelVentasL.setVisible(true);
        panelVentasL.setEnabled(true);
        
        controladorVentasL = new ControladorVentaLocal("Folio");
        tablaVentasL.setModel(controladorVentasL.getModelo());
        
        boolean seleccionado = false;
        for(int i = 0;i < tablaVentasL.getRowCount(); i++){
            if(tablaVentasL.isRowSelected(i))
                seleccionado = true;                
        }
        if(seleccionado != false)
            tablaVentasL.changeSelection(renglonVentaLSeleccionado, columnVentaLSeleccionado, true, false);
        
        if(tablaProdVentasL.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentasL.getModel();
            modeloProd.setRowCount(0);
        }
        
        renglonVentaLSeleccionado = 0;
        columnVentaLSeleccionado = 0;
    }//GEN-LAST:event_btnVentaLocalActionPerformed

    private void btnVentaWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaWebActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelVentasW.setVisible(true);
        panelVentasW.setEnabled(true);
        controladorVentasW = new ControladorVentaWeb();
        //controladorVentasW.actualizarModelo();
        tablaVentasW.setModel(controladorVentasW.getModelo());
        
        boolean seleccionado = false;
        for(int i = 0;i < tablaVentasW.getRowCount(); i++){
            if(tablaVentasW.isRowSelected(i))
                seleccionado = true;                
        }
        if(seleccionado != false)
            tablaVentasW.changeSelection(renglonVentaWSeleccionado, columnVentaWSeleccionado, true, false);
        
        if(tablaProdVentaW.getRowCount() != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentaW.getModel();
            for(int i = 0 ; i < tablaProdVentaW.getRowCount() ; i++)
                modeloProd.removeRow(0);
        }
        renglonVentaWSeleccionado=0;
        columnVentaWSeleccionado=0;
    }//GEN-LAST:event_btnVentaWebActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelClientes.setVisible(true);
        panelClientes.setEnabled(true);
        controladorClienteG = new ControladorClienteGeneral("ID");
        tablaClientes.setModel(controladorClienteG.getModelo());
        
        boolean seleccionado = false;
        for(int i = 0;i < tablaClientes.getRowCount(); i++){
            if(tablaClientes.isRowSelected(i))
                seleccionado = true;                
        }
        if(seleccionado != false)
            tablaClientes.changeSelection(renglonClienteSeleccionado, columnClienteSeleccionado, true, false);
        renglonClienteSeleccionado=0;
        columnClienteSeleccionado=0;
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnNotificacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificacionesActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelNotificaciones.setVisible(true);
        panelNotificaciones.setEnabled(true);
        
        controladorAlertas = new ControladorAlerta(1);
        tablaAlertas.setModel(controladorAlertas.getModelo());
        
        controladorAlertasPedido = new ControladorAlertaPedido(1);
        tablaNotificacion.setModel(controladorAlertasPedido.getModelo());
    }//GEN-LAST:event_btnNotificacionesActionPerformed

    private void btnNuevaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaFacturaActionPerformed
        Calendar calendario = Calendar.getInstance();
        String dia = "" + calendario.get(Calendar.DATE);
        String mes = "" + (calendario.get(Calendar.MONTH)+1);
        String anio = "" + calendario.get(Calendar.YEAR);
        this.campoFechaNFactura.setText(anio+"-"+mes+"-"+dia);
        
        //si cuando se hace clic en el ícono de factura hay productos en la tabla "tablaProdAgregadosNV"
        //se tomará como si hubera una ventaL nueva que no ha sido registrada, por lo tanto,
        //se registrará la ventaL con sus productos, y se tomará la ventaL como la ventaL a la que se realizará
        //la factura.
        int row = tablaProdAgregadosNV.getRowCount();
        if(row != 0){
            VentaLocal venta = new VentaLocal();
            ControladorMetodos controladorMetodo = new ControladorMetodos();
            int ultimaVenta = controladorMetodo.ultimaVenta();
            int nuevaID = ultimaVenta + 2;
            float precioV = 0;
            //Productos producto = new Productos();
            ControladorProductosVenta controladorProdV = new ControladorProductosVenta();
            boolean agregado = false;
            
            venta.setId_venta_local(nuevaID);
            venta.setPrecio_total(Float.parseFloat(this.campoTotalNVenta.getText()));
            int cant_prod = 0;
                for(int i = 0 ; i < row ; i++)
                    cant_prod += Integer.parseInt(tablaProdAgregadosNV.getValueAt(i,2).toString());
            venta.setCant_productos(cant_prod);
            venta.setFecha(anio+"-"+mes+"-"+dia);
            venta.setForma_pago(String.valueOf(this.comboFormaNVenta.getSelectedItem()));
            if(controladorVentasL.agregarVentaL(venta)){
                for(int i = 0 ; i < row ; i++){
                    ProductosVenta prodV = new ProductosVenta();                    
                    controladorProductos = new ControladorProductos("ID");
                    precioV = controladorMetodo.precioVenta(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 0))));
                    prodV.setId_producto(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 0))));
                    prodV.setId_venta(nuevaID);
                    prodV.setCant_producto(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 2))));
                    prodV.setPrecio_venta(precioV);
                    prodV.setEstado("Funcional");
                    
                    
                    controladorProductos.setProductoActual(controladorProductos.buscarProductoPorID(String.valueOf(tablaProdAgregadosNV.getValueAt(i,0))));
                    Productos producto = controladorProductos.getProductoActual();
                    producto.setStock(producto.getStock() - prodV.getCant_producto());
                    
                    prodV.setNombre(producto.getNombre());
                    prodV.setPrecio_compra(producto.getPrecioC());
                    
                    agregado = controladorProdV.agregarProdVL(prodV);
                    
                    if(controladorProductos.actualizaProducto())
                        controladorProductos = new ControladorProductos("ID");
                }
                
                if(agregado != false){
                    
                    String nombreArchivo = ruta + "Ticket_"+nuevaID+".pdf";
                    String titulo = "Ticket";
                    String consulta = "SELECT p.Nombre,pv.CantProducto,pv.PrecioTotal FROM productosventa pv JOIN productos p ON pv.IDProducto = p.IDProducto WHERE pv.IDVenta = "+ nuevaID +" AND pv.EstadoP = 'Funcional';";
                    Reporte reporte = new ReportePDF(consulta,String.valueOf(nuevaID));
                    try {
                        reporte.generarReporte(nombreArchivo, titulo);
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(AreaVentas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("Nueva venta agregada.");
                    salir.setVisible(true);
                    
                    controladorProductos.actualizarDisponibiilidad();
                    
                    int filasPA = tablaProdAgregadosNV.getRowCount();
                    int filasPB = tablaProdBuscadosNV.getRowCount();
                    if(filasPA != 0){
                        DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
                        for(int i=0;i < filasPA;i++)
                            modeloProdAgregados.removeRow(0);

                        DefaultTableModel modeloProdBuscados = (DefaultTableModel) tablaProdBuscadosNV.getModel();
                        for(int i=0;i < filasPB;i++)
                            modeloProdBuscados.removeRow(0);

                        this.campoIDProdBuscado.setText("");
                        this.campoCambioNV.setText("0.0");
                        this.campoCantProdBuscado.setText("0");
                        this.campoPagaConNV.setText("");
                        this.campoPrecioProd.setText("0.0");
                        this.campoTotalNVenta.setText("0.0");
                    }
                    controladorVentasL = new ControladorVentaLocal("Folio");
                    VentaLocal ventaA = controladorVentasL.buscarVentaLPorID(String.valueOf(nuevaID));
                    ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(nuevaID),0);
                    tablaProdNuevFacL.setModel(controladorProdVenta.getModelo());
                    this.campoIDVentaFacEncon.setText(""+ventaA.getId_venta_local());
                    this.campoTotalNFactura.setText(""+ventaA.getPrecio_total());
                    this.campoIDVentaFacBuscar.setText(""+ventaA.getId_venta_local());
                }
            }
        }
        //si la tabla "tablaProdAgregadosNV" no tiene productos agregados, entonces se tomará en cuenta
        //que la factura será de una ventaL ya registrada.
        else{
            this.campoIDClienFacEncon.setText("");
            this.campoIDClienFacBuscar.setText("");
            this.campoNomClienNFac.setText("");
            this.campoApeClienNFac.setText("");
            this.campoMailClienNFac.setText("");
            this.campoEdadClienNFac.setText("");
            this.campoTelClienNFac.setText("");
            this.campoRfcClienNFac.setText("");
            this.campoEmpClienNFac.setText("");
            this.campoDomClienNFac.setText("");
            
            int rowF = tablaProdNuevFacL.getRowCount();
            if(rowF != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNuevFacL.getModel();
                for(int i = 0 ; i < rowF ; i++)
                    modeloProd.removeRow(0);
            }
            this.campoIDVentaFacEncon.setText("");
            this.campoTotalNFactura.setText("");
            this.campoIDVentaFacBuscar.setText("");
        }
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelNuevaFactura.setVisible(true);
        panelNuevaFactura.setEnabled(true);
        
        this.campoIDClienFacEncon.setEnabled(true);
        this.campoNomClienNFac.setEnabled(true);
        this.campoApeClienNFac.setEnabled(true);
        this.campoMailClienNFac.setEnabled(true);
        this.campoEdadClienNFac.setEnabled(true);
        this.campoTelClienNFac.setEnabled(true);
        this.campoRfcClienNFac.setEnabled(true);
        this.campoEmpClienNFac.setEnabled(true);
        this.campoDomClienNFac.setEnabled(true);
    }//GEN-LAST:event_btnNuevaFacturaActionPerformed

    private void btnNuevaDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaDevActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelNuevaDev.setVisible(true);
        panelNuevaDev.setEnabled(true);
        Calendar calendario = Calendar.getInstance();
        String dia = "" + calendario.get(Calendar.DATE);
        String mes = "" + (calendario.get(Calendar.MONTH)+1);
        String anio = "" + calendario.get(Calendar.YEAR);
        this.campoFechaNDev.setText(anio+"-"+mes+"-"+dia);
        
        this.campoIDVenDevBuscar.setText("");
        this.campoIDVentaEnDev.setText("");
        this.campoProvNDev.setText("");
        this.campoIDProvEn.setText("");
        this.campoTotalVenNDev.setText("");
        this.campoCantDevolver.setText("");
        this.campoFechaVenNDev.setText("");
        
        int row = tablaProdSelecNDev.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
            modeloProdS.setRowCount(0);
        }
        
        row = tablaProdNDev.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNDev.getModel();
            modeloProd.setRowCount(0);
        }
        
    }//GEN-LAST:event_btnNuevaDevActionPerformed

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

    private void btnVerFacturaLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerFacturaLActionPerformed
        boolean seleccionado = false;
        int row = tablaVentasL.getRowCount();
        if(row != 0){
            for(int i = 0;i < row; i++){
                if(tablaVentasL.isRowSelected(i))
                    seleccionado = true;                
            }
            if(seleccionado!=false){
                String idF = String.valueOf(tablaVentasL.getValueAt(renglonVentaLSeleccionado, 2));
                String idC = String.valueOf(tablaVentasL.getValueAt(renglonVentaLSeleccionado, 1));
                controladorFacturas = new ControladorFacturacion();
                controladorClientesL = new ControladorClienteLocal();
                Facturacion factura = controladorFacturas.buscarFactura(idF);
                ClienteLocal cliente = controladorClientesL.buscarClienteLPorID(idC);
                if((factura != null) && (cliente != null)){
                    campoIDClienteFacL.setText(""+cliente.getId_cliente_local());
                    campoNomClienteFacL.setText(cliente.getNombre()+" "+cliente.getApellidos());
                    campoTelClienteFacL.setText(cliente.getTelefono());
                    campoEdadClienteFacL.setText(""+cliente.getEdad());
                    campoRfcClienteFacL.setText(cliente.getRfc());
                    campoEmpClienteFacL.setText(cliente.getNom_empresa());
                    campoDomClienteFacL.setText(cliente.getDomicilio_f());
                    campoMailClienteFacL.setText(cliente.getCorreo());
                    campoFechaFacL.setText(factura.getFecha_exp());
                    campoTotalFacL.setText("$ "+factura.getPago_total());
                    comboFormaFacL.setSelectedItem(factura.getForma_pago());
                    campoIDFacturaL.setText(""+factura.getNum_factura());
                    
                    ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(tablaVentasL.getValueAt(renglonVentaLSeleccionado,0)),0);
                    tablaProdFacturaL.setModel(controladorProdVenta.getModelo());
                    ocultarTodo();
                    //Se habilita solo el apartado correspondiente
                    panelFacturaL.setVisible(true);
                    panelFacturaL.setEnabled(true);
                }
                else{
                    String m = "La venta no cuenta con una factura.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }    
            }else{
                String m = "No hay una venta seleccionada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnVerFacturaLActionPerformed

    private void btnVerFacturaWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerFacturaWActionPerformed
        boolean seleccionado = false;
        int row = tablaVentasW.getRowCount();
        if(row != 0){
            for(int i = 0;i < row; i++){
                if(tablaVentasW.isRowSelected(i))
                    seleccionado = true;                
            }
            if(seleccionado!=false){
                String idF = String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado, 6));
                String idC = String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado, 1));
                String idV = String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado, 0));
                controladorFacturas = new ControladorFacturacion();
                controladorClientesW = new ControladorClienteWeb();
                controladorVentasW = new ControladorVentaWeb();
                Facturacion factura = controladorFacturas.buscarFactura(idF);
                ClienteWeb cliente = controladorClientesW.buscarClienteWPorID(idC);
                VentaWeb venta = controladorVentasW.buscarVentaWPorID(idV);
                if((factura != null) && (cliente != null)){
                    campoIDClienteW.setText(""+cliente.getId_cliente_web());
                    campoNomClienteW.setText(cliente.getNombre()+" "+cliente.getApellidos());
                    campoTelClienteW.setText(cliente.getTelefono());
                    campoEdadClienteW.setText(""+cliente.getEdad());
                    campoRfcClienteW.setText(cliente.getRfc());
                    campoEmClienteW.setText(cliente.getNom_empresa());
                    campoDomClienteW.setText(cliente.getDomicilio_f());
                    campoMailClienteW.setText(cliente.getCorreo());
                    campoTotalFacW.setText("$ "+factura.getPago_total());
                    campoEntregaFacW.setText(venta.getFecha_entrega());
                    comboFormaFacW.setSelectedItem(factura.getForma_pago());
                    comboEstadoFacW.setSelectedItem(venta.getEstado());
                    
                    campoIDFacWBuscado.setText(""+venta.getId_venta_web());
                    ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado,0)),0);
                    tablaProdFacW.setModel(controladorProdVenta.getModelo());
                    
                    ocultarTodo();
                    //Se habilita solo el apartado correspondiente
                    panelFacturaW.setVisible(true);
                    panelFacturaW.setEnabled(true);
                }
                else{
                    String m = "La venta no cuenta con una factura.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }     
            }else{
                String m = "No hay una venta seleccionada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnVerFacturaWActionPerformed

    private void btnElimClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimClienteActionPerformed
        boolean seleccionado = false;
        int row = tablaClientes.getRowCount();
        if(row != 0){
            for(int i = 0;i < row; i++){
                if(tablaClientes.isRowSelected(i))
                    seleccionado = true;                
            }
            if(seleccionado!=false){
                AlertaConfirm pregunta = new AlertaConfirm(this, true);
                pregunta.mensaje.setText("¿Desea eliminar?.");
                pregunta.setVisible(true);

                boolean respuesta = pregunta.respuesta;
                
                if(respuesta){
                    int id = Integer.parseInt(String.valueOf(tablaClientes.getValueAt(renglonClienteSeleccionado, 0)));
                    if((id % 2) == 0){
                        controladorClienteG.eliminarClienteW(id);
                        controladorClienteG = new ControladorClienteGeneral("ID");
                        tablaClientes.setModel(controladorClienteG.getModelo());
                        
                        String m = "Eliminación realizada.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                        
                        renglonClienteSeleccionado=0;
                        columnClienteSeleccionado=0;
                    }else{
                        controladorClienteG.eliminarClienteL(id);
                        controladorClienteG = new ControladorClienteGeneral("ID");
                        tablaClientes.setModel(controladorClienteG.getModelo());
                        
                        String m = "Eliminación realizada.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                        
                        renglonClienteSeleccionado=0;
                        columnClienteSeleccionado=0;
                    }
                }
            }else{
                String m = "No hay un cliente seleccionado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnElimClienteActionPerformed

    private void btnModiClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiClienteActionPerformed
        //Se oculta todo
        ocultarTodo();
        //Se habilita solo el apartado correspondiente
        panelModiCliente.setVisible(true);
        panelModiCliente.setEnabled(true);
        
        this.campoIDClienteModi.setText("");
        this.campoIDClienEncon.setText("");
        this.campoNomClienteModi.setText("");
        this.campoApeClienteModi.setText("");
        this.campoEdadClienteModi.setText("");
        this.campoTelClienteModi.setText("");
        this.campoRfcClienteModi.setText("");
        this.campoDomClienteModi.setText("");
        this.campoEmClienteModi.setText("");
        this.campoMailClienteModi.setText("");
    }//GEN-LAST:event_btnModiClienteActionPerformed

    private void iconBuscarProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarProdMouseClicked
        if(!(this.campoIDProdBuscado.getText().equals(""))){
            controladorProductos = new ControladorProductos("ID");
            String id = this.campoIDProdBuscado.getText();
            Productos producto = controladorProductos.buscarProductoPorID(id);
            if(producto != null){
                if(controladorProductos.buscarProductosIDCat(producto.getId_producto(),producto.getCat_producto()))
                    tablaProdBuscadosNV.setModel(controladorProductos.getModelo());
            }else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Producto no encontrado.");
                salir.setVisible(true);
                
                this.campoIDProdBuscado.setText("");
            }
        }
    }//GEN-LAST:event_iconBuscarProdMouseClicked

    private void tablaProdBuscadosNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProdBuscadosNVMouseClicked
        if(tablaProdBuscadosNV.getRowCount() != 0){
            renglonProdBuscado = tablaProdBuscadosNV.rowAtPoint(evt.getPoint());
            String codigo = (String.valueOf(tablaProdBuscadosNV.getValueAt(renglonProdBuscado, 0)));
            Productos producto = controladorProductos.buscarProductoPorID(codigo);
            if((this.campoCantProdBuscado.getText()).equals(""))
                this.campoCantProdBuscado.setText("0");
            this.campoPrecioProd.setText(""+(producto.getPrecioV()*(Float.parseFloat(this.campoCantProdBuscado.getText()))));
        }
    }//GEN-LAST:event_tablaProdBuscadosNVMouseClicked

    private void campoCantProdBuscadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoCantProdBuscadoKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(tablaProdBuscadosNV.getRowCount()!=0){
                String codigo = (String.valueOf(tablaProdBuscadosNV.getValueAt(renglonProdBuscado, 0)));
                Productos producto = controladorProductos.buscarProductoPorID(codigo);
                if((this.campoCantProdBuscado.getText()).equals(""))
                    this.campoCantProdBuscado.setText("0");
                if((Integer.parseInt(this.campoCantProdBuscado.getText()) > producto.getStock()) ||
                        (Integer.parseInt(this.campoCantProdBuscado.getText()) < 0)){
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("Valor invalido.");
                    salir.setVisible(true);
                    
                    this.campoCantProdBuscado.setText("0");
                    this.campoPrecioProd.setText("0.0");
                }
                else
                    this.campoPrecioProd.setText(""+(producto.getPrecioV()*(Float.parseFloat(this.campoCantProdBuscado.getText()))));
            }
            
        }
    }//GEN-LAST:event_campoCantProdBuscadoKeyReleased

    private void btnAgregarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProdActionPerformed
        boolean seleccionado = false;
        int filas = tablaProdBuscadosNV.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaProdBuscadosNV.isRowSelected(i))
                seleccionado = true;                
        }
        if(seleccionado!=false){
            String codigo = (String.valueOf(tablaProdBuscadosNV.getValueAt(renglonProdBuscado, 0)));
            Productos producto = controladorProductos.buscarProductoPorID(codigo);
            //se valida que no se encuentre el mismo producto en la tabla de productos agregados
            boolean existe = false;
            int row = tablaProdAgregadosNV.getRowCount();
            if(row!=0){
                for(int i = 0;i < row; i++){
                    if(producto.getId_producto() == Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 0))))
                        existe = true;                
                }
            }
            
            if(this.campoCantProdBuscado.getText().equals(""))
                this.campoCantProdBuscado.setText("0");

            //se agrega el producto, se realiza de nuevo la validación de la cantidad ingresada, en caso de que
            //el usuario no hay confirmado con la tecla ENTER
            if((Integer.parseInt(this.campoCantProdBuscado.getText()) > producto.getStock()) ||
                        (Integer.parseInt(this.campoCantProdBuscado.getText()) < 0)){
                
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Valor invalido.");
                salir.setVisible(true);
                
                if(Integer.parseInt(this.campoCantProdBuscado.getText()) > producto.getStock()){
                    AlertaConfirm preguntaPedido = new AlertaConfirm(this, true);
                    preguntaPedido.mensaje.setText("¿Desea agregar una alerta de pedido del producto?");
                    preguntaPedido.setVisible(true);

                    boolean respuesta = preguntaPedido.respuesta;
                    
                    if(respuesta){
                        AlertaPedido nuevaAlerta = new AlertaPedido();
                        Calendar calendario = Calendar.getInstance();
                        String dia = "" + calendario.get(Calendar.DATE);
                        String mes = "" + (calendario.get(Calendar.MONTH)+1);
                        String anio = "" + calendario.get(Calendar.YEAR);
                        
                        nuevaAlerta.setId_producto(producto.getId_producto());
                        nuevaAlerta.setCantidad(Integer.parseInt(this.campoCantProdBuscado.getText()));
                        nuevaAlerta.setRazon("Venta");
                        nuevaAlerta.setFecha(anio+"-"+mes+"-"+dia);
                        nuevaAlerta.setEstado("NL");
                        
                        if(controladorAlertasPedido.agregarAlerta(nuevaAlerta)){
                            AlertaWarning salirA = new AlertaWarning(this, true);
                            salirA.mensaje.setText("Se agregó la nueva alerta.");
                            salirA.setVisible(true);
                        }
                    }
                }
                
                this.campoCantProdBuscado.setText("0");
                this.campoPrecioProd.setText("0.0");
                
            }
            else{
                this.campoPrecioProd.setText(""+(producto.getPrecioV()*(Float.parseFloat(this.campoCantProdBuscado.getText()))));
                //se agrega fila si la cantidad es diferente a 0 y que no se encuentra ya agregado el producto
                if((Integer.parseInt(this.campoCantProdBuscado.getText()) != 0) && (existe != true)){
                    DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
                    modeloProdAgregados.addRow(new Object[]{
                        producto.getId_producto(),
                        producto.getNombre(),
                        this.campoCantProdBuscado.getText(),
                        this.campoPrecioProd.getText()
                    });
                    row = tablaProdAgregadosNV.getRowCount();
                    float suma = 0;
                    for(int i = 0;i < row; i++)                    
                        suma+=Float.parseFloat(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 3)));
                    if(suma != 0)
                        this.campoTotalNVenta.setText(""+suma);
                    
                    this.campoCambioNV.setText("0.0");
                    this.campoPagaConNV.setText("");
                }
                else{
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("No se puede agregar el producto.");
                    salir.setVisible(true);
                }
            }
        }
        else{
            String m = "No hay un producto seleccionado.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnAgregarProdActionPerformed

    private void btnElimProdAgregadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimProdAgregadoActionPerformed
        int row = tablaProdAgregadosNV.getRowCount();
        boolean seleccionado = false;
        for(int i = 0;i < row; i++){
            if(tablaProdAgregadosNV.isRowSelected(i))
                seleccionado = true;                
        }
        
        if((row != 0) && (seleccionado!=false)){
            DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
            
            tablaProdAgregadosNV.changeSelection(renglonProdAgregado, columnProdAgregado, true, false);
            
            modeloProdAgregados.removeRow(renglonProdAgregado);
            
            row = tablaProdAgregadosNV.getRowCount();
            float suma = 0;
            for(int i = 0;i < row; i++)                    
                suma+=Float.parseFloat(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 3)));
            if(suma != 0)
                this.campoTotalNVenta.setText(""+suma);
            
            this.campoCambioNV.setText("0.0");
            this.campoPagaConNV.setText("");
        }        
    }//GEN-LAST:event_btnElimProdAgregadoActionPerformed

    private void tablaProdAgregadosNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProdAgregadosNVMouseClicked
        if(tablaProdAgregadosNV.getRowCount() != 0){
            renglonProdAgregado = tablaProdAgregadosNV.rowAtPoint(evt.getPoint());
            columnProdAgregado = tablaProdAgregadosNV.columnAtPoint(evt.getPoint());
            if(!(String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 0)).equals(""))){
                this.campoCantProdBuscado.setText(String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 2)));
                this.campoPrecioProd.setText((String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 3))));
            }
        }
    }//GEN-LAST:event_tablaProdAgregadosNVMouseClicked

    private void btnModiCantAgregadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiCantAgregadoActionPerformed
        boolean seleccionado = false;
        int filas = tablaProdAgregadosNV.getRowCount();
        float suma = 0;
        for(int i = 0;i < filas; i++){
            if(tablaProdAgregadosNV.isRowSelected(i))
                seleccionado = true;                
        }
        
        if((filas != 0) && (seleccionado!=false)){
            String codigo = (String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 0)));
            Productos producto = controladorProductos.buscarProductoPorID(codigo);

            //se agrega el producto, se realiza de nuevo la validación de la cantidad ingresada, en caso de que
            //el usuario no hay confirmado con la tecla ENTER
            if((Integer.parseInt(this.campoCantProdBuscado.getText()) > producto.getStock()) ||
                        (Integer.parseInt(this.campoCantProdBuscado.getText()) <= 0)){
                
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Valor invalido.");
                salir.setVisible(true);
                
                this.campoCantProdBuscado.setText(String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 2)));
                this.campoPrecioProd.setText((String.valueOf(tablaProdAgregadosNV.getValueAt(renglonProdAgregado, 3))));
            }
            else{
                this.campoPrecioProd.setText(""+(producto.getPrecioV()*(Float.parseFloat(this.campoCantProdBuscado.getText()))));
                //se agrega fila si la cantidad es diferente a 0 y que no se encuentra ya agregado el producto
                if((Integer.parseInt(this.campoCantProdBuscado.getText()) != 0)){
                    DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
                    modeloProdAgregados.setValueAt(this.campoCantProdBuscado.getText(), renglonProdAgregado, 2);
                    modeloProdAgregados.setValueAt(this.campoPrecioProd.getText(), renglonProdAgregado, 3);
                    
                    tablaProdAgregadosNV.changeSelection(renglonProdAgregado, columnProdAgregado, true, false);
                    
                    for(int i = 0;i < filas; i++)                    
                        suma+=Float.parseFloat(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 3)));
                    if(suma != 0)
                        this.campoTotalNVenta.setText(""+suma);
                    
                    this.campoCambioNV.setText("0.0");
                    this.campoPagaConNV.setText("");
                }
                else{
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("No se puedo modificar el producto.");
                    salir.setVisible(true);
                }
            }
        } 
        
        
    }//GEN-LAST:event_btnModiCantAgregadoActionPerformed

    private void campoPagaConNVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPagaConNVKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(tablaProdAgregadosNV.getRowCount() != 0){
                if((this.campoPagaConNV.getText()).equals(""))
                    this.campoPagaConNV.setText("0");
                
                float total = Float.parseFloat(this.campoTotalNVenta.getText());
                float paga = Float.parseFloat(this.campoPagaConNV.getText());
                
                if(paga >= total)
                    this.campoCambioNV.setText(""+(paga-total));
                else{
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("No es suficiente para pagar la compra.");
                    salir.setVisible(true);
                }
            }
            
        }
    }//GEN-LAST:event_campoPagaConNVKeyReleased

    private void btnCanNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanNuevaVentaActionPerformed
        int filasPA = tablaProdAgregadosNV.getRowCount();
        int filasPB = tablaProdBuscadosNV.getRowCount();
        if(filasPA != 0){
            DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
            for(int i=0;i < filasPA;i++)
                modeloProdAgregados.removeRow(0);
            
            DefaultTableModel modeloProdBuscados = (DefaultTableModel) tablaProdBuscadosNV.getModel();
            for(int i=0;i < filasPB;i++)
                modeloProdBuscados.removeRow(0);
            
            this.campoIDProdBuscado.setText("");
            this.campoCambioNV.setText("0.0");
            this.campoCantProdBuscado.setText("0");
            this.campoPagaConNV.setText("");
            this.campoPrecioProd.setText("0.0");
            this.campoTotalNVenta.setText("0.0");
        }
    }//GEN-LAST:event_btnCanNuevaVentaActionPerformed

    private void btnAcepNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcepNuevaVentaActionPerformed
        int row = tablaProdAgregadosNV.getRowCount();
        
        if(row != 0){
            
            AlertaConfirm pregunta = new AlertaConfirm(this, true);
            pregunta.mensaje.setText("¿Desea facturar?.");
            pregunta.setVisible(true);
            
            boolean respuesta = pregunta.respuesta;
            
            
            if(respuesta)
                this.btnNuevaFacturaActionPerformed(evt);
            else{
                VentaLocal venta = new VentaLocal();
                Calendar calendario = Calendar.getInstance();
                String dia = "" + calendario.get(Calendar.DATE);
                String mes = "" + (calendario.get(Calendar.MONTH)+1);
                String anio = "" + calendario.get(Calendar.YEAR);
                ControladorMetodos controladorMetodo = new ControladorMetodos();
                int ultimaVenta = controladorMetodo.ultimaVenta();
                int nuevaID = ultimaVenta + 2;
                float precioV = 0;
                //Productos producto = new Productos();
                ControladorProductosVenta controladorProdV = new ControladorProductosVenta();
                boolean agregado = false;

                venta.setId_venta_local(nuevaID);
                venta.setPrecio_total(Float.parseFloat(this.campoTotalNVenta.getText()));
                int cant_prod = 0;
                for(int i = 0 ; i < row ; i++)
                    cant_prod += Integer.parseInt(tablaProdAgregadosNV.getValueAt(i,2).toString());
                venta.setCant_productos(cant_prod);
                venta.setFecha(anio+"-"+mes+"-"+dia);
                venta.setForma_pago(String.valueOf(this.comboFormaNVenta.getSelectedItem()));
                if(controladorVentasL.agregarVentaL(venta)){
                    
                    for(int i = 0 ; i < row ; i++){
                        ProductosVenta prodV = new ProductosVenta();                    
                        controladorProductos = new ControladorProductos("ID");
                        precioV = controladorMetodo.precioVenta(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 0))));
                        prodV.setId_producto(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 0))));
                        prodV.setId_venta(nuevaID);
                        prodV.setCant_producto(Integer.parseInt(String.valueOf(tablaProdAgregadosNV.getValueAt(i, 2))));
                        prodV.setPrecio_venta(precioV);
                        prodV.setEstado("Funcional");
                        
                        
                        controladorProductos.setProductoActual(controladorProductos.buscarProductoPorID(String.valueOf(tablaProdAgregadosNV.getValueAt(i,0))));
                        Productos producto = controladorProductos.getProductoActual();
                        producto.setStock(producto.getStock() - prodV.getCant_producto());
                        
                        prodV.setNombre(producto.getNombre());
                        prodV.setPrecio_compra(producto.getPrecioC());
                        
                        agregado = controladorProdV.agregarProdVL(prodV);
                        
                        if(controladorProductos.actualizaProducto())
                            controladorProductos = new ControladorProductos("ID");
                    }

                    if(agregado != false){
                        String nombreArchivo = ruta + "Ticket_"+nuevaID+".pdf";
                        String titulo = "Ticket";
                        String consulta = "SELECT p.Nombre,pv.CantProducto,pv.PrecioTotal FROM productosventa pv JOIN productos p ON pv.IDProducto = p.IDProducto WHERE pv.IDVenta = "+ nuevaID +" AND pv.EstadoP = 'Funcional';";
                        Reporte reporte = new ReportePDF(consulta,String.valueOf(nuevaID));
                        try {
                            reporte.generarReporte(nombreArchivo, titulo);
                        } catch (SQLException | IOException ex) {
                            Logger.getLogger(AreaVentas.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText("Nueva venta agregada.");
                        salir.setVisible(true);
                        
                        controladorProductos.actualizarDisponibiilidad();
                        
                        int filasPA = tablaProdAgregadosNV.getRowCount();
                        int filasPB = tablaProdBuscadosNV.getRowCount();
                        if(filasPA != 0){
                            DefaultTableModel modeloProdAgregados = (DefaultTableModel) tablaProdAgregadosNV.getModel();
                            for(int i=0;i < filasPA;i++)
                                modeloProdAgregados.removeRow(0);

                            DefaultTableModel modeloProdBuscados = (DefaultTableModel) tablaProdBuscadosNV.getModel();
                            for(int i=0;i < filasPB;i++)
                                modeloProdBuscados.removeRow(0);

                            this.campoIDProdBuscado.setText("");
                            this.campoCambioNV.setText("0.0");
                            this.campoCantProdBuscado.setText("0");
                            this.campoPagaConNV.setText("");
                            this.campoPrecioProd.setText("0.0");
                            this.campoTotalNVenta.setText("0.0");
                        }
                    }
                }
            }
        }
        else{
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText("No hay productos agregados.");
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnAcepNuevaVentaActionPerformed

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
                    String m = "Actualización realizada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                    
                    controladorProductos = new ControladorProductos("ID");
                    tablaInventario.setModel(controladorProductos.getModelo());
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
            }
            else{
                String m = "Cantidad no ingresada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
        }
        
    }//GEN-LAST:event_btnElimProdCantActionPerformed

    private void btnElimProductoBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimProductoBDActionPerformed
        if(!(this.campoIDProElimEnc.getText().equals(""))){
            if(this.campoStockProdElim.getText().equals(""))
                this.campoStockProdElim.setText("0");
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

    private void iconBuscarVentaLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarVentaLMouseClicked
        if(!(this.campoIDVentaLlBuscar.getText().equals(""))){
            controladorVentasL = new ControladorVentaLocal("Folio");
            if(controladorVentasL.buscarVentaLAct(this.campoIDVentaLlBuscar.getText())){
                tablaVentasL.setModel(controladorVentasL.getModelo());
                int row = tablaProdVentasL.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentasL.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProd.removeRow(0);
                }
            }
            else{
                String m = "Venta no encontrada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDVentaLlBuscar.setText("");
            }
        }else{
            controladorVentasL = new ControladorVentaLocal("Folio");
            tablaVentasL.setModel(controladorVentasL.getModelo());
            
            int row = tablaProdVentasL.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentasL.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
        }
    }//GEN-LAST:event_iconBuscarVentaLMouseClicked

    private void tablaVentasLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasLMouseClicked
        if(tablaVentasL.getRowCount() != 0){
            renglonVentaLSeleccionado = tablaVentasL.rowAtPoint(evt.getPoint());
            columnVentaLSeleccionado = tablaVentasL.columnAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaVentasLMouseClicked

    private void btnMostrarVentaLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarVentaLActionPerformed
        boolean seleccionado = false;
        int filas = tablaVentasL.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaVentasL.isRowSelected(i))
                seleccionado = true;                
        }
        
        if((filas != 0) && (seleccionado != false)){
            ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(tablaVentasL.getValueAt(renglonVentaLSeleccionado,0)),0);
            tablaProdVentasL.setModel(controladorProdVenta.getModelo());
            tablaProdFacturaL.setModel(controladorProdVenta.getModelo());
        }else{
            String m = "No hay una venta seleccionada.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnMostrarVentaLActionPerformed

    private void btnAceptarFacLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarFacLActionPerformed
        ocultarTodo();
        panelVentasL.setEnabled(true);
        panelVentasL.setVisible(true);
        tablaVentasL.changeSelection(renglonVentaLSeleccionado, columnVentaLSeleccionado, true, false);
    }//GEN-LAST:event_btnAceptarFacLActionPerformed

    private void btnElimFacturaLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimFacturaLActionPerformed
        if(!(this.campoIDFacturaL.getText().equals(""))){
            AlertaConfirm pregunta = new AlertaConfirm(this, true);
            pregunta.mensaje.setText("¿Desea eliminar?.");
            pregunta.setVisible(true);
            
            boolean respuesta = pregunta.respuesta;
            
            if(respuesta){
                controladorFacturas.eliminarFactura(Integer.parseInt(this.campoIDFacturaL.getText()));
                controladorVentasL = new ControladorVentaLocal("Folio");
                tablaVentasL.setModel(controladorVentasL.getModelo());
                
                String m = "Eliminación realizada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                ocultarTodo();
                panelVentasL.setEnabled(true);
                panelVentasL.setVisible(true);
                tablaVentasL.changeSelection(renglonVentaLSeleccionado, columnVentaLSeleccionado, true, false);
                this.campoIDFacturaL.setText("");
            }            
        }
    }//GEN-LAST:event_btnElimFacturaLActionPerformed

    private void btnMostrarVentaWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarVentaWActionPerformed
        boolean seleccionado = false;
        int filas = tablaVentasW.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaVentasW.isRowSelected(i))
                seleccionado = true;                
        }
        
        if((filas != 0) && (seleccionado != false)){
            ControladorProductosVenta controladorProdVentaW = new ControladorProductosVenta(String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado,0)),0);
            tablaProdVentaW.setModel(controladorProdVentaW.getModelo());
            controladorClientesW = new ControladorClienteWeb();
            controladorClientesW.buscarClienteWAct(String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado,1)));
            tablaClienteVentaW.setModel(controladorClientesW.getModelo());
            
            tablaProdFacW.setModel(controladorProdVentaW.getModelo());
        }else{
            String m = "No hay una venta seleccionada.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnMostrarVentaWActionPerformed

    private void iconBuscarVentaWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarVentaWMouseClicked
        if(!(this.campoIDVentaWBuscar.getText().equals(""))){
            controladorVentasW = new ControladorVentaWeb();
            if(controladorVentasW.buscarVentaWAct(this.campoIDVentaWBuscar.getText())){
                tablaVentasW.setModel(controladorVentasW.getModelo());
                
                int row = tablaProdVentaW.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentaW.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProd.removeRow(0);
                }

                row = tablaClienteVentaW.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaClienteVentaW.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProd.removeRow(0);
                }
            }else{
                String m = "Venta no encontrada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDVentaWBuscar.setText("");
            }
        }else{
            controladorVentasW = new ControladorVentaWeb();
            tablaVentasW.setModel(controladorVentasW.getModelo());
            
            int row = tablaProdVentaW.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentaW.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            
            row = tablaClienteVentaW.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaClienteVentaW.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
        }
    }//GEN-LAST:event_iconBuscarVentaWMouseClicked

    private void tablaVentasWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasWMouseClicked
        if(tablaVentasW.getRowCount() != 0){
            renglonVentaWSeleccionado = tablaVentasW.rowAtPoint(evt.getPoint());
            columnVentaWSeleccionado = tablaVentasW.columnAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaVentasWMouseClicked

    private void btnMarcarVentaWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarcarVentaWActionPerformed
        boolean seleccionado = false;
        int filas = tablaVentasW.getRowCount();
        for(int i = 0;i < filas; i++){
            if(tablaVentasW.isRowSelected(i))
                seleccionado = true;                
        }
        if((filas != 0) && (seleccionado != false)){
            controladorVentasW.setVentaWebActual(controladorVentasW.buscarVentaWPorID(String.valueOf(tablaVentasW.getValueAt(renglonVentaWSeleccionado,0))));
            VentaWeb venta = controladorVentasW.getVentaWebActual();
            if(venta.getEstado().equals("Entregado")){
                String m = "Esta venta ya ha sido entregada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }else{
                venta.setEstado("Entregado");
                if(controladorVentasW.actualizaVentaW()){
                    controladorVentasW = new ControladorVentaWeb();
                    tablaVentasW.setModel(controladorVentasW.getModelo());

                    String m = "Venta marcada como entregada.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
            }
        }else{
            String m = "No hay una venta seleccionada.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnMarcarVentaWActionPerformed

    private void btnAceptarFacWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarFacWActionPerformed
        ocultarTodo();
        panelVentasW.setEnabled(true);
        panelVentasW.setVisible(true);
        tablaVentasW.changeSelection(renglonVentaWSeleccionado, columnVentaWSeleccionado, true, false);
        renglonVentaWSeleccionado = 0;
        columnVentaWSeleccionado = 0;
    }//GEN-LAST:event_btnAceptarFacWActionPerformed

    private void btnElimFacturaWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElimFacturaWActionPerformed
        if(!(this.campoIDFacWBuscado.getText().equals(""))){
            AlertaConfirm pregunta = new AlertaConfirm(this, true);
            pregunta.mensaje.setText("¿Desea eliminar?.");
            pregunta.setVisible(true);
            
            boolean respuesta = pregunta.respuesta;
            
            if(respuesta){            
                controladorFacturas.eliminarFactura(Integer.parseInt(this.campoIDFacWBuscado.getText()));
                controladorVentasW = new ControladorVentaWeb();
                tablaVentasW.setModel(controladorVentasW.getModelo());
                
                String m = "Eliminación realizada.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                ocultarTodo();
                panelVentasW.setEnabled(true);
                panelVentasW.setVisible(true);
                tablaVentasW.changeSelection(renglonVentaLSeleccionado, columnVentaLSeleccionado, true, false);
                this.campoIDFacWBuscado.setText("");
            }            
        }
    }//GEN-LAST:event_btnElimFacturaWActionPerformed

    private void iconBuscarClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarClienteMouseClicked
        if(!(this.campoIDClienteBuscar.getText().equals(""))){
            controladorClienteG = new ControladorClienteGeneral("ID");
            if(controladorClienteG.buscarClienteGAct(this.campoIDClienteBuscar.getText()))
                tablaClientes.setModel(controladorClienteG.getModelo());
            else{
                String m = "Cliente no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDClienteBuscar.setText("");
            }
                
        }else{
            controladorClienteG = new ControladorClienteGeneral("ID");
            tablaClientes.setModel(controladorClienteG.getModelo());
        }
    }//GEN-LAST:event_iconBuscarClienteMouseClicked

    private void btnModiClienteBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModiClienteBDActionPerformed
        controladorClientesL = new ControladorClienteLocal();
        String nombre = this.campoNomClienteModi.getText();
        String apellidos = this.campoApeClienteModi.getText();
        String edad = this.campoEdadClienteModi.getText();
        String telefono = this.campoTelClienteModi.getText();
        String rfc = this.campoRfcClienteModi.getText();
        String dom = this.campoDomClienteModi.getText();
        String em = this.campoEmClienteModi.getText();
        String mail = this.campoMailClienteModi.getText();
        
        //formato para validar el correo
        String formato = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Pattern pat = Pattern.compile(formato);
        
        if(!(this.campoIDClienEncon.getText().equals(""))){
            if((nombre.equals("")) || (apellidos.equals("")) || (edad.equals("")) || (telefono.equals(""))
                     || (rfc.equals("")) || (dom.equals("")) || (em.equals("")) || (mail.equals(""))){
                String m = "Hay campos vacíos.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
            else if(telefono.length() != 10){
                String m = "Teléfono inválido.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
            }
            else{
                //se valida el correo según el formato 
                //Ejm. de correo válido: prueba.P1@prueba
                /*Ejm. de correo inválido: @prueba
                                           prueba.P1 prueba
                */
                Matcher mather = pat.matcher(mail);
                if(mather.find() == true){
                    controladorClientesL.setClienteLActual(controladorClientesL.buscarClienteLPorID(this.campoIDClienEncon.getText()));
                    ClienteLocal cliente = controladorClientesL.getClienteLActual();
                    cliente.setNombre(nombre);
                    cliente.setApellidos(apellidos);
                    cliente.setEdad(Integer.parseInt(edad));
                    cliente.setTelefono(telefono);
                    cliente.setRfc(rfc);
                    cliente.setDomicilio_f(dom);
                    cliente.setNom_empresa(em);
                    cliente.setCorreo(mail);
                    if(controladorClientesL.actualizaClienteL()){
                        controladorClienteG = new ControladorClienteGeneral("ID");
                        tablaClientes.setModel(controladorClienteG.getModelo());
                        
                        String m = "Actualización realizada.";
                        AlertaWarning salir = new AlertaWarning(this, true);
                        salir.mensaje.setText(m);
                        salir.setVisible(true);
                        
                        ocultarTodo();
                        panelClientes.setEnabled(true);
                        panelClientes.setVisible(true);
                    }
                }
                else{
                    String m = "Correo inválido.";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(m);
                    salir.setVisible(true);
                }
            }
        }else{
            String m = "No se ha buscado un cliente.";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
        
    }//GEN-LAST:event_btnModiClienteBDActionPerformed

    private void btnCancelarClienModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarClienModiActionPerformed
        this.campoIDClienteModi.setText("");
        this.campoNomClienteModi.setText("");
        this.campoApeClienteModi.setText("");
        this.campoEdadClienteModi.setText("");
        this.campoTelClienteModi.setText("");
        this.campoRfcClienteModi.setText("");
        this.campoDomClienteModi.setText("");
        this.campoEmClienteModi.setText("");
        this.campoMailClienteModi.setText("");
        this.campoIDClienEncon.setText("");
        ocultarTodo();
        this.panelClientes.setEnabled(true);
        this.panelClientes.setVisible(true);
    }//GEN-LAST:event_btnCancelarClienModiActionPerformed

    private void btnBuscarClienModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienModiActionPerformed
        if(!(this.campoIDClienteModi.getText().equals(""))){
            controladorClientesL = new ControladorClienteLocal();
            ClienteLocal cliente = controladorClientesL.buscarClienteLPorID(this.campoIDClienteModi.getText());
            if(cliente != null){
                this.campoNomClienteModi.setText(cliente.getNombre());
                this.campoApeClienteModi.setText(cliente.getApellidos());
                this.campoEdadClienteModi.setText(""+cliente.getEdad());
                this.campoTelClienteModi.setText(cliente.getTelefono());
                this.campoRfcClienteModi.setText(cliente.getRfc());
                this.campoDomClienteModi.setText(cliente.getDomicilio_f());
                this.campoEmClienteModi.setText(cliente.getNom_empresa());
                this.campoMailClienteModi.setText(cliente.getCorreo());
                this.campoIDClienEncon.setText(""+cliente.getId_cliente_local());
            }else{
                String m = "Cliente no encontrado.";
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText(m);
                salir.setVisible(true);
                
                this.campoIDClienteModi.setText("");
            }
                
        }
    }//GEN-LAST:event_btnBuscarClienModiActionPerformed

    private void btnRecarClienModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecarClienModiActionPerformed
        this.campoIDClienteModi.setText("");
        this.campoNomClienteModi.setText("");
        this.campoApeClienteModi.setText("");
        this.campoEdadClienteModi.setText("");
        this.campoTelClienteModi.setText("");
        this.campoRfcClienteModi.setText("");
        this.campoDomClienteModi.setText("");
        this.campoEmClienteModi.setText("");
        this.campoMailClienteModi.setText("");
        this.campoIDClienEncon.setText("");
    }//GEN-LAST:event_btnRecarClienModiActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        if(tablaClientes.getRowCount() != 0){
            renglonClienteSeleccionado = tablaClientes.rowAtPoint(evt.getPoint());
            columnClienteSeleccionado = tablaClientes.rowAtPoint(evt.getPoint());
        }
    }//GEN-LAST:event_tablaClientesMouseClicked

    private void iconBuscarClienFacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarClienFacMouseClicked
        if(!(this.campoIDClienFacBuscar.getText().equals(""))){
            controladorClientesL = new ControladorClienteLocal();
            ClienteLocal cliente = controladorClientesL.buscarClienteLPorID(this.campoIDClienFacBuscar.getText());
            if(cliente != null){
                this.campoIDClienFacEncon.setText(""+cliente.getId_cliente_local());
                this.campoNomClienNFac.setText(cliente.getNombre());
                this.campoApeClienNFac.setText(cliente.getApellidos());
                this.campoMailClienNFac.setText(cliente.getCorreo());
                this.campoEdadClienNFac.setText(""+cliente.getEdad());
                this.campoTelClienNFac.setText(cliente.getTelefono());
                this.campoRfcClienNFac.setText(cliente.getRfc());
                this.campoEmpClienNFac.setText(cliente.getNom_empresa());
                this.campoDomClienNFac.setText(cliente.getDomicilio_f());
                
                this.campoIDClienFacEncon.setEnabled(false);
                this.campoNomClienNFac.setEnabled(false);
                this.campoApeClienNFac.setEnabled(false);
                this.campoMailClienNFac.setEnabled(false);
                this.campoEdadClienNFac.setEnabled(false);
                this.campoTelClienNFac.setEnabled(false);
                this.campoRfcClienNFac.setEnabled(false);
                this.campoEmpClienNFac.setEnabled(false);
                this.campoDomClienNFac.setEnabled(false);
            }else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Cliente no encontrado.");
                salir.setVisible(true);
                this.campoIDClienFacBuscar.setText("");
            }
        }else{
            this.campoIDClienFacEncon.setText("");
            this.campoNomClienNFac.setText("");
            this.campoApeClienNFac.setText("");
            this.campoMailClienNFac.setText("");
            this.campoEdadClienNFac.setText("");
            this.campoTelClienNFac.setText("");
            this.campoRfcClienNFac.setText("");
            this.campoEmpClienNFac.setText("");
            this.campoDomClienNFac.setText("");
            
            this.campoIDClienFacEncon.setEnabled(true);
            this.campoNomClienNFac.setEnabled(true);
            this.campoApeClienNFac.setEnabled(true);
            this.campoMailClienNFac.setEnabled(true);
            this.campoEdadClienNFac.setEnabled(true);
            this.campoTelClienNFac.setEnabled(true);
            this.campoRfcClienNFac.setEnabled(true);
            this.campoEmpClienNFac.setEnabled(true);
            this.campoDomClienNFac.setEnabled(true);
        }
    }//GEN-LAST:event_iconBuscarClienFacMouseClicked

    private void iconBuscarVentaFacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarVentaFacMouseClicked
        if(!(this.campoIDVentaFacBuscar.getText().equals(""))){
            controladorVentasL = new ControladorVentaLocal("Folio");
            VentaLocal venta = controladorVentasL.buscarVentaLPorID(this.campoIDVentaFacBuscar.getText());
            if(venta != null){
                if(venta.getNum_factura() == 0){
                    ControladorProductosVenta controladorProdVenta = new ControladorProductosVenta(String.valueOf(venta.getId_venta_local()),0);                    tablaProdNuevFacL.setModel(controladorProdVenta.getModelo());
                    this.campoIDVentaFacEncon.setText(""+venta.getId_venta_local());
                    this.campoTotalNFactura.setText(""+venta.getPrecio_total());
                    
                }else{
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("Esta venta ya cuenta con una factura.");
                    salir.setVisible(true);
                }
            }else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Venta no encontrada.");
                salir.setVisible(true);
            }
        }else{
            int row = tablaProdNuevFacL.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNuevFacL.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            this.campoIDVentaFacEncon.setText("");
            this.campoTotalNFactura.setText("");
        }
    }//GEN-LAST:event_iconBuscarVentaFacMouseClicked

    private void btnAcepNuevaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcepNuevaFacturaActionPerformed
        String nombre = this.campoNomClienNFac.getText();
        String apellidos = this.campoApeClienNFac.getText();
        String mail = this.campoMailClienNFac.getText();
        
        //formato para validar el correo
        String formato = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Pattern pat = Pattern.compile(formato);
        
        String edad = this.campoEdadClienNFac.getText();
        String tel = this.campoTelClienNFac.getText();
        String rfc = this.campoRfcClienNFac.getText();
        String empresa = this.campoEmpClienNFac.getText();
        String dom = this.campoDomClienNFac.getText();
        int row = tablaProdNuevFacL.getRowCount();
        ControladorMetodos controladorMetodo = new ControladorMetodos();
        
        if((nombre.equals("")) || (apellidos.equals("")) || (mail.equals("")) || (edad.equals(""))
                || (tel.equals("")) || (rfc.equals("")) || (empresa.equals("")) || (dom.equals("")) 
                || (row == 0)){
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText("Hay campos vacíos.");
            salir.setVisible(true);
        }
        else if(tel.length() != 10){
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText("Teléfono inválido.");
            salir.setVisible(true);
        }
        else{
            //se valida el correo según el formato 
            //Ejm. de correo válido: prueba.P1@prueba
            /*Ejm. de correo inválido: @prueba
                                       prueba.P1 prueba
            */
            Matcher mather = pat.matcher(mail);
            if(mather.find() == true){
                if(this.campoIDClienFacEncon.getText().equals("")){
                    int nuevoID = controladorMetodo.ultimoCliente() + 2;
                    ClienteLocal cliente = new ClienteLocal();
                    cliente.setId_cliente_local(nuevoID);
                    cliente.setNombre(nombre);
                    cliente.setApellidos(apellidos);
                    cliente.setEdad(Integer.parseInt(edad));
                    cliente.setCorreo(mail);
                    cliente.setTelefono(tel);
                    cliente.setRfc(rfc);
                    cliente.setDomicilio_f(dom);
                    cliente.setNom_empresa(empresa);
                    if(controladorClientesL.agregarClienteL(cliente)){
                        int nuevoIDF = controladorMetodo.ultimaFactura() + 2;
                        Facturacion factura = new Facturacion();
                        factura.setNum_factura(nuevoIDF);
                        factura.setFecha_exp(this.campoFechaNFactura.getText());
                        factura.setDesc_servicio("");
                        factura.setPago_total(Float.parseFloat(this.campoTotalNFactura.getText()));
                        factura.setForma_pago(String.valueOf(this.comboFormaNFactura.getSelectedItem()));
                        controladorFacturas = new ControladorFacturacion();
                        if(controladorFacturas.agregarFactura(factura)){
                            controladorVentasL.setVentaLocalActual(controladorVentasL.buscarVentaLPorID(this.campoIDVentaFacEncon.getText()));
                            VentaLocal venta = controladorVentasL.getVentaLocalActual();
                            venta.setId_cliente_local(nuevoID);
                            venta.setNum_factura(nuevoIDF);
                            if(controladorVentasL.actualizaVenta()){
                                AlertaWarning salir = new AlertaWarning(this, true);
                                salir.mensaje.setText("Factura agregada.");
                                salir.setVisible(true);
                                
                                ocultarTodo();
                                panelNuevaVenta.setEnabled(true);
                                panelNuevaVenta.setVisible(true);
                            }
                        }
                    }
                }
                else{
                    int nuevoIDF = controladorMetodo.ultimaFactura() + 2;
                    Facturacion factura = new Facturacion();
                    factura.setNum_factura(nuevoIDF);
                    factura.setFecha_exp(this.campoFechaNFactura.getText());
                    factura.setDesc_servicio("");
                    factura.setPago_total(Float.parseFloat(this.campoTotalNFactura.getText()));
                    factura.setForma_pago(String.valueOf(this.comboFormaNFactura.getSelectedItem()));
                    controladorFacturas = new ControladorFacturacion();
                    if(controladorFacturas.agregarFactura(factura)){
                        controladorVentasL.setVentaLocalActual(controladorVentasL.buscarVentaLPorID(this.campoIDVentaFacEncon.getText()));
                        VentaLocal venta = controladorVentasL.getVentaLocalActual();
                        venta.setId_cliente_local(Integer.parseInt(this.campoIDClienFacEncon.getText()));
                        venta.setNum_factura(nuevoIDF);
                        if(controladorVentasL.actualizaVenta()){
                            AlertaWarning salir = new AlertaWarning(this, true);
                            salir.mensaje.setText("Factura agregada.");
                            salir.setVisible(true);
                            
                            ocultarTodo();
                            panelNuevaVenta.setEnabled(true);
                            panelNuevaVenta.setVisible(true);
                        }
                    }
                }
            }
            else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Correo inválido.");
                salir.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnAcepNuevaFacturaActionPerformed

    private void btnCanNuevaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanNuevaFacturaActionPerformed
        //se limpian los campos y la tabla del apartado
        this.campoIDClienFacEncon.setText("");
        this.campoIDClienFacBuscar.setText("");
        this.campoNomClienNFac.setText("");
        this.campoApeClienNFac.setText("");
        this.campoMailClienNFac.setText("");
        this.campoEdadClienNFac.setText("");
        this.campoTelClienNFac.setText("");
        this.campoRfcClienNFac.setText("");
        this.campoEmpClienNFac.setText("");
        this.campoDomClienNFac.setText("");

        int rowF = tablaProdNuevFacL.getRowCount();
        if(rowF != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNuevFacL.getModel();
            for(int i = 0 ; i < rowF ; i++)
                modeloProd.removeRow(0);
        }
        this.campoIDVentaFacEncon.setText("");
        this.campoTotalNFactura.setText("");
        this.campoIDVentaFacBuscar.setText("");
        //se regresa al apartado de nueva ventaL
        ocultarTodo();
        panelNuevaVenta.setEnabled(true);
        panelNuevaVenta.setVisible(true);
    }//GEN-LAST:event_btnCanNuevaFacturaActionPerformed

    private void iconBuscarDevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBuscarDevMouseClicked
        if(!(this.campoIDDevBuscar.getText().equals(""))){
            controladorDev = new ControladorDevoluciones("ID");
            if(controladorDev.buscarDevAct(this.campoIDDevBuscar.getText()))
                tablaDev.setModel(controladorDev.getModelo());
            else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Devolucion no encontrada.");
                salir.setVisible(true);
                
                this.campoIDDevBuscar.setText("");
            }
        }else{
            controladorDev = new ControladorDevoluciones("ID");
            tablaDev.setModel(controladorDev.getModelo());
        }
        
        if(tablaProdSelecDev.getRowCount() != 0){
            int row = tablaProdSelecDev.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecDev.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProdS.removeRow(0);
            }
        }
    }//GEN-LAST:event_iconBuscarDevMouseClicked

    private void comboOrdenarDevItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarDevItemStateChanged
        //controladorDev = new ControladorDevoluciones();
        if(comboOrdenarDev.getSelectedItem().equals("ID Devolucion")){
            controladorDev = new ControladorDevoluciones("ID");
        }
        if(comboOrdenarDev.getSelectedItem().equals("Fecha descendente")){
            controladorDev = new ControladorDevoluciones("FechaDESC");
        }
        if(comboOrdenarDev.getSelectedItem().equals("Fecha ascendente")){
            controladorDev = new ControladorDevoluciones("FechaASC");
        }
        tablaDev.setModel(controladorDev.getModelo());
        if(tablaProdSelecDev.getRowCount() != 0){
            int row = tablaProdSelecDev.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecDev.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProdS.removeRow(0);
            }
        }
    }//GEN-LAST:event_comboOrdenarDevItemStateChanged

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

    private void comboOrdenarVentasLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarVentasLItemStateChanged
        if(comboOrdenarVentasL.getSelectedItem().equals("Folio")){
            controladorVentasL = new ControladorVentaLocal("Folio");
            tablaVentasL.setModel(controladorVentasL.getModelo());
        }
        if(comboOrdenarVentasL.getSelectedItem().equals("Fecha ascendente")){
            controladorVentasL = new ControladorVentaLocal("FechaASC");
            tablaVentasL.setModel(controladorVentasL.getModelo());
        }
        if(comboOrdenarVentasL.getSelectedItem().equals("Fecha descendente")){
            controladorVentasL = new ControladorVentaLocal("FechaDESC");
            tablaVentasL.setModel(controladorVentasL.getModelo());
        }
        int row = tablaProdVentasL.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentasL.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }
    }//GEN-LAST:event_comboOrdenarVentasLItemStateChanged

    private void comboOrdenarVentasWItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarVentasWItemStateChanged
        if(comboOrdenarVentasW.getSelectedItem().equals("Folio")){
            controladorVentasW = new ControladorVentaWeb();
            tablaVentasW.setModel(controladorVentasW.getModelo());
        }
        if(comboOrdenarVentasW.getSelectedItem().equals("Fecha Compra Descendente")){
            controladorVentasW = new ControladorVentaWeb("");
            tablaVentasW.setModel(controladorVentasW.getModelo());
        }
        if(comboOrdenarVentasW.getSelectedItem().equals("Fecha Entrega Descendente")){
            controladorVentasW = new ControladorVentaWeb("","");
            tablaVentasW.setModel(controladorVentasW.getModelo());
        }
        if(comboOrdenarVentasW.getSelectedItem().equals("Estado")){
            controladorVentasW = new ControladorVentaWeb("","","");
            tablaVentasW.setModel(controladorVentasW.getModelo());
        }
        
        int row = tablaProdVentaW.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdVentaW.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }

        row = tablaClienteVentaW.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaClienteVentaW.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }
        
    }//GEN-LAST:event_comboOrdenarVentasWItemStateChanged

    private void comboOrdenarClientesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarClientesItemStateChanged
        if(comboOrdenarClientes.getSelectedItem().equals("ID")){
            controladorClienteG = new ControladorClienteGeneral("ID");
            tablaClientes.setModel(controladorClienteG.getModelo());
        }
        if(comboOrdenarClientes.getSelectedItem().equals("Nombre")){
            controladorClienteG = new ControladorClienteGeneral("Nombre");
            tablaClientes.setModel(controladorClienteG.getModelo());
        }
        if(comboOrdenarClientes.getSelectedItem().equals("Apellidos")){
            controladorClienteG = new ControladorClienteGeneral("Apellidos");
            tablaClientes.setModel(controladorClienteG.getModelo());
        }
    }//GEN-LAST:event_comboOrdenarClientesItemStateChanged

    private void btnBuscarNuevDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNuevDevActionPerformed
        if(!(this.campoIDVenDevBuscar.getText().equals(""))){
            controladorVentasL = new ControladorVentaLocal("Folio");
            VentaLocal ventaL = controladorVentasL.buscarVentaLPorID(this.campoIDVenDevBuscar.getText());
            controladorVentasW = new ControladorVentaWeb();
            VentaWeb ventaW = controladorVentasW.buscarVentaWPorID(this.campoIDVenDevBuscar.getText());
            if(ventaL != null){
                ControladorProductosDev controladorProdVenta = new ControladorProductosDev(String.valueOf(ventaL.getId_venta_local()));                
                tablaProdNDev.setModel(controladorProdVenta.getModelo());
                this.campoFechaVenNDev.setText(ventaL.getFecha());
                this.campoTotalVenNDev.setText(""+ventaL.getPrecio_total());
                this.campoIDVentaEnDev.setText(""+ventaL.getId_venta_local());
               // System.out.println("Factura l:"+ventaL.getNum_factura());
            }
            else if(ventaW != null){
                if(ventaW.getEstado().equals("Pendiente")){
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText("Esta venta web no ha sido entregada.");
                    salir.setVisible(true);
                    
                    this.campoIDVenDevBuscar.setText("");
                }
                else{
                    ControladorProductosDev controladorProdVenta = new ControladorProductosDev(String.valueOf(ventaW.getId_venta_web()));                
                    tablaProdNDev.setModel(controladorProdVenta.getModelo());
                    this.campoFechaVenNDev.setText(ventaW.getFecha_entrega());
                    this.campoTotalVenNDev.setText(""+ventaW.getPrecio_total());
                    this.campoIDVentaEnDev.setText(""+ventaW.getId_venta_web());
                    System.out.println("Factura l:"+ventaW.getNum_factura());
                }
            }
            else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Venta no encontrada.");
                salir.setVisible(true);
                
                this.campoIDVenDevBuscar.setText("");
            }
            int row = tablaProdSelecNDev.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdSelecNDev.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            
            this.campoProvNDev.setText("");
            this.campoDefectoNDev.setText("");
            this.campoIDProvEn.setText("");
        }else{
            int row = tablaProdNDev.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNDev.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            row = tablaProdSelecNDev.getRowCount();
            if(row != 0){
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdSelecNDev.getModel();
                for(int i = 0 ; i < row ; i++)
                    modeloProd.removeRow(0);
            }
            this.campoFechaVenNDev.setText("");
            this.campoTotalVenNDev.setText("");
            this.campoProvNDev.setText("");
            this.campoDefectoNDev.setText("");
            this.campoIDVentaEnDev.setText("");
            this.campoIDProvEn.setText("");
        }
    }//GEN-LAST:event_btnBuscarNuevDevActionPerformed

    private void btnRecarNuevDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecarNuevDevActionPerformed
        this.campoIDVenDevBuscar.setText("");
        this.campoProvNDev.setText("");
        this.campoFechaVenNDev.setText("");
        this.campoTotalVenNDev.setText("");
        this.campoCantDevolver.setText("");
        this.campoIDVentaEnDev.setText("");
        this.campoIDProvEn.setText("");
        
        int row = tablaProdSelecNDev.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProdS.removeRow(0);
        }
        
        row = tablaProdNDev.getRowCount();
        if(row != 0){
            DefaultTableModel modeloProd = (DefaultTableModel) tablaProdNDev.getModel();
            for(int i = 0 ; i < row ; i++)
                modeloProd.removeRow(0);
        }
        
    }//GEN-LAST:event_btnRecarNuevDevActionPerformed

    private void tablaProdNDevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProdNDevMouseClicked
        if(tablaProdNDev.getRowCount() != 0){
            renglonProdDevSeleccionado = tablaProdNDev.rowAtPoint(evt.getPoint());
            columnProdDevSeleccionado = tablaProdNDev.columnAtPoint(evt.getPoint());
            if(tablaProdSelecNDev.getRowCount() != 0){
                int row = tablaProdSelecNDev.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProdS.removeRow(0);
                }
            }
            if((tablaProdSelecNDev.getRowCount() == 0) && (String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 4)).equals("Funcional"))
                    && (Integer.parseInt((tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 0)).toString()) > 0)){
                String codigo = String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 0));
                String producto = String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 1));
                String cantidad = String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 2));
                String precio = String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 3));
                String estado = String.valueOf(tablaProdNDev.getValueAt(renglonProdDevSeleccionado, 4));
                DefaultTableModel modeloProd = (DefaultTableModel) tablaProdSelecNDev.getModel();                
                modeloProd.addRow(new Object[]{codigo,producto,cantidad,precio,estado});
                controladorProductos = new ControladorProductos("ID");
                ControladorMetodos controladorM = new ControladorMetodos();
                if(controladorM.estadoProducto(Integer.parseInt(codigo)).equals("NE")){
                    Productos prodSelec = controladorProductos.buscarProductoPorID(codigo);
                    controladorProveedor = new ControladorProveedores("ID");
                    Proveedor proveedor = controladorProveedor.buscarProveedorPorID(String.valueOf(prodSelec.getId_proveedor()));
                    this.campoProvNDev.setText(proveedor.getNombre());
                    this.campoDefectoNDev.setText("Dañado");
                    this.campoIDProvEn.setText(""+proveedor.getId_proveedor());
                }else{
                    
                    this.campoIDProvEn.setText(""+controladorM.provProducto(Integer.parseInt(codigo)));
                }
            }
        }
    }//GEN-LAST:event_tablaProdNDevMouseClicked

    private void btnDevolverNDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverNDevActionPerformed
        boolean fechaAceptada = false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date fechaVenta = dateFormat.parse(this.campoFechaVenNDev.getText());
            Calendar fI = Calendar.getInstance();
            fI.setTime(fechaVenta);
            fI.add(Calendar.DATE, 7);
            fechaVenta = fI.getTime();
            Date fechaDev = dateFormat.parse(this.campoFechaNDev.getText());
            fechaAceptada = !(fechaDev.after(fechaVenta));
            
        } catch (ParseException ex) {}
        if(fechaAceptada == true){
            if((tablaProdSelecNDev.getRowCount() != 0) && !(this.campoCantDevolver.getText().equals(""))){
                ControladorMetodos metodoBuscarEstado = new ControladorMetodos();
                String estado = metodoBuscarEstado.estadoProducto(Integer.parseInt(tablaProdSelecNDev.getValueAt(0,0).toString()));
                if(estado.equals("E") && this.comboTipoNDev.getSelectedItem().equals("Cambio")){
                    String cadena = "<html><center>" + "Producto no encontrado, no se puede<p>realizar un cambio del producto." + "<center></html>";
                    AlertaWarning salir = new AlertaWarning(this, true);
                    salir.mensaje.setText(cadena);
                    salir.setVisible(true);
                }
                else{
                    int cantidadIn = Integer.parseInt(this.campoCantDevolver.getText());
                    int cantidadEx = Integer.parseInt(String.valueOf(tablaProdSelecNDev.getValueAt(0,2)));
                    if(cantidadIn != 0){
                        if(cantidadIn <= cantidadEx){
                            Devoluciones dev = new Devoluciones();
                            dev.setId_devolucion(0);
                            dev.setId_venta(Integer.parseInt(this.campoIDVentaEnDev.getText()));
                            dev.setId_producto(Integer.parseInt(String.valueOf(tablaProdSelecNDev.getValueAt(0,0))));
                            dev.setId_proveedor(Integer.parseInt(this.campoIDProvEn.getText()));
                            dev.setDefecto(String.valueOf(this.comboRazonDev.getSelectedItem()));
                            dev.setFecha(this.campoFechaNDev.getText());
                            String tipo = String.valueOf(this.comboTipoNDev.getSelectedItem());
                            if(tipo.equals("Efectivo"))
                                tipo = "EF";
                            else
                                tipo = "CP";
                            dev.setTipo(tipo);
                            dev.setCantidad(Integer.parseInt(this.campoCantDevolver.getText()));

                            boolean seguir = true;
                            if(dev.getTipo().equals("CP")){
                                controladorProductos.setProductoActual(controladorProductos.buscarProductoPorID(String.valueOf(tablaProdSelecNDev.getValueAt(0,0))));
                                Productos producto = controladorProductos.getProductoActual();
                                if(producto.getStock() < dev.getCantidad()){
                                    String m = "<html><center>" + "La cantidad es mayor al Stock<p> del producto." + "<center></html>";
                                    AlertaWarning salir = new AlertaWarning(this, true);
                                    salir.mensaje.setText(m);
                                    salir.setVisible(true);
                                    
                                    AlertaConfirm preguntaPedido = new AlertaConfirm(this, true);
                                    preguntaPedido.mensaje.setText("¿Desea agregar una alerta de pedido del producto?");
                                    preguntaPedido.setVisible(true);

                                    boolean respuesta = preguntaPedido.respuesta;

                                    if(respuesta){
                                        AlertaPedido nuevaAlerta = new AlertaPedido();
                                        Calendar calendario = Calendar.getInstance();
                                        String dia = "" + calendario.get(Calendar.DATE);
                                        String mes = "" + (calendario.get(Calendar.MONTH)+1);
                                        String anio = "" + calendario.get(Calendar.YEAR);

                                        nuevaAlerta.setId_producto(producto.getId_producto());
                                        nuevaAlerta.setCantidad(dev.getCantidad());
                                        nuevaAlerta.setRazon("Devolución");
                                        nuevaAlerta.setFecha(anio+"-"+mes+"-"+dia);
                                        nuevaAlerta.setEstado("NL");

                                        if(controladorAlertasPedido.agregarAlerta(nuevaAlerta)){
                                            AlertaWarning salirA = new AlertaWarning(this, true);
                                            salirA.mensaje.setText("Se agregó la nueva alerta.");
                                            salirA.setVisible(true);
                                        }
                                    }

                                    seguir = false;
                                }
                            }

                            if(seguir == true){
                                ControladorMetodos controladorMetodos = new ControladorMetodos();
                                if(controladorDev.agregarDevolucion(dev)){

                                    int cant = controladorMetodos.cantidadProdVenta(dev.getId_venta(), dev.getId_producto());
                                    if((cantidadIn == cantidadEx) && (cant == 1)){
                                        ControladorProductosVenta controladorProd = new ControladorProductosVenta();
                                        ProductosVenta prod = new ProductosVenta();
                                        prod.setId_venta(dev.getId_venta());
                                        prod.setId_producto(dev.getId_producto());
                                        prod.setCant_producto(cantidadEx);
                                        if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("EF"))){
                                            prod.setEstado("Dañado_Efectivo"); //no se toma en cuenta al contabilizar la venta
                                        }
                                        if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("CP"))){
                                            prod.setEstado("Dañado_Cambiado"); //no se toma en cuenta al contabilizar la venta
                                        }
                                        if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("EF"))){
                                            prod.setEstado("Funcional_Efectivo"); //no se toma en cuenta al contabilizar la venta
                                        }
                                        if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("CP"))){
                                            prod.setEstado("Funcional_Cambiado"); //sí se toma en cuenta al contabilizar la venta
                                        }

                                        if(controladorProd.actualizaProdDev(prod)){
                                            AlertaWarning salir = new AlertaWarning(this, true);
                                            salir.mensaje.setText("Devolución realizada.");
                                            salir.setVisible(true);
                                            
                                            controladorProductos.actualizarDisponibiilidad();
                                            
                                            this.campoCantDevolver.setText("");
                                            this.campoTotalVenNDev.setText("");
                                            this.campoFechaVenNDev.setText("");
                                            int row = tablaProdSelecNDev.getRowCount();
                                            if(row != 0){
                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                for(int i = 0 ; i < row ; i++)
                                                    modeloProdS.removeRow(0);
                                            }
                                            row = tablaProdNDev.getRowCount();
                                            if(row != 0){
                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                for(int i = 0 ; i < row ; i++)
                                                    modeloProdS.removeRow(0);
                                            }
                                            this.campoIDVenDevBuscar.setText("");
                                        }
                                    }
                                    else{
                                        //System.out.println(cant);
                                        if(cant > 1){
                                            ControladorProductosVenta controladorProd = new ControladorProductosVenta();
                                            ProductosVenta productosDevueltos = new ProductosVenta();
                                            String estadoD = "";
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("EF"))){
                                                estadoD = "Dañado_Efectivo";
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("CP"))){
                                                estadoD = "Dañado_Cambiado";
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("EF"))){
                                                estadoD = "Funcional_Efectivo";
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("CP"))){
                                                estadoD = "Funcional_Cambiado";
                                            }
                                            
                                            int existeRegistro = controladorMetodos.existeProdEstado(dev.getId_venta(), dev.getId_producto(), estadoD);
                                            System.out.println("Existe registro con ese estado: " + existeRegistro);
                                            int cantDañadoEx = controladorMetodos.cantidadProdDañado(dev.getId_venta(), dev.getId_producto(), estadoD);

                                            productosDevueltos.setId_venta(dev.getId_venta());
                                            productosDevueltos.setId_producto(dev.getId_producto());
                                            productosDevueltos.setCant_producto(cantidadIn + cantDañadoEx);
                                            productosDevueltos.setEstado(estadoD);
                                            productosDevueltos.setPrecio_venta(Float.parseFloat(String.valueOf(tablaProdSelecNDev.getValueAt(0,3))));
                                            
                                            ProductosVenta productoActualizado = new ProductosVenta();
                                            productoActualizado.setId_venta(dev.getId_venta());
                                            productoActualizado.setId_producto(dev.getId_producto());
                                            productoActualizado.setCant_producto(cantidadEx-cantidadIn);
                                            productoActualizado.setEstado("Funcional");

                                            if((cantidadEx-cantidadIn) == 0){
                                                if(controladorProd.eliminarProdV(productoActualizado.getId_venta(),productoActualizado.getId_producto())){
                                                    if(existeRegistro == 1){ //existe registro con el mismo estado
                                                        if(controladorProd.actualizaProdDevDañado(productosDevueltos)){
                                                            AlertaWarning salir = new AlertaWarning(this, true);
                                                            salir.mensaje.setText("Devolución realizada.");
                                                            salir.setVisible(true);

                                                            controladorProductos.actualizarDisponibiilidad();

                                                            this.campoCantDevolver.setText("");
                                                            this.campoTotalVenNDev.setText("");
                                                            this.campoFechaVenNDev.setText("");
                                                            int row = tablaProdSelecNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            row = tablaProdNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            this.campoIDVenDevBuscar.setText("");
                                                        }
                                                    }else{
                                                        if(controladorProd.agregarProdVL(productosDevueltos)){
                                                            AlertaWarning salir = new AlertaWarning(this, true);
                                                            salir.mensaje.setText("Devolución realizada.");
                                                            salir.setVisible(true);

                                                            controladorProductos.actualizarDisponibiilidad();

                                                            this.campoCantDevolver.setText("");
                                                            this.campoTotalVenNDev.setText("");
                                                            this.campoFechaVenNDev.setText("");
                                                            int row = tablaProdSelecNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            row = tablaProdNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            this.campoIDVenDevBuscar.setText("");
                                                        }
                                                    }
                                                    
                                                }
                                            }
                                            else{
                                                if(controladorProd.actualizaProdDevDañado(productoActualizado)){
                                                    if(existeRegistro == 1){ //existe registro con el mismo estado
                                                        if(controladorProd.actualizaProdDevDañado(productosDevueltos)){
                                                            AlertaWarning salir = new AlertaWarning(this, true);
                                                            salir.mensaje.setText("Devolución realizada.");
                                                            salir.setVisible(true);

                                                            controladorProductos.actualizarDisponibiilidad();

                                                            this.campoCantDevolver.setText("");
                                                            this.campoTotalVenNDev.setText("");
                                                            this.campoFechaVenNDev.setText("");
                                                            int row = tablaProdSelecNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            row = tablaProdNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            this.campoIDVenDevBuscar.setText("");
                                                        }
                                                    }else{
                                                        if(controladorProd.agregarProdVL(productosDevueltos)){
                                                            AlertaWarning salir = new AlertaWarning(this, true);
                                                            salir.mensaje.setText("Devolución realizada.");
                                                            salir.setVisible(true);

                                                            controladorProductos.actualizarDisponibiilidad();

                                                            this.campoCantDevolver.setText("");
                                                            this.campoTotalVenNDev.setText("");
                                                            this.campoFechaVenNDev.setText("");
                                                            int row = tablaProdSelecNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            row = tablaProdNDev.getRowCount();
                                                            if(row != 0){
                                                                DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                                for(int i = 0 ; i < row ; i++)
                                                                    modeloProdS.removeRow(0);
                                                            }
                                                            this.campoIDVenDevBuscar.setText("");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            ControladorProductosVenta controladorProd = new ControladorProductosVenta();
                                            ProductosVenta productosDevueltos = new ProductosVenta();
                                            productosDevueltos.setId_venta(dev.getId_venta());
                                            productosDevueltos.setId_producto(dev.getId_producto());
                                            productosDevueltos.setCant_producto(cantidadIn);
                                            
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("EF"))){
                                                productosDevueltos.setEstado("Dañado_Efectivo"); //no se toma en cuenta al contabilizar la venta
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("Dañado")) && (dev.getTipo().equals("CP"))){
                                                productosDevueltos.setEstado("Dañado_Cambiado"); //no se toma en cuenta al contabilizar la venta
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("EF"))){
                                                productosDevueltos.setEstado("Funcional_Efectivo"); //no se toma en cuenta al contabilizar la venta
                                            }
                                            if((String.valueOf(this.comboRazonDev.getSelectedItem()).equals("No deseado")) && (dev.getTipo().equals("CP"))){
                                                productosDevueltos.setEstado("Funcional_Cambiado"); //sí se toma en cuenta al contabilizar la venta
                                            }
                                            
                                            productosDevueltos.setPrecio_venta(Float.parseFloat(String.valueOf(tablaProdSelecNDev.getValueAt(0,3))));
                                            productosDevueltos.setNombre(tablaProdSelecNDev.getValueAt(0,1).toString());


                                            ProductosVenta productoActualizado = new ProductosVenta();
                                            productoActualizado.setId_venta(dev.getId_venta());
                                            productoActualizado.setId_producto(dev.getId_producto());
                                            productoActualizado.setCant_producto(cantidadEx-cantidadIn);
                                            productoActualizado.setEstado("Funcional");

                                            if(controladorProd.actualizaProdDevDañado(productoActualizado)){
                                                if(controladorProd.agregarProdVL(productosDevueltos)){
                                                    AlertaWarning salir = new AlertaWarning(this, true);
                                                    salir.mensaje.setText("Devolución realizada.");
                                                    salir.setVisible(true);
                                                    
                                                    controladorProductos.actualizarDisponibiilidad();
                                                    
                                                    this.campoCantDevolver.setText("");
                                                    this.campoTotalVenNDev.setText("");
                                                    this.campoFechaVenNDev.setText("");
                                                    int row = tablaProdSelecNDev.getRowCount();
                                                    if(row != 0){
                                                        DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecNDev.getModel();
                                                        for(int i = 0 ; i < row ; i++)
                                                            modeloProdS.removeRow(0);
                                                    }
                                                    row = tablaProdNDev.getRowCount();
                                                    if(row != 0){
                                                        DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdNDev.getModel();
                                                        for(int i = 0 ; i < row ; i++)
                                                            modeloProdS.removeRow(0);
                                                    }
                                                    this.campoIDVenDevBuscar.setText("");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            AlertaWarning salir = new AlertaWarning(this, true);
                            salir.mensaje.setText("Cantidad ingresada mayor a lo comprado.");
                            salir.setVisible(true);

                            this.campoCantDevolver.setText("");
                        } 
                    }
                }
                
            }
            else{
                AlertaWarning salir = new AlertaWarning(this, true);
                salir.mensaje.setText("Hay campos vacíos.");
                salir.setVisible(true);
            }
        }
        else{
            String m = "<html><center>" + "La fecha de la venta no cumple<p>con los siete días hábiles." + "<center></html>";
            AlertaWarning salir = new AlertaWarning(this, true);
            salir.mensaje.setText(m);
            salir.setVisible(true);
        }
    }//GEN-LAST:event_btnDevolverNDevActionPerformed

    private void campoIDProdBuscadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdBuscadoKeyTyped
        if(!(this.campoIDProdBuscado.getText().length() >= 10)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if ((!numeros))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDProdBuscadoKeyTyped

    private void campoCantProdBuscadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoCantProdBuscadoKeyTyped
        if(!(this.campoCantProdBuscado.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoCantProdBuscadoKeyTyped

    private void campoPagaConNVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPagaConNVKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
            evt.consume();
        if(evt.getKeyChar() == '.' && campoPagaConNV.getText().contains("."))
            evt.consume();
    }//GEN-LAST:event_campoPagaConNVKeyTyped

    private void campoIDClienFacBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDClienFacBuscarKeyTyped
        if(!(this.campoIDClienFacBuscar.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDClienFacBuscarKeyTyped

    private void campoIDVentaFacBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDVentaFacBuscarKeyTyped
        if(!(this.campoIDVentaFacBuscar.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDVentaFacBuscarKeyTyped

    private void campoIDDevBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDDevBuscarKeyTyped
        if(!(this.campoIDDevBuscar.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDDevBuscarKeyTyped

    private void campoIDVenDevBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDVenDevBuscarKeyTyped
        if(!(this.campoIDVenDevBuscar.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDVenDevBuscarKeyTyped

    private void campoCantDevolverKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoCantDevolverKeyTyped
        if(!(this.campoCantDevolver.getText().length() >= 11)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoCantDevolverKeyTyped

    private void campoIDProdInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdInventarioKeyTyped
        if(!(this.campoIDProdInventario.getText().length() >= 10)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDProdInventarioKeyTyped

    private void campoIDProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDProdModiKeyTyped
        if(!(this.campoIDProdModi.getText().length() >= 10)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
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

    private void campoIDVentaLlBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDVentaLlBuscarKeyTyped
        if(this.campoIDVentaLlBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDVentaLlBuscarKeyTyped

    private void campoIDVentaWBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDVentaWBuscarKeyTyped
        if(this.campoIDVentaWBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDVentaWBuscarKeyTyped

    private void campoIDClienteBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDClienteBuscarKeyTyped
        if(this.campoIDClienteBuscar.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDClienteBuscarKeyTyped

    private void campoIDClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDClienteModiKeyTyped
        if(this.campoIDClienteModi.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDClienteModiKeyTyped

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
        if(this.campoIDProdIEliminado.getText().length() >= 11)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoIDProdIEliminadoKeyTyped

    private void btnMostrarElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarElimActionPerformed
        ocultarTodo();
        panelRegistroElim.setVisible(true);
        panelRegistroElim.setEnabled(true);
        controladorPEliminados = new ControladorProducElim("ID");
        tablaEliminados.setModel(controladorPEliminados.getModelo());
    }//GEN-LAST:event_btnMostrarElimActionPerformed

    private void btnAceptarRegElimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarRegElimActionPerformed
        ocultarTodo();
        panelInventario.setVisible(true);
        panelInventario.setEnabled(true);
    }//GEN-LAST:event_btnAceptarRegElimActionPerformed

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

    private void tablaNotificacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaNotificacionMouseClicked
        if(tablaNotificacion.getRowCount() != 0){
            renglonNotif = tablaNotificacion.rowAtPoint(evt.getPoint());
            columnNotif = tablaNotificacion.columnAtPoint(evt.getPoint());
            
            //se realiza el evento de selecciona de renglones (marcar las casillas de cada fila)
            if (tablaNotificacion.getValueAt(renglonNotif, 3).equals(true))
                tablaNotificacion.setValueAt(false, renglonNotif, 3);
            else
                tablaNotificacion.setValueAt(true, renglonNotif, 3);
        }
    }//GEN-LAST:event_tablaNotificacionMouseClicked

    private void campoNomClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomClienNFacKeyTyped
        if(!(this.campoNomClienNFac.getText().length() >= 30)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoNomClienNFacKeyTyped

    private void campoApeClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoApeClienNFacKeyTyped
        if(!(this.campoApeClienNFac.getText().length() >= 30)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoApeClienNFacKeyTyped

    private void campoMailClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMailClienNFacKeyTyped
        if(this.campoMailClienNFac.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoMailClienNFacKeyTyped

    private void campoEdadClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEdadClienNFacKeyTyped
        if(this.campoEdadClienNFac.getText().length() >= 2)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoEdadClienNFacKeyTyped

    private void campoTelClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTelClienNFacKeyTyped
        if(this.campoTelClienNFac.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoTelClienNFacKeyTyped

    private void campoRfcClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoRfcClienNFacKeyTyped
        if(this.campoRfcClienNFac.getText().length() >= 13)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            boolean mayusculas = key >= 65 && key <= 90;
            if (!(numeros || mayusculas))
                evt.consume();
        }
    }//GEN-LAST:event_campoRfcClienNFacKeyTyped

    private void campoEmpClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEmpClienNFacKeyTyped
        if(this.campoEmpClienNFac.getText().length() >= 25)
            evt.consume();
    }//GEN-LAST:event_campoEmpClienNFacKeyTyped

    private void campoDomClienNFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDomClienNFacKeyTyped
        if(this.campoDomClienNFac.getText().length() >= 70)
            evt.consume();
    }//GEN-LAST:event_campoDomClienNFacKeyTyped

    private void campoNomProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomProdModiKeyTyped
        if(!(this.campoNomProdModi.getText().length() >= 50)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            boolean numeros = key >= 48 && key <= 57;
            if (!(minusculas || mayusculas || espacio || numeros))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoNomProdModiKeyTyped

    private void campoMarcaProdModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMarcaProdModiKeyTyped
        if(!(this.campoMarcaProdModi.getText().length() >= 50)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            boolean numeros = key >= 48 && key <= 57;
            if (!(minusculas || mayusculas || espacio || numeros))
                evt.consume();
        }
        else
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
        if(!(this.campoIDProdElim.getText().length() >= 10)){
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoIDProdElimKeyTyped

    private void campoNomClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomClienteModiKeyTyped
        if(!(this.campoNomClienteModi.getText().length() >= 30)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoNomClienteModiKeyTyped

    private void campoApeClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoApeClienteModiKeyTyped
        if(!(this.campoApeClienteModi.getText().length() >= 30)){
            int key = evt.getKeyChar();
            boolean mayusculas = key >= 65 && key <= 90;
            boolean minusculas = key >= 97 && key <= 122;
            boolean espacio = key == 32;
            if (!(minusculas || mayusculas || espacio))
                evt.consume();
        }
        else
            evt.consume();
    }//GEN-LAST:event_campoApeClienteModiKeyTyped

    private void campoEdadClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEdadClienteModiKeyTyped
        if(this.campoEdadClienteModi.getText().length() >= 2)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoEdadClienteModiKeyTyped

    private void campoTelClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTelClienteModiKeyTyped
        if(this.campoTelClienteModi.getText().length() >= 10)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            if (!numeros)
                evt.consume();
        }
    }//GEN-LAST:event_campoTelClienteModiKeyTyped

    private void campoRfcClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoRfcClienteModiKeyTyped
        if(this.campoRfcClienteModi.getText().length() >= 13)
            evt.consume();
        else{
            int key = evt.getKeyChar();
            boolean numeros = key >= 48 && key <= 57;
            boolean mayusculas = key >= 65 && key <= 90;
            if (!(numeros || mayusculas))
                evt.consume();
        }
    }//GEN-LAST:event_campoRfcClienteModiKeyTyped

    private void campoDomClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDomClienteModiKeyTyped
        if(this.campoDomClienteModi.getText().length() >= 70)
            evt.consume();
    }//GEN-LAST:event_campoDomClienteModiKeyTyped

    private void campoEmClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEmClienteModiKeyTyped
        if(this.campoEmClienteModi.getText().length() >= 25)
            evt.consume();
    }//GEN-LAST:event_campoEmClienteModiKeyTyped

    private void campoMailClienteModiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMailClienteModiKeyTyped
        if(this.campoMailClienteModi.getText().length() >= 50)
            evt.consume();
    }//GEN-LAST:event_campoMailClienteModiKeyTyped

    private void tablaDevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDevMouseClicked
        if(tablaDev.getRowCount() != 0){
            renglonProdDevSeleccionado = tablaDev.rowAtPoint(evt.getPoint());
            columnProdDevSeleccionado = tablaDev.columnAtPoint(evt.getPoint());
            if(tablaProdSelecDev.getRowCount() != 0){
                int row = tablaProdSelecDev.getRowCount();
                if(row != 0){
                    DefaultTableModel modeloProdS = (DefaultTableModel) tablaProdSelecDev.getModel();
                    for(int i = 0 ; i < row ; i++)
                        modeloProdS.removeRow(0);
                }
            }
            if(tablaProdSelecDev.getRowCount() == 0){
                String codigoProd = String.valueOf(tablaDev.getValueAt(renglonProdDevSeleccionado, 2));
                
                controladorProductos = new ControladorProductos("ID");
                Productos producto = controladorProductos.buscarProductoPorID(codigoProd);
                
                if(producto != null){
                    DefaultTableModel modeloProd = (DefaultTableModel) tablaProdSelecDev.getModel();                
                    modeloProd.addRow(new Object[]{producto.getId_producto(),producto.getId_proveedor(),producto.getNombre(),producto.getStock(),producto.getPrecioV()});
                }
            }
        }
    }//GEN-LAST:event_tablaDevMouseClicked
    
    
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
            java.util.logging.Logger.getLogger(AreaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AreaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AreaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AreaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
   
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AreaVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcepNuevaFactura;
    private javax.swing.JButton btnAcepNuevaVenta;
    private javax.swing.JButton btnAceptarFacL;
    private javax.swing.JButton btnAceptarFacW;
    private javax.swing.JButton btnAceptarRegElim;
    private javax.swing.JButton btnAgregarProd;
    private javax.swing.JButton btnBuscarClienModi;
    private javax.swing.JButton btnBuscarNuevDev;
    private javax.swing.JButton btnBuscarProdElim;
    private javax.swing.JButton btnBuscarProdModi;
    private javax.swing.JButton btnCanNuevaFactura;
    private javax.swing.JButton btnCanNuevaVenta;
    private javax.swing.JButton btnCancelarClienModi;
    private javax.swing.JButton btnCancelarProdElim;
    private javax.swing.JButton btnCancelarProdModi;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnDevolucion;
    private javax.swing.JButton btnDevolverNDev;
    private javax.swing.JButton btnElimCliente;
    private javax.swing.JButton btnElimFacturaL;
    private javax.swing.JButton btnElimFacturaW;
    private javax.swing.JButton btnElimProdAgregado;
    private javax.swing.JButton btnElimProdCant;
    private javax.swing.JButton btnElimProducto;
    private javax.swing.JButton btnElimProductoBD;
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnMarcarVentaW;
    private javax.swing.JButton btnModiCantAgregado;
    private javax.swing.JButton btnModiCliente;
    private javax.swing.JButton btnModiClienteBD;
    private javax.swing.JButton btnModiProducto;
    private javax.swing.JButton btnModiProductoBD;
    private javax.swing.JButton btnMostrarElim;
    private javax.swing.JButton btnMostrarVentaL;
    private javax.swing.JButton btnMostrarVentaW;
    private javax.swing.JButton btnNotificaciones;
    private javax.swing.JButton btnNuevaDev;
    private javax.swing.JButton btnNuevaFactura;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnRecarClienModi;
    private javax.swing.JButton btnRecarNuevDev;
    private javax.swing.JButton btnRecarProdElim;
    private javax.swing.JButton btnRecarProdModi;
    private javax.swing.JButton btnVentaLocal;
    private javax.swing.JButton btnVentaWeb;
    private javax.swing.JButton btnVerFacturaL;
    private javax.swing.JButton btnVerFacturaW;
    private javax.swing.JTextField campoApeClienNFac;
    private javax.swing.JTextField campoApeClienteModi;
    private javax.swing.JTextField campoCambioNV;
    private javax.swing.JTextField campoCantDevolver;
    private javax.swing.JTextField campoCantElimProdElim;
    private javax.swing.JTextField campoCantProdBuscado;
    private javax.swing.JTextField campoCatProdElim;
    private javax.swing.JTextField campoDefectoNDev;
    private javax.swing.JTextField campoDispProdElim;
    private javax.swing.JTextField campoDomClienNFac;
    private javax.swing.JTextField campoDomClienteFacL;
    private javax.swing.JTextField campoDomClienteModi;
    private javax.swing.JTextField campoDomClienteW;
    private javax.swing.JTextField campoEdadClienNFac;
    private javax.swing.JTextField campoEdadClienteFacL;
    private javax.swing.JTextField campoEdadClienteModi;
    private javax.swing.JTextField campoEdadClienteW;
    private javax.swing.JTextField campoEmClienteModi;
    private javax.swing.JTextField campoEmClienteW;
    private javax.swing.JTextField campoEmpClienNFac;
    private javax.swing.JTextField campoEmpClienteFacL;
    private javax.swing.JTextField campoEntregaFacW;
    private javax.swing.JTextField campoFechaFacL;
    private javax.swing.JTextField campoFechaNDev;
    private javax.swing.JTextField campoFechaNFactura;
    private javax.swing.JTextField campoFechaVenNDev;
    private javax.swing.JTextField campoIDClienEncon;
    private javax.swing.JTextField campoIDClienFacBuscar;
    private javax.swing.JTextField campoIDClienFacEncon;
    private javax.swing.JTextField campoIDClienteBuscar;
    private javax.swing.JTextField campoIDClienteFacL;
    private javax.swing.JTextField campoIDClienteModi;
    private javax.swing.JTextField campoIDClienteW;
    private javax.swing.JTextField campoIDDevBuscar;
    private javax.swing.JTextField campoIDFacWBuscado;
    private javax.swing.JTextField campoIDFacturaL;
    private javax.swing.JTextField campoIDProElimEnc;
    private javax.swing.JTextField campoIDProdBuscado;
    private javax.swing.JTextField campoIDProdElim;
    private javax.swing.JTextField campoIDProdIEliminado;
    private javax.swing.JTextField campoIDProdInventario;
    private javax.swing.JTextField campoIDProdModi;
    private javax.swing.JTextField campoIDProdModiEncon;
    private javax.swing.JTextField campoIDProvEn;
    private javax.swing.JTextField campoIDVenDevBuscar;
    private javax.swing.JTextField campoIDVentaEnDev;
    private javax.swing.JTextField campoIDVentaFacBuscar;
    private javax.swing.JTextField campoIDVentaFacEncon;
    private javax.swing.JTextField campoIDVentaLlBuscar;
    private javax.swing.JTextField campoIDVentaWBuscar;
    private javax.swing.JTextField campoLimiteProdModi;
    private javax.swing.JTextField campoMailClienNFac;
    private javax.swing.JTextField campoMailClienteFacL;
    private javax.swing.JTextField campoMailClienteModi;
    private javax.swing.JTextField campoMailClienteW;
    private javax.swing.JTextField campoMarcaProdElim;
    private javax.swing.JTextField campoMarcaProdModi;
    private javax.swing.JTextField campoNomClienNFac;
    private javax.swing.JTextField campoNomClienteFacL;
    private javax.swing.JTextField campoNomClienteModi;
    private javax.swing.JTextField campoNomClienteW;
    private javax.swing.JTextField campoNomProdElim;
    private javax.swing.JTextField campoNomProdModi;
    private javax.swing.JTextField campoPagaConNV;
    private javax.swing.JTextField campoPrCProdModi;
    private javax.swing.JTextField campoPrVProdModi;
    private javax.swing.JTextField campoPrecioCProdElim;
    private javax.swing.JTextField campoPrecioProd;
    private javax.swing.JTextField campoPrecioVProdElim;
    private javax.swing.JTextField campoProvNDev;
    private javax.swing.JTextField campoRfcClienNFac;
    private javax.swing.JTextField campoRfcClienteFacL;
    private javax.swing.JTextField campoRfcClienteModi;
    private javax.swing.JTextField campoRfcClienteW;
    private javax.swing.JTextField campoStockProdElim;
    private javax.swing.JTextField campoStockProdModi;
    private javax.swing.JTextField campoTelClienNFac;
    private javax.swing.JTextField campoTelClienteFacL;
    private javax.swing.JTextField campoTelClienteModi;
    private javax.swing.JTextField campoTelClienteW;
    private javax.swing.JTextField campoTotalFacL;
    private javax.swing.JTextField campoTotalFacW;
    private javax.swing.JTextField campoTotalNFactura;
    private javax.swing.JTextField campoTotalNVenta;
    private javax.swing.JTextField campoTotalVenNDev;
    private javax.swing.JLabel cerrarAreaVentas;
    private javax.swing.JComboBox<String> comboCatProdModi;
    private javax.swing.JComboBox<String> comboDisProdModi;
    private javax.swing.JComboBox<String> comboEstadoFacW;
    private javax.swing.JComboBox<String> comboFormaFacL;
    private javax.swing.JComboBox<String> comboFormaFacW;
    private javax.swing.JComboBox<String> comboFormaNFactura;
    private javax.swing.JComboBox<String> comboFormaNVenta;
    private javax.swing.JComboBox<String> comboMotElimProd;
    private javax.swing.JComboBox<String> comboOrdenarClientes;
    private javax.swing.JComboBox<String> comboOrdenarDev;
    private javax.swing.JComboBox<String> comboOrdenarElim;
    private javax.swing.JComboBox<String> comboOrdenarInventario;
    private javax.swing.JComboBox<String> comboOrdenarVentasL;
    private javax.swing.JComboBox<String> comboOrdenarVentasW;
    private javax.swing.JComboBox<String> comboRazonDev;
    private javax.swing.JComboBox<String> comboTipoNDev;
    private javax.swing.JLabel fechaSistema;
    private javax.swing.JLabel fondoClientes;
    private javax.swing.JLabel fondoDevoluciones;
    private javax.swing.JLabel fondoElimProducto;
    private javax.swing.JLabel fondoFacturaL;
    private javax.swing.JLabel fondoFacturaW;
    private javax.swing.JLabel fondoInventario;
    private javax.swing.JLabel fondoInventario1;
    private javax.swing.JLabel fondoModiCliente;
    private javax.swing.JLabel fondoModiProducto;
    private javax.swing.JLabel fondoNotificaciones;
    private javax.swing.JLabel fondoNuevaDev;
    private javax.swing.JLabel fondoNuevaFactura;
    private javax.swing.JLabel fondoNuevaVenta;
    private javax.swing.JLabel fondoVentas;
    private javax.swing.JLabel fondoVentasL;
    private javax.swing.JLabel fondoVentasW;
    private javax.swing.JPanel headerVentas;
    private javax.swing.JLabel horaSistema;
    private javax.swing.JLabel iconBuscarClienFac;
    private javax.swing.JLabel iconBuscarCliente;
    private javax.swing.JLabel iconBuscarDev;
    private javax.swing.JLabel iconBuscarProd;
    private javax.swing.JLabel iconBuscarProdEliminado;
    private javax.swing.JLabel iconBuscarProdInven;
    private javax.swing.JLabel iconBuscarVentaFac;
    private javax.swing.JLabel iconBuscarVentaL;
    private javax.swing.JLabel iconBuscarVentaW;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel panelAcCanFac;
    private javax.swing.JPanel panelAcCanFacFacL;
    private javax.swing.JPanel panelAcCanNueFactura;
    private javax.swing.JPanel panelAcFacW;
    private javax.swing.JPanel panelBtnAcepElim;
    private javax.swing.JPanel panelBtnBuscRecClienModi;
    private javax.swing.JPanel panelBtnBuscRecProdElim;
    private javax.swing.JPanel panelBtnBuscRecProdModi;
    private javax.swing.JPanel panelBtnBuscRecProdModi1;
    private javax.swing.JPanel panelBtnCanProdElim;
    private javax.swing.JPanel panelBtnClientes;
    private javax.swing.JPanel panelBtnElimFacL;
    private javax.swing.JPanel panelBtnElimFacW;
    private javax.swing.JPanel panelBtnMarcarVentaW;
    private javax.swing.JPanel panelBtnModiClien;
    private javax.swing.JPanel panelBtnModiProd;
    private javax.swing.JPanel panelBtnMostrarVentaW;
    private javax.swing.JPanel panelBtnNueDev;
    private javax.swing.JPanel panelBtnProduc;
    private javax.swing.JPanel panelBtnProductos;
    private javax.swing.JPanel panelBtnVerFacturaL;
    private javax.swing.JPanel panelCerrar;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelDevoluciones;
    private javax.swing.JPanel panelElimProducto;
    private javax.swing.JPanel panelFacturaL;
    private javax.swing.JPanel panelFacturaW;
    private javax.swing.JPanel panelFechaHeader;
    private javax.swing.JPanel panelInventario;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelModiCliente;
    private javax.swing.JPanel panelModiProducto;
    private javax.swing.JPanel panelNotificaciones;
    private javax.swing.JPanel panelNuevaDev;
    private javax.swing.JPanel panelNuevaFactura;
    private javax.swing.JPanel panelNuevaVenta;
    private javax.swing.JPanel panelRaiz;
    private javax.swing.JPanel panelRegistroElim;
    private javax.swing.JPanel panelTabProdFacW;
    private javax.swing.JPanel panelTablaAlertas;
    private javax.swing.JPanel panelTablaClienteVentaW;
    private javax.swing.JPanel panelTablaClientes;
    private javax.swing.JPanel panelTablaDerecha;
    private javax.swing.JPanel panelTablaDev;
    private javax.swing.JPanel panelTablaEliminados;
    private javax.swing.JPanel panelTablaInventario;
    private javax.swing.JPanel panelTablaNotificaciones;
    private javax.swing.JPanel panelTablaProdFactura;
    private javax.swing.JPanel panelTablaProdNDev;
    private javax.swing.JPanel panelTablaProdNuevFacL;
    private javax.swing.JPanel panelTablaProdSelecNDev;
    private javax.swing.JPanel panelTablaProdSelecNDev1;
    private javax.swing.JPanel panelTablaProdVentaL;
    private javax.swing.JPanel panelTablaVentasL;
    private javax.swing.JPanel panelTablaVentasW;
    private javax.swing.JPanel panelVentasL;
    private javax.swing.JPanel panelVentasW;
    private javax.swing.JPanel panelVerFacturaW;
    private javax.swing.JScrollPane scrollAlertas;
    private javax.swing.JScrollPane scrollClienteVentaW;
    private javax.swing.JScrollPane scrollClientes;
    private javax.swing.JScrollPane scrollDev;
    private javax.swing.JScrollPane scrollEliminados;
    private javax.swing.JScrollPane scrollInventario;
    private javax.swing.JScrollPane scrollNotificacion;
    private javax.swing.JScrollPane scrollProdAgregados;
    private javax.swing.JScrollPane scrollProdBuscadosNV;
    private javax.swing.JScrollPane scrollProdFacW;
    private javax.swing.JScrollPane scrollProdFactura;
    private javax.swing.JScrollPane scrollProdNDev;
    private javax.swing.JScrollPane scrollProdNuevFacL;
    private javax.swing.JScrollPane scrollProdSelecDev;
    private javax.swing.JScrollPane scrollProdSelecNDev;
    private javax.swing.JScrollPane scrollProdVentaW;
    private javax.swing.JScrollPane scrollProdVentasL;
    private javax.swing.JScrollPane scrollVentasL;
    private javax.swing.JScrollPane scrollVentasW;
    private javax.swing.JTable tablaAlertas;
    private javax.swing.JTable tablaClienteVentaW;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaDev;
    private javax.swing.JTable tablaEliminados;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaNotificacion;
    private javax.swing.JTable tablaProdAgregadosNV;
    private javax.swing.JTable tablaProdBuscadosNV;
    private javax.swing.JTable tablaProdFacW;
    private javax.swing.JTable tablaProdFacturaL;
    private javax.swing.JTable tablaProdNDev;
    private javax.swing.JTable tablaProdNuevFacL;
    private javax.swing.JTable tablaProdSelecDev;
    private javax.swing.JTable tablaProdSelecNDev;
    private javax.swing.JTable tablaProdVentaW;
    private javax.swing.JTable tablaProdVentasL;
    private javax.swing.JTable tablaVentasL;
    private javax.swing.JTable tablaVentasW;
    // End of variables declaration//GEN-END:variables
    
        
}

