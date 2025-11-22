package views;

import models.Domiciliario;
import models.Restaurante;
import controllers.SistemaPedidos;
import exceptions.DatosInvalidosException;

import java.util.Scanner;

public class VistaDomiciliario {
    Scanner Lea = new Scanner(System.in);
    private SistemaPedidos sistema;
    private Domiciliario domiciliarioActual;

    public VistaDomiciliario(SistemaPedidos sistema) {
        this.sistema = sistema;
    }

    public void iniciar() {
        int OpcionDIO = 0;
        do {
            System.out.println("----BIENVENIDO A LA VISTA DOMICILIARIO----");
            System.out.println("1) Registrar Domiciliario\n" +
                    "2) Iniciar Sesion\n" +
                    "3) Salir\n" +
                    "4) Ver Domiciliarios\n" +
                    "Que opcion desea ejecutar?: ");
            String entrada = Lea.nextLine();
            
            if(entrada.isEmpty()){
                System.out.println("Debe ingresar un numero");
                return;
            }
            
            try{
                OpcionDIO = Integer.parseInt(entrada);
            } catch(NumberFormatException e){
                System.out.println("Solo se permiten numeros, intentelo de nuevo");
                continue;
            }

            switch (OpcionDIO) {
                case 1:
                    registrarDomiciliario();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    System.out.println("Saliendo de la vista de domiciliario...");
                    break;
                case 4:
                    sistema.verDomiciliarios();
                    break;
                default:
                    System.out.println("Opcion Invalida");
                    break;
            }

        } while (OpcionDIO != 3);
    }

    private void registrarDomiciliario() {
        System.out.println("Registro de domiciliario");
        
        try {
            System.out.print("Ingrese el nombre del domiciliario: ");
            String nombreD = Lea.nextLine();
            
            System.out.print("Ingrese el codigo del domiciliario: ");
            String codigoD = Lea.nextLine();
            int codig = Integer.parseInt(codigoD);

            sistema.listarZonasDisponibles();
            System.out.print("Ingrese la zona donde se encuentra el domiciliario: ");
            String zonaD = Lea.nextLine();

            Domiciliario domiciliarioNuevo = new Domiciliario(nombreD, codig, zonaD);
            domiciliarioActual = domiciliarioNuevo;
            sistema.registrarDomiciliario(domiciliarioNuevo);
            System.out.println();
        } catch (NumberFormatException e){
            System.out.println("El codigo debe ser un numero");
        } catch (DatosInvalidosException e) {
            System.out.println(e.getMessage());
        }
    }

    private void iniciarSesion() {
        System.out.println("Inicio de sesion de domiciliario");

        try {
            System.out.print("Ingrese su codigo del domiciliario: ");
            String codigoD = Lea.nextLine();
            
            if (codigoD.isEmpty()){
                System.out.println("Ingrese un codigo");
                return;
            }

            int codigoInt = Integer.parseInt(codigoD);
            Domiciliario domicliarioEncontrado = sistema.buscarDomiciliarioPorCodigo(codigoInt);
    
            if (domicliarioEncontrado != null) {
                domiciliarioActual = domicliarioEncontrado;
                System.out.println("Sesion iniciada correctamente, Bienvenido " + domiciliarioActual.getNombre());
                mostrarOpciones();
            } else {
                System.out.println("No existe registro de un cliente con ese codigo.");
            }
        } catch(NumberFormatException e){
            System.out.println("El codigo debe ser un numero");
        }
    }

    private void mostrarOpciones() {
        int opcionD = 0;
        do {
            System.out.println("1) Ver Pedidos Activos\n" +
                    "2) Marcar como disponible\n" +
                    "3) Marcar como ocupado\n" +
                    "4) Buscar restaurante mas cercano\n" +
                    "5) Cambiar ubicacion\n" +
                    "6) Salir\n" +
                    "Que opcion desea ejecutar?: ");
            opcionD = Lea.nextInt();
            Lea.nextLine();

            switch (opcionD) {
                case 1:
                    sistema.mostrarPedidosActivos();
                    break;
                case 2:
                    domiciliarioActual.setDiponible(true);
                    break;
                case 3:
                    domiciliarioActual.setDiponible(false);
                    break;
                case 4:
                    buscarRestauranteCercano(domiciliarioActual);
                    break;
                case 5:
                    cambiarUbicacion(domiciliarioActual);
                    break;
                case 6:
                    System.out.println("Saliendo del menu de opciones de la vista domiciliario.....");
                    break;
                default:
                    System.out.println("Opcion Invalida");
                    break;
            }
        } while (opcionD != 6);

    }


// ~~~~~~ METODOS ADCIONALES PROPIOS DE LA VISTA DOMICILIARIO ~~~~~~

    private void cambiarUbicacion(Domiciliario domiciliario){
        System.out.println("Escoja una de las zona disponibles");
        sistema.listarZonasDisponibles();
        String nuevaZona = Lea.nextLine();

        if (!nuevaZona.isEmpty()){
            domiciliarioActual.setUbicacionActual(nuevaZona);
            System.out.println("Nueva zona asignada al domiciliario");
        } else {
            System.out.println("Zona invalida");
        }
    }

    private void buscarRestauranteCercano(Domiciliario domiciliario){
        System.out.println("Ubicacion actual del domiciliario: "+domiciliario.getUbicacionActual());

        Restaurante restaurante = sistema.encontrarRestauranteMasCercano(domiciliario);

        if (sistema.getRestaurantes().isEmpty()){
            System.out.println("No hay restaurantes todavia registrados en el sistema");
            return;
        }

        if (restaurante==null){
            System.out.println("Restaurante no encontrado");
        }

        System.out.println("Restaurante mas cercano: ");
        System.out.print(restaurante.toString());
    }
}
