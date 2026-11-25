package vista;

import modelo.Cliente;
import modelo.Dinero;
import modelo.LineaVenta;
import modelo.Libreria;
import modelo.Producto;
import modelo.Venta;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class PanelVentas extends JPanel {

    private final Libreria libreria;
    private Venta ventaActual = null;

    private final JComboBox<Cliente> comboCliente = new JComboBox<>();
    private final JComboBox<Producto> comboProducto = new JComboBox<>();
    private final JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Precio unitario", "Subtotal", "Descuento", "IVA", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JLabel lblSubtotal = new JLabel();
    private final JLabel lblDescuento = new JLabel();
    private final JLabel lblIva = new JLabel();
    private final JLabel lblTotal = new JLabel();

    public PanelVentas(Libreria libreria) {
        this.libreria = libreria;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Cliente c : libreria.getClientes()) {
            comboCliente.addItem(c);
        }
        comboProducto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                Object texto = valor;
                if (valor instanceof Producto) {
                    Producto p = (Producto) valor;
                    texto = p.getCodigo() + " - " + p.getNombre() + "  (stock: " + p.getStockTexto() + ")";
                }
                return super.getListCellRendererComponent(lista, texto, indice, seleccionado, foco);
            }
        });

        add(crearPanelArmarVenta(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelTotales(), BorderLayout.SOUTH);

        actualizar();
        refrescarTabla();
    }

    // ---------- Construcción de la ventana ----------

    private JPanel crearPanelArmarVenta() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Armar la venta"));

        JButton btnAgregar = new JButton("Agregar a la venta");
        btnAgregar.setFont(btnAgregar.getFont().deriveFont(Font.BOLD, 18f));
        btnAgregar.setPreferredSize(new Dimension(260, 50));
        btnAgregar.addActionListener(e -> agregar());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboProducto, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3;
        panel.add(spinnerCantidad, gbc);

        gbc.gridx = 4;
        panel.add(btnAgregar, gbc);
        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Productos de esta venta"));
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(260);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTotales() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel totales = new JPanel(new GridLayout(4, 1, 0, 3));
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDescuento.setHorizontalAlignment(SwingConstants.RIGHT);
        lblIva.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 24f));
        totales.add(lblSubtotal);
        totales.add(lblDescuento);
        totales.add(lblIva);
        totales.add(lblTotal);

        JButton btnFinalizar = new JButton("Finalizar venta");
        btnFinalizar.setFont(btnFinalizar.getFont().deriveFont(Font.BOLD, 18f));
        btnFinalizar.setPreferredSize(new Dimension(260, 55));
        btnFinalizar.addActionListener(e -> finalizar());

        JButton btnCancelar = new JButton("Cancelar venta");
        btnCancelar.setFont(btnCancelar.getFont().deriveFont(Font.BOLD, 18f));
        btnCancelar.setPreferredSize(new Dimension(260, 55));
        btnCancelar.addActionListener(e -> cancelar());

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.add(btnCancelar);
        botones.add(btnFinalizar);

        panel.add(totales, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Acciones ----------

    private void agregar() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        Producto producto = (Producto) comboProducto.getSelectedItem();
        int cantidad = (Integer) spinnerCantidad.getValue();
        boolean ventaNueva = (ventaActual == null);
        try {
            if (ventaNueva) {
                ventaActual = new Venta(cliente);
            }
            ventaActual.agregarProducto(producto, cantidad);
            comboCliente.setEnabled(false);
            spinnerCantidad.setValue(1);
            refrescarTabla();
        } catch (IllegalArgumentException e) {
            if (ventaNueva) {
                ventaActual = null;
            }
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo agregar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finalizar() {
        if (ventaActual == null) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto antes de finalizar la venta.",
                    "Venta vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ventaActual.confirmar();
            mostrarResumen(ventaActual.getResumen());
            reiniciarVenta();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo finalizar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelar() {
        if (ventaActual == null) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cancelar esta venta?",
                "Cancelar venta", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarVenta();
        }
    }

    private void reiniciarVenta() {
        ventaActual = null;
        comboCliente.setEnabled(true);
        spinnerCantidad.setValue(1);
        actualizar();
        refrescarTabla();
    }

    private void mostrarResumen(String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(640, 320));
        JOptionPane.showMessageDialog(this, scroll, "Venta registrada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        if (ventaActual == null) {
            lblSubtotal.setText("Subtotal: $0.00");
            lblDescuento.setText("Descuento de socio: $0.00");
            lblIva.setText("IVA: $0.00");
            lblTotal.setText("TOTAL A PAGAR: $0.00");
            return;
        }
        for (LineaVenta l : ventaActual.getLineas()) {
            modeloTabla.addRow(new Object[]{
                    l.getProducto().getNombre(), l.getCantidad(),
                    Dinero.formato(l.getProducto().getPrecio()), Dinero.formato(l.getSubtotal()),
                    Dinero.formato(l.getDescuento()), Dinero.formato(l.getIva()),
                    Dinero.formato(l.getTotal())
            });
        }
        lblSubtotal.setText("Subtotal: " + Dinero.formato(ventaActual.getSubtotal()));
        lblDescuento.setText("Descuento de socio: " + Dinero.formato(ventaActual.getDescuentoTotal()));
        lblIva.setText("IVA: " + Dinero.formato(ventaActual.getIvaTotal()));
        lblTotal.setText("TOTAL A PAGAR: " + Dinero.formato(ventaActual.getTotal()));
    }


    public void actualizar() {
        Object seleccionado = comboProducto.getSelectedItem();
        DefaultComboBoxModel<Producto> modelo = new DefaultComboBoxModel<>();
        for (Producto p : libreria.getProductos()) {
            modelo.addElement(p);
        }
        comboProducto.setModel(modelo);
        if (seleccionado != null) {
            comboProducto.setSelectedItem(seleccionado);
        }
    }
}
