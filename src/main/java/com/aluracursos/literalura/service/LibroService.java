package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.RespuestaGutendex;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Idioma;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service

public class LibroService {

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConversorDatos conversor = new ConversorDatos();

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    private final Scanner teclado = new Scanner(System.in);

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro: ");
        String titulo = teclado.nextLine();

        String url = "https://gutendex.com/books/?search="
                + titulo.replace(" ", "%20")
                + "&languages=es,en,fr,pt,de,it";

        String json = consumoAPI.obtenerDatos(url);
        RespuestaGutendex datos = conversor.convertirDatos(json, RespuestaGutendex.class);

        if (datos.results().isEmpty()) {
            System.out.println("Libro no encontrado");
            return;
        }

        datos.results().forEach(datosLibro -> {

            if(libroRepository.existsByTituloIgnoreCase((datosLibro.titulo()))) {
                return;
            }
            datosLibro.autores().forEach(datosAutor -> {

                Autor autor = autorRepository
                        .findByNombreIgnoreCase(datosAutor.nombre())
                        .orElseGet(() ->
                                autorRepository.save(
                                        new Autor(
                                                datosAutor.nombre(),
                                                datosAutor.nacimiento(),
                                                datosAutor.fallecimiento()
                                        )
                                )
                        );
                Idioma idioma = Idioma.fromString(datosLibro.idiomas().get(0));
                Libro libro = new Libro(
                        datosLibro.titulo(),
                        idioma,
                        datosLibro.descargas(),
                        autor
                );

                libroRepository.save(libro);
            });
        });

        datos.results().forEach(libro -> {
            System.out.println("\n Título: " + libro.titulo());
            System.out.println("Descargas: " + libro.descargas());
            System.out.println("Idiomas: " + libro.idiomas());
        });
    }
    public void listarLibrosRegistrados() {
        var libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }
        System.out.println("\n Libros registrados: \n");

        libros.forEach(libro -> {
            System.out.println("Títulos: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getDescargas());
            System.out.println("----------------------------------");
        });
    }
    public void listarAutoresRegistrados() {
        var autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }
        System.out.println("\n Autores registrados: \n");
        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fallecimiento: " + autor.getFechaFallecimiento());
            System.out.println("------------------------------------------");
        });
    }
    public void listarAutoresVivos() {

        System.out.println("Ingrese el año para autores vivos: ");
        int fecha = teclado.nextInt();

        var autores = autorRepository.autoresVivosEnFecha(fecha);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año.");
            return;
        }

        System.out.println("\n Autores vivos en el año " + fecha + ": \n");

        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fallecimiento: " +
                    (autor.getFechaFallecimiento() != null
                    ? autor.getFechaFallecimiento()
                    : "Aún vivo"));
            System.out.println("----------------------------------------");

        });
    }
    public void listarLibrosPorIdioma() {

        System.out.println("Ingrese el idioma (ES, EN, FT, PT, DE, IT): ");
        String idiomaInput = teclado.nextLine().toUpperCase();

        Idioma idioma;
        try {
            idioma = Idioma.valueOf(idiomaInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no válido.");
            return;
        }

        var libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma.");
            return;
        }
        System.out.println("\n Libros en idioma: \n ");

        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Descargas: " + libro.getDescargas());
            System.out.println("----------------------------------");
        });
    }
}
