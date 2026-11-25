package vista;

import modelo.Dinero;
import modelo.FormatoDigital;
import modelo.Libreria;
import modelo.Producto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;


public class PanelProductos extends JPanel {

    private static final String LIBRO_PAPEL = "Libro de papel";
    private static final String LIBRO_DIGITAL = "Libro digital";
    private static final String PAPELERIA = "Papelería";

    private final Libreria libreria;

    private final JComboBox<String> comboTipo =
            new JComboBox<>(new String[]{LIBRO_PAPEL, LIBRO_DIGITAL, PAPELERIA});
    private final JTextField txtCodigo = new JTextField(14);
    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtPrecio = new JTextField(14);
    private final JLabel lblAutorMarca = new JLabel("Autor:");
    private final JTextField txtAutorMarca = new JTextField(14);
    private final JLabel lblStockFormato = new JLabel("Stock (cantidad en bodega):");
    private final JTextField txtStock = new JTextField(14);
    private final JComboBox<FormatoDigital> comboFormato = new JComboBox<>(FormatoDigital.values());
    private final CardLayout cartas = new CardLayout();
    private final JPanel panelStockFormato = new JPanel(cartas);

    private final JTextField txtBuscar = new JTextField(20);
    private final JLabel lblResultado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Código", "Tipo", "Nombre", "Precio", "Detalle", "Stock"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public PanelProductos(Libreria libreria) {
        this.libreria = libreria;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelRegistro(), BorderLayout.NORTH);
        add(crearPanelLista(), BorderLayout.CENTER);

        cambiarTipo();
        buscar();
    }

    // ---------- Construcción de la ventana ----------

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar un producto nuevo"));

        panelStockFormato.add(txtStock, "stock");
        panelStockFormato.add(comboFormato, "formato");

        agregarCampo(panel, new JLabel("Tipo de producto:"), comboTipo, 0, 0);
        agregarCampo(panel, new JLabel("Código:"), txtCodigo, 0, 1);
        agregarCampo(panel, new JLabel("Nombre:"), txtNombre, 1, 0);
        agregarCampo(panel, new JLabel("Precio ($):"), txtPrecio, 1, 1);
        agregarCampo(panel, lblAutorMarca, txtAutorMarca, 2, 0);
        agregarCampo(panel, lblStockFormato, panelStockFormato, 2, 1);

        JButton btnRegistrar = new JButton("Registrar producto");
        btnRegistrar.setFont(btnRegistrar.getFont().deriveFont(Font.BOLD, 18f));
        btnRegistrar.setPreferredSize(new Dimension(260, 50));
        btnRegistrar.addActionListener(e -> registrar());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 5, 8, 5);
        panel.add(btnRegistrar, gbc);

        comboTipo.addActionListener(e -> cambiarTipo());
        return panel;
    }

    private void agregarCampo(JPanel panel, JLabel etiqueta, java.awt.Component campo, int fila, int columna) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = columna * 2;
        panel.add(etiqueta, gbc);

        gbc.gridx = columna * 2 + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lista de productos"));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(btnBuscar.getFont().deriveFont(Font.BOLD, 16f));
        btnBuscar.setPreferredSize(new Dimension(140, 42));
        btnBuscar.addActionListener(e -> buscar());

        JButton btnVerTodos = new JButton("Ver todos");
        btnVerTodos.setFont(btnVerTodos.getFont().deriveFont(Font.BOLD, 16f));
        btnVerTodos.setPreferredSize(new Dimension(140, 42));
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            buscar();
        });

        txtBuscar.addActionListener(e -> buscar());

        JPanel barra = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));
        barra.add(new JLabel("Buscar por nombre:"));
        barra.add(txtBuscar);
        barra.add(btnBuscar);
        barra.add(btnVerTodos);
        barra.add(lblResultado);

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(240);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(300);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);

        panel.add(barra, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // ---------- Acciones ----------

    /** Muestra u oculta los campos según el tipo de producto elegido. */
    private void cambiarTipo() {
        String tipo = (String) comboTipo.getSelectedItem();
        if (PAPELERIA.equals(tipo)) {
            lblAutorMarca.setText("Marca:");
        } else {
            lblAutorMarca.setText("Autor:");
        }
        if (LIBRO_DIGITAL.equals(tipo)) {
            lblStockFormato.setText("Formato:");
            cartas.show(panelStockFormato, "formato");
        } else {
            lblStockFormato.setText("Stock (cantidad en bodega):");
            cartas.show(panelStockFormato, "stock");
        }
    }

    private void registrar() {
        try {
            String tipo = (String) comboTipo.getSelectedItem();
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            double precio = leerPrecio();
            String autorMarca = txtAutorMarca.getText();

            if (LIBRO_PAPEL.equals(tipo)) {
                libreria.registrarLibroPapel(codigo, nombre, precio, autorMarca, leerStock());
            } else if (LIBRO_DIGITAL.equals(tipo)) {
                libreria.registrarLibroDigital(codigo, nombre, precio, autorMarca,
                        (FormatoDigital) comboFormato.getSelectedItem());
            } else {
                libreria.registrarPapeleria(codigo, nombre, precio, autorMarca, leerStock());
            }

            limpiarFormulario();
            txtBuscar.setText("");
            buscar();
            JOptionPane.showMessageDialog(this, "El producto se registró correctamente.",
                    "Producto registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Revise los datos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private double leerPrecio() {
        try {
            return Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser un número, por ejemplo 12.50.");
        }
    }

    private int leerStock() {
        try {
            return Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El stock debe ser un número entero, por ejemplo 10.");
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtAutorMarca.setText("");
        txtStock.setText("");
        txtCodigo.requestFocusInWindow();
    }

    private void buscar() {
        mostrar(libreria.buscarPorNombre(txtBuscar.getText()));
    }

    private void mostrar(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getCodigo(), p.getTipo(), p.getNombre(), Dinero.formato(p.getPrecio()),
                    p.getDetalle(), p.getStockTexto()
            });
        }
        if (productos.isEmpty()) {
            lblResultado.setText("No se encontraron productos con ese nombre.");
        } else {
            lblResultado.setText("Productos encontrados: " + productos.size());
        }
    }

    /** Vuelve a cargar la tabla (por ejemplo, después de una venta cambia el stock). */
    public void actualizar() {
        buscar();
    }
}
