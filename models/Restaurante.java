package models;

import exceptions.DatosInvalidosException;
import structures.ListaSimple;

public class Restaurante {
    private String nombre;
    private int codigo;
    private String zona;
    private ListaSimple<Plato> menu;

    public Restaurante(String nombre, int codigo, String zona) throws DatosInvalidosException {

        if (nombre==null || nombre.trim().isEmpty()){
            throw new DatosInvalidosException("El nombre no puede estar vacio");
        }

        if(codigo<=0){
            throw new DatosInvalidosException("El codigo no puede ser negativo");
        }

        if (zona==null || zona.trim().isEmpty()){
            throw new DatosInvalidosException("La zoan no pued estar vacia");
        }

        this.codigo = codigo;
        this.menu = new ListaSimple<>();
        this.nombre = nombre;
        this.zona = zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public ListaSimple<Plato> getMenu() {
        return menu;
    }

    public void setMenu(ListaSimple<Plato> menu) {
        this.menu = menu;
    }

    public void agregarPlato(Plato plato){
        menu.insertarFin(plato);
    }

    public void mostrarMenu(){
        if (menu.isEmpty()){
            System.out.println("No hay menu que mostrar");
        }
        menu.recorrer();
    }

    @Override
    public String toString() {
        return "Restaurante [nombre=" + nombre + ", codigo=" + codigo + ", zona=" + zona + ", menu=" + menu + "]";
    }

}
