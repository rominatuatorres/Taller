package modelo;

public class Cliente {

    private static final double DESCUENTO_SOCIO = 0.10;

    private final String nombre;
    private final boolean socio;

    public Cliente(String nombre, boolean socio) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        this.nombre = nombre.trim();
        this.socio = socio;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isSocio() {
        return socio;
    }

    /** 0.10 si es socio, 0 si no lo es. */
    public double getPorcentajeDescuento() {
        return socio ? DESCUENTO_SOCIO : 0.0;
    }

    @Override
    public String toString() {
        return socio ? nombre + " (socio)" : nombre;
    }
}
