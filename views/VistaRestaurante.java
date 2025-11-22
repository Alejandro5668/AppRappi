package views;

import controllers.SistemaPedidos;
import exceptions.DatosInvalidosException;
import models.Restaurante;
import models.Pedido;
import models.Plato;

import java.util.Scanner;

public class VistaRestaurante {
    private SistemaPedidos sistema;
    private Scanner sc = new Scanner(System.in);

    public VistaRestaurante(SistemaPedidos sistema) {
        this.sistema = sistema;
    }

    public void iniciar() {
        System.out.println("----BIENVENIDO A LA VISTA RESTAURANTE----");
        int opcionIR = 0;
        do {
            System.out.println("1) Gestionar menu\n" +
                    "2) Registrarse\n" +
                    "3) Ver restaurantes\n" +
                    "4) Salir\n" +
                    "Que opcion desea ejecutar?: ");
            String entrada = sc.nextLine();

            if (entrada.isEmpty()) {
                System.out.println("Debe ingresar un numero, intente de nuevo");
                continue;
            }

            try {
                opcionIR = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Solo se permiten numeros, intentelo de nuevo");
                continue;
            }

            switch (opcionIR) {
                case 1:
                    gestionarMenu();
                    break;
                case 2:
                    registrarRestaurante();
                    break;
                case 3:
                    sistema.verRestaurantes();
                    break;
                case 4:
                    System.out.println("Saliendo de la vista domiciliario....");
                    break;
                default:
                    System.out.println("Opcion Invalida");
                    break;
            }
        } while (opcionIR != 4);
    }

    public void registrarRestaurante() {

        try {

            System.out.print("Ingrese el nombre del restaurante: ");
            String nombreRestaurante = sc.nextLine();

            System.out.print("Ingrese el codigo del restaurante: ");
            String cod = sc.nextLine();
            int codigo = Integer.parseInt(cod);

            sistema.listarZonasDisponibles();
            System.out.print("Ingrese la zona donde se encuentra el restaurante: ");
            String zonaRestaurante = sc.nextLine();

            Restaurante restauranteNuevo = new Restaurante(nombreRestaurante, codigo, zonaRestaurante);
            sistema.registrarRestaurante(restauranteNuevo);
            System.out.println("Restaurante registrado exitosamente");
        } catch (NumberFormatException e) {
            System.out.println("El codigo debe ser un numero");
        } catch (DatosInvalidosException e) {
            System.out.println(e.getMessage());
        }

    }

    private void gestionarMenu() {

        System.out.println("----GESTION DE MENU----");

        try {
            System.out.println("Ingrese el codigo del restaurante: ");
            String cod = sc.nextLine();
            int codigo = Integer.parseInt(cod);

            Restaurante restaurante = sistema.consultarRestaurantePorCodigo(codigo);

            if (restaurante == null) {
                System.out.println("Restaurante no encontrado");
                return;
            }

            menuRestaurante(restaurante);
        } catch (NumberFormatException e) {
            System.out.println("El codigo debe ser un numero");
        }
    }

    private void menuRestaurante(Restaurante restaurante) {
        int opcion = 0;

        do {

            System.out.println("Restaurante: " + restaurante.getNombre());
            System.out.println();

            System.out.println("1) Agregar Plato al Menu");
            System.out.println("2) Ver Menu Completo");
            System.out.println("3) Ver Pedidos Activos");
            System.out.println("4) Salir");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    agregarPlato(restaurante);
                    break;
                case 2:
                    mostrarMenu(restaurante);
                    break;
                case 3:
                    sistema.mostrarPedidosActivos();
                    break;
                case 4:
                    System.out.println("Saliendo del menu del restaurante....");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcion != 4);

    }

    private void agregarPlato(Restaurante restaurante) {
        try {

            System.out.println("Ingrese el nombre del plato: ");
            String nombre = sc.nextLine();

            System.out.println("Ingrese el precio del plato: ");
            String pre = sc.nextLine();
            int precio = Integer.parseInt(pre);

            System.out.println("Ingrese la categoria del plato( Entrada, Plato fuerte, Bebida, Postre): ");
            String categoria = sc.nextLine();

            Plato plato = new Plato(nombre, categoria, precio);
            restaurante.agregarPlato(plato);

            System.out.println("Plato agregado exitosamente");

        } catch (NumberFormatException e) {
            System.out.println("Valor numerico invalido");
        } catch (DatosInvalidosException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarMenu(Restaurante restaurante) {
        System.out.println("Menu del restaurante " + restaurante.getNombre());
        System.out.println();

        if (restaurante.getMenu().isEmpty()) {
            System.out.println("El menu esta vacio");
            return;
        }

        String categorias[] = { "entrada", "plato fuerte", "bebida", "postre" };

        for (String categoria : categorias) {
            boolean hayplatos = false;
            System.out.println(categoria);

            for (int i = 0; i < restaurante.getMenu().getTamanio(); i++) {
                Plato plato = restaurante.getMenu().obtener(i);

                if (plato.getCategoria().equalsIgnoreCase(categoria)) {
                    System.out.println((i + 1) + ") " + plato.getNombre() + "-" + plato.getPrecio());
                    hayplatos = true;
                }
            }

            if (!hayplatos) {
                System.out.println("No hay platos en esta categoria");
            }
        }
        System.out.println();
    }

    // Ejemplo de vista para mostrar pedidos del restaurante y el camino completo
    // (si tiene domiciliario)
    @SuppressWarnings("unused")
    public void verPedidosRestaurante(Restaurante rest) {
        // ...existing código para iterar/seleccionar pedidos del restaurante...
        Pedido pedidoSeleccionado = null; // ...existing selection logic...

        if (pedidoSeleccionado != null) {
            sistema.mostrarCaminoCompletoPedido(pedidoSeleccionado);
        }
    }

}
