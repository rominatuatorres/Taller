package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;


public final class Dinero {

    private Dinero() {
    }

    public static double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static String formato(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }
}
