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

    public static OperadorRelacional fromOperador(String operador) {
        for (OperadorRelacional op : OperadorRelacional.values()) {
            if (op.getOperador().equals(operador)) {
                return op;
            }
        }
        // É importante lidar com o caso em que o operador não é encontrado
        throw new IllegalArgumentException("Operador relacional desconhecido: " + operador);
    }

    public String getOperador() {
        return operador;
    }

    public String getIlasmCode() {
        return ilasmCode;
    }
}
