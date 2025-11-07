package br.furb.compilador.util;

public enum OperadorRelacional {

    IGUAL("==", "eq"),
    DIFERENTE("~=", "neq"),
    MAIOR(">", "cgt"),
    MENOR("<", "clt");

    String operador;
    String ilasmCode;

    OperadorRelacional(String value, String ilasmCode) {
        this.operador = value;
        this.ilasmCode = ilasmCode;
    }

    public String getOperador() {
        return operador;
    }

    public String getIlasmCode() {
        return ilasmCode;
    }
}
