import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Small Swing helpers: global font + table tuning + money formatting.
 * Default package (no package statement) to match your project.
 */
public final class UIUtil {
    private UIUtil() {}

    private static final DecimalFormat MONEY;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols();
        sym.setGroupingSeparator(',');
        sym.setDecimalSeparator('.');
        MONEY = new DecimalFormat("#,##0.00", sym);
    }

    public static void setGlobalFontSize(int size) {
        Font base = new Font(Font.SANS_SERIF, Font.PLAIN, size);
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object val = UIManager.get(key);
            if (val instanceof Font) UIManager.put(key, base);
        }
    }

    public static String money(double v) {
        return "\u09F3" + MONEY.format(v);
    }

    public static void tuneTable(JTable table) {
        if (table == null) return;
        table.setRowHeight(28);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        }
    }

    public static void applyMoneyRenderer(JTable table, int... moneyCols) {
        if (table == null || moneyCols == null) return;

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value == null) {
                    setText("");
                    return;
                }
                Double d = null;
                if (value instanceof Number) {
                    d = ((Number) value).doubleValue();
                } else {
                    String s = String.valueOf(value).trim();
                    if (s.isEmpty()) { setText(""); return; }
                    // strip currency sign + commas if user stored as text
                    s = s.replace("\u09F3", "").replace(",", "");
                    try { d = Double.parseDouble(s); } catch (Exception ignored) {}
                }
                setText(d == null ? String.valueOf(value) : money(d));
            }
        };
        r.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int col : moneyCols) {
            if (col >= 0 && col < table.getColumnModel().getColumnCount()) {
                table.getColumnModel().getColumn(col).setCellRenderer(r);
            }
        }
    }
}
