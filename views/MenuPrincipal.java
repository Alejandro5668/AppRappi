package views;

import controllers.SistemaPedidos;

import java.util.Scanner;
public class MenuPrincipal {
    private SistemaPedidos sistema;
    Scanner Lea = new Scanner(System.in);

    public MenuPrincipal(SistemaPedidos sistema) {
        this.sistema = sistema;
    }

    public void menuPrincipal() {
        int opcion = 0;

        do {

            System.out.println("----BIENVENIDO A NUESTRA APP TIPO RAPPI----");
            System.out.println("1) Modulo Cliente\n" +
                    "2) Modulo Restaurante\n" +
                    "3) Modulo Domiciliario\n" +
                    "4) Salir del Programa\n" +
                    "Ingrese el número de la opción que desea ejecutar: ");
            String entrada = Lea.nextLine().trim();

            // validacion 
            if (entrada.isEmpty()) {
                System.out.println("Debe ingresar un número, intente de nuevo.");
                continue; 
            }

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Sólo se permiten números. Intentelo de nuevo. \n");
                continue;
            }
            

            switch (opcion) {
                case 1:
                    VistaCliente vistaCliente = new VistaCliente(sistema);
                    vistaCliente.iniciar();
                    break;
                case 2:
                    VistaRestaurante vistaRestaurante = new VistaRestaurante(sistema);
                    vistaRestaurante.iniciar();
                    break;
                case 3:
                    VistaDomiciliario vistaDomiciliario = new VistaDomiciliario(sistema);
                    vistaDomiciliario.iniciar();
                    break;
                case 4:
                    System.out.println("Gracias por visitar nuestra app :)");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
                    break;
            }

        } while (opcion != 4);
    }

}
