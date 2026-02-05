package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final LibroService servicio;

    public Principal(LibroService servicio) {
        this.servicio = servicio;
    }
    public void muestraMenu() {

        while (true) {
            System.out.println("""
        
             Menu Principal LITERALURA 
             
             1 - Buscar libro por título
             2 - Listar libros registrados
             3 - Listar autores registrados
             4 - Listar autores vivos en un año
             5 - Listar libros por idioma
             0 - Salir
             """);

            System.out.println("Seleccione una opción: ");
            String entrada = teclado.nextLine();

            int opcion;
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido");
                continue;
            }

            switch (opcion) {
                case 1: servicio.buscarLibroPorTitulo();
                break;
                case 2: servicio.listarLibrosRegistrados();
                break;
                case 3: servicio.listarAutoresRegistrados();
                break;
                case 4: servicio.listarAutoresVivos();
                break;
                case 5: servicio.listarLibrosPorIdioma();
                break;
                case 0: System.out.println(" ¡Hasta luego! ");
                return;
                default: System.out.println(" ¡Opción inválida! ");
            }
        }
    }
}
