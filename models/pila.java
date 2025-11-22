package structures;

import java.util.ArrayList;

public class Pila<T> {
    private ArrayList<T> items;

    public Pila() {
        items = new ArrayList<>();
    }

    public void push(T elemento) {
        items.add(elemento);        
    }

    public T pop() {
        if (this.empty()) {
            return null;
        }
        return items.remove(items.size() - 1);        
    }

    public void print_stack() {
        for (int i = this.size() - 1; i >= 0; i--) {
            System.out.print(items.get(i) + " ");
        }
    }
    
    public boolean empty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public T top() {
        if (this.empty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }    
}

