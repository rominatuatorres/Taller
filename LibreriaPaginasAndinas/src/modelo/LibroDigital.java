package modelo;

public class LibroDigital extends Producto {

    private final String autor;
    private final FormatoDigital formato;

    public LibroDigital(String codigo, String nombre, double precio, String autor, FormatoDigital formato) {
        super(codigo, nombre, precio);
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor es obligatorio.");
        }
        if (formato == null) {
            throw new IllegalArgumentException("Debe elegir un formato (PDF o EPUB).");
        }
        this.autor = autor.trim();
        this.formato = formato;
    }

    public String getAutor() {
        return autor;
    }

    public FormatoDigital getFormato() {
        return formato;
    }

    /** Los libros digitales nunca se acaban: siempre hay stock. */
    @Override
    public boolean hayStock(int cantidad) {
        return true;
    }

    /** No hay nada que descontar: es el mismo archivo para cada cliente. */
    @Override
    public void descontarStock(int cantidad) {
    }

    @Override
    public double getTasaIva() {
        return 0.0;
    }

    @Override
    public boolean aplicaDescuentoSocio() {
        return true;
    }

    @Override
    public String getTipo() {
        return "Libro digital";
    }

    @Override
    public String getDetalle() {
        return "Autor: " + autor + " | Formato: " + formato;
    }

    @Override
    public String getStockTexto() {
        return "Ilimitado";
    }
}
