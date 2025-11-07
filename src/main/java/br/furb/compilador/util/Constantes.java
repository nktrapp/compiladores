package br.furb.compilador.util;

import br.furb.compilador.componentes.Constants;

import java.util.HashMap;
import java.util.Map;

public class Constantes {

    public static final char ESPACO = ' ';
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String STRING = "string";
    public static final String BOOL = "bool";


    public static final Map<Integer, String> CLASSES = new HashMap<>() {{
        put(Constants.t_identificador, "identificador");
        put(Constants.t_pr_add, "palavra reservada");
        put(Constants.t_pr_and, "palavra reservada");
        put(Constants.t_pr_begin, "palavra reservada");
        put(Constants.t_pr_bool, "palavra reservada");
        put(Constants.t_pr_count, "palavra reservada");
        put(Constants.t_pr_delete, "palavra reservada");
        put(Constants.t_pr_do, "palavra reservada");
        put(Constants.t_pr_elementOf, "palavra reservada");
        put(Constants.t_pr_else, "palavra reservada");
        put(Constants.t_pr_end, "palavra reservada");
        put(Constants.t_pr_false, "palavra reservada");
        put(Constants.t_pr_float, "palavra reservada");
        put(Constants.t_pr_if, "palavra reservada");
        put(Constants.t_pr_int, "palavra reservada");
        put(Constants.t_pr_list, "palavra reservada");
        put(Constants.t_pr_not, "palavra reservada");
        put(Constants.t_pr_or, "palavra reservada");
        put(Constants.t_pr_print, "palavra reservada");
        put(Constants.t_pr_read, "palavra reservada");
        put(Constants.t_pr_size, "palavra reservada");
        put(Constants.t_pr_string, "palavra reservada");
        put(Constants.t_pr_true, "palavra reservada");
        put(Constants.t_pr_until, "palavra reservada");
        put(Constants.t_cint, "constante_int");
        put(Constants.t_cfloat, "constante_float");
        put(Constants.t_cstring, "constante_string");
        put(Constants.t_TOKEN_31, "símbolo especial");
        put(Constants.t_TOKEN_32, "símbolo especial");
        put(Constants.t_TOKEN_33, "símbolo especial");
        put(Constants.t_TOKEN_34, "símbolo especial");
        put(Constants.t_TOKEN_35, "símbolo especial");
        put(Constants.t_TOKEN_36, "símbolo especial");
        put(Constants.t_TOKEN_37, "símbolo especial");
        put(Constants.t_TOKEN_38, "símbolo especial");
        put(Constants.t_TOKEN_39, "símbolo especial");
        put(Constants.t_TOKEN_40, "símbolo especial");
        put(Constants.t_TOKEN_41, "símbolo especial");
        put(Constants.t_TOKEN_42, "símbolo especial");
    }};


}
