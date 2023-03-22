package com.sistema.examenes.controladores;

import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.modelo.JwtRequest;
import com.sistema.examenes.modelo.JwtResponse;
import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios.impl.UserDetailsServiceImpl;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

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

    @PostMapping("/generate-token")
    public ResponseEntity<?> generarToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        Usuario usuario = usuarioService.obtenerUsuario(jwtRequest.getUsername());

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
/*
        if (!encoder2.matches(jwtRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
        */

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), usuario.getPassword()));
        } catch (DisabledException exception) {
            throw new Exception("USUARIO DESHABILITADO " + exception.getMessage());
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales invalidas autenticadas" );
        }

        UserDetails userDetails =  this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));

    }


    private void autenticar(String username,String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException exception){
            throw  new Exception("USUARIO DESHABILITADO " + exception.getMessage());
        }catch (BadCredentialsException e){
            throw  new Exception("Credenciales invalidas " + e.getMessage());
        }
    }


    @GetMapping("user/{id}")
    public ResponseEntity<Optional<Usuario>> obtenerUsuarioPorId(@PathVariable Long id){
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }



    @PutMapping("user/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(!optionalUsuario.isPresent()){
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        usuarioService.modificarUsuario(usuario);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/user/actual")
    public Usuario obtenerUsuarioActual(Principal principal){
        return (Usuario) this.userDetailsService.loadUserByUsername(principal.getName());
    }

    @GetMapping("/user/todos")
    public List<Usuario> obtenerUsuarios() {

        return usuarioService.obtenerUsuarios();

    }


    @DeleteMapping("/user/eliminarUsuario/{id}")
    public ResponseEntity<Boolean> eliminarUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            if (usuario.isPresent()) {
                usuarioRepository.deleteById(id);
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping("/user/perfil/cambiarClave")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null){
            return ResponseEntity.badRequest().body("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(currentPassword, usuario.getPassword())){
            return ResponseEntity.badRequest().body("Incorrect current password");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);

        usuario.setPassword(encodedPassword);
        usuarioService.guardarClave(usuario);
        return ResponseEntity.ok("Password changed succesfully");
    }





    private void sendEmailWithPassword(String email, String password) {
        // Implement your logic to send an email with the password to the user
    }



    @GetMapping("/user/perfil")
    public String  getUserDetails(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("emailId");
        if (user != null) {
            return "Welcome, " + user.getEmail();
        } else {
            return "Please log in first.";
        }
    }
}
