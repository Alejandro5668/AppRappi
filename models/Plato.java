package models;

import exceptions.DatosInvalidosException;

public class Plato {
    private String nombre;
    private String categoria;
    private int precio;

    public Plato(String nombre, String categoria, int precio) throws DatosInvalidosException {

        if (nombre == null || nombre.isEmpty()) {
            System.out.println("No se puede dejar el campo de nombre vacio");
        }

        if (nombre.length() < 2) {
            System.out.println("El nombre debe tener mas de dos caracteres");
        }

        if (!categoria.equalsIgnoreCase("entrada") && !categoria.equalsIgnoreCase("plato fuerte") && !categoria.equalsIgnoreCase("bebida")
                && categoria.equals("postre")) {
            System.out.println("Categoria invalida");
        }

        if (precio<0){
            System.out.println("El precio no puede ser negativo");
        }

        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "Plato [nombre=" + nombre + ", categoria=" + categoria + ", Precio: " + precio + "]";
    }

}
