package bda;

public class QueryWithParser {

    public static class Parts {
        public final String sql;
        public final String text; // null si pas de WITH
        public Parts(String sql, String text) {
            this.sql = sql;
            this.text = text;
        }
        public boolean isMixed() { return text != null; }
    }

    public static Parts parse(String query) {
        String q = query.trim();
        String lower = q.toLowerCase();

        int idx = lower.indexOf(" with ");
        if (idx < 0) {
            return new Parts(q, null);
        }

        String sql = q.substring(0, idx).trim();
        String text = q.substring(idx + " with ".length()).trim();
        return new Parts(sql, text);
    }
}
