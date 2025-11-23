package com.example.cloudy.repository;

import java.util.List;
import java.util.Optional;

public interface iRepository<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}