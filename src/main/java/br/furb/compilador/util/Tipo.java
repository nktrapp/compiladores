package br.furb.compilador.util;

public enum Tipo {
    INT64("int", "int64", "Int64"),
    FLOAT64("float", "float64", "Double"),
    BOOL("bool", "bool", "Boolean"),
    STRING("string", "string", "String"),
    LIST("list", "list", "List");

    private String value;
    private String ilasmValue;
    private String ilasmClass;

    Tipo(String value, String ilasmValue, String ilasmClass) {
        this.value = value;
        this.ilasmValue = ilasmValue;
        this.ilasmClass = ilasmClass;
    }

    public String getValue() {
        return value;
    }

    public String getIlasmValue() {
        return ilasmValue;
    }

    public String getIlasmClass() {
        return ilasmClass;
    }
}
