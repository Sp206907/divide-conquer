package algo;

import java.io.PrintStream;

public final class CsvPrinter {
    private final PrintStream out;

    public CsvPrinter(PrintStream out) {
        this.out = out;
    }

    public void header(String... columns) {
        printRow(columns);
    }

    public void row(Object... values) {
        String[] cols = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cols[i] = String.valueOf(values[i]);
        }
        printRow(cols);
    }

    private void printRow(String... columns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escape(columns[i]));
        }
        out.println(sb);
    }

    private String escape(String s) {
        if (s == null) return "";
        boolean needQuotes = s.contains(",") || s.contains("\n") || s.contains("\"");
        String r = s.replace("\"", "\"\"");
        return needQuotes ? '"' + r + '"' : r;
    }
}


