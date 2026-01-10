import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Small UI helpers (default package).
 *
 * - Global font scaling (for the whole app)
 * - Currency + number formatting renderers for JTable
 */
public final class UiUtil {
    private UiUtil() {}

    private static final DecimalFormat MONEY_FMT = buildMoneyFmt();
    private static final DecimalFormat NUMBER_FMT = buildNumberFmt();

    private static DecimalFormat buildMoneyFmt() {
        DecimalFormatSymbols sym = new DecimalFormatSymbols();
        sym.setGroupingSeparator(',');
        sym.setDecimalSeparator('.');
        sym.setCurrencySymbol("\u09F3");
        return new DecimalFormat("\u00A4#,##0.00", sym);
    }

    private static DecimalFormat buildNumberFmt() {
        DecimalFormatSymbols sym = new DecimalFormatSymbols();
        sym.setGroupingSeparator(',');
        sym.setDecimalSeparator('.');
        return new DecimalFormat("#,##0.00", sym);
    }

    public static String money(double v) {
        synchronized (MONEY_FMT) {
            return MONEY_FMT.format(v);
        }
    }

    public static String num2(double v) {
        synchronized (NUMBER_FMT) {
            return NUMBER_FMT.format(v);
        }
    }

    /** Increase default UI fonts across the entire app. */
    public static void setGlobalFontSize(float size) {
        UIDefaults defaults = UIManager.getDefaults();
        for (Object key : defaults.keySet()) {
            Object val = defaults.get(key);
            if (val instanceof FontUIResource fr) {
                FontUIResource n = new FontUIResource(fr.getFamily(), fr.getStyle(), Math.round(size));
                defaults.put(key, n);
            }
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setFont(table.getFont().deriveFont(16f));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        }
    }

    public static void applyCurrencyRenderer(JTable table, int... modelCols) {
        DefaultTableCellRenderer r = new CurrencyCellRenderer();
        for (int c : modelCols) {
            if (c >= 0 && c < table.getColumnCount()) {
                table.getColumnModel().getColumn(c).setCellRenderer(r);
            }
        }
    }

    public static void applyNumberRenderer(JTable table, int... modelCols) {
        DefaultTableCellRenderer r = new NumberCellRenderer();
        for (int c : modelCols) {
            if (c >= 0 && c < table.getColumnCount()) {
                table.getColumnModel().getColumn(c).setCellRenderer(r);
            }
        }
    }

    private static double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static final class CurrencyCellRenderer extends DefaultTableCellRenderer {
        CurrencyCellRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value) {
            if (value == null) {
                setText("");
                return;
            }
            setText(UiUtil.money(toDouble(value)));
        }
    }

    private static final class NumberCellRenderer extends DefaultTableCellRenderer {
        NumberCellRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value) {
            if (value == null) {
                setText("");
                return;
            }
            setText(UiUtil.num2(toDouble(value)));
        }
    }
}
