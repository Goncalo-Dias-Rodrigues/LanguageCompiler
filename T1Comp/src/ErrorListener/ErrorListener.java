package ErrorListener;

import org.antlr.v4.runtime.*;

/**
 * Classe que implementa um ouvinte de erros para o analisador lexico e sintatico.
 * Regista e, opcionalmente, exibe os erros ocorridos durante a analise.
 */
public class ErrorListener extends BaseErrorListener {
    private boolean showLexerErrors;
    private boolean showParserErrors;
    private int numLexerErrors = 0;
    private int numParsingErrors = 0;

    /**
     * Construtor do ErrorListener.
     *
     * @param showLexerErrors Indica se os erros lexicos devem ser apresentados.
     * @param showParserErrors Indica se os erros de parsing devem ser apresentados.
     */
    public ErrorListener(boolean showLexerErrors, boolean showParserErrors) {
        super();
        this.showLexerErrors = showLexerErrors;
        this.showParserErrors = showParserErrors;
    }

    /**
     * Metodo chamado em caso de erro lexico ou sintatico.
     *
     * @param recognizer O reconhecedor que detectou o erro.
     * @param offendingSymbol O simbolo que causou o erro.
     * @param line Numero da linha em que ocorreu o erro.
     * @param charPositionInLine Posicao do caractere na linha.
     * @param msg Mensagem de erro.
     * @param e Excecao associada ao erro (se existir).
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        if (recognizer instanceof Lexer) {
            this.numLexerErrors++;
            if (this.showLexerErrors)
                System.err.printf("linha %d:%d erro: %s\n", line, charPositionInLine, msg);
        }
        if (recognizer instanceof Parser) {
            this.numParsingErrors++;
            if (this.showParserErrors)
                System.err.printf("linha %d:%d erro: %s\n", line, charPositionInLine, msg);
        }
    }

    /**
     * Retorna o numero de erros lexicos registados.
     *
     * @return Numero de erros lexicos.
     */
    public int getNumLexerErrors() {
        return this.numLexerErrors;
    }

    /**
     * Retorna o numero de erros de parsing registados.
     *
     * @return Numero de erros de parsing.
     */
    public int getNumParsingErrors() {
        return this.numParsingErrors;
    }
}
