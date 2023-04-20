package com.sistema.examenes.controladores;

import com.sistema.examenes.DTO.UsuarioDTO;
import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.modelo.JwtRequest;
import com.sistema.examenes.modelo.JwtResponse;
import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;
import java.util.function.Function;

@RestController
@CrossOrigin("*")
public class AuthenticationController {
    BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public AuthenticationController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> generarToken(@RequestBody JwtRequest jwtRequest, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        Usuario usuario = new Usuario();
        if(jwtRequest.getEmail()!=null){
             usuario = usuarioRepository.findByEmail(jwtRequest.getEmail());
             if(usuario == null){
                 throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas");
             }
             jwtRequest.setUsername(usuario.getUsername());
        }else{
             usuario = usuarioService.obtenerUsuario(jwtRequest.getUsername());
            if( usuario  == null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas");
            }
        }

         if(!encoder2.matches(jwtRequest.getPassword(), usuario.getPassword()) || usuario == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getUsername(),usuario.getPassword()));
        UserDetails userDetails =  this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        URI location = UriComponentsBuilder.fromPath("/generate-token").path("/" + usuario.getId()).build().toUri();
        return ResponseEntity.created(location).body(new JwtResponse(token));

    }

    @PostMapping("/passwordreset")
    public ResponseEntity<?> requestPassword(@RequestBody Map<String, String> emailBody, UriComponentsBuilder uriBuilder) {
        String email = emailBody.get("email");
        Usuario usuario = usuarioService.obtenerEmail(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        String newPassword = generateRandomPassword();
        String cipherPassword = encoder2.encode(newPassword);
        usuario.setPassword(cipherPassword);
        usuarioService.guardarClave(usuario);
        usuarioRepository.save(usuario);
        /*
        MICROSERVICIO DE NOTIFICACIONES
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("silosabia3@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);

        // Envío de correo electrónico con la nueva contraseña
        String subject = "Nueva contraseña generada";
        String message = "Su nueva contraseña es: " + newPassword;
        */

        // Devolución de la URI del usuario actualizado
        return ResponseEntity.ok("Contraseña modificada correctamente");
    }



    private String generateRandomPassword() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }




    public static Function<Long, URI> productoUriBuilder(UriComponents uriBuilder) {
        ;
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/usuarios")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }








/*
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Optional<Usuario>> obtenerUsuarioPorId(@PathVariable Long id, UriComponentsBuilder uriComponentsBuilder){
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/usuario/actual")
    public Usuario obtenerUsuarioActual(Principal principal){
        return (Usuario) this.userDetailsService.loadUserByUsername(principal.getName());
    }

    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {

        return usuarioService.obtenerUsuarios();

    }
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request, UriComponentsBuilder uriBuilder) {
        String email = request.get("email");
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect current password");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);

        usuario.setPassword(encodedPassword);
        usuarioService.guardarClave(usuario);

        URI location = uriBuilder.path("/passwordreset").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.ok("Contraseña modificada correctamente");

    }

@PostMapping
	public ResponseEntity<?> aniadirProducto(@RequestBody ProductoDTO producto, UriComponentsBuilder uriBuilder) {
		Producto prod = producto.producto();
		Long id = servicio.aniadirProducto(prod);
		return ResponseEntity.created(
				productoUriBuilder(uriBuilder.build()).apply(id))
				.build();
	}
 */

}
