package models;


import exceptions.DatosInvalidosException;


public class Domiciliario {
    
    private String nombre;
    private int codigo;
    private String ubicacionActual;
    private boolean diponible;
    

    public Domiciliario(String nombre, int codigo, String ubicacionActual) throws DatosInvalidosException{

        if (nombre==null || nombre.trim().isEmpty()){
            throw new DatosInvalidosException("El nombre no puede estar vacio");
        }

        if (nombre.trim().length()<2){
            throw new DatosInvalidosException("El nombre debe tener mas de dos caracteres");
        }

        if (codigo<=0){
            throw new DatosInvalidosException("El codigo no puede ser negativo");
        }

        if (ubicacionActual==null || ubicacionActual.trim().isEmpty()){
            throw new DatosInvalidosException("La ubicacion no puede estar vacia");
        }

        if (ubicacionActual.trim().length()<2){
            throw new DatosInvalidosException("La ubicacion debe tener mas de dos caracteres");
        }

        this.nombre = nombre;
        this.codigo = codigo;
        this.diponible = true;
        this.ubicacionActual = ubicacionActual;
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


    public String getUbicacionActual() {
        return ubicacionActual;
    }


    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }


    public boolean isDiponible() {
        return diponible;
    }


    public void setDiponible(boolean diponible) {
        this.diponible = diponible;
    }

    @Override
    public String toString() {
        return "Domiciliario [nombre=" + nombre + ", codigo=" + codigo + ", ubicacionActual=" + ubicacionActual
                + ", diponible=" + diponible + "]";
    }

}
