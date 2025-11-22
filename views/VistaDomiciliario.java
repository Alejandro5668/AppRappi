package controllers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import models.*;
import structures.*;

// Nosotros utilizamos esta clase que oficiara de "Controlador" para nuestro sistema, es la que contendra toda
// La logica del negocio de nuestra app

public class SistemaPedidos {
    ListaSimple<Cliente> clientes;
    ListaSimple<Restaurante> restaurantes;
    ListaSimple<Domiciliario> domiciliarios;
    Cola<Pedido> pedidosActivos;
    Pila<Pedido> pedidosCancelados;
    Pila<Pedido> pedidosEntregados;
    Pila<Pedido> historialPedidos;

    private GrafoZonas grafoZonas;

    public SistemaPedidos() {
        this.clientes = new ListaSimple<>();
        this.domiciliarios = new ListaSimple<>();
        this.restaurantes = new ListaSimple<>();
        this.pedidosActivos = new Cola<>();
        this.historialPedidos = new Pila<>();
        this.pedidosCancelados = new Pila<>();
        this.pedidosEntregados = new Pila<>();
        grafoZonas = new GrafoZonas();
        try {
            grafoZonas.cargarDesdeCSV("structures/conexiones_santamarta.csv");
        } catch (IOException e) {
            System.out.println("Error al cargar el grafo de zonas: " + e.getMessage());
        }
    }

    // ~~~~~~~~~ METODOS PARA CLIENTES ~~~~~~~~~~

    public void registrarCliente(Cliente cliente) {
        clientes.insertarFin(cliente);
        System.out.println("Cliente registrado correctamente");
    }

    public Cliente ConsultarClienteCodigo(int codigo) {
        for (int i = 0; i < clientes.getTamanio(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }

    public ListaSimple<Pedido> historialPedidosCliente(Cliente cliente) {
        ListaSimple<Pedido> historial = new ListaSimple<>();
        Pila<Pedido> tmp = new Pila<>();

        while (!historialPedidos.empty()) {
            Pedido p = historialPedidos.pop();
            if (p.getCliente().getCodigo() == cliente.getCodigo()) {
                historial.insertarFin(p);
            }
            tmp.push(p);
        }

        while (!tmp.empty()) {
            historialPedidos.push(tmp.pop());
        }
        return historial;
    }

    public void mostrarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados todavia....");
            return;
        }
        clientes.recorrer();
    }

    // ~~~~~~~~~ METODOS PARA RESTAURANTES ~~~~~~~~~~

    public void registrarRestaurante(Restaurante restaurante) {
        restaurantes.insertarFin(restaurante);
        System.out.println("Restaurante registrado exitosamente");
    }

    public Restaurante consultarRestaurantePorCodigo(int codigo) {

        for (int i = 0; i < restaurantes.getTamanio(); i++) {
            Restaurante tmp = restaurantes.obtener(i);
            if (tmp.getCodigo() == codigo) {
                return tmp;
            }
        }
        return null;
    }

    public void verRestaurantes() {
        if (restaurantes.isEmpty()) {
            System.out.println("No hay restaurantes registrados todavia");
            return;
        }
        restaurantes.recorrer();
    }

    public Restaurante encontrarRestauranteMasCercano(Domiciliario domiciliario) {
        if (domiciliario == null)
            return null;

        List<String> zonasRestaurantes = new ArrayList<>();
        Nodo<Restaurante> tmpRest = restaurantes.getFrente();
        while (tmpRest != null) {
            zonasRestaurantes.add(tmpRest.getDato().getZona());
            tmpRest = tmpRest.getSiguiente();
        }

        String zonaMasCercana = grafoZonas.encontrarZonaRestauranteMasCercana(
                domiciliario.getUbicacionActual(),
                zonasRestaurantes);

        if (zonaMasCercana != null) {
            Nodo<Restaurante> actual = restaurantes.getFrente();
            while (actual != null) {
                if (actual.getDato().getZona().equals(zonaMasCercana)) {
                    return actual.getDato();
                }
                actual = actual.getSiguiente();
            }
        }
        return null;
    }

    // ~~~~~~~~~ METODOS PARA DOMICILIARIOS ~~~~~~~~~~

    public void registrarDomiciliario(Domiciliario domiciliario) {
        domiciliarios.insertarFin(domiciliario);
        System.out.println("Domiciliario registrado exitosamente");
    }

    public Domiciliario buscarDomiciliarioPorCodigo(int codigo) {

        for (int i = 0; i < domiciliarios.getTamanio(); i++) {
            Domiciliario d = domiciliarios.obtener(i);
            if (d.getCodigo() == codigo) {
                return d;
            }
        }
        return null;
    }

