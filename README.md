# Aplicación Android para la "Autenticación, Persistencia y Conexión con Servicios Remotos"

## Descripción del Proyecto

Este proyecto Android, desarrollado para el trabajo "Autenticación, Persistencia y Conexión con Servicios Remotos," se enfoca en el registro y autenticación de usuarios utilizando Firebase Authentication, así como en la conexión con servicios de datos externos para obtener información climatológica. La aplicación consta de dos secciones principales: la autenticación de usuarios y la consulta de datos sobre el tiempo en una ubicación.

## Funcionalidades Principales

### Autenticación de Usuarios

La aplicación presenta una tarjeta en la pantalla principal que permite a los usuarios autenticarse. Hay dos opciones disponibles:

1. **Registro e Inicio de Sesión con Correo Electrónico y Contraseña**: Los usuarios pueden  registrarse e iniciar sesión utilizando su correo electrónico y contraseña con Firebase.

2. **Inicio de Sesión Anónimo**: También se ofrece la opción de iniciar sesión de forma anónima para aquellos usuarios que deseen explorar la aplicación sin registrarse.

### Consulta de Datos del Tiempo

La segunda tarjeta en la pantalla principal proporciona acceso a la información del tiempo actual y la predicción del tiempo. Aquí se describen los pasos para utilizar esta funcionalidad:

1. **Inicio de Sesión Requerido**: Cuando se inicia la aplicación, se verifica si el usuario ha iniciado sesión. Si no lo ha hecho, se le redirige automáticamente a la actividad de autenticación para iniciar sesión o registrarse.

2. **Permiso de Ubicación**: Si el usuario ha iniciado sesión, la aplicación solicitará acceso a su ubicación actual. En caso de que el usuario otorgue este permiso, la aplicación mostrará automáticamente el tiempo en su ubicación actual.

3. **Ubicación Manual**: Si el usuario no otorga el permiso de ubicación, se le pedirá que introduzca manualmente la ubicación de la cual desea obtener información sobre el tiempo.

4. **Obtención de Datos del Tiempo**: Se realiza de forma automática en caso de haberse concedido permisos de ubicación. En caso de búesqueda manual, una vez que se haya ingresado la ubicación deseada, el usuario deberá hacer clic en el botón "Fetch Weather Info." La aplicación utilizará dos APIs de OpenWeatherMap: una para obtener las condiciones climáticas en tiempo real y otra para acceder al pronóstico de los próximos 5 días, siendo esta última utilizada para mostrar el pronóstico para las próximas 24 horas.

5. **Persistencia de los Datos**: Cada vez que un usuario busca el tiempo de una ubicación específica. Estos datos se almacenan en Firebase Firestore y se les agrega un timestamp proporcionaddo por la API que varia solo cuando actualiza sus datos, así se evitan tener duplicados.

### Visualización de Datos

En la actividad del tiempo, se muestra la siguiente información:

- **Ubicación y País**: Se muestra el nombre de la ubicación y su país correspondiente.
- **Tiempo Actual**: Se muestra el estado actual del tiempo.
- **Tiempo en las Siguientes 24 Horas**: Se proporciona información sobre las condiciones climáticas para las próximas 24 horas.
- **Pronóstico para Hoy y los Próximos 5 Días**: Se muestra el pronóstico del tiempo para el día actual y los próximos 5 días.

### Histórico de Temperaturas

La aplicación también ofrece la funcionalidad de histórico de temperaturas. Para acceder a esta función, se encuentra un icono de histórico en la parte superior derecha del menú. Al hacer clic en él, si ya se ha buscado información del tiempo para una ubicación, la aplicación busca en la base de datos todos los registros históricos de temperaturas para esa ubicación, en caso contrario se le pide al usuario buscar primero el tiempo para una ubicación.

## Futuro Desarrollo

En futuras iteraciones del proyecto, se prevé mejorar la presentación de la información del tiempo utilizando iconos e imágenes. También se considera la posibilidad de incorporar diagramas o gráficos para mostrar de manera más visual la variación de temperatura a lo largo del tiempo en una ubicación determinada.

## Información Adicional

- **Nombre del Proyecto:** Autenticación, Persistencia y Conexión con Servicios Remotos
- **Asignatura:** FEM (Front-End para Móviles)

## Tecnologías y Librerías Utilizadas

- Firebase Authentication: Para la autenticación de usuarios.
- Firebase Firestore: Para el almacenamiento de datos históricos y persistencia en la nube.
- OpenWeatherMap API: Para obtener datos climatológicos en tiempo real y pronósticos del tiempo.
- Android Studio: Entorno de desarrollo integrado para la creación de la aplicación Android.
- Java: Lenguajes de programación utilizados en el desarrollo de la aplicación.
- Retrofit: Librerías para la conexión y obtención de datos desde servicios web externos.

---

Este proyecto ofrece una experiencia de usuario completa que combina la autenticación de usuarios con la obtención de datos en tiempo real, la predicción del tiempo y la visualización de datos históricos. ¡Espero que esta descripción te haya dado una idea clara de las funcionalidades y el propósito de la aplicación!