package net.comsoria.engine.loaders;

import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    private Row[] rows;

    public CSVLoader(String text) {
        this.load(text.split("\n"));
    }

    public CSVLoader(String[] lines) {
        this.load(lines);
    }

    public void load(String[] lines) {
        List<Row> rows = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().equals("")) {
                rows.add(new Row(line.replace("\"", "").split(",")));
            }
        }

        this.rows = new Row[rows.size()];
        for (int i = 0; i < rows.size(); i++)
            this.rows[i] = rows.get(i);
    }

    public String get(int x, int y) {
        return rows[y].getPart(x);
    }

    public Row getRow(int y) {
        return rows[y];
    }

    public int rows() {
        return rows.length;
    }

    public static class Row {
        private final String[] parts;

        public Row(String[] parts) {
            this.parts = parts;
        }

        public String getPart(int id) {
            return parts[id].trim();
        }

        @Override
        public String toString() {
            String text = "";
            for (String string : this.parts) {
                text += "\"" + string + "\",";
            }
            return text.replaceAll(",$", "");
        }
    }

    @Override public String toString() {
        String text = "";
        for (Row row : this.rows) {
            text += row.toString() + "\n";
        }
        return text.trim();
    }
}
