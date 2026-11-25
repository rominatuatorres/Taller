package vista;

import modelo.Libreria;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(Libreria libreria) {
        super("Librería Páginas Andinas");

        PanelProductos panelProductos = new PanelProductos(libreria);
        PanelVentas panelVentas = new PanelVentas(libreria);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Productos", panelProductos);
        pestanas.addTab("Nueva venta", panelVentas);
        // Al cambiar de pestaña se actualizan los datos (productos nuevos, stock).
        pestanas.addChangeListener(e -> {
            panelProductos.actualizar();
            panelVentas.actualizar();
        });

        add(pestanas);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 740);
        setLocationRelativeTo(null);
    }
}
