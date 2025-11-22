package views;

import java.util.Scanner;

import models.Cliente;
import models.Domiciliario;
import models.Pedido;
import models.Plato;
import models.Restaurante;
import structures.Cola;
import structures.ListaSimple;
import controllers.SistemaPedidos;
import exceptions.DatosInvalidosException;

public class VistaCliente {
    private SistemaPedidos sistema;
    private Cliente clienteActual;
    Scanner Lea = new Scanner(System.in);

    public VistaCliente(SistemaPedidos sistema) {
        this.sistema = sistema;
    }

    public void iniciar() {
        int opcion = 0;

        do {

            System.out.println("Bienvenido a la vista de cliente");
            System.out.println("1) Registrar Cliente\n" +
                    "2) Iniciar Sesion\n" +
                    "3) Salir\n" +
                    "4) Ver clientes\n" +
                    "Que opcion desea ejecutar?:");
            String entrada1 = Lea.nextLine();
            
            if (entrada1.isEmpty()){
                System.out.println("Debe ingresar un numero, intentelo de nuevo");
                continue;
            }

            try{
                opcion = Integer.parseInt(entrada1);
            }catch(NumberFormatException e){
                System.out.println("Solo se permiten numeros, intentelo de nuevo");
                continue;
            }

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    System.out.println("Saliendo del programa....");
                    break;
                case 4:
                    sistema.mostrarClientes();
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcion != 3);
    }

    private void registrarCliente() {
        System.out.println("----Registrar Cliente----");

        try {
            System.out.println("Ingrese el nombre del cliente: ");
            String nombre = Lea.nextLine();

            System.out.println("Ingrese el codigo del cliente: ");
            String codigoStr = Lea.nextLine();
            sistema.listarZonasDisponibles();

            System.out.println("Ingrese la zona del cliente: ");
            String zona = Lea.nextLine();

            int codigoCli = Integer.parseInt(codigoStr);
            Cliente clientenuevo = new Cliente(nombre, codigoCli, zona);
            clienteActual = clientenuevo;
            sistema.registrarCliente(clientenuevo);

            System.out.println("Cliente registrado");
        } catch (NumberFormatException e) {
            System.out.println("El codigo debe ser un numero");
        } catch (DatosInvalidosException e) {
            System.out.println(e.getMessage());
        }

    }

    private void iniciarSesion() {
        System.out.println("----Inicio de sesion----");

        try {
            System.out.print("Código: ");
            String codigoStr = Lea.nextLine().trim();

            if (codigoStr.isEmpty()) {
                System.out.println("Ingrese un código");
                return;
            }

            int codigo = Integer.parseInt(codigoStr);
            Cliente cliente = sistema.ConsultarClienteCodigo(codigo);

            if (cliente != null) {
                clienteActual = cliente;
                System.out.println("Bienvenido, " + cliente.getNombre());
                mostrarOpciones();
            } else {
                System.out.println("Cliente no encontrado");
            }

        } catch (NumberFormatException e) {
            System.out.println("El código debe ser un número");
        }
    }

