package bda;

import java.util.List;
import java.util.Map;

public class BDeResult {

    private final List<Map<String, Object>> rows;
    private int pos = -1;

    public BDeResult(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public void init() { pos = -1; }

    public boolean next() {
        pos++;
        return pos < rows.size();
    }

    public Object getObject(String col) {
        return rows.get(pos).get(col);
    }

    public String getString(String col) {
        Object v = getObject(col);
        return v == null ? null : v.toString();
    }

    public int getInt(String col) {
        Object v = getObject(col);
        if (v == null) return 0;
        if (v instanceof Number) return ((Number) v).intValue();
        return Integer.parseInt(v.toString());
    }

    // score Lucene (pour plus tard quand on fera WITH)
    public Float getScore() {
        Object v = rows.get(pos).get("_score");
        return v == null ? null : (Float) v;
    }
}
