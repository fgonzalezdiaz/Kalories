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