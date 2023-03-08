package com.sistema.examenes.controladores;

import com.sistema.examenes.modelo.Rol;
import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.modelo.UsuarioRol;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) throws Exception{
        usuario.setPerfil("default.png");
        Set<UsuarioRol> usuarioRoles = new HashSet<>();

        Rol rol = new Rol();
        rol.setRolId(2L);
        rol.setRolNombre("NORMAL");

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuario.setPassword(encoder2.encode(usuario.getPassword()));
        usuarioRoles.add(usuarioRol);
        return usuarioService.guardarUsuario(usuario,usuarioRoles);
    }

    @PostMapping("/requestNewPassword")
    public Usuario requestPassword(@RequestBody Map<String, String> emailBody, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //maybe redundant function (Header implements)
        String email = emailBody.get("email");
        Usuario usuario = usuarioRepository.findByEmail(email);

        String newPassword = generateRandomPassword();
        String cipherPassword = encoder2.encode(newPassword);
        usuario.setPassword(cipherPassword);
        usuarioService.guardarClave(usuario);
        usuarioRepository.save(usuario);

        String subject = "Nueva contraseña generada";
        String message = "Su nueva contraseña es: " + newPassword;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("silosabia3@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);


        return usuario;
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
/*
        // Crear el mensaje de correo electrónico
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Nueva contraseña para su cuenta");
    message.setText("Su nueva contraseña es: " + newPassword);

    // Enviar el mensaje de correo electrónico
    mailSender.send(message);
         */








    @GetMapping("/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username){
        return usuarioService.obtenerUsuario(username);
    }




    @DeleteMapping("/{usuarioId}")
    public void eliminarUsuario(@PathVariable("usuarioId") Long usuarioId){
        usuarioService.eliminarUsuario(usuarioId);
    }





}
