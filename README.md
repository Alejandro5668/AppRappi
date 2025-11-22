# Sistema de Gestión y Simulación de Pedidos a Domicilio Multizona

Proyecto final del curso de Estructuras de Datos I que simula una plataforma tipo Rappi o Uber Eats para gestionar pedidos a domicilio en una ciudad dividida en zonas conectadas por un grafo vial.

---

## Descripción General

Este sistema permite gestionar el ciclo completo de pedidos a domicilio, incluyendo el registro y consulta de clientes, restaurantes y domiciliarios; creación, asignación, entrega y cancelación de pedidos; y manejo de rutas en un mapa simulado. Utiliza estructuras de datos implementadas manualmente (listas, colas, pilas, árboles, grafos) para representar las entidades y la topología de la ciudad.

---

## Objetivos del Proyecto

- Diseñar e implementar un sistema funcional que simule la operación de una plataforma de pedidos a domicilio multizona.
- Practicar la implementación manual de estructuras de datos fundamentales sin depender de las estructuras nativas del lenguaje.
- Desarrollar una arquitectura organizada en modelo, controlador y vistas, para un código limpio y separable.
- Aplicar algoritmos para la búsqueda eficiente del restaurante y domiciliario más cercano, asignando pedidos de forma lógica.

---

## Arquitectura del Sistema

### Modelo

Define las clases y estructuras para representar:

- *Clientes*: Usuarios que realizan pedidos.
- *Restaurantes*: Con menús y zona de ubicación.
- *Domiciliarios*: Personal encargado de la entrega, con estados de disponibilidad.
- *Pedidos*: Objetos que unen cliente, restaurante y domiciliario, con estados (pendiente, en entrega, entregado, cancelado).
- *Mapa y Zonas*: Grafo que modela las zonas y conexiones viales para cálculos de rutas y distancias.

---

### Controlador

Es el componente central que contiene toda la lógica de negocio y coordina la interacción entre modelo y vistas.

#### Funcionalidades Principales

- Registrar y validar clientes, restaurantes y domiciliarios.
- Mantener y consultar el grafo con zonas y caminos.
- Crear nuevos pedidos y asignar el restaurante y domiciliario más cercano.
- Actualizar los estados de los pedidos durante su ciclo (asignado, en entrega, entregado, cancelado).
- Liberar domiciliarios cuando terminan o cancelan su entrega.
- Proveer métodos para listar pedidos por estado y consultar historiales por cliente o zona.

El controlador asegura la integridad y correcta ejecución de las reglas del negocio, manteniendo el sistema sincronizado y sin inconsistencias.

---

### Vistas

La interfaz puede ser por consola o gráfica, y está diseñada para facilitar una interacción clara y sencilla con el sistema.

#### Funciones de la vista

- Mostrar menús para registrar entidades y gestionar pedidos.
- Solicitar datos al usuario de forma guiada para crear pedidos.
- Permitir cambiar el estado de pedidos (entregar, cancelar).
- Mostrar listados actuales de pedidos activos, entregados y cancelados.
- Consultar historiales por cliente o zona.
- Presentar mensajes de error y confirmación para mantener una buena experiencia de uso.

Las vistas no contienen lógica de negocio, delegando esa responsabilidad al controlador.

---

## Funcionalidades Logradas

- Registro completo y consulta de clientes, domiciliarios y restaurantes.
- Modelado del mapa de la ciudad con grafo de zonas y rutas.
- Gestión del ciclo de vida de pedidos con estados y asignación automática.
- Búsqueda inteligente del restaurante y domiciliario más cercano para cada pedido.
- Visualización clara de pedidos según estado y consulta de historiales detallados.
- Manejo de casos de cancelación y entrega, liberando recursos (domiciliarios) para nuevos pedidos.

---

## Casos de Prueba

- Registro masivo de clientes, restaurantes y domiciliarios validando integridad y consultas.
- Creación de pedidos con asignación correcta basándose en la zona del cliente.
- Cambio de estados de pedidos y confirmación de actualización en reportes.
- Búsqueda de zonas y rutas en el grafo para validar algoritmo de asignación.
- Consulta de historiales y listados de pedidos por diferentes filtros.

Estos casos garantizan el correcto funcionamiento y coherencia del sistema en escenarios reales simulados.

---

## Estructuras de Datos

El proyecto hace uso intensivo de estructuras de datos creadas manualmente para:

- Manejar listas enlazadas para almacenar clientes, restaurantes, domiciliarios y pedidos.
- Utilizar colas y pilas para procesos internos de gestión y recorrido.
- Implementar árboles para representar menús o jerarquías.
- Usar grafos para el mapa de zonas y rutas viales, con métodos para buscar nodos más cercanos y caminos óptimos.
- Aplicar recursividad en algoritmos de búsqueda y recorrido.
