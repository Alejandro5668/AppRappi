package models;

import exceptions.DatosInvalidosException;

public class Cliente {

    private String nombre;
    private String zona;
    private int Codigo;


    public Cliente(String nombre, int Codigo, String zona) throws DatosInvalidosException {

        if (nombre==null || nombre.trim().isEmpty()){
            throw new DatosInvalidosException("El nombre no puede estar vacio");
        }

        if (nombre.length()<2){
            throw new DatosInvalidosException("El nombre debe tener al menos dos caracteres");
        }

        if (Codigo<=0){
            throw new DatosInvalidosException("El codigo no puede ser negativo");
        }

        if (zona==null || zona.trim().isEmpty()){
            throw new DatosInvalidosException("La zona no puede estar vacia");
        }

        this.Codigo = Codigo;
        this.nombre = nombre;
        this.zona = zona;

    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getZona() {
        return zona;
    }


    public void setZona(String zona) {
        this.zona = zona;
    }


    public int getCodigo() {
        return Codigo;
    }


    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    @Override
    public String toString() {
        return "Cliente [nombre=" + nombre + ", zona=" + zona + ", Codigo=" + Codigo + "]";
    }

    
    
}

