# Proyecto-ING-Base-de-datos


# Sistema de Gestión de Pedidos — Cafetería o Restaurante

**Universidad Libre — Ingeniería de Software II**  
* **Integrantes:** Daniel Stiven Avellaneda Medina, Luis Hernando Rico Batanero, Duvan Felipe Leon Vallejo   
* **Docente:** Marcela Cifuentes  

---

## 1. Alcance

El sistema cubrirá la gestión de pedidos y productos personalizables para una cafetería o restaurante, incluyendo bebidas y platos con adiciones opcionales, combos armados paso a paso, menú configurable, avisos automáticos por cambio de estado, promociones por horario o día, registro de pagos, cierre de caja y reporte de ventas.

### Funcionalidades dejadas fuera
Quedan fuera integraciones externas como:
* Pasarelas de pagos en línea.
* Facturación electrónica fiscal externa.
* Módulos administrativos como gestión de nómina, proveedores o inventario avanzado multiproveedor.

> Se prioriza el flujo interno del local, evitando dependencias ajenas al modelado de pedidos y patrones del establecimiento.

### Áreas con variación o cambio frecuente
El sistema contempla cambios recurrentes en:
* El catálogo de adiciones y reglas de personalización sobre bebidas y platos.
* La estructura de combos armados paso a paso junto con las promociones por horario o día.
* Los estados de un pedido y los avisos automáticos enviados a cocina y caja ante cada cambio de estado.

---

## 2. Requisitos

### Requisitos Funcionales

| ID | Requisito | Descripción y Justificación |
| :--- | :--- | :--- |
| **RF-01** | **Gestión de roles y acceso** | El sistema permitirá operar bajo los roles de **cliente, cajero, cocinero y admin** para separar las acciones de compra, atención de caja, preparación de pedidos y administración del local. Se sustenta en la necesidad de controlar quién puede consultar o pedir, quién registra pagos y cierra caja, quién actualiza estados de preparación y quién administra el menú, productos y promociones. |
| **RF-02** | **Menú configurable y tipos de producto** | El sistema permitirá consultar un menú configurable y administrar distintos tipos de producto, incluyendo **bebidas, platos y combos**. Esto responde a la operación diaria de una cafetería o restaurante que necesita mantener vigente su catálogo y organizar sus ofertas en categorías o tipos diferenciados. |
| **RF-03** | **Personalización con adiciones opcionales** | El sistema permitirá añadir adiciones opcionales a bebidas y platos, actualizando de forma coherente tanto el precio como la descripción del producto. Se justifica porque los clientes solicitan variaciones puntuales sobre los productos base y el sistema debe reflejar el costo y el detalle exacto en el pedido. |
| **RF-04** | **Armado de combos paso a paso** | El sistema permitirá construir combos paso a paso a partir de diversas opciones disponibles. Es necesario para guiar la selección de los componentes de un combo de manera ordenada sin obligar a definir combinaciones fijas rígidas. |
| **RF-05** | **Registro y control de pedidos** | El sistema permitirá realizar pedidos, calcular sus montos y actualizar su estado a lo largo del flujo de atención. Esto garantiza el seguimiento del pedido desde que el cliente lo solicita hasta que avanza en su preparación y cobro. |
| **RF-06** | **Avisos automáticos por cambio de estado** | El sistema enviará avisos automáticos a cocina y caja cuando cambie el estado de un pedido. Se requiere para coordinar en tiempo oportuno la preparación de los alimentos y las tareas de cobro sin depender de revisiones manuales constantes. |
| **RF-07** | **Promociones por horario o día** | El sistema permitirá gestionar y aplicar promociones según el horario o el día configurado. Se sustenta en la necesidad de aplicar descuentos condicionales de forma automática cuando se cumpla la franja horaria o el día establecido. |
| **RF-08** | **Pagos, cierre de caja y reporte de ventas** | El sistema permitirá registrar pagos, ejecutar el cierre de caja y generar reportes de ventas. Esto asegura el control financiero de la jornada y el consolidado de las ventas realizadas en el local. |

### Requisitos No Funcionales

| ID | Requisito | Descripción |
| :--- | :--- | :--- |
| **RNF-01** | **Separación entre interfaces y núcleo** | El sistema mantendrá la lógica del negocio separada de la consola y de la interfaz gráfica para que ambas interfaces trabajen sobre el mismo núcleo. Esto permitirá ofrecer ambas formas de uso sin duplicar reglas de cálculo, promociones, combos ni validación de pedidos. |
| **RNF-02** | **Soporte a cambios en áreas variables** | El sistema permitirá incorporar nuevas adiciones, modificar reglas de combos y promociones o ajustar destinatarios de avisos sin reescribir toda la aplicación. Se orienta a que el sistema responda de forma ágil a los cambios frecuentes propios de una cafetería o restaurante. |
| **RNF-03** | **Consistencia en cálculos y estados** | El sistema calculará de forma uniforme los precios con adiciones y promociones, y reflejará los mismos cambios de estado y totales en pedidos, caja y reportes de ventas. Esto evita discrepancias entre lo que se cobra, lo que se prepara y lo que figura en el reporte. |
| **RNF-04** | **Claridad y trazabilidad del flujo** | El sistema registrará y mostrará de forma comprensible el estado del pedido, los detalles del producto personalizado y los resultados del cierre de caja. Así el personal y el cliente podrán verificar qué se pidió, qué adiciones incluye y en qué etapa se encuentra. |

---

## 3. Problemas de Diseño

