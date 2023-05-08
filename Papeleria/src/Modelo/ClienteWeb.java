/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author josei
 */
public class ClienteWeb {
    private int id_cliente_web;
    private String nombre;
    private String apellidos;
    private int edad;
    private String correo;
    private String telefono;
    private String rfc;
    private String domicilio_f;
    private String nom_empresa;
    private String contraseña;
    
    public ClienteWeb(){}
    
    public ClienteWeb(int id_cliente_web,String nombre,String apellidos,int edad,String correo,String telefono,
            String rfc,String domicilio_f,String nom_empresa,String contraseña){
        this.id_cliente_web=id_cliente_web;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.edad=edad;
        this.correo=correo;
        this.telefono=telefono;
        this.rfc=rfc;
        this.domicilio_f=domicilio_f;
        this.nom_empresa=nom_empresa;
        this.contraseña=contraseña;
    }

    /**
     * @return the id_cliente_web
     */
    public int getId_cliente_web() {
        return id_cliente_web;
    }

    /**
     * @param id_cliente_web the id_cliente_web to set
     */
    public void setId_cliente_web(int id_cliente_web) {
        this.id_cliente_web = id_cliente_web;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @param apellidos the apellidos to set
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @return the edad
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @param edad the edad to set
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the rfc
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * @param rfc the rfc to set
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * @return the domicilio_f
     */
    public String getDomicilio_f() {
        return domicilio_f;
    }

    /**
     * @param domicilio_f the domicilio_f to set
     */
    public void setDomicilio_f(String domicilio_f) {
        this.domicilio_f = domicilio_f;
    }

    /**
     * @return the nom_empresa
     */
    public String getNom_empresa() {
        return nom_empresa;
    }

    /**
     * @param nom_empresa the nom_empresa to set
     */
    public void setNom_empresa(String nom_empresa) {
        this.nom_empresa = nom_empresa;
    }

    /**
     * @return the contraseña
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * @param contraseña the contraseña to set
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    
    
}
