package com.sistema.examenes;

import com.sistema.examenes.controladores.AuthenticationController;
import com.sistema.examenes.controladores.UsuarioController;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.*;

@SuppressWarnings("ALL")
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

	@Autowired
	private UsuarioController usuarioController;

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
	public class authenticationControll {

		@Nested
		@DisplayName("Generar Token") //FUNCIONANDO
		public class generarToken {

			@Test
			@DisplayName("Test Generar Token")
			public void testGenerarToken() {

				//loginData = username, password
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

				assertThat(!token.isEmpty());


			}

			@Test
			@DisplayName("Test Generar Token Con Credenciales Invalidas")
			public void testGenerarTokenConCredencialesInvalidas() throws Exception {
				//loginData = username, password
				JwtRequest jwtRequest = new JwtRequest("napazo2000", "password2");
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

				assertThat(response.badRequest().body("Credenciales inválidas"));
			}

			@Test
			@DisplayName("test Generar Token Con Usuario Deshabilitado")
			public void testGenerarTokenConUsuarioDeshabilitado() throws Exception {
//loginData = username, password
				JwtRequest jwtRequest = new JwtRequest("napazo2000", "password2");
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

				assertThat(response.badRequest().body("Usuario no encontrado"));
			}

		}


		@Nested
		@DisplayName("Obtener Usuario por ID") //FUNCIONANDO
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
				assertThat(token).isNotBlank();

				//Usuario usuario = new Usuario(null, "usuari2", "password1", "Nombre1", "Apellido1", "email2@dominio.com", "1234567890", true, "NORMAL");
				//usuarioRepository.save(usuario);
				//assertThat(usuarioRepository.findById(usuario.getId())).isNotEmpty();

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Optional<Usuario>> request2 = new HttpEntity<>(null, headers2);
				ResponseEntity<Optional<Usuario>> response2 = restTemplate.exchange("http://localhost:" + port + "/user/29",
						HttpMethod.GET,
						request2,
						new ParameterizedTypeReference<Optional<Usuario>>() {
						}
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

				// Verificar que la respuesta contiene un usuario y que su ID es igual a 1
				Optional<Usuario> usuarioResponse = response2.getBody();
				assertThat(usuarioResponse.isPresent()).isTrue();
				assertThat(usuarioResponse.get().getId()).isEqualTo(29L);
			}


			@Test
			@DisplayName("Test Obtener Usuario Por Id Inexistente")
			@DirtiesContext
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
				assertThat(token).isNotBlank();

				//Usuario usuario = new Usuario(null, "usuari2", "password1", "Nombre1", "Apellido1", "email2@dominio.com", "1234567890", true, "NORMAL");
				//usuarioRepository.save(usuario);
				//assertThat(usuarioRepository.findById(usuario.getId())).isNotEmpty();

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Optional<Usuario>> request2 = new HttpEntity<>(null, headers2);
				ResponseEntity<Optional<Usuario>> response2 = restTemplate.exchange("http://localhost:" + port + "/user/1",
						HttpMethod.GET,
						request2,
						new ParameterizedTypeReference<Optional<Usuario>>() {
						}
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getBody()).isEmpty();



			}

		}


		@Nested
		@DisplayName("Modificar Usuario") //FUNCIONANDO
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

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";


				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				usuarioRepository.save(usuario);
				usuario.setApellido("Fern");


				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Usuario> request2 = new HttpEntity<>(usuario, headers2);


				ResponseEntity<Usuario> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/" + usuario.getId() ,
						HttpMethod.PUT,
						request2,
						Usuario.class
				);

				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

			}


			@Test
			@DisplayName("Test Modificar Usuario Inexistente")
			public void testModificarUsuarioInexistente() throws Exception {
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

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";


				Usuario usuario = new Usuario();
				usuario.setApellido("Fern");


				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Usuario> request2 = new HttpEntity<>(usuario, headers2);


				ResponseEntity<Usuario> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/1"  ,
						HttpMethod.PUT,
						request2,
						Usuario.class
				);

				assertThat(response2.getStatusCode().value()).isEqualTo(404);

			}

		}


		@Nested
		@DisplayName("Obtener Usuarios Actual") //FUNCIONANDO
		public class obtenerUsuarioActual {

			@Test
			@DisplayName("Test Obtener Usuario Actual")
			public void testObtenerUsuarioActual() throws Exception {
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


				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<String> request2 = new HttpEntity<>(null, headers2);

				ResponseEntity<Usuario> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/actual",
						HttpMethod.GET,
						request2,
						Usuario.class
				);

				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
				Usuario usuario = response2.getBody();
				assertThat(usuario.getUsername()).isEqualTo("napazo2000");
			}
		}

		@Nested
		@DisplayName("Obtener Usuarios") //FUNCIONANDO
		public class obtenerUsuarios {

			@Test
			@DisplayName("Devolver lista usuarios") //FUNCIONANDO
			public void devolverListaUsuarios() throws Exception {
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
				assertThat(token).isNotBlank();

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Void> request2 = new HttpEntity<>(null, headers2);
				ResponseEntity<List<Usuario>> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/todos",
						HttpMethod.GET,
						request2,
						new ParameterizedTypeReference<List<Usuario>>() {}
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

				// Verificar que la lista de usuarios no es nula y tiene al menos un usuario
				List<Usuario> usuarios = response2.getBody();
				assertThat(usuarios).isNotNull();
				assertThat(usuarios.size()).isGreaterThan(0);
			}
		}


		@Nested
		@DisplayName("Eliminar Usuario")//FUNCIONANDO
		public class eliminarUsuario {

			@Test
			@DisplayName("Test Eliminar Usuario Existente")
			public void testEliminarUsuarioExistente() {

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
					assertThat(token).isNotBlank();

					String username = "usuaOO_" + UUID.randomUUID().toString();
					String email = UUID.randomUUID().toString() + "@dominio.com";


					Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
					usuarioRepository.save(usuario);



					HttpHeaders headers2 = new HttpHeaders();
					headers2.setContentType(MediaType.APPLICATION_JSON);
					headers2.setBearerAuth(token);
					HttpEntity<Usuario> request2 = new HttpEntity<>(usuario, headers2);
					ResponseEntity<Void> response2 = restTemplate.exchange(
							"http://localhost:" + port + "/user/eliminarUsuario/" + usuario.getId(),
							HttpMethod.DELETE,
							request2,
							Void.class,
							usuario.getId()
					);

					// Verificar que la respuesta tiene un código de estado OK (200)
					assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
				}

				@Test
				@DisplayName("Test Eliminar Usuario Inexistente")
				public void testEliminarUsuarioInexistente() throws Exception {

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
					assertThat(token).isNotBlank();




					Usuario usuario = new Usuario();
					usuario.setId(1L);



					HttpHeaders headers2 = new HttpHeaders();
					headers2.setContentType(MediaType.APPLICATION_JSON);
					headers2.setBearerAuth(token);
					HttpEntity<Usuario> request2 = new HttpEntity<>(usuario, headers2);
					ResponseEntity<Void> response2 = restTemplate.exchange(
							"http://localhost:" + port + "/user/eliminarUsuario/" + usuario.getId(),
							HttpMethod.DELETE,
							request2,
							Void.class,
							usuario.getId()
					);

					// Verificar que la respuesta tiene un código de estado OK (200)
					assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				}

			}





		@Nested
		@DisplayName("Cambiar Contraseña") //FUNCIONANDO
		public class cambiarContrasena {

			@Test
			@DisplayName("Test Change Password")
			public void testChangePassword() {
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
				assertThat(token).isNotBlank();

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";


				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				String password = "password1";
				usuario.setPassword(encoder2.encode(usuario.getPassword()));
				usuarioRepository.save(usuario);


				// Crear un objeto Map con los valores de email, contraseña actual y contraseña nueva
				Map<String, String> requestMap = new HashMap<>();
				requestMap.put("email", usuario.getEmail());
				requestMap.put("currentPassword", password);
				requestMap.put("newPassword", "usuario");

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Map<String, String>> request2 = new HttpEntity<>(requestMap, headers2);
				ResponseEntity<String> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/perfil/cambiarClave",
						HttpMethod.POST,
						request2,
						String.class
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
			}

			@Test
			@DisplayName("Test Change Password Con Usuario Inexistente")
			public void testChangePasswordConUsuarioInexistente() throws Exception {
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
				assertThat(token).isNotBlank();

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";


				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				String password = "password1";
				usuario.setPassword(encoder2.encode(usuario.getPassword()));
				//usuarioRepository.save(usuario);


				// Crear un objeto Map con los valores de email, contraseña actual y contraseña nueva
				Map<String, String> requestMap = new HashMap<>();
				requestMap.put("email", usuario.getEmail());
				requestMap.put("currentPassword", password);
				requestMap.put("newPassword", "usuario");

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Map<String, String>> request2 = new HttpEntity<>(requestMap, headers2);
				ResponseEntity<String> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/perfil/cambiarClave",
						HttpMethod.POST,
						request2,
						String.class
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			}

			@Test
			@DisplayName("Test Change Password Con Credenciales Incorrectas")
			public void testChangePasswordConCredencialesIncorrectas() throws Exception {
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
				assertThat(token).isNotBlank();

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";


				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				String password = "pass";
				usuario.setPassword(encoder2.encode(usuario.getPassword()));
				usuarioRepository.save(usuario);


				// Crear un objeto Map con los valores de email, contraseña actual y contraseña nueva
				Map<String, String> requestMap = new HashMap<>();
				requestMap.put("email", usuario.getEmail());
				requestMap.put("currentPassword", password);
				requestMap.put("newPassword", "usuario");

				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Map<String, String>> request2 = new HttpEntity<>(requestMap, headers2);
				ResponseEntity<String> response2 = restTemplate.exchange(
						"http://localhost:" + port + "/user/perfil/cambiarClave",
						HttpMethod.POST,
						request2,
						String.class
				);

				// Verificar que la respuesta tiene un código de estado OK (200)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			}

		}


	}



	@Nested
	@DisplayName("UsuarioController")
	public class UsuarioControll {

		@Nested
		@DisplayName("Guardar Usuario") //FUNCIONANDO
		public class guardarUsuario {

			@Test
			@DisplayName("Debe Devolver 200 ")
			@DirtiesContext
			public void testGuardarUsuario() throws Exception {
				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";

				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");

				var peticion = post("http", "localhost", port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

			}

			@Test
			@DisplayName("Debe Asignar Rol Normal")
			public void testGuardarUsuarioDebeAsignarRolNormal() throws Exception {
				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";

				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");

				var peticion = post("http", "localhost", port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Usuario usuario1 = usuarioRepository.findByEmail(email);
				assertThat(usuario1.getRolAsignado() == "NORMAL");
			}

			@Test
			@DisplayName("Debe Cifrar Correctamente La Contrasena")
			public void testGuardarUsuarioDebeCifrarCorrectamenteLaContrasena() throws Exception {
				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";
				String password = "password1";

				Usuario usuario = new Usuario(null, username, password, "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				var peticion = post("http", "localhost", port, "/usuarios/", usuario);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Usuario usuario1 = usuarioRepository.findByEmail(email);

				assertThat(encoder2.matches(password, usuario1.getPassword()));
			}

		}


		@Nested
		@DisplayName("Obtener Usuario")
		public class obtenerUsuario {

			@Test
			@DisplayName("Debe Devolver Usuario Correcto Para Nombre De Usuario Valido")
			public void testObtenerUsuarioDebeDevolverUsuarioCorrectoParaNombreDeUsuarioValido() {

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
					assertThat(token).isNotBlank();

					String username = "usuaOO_" + UUID.randomUUID().toString();
					String email = UUID.randomUUID().toString() + "@dominio.com";

					Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
					usuarioRepository.save(usuario);

					HttpHeaders headers2 = new HttpHeaders();
					headers2.setContentType(MediaType.APPLICATION_JSON);
					headers2.setBearerAuth(token);
					HttpEntity<Optional<Usuario>> request2 = new HttpEntity<>(null, headers2);
					ResponseEntity<Usuario> response2 = restTemplate.exchange("http://localhost:" + port + "/usuarios/" +  username,
							HttpMethod.GET,
							request2,
							Usuario.class
					);

					// Verificar que la respuesta tiene un código de estado OK (200)
					assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

					// Verificar que el usuario devuelto coincide con el usuario creado
					Usuario usuarioDevuelto = response2.getBody();
					assertThat(usuarioDevuelto.getId()).isEqualTo(usuario.getId());
					assertThat(usuarioDevuelto.getUsername()).isEqualTo(usuario.getUsername());
					assertThat(usuarioDevuelto.getEmail()).isEqualTo(usuario.getEmail());
					assertThat(usuarioDevuelto.getNombre()).isEqualTo(usuario.getNombre());
					assertThat(usuarioDevuelto.getApellido()).isEqualTo(usuario.getApellido());
					assertThat(usuarioDevuelto.getTelefono()).isEqualTo(usuario.getTelefono());



			}


			@Test
			@DisplayName("Debe Devolver Null Si Se Proporciona Nombre De Usuario No Valido")
			public void testObtenerUsuarioDebeDevolverNullSiSeProporcionaNombreDeUsuarioNoValido() throws Exception {
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
				assertThat(token).isNotBlank();

				String username = "usuaOO_" + UUID.randomUUID().toString();
				String email = UUID.randomUUID().toString() + "@dominio.com";
				String invalidUsername = "invalid";
				Usuario usuario = new Usuario(null, username, "password1", "Nombre1", "valderramas", email, "1234567890", true, "NORMAL");
				usuarioRepository.save(usuario);


				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				headers2.setBearerAuth(token);
				HttpEntity<Optional<Usuario>> request2 = new HttpEntity<>(null, headers2);
				ResponseEntity<Usuario> response2 = restTemplate.exchange("http://localhost:" + port + "/usuarios/" +  invalidUsername,
						HttpMethod.GET,
						request2,
						Usuario.class
				);

				// Verificar que la respuesta tiene un código de estado NOT FOUND (404)
				assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);



			}


		}




	}


}//TEST
