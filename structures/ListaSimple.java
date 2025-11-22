package structures;

public class ListaSimple<T> {
    private Nodo<T> frente = null;
    private Nodo<T> fin = null;

    public void insertarInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (frente == null)
            fin = nuevoNodo;
        else
            nuevoNodo.siguiente = frente;
        frente = nuevoNodo;
    }

    public void insertarFin(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (frente == null) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
    }

    public void insertarLuego(T busqueda, T dato) {
        if (frente == null) {
            System.out.println("Lista vacía");
            return;
        }
        Nodo<T> tmp = frente;
        while (tmp != null && !tmp.dato.equals(busqueda))
            tmp = tmp.siguiente;

        if (tmp == null) {
            System.out.println("Elemento de búsqueda no encontrado");
            return;
        }
        Nodo<T> nuevoNodo = new Nodo<T>(dato);
        nuevoNodo.siguiente = tmp.siguiente;
        tmp.siguiente = nuevoNodo;

        if (tmp == fin)
            fin = nuevoNodo;
    }

    public void borrarNodo(T busqueda) {
        if (frente == null) {
            System.out.println("Lista vacía");
            return;
        }
        if (frente.dato.equals(busqueda)) {
            if (frente == fin)
                frente = fin = null;
            else
                frente = frente.siguiente;
            return;
        }
        Nodo<T> tmp = frente;
        while (tmp.siguiente != null && !tmp.siguiente.dato.equals(busqueda))
            tmp = tmp.siguiente;

        if (tmp.siguiente == null) {
            System.out.println("Elemento de búsqueda no encontrado");
            return;
        }
        Nodo<T> objetivo = tmp.siguiente;
        tmp.siguiente = objetivo.siguiente;
        if (tmp.siguiente == null)
            fin = tmp;
    }

    public void borrarPrimero() {
        if (frente == null) {
            System.out.println("Lista vacía");
            return;
        }
        frente = frente.siguiente;
        if (frente == null)
            fin = null;
    }

    public void borrarUltimo() {
        if (frente == null) {
            System.out.println("Lista vacía");
            return;
        }
        if (frente == fin) {
            frente = null;
            fin = null;
        } else {
            Nodo<T> tmp = frente;
            while (tmp.siguiente != fin)
                tmp = tmp.siguiente;
            fin = tmp;
            fin.siguiente = null;
        }
    }

    public void borrarLuego(T busqueda) {
        if (frente == null) {
            System.out.println("Lista vacía");
            return;
        }
        Nodo<T> tmp = frente;
        while (tmp != null && !tmp.dato.equals(busqueda))
            tmp = tmp.siguiente;

        if (tmp == null || tmp.siguiente == null) {
            System.out.println("Elemento de búsqueda no encontrado o no hay nodo siguiente para borrar");
            return;
        }

        Nodo<T> objetivo = tmp.siguiente;
        tmp.siguiente = objetivo.siguiente;

        if (objetivo == fin)
            fin = tmp;
    }

    public void recorrer() {
        Nodo<T> tmp = frente;
        while (tmp != null) {
            System.out.print(tmp.dato + "->");
            tmp = tmp.siguiente;
        }
        System.out.println("null");
    }

    // Implementacion de metodos adicionales

    public Nodo<T> getFrente() {
        return frente;
    }

    public boolean isEmpty() {
        return frente == null;
    }

    public int getTamanio() {
        int count = 0;
        Nodo<T> tmp = frente;
        while (tmp != null) {
            count++;
            tmp = tmp.siguiente;
        }
        return count;
    }

    public T obtener(int indice) {
        Nodo<T> tmp = frente;
        int count = 0;
        while (tmp != null) {
            if (count == indice) {
                return tmp.dato;
            }
            count++;
            tmp = tmp.siguiente;
        }
        return null;
    }

}