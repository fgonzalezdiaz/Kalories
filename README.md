# Kalories

## Funcionalidades Recientes

### Gestión de Peso (RecyclerView y Filtro)
Se ha implementado una lista dinámica usando `RecyclerView` en la actividad `historial_de_pesos`.
- **Persistencia**: Los datos se guardan en almacenamiento local modificable (internal storage), migrando desde el archivo estático en assets.
- **Filtrado**: Se ha añadido un campo de texto y un botón de lupa que permiten filtrar los registros de peso por fecha o valor.
- **Adaptador Personalizado**: Se utiliza un `CustomAdapter` para gestionar la visualización de los items en la lista.

### Navegación (BottomNavigationBar)
Se ha integrado una barra de navegación inferior (`BottomNavigationBar`) en las actividades principales: `MainMenu`, `DailyReports` y `historial_de_pesos`.
- **Navegación Unificada**: Permite el acceso rápido a Home, ContactUs, Configuration y LogOut desde las pantallas principales.
- **Gestión de Fragmentos**:
  - Las opciones de "Dashboard" y "Settings" abren sus respectivos fragmentos (`ContactUs` y `Configuration`) en un `FrameLayout`.
  - Se ha configurado el layout para que el contenedor de los fragmentos **no oculte** la barra de navegación, permitiendo seguir interactuando con el menú.
- **Flujo de Usuario**: Se mantiene la consistencia visual y funcional en toda la aplicación.

### Implementación de ViewModel (Login y SignIn)
Se ha refactorizado la lógica de autenticación en las actividades `Login` y `SignInActivity` siguiendo el patrón de arquitectura **MVVM (Model-View-ViewModel)**.

- **Separación de Responsabilidades**:
  - La lógica de negocio (validación de email, contraseñas, etc.) se ha movido de la Activity al ViewModel (`LoginControl` y `SignInControl`).
  - La Activity se encarga únicamente de la UI y de observar los cambios en el estado.

- **Programación Reactiva con LiveData**:
  - Se utilizan objetos `LiveData` y `MutableLiveData` para manejar el estado de los campos de texto (`email`, `password`) y los mensajes de error (`errorEmail`, `errorPassword`).
  - En lugar de llamar a una función de validación y esperar un valor de retorno (estilo imperativo), la Activity **observa** las variables de error expuestas por el ViewModel.
  - Cuando el usuario escribe, el ViewModel valida los datos y actualiza el `LiveData` de error. La Activity reacciona automáticamente a estos cambios mostrando u ocultando los mensajes de error en la UI.

Esta implementación mejora la testabilidad y asegura que la UI esté siempre sincronizada con el estado actual de los datos, evitando llamadas bloqueantes o gestión manual de estados.