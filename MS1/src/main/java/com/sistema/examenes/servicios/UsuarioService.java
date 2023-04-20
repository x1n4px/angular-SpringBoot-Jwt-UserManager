package com.sistema.examenes.servicios;

import com.sistema.examenes.modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {


    public Usuario guardarClave(Usuario usuario);

    public Usuario obtenerUsuario(String username);
    public Usuario obtenerEmail(String email);



    public List<Usuario> obtenerUsuarios();


    public void modificarUsuario(Usuario usuario);


 }