* **Multiplicación de clases o condiciones por adiciones en bebidas y platos:**  
  Si se intentan cubrir todas las combinaciones posibles de adiciones (por ejemplo, extra de leche, salsa o tamaño) mediante subclases por combinación o llenando los productos de indicadores condicionales, el código se vuelve rígido y repetitivo. Cada vez que se agregue o cambie una adición que modifique el precio y la descripción, habrá que tocar muchas clases o ramificaciones.

* **Armado rígido de combos paso a paso:**  
  Si los combos con múltiples opciones se construyen directamente con constructores extensos o ensamblando piezas sueltas dentro de la vista o del flujo de pedido, el código queda acoplado y difícil de mantener. Cambiar el orden de los pasos o las opciones permitidas al armar un combo obligaría a repetir lógica en distintos lugares.

* **Avisos fijos y acoplados al cambiar el estado de un pedido:**  
  Si el cambio de estado de un pedido llama de manera directa y fija a cocina y a caja, la clase del pedido queda atada a esos destinos específicos. Agregar una nueva notificación o cambiar cómo reacciona un área ante un cambio de estado exigiría modificar directamente la lógica central del pedido.

---

## 4. Patrones de Diseño Elegidos

### Decorator
* **Justificación:** Se eligió frente a la herencia por combinación o el uso de condicionales fijos en los productos porque permite envolver bebidas o platos con adiciones opcionales que ajustan precio y descripción de forma dinámica.
* **Consecuencia:** Se gana flexibilidad para combinar adiciones libremente, aunque se añaden objetos envoltorio que aumentan la cantidad de instancias en memoria y exigen cuidado al encadenarlos.

### Builder
* **Justificación:** Se eligió frente a constructores cargados de parámetros o al ensamblado directo en las pantallas porque separa y ordena el armado paso a paso de un combo con varias opciones.
* **Consecuencia:** La construcción de combos complejos queda más clara y extensible, a cambio de introducir una clase constructora adicional y pasos estructurados para armar el combo.

### Observer
* **Justificación:** Se eligió frente a llamadas directas fijas dentro del pedido o consultas repetitivas de estado porque permite avisar automáticamente a cocina y a caja cuando cambia el estado del pedido sin acoplar el pedido a cada destinatario.
* **Consecuencia:** Se desacopla la emisión del aviso de quienes lo reciben y facilita ajustar los observadores, aunque hace el flujo de notificaciones más indirecto y dependiente del registro de suscriptores.

---

## 5. Arquitectura General

La arquitectura divide el sistema en **tres capas**, de modo que tanto la consola como la interfaz gráfica interactúen con las mismas reglas y datos:

1. **Capa de Presentación (Consola e Interfaz Gráfica):**  
   Se limita a capturar las acciones del usuario según el rol (`cliente`, `cajero`, `cocinero` o `admin`) y a mostrar la información resultante, sin implementar por sí misma cálculos de precios, armado de combos, aplicación de promociones ni lógica de notificaciones.
2. **Capa de Coordinación o Aplicación (Servicios):**  
   Actúa entre la presentación y el núcleo; recibe las solicitudes desde la consola o desde la interfaz gráfica, invoca las operaciones del núcleo y devuelve los resultados, garantizando que ambas interfaces compartan exactamente el mismo comportamiento.
3. **Núcleo de Negocio (Dominio y Patrones):**  
   Concentra las entidades del dominio, los procesos de pedidos, combos, promociones, pagos, cierre de caja y reportes de ventas, así como los mecanismos de personalización (`Decorator`), construcción paso a paso (`Builder`) y aviso automático ante cambios de estado (`Observer`).

---

## 6. Entregables — Diagramas

1. **Diagrama de Casos de Uso** *(ver `docs/diagramas/casos-de-uso.png`)*
Este diagrama tiene como finalidad representar cómo está estructurado nuestro sistema internamente. Nos permite identificar las diferentes clases que lo componen, sus atributos, sus métodos y la manera en que se relacionan entre sí.
2.**Diagramas de clases por patron**
   Este diagrama tiene como objetivo representar las funcionalidades que ofrece nuestro sistema y la manera en que los diferentes usuarios interactúan con él.
-----------------------------------------------------------------------------------------------------------------------------------------------------------
**patron(Decorator):**
<img width="748" height="696" alt="image" src="https://github.com/user-attachments/assets/44a53483-1e17-4609-bbe8-d796d71cc376" />
------------------------------------------------------------------------------------------------------------------------------------------------------------
**patron(Builder):**
<img width="516" height="673" alt="image" src="https://github.com/user-attachments/assets/6be64fd0-5ec1-4e98-a660-5bed7fedfc6e" />
--------------------------------------------------------------------------------------------------------------------------------------------------------------
 **patron(Observer):**
<img width="854" height="690" alt="image" src="https://github.com/user-attachments/assets/ce60eda8-5e9e-4bf5-a846-2b4e94c97277" />

------------------------------------------------------------------------------------------------------------------------------------------------------------

**Diagrama de secuencia(Flujo principal del pedido):**
<img width="1129" height="556" alt="image" src="https://github.com/user-attachments/assets/2042b10f-9b55-4a84-a653-f9e44ee16989" />
Este diagrama nos permite representar cómo se desarrolla un proceso dentro del sistema a través del tiempo, mostrando el orden de las operaciones y la comunicación entre los diferentes participantes.
   
3. **Diagrama de Clases General** *(ver `docs/diagramas/clases-general.png`)*
   este mapa nos permite ver el proceso
