package com.sistema.examenes.servicios;

import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.modelo.UsuarioRol;
import com.sistema.examenes.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public interface UsuarioService {

    public Usuario guardarUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws Exception;

    public Usuario guardarClave(Usuario usuario);

    public Usuario obtenerUsuario(String username);
    public Usuario obtenerUsuarioPorEmail(String email);


    public void eliminarUsuario(Long usuarioId);

    public List<Usuario> obtenerUsuarios();


}
