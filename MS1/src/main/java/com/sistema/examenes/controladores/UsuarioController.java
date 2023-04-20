package com.sistema.examenes.controladores;

import com.sistema.examenes.DTO.UsuarioDTO;
import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
 @CrossOrigin("*")
public class UsuarioController {

    private final BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;





    @PostMapping("/usuarios")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> guardarUsuario(@RequestBody Usuario usuario, UriComponentsBuilder uriBuilder) throws Exception {

        if (usuarioService.obtenerUsuario(usuario.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hay un usuario con el mismo correo electrónico en el sistema");
        }
        usuario.setRolAsignado("NORMAL");
        usuario.setPassword(encoder2.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return ResponseEntity.created(uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri()).build();
    }


    @GetMapping("/usuarios")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code= HttpStatus.OK)
    public List<UsuarioDTO> obtenerTodosLosUsuarios(UriComponentsBuilder uriBuilder){
        var usuarios = usuarioService.obtenerUsuarios();
        Function<Usuario, UsuarioDTO> mapper = (p ->
                UsuarioDTO.fromProducto(p,
                         null));
        return usuarios.stream()
                .map(mapper)
                .toList();
    }


    @GetMapping("/usuarios/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code= HttpStatus.OK)
    public UsuarioDTO obtenerUsuario(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(!usuario.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        Usuario user = usuario.get();
        return UsuarioDTO.fromProducto(user,null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return ex.getMessage();
    }


    @PutMapping("/usuarios/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(!optionalUsuario.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuario.setId(id);
        usuarioService.modificarUsuario(usuario);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Boolean> eliminarUsuario(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }



    public static Function<Long, URI> productoUriBuilder(UriComponents uriBuilder) {

        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/usuarios")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }












}
