import controllers.SistemaPedidos;
import views.MenuPrincipal;

public class main {
    public static void main(String[] args) {
        SistemaPedidos s = new SistemaPedidos();
        MenuPrincipal menuPrincipal = new MenuPrincipal(s);
        menuPrincipal.menuPrincipal();
    }
}
