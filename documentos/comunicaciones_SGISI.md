# Definiciones de Comunicación – SGISI
## Sistema de Gestión de Incidentes de Seguridad Informática – Banco Regional SA
### Borrador para TP2 – Módulo 2

---

## 1. Descripción del entorno de comunicaciones

El SGISI es una aplicación de escritorio que se ejecuta en las workstations del equipo
SOC de Banco Regional SA. La persistencia de datos se resuelve a través de un servidor
MySQL 8.0 ubicado en el datacenter on-premise de la institución.

La comunicación principal del sistema se produce entre las workstations de los analistas
(clientes) y el servidor de base de datos (servidor), a través de una red de área local
(LAN) corporativa. Este esquema sigue el modelo cliente-servidor descripto por
Tanenbaum y Wetherall (2012): el cliente genera solicitudes, el servidor las procesa y
devuelve una respuesta.

No existen en esta versión del prototipo comunicaciones con sistemas externos ni con
redes de área extensa (WAN). Toda la comunicación del SGISI es interna a la
infraestructura del banco.

---

## 2. Modelo de referencia OSI aplicado al SGISI

El modelo OSI (Open Systems Interconnection), definido por la ISO para la
interconexión de sistemas heterogéneos, organiza las funciones de comunicación en
siete capas independientes (Stallings, 2004). Cada capa ofrece servicios a la capa
superior y utiliza los servicios de la capa inferior, sin que las capas se conozcan entre sí
internamente.

A continuación se describe cómo cada capa del modelo OSI se manifiesta en la
comunicación entre la aplicación SGISI y el servidor MySQL:

| Capa | Nombre | Protocolo / Estándar | Rol en el SGISI |
|------|--------|---------------------|-----------------|
| 7 | Aplicación | JDBC / MySQL Protocol | El driver JDBC serializa las sentencias SQL y recibe los resultados. Es la interfaz entre el código Java y la base de datos. |
| 6 | Presentación | Codificación UTF-8 | Garantiza que los caracteres especiales del español (tildes, ñ) se representen correctamente entre cliente y servidor. |
| 5 | Sesión | Sesión MySQL | Gestiona el establecimiento, mantenimiento y cierre de la conexión entre la aplicación y el servidor de base de datos. |
| 4 | Transporte | TCP | Garantiza la entrega ordenada y sin pérdidas de los datos. MySQL utiliza TCP como protocolo de transporte sobre el puerto 3306. |
| 3 | Red | IP (IPv4) | Enruta los paquetes entre la workstation del analista y el servidor MySQL dentro de la red corporativa. |
| 2 | Enlace de datos | Ethernet 802.3 | Organiza los datos en tramas y gestiona el acceso al medio físico entre dispositivos conectados al mismo segmento de red. |
| 1 | Física | Par trenzado Cat6 / Fibra óptica | Transmite los bits como señales eléctricas u ópticas a través del cableado de la red. |

Como señala Stallings (2004), el proceso de comunicación implica la **encapsulación**
de los datos: cada capa agrega su propia cabecera (o trama) al bloque de datos recibido
de la capa superior, hasta llegar a la capa física donde los bits se transmiten por el medio.
En el receptor, el proceso se invierte: cada capa desencapsula y procesa su cabecera
correspondiente.

---

## 3. Pila TCP/IP del SGISI

El conjunto de protocolos TCP/IP, adoptado como estándar de facto para las
comunicaciones en redes modernas, se organiza en cuatro capas que se corresponden
con las capas del modelo OSI (Tanenbaum y Wetherall, 2012):

| Capa TCP/IP | Equivalencia OSI | Protocolo en el SGISI |
|-------------|-----------------|----------------------|
| Aplicación | Capas 5, 6 y 7 | JDBC / MySQL Protocol |
| Transporte | Capa 4 | TCP (puerto 3306) |
| Internet | Capa 3 | IPv4 |
| Acceso a la red | Capas 1 y 2 | Ethernet 802.3 + Cat6 / Fibra |

**¿Por qué TCP y no UDP?** TCP es un protocolo orientado a la conexión que
garantiza la entrega confiable y el orden de los segmentos (Stallings, 2004). Para una
base de datos transaccional como MySQL, donde la pérdida de un solo paquete puede
comprometer la integridad de una operación, TCP es la única opción adecuada.
UDP, en cambio, no ofrece confirmación de entrega y es apropiado para aplicaciones
que priorizan la velocidad sobre la confiabilidad (como streaming de video).

---

## 4. Infraestructura física y medios de transmisión

Los medios de transmisión son el soporte físico por el cual viajan las señales que
representan los datos. Stallings (2004) los clasifica en **medios guiados** (con soporte
físico concreto) y **medios no guiados** (inalámbricos). El SGISI utiliza exclusivamente
medios guiados en sus comunicaciones internas.

### 4.1 Par trenzado – Cat6

Las conexiones entre las workstations del SOC y el switch de acceso utilizan cable de
par trenzado no apantallado (UTP) categoría 6, que soporta velocidades de hasta
1 Gbps con distancias de hasta 100 metros. El trenzado de los pares reduce la
interferencia electromagnética entre conductores adyacentes (diafonía), mejorando la
calidad de la señal (Forouzan, 2006).

### 4.2 Fibra óptica

El enlace entre el switch de acceso y el servidor MySQL en el datacenter utiliza fibra
óptica multimodo. A diferencia del par trenzado, la fibra transmite la información
mediante pulsos de luz, lo que ofrece mayor velocidad, mayor distancia de transmisión
y total inmunidad ante interferencias electromagnéticas (Tanenbaum y Wetherall, 2012).
Esta característica es especialmente relevante en un datacenter bancario, donde los
equipos de alimentación pueden generar interferencias significativas.

