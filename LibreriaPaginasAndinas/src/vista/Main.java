package vista;

import modelo.Libreria;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si falla, se usa el aspecto por defecto de Swing.
            }
            letraGrande();
            textosEnEspanol();
            new VentanaPrincipal(new Libreria()).setVisible(true);
        });
    }


    private static void letraGrande() {
        for (Object clave : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(clave) instanceof FontUIResource) {
                UIManager.put(clave, new FontUIResource("SansSerif", Font.PLAIN, 16));
            }
        }
    }

    private static void textosEnEspanol() {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
    }
}
