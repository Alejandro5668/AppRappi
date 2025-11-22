package structures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GrafoZonas {
    private final Map<String, Map<String, Double>> adj = new HashMap<>();

    private static class Arista {
        String destino;
        int peso;
        Arista siguiente;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
            this.siguiente = null;
        }
    }

    private static class ListaAdyacencia {
        Arista primera;

        public ListaAdyacencia() {
            this.primera = null;
        }

        public void agregar(String destino, int peso) {
            if (!existe(destino)) {
                Arista nueva = new Arista(destino, peso);
                if (primera == null) {
                    primera = nueva;
                } else {
                    Arista actual = primera;
                    while (actual.siguiente != null) {
                        actual = actual.siguiente;
                    }
                    actual.siguiente = nueva;
                }
            }
        }

        public boolean existe(String destino) {
            Arista actual = primera;
            while (actual != null) {
                if (actual.destino.equals(destino)) {
                    return true;
                }
                actual = actual.siguiente;
            }
            return false;
        }
    }

    private static class Vertice {
        String nombre;
        ListaAdyacencia adyacentes;
        Vertice siguiente;

        public Vertice(String nombre) {
            this.nombre = nombre;
            this.adyacentes = new ListaAdyacencia();
            this.siguiente = null;
        }
    }

    private Vertice primer;

    public GrafoZonas() {
        this.primer = null;
    }

    public void agregarVertice(String nombre) {
        if (buscarVertice(nombre) == null) {
            Vertice nuevo = new Vertice(nombre);
            if (primer == null) {
                primer = nuevo;
            } else {
                Vertice actual = primer;
                while (actual.siguiente != null) {
                    actual = actual.siguiente;
                }
                actual.siguiente = nuevo;
            }
        }
    }

    public Vertice buscarVertice(String nombre) {
        Vertice actual = primer;
        while (actual != null) {
            if (actual.nombre.equals(nombre)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public void agregarArista(String origen, String destino, int peso) {
        Vertice vOrigen = buscarVertice(origen);
        Vertice vDestino = buscarVertice(destino);
        if (vOrigen != null && vDestino != null) {
            vOrigen.adyacentes.agregar(destino, peso);
            vDestino.adyacentes.agregar(origen, peso);
        }
    }

    // Carga un CSV con formato: zonaA,zonaB,distancia
   public void cargarDesdeCSV(String rutaCSV) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            
            String[] partes = linea.split(",");
            if (partes.length < 3) continue;
            
            String a = partes[0].trim();
            String b = partes[1].trim();
            String sDist = partes[2].trim();
            
            double dist;
            try {
                dist = Double.parseDouble(sDist);
            } catch (NumberFormatException e) {
                continue;
            }
            
           
            añadirArista(a, b, dist);
            
          
            agregarVertice(a);
            agregarVertice(b);
            agregarArista(a, b, (int)dist);
        }
    }
}

    public void añadirArista(String a, String b, double distancia) {
        adj.computeIfAbsent(a, k -> new HashMap<>()).put(b, distancia);
        adj.computeIfAbsent(b, k -> new HashMap<>()).put(a, distancia); // grafo no dirigido
    }

    // Resultado simple de camino
    public static class ResultadoCamino {
        public final double distancia;
        public final List<String> camino;

        public ResultadoCamino(double d, List<String> c) {
            distancia = d;
            camino = c;
        }
    }

    // Dijkstra desde inicio a destino (si destino == null devuelve distancias a
    // todos)
    public ResultadoCamino dijkstra(String inicio, String destino) {
        if (!adj.containsKey(inicio))
            return new ResultadoCamino(Double.POSITIVE_INFINITY, Collections.emptyList());
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String nodo : adj.keySet())
            dist.put(nodo, Double.POSITIVE_INFINITY);
        dist.put(inicio, 0.0);

        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue));
        pq.add(new AbstractMap.SimpleEntry<>(inicio, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<String, Double> entry = pq.poll();
            String u = entry.getKey();
            double dU = entry.getValue();
            if (dU > dist.getOrDefault(u, Double.POSITIVE_INFINITY))
                continue;
            if (destino != null && u.equals(destino))
                break;
            Map<String, Double> vecinos = adj.getOrDefault(u, Collections.emptyMap());
            for (Map.Entry<String, Double> vEntry : vecinos.entrySet()) {
                String v = vEntry.getKey();
                double peso = vEntry.getValue();
                double alt = dU + peso;
                if (alt < dist.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(new AbstractMap.SimpleEntry<>(v, alt));
                }
            }
        }

        if (destino == null)
            return new ResultadoCamino(Double.NaN, Collections.emptyList());

        double distanciaFinal = dist.getOrDefault(destino, Double.POSITIVE_INFINITY);
        if (distanciaFinal == Double.POSITIVE_INFINITY)
            return new ResultadoCamino(Double.POSITIVE_INFINITY, Collections.emptyList());

        // reconstruir camino
        LinkedList<String> camino = new LinkedList<>();
        String paso = destino;
        while (paso != null) {
            camino.addFirst(paso);
            paso = prev.get(paso);
        }
        return new ResultadoCamino(distanciaFinal, new ArrayList<>(camino));
    }

    // Devuelve la zona (String) de restaurante más cercana a partir de la zona del
    // domiciliario.
    // zonasRestaurantes: lista de zonas (Strings) donde hay restaurantes
    public String encontrarZonaRestauranteMasCercana(String zonaDomiciliario, List<String> zonasRestaurantes) {
        if (zonaDomiciliario == null || zonasRestaurantes == null || zonasRestaurantes.isEmpty())
            return null;
        String mejorZona = null;
        double mejorDist = Double.POSITIVE_INFINITY;
        for (String zonaRest : zonasRestaurantes) {
            ResultadoCamino res = dijkstra(zonaDomiciliario, zonaRest);
            double dist = res.distancia;
            if (dist < mejorDist) {
                mejorDist = dist;
                mejorZona = zonaRest;
            }
        }
        return mejorZona;
    }

    // Método utilitario para obtener distancia entre dos zonas
    public double obtenerDistanciaEntreZonas(String a, String b) {
        ResultadoCamino r = dijkstra(a, b);
        return r.distancia;
    }

    public Map<String, Object> dfsCaminoMinimo(String inicio, String destino) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("camino", new ArrayList<String>());
        resultado.put("tiempo", Integer.MAX_VALUE);

        if (buscarVertice(inicio) == null || buscarVertice(destino) == null) {
            return resultado;
        }

        Set<String> visitados = new HashSet<>();
        List<String> mejorCamino = new ArrayList<>();
        int[] mejorTiempo = { Integer.MAX_VALUE };

        visitados.add(inicio);
        List<String> caminoActual = new ArrayList<>();
        caminoActual.add(inicio);

        dfsRecursivo(inicio, destino, visitados, caminoActual, 0, mejorCamino, mejorTiempo);

        resultado.put("camino", mejorCamino);
        resultado.put("tiempo", mejorTiempo[0]);
        return resultado;
    }

    private void dfsRecursivo(String actual, String destino, Set<String> visitados,
            List<String> camino, int tiempoTotal,
            List<String> mejorCamino, int[] mejorTiempo) {
        if (actual.equals(destino)) {
            if (tiempoTotal < mejorTiempo[0]) {
                mejorCamino.clear();
                mejorCamino.addAll(camino);
                mejorTiempo[0] = tiempoTotal;
            }
            return;
        }

        Vertice verticeActual = buscarVertice(actual);
        if (verticeActual == null)
            return;

        Arista adyacente = verticeActual.adyacentes.primera;
        while (adyacente != null) {
            if (!visitados.contains(adyacente.destino)) {
                visitados.add(adyacente.destino);
                camino.add(adyacente.destino);
                dfsRecursivo(adyacente.destino, destino, visitados, camino,
                        tiempoTotal + adyacente.peso, mejorCamino, mejorTiempo);
                camino.remove(camino.size() - 1);
                visitados.remove(adyacente.destino);
            }
            adyacente = adyacente.siguiente;
        }
    }

    public void mostrar() {
        Vertice actual = primer;
        while (actual != null) {
            List<String> conexiones = new ArrayList<>();
            Arista adya = actual.adyacentes.primera;
            while (adya != null) {
                conexiones.add(adya.destino + "(" + adya.peso + "min)");
                adya = adya.siguiente;
            }
            System.out.println("📍 " + actual.nombre + " -> " + String.join(" | ", conexiones));
            actual = actual.siguiente;
        }
    }

    public Set<String> obtenerZonas() {
        Set<String> zonas = new HashSet<>();
        Vertice actual = primer;
        while (actual != null) {
            zonas.add(actual.nombre);
            actual = actual.siguiente;
        }
        return zonas;
    }

    public List<String> obtenerConexiones(String zona) {
        List<String> conexiones = new ArrayList<>();
        Vertice vertice = buscarVertice(zona);
        if (vertice != null) {
            Arista adya = vertice.adyacentes.primera;
            while (adya != null) {
                conexiones.add(adya.destino);
                adya = adya.siguiente;
            }
        }
        return conexiones;
    }
}
