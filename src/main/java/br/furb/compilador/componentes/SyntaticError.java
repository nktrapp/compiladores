package br.furb.compilador.componentes;

public class SyntaticError extends AnalysisError {
    
    private String lexeme;

    public SyntaticError(String msg, int position, String lexeme) {
        super(msg, position);
        this.lexeme = lexeme;
    }

    public SyntaticError(String msg, int position) {
        super(msg, position);
        this.lexeme = null;
    }

    public SyntaticError(String msg) {
        super(msg);
        this.lexeme = null;
    }

    public String getLexeme() {
        return lexeme;
    }
}
