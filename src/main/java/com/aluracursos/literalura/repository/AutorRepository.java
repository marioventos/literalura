package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    @Query("""
           SELECT a FROM Autor a
           WHERE a.fechaNacimiento <= :fecha
           AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento > :fecha) 
            """)
    List<Autor> autoresVivosEnFecha(@Param("fecha") int fecha);
}
