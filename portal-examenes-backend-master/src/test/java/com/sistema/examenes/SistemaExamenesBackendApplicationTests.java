package com.sistema.examenes;

import com.sistema.examenes.controladores.AuthenticationController;
import com.sistema.examenes.modelo.JwtRequest;
import com.sistema.examenes.modelo.JwtResponse;
import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import lombok.var;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SistemaExamenesBackendApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private UsuarioRepository usuarioRepository;


	private final BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();

	@Autowired
	private UsuarioService usuarioService;

	private MockMvc mockMvc;

	@Autowired
	private AuthenticationController authenticationController;


	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}


	@Nested
	@DisplayName("AuthenticatorController")
	public class authenticationControll{

	@Nested
	@DisplayName("Generar Token")
	public class generarToken {

		@Test
		@DisplayName("Test Generar Token")
		public void testGenerarToken() {
			Usuario usuario1 = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "ROL1");
			usuarioRepository.save(usuario1);
			//loginData = username, password





		}

		@Test
		@DisplayName("Test Generar Token Con Credenciales Invalidas")
		public void testGenerarTokenConCredencialesInvalidas() throws Exception {

		}

		@Test
		@DisplayName("test Generar Token Con Usuario Deshabilitado")
		public void testGenerarTokenConUsuarioDeshabilitado() throws Exception {

		}

	}

	@Nested
	@DisplayName("Autenticar")
	public class autenticar {




		@Test
		@DisplayName("Test autenticar")
		public void testAutenticar() {

			// Crear un objeto JwtRequest con el nombre de usuario y la contraseña
			JwtRequest jwtRequest = new JwtRequest("napazo2000", "password1");

			// Crear una cabecera HTTP para indicar el tipo de contenido de la solicitud
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// Crear una entidad HTTP con el objeto JwtRequest y la cabecera
			HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);

			// Enviar una solicitud POST al endpoint /generate-token con la entidad
			ResponseEntity<JwtResponse> response = restTemplate.exchange(
					"http://localhost:" + port + "/generate-token",
					HttpMethod.POST,
					request,
					JwtResponse.class
			);

			// Verificar que la respuesta tiene un estado HTTP 200 OK
			assertEquals(200, response.getStatusCodeValue());

			// Verificar que la respuesta contiene un objeto JwtResponse con un token no vacío
			JwtResponse jwtResponse = response.getBody();
			String token = jwtResponse.getToken();
			assertEquals(true, jwtResponse.getToken().length() > 0);

		}

		@Test
		@DisplayName("Test Autenticar Con Credenciales Invalidas")
		public void testAutenticarConCredencialesInvalidas() throws Exception {
			// Credenciales inválidas
			JwtRequest jwtRequest = new JwtRequest("napazo2000", "contraseñaInvalida");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);

			ResponseEntity<JwtResponse> response = restTemplate.exchange(
					"http://localhost:" + port + "/generate-token",
					HttpMethod.POST,
					request,
					JwtResponse.class
			);

			assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		}


	}

	@Nested
	@DisplayName("Obtener Usuario por ID")
	public class obtenerUsuarioPorId {

		@Test
		@DisplayName("Test Obtener Usuario Por Id Existente")
		public void testObtenerUsuarioPorIdExistente() {
			JwtRequest jwtRequest = new JwtRequest("napazo2000", "password1");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);
			ResponseEntity<JwtResponse> response = restTemplate.exchange(
					"http://localhost:" + port + "/generate-token",
					HttpMethod.POST,
					request,
					JwtResponse.class
			);
			JwtResponse jwtResponse = response.getBody();
			String token = jwtResponse.getToken();

			Usuario usuario = new Usuario(95L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "NORMAL");
			usuarioRepository.save(usuario);
			ResponseEntity<Optional<Usuario>> response2 = restTemplate.exchange(
					"http://localhost:" + port + "/user/" + usuario.getId(),
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<Optional<Usuario>>() {}
			);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		}


		@Test
		@DisplayName("Test Obtener Usuario Por Id Inexistente")
		public void testObtenerUsuarioPorIdInexistente() throws Exception {
			JwtRequest jwtRequest = new JwtRequest("napazo2000", "password1");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);
			ResponseEntity<JwtResponse> response = restTemplate.exchange(
					"http://localhost:" + port + "/generate-token",
					HttpMethod.POST,
					request,
					JwtResponse.class
			);
			JwtResponse jwtResponse = response.getBody();
			String token = jwtResponse.getToken();

			Usuario usuario = new Usuario(95L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "NORMAL");
			usuarioRepository.save(usuario);
			ResponseEntity<Optional<Usuario>> response2 = restTemplate.exchange(
					"http://localhost:" + port + "/user/1000" ,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<Optional<Usuario>>() {}
			);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		}

	}


	@Nested
	@DisplayName("Modificar Usuario")
	public class modificarUsuario {

		@Test
		@DisplayName("Test Modificar Usuario Existente")
		public void testModificarUsuarioExistente() {
			JwtRequest jwtRequest = new JwtRequest("napazo2000", "password1");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);
			ResponseEntity<JwtResponse> response = restTemplate.exchange(
					"http://localhost:" + port + "/generate-token",
					HttpMethod.POST,
					request,
					JwtResponse.class
			);
			JwtResponse jwtResponse = response.getBody();
			String token = jwtResponse.getToken();

			Usuario usuario = new Usuario(95L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "NORMAL");
			usuarioRepository.save(usuario);

			usuario.setApellido("NuevoApellido");
			HttpEntity<Usuario> request2 = new HttpEntity<>(usuario, headers);

			ResponseEntity<Usuario> response2 = restTemplate.exchange(
					"http://localhost:" + port + "user/" + usuario.getId(),
					HttpMethod.PUT,
					request2,
					Usuario.class
			);

			assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response2.getBody().getApellido()).isEqualTo("NuevoApellido");
		}




		@Test
		@DisplayName("Test Modificar Usuario Inexistente")
		public void testModificarUsuarioInexistente() throws Exception {

		}

	}


	@Nested
	@DisplayName("Obtener Usuarios Actual")
	public class obtenerUsuarioActual {

		@Test
		@DisplayName("Test Obtener Usuario Actual")
		public void testObtenerUsuarioActual() throws Exception {
			// Given
			Usuario usuario1 = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "ROL1");
			Usuario usuario2 = new Usuario(2L, "usuario2", "password2", "Nombre2", "Apellido2", "email2@dominio.com", "1234567891", true, "ROL2");
			List<Usuario> usuariosEsperados = List.of(usuario1, usuario2);
			// When
			List<Usuario> usuariosObtenidos = authenticationController.obtenerUsuarios();
			// Then
			assertThat(usuariosObtenidos.size() == 2);
		}
	}

	@Nested
	@DisplayName("Obtener Usuarios")
	public class authenticationControllerGET {

		@Test
		@DisplayName("Devolver lista usuarios")
		public void devolverListaUsuarios() throws Exception {
			// Given
			Usuario usuario1 = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "ROL1");
			Usuario usuario2 = new Usuario(2L, "usuario2", "password2", "Nombre2", "Apellido2", "email2@dominio.com", "1234567891", true, "ROL2");
			List<Usuario> usuariosEsperados = List.of(usuario1, usuario2);
			// When
			List<Usuario> usuariosObtenidos = authenticationController.obtenerUsuarios();
			// Then
			assertThat(usuariosObtenidos.size() == 2);
		}
	}


	@Nested
	@DisplayName("Eliminar Usuario")
	public class eliminarUsuario {

		@Test
		@DisplayName("Test Eliminar Usuario Existente")
		public void testEliminarUsuarioExistente() {

		}

		@Test
		@DisplayName("Test Eliminar Usuario Inexistente")
		public void testEliminarUsuarioInexistente() throws Exception {

		}

	}


	@Nested
	@DisplayName("Cambiar Contraseña")
	public class cambiarContrasena {

		@Test
		@DisplayName("Test Change Password")
		public void testChangePassword() {

		}

		@Test
		@DisplayName("Test Change Password Con Usuario Inexistente")
		public void testChangePasswordConUsuarioInexistente() throws Exception {

		}

		@Test
		@DisplayName("Test Change Password Con Credenciales Incorrectas")
		public void testChangePasswordConCredencialesIncorrectas() throws Exception {

		}

	}

}

	@Nested
	@DisplayName("UsuarioController")
	public class UsuarioControll{

		@Nested
		@DisplayName("Guardar Usuario")
		public class guardarUsuario {

			@Test
			@DisplayName("Debe Devolver 200 ")
			public void testGuardarUsuario() {
				Usuario usuario = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email1@dominio.com", "1234567890", true, "NORMAL");

				var peticion = post("http", "localhost",port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			}

			@Test
			@DisplayName("Debe Asignar Rol Normal")
			public void testGuardarUsuarioDebeAsignarRolNormal() throws Exception {
				Usuario usuario = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email2@dominio.com", "1234567890", true, "NORMAL");

				var peticion = post("http", "localhost",port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Usuario usuario1 = usuarioRepository.findByEmail("email24@dominio.com");
				assertThat(usuario1.getRolAsignado() == "NORMAL");
			}

			@Test
			@DisplayName("Debe Cifrar Correctamente La Contrasena")
			public void testGuardarUsuarioDebeCifrarCorrectamenteLaContrasena() throws Exception {
				Usuario usuario = new Usuario(1L, "usuario1", "password1", "Nombre1", "Apellido1", "email3@dominio.com", "1234567890", true, "NORMAL");
				String password = "password1";
				var peticion = post("http", "localhost",port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Usuario usuario1 = usuarioRepository.findByEmail("email24@dominio.com");

				assertThat(encoder2.matches(password, usuario1.getPassword()));
			}

		}

		@Nested
		@DisplayName("request Password")
		public class requestPassword {

			@Test
			@DisplayName("Debe Devolver Usuario Despues De Actualizar Su Contrasena")
			public void testRequestPasswordDebeDevolverUsuarioDespuesDeActualizarSuContrasena() {

			}

			@Test
			@DisplayName("Debe Generar Contrasena Aleatoria Con Longitud Diez")
			public void testRequestPasswordDebeGenerarContrasenaAleatoriaConLongitudDiez() throws Exception {

			}

			@Test
			@DisplayName("Debe Devolver Null Si No Se Encuentra Usuario Asociado Al Correo Electronico")
			public void testRequestPasswordDebeDevolverNullSiNoSeEncuentraUsuarioAsociadoAlCorreoElectronico() throws Exception {

			}

		}



		@Nested
		@DisplayName("Obtener Usuario")
		public class obtenerUsuario {

			@Test
			@DisplayName("Debe Devolver Usuario Correcto Para Nombre De Usuario Valido")
			public void testObtenerUsuarioDebeDevolverUsuarioCorrectoParaNombreDeUsuarioValido() {
				Usuario usuario = new Usuario(1L, "usuarioObtener", "password1", "Nombre1", "Apellido1", "email3@dominio.com", "1234567890", true, "NORMAL");
				usuarioRepository.save(usuario);
				var peticion = get("http", "localhost",port, "/usuarios/usuarioObtener");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			}

			@Test
			@DisplayName("Debe Devolver Null Si Se Proporciona Nombre De Usuario No Valido")
			public void testObtenerUsuarioDebeDevolverNullSiSeProporcionaNombreDeUsuarioNoValido() throws Exception {

			}

		}


		@Nested
		@DisplayName("Eliminar Usuario")
		public class eliminarUsuario {

			@Test
			@DisplayName("Debe Eliminar Usuario Correctamente De La Base De Datos")
			public void EliminarUsuarioDebeEliminarUsuarioCorrectamenteDeLaBaseDeDatos() {

			}

			@Test
			@DisplayName("No Debe Generar Excepcion Si Se Proporciona ID De Usuario No Valido")
			public void testEliminarUsuarioNoDebeGenerarExcepcionSiSeProporcionaIDDeUsuarioNoValido() throws Exception {

			}

		}


	}


}


