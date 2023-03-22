package com.sistema.examenes.servicios.impl;


import com.sistema.examenes.modelo.Usuario;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios.exceptions.EntidadNoEncontradaException;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Usuario obtenerUsuario(String username) {
        var usuario = usuarioRepository.findByUsername(username);
        if(usuario != null){
            return usuario;
        }else{
            throw new EntidadNoEncontradaException();
        }

    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id){return usuarioRepository.findById(id);}

    public Usuario guardarClave(Usuario usuario){return usuarioRepository.save(usuario);}


    @Override
    public void eliminarUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }

    @Override
    public List<Usuario> obtenerUsuarios(){

        return usuarioRepository.findAll();


    }

    public void modificarUsuario(Usuario usuario){
        if(usuarioRepository.existsById(usuario.getId())){
            usuarioRepository.save(usuario);
        }else{
            throw new RuntimeException();
        }
    }




}