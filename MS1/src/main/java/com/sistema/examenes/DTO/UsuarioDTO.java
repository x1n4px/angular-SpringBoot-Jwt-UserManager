package com.sistema.examenes.DTO;

import com.sistema.examenes.modelo.Usuario;
import lombok.*;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
      private String nombre;
    private String apellido1;
    private String apellido2;
     private String email;




    public static UsuarioDTO fromProducto(Usuario usuario,
                                          URI productoUriBuilder) {
        var dto = new UsuarioDTO();
        dto.setNombre(usuario.getNombre());
         dto.setId(usuario.getId());
         dto.setApellido1(usuario.getApellido1());
        dto.setApellido2(usuario.getApellido2());

        dto.setEmail(usuario.getEmail());
        return dto;
    }

    public Usuario usuario() {
        var prod = new Usuario();
        prod.setNombre(nombre);
        prod.setId(id);
        prod.setEmail(email);
        prod.setApellido1(apellido1);
        prod.setApellido2(apellido2);

        return prod;
    }



}
