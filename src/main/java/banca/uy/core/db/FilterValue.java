package banca.uy.core.db;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

enum EFilterType {

    // FILTROS DE TIPO TEXTO
    /** Representa los tipos operadores like y ilike tal cual en SQL */
    LIKE, ILIKE,

    // FILTROS DE TIPO NUMERICO O FECHA

    GT, GTE, LT, LTE,

    // FILTROS GENERALES
    EQ, NEQ, IN,
    /** Representa un operador para determinar valores Nulos o No Nulos */
    SPEC
}

public class FilterValue implements Serializable {
    /**
     * Autogenerated value for serialVersionUID
     */
    private static final long serialVersionUID = 6581445480416138488L;
    private String field;
    private String value;
    private EFilterType type;

    public FilterValue() {
    }

    public FilterValue(String field, String value, EFilterType type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EFilterType getType() {
        return this.type;
    }

    public void setType(EFilterType type) {
        this.type = type;
    }

    public FilterValue field(String field) {
        this.field = field;
        return this;
    }

    public FilterValue value(String value) {
        this.value = value;
        return this;
    }

    public FilterValue type(EFilterType type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " field='" + getField() + "'" + ", value='" + getValue() + "'" + ", type='" + getType() + "'"
                + "}";
    }

    /**
     * Ver {@link #loadFilter(String, String)}, este método por detrás ejecuta ese
     * 
     * @param entry
     * @return
     */
    public static Optional<FilterValue> loadFilter(Map.Entry<String, String> entry) {
        return FilterValue.loadFilter(entry.getKey(), entry.getValue());
    }

    /**
     * 
     * Devuelve un {@link Optional}<{@link FilterValue}> con el objeto deserializado
     * a partir de los parámetros pasados al método
     * 
     * @param key
     * @param value
     * @return {@link Optional}<{@link FilterValue}>
     */
    public static Optional<FilterValue> loadFilter(String key, String value) {
        Optional<FilterValue> filterValueOpt = Optional.empty();
        Optional<Map.Entry<String, EFilterType>> filterTypeOpt = FilterValue.getFilterType(key);
        filterValueOpt = filterTypeOpt.flatMap((e) -> {
            if (value == null || value.trim().isEmpty())
                return Optional.empty();
            FilterValue filterValue = new FilterValue(e.getKey(), value, e.getValue());
            return Optional.of(filterValue);
        });

        return filterValueOpt;
    }

    public static Optional<Map.Entry<String, EFilterType>> getFilterType(String key) {
        ArrayList<String> destructuredKey = new ArrayList<String>();
        if (key.indexOf(".", 0) != -1)
            destructuredKey.add(key.substring(0, key.indexOf(".", 0)));
        destructuredKey.add(key.substring(key.indexOf(".", 0) + 1, key.length()));
        if (destructuredKey.size() != 2)
            return Optional.empty();
        EFilterType filterType = null;
        switch (destructuredKey.get(1).toUpperCase()) {
        case "EQ":
        case "EQUALS":
            filterType = EFilterType.EQ;
            break;
        case "NEQ":
        case "NOTEQUALS":
            filterType = EFilterType.NEQ;
            break;
        case "LT":
        case "LESSTHAN":
            filterType = EFilterType.LT;
            break;
        case "LTE":
        case "LESSOREQUALTHAN":
            filterType = EFilterType.LTE;
            break;
        case "GT":
        case "GREATERTHAN":
            filterType = EFilterType.GT;
            break;
        case "GTE":
        case "GREATEROREQUALTHAN":
            filterType = EFilterType.GTE;
            break;
        case "LIKE":
        case "CONTAINS":
            filterType = EFilterType.LIKE;
            break;
        case "ILIKE":
            filterType = EFilterType.ILIKE;
            break;
        case "IN":
            filterType = EFilterType.IN;
            break;
        case "SPEC":
        case "SPECIFIED":
            filterType = EFilterType.SPEC;
            break;
        default:
            return Optional.empty();
        }

        return Optional.of(new AbstractMap.SimpleEntry<String, EFilterType>(destructuredKey.get(0), filterType));
    }

    public Optional<String> toSQL() {
        String template = " :query ";
        switch (this.type) {
        case EQ:
            template = template.replace(":query", this.field + " = '" + this.value + "'");
            break;
        case NEQ:
            template = template.replace(":query", this.field + " != '" + this.value + "'");
            break;
        case GT:
            template = template.replace(":query", this.field + " > " + this.value);
            break;
        case GTE:
            template = template.replace(":query", this.field + " >= " + this.value);
            break;
        case LT:
            template = template.replace(":query", this.field + " < " + this.value);
            break;
        case LTE:
            template = template.replace(":query", this.field + " <= " + this.value);
            break;
        case LIKE:
        case ILIKE:
            template = template.replace(":query", this.field + " like '%" + this.value + "%'");
            break;
        case IN:
            template = template.replace(":query", this.field + " IN (" + this.value + ")");
            break;
        case SPEC:
            if (this.value.equals("true"))
                template = template.replace(":query", this.field + " IS NOT NULL");
            else
                template = template.replace(":query", this.field + " IS NULL");
            break;
        default:
            template = null;
            break;
        }
        return Optional.ofNullable(template);
    }

}