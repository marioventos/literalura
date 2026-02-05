package com.aluracursos.literalura.model;

public enum Idioma {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt"),
    DE("de"),
    IT("it");

    private String codigo;

    Idioma(String codigo) {
        this.codigo = codigo;
    }

    public static Idioma fromString(String codigo) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.codigo.equalsIgnoreCase(codigo)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma no encontrado");
    }
}