    private void realizarPedido(Cliente cliente) {
        System.out.println("----REALIZACION DE PEDIDO----");

        try {

            if (sistema.getRestaurantes().isEmpty()) {
                System.out.println("No hay restaurantes registrados actualmente");
                return;
            }

            System.out.println("Restaurantes disponibles: ");
            sistema.verRestaurantes();
            System.out.println();

            System.out.println("Ingrese el codigo del restaurante: ");
            String cod = Lea.nextLine();
            int codigo = Integer.parseInt(cod);
            Restaurante restaurante = sistema.consultarRestaurantePorCodigo(codigo);

            if (restaurante == null) {
                System.out.println("Restaurante no encontrado");
                return;
            }

            if (restaurante.getMenu().isEmpty()) {
                System.out.println("El restaurante no tiene menu todavia");
            } else {
                System.out.println("El menu del restaurante es el siguiente: ");
                mostrarMenuRestaurante(restaurante);
            }

            System.out.println("Ingrese el codigo del pedido: ");
            String codis = Lea.nextLine();
            int codigost = Integer.parseInt(codis);

            Pedido pedido = new Pedido(codigost, cliente, null, restaurante);

            agregarPlatosAlPedido(pedido, restaurante);

            int total = pedido.calcularTotal();
            System.out.println("$Total a pagar: " + total);

            System.out.print("\n¿Confirmar pedido? (s/n): ");
            if (!Lea.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.println("Pedido cancelado");
                return;
            }

            System.out.println("Buscando domiciliario disponible.....");
            Domiciliario domiciliario = sistema.buscarDomiciliarioMasCercano(pedido.getCliente().getZona());

            if (domiciliario == null) {
            if (sistema.getDomiciliarios().isEmpty()) {
                System.out.println("No hay domiciliarios registrados en el sistema");
            } else {
                System.out.println("No hay domiciliarios disponibles en este momento");
            }
            System.out.println("No se puede completar el pedido");
            return; 
        }

            sistema.crearPedido(pedido);
            sistema.asignarDomiciliario(pedido);

            System.out.print("Pedido creado exitosamente");
            System.out.print("Estado: " + pedido.getEstado() + "-" + "Fecha: " + pedido.getFecha());
            if (pedido.getDomiciliario() != null) {
                System.out.print("Domciliario: " + pedido.getDomiciliario().getNombre());
            }

        } catch (NumberFormatException e) {
            System.out.println("Codigo invalido");
        } catch (DatosInvalidosException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cancelarPedido(Cliente cliente) {
        System.out.println("----CANCELAR PEDIDO----");

        Cola<Pedido> temp = new Cola<>();
        ListaSimple<Pedido> misPedidos = new ListaSimple<>();
        int contador = 1;

        while (!sistema.getPedidosActivos().isEmpty()) {
            Pedido p = sistema.getPedidosActivos().desencolar();

            if (p.getCliente().getCodigo() == cliente.getCodigo()) {
                System.out.println(contador + ". Pedido #" + p.getCodigo());
                System.out.println("   Restaurante: " + p.getRestaurante().getNombre());
                System.out.println("   Estado: " + p.getEstado());
                System.out.println("   Total: $" + p.calcularTotal());
                System.out.println();
                misPedidos.insertarFin(p);
                contador++;
            }
            temp.encolar(p);
        }

        while (!temp.isEmpty()) {
            sistema.getPedidosActivos().encolar(temp.desencolar());
        }

        if (misPedidos.isEmpty()) {
            System.out.println("No tienes pedidos activos para cancelar");
            return;
        }

        try {
            System.out.print("Ingrese el codigo del pedido a cancelar: ");
            int codigoPedido = Integer.parseInt(Lea.nextLine().trim());

            boolean encontrado = false;
            for (int i = 0; i < misPedidos.getTamanio(); i++) {
                if (misPedidos.obtener(i).getCodigo() == codigoPedido) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("El pedido no existe o no te pertenece");
                return;
            }

            System.out.print("¿Esta seguro? (s/n): ");
            if (Lea.nextLine().trim().equalsIgnoreCase("s")) {
                sistema.cancelarPedido(codigoPedido);
            } else {
                System.out.println("Cancelacion abortada");
            }

        } catch (NumberFormatException e) {
            System.out.println("Codigo invalido");
        }
    }

    private void verMisPedidos(Cliente cliente) {
        System.out.println("\n----Mis Pedidos----");

        Cola<Pedido> temp = new Cola<>();
        boolean tienePedidos = false;
        int contador = 1;

        while (!sistema.getPedidosActivos().isEmpty()) {
            Pedido p = sistema.getPedidosActivos().desencolar();

            if (p.getCliente().getCodigo() == cliente.getCodigo()) {
                System.out.println("\n" + contador + ". Pedido #" + p.getCodigo());
                System.out.println("   Restaurante: " + p.getRestaurante().getNombre());
                System.out.println("   Estado: " + p.getEstado());
                System.out.println("   Total: $" + p.calcularTotal());
                if (p.getDomiciliario() != null) {
                    System.out.println("   Domiciliario: " + p.getDomiciliario().getNombre());
                }
                System.out.println("   Fecha: " + p.getFecha());

                if (!p.getPlatos().isEmpty()) {
                    System.out.println("   Platos:");
                    for (int j = 0; j < p.getPlatos().getTamanio(); j++) {
                        Plato plato = p.getPlatos().obtener(j);
                        int cantidad = p.getCantidadTotal().obtener(j);
                        System.out.println("      - " + cantidad + "x " + plato.getNombre());
                    }
                }

                tienePedidos = true;
                contador++;
            }
            temp.encolar(p);
        }

        while (!temp.isEmpty()) {
            sistema.getPedidosActivos().encolar(temp.desencolar());
        }

        if (!tienePedidos) {
            System.out.println("No tienes pedidos activos");
        }
    }

    private void mostrarMenuRestaurante(Restaurante restaurante) {
        System.out.println("\n=== MENU DE " + restaurante.getNombre() + " ===");

        String categorias[] = { "entrada", "plato fuerte", "bebida", "postre" };
        int numPlato = 1;

        for (String categoria : categorias) {
            boolean hayPlatos = false;
            System.out.println("\n--- " + categoria.toUpperCase() + " ---");

            for (int i = 0; i < restaurante.getMenu().getTamanio(); i++) {
                Plato plato = restaurante.getMenu().obtener(i);
                if (plato.getCategoria().equalsIgnoreCase(categoria)) {
                    System.out.println(numPlato + ") " + plato.getNombre() + " - $" + plato.getPrecio());
                    hayPlatos = true;
                    numPlato++;
                }
            }

            if (!hayPlatos) {
                System.out.println("(No hay platos)");
            }
        }
    }

    private void agregarPlatosAlPedido(Pedido pedido, Restaurante restaurante) {
        System.out.println("\n----Agregar Platos----");
        String continuar = "s";

        while (continuar.equalsIgnoreCase("s")) {
            System.out.print("\nNumero del plato (1-" + restaurante.getMenu().getTamanio() + "): ");

            try {
                int numPlato = Integer.parseInt(Lea.nextLine().trim());

                if (numPlato >= 1 && numPlato <= restaurante.getMenu().getTamanio()) {
                    Plato plato = restaurante.getMenu().obtener(numPlato - 1);

                    System.out.print("Cantidad: ");
                    int cantidad = Integer.parseInt(Lea.nextLine().trim());

                    if (cantidad > 0) {
                        pedido.agregarPlatoAlPedido(plato, cantidad);
                        System.out.println("✓ " + cantidad + "x " + plato.getNombre());
                    } else {
                        System.out.println("Cantidad invalida");
                    }
                } else {
                    System.out.println("Numero invalido");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido");
            }

            System.out.print("\n¿Agregar otro plato? (s/n): ");
            continuar = Lea.nextLine().trim();
        }
    }

    private void mostrarOpciones() {
        int opcionCliente = 0;
        do {
            System.out.println("----MENU CLIENTE----");
            System.out.println("1) Realizar Pedido\n" +
                    "2) Cancelar Pedido\n" +
                    "3) Ver Mis pedidos\n" +
                    "4) Cerrar Sesion\n" +
                    "Que opcion desea ejecutar: ");
            String entrada = Lea.nextLine();

            if (entrada.isEmpty()){
                System.out.println("Debe ingresar un numero");
                continue;
            }

            try{
                opcionCliente = Integer.parseInt(entrada);
            } catch(NumberFormatException e){
                System.out.println("Solo se permiten numeros, intentelo de nuevo");
                continue;
            }

            switch (opcionCliente) {
                case 1:
                    realizarPedido(clienteActual);
                    break;
                case 2:
                    cancelarPedido(clienteActual);
                    break;
                case 3:
                    verMisPedidos(clienteActual);
                    break;
                case 4:
                    System.out.println("Saliendo del menu de opciones de la vista cliente.....");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcionCliente != 4);
    }
}

