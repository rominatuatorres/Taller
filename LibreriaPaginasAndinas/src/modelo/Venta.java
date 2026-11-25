package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Venta {

    private final Cliente cliente;
    private final ArrayList<LineaVenta> lineas = new ArrayList<>();
    private boolean confirmada = false;

    public Venta(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe elegir un cliente.");
        }
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<LineaVenta> getLineas() {
        return Collections.unmodifiableList(lineas);
    }


    public void agregarProducto(Producto producto, int cantidad) {
        if (confirmada) {
            throw new IllegalStateException("Esta venta ya fue finalizada.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("Debe elegir un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
        }
        LineaVenta existente = buscarLinea(producto);
        int cantidadTotal = cantidad + (existente == null ? 0 : existente.getCantidad());
        if (!producto.hayStock(cantidadTotal)) {
            throw new IllegalArgumentException("No hay stock suficiente de '" + producto.getNombre()
                    + "'. Disponible: " + producto.getStockTexto() + ".");
        }
        if (existente == null) {
            lineas.add(new LineaVenta(producto, cantidad, cliente.getPorcentajeDescuento()));
        } else {
            existente.sumarCantidad(cantidad);
        }
    }

    /** Finaliza la venta y descuenta el stock de cada producto. */
    public void confirmar() {
        if (confirmada) {
            throw new IllegalStateException("Esta venta ya fue finalizada.");
        }
        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("La venta no tiene productos.");
        }
        for (LineaVenta linea : lineas) {
            if (!linea.getProducto().hayStock(linea.getCantidad())) {
                throw new IllegalArgumentException("No hay stock suficiente de '"
                        + linea.getProducto().getNombre() + "'. Disponible: "
                        + linea.getProducto().getStockTexto() + ".");
            }
        }
        for (LineaVenta linea : lineas) {
            linea.getProducto().descontarStock(linea.getCantidad());
        }
        confirmada = true;
    }

    public double getSubtotal() {
        double suma = 0;
        for (LineaVenta linea : lineas) {
            suma += linea.getSubtotal();
        }
        return Dinero.redondear(suma);
    }

    public double getDescuentoTotal() {
        double suma = 0;
        for (LineaVenta linea : lineas) {
            suma += linea.getDescuento();
        }
        return Dinero.redondear(suma);
    }

    public double getIvaTotal() {
        double suma = 0;
        for (LineaVenta linea : lineas) {
            suma += linea.getIva();
        }
        return Dinero.redondear(suma);
    }

    public double getTotal() {
        double suma = 0;
        for (LineaVenta linea : lineas) {
            suma += linea.getTotal();
        }
        return Dinero.redondear(suma);
    }

    /** Texto con el detalle completo de la venta. */
    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cliente: ").append(cliente).append("\n");
        sb.append("------------------------------------------------------------\n");
        for (LineaVenta linea : lineas) {
            sb.append(linea.getCantidad()).append(" x ").append(linea.getProducto().getNombre())
                    .append("  (").append(Dinero.formato(linea.getProducto().getPrecio())).append(" c/u)\n");
            sb.append("    Subtotal: ").append(Dinero.formato(linea.getSubtotal()))
                    .append(" | Descuento: -").append(Dinero.formato(linea.getDescuento()))
                    .append(" | IVA: ").append(Dinero.formato(linea.getIva()))
                    .append(" | Total: ").append(Dinero.formato(linea.getTotal())).append("\n");
        }
        sb.append("------------------------------------------------------------\n");
        sb.append("Subtotal:         ").append(Dinero.formato(getSubtotal())).append("\n");
        sb.append("Descuento socio: -").append(Dinero.formato(getDescuentoTotal())).append("\n");
        sb.append("IVA:              ").append(Dinero.formato(getIvaTotal())).append("\n");
        sb.append("TOTAL A PAGAR:    ").append(Dinero.formato(getTotal())).append("\n");
        return sb.toString();
    }

    private LineaVenta buscarLinea(Producto producto) {
        for (LineaVenta linea : lineas) {
            if (linea.getProducto() == producto) {
                return linea;
            }
        }
        return null;
    }
}