    public Domiciliario consultarDomiciliarioPorCodigo(int codigo) {

        for (int i = 0; i < domiciliarios.getTamanio(); i++) {
            Domiciliario d = domiciliarios.obtener(i);
            if (d.getCodigo() == codigo) {
                return d;
            }
        }
        return null;
    }

    public void verDomiciliarios() {
        if (domiciliarios.isEmpty()) {
            System.out.println("No hay domiciliarios registrados todavia");
            return;
        }

        domiciliarios.recorrer();
    }

    public Domiciliario buscarDomiciliarioMasCercano(String zonaRestaurante) {

        Domiciliario masCercano = null;
        double distanciaMinima = Double.POSITIVE_INFINITY;

        for (int i = 0; i < domiciliarios.getTamanio(); i++) {
            Domiciliario d = domiciliarios.obtener(i);

            if (d.isDiponible()) {
                double distancia = grafoZonas.obtenerDistanciaEntreZonas(
                        d.getUbicacionActual(),
                        zonaRestaurante);

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    masCercano = d;
                }
            }
        }

        return masCercano;

    }

    // ~~~~~~~~~ METODOS PARA PEDIDOS ~~~~~~~~~~

    public void crearPedido(Pedido pedido) {
        pedidosActivos.encolar(pedido);
        historialPedidos.push(pedido);
        System.out.println("Pedido creado exitosamente");
    }

    public void entregarPedido() {
        if (!pedidosActivos.isEmpty()) {
            Pedido p = pedidosActivos.desencolar();
            p.setEstado("Entregado");
            pedidosEntregados.push(p);
        }
    }

    public void cancelarPedido(int codigo) {
        Cola<Pedido> temp = new Cola<>();
        boolean encontrado = false;

        while (!pedidosActivos.isEmpty()) {
            Pedido p = pedidosActivos.desencolar();

            if (p.getCodigo() == codigo) {
                p.setEstado("cancelado");

                if (p.getDomiciliario() != null) {
                    p.getDomiciliario().setDiponible(true);
                }

                pedidosCancelados.push(p);
                encontrado = true;
                System.out.println("Pedido #" + codigo + " cancelado");
            } else {
                temp.encolar(p);
            }
        }
        while (!temp.isEmpty()) {
            pedidosActivos.encolar(temp.desencolar());
        }
        if (!encontrado) {
            System.out.println("Pedido no encontrado");
        }
    }

    public void mostrarPedidosActivos() {
        if (pedidosActivos.isEmpty()) {
            System.out.println("No hay pedidos activos");
        }
        pedidosActivos.imprimir();
    }

    public void mostrarPedidosCancelados() {
        if (pedidosCancelados.empty()) {
            System.out.println("No hay pedidos cancelados");
        }
        pedidosCancelados.print_stack();
    }

    public void mostrarPedidosEntregados() {
        if (pedidosEntregados.empty()) {
            System.out.println("No hay pedidos entregados");
        }
        pedidosEntregados.print_stack();
    }

    public void VerHistorialPedidos() {
        if (!historialPedidos.empty()) {
            historialPedidos.print_stack();
        } else {
            System.out.println("No Hay Pedidos En Historial");
        }
    }

    public void asignarDomiciliario(Pedido pedido) {
        String zonaRestaurante = pedido.getRestaurante().getZona();

        Domiciliario domiciliario = buscarDomiciliarioMasCercano(zonaRestaurante);

        if (domiciliario == null) {
            System.out.println("No hay domiciliarios disponibles");
            return;
        }

        double distancia = grafoZonas.obtenerDistanciaEntreZonas(
                domiciliario.getUbicacionActual(),
                zonaRestaurante);

        pedido.setDomiciliario(domiciliario);
        pedido.setEstado("asignado");
        domiciliario.setDiponible(false);

        System.out.println("Pedido asignado a " + domiciliario.getNombre());
        System.out.println("Distancia: " + distancia + " minutos");
    }

    public void listarZonasDisponibles() {
        Set<String> zonas = grafoZonas.obtenerZonas();

        if (zonas == null || zonas.isEmpty()) {
            System.out.println("No hay zonas cargadas en el grafo");
            return;
        }

        System.out.println("Zonas Disponibles");

        int i = 1;
        for (String zona : zonas) {
            System.out.println(i++ + ". " + zona);
        }
        System.out.println();

    }

    /**
     * Muestra las zonas disponibles y solicita al usuario que ingrese la zona.
     * Se puede invocar desde las vistas al registrar
     * Cliente/Restaurante/Domiciliario.
     * Devuelve la cadena ingresada (zona) o null si no se ingresó nada.
     */
    public String solicitarZonaRegistro() {
        listarZonasDisponibles();
        System.out.print("Ingrese la zona (copie exactamente el nombre): ");
        Scanner sc = new Scanner(System.in);
        String zona = sc.nextLine().trim();
        if (zona.isEmpty())
            return null;
        return zona;
    }

    /**
     * Intento por reflexión de asignar la zona indicada al objeto entidad.
     * Soporta setters comunes: setZona, setUbicacionActual, setUbicacion,
     * setDireccion
     * Devuelve true si se pudo asignar.
     */
    public boolean asignarZonaPorReflexion(Object entidad, String zona) {
        if (entidad == null || zona == null)
            return false;
        String[] posibles = { "setZona", "setUbicacionActual", "setUbicacion", "setDireccion" };
        for (String metodo : posibles) {
            try {
                Method m = entidad.getClass().getMethod(metodo, String.class);
                m.invoke(entidad, zona);
                return true;
            } catch (NoSuchMethodException ns) {
                // seguir probando otros nombres
            } catch (Exception e) {
                // si ocurre otra excepción, reportar y intentar el siguiente
                System.out.println("Error asignando zona mediante " + metodo + ": " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Muestra en consola el camino más corto del domiciliario -> restaurante ->
     * cliente
     * para el pedido dado. Usa el grafo para calcular rutas.
     */
    public void mostrarCaminoCompletoPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("Pedido nulo");
            return;
        }
        if (pedido.getDomiciliario() == null) {
            System.out.println("Pedido sin domiciliario asignado");
            return;
        }
        if (pedido.getRestaurante() == null) {
            System.out.println("Pedido sin restaurante asignado");
            return;
        }

        String origen = pedido.getDomiciliario().getUbicacionActual();
        String zonaRest = pedido.getRestaurante().getZona();
        String zonaCliente = obtenerZonaClientePorReflexion(pedido.getCliente());

        System.out.println("Camino para Pedido #" + pedido.getCodigo() + ":");
        System.out.println("Domiciliario (origen): " + origen);
        System.out.println("Restaurante (zona): " + zonaRest);
        System.out.println("Cliente (zona/destino): " + (zonaCliente != null ? zonaCliente : "Desconocida"));

        // desde domiciliario hasta restaurante
        GrafoZonas.ResultadoCamino tramo1 = grafoZonas.dijkstra(origen, zonaRest);
        if (tramo1 == null || Double.isInfinite(tramo1.distancia)) {
            System.out.println("No se encontró ruta desde el domiciliario hasta el restaurante.");
        } else {
            System.out.println("Ruta domiciliario -> restaurante (" + tramo1.distancia + " minutos): "
                    + String.join(" -> ", tramo1.camino));
        }

        // desde restaurante hasta cliente (si se conoce la zona del cliente)
        if (zonaCliente != null) {
            GrafoZonas.ResultadoCamino tramo2 = grafoZonas.dijkstra(zonaRest, zonaCliente);
            if (tramo2 == null || Double.isInfinite(tramo2.distancia)) {
                System.out.println("No se encontró ruta desde el restaurante hasta el cliente.");
            } else {
                System.out.println("Ruta restaurante -> cliente (" + tramo2.distancia + " minutos): "
                        + String.join(" -> ", tramo2.camino));
            }
        } else {
            System.out.println("No se puede calcular restaurante -> cliente: zona del cliente desconocida.");
        }
    }

    /**
     * Intenta obtener la zona del cliente usando reflexion.
     * Busca getters comunes: getZona, getUbicacion, getDireccion
     * Devuelve null si no encuentra.
     */
    private String obtenerZonaClientePorReflexion(Cliente cliente) {
        if (cliente == null)
            return null;
        String[] getters = { "getZona", "getUbicacion", "getDireccion" };
        for (String g : getters) {
            try {
                Method m = cliente.getClass().getMethod(g);
                Object res = m.invoke(cliente);
                if (res != null)
                    return res.toString();
            } catch (NoSuchMethodException ns) {
                // siguiente intento
            } catch (Exception e) {
                System.out.println("Error al obtener zona del cliente con " + g + ": " + e.getMessage());
            }
        }
        return null;
    }

    // ~~~~~~~~ METODOS PARA GRAFO ~~~~~~~~~

    public GrafoZonas getGrafoZonas() {
        return grafoZonas;
    }

    // ~~~~~~~~ METODOS GETTERS ~~~~~~~~~

    public ListaSimple<Cliente> getClientes() {
        return clientes;
    }

    public ListaSimple<Restaurante> getRestaurantes() {
        return restaurantes;
    }

    public ListaSimple<Domiciliario> getDomiciliarios() {
        return domiciliarios;
    }

    public Cola<Pedido> getPedidosActivos() {
        return pedidosActivos;
    }

    public Pila<Pedido> getPedidosCancelados() {
        return pedidosCancelados;
    }

    public Pila<Pedido> getPedidosEntregados() {
        return pedidosEntregados;
    }

    public Pila<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

}
