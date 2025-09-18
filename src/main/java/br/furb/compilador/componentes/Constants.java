package br.furb.compilador.componentes;

public interface Constants extends ScannerConstants, ParserConstants {
    int EPSILON = 0;
    int DOLLAR = 1;

    int t_identificador = 2;
    int t_pr_add = 3;
    int t_pr_and = 4;
    int t_pr_begin = 5;
    int t_pr_bool = 6;
    int t_pr_count = 7;
    int t_pr_delete = 8;
    int t_pr_do = 9;
    int t_pr_elementOf = 10;
    int t_pr_else = 11;
    int t_pr_end = 12;
    int t_pr_false = 13;
    int t_pr_float = 14;
    int t_pr_if = 15;
    int t_pr_int = 16;
    int t_pr_list = 17;
    int t_pr_not = 18;
    int t_pr_or = 19;
    int t_pr_print = 20;
    int t_pr_read = 21;
    int t_pr_size = 22;
    int t_pr_string = 23;
    int t_pr_true = 24;
    int t_pr_until = 25;
    int t_cint = 26;
    int t_cfloat = 27;
    int t_cstring = 28;
    int t_TOKEN_29 = 29; //"+"
    int t_TOKEN_30 = 30; //"-"
    int t_TOKEN_31 = 31; //"*"
    int t_TOKEN_32 = 32; //"/"
    int t_TOKEN_33 = 33; //"=="
    int t_TOKEN_34 = 34; //"~="
    int t_TOKEN_35 = 35; //"<"
    int t_TOKEN_36 = 36; //">"
    int t_TOKEN_37 = 37; //"="
    int t_TOKEN_38 = 38; //"<-"
    int t_TOKEN_39 = 39; //"("
    int t_TOKEN_40 = 40; //")"
    int t_TOKEN_41 = 41; //";"
    int t_TOKEN_42 = 42; //","

}
