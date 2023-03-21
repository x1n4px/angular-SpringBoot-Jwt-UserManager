# angular-SpringBoot-Jwt-UserManager

# Tasks
- [x] Crear entidades en proyecto java
- [x] Crear relaciones en el modelo
- [x] Añadir JWT al proyecto
- [x] Crear Repositorio, Servicio, Controlador
- [x] Añadir funciones al Repositorio, Servicio, Controlador
- [x] Añadir dependencias al pom
- [x] Añadir ajustes a las propiedades del proyecto
- [x] Crear entidades en la base de datos H2
- [x] Crear proyeto angular
- [x] Crear componentes necesarios
- [x] Añadir HTML
- [x] Añadir CSS
- [x] Añadir TS
- [x] Añadir ruta de TS
- [x] Añadir funciones en Service.ts
- [x] Conectar funcione angular con las funciones en spring boot
- [x] Comprobar conexiones
- [x] Comprobar console.log()
- [x] Añadir login con contraseña cifrada
- [x] Añadir Registro con contraseña cifrada
- [x] Añadir perfil (vista usuario normal)
- [x] Añadir cambiar contraseña post-login (cifrada)
- [x] Añadir vista gestión usuario (vista admin)
- [x] Añadir eliminar usuario (vista admin)
- [x] Añadir modificar usuario (vista admin)
- [x] Añadir cambiar contraseña pre-login (generar contraseña cifrada y enviar por email)


### generarToken:

- [ ] testGenerarToken - probar la generación de un token exitoso

- [ ] testGenerarTokenConCredencialesInvalidas - probar la generación de un token con credenciales incorrectas
 
- [ ] testGenerarTokenConUsuarioDeshabilitado - probar la generación de un token con un usuario deshabilitado

### autenticar:

- [x] testAutenticar - probar la autenticación exitosa de un usuario

- [x] testAutenticarConCredencialesInvalidas - probar la autenticación con credenciales incorrectas



### obtenerUsuarioPorId:

- [x] testObtenerUsuarioPorIdExistente - probar la obtención exitosa de un usuario existente por su id

- [x] testObtenerUsuarioPorIdInexistente - probar la obtención de un usuario inexistente por su id


### modificarUsuario:

- [ ] testModificarUsuarioExistente - probar la modificación exitosa de un usuario existente

- [ ] testModificarUsuarioInexistente - probar la modificación de un usuario inexistente


### obtenerUsuarioActual:

- [x] testObtenerUsuarioActual - probar la obtención exitosa del usuario actual


### obtenerUsuarios:

- [x] testObtenerUsuarios - probar la obtención exitosa de la lista de usuarios

### eliminarUsuario:

- [ ] testEliminarUsuarioExistente - probar la eliminación exitosa de un usuario existente

- [ ] testEliminarUsuarioInexistente - probar la eliminación de un usuario inexistente


### changePassword:

- [ ] testChangePassword - probar el cambio de contraseña exitoso

- [ ] testChangePasswordConUsuarioInexistente - probar el cambio de contraseña con un usuario inexistente

- [ ] testChangePasswordConCredencialesIncorrectas - probar el cambio de contraseña con credenciales incorrectas


## UsuarioController

### Guardar Usuario

- [x] Debe devolver 200

- [x] Debe asignar Rol normal

- [x] Debe cifrar Correctamente la contrseña

### Request password

- [ ] Debe Devolver Usuario Despues De Actualizar Su Contrasena

- [ ] Debe Generar Contrasena Aleatoria Con Longitud Diez

- [ ] Debe Devolver Null Si No Se Encuentra Usuario Asociado Al Correo Electronico

### Obtener Usuario

- [ ] Debe Devolver Usuario Correcto Para Nombre De Usuario Valido

- [ ] Debe Devolver Null Si Se Proporciona Nombre De Usuario No Valido

### Eliminar Usuario

- [ ] Debe Eliminar Usuario Correctamente De La Base De Datos

- [ ] No Debe Generar Excepcion Si Se Proporciona ID De Usuario No Valido
