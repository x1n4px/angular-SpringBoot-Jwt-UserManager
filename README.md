# angular-SpringBoot-Jwt-UserManager

# Back-end (Spring-boot)

### Entidades:

Usuario(id, username, password, nombre, apellidos, email, telefono, perfil)

Rol(rolId, rolNombre)

usuarioRol(usuarioRolId)

Un ejemplo de usuario normal:

Usuario -> (1, xxjuan, 12345, juan, mendoza, juanmendoza@gmail.com, 910000000, )

rol -> (1, NORMAL)

usuarioRol -> (1, 1) 

Esto implica que el usuario con id = 1, tiene rol normal, es decir, podrá acceder a la vista de un usuario no admin, a su vez, tendremos un adminitrador configurado por la base de datos, que correspondera a rol -> (2, ADMIN)

### JWT

JSON Web Token es un estándar abierto basado en JSON propuesto por IETF para la creación de tokens de acceso que permiten la propagación de identidad y privilegios, nos permite autenticar el inicio de sesion de un usuario, y a su vez, el cierre de sesion de la misma a través de un token de sesión

Este esta desarrollado a lo largo de todo el proyecto

Contamos con 3 clases dentro del modelo:

- Authority.java

- JwtRequest.java

- JwtResponse.java

Gestionaran las peticiones del usuario, a su vez, tiene funciones a través del repositorio y del servicio


### Para gestionar al usuario, recibiremos peticiones de parte de angular, en el cual, según necesidades, nos pasarán los datos necesarios para el mismo, por tanto, en el controlador, nos encontraremos las funciones.



# Angular

Angular se encarga del front-end de la aplicación, por tanto, contamos con un paquete APP donde vamos a desarrollar todo el código del proyecto, en el mismo encontraremos 

- Navbar -> Barra que se mantiene en todas las vistas, la cual tiene botones con ciertas utilidades

- Pages -> Una carpeta por cada pagina, en la cual, contamos con 4 clases, html y css para la vista web del usuario, y un componente que vamos a desarrollar para la accion de cada página, ya sea un formulario, una vista estática con algún botón, o simplemente, una vista de gestión de usuarios 

- Servicios -> Aqui vamos a desarrollar las rutas de las paginas, para que la acción de los botones rediriga de una forma correcta a la página, a su vez, es donde vamos a definir la petición hacia el back-end
