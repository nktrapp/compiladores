package br.furb.compilador.componentes;

import br.furb.compilador.util.OperadorRelacional;
import br.furb.compilador.util.Tipo;

import java.util.*;

import static br.furb.compilador.util.Constantes.BOOL;

public class Semantico implements Constants {

    private final StringBuilder sb = new StringBuilder();
    private final Stack<Tipo> tipos = new Stack<>();
    private final Stack<OperadorRelacional> operadoresRelacionais = new Stack<>();
    private final List<String> identificadores = new ArrayList<>();
    private final HashMap<String, Tipo> tabelaIdentificadores = new HashMap<>();
    private final Stack<String> rotulos = new Stack<>();
    private int numeroRotulo = 1;

    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 100:
                metodo100(token);
                break;
            case 101:
                metodo101(token);
                break;
            case 102:
                metodo102(token);
                break;
            case 103:
                metodo103(token);
                break;
            case 104:
                metodo104(token);
                break;
            case 105:
                metodo105(token);
                break;
            case 106:
                metodo106(token);
                break;
            case 107:
                metodo107(token);
                break;
            case 108:
                metodo108(token);
                break;
            case 109:
                metodo109(token);
                break;
            case 110:
                metodo110(token);
                break;
            case 111:
                metodo111(token);
                break;
            case 112:
                metodo112(token);
                break;
            case 113:
                metodo113(token);
                break;
            case 114:
                metodo114(token);
                break;
            case 115:
                metodo115(token);
                break;
            case 116:
                metodo116(token);
                break;
            case 117:
                metodo117(token);
                break;
            case 118:
                metodo118(token);
                break;
            case 119:
                metodo119(token);
                break;
            case 120:
                metodo120(token);
                break;
            case 121:
                metodo121(token);
                break;
            case 122:
                metodo122(token);
                break;
            case 123:
                metodo123(token);
                break;
            case 124:
                metodo124(token);
                break;
            case 125:
                metodo125(token);
                break;
            case 126:
                metodo126(token);
                break;
            case 127:
                metodo127(token);
                break;
            case 128:
                metodo128(token);
                break;
            case 129:
                metodo129(token);
                break;
            case 130:
                metodo130(token);
                break;
        }
    }

    public void metodo100(Token token) {
        appendCode(".assembly extern mscorlib {}");
        appendCode(".assembly _programa{}");
        appendCode(".module _programa.exe");
        appendCode(".class public _unica{");
        appendCode(".method static public void _principal(){");
        appendCode(".entrypoint");
    }

    public void metodo101(Token token) {
        appendCode("ret");
        appendCode("}");
        appendCode("}");
    }

    public void metodo102(Token token) {
        var tipo = tipos.pop();
        if (Tipo.INT64.equals(tipo)) {
            appendCode("conv.i8");
        }

        var print = "[mscorlib]System.Console::Write(" +
                tipo.getIlasmValue() +
                ")";

        appendCode(print);
    }

    public void metodo103(Token token) {
        tipos.push(Tipo.INT64);
        sb.append("ldc.i8 ");
        appendCode(token.getLexeme());
        appendCode("conv.r8");
    }

    public void metodo104(Token token) {
        tipos.push(Tipo.FLOAT64);
        sb.append("ldc.r8 ");
        appendCode(token.getLexeme());
    }

    public void metodo105(Token token) {
        tipos.push(Tipo.STRING);
        sb.append("ldstr ");
        appendCode(token.getLexeme());
    }

    public void metodo106(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());
        appendCode("add");
    }

    public void metodo107(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());
        appendCode("sub");
    }

    public void metodo108(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());
        appendCode("mul");
    }

    public void metodo109(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());
        appendCode("div");
    }

    public void metodo110(Token token) {
        appendCode("sub");
    }

    public void metodo111(Token token) {
        operadoresRelacionais.push(OperadorRelacional.valueOf(token.getLexeme()));
    }

    public void metodo112(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());

        var relacional = OperadorRelacional.valueOf(token.getLexeme());
        appendCode(relacional.getIlasmCode());
    }

    public void metodo113(Token token) {
        desempilharDoisEmpilharDeVolta(token.getLexeme());

    }

    public void metodo114(Token token) {
    }

    public void metodo115(Token token) {
        tipos.push(Tipo.BOOL);
        appendCode("ldc.i4.1");
    }

    public void metodo116(Token token) {
        tipos.push(Tipo.BOOL);
        appendCode("ldc.i4.0");
    }

    public void metodo117(Token token) {
        appendCode("not");
    }

    public void metodo118(Token token) {
        appendCode("[mscorlib]System.Console::WriteLine()");
    }

    public void metodo119(Token token) {
        var locals = new StringBuilder();
        locals.append(".locals (");
        identificadores.forEach(id -> {
            var tipo = tipos.pop();
            tabelaIdentificadores.put(id, tipo);
            locals.append(tipo.getIlasmValue());
            locals.append(id);
            locals.append(", ");
        });
        locals.deleteCharAt(locals.length() - 2);
        locals.append(")");

        appendCode(locals.toString());
        identificadores.clear();
    }

    public void metodo120(Token token) throws SemanticError {
        var tipo = token.getLexeme();
        switch (tipo) {
            case "int":
                tipos.push(Tipo.INT64);
                break;
            case "float":
                tipos.push(Tipo.FLOAT64);
                break;
            case "bool":
                tipos.push(Tipo.BOOL);
                break;
            case "string":
                tipos.push(Tipo.STRING);
            case "list":
                tipos.push(Tipo.LIST);
                break;
            default:
                throw new SemanticError("Tipo nao encontrado", token.getPosition());
        }
    }

    public void metodo121(Token token) {
        identificadores.add(token.getLexeme());
    }

    public void metodo122(Token token) {
        var tipo = tipos.pop();
        if (Tipo.INT64.equals(tipo)) {
            appendCode("conv.i8");
        }

        var id = identificadores.getLast();
        sb.append("stloc ");
        appendCode(id);

        identificadores.clear();
    }

    public void metodo123(Token token) throws SemanticError {
        var tipo = tabelaIdentificadores.get(token.getLexeme());
        if (BOOL.equals(token.getLexeme())) {
            throw new SemanticError("id inválido para comando de entrada", token.getPosition());
        }

        //call string [mscorlib]System.Console::ReadLine(
        appendCode("call string [mscorlib]System.Console::ReadLine()");

        //call int64 [mscorlib]System.Int64::Parse(string
        sb.append("call ");
        sb.append(tipo.getIlasmValue());
        sb.append(" [mscorlib]System.");
        sb.append(tipo.getIlasmClass());
        sb.append("::Parse(string)\n");

        appendCode("stloc " + token.getLexeme());

    }

    public void metodo124(Token token) {
        appendCode("ldstr" + token.getLexeme());
        appendCode("call void [mscorlib]System.Console::Write(string)");
    }

    public void metodo125(Token token) throws SemanticError {
        var tipo = tipos.pop();

        if (!Tipo.BOOL.equals(tipo)) {
            throw new SemanticError("expressão incompatível em comando de seleção", token.getPosition());
        }

        var rot = proximoRotulo();
        appendCode("brfalse " + rot);
        rotulos.push(rot);

    }

    public void metodo126(Token token) {
        var desempilhado = rotulos.pop();
        appendCode(desempilhado + ":");
    }

    public void metodo127(Token token) {
        var rot = proximoRotulo();
        appendCode("br " + rot);

        var anterior = rotulos.pop();
        appendCode(anterior + ":");

        rotulos.push(rot);
    }

    public void metodo128(Token token) {
        var rot = proximoRotulo();
        appendCode(rot + ":");

        rotulos.push(rot);
    }

    public void metodo129(Token token) throws SemanticError {
        var tipo = tipos.pop();

        if (!Tipo.BOOL.equals(tipo)) {
            throw new SemanticError("expressão incompatível em comando de repetição", token.getPosition());
        }

        var desempilhado = rotulos.pop();
        appendCode("brfalse " + desempilhado);
    }

    public void metodo130(Token token) {
        var tipo = tabelaIdentificadores.get(token.getLexeme());
        tipos.push(tipo);

        appendCode("ldloc " + token.getLexeme());
        if (Tipo.INT64.equals(tipo)) {
            appendCode("conv.i8");
        }
    }

    public String getIlasmCode() {
        return sb.toString();
    }

    private void appendCode(String code) {
        sb.append(code).append("\n");
    }

    private void desempilharDoisEmpilharDeVolta(String operacao) {
        var tipo1 =  tipos.pop();
        var tipo2 = tipos.pop();

        if (Tipo.INT64.equals(tipo1) && Tipo.INT64.equals(tipo2)) {
            tipos.push(Tipo.INT64);
        }

        if (Tipo.FLOAT64.equals(tipo1)) {

        }

    }

    private String proximoRotulo() {
        return "novo_rotulo" +  numeroRotulo++;
    }

}




