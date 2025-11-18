package com.example.cloudy.service;

import java.util.List;

public interface iService<T> {
    T guardar(T entidad);
    T buscar(Long id);
    void eliminar(Long id);
    T actualizar(T entidad);
    List<T> buscarTodos();
}