### 4.3 Limitaciones en la transmisión

Cualquier medio de transmisión está sujeto a limitaciones que pueden degradar la señal
(Stallings, 2004):

- **Atenuación:** pérdida de potencia de la señal a medida que aumenta la distancia. Se
  mitiga con el uso de switches que regeneran la señal en cada segmento.
- **Ruido:** interferencias externas que alteran la señal original. El par trenzado y la
  fibra óptica minimizan este efecto dentro del datacenter.
- **Distorsión:** alteración de la forma de la señal por las características del medio.

---

## 5. Control de enlace de datos

La capa de enlace de datos (capa 2 del modelo OSI) es responsable de organizar los
bits provenientes de la capa física en **tramas** y de gestionar el acceso compartido al
medio de transmisión (Stallings, 2004).

### 5.1 Ethernet 802.3

La red del SGISI implementa el estándar Ethernet definido por el comité IEEE 802.3.
Una trama Ethernet está compuesta por: dirección MAC de destino, dirección MAC de
origen, tipo de protocolo, datos y un campo de verificación de errores (FCS).

Las direcciones MAC (Media Access Control) identifican de forma única cada
dispositivo en el segmento de red local. El switch utiliza estas direcciones para
reenviar las tramas únicamente al puerto de destino correcto, en lugar de difundirlas
a todos los dispositivos (Tanenbaum y Wetherall, 2012).

### 5.2 CSMA/CD

El método de acceso al medio utilizado por Ethernet es **CSMA/CD** (Carrier Sense
Multiple Access with Collision Detection). Su funcionamiento es el siguiente
(Tanenbaum y Wetherall, 2012):

1. Un dispositivo que desea transmitir escucha el medio para verificar que no haya
   tráfico activo (Carrier Sense).
2. Si el medio está libre, inicia la transmisión.
3. Si dos dispositivos transmiten simultáneamente, se produce una colisión que ambos
   detectan (Collision Detection).
4. Ante una colisión, ambos dispositivos detienen la transmisión, esperan un tiempo
   aleatorio y vuelven a intentar.

En redes modernas con switches (como la del banco), cada puerto del switch forma su
propio **dominio de colisión**, lo que elimina en la práctica las colisiones y mejora
notablemente el rendimiento respecto a las redes con hubs.

---

## 6. Entorno de red – LAN corporativa

### 6.1 Topología

La red del SOC de Banco Regional SA sigue una **topología en estrella**: todos los
dispositivos (workstations y servidor) se conectan a un switch central. Esta topología,
ampliamente descripta por Tanenbaum y Wetherall (2012), ofrece como ventajas la
facilidad de diagnóstico de fallas (un cable cortado solo afecta al dispositivo
conectado) y la posibilidad de agregar o quitar dispositivos sin interrumpir el resto
de la red.

### 6.2 Segmentación de la red

La red corporativa se divide en segmentos lógicos para separar el tráfico por función
y mejorar la seguridad. Como describe Tanenbaum y Wetherall (2012), la segmentación
reduce los **dominios de difusión**: los mensajes broadcast solo circulan dentro del
segmento al que pertenecen, sin propagarse a toda la red.

Los segmentos relevantes para el SGISI son:

| Segmento | Dispositivos | Función |
|----------|-------------|---------|
| Red SOC | Workstations de analistas y supervisor | Ejecución de la aplicación SGISI |
| Red Servidores | Servidor MySQL | Persistencia de datos del sistema |
| Red Gestión | Consola de administración | Administración de la base de datos |

El acceso entre segmentos está controlado por el switch de capa 3, que aplica
reglas para permitir únicamente el tráfico necesario: la aplicación SGISI (Red SOC)
puede comunicarse con el servidor MySQL (Red Servidores) por el puerto TCP 3306,
y el resto del tráfico no autorizado es descartado.

### 6.3 Tipo de red

Según la clasificación de Tanenbaum y Wetherall (2012), el SGISI opera sobre una
**red de área local (LAN)**, definida como una red privada que interconecta dispositivos
dentro de un área geográfica reducida (edificio o campus). La transmisión en la LAN del
banco es del tipo **conmutación de paquetes**: los datos se dividen en paquetes
independientes que viajan por la red y se reensamblan en el destino, lo que permite
compartir eficientemente el medio entre múltiples dispositivos.

---

## 7. Protocolos y estándares referenciados

| Protocolo / Estándar | Organismo | Función en el SGISI |
|---|---|---|
| Modelo OSI | ISO | Marco de referencia para el análisis por capas de la comunicación |
| TCP/IP | IETF | Conjunto de protocolos de comunicación entre aplicación y base de datos |
| TCP (puerto 3306) | IETF | Transporte confiable y ordenado de datos entre cliente y servidor MySQL |
| IPv4 | IETF | Direccionamiento y enrutamiento de paquetes en la LAN corporativa |
| Ethernet IEEE 802.3 | IEEE | Estándar de enlace de datos para la LAN del banco |
| CSMA/CD | IEEE | Método de acceso al medio en la red Ethernet |
| JDBC 4.3 | JCP | API de Java para el acceso a bases de datos relacionales |
| UTP Cat6 | TIA/EIA-568 | Estándar de cableado estructurado para conexiones de escritorio |

---

## 8. Referencias bibliográficas

Forouzan, B. (2006). *Transmisión de datos y redes de comunicaciones* (4.ª ed.). McGraw-Hill.

Stallings, W. (2004). *Comunicaciones y redes de computadores* (7.ª ed.). McGraw-Hill.

Tanenbaum, A. y Wetherall, D. (2012). *Redes de computadoras* (5.ª ed.). Pearson Education.
