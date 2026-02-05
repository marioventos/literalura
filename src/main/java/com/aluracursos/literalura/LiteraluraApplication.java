package com.aluracursos.literalura;

import com.aluracursos.literalura.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private Principal principal;
    
    public LiteraluraApplication(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void run(String... args) {
        principal.muestraMenu();
    }
    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }
}