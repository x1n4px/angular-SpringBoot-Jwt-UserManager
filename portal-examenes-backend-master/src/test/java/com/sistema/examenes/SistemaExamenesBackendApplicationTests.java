package com.sistema.examenes;

import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.repositorios.UsuarioRepository;
import io.jsonwebtoken.Claims;
import jdk.jfr.internal.Utils;
import lombok.var;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import javax.crypto.SecretKey;
import java.net.URI;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SistemaExamenesBackendApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private UsuarioRepository usuarioRepository;


	private JwtUtils jwtUtils;

		@Mock
		private SecretKey secretKey;





		private URI uri(String scheme, String host, int port, String ...paths) {
			UriBuilderFactory ubf = new DefaultUriBuilderFactory();
			UriBuilder ub = ubf.builder()
					.scheme(scheme)
					.host(host).port(port);
			for (String path: paths) {
				ub = ub.path(path);
			}
			return ub.build();
		}

		private RequestEntity<Void> get(String scheme, String host, int port, String path) {
			URI uri = uri(scheme, host,port, path);
			var peticion = RequestEntity.get(uri)
					.accept(MediaType.APPLICATION_JSON)
					.build();
			return peticion;
		}

		private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
			URI uri = uri(scheme, host,port, path);
			var peticion = RequestEntity.delete(uri)
					.build();
			return peticion;
		}

		private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
			URI uri = uri(scheme, host,port, path);
			var peticion = RequestEntity.post(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.body(object);
			return peticion;
		}

		private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
			URI uri = uri(scheme, host,port, path);
			var peticion = RequestEntity.put(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.body(object);
			return peticion;
		}







	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

	}


	@Nested
	@DisplayName("Cuando la base de datos tiene información")
	public class BaseDatosLlena{

	}

}
