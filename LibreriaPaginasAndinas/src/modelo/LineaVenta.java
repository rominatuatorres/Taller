package modelo;


public class LineaVenta {

    private final Producto producto;
    private int cantidad;
    private final double porcentajeDescuento;

    public LineaVenta(Producto producto, int cantidad, double porcentajeDescuento) {
        if (producto == null) {
            throw new IllegalArgumentException("Debe elegir un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void sumarCantidad(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
        }
        cantidad += unidades;
    }


    public double getSubtotal() {
        return Dinero.redondear(producto.getPrecio() * cantidad);
    }


    public double getDescuento() {
        if (!producto.aplicaDescuentoSocio()) {
            return 0.0;
        }
        return Dinero.redondear(getSubtotal() * porcentajeDescuento);
    }


    public double getIva() {
        return Dinero.redondear((getSubtotal() - getDescuento()) * producto.getTasaIva());
    }

    public double getTotal() {
        return Dinero.redondear(getSubtotal() - getDescuento() + getIva());
    }
}
