package structures;

import java.util.ArrayList;

public class Cola<T> {
    private ArrayList<T> items;

    public Cola() {
        items = new ArrayList<>();
    }

    public void encolar(T elemento) {
        items.add(elemento);
    }

    public T desencolar() {
        if (items.isEmpty()) {
            return null;
        }
        return items.remove(0);
    }

    public void imprimir() {
        if (items.isEmpty()) {
            return;
        }
        for (T elemento : items) {
            System.out.print(elemento + " ");
        }
        System.out.println();
    }

    public boolean isEmpty() {

        return items.isEmpty();
    }

}

