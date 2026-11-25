package modelo;


public abstract class Producto {

    private final String codigo;
    private final String nombre;
    private final double precio;

    protected Producto(String codigo, String nombre, double precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (Double.isNaN(precio) || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    /** Indica si se pueden vender 'cantidad' unidades con el stock actual. */
    public abstract boolean hayStock(int cantidad);

    /** Resta del stock las unidades vendidas. */
    public abstract void descontarStock(int cantidad);

    /** Tasa de IVA del producto (0.0 = sin IVA, 0.15 = 15 %). */
    public abstract double getTasaIva();

    /** Indica si el descuento de socio se aplica a este producto. */
    public abstract boolean aplicaDescuentoSocio();

    /** Nombre del tipo de producto, para mostrar en la tabla. */
    public abstract String getTipo();

    /** Datos propios del tipo (autor, formato, marca), para mostrar en la tabla. */
    public abstract String getDetalle();

    /** Stock para mostrar en pantalla. */
    public abstract String getStockTexto();

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
