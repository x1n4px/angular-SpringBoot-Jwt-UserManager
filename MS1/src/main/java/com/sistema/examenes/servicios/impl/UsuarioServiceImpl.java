package com.sistema.examenes.servicios.impl;


import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.Exceptions.EntidadNoEncontradaException;
import com.sistema.examenes.servicios.UsuarioService;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Usuario obtenerUsuario(String username) {
        var usuario = usuarioRepository.findByUsername(username);

        return usuario;
    }

    @Override
    public Usuario obtenerEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }




    public Usuario guardarClave(Usuario usuario){return usuarioRepository.save(usuario);}

    @Override
    public List<Usuario> obtenerUsuarios(){

        return usuarioRepository.findAll();


    }

    public void modificarUsuario(Usuario usuario){
            usuarioRepository.save(usuario);
    }




}