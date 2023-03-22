package com.sistema.examenes.controladores;

import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        usuario.setRolAsignado("NORMAL");
        usuario.setPassword(encoder2.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }



    //MICROSERVICIO DE NOTIFICACIONES
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
        //microservicio notificaciones
        String subject = "Nueva contraseña generada";
        String message = "Su nueva contraseña es: " + newPassword;
        /*
        MICROSERVICIO DE NOTIFICACIONES

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("silosabia3@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
        */

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
    @GetMapping("/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username){
        return usuarioService.obtenerUsuario(username);
    }
*/
    @GetMapping("/{username}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("username") String username) {
        try {
            Usuario usuario = usuarioService.obtenerUsuario(username);
            if (usuario == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }








}
