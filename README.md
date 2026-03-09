# Simulador de Flujo en Redes (Algoritmo de Ford-Fulkerson)

Este es un simulador de escritorio desarrollado en JavaFX que visualiza el funcionamiento del algoritmo de Ford-Fulkerson para encontrar el flujo máximo en una red. La aplicación permite a los usuarios construir una red de forma interactiva, definir nodos fuente y sumidero, y observar una animación paso a paso del algoritmo mientras encuentra los caminos de aumento.



## Características

- **Creación Interactiva de Grafos:** Añade nodos con un clic en el lienzo y únelos para crear aristas dirigidas con una capacidad específica.
- **Edición Dinámica:** Arrastra los nodos para reorganizar la vista del grafo. Las aristas se ajustarán automáticamente.
- **Definición de Fuente y Sumidero:** Designa cualquier nodo como el inicio (Fuente 'S') o el final (Sumidero 'T') del flujo mediante un menú contextual (clic derecho).
- **Simulación Animada:** Ejecuta el algoritmo de Ford-Fulkerson y observa cómo se resaltan los caminos de aumento arista por arista, con pausas para facilitar la comprensión.
- **Feedback en Tiempo Real:** Una consola muestra los registros detallados de cada paso del algoritmo, como los caminos encontrados y el aumento del flujo. El flujo máximo actual se actualiza constantemente.
- **Controles de Simulación:** Reinicia el flujo a cero sin alterar el grafo o limpia completamente el lienzo para empezar de nuevo.

## ¿Cómo Usar?

1.  **Crear Nodos:** Haz clic izquierdo en cualquier parte del lienzo blanco para añadir un nuevo nodo.
2.  **Crear Aristas:** Haz clic izquierdo en un nodo de origen (se resaltará en cian) y luego haz clic en un nodo de destino. Se te pedirá que introduzcas la capacidad de la arista.
3.  **Mover Nodos:** Haz clic y arrastra cualquier nodo para cambiar su posición.
4.  **Definir Fuente/Sumidero:** Haz clic derecho en un nodo para abrir el menú contextual y selecciona "Definir como Fuente (S)" o "Definir como Sumidero (T)".
5.  **Ejecutar Simulación:** Una vez que la fuente y el sumidero estén definidos, presiona el botón "**▶ Ejecutar Simulación**".
6.  **Reiniciar/Limpiar:** Utiliza los botones "**Reiniciar Flujo**" o "**Limpiar Grafo**" según sea necesario.

## Estructura del Proyecto

El proyecto sigue una arquitectura similar al patrón Modelo-Vista-Controlador (MVC) para separar las responsabilidades:

- `com.simulador.controlador`: Contiene la lógica de la aplicación que conecta el modelo y la vista (`NetworkController`).
- `com.simulador.modelo`: Define las clases de datos del dominio (`Nodo`, `Arista`, `RedFlujo`).
- `com.simulador.vista`: Clases responsables de la representación visual de los elementos del modelo (`UINodo`, `UIArista`).
- `com.simulador.algoritmo`: Implementación del algoritmo de Ford-Fulkerson y las estrategias de búsqueda de caminos (en este caso, `BFSBusqueda`).
- `resources/com/simulador/vista`: Contiene los archivos FXML para la interfaz y las hojas de estilo CSS.

## Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **Framework UI:** JavaFX 17+
- **Gestión de Proyecto:** Maven

## Compilación y Ejecución

Para compilar y ejecutar el proyecto localmente, necesitarás tener instalado lo siguiente:

- JDK 17 o superior.
- Apache Maven.

### Pasos

1.  **Clona el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd Simulador
    ```

2.  **Compila el proyecto:**
    Usa Maven para resolver las dependencias y compilar el código fuente.
    ```bash
    mvn clean install
    ```

3.  **Ejecuta la aplicación:**
    El plugin de JavaFX para Maven facilita la ejecución.
    ```bash
    mvn javafx:run
    ```

Esto abrirá la ventana del simulador, y estará listo para ser utilizado.