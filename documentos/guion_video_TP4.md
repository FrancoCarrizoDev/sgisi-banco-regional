# Guion del video — TP4 SGISI (~3 minutos)

**Alumno:** Carrizo, Franco Adrián — Legajo VINF017496
**Proyecto:** SGISI – Sistema de Gestión de Incidentes de Seguridad Informática (Banco Regional SA)
**Materia:** Seminario de Práctica Informática – Módulo 4

> Formato de uso: la columna izquierda es lo que **decís** (leelo de corrido, ritmo tranquilo).
> La columna entre corchetes **[ ]** es lo que se debe **ver en pantalla** en ese momento.
> Objetivo: una sola toma de grabación de pantalla con audio. Duración objetivo: 2:50–3:00.

---

## Checklist ANTES de grabar (no saltear)

- [ ] El prototipo compila y arranca con `mvn clean compile` + `mvn exec:java`.
- [ ] La base MySQL está levantada (`docker compose up -d`) y tiene **2–3 incidentes ya cargados** (para que el listado no se vea vacío).
- [ ] Tener abierto MySQL Workbench (o consola) con la query `SELECT * FROM incidente;` lista para ejecutar.
- [ ] Tener abierto el diagrama de clases en una pestaña/ventana.
- [ ] Tener abierta la página del repositorio de GitHub.
- [ ] Cerrar notificaciones, mail, Slack. Pantalla limpia.
- [ ] Probar el recorrido completo UNA vez sin grabar, para que salga fluido.

---

## GUION

### Bloque 1 — Presentación y problema · 0:00 – 0:25
*[En pantalla: portada del proyecto o el documento del TP, con título, tu nombre y legajo.]*

> "Buenas. Mi nombre es Franco Carrizo, legajo VINF017496. Les presento el trabajo integrador del Seminario de Práctica Informática: SGISI, el Sistema de Gestión de Incidentes de Seguridad Informática, desarrollado para el Banco Regional.
>
> El problema que resuelve es concreto: hoy el equipo de seguridad del banco registra y sigue los incidentes de forma manual y dispersa, sin trazabilidad ni control de los tiempos de respuesta. SGISI centraliza el registro, el seguimiento del ciclo de vida y la auditoría de cada incidente."

---

### Bloque 2 — Arquitectura y patrones · 0:25 – 0:50
*[En pantalla: el diagrama de clases. Señalá las capas con el cursor mientras hablás.]*

> "La solución se construyó en Java 21, con interfaz gráfica en Swing y persistencia en MySQL a través de JDBC. La arquitectura está organizada en cuatro capas: presentación, servicio, acceso a datos y modelo.
>
> Se aplicaron tres patrones de diseño: State, para gobernar el ciclo de vida del incidente; DAO con interfaces, para desacoplar la lógica de negocio del acceso a datos; y Singleton, en la gestión de la conexión a la base."

---

### Bloque 3 — DEMO en vivo · 0:50 – 2:30  *(el núcleo del video)*
*[En pantalla: la aplicación corriendo.]*

> "Veamos el prototipo funcionando."

*[Pantalla de login.]*
> "Primero, el inicio de sesión. Las contraseñas se almacenan hasheadas, nunca en texto plano."

*[Escribí credenciales inválidas y dale entrar — mostrá el cartel de error.]*
> "Si las credenciales son inválidas, el sistema captura la excepción y avisa, sin caerse."

*[Ahora ingresá con un usuario analista válido.]*
> "Ingreso con un usuario analista."

*[Abrí el formulario de registro de incidente y completalo.]*
> "Registro un incidente nuevo: selecciono el tipo, la severidad y el activo afectado, y guardo."

*[Guardá. Cambiá a MySQL Workbench y ejecutá `SELECT * FROM incidente;`]*
> "Para comprobar que la persistencia es real, voy a la base de datos MySQL y consulto la tabla incidente. Ahí está el registro recién creado, con su fecha y su estado inicial."

*[Volvé a la app. Cambiá el estado del incidente — UC04.]*
> "Cada incidente tiene un ciclo de vida. Cambio su estado: el patrón State solo habilita las transiciones válidas."

*[Intentá una transición no permitida — mostrá el rechazo.]*
> "Si intento una transición no permitida, el sistema la rechaza."

*[Volvé a Workbench y re-ejecutá la query — mostrá el estado actualizado.]*
> "Vuelvo a la base y se ve el estado actualizado."

*[Volvé a la app, a la lista de incidentes, y aplicá un filtro por estado y severidad.]*
> "Por último, la lista de incidentes permite filtrar por estado y por severidad sobre la tabla."

---

### Bloque 4 — Cierre técnico · 2:30 – 3:00
*[En pantalla: la página del repositorio de GitHub.]*

> "En síntesis, el prototipo cumple los requisitos del trabajo: usa interfaces y clases abstractas para favorecer la reutilización de métodos, combina arreglos y ArrayList de forma complementaria, y maneja excepciones en toda la interacción con la base de datos.
>
> El código completo, junto con los diagramas y el script de la base, está disponible en este repositorio de GitHub. Muchas gracias."

---

## Notas

- **Ritmo:** son ~400 palabras habladas. A ritmo normal entran en 3 minutos con margen. Si vas justo de tiempo, el bloque que se puede acortar es el 2 (arquitectura), no la demo.
- **Audio:** grabá en un ambiente silencioso. Si trabás una frase, parás, respirás y la repetís entera — después se corta en edición, o se regraba la toma.
- **Reemplazar:** poné la URL real del repo en pantalla en el bloque 4 (no hace falta leerla en voz alta).
- **Lo que MÁS suma puntos:** mostrar el dato apareciendo/cambiando en la tabla MySQL después de cada acción. Es la prueba directa de "establecer conexiones, realizar consultas, actualizar registros y presentar resultados", que el enunciado repite tres veces.
