package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Libreria {

    private final ArrayList<Producto> productos = new ArrayList<>();
    private final ArrayList<Cliente> clientes = new ArrayList<>();

    public Libreria() {
        cargarDatosDePrueba();
    }

    public void registrarLibroPapel(String codigo, String nombre, double precio, String autor, int stock) {
        agregar(new LibroPapel(codigo, nombre, precio, autor, stock));
    }

    public void registrarLibroDigital(String codigo, String nombre, double precio, String autor,
                                      FormatoDigital formato) {
        agregar(new LibroDigital(codigo, nombre, precio, autor, formato));
    }

    public void registrarPapeleria(String codigo, String nombre, double precio, String marca, int stock) {
        agregar(new Papeleria(codigo, nombre, precio, marca, stock));
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }


    public List<Producto> buscarPorNombre(String texto) {
        String buscado = texto == null ? "" : texto.trim().toLowerCase();
        ArrayList<Producto> encontrados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(buscado)) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    private void agregar(Producto nuevo) {
        for (Producto existente : productos) {
            if (existente.getCodigo().equalsIgnoreCase(nuevo.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un producto con el código '"
                        + nuevo.getCodigo() + "' (" + existente.getNombre() + ").");
            }
        }
        productos.add(nuevo);
    }

    private void cargarDatosDePrueba() {
        registrarLibroPapel("LP-001", "Cien años de soledad", 18.50, "Gabriel García Márquez", 10);
        registrarLibroPapel("LP-002", "Huasipungo", 12.00, "Jorge Icaza", 2);
        registrarLibroDigital("LD-001", "El laberinto de la soledad", 8.00, "Octavio Paz", FormatoDigital.PDF);
        registrarLibroDigital("LD-002", "Don Quijote de la Mancha", 6.50, "Miguel de Cervantes", FormatoDigital.EPUB);
        registrarPapeleria("PA-001", "Cuaderno universitario", 2.50, "Norma", 40);
        registrarPapeleria("PA-002", "Esfero azul", 0.50, "BIC", 100);

        clientes.add(new Cliente("Carlos Mena", true));
        clientes.add(new Cliente("Lucía Andrade", false));
    }
}
