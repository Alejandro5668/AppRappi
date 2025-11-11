package models;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import exceptions.DatosInvalidosException;
import structures.ListaSimple;

public class Pedido {
    private int codigo;
    private String estado;
    private String fecha;
    private Cliente cliente;
    private Restaurante restaurante;
    private Domiciliario domiciliario;
    ListaSimple<Plato> platos;
    ListaSimple<Integer> cantidadTotal;

    public Pedido(int codigo, Cliente cliente, Domiciliario domiciliario, Restaurante restaurante) throws DatosInvalidosException {

        if (domiciliario==null){
            throw new DatosInvalidosException("El domciliario no puede estar vacio");
        }

        if (cliente==null){
            throw new DatosInvalidosException("El cliente no puede estar vacio");
        }

        if (restaurante==null){
            throw new DatosInvalidosException("El restaurante no puede estar vacio");
        }

        this.domiciliario = domiciliario;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.estado = "pendiente";
        this.platos = new ListaSimple<>();
        this.cantidadTotal = new ListaSimple<>();


        // Me da la fecha automaticamente 
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.fecha = LocalDateTime.now().format(formato);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Domiciliario getDomiciliario() {
        return domiciliario;
    }

    public void setDomiciliario(Domiciliario domiciliario) {
        this.domiciliario = domiciliario;
    }

    public void agregarPlatoAlPedido(Plato plato, int cant){
        platos.insertarFin(plato);
        cantidadTotal.insertarFin(cant);
    }

    public int calcularTotal(){
        int total = 0;
        for(int i=0; i<platos.getTamanio(); i++){
            Plato p = platos.obtener(i);
            int cantidad = cantidadTotal.obtener(i);
            total = total + p.getPrecio()*cantidad;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido [codigo=" + codigo + ", estado=" + estado + ", fecha=" + fecha + ", cliente=" + cliente
                + ", restaurante=" + restaurante + ", domiciliario=" + domiciliario + "]";
    }

}
