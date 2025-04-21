package TypeChecker;

import Tuga.*;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Classe que realiza a verificacao de tipos na lingua Tuga.
 * Analisa a arvore sintatica, anota os tipos das expressoes e reporta inconsistencias.
 *
 * @version 1.0 (Abril 2025)
 */
public class TugaTypeChecker extends TugaBaseListener {
    private boolean showTypeErrors;
    private ParseTreeProperty<String> types = new ParseTreeProperty<>();
    private boolean hasError = false;

    /**
     * Construtor do verificador de tipos.
     *
     * @param showTypeErrors Define se os erros de tipo devem ser apresentados.
     */
    public TugaTypeChecker(boolean showTypeErrors) {
        this.showTypeErrors = showTypeErrors;
    }

    /**
     * Retorna a propriedade que associa os tipos as expressoes.
     *
     * @return A propriedade dos tipos.
     */
    public ParseTreeProperty<String> getTypes() {
        return types;
    }

    /**
     * Verifica se ocorreram erros de tipo.
     *
     * @return true se existirem erros, false caso contrario.
     */
    public boolean hasErrors() {
        return hasError;
    }

    /**
     * Regista um erro de tipo.
     *
     * @param msg Mensagem de erro a registar.
     */
    private void error(String msg) {
        hasError = true;
        if (showTypeErrors)
            System.out.println(msg);
    }

    /**
     * Verifica se ambos os operandos sao numericos (inteiro ou real).
     *
     * @param type1 Tipo do primeiro operando.
     * @param type2 Tipo do segundo operando.
     * @return true se ambos forem numericos, false caso contrario.
     */
    private boolean areNumerics(String type1, String type2) {
        return ((type1.equals("inteiro") || type1.equals("real")) &&
                (type2.equals("inteiro") || type2.equals("real")));
    }

    /**
     * Determina o tipo resultante de uma operacao numerica.
     *
     * @param type1 Tipo do primeiro operando.
     * @param type2 Tipo do segundo operando.
     * @return "real" se pelo menos um operando for real, "inteiro" caso ambos sejam inteiros.
     */
    private String getExprType(String type1, String type2) {
        return (type1.equals("real") || type2.equals("real")) ? "real" : "inteiro";
    }

    /**
     * Associa o tipo "inteiro" aos literais inteiros.
     *
     * @param ctx Contexto do literal inteiro.
     */
    @Override
    public void exitInt(TugaParser.IntContext ctx) {
        types.put(ctx, "inteiro");
    }

    /**
     * Associa o tipo "real" aos literais reais.
     *
     * @param ctx Contexto do literal real.
     */
    @Override
    public void exitReal(TugaParser.RealContext ctx) {
        types.put(ctx, "real");
    }

    /**
     * Associa o tipo "booleano" aos literais booleanos.
     *
     * @param ctx Contexto do literal booleano.
     */
    @Override
    public void exitBool(TugaParser.BoolContext ctx) {
        types.put(ctx, "booleano");
    }

    /**
     * Associa o tipo "string" aos literais de texto.
     *
     * @param ctx Contexto do literal string.
     */
    @Override
    public void exitString(TugaParser.StringContext ctx) {
        types.put(ctx, "string");
    }

    /**
     * Propaga o tipo de uma expressao entre parenteses.
     *
     * @param ctx Contexto dos parenteses.
     */
    @Override
    public void exitParens(TugaParser.ParensContext ctx) {
        types.put(ctx, types.get(ctx.expr()));
    }

    /**
     * Verifica a validade da operacao de negacao unaria e regista o tipo.
     *
     * @param ctx Contexto do operador unario '-'.
     */
    @Override
    public void exitUminus(TugaParser.UminusContext ctx) {
        String type = types.get(ctx.expr());

        if (type.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        if (!(type.equals("inteiro") || type.equals("real"))) {
            error("Erro de tipo: Operador unario '-' nao pode ser aplicado ao tipo '"
                    + type + "' (linha " + ctx.getStart().getLine() + ")");
            type = "erro";
        }
        types.put(ctx, type);
    }

    /**
     * Verifica a validade da operacao de negacao logica e regista o tipo.
     *
     * @param ctx Contexto do operador 'nao'.
     */
    @Override
    public void exitNot(TugaParser.NotContext ctx) {
        String type = types.get(ctx.expr());

        if (type.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        if (!type.equals("booleano")) {
            error("Erro de tipo: Operador de negacao 'nao' nao pode ser aplicado ao tipo '"
                    + type + "' (linha " + ctx.getStart().getLine() + ")");
            type = "erro";
        }
        types.put(ctx, type);
    }

    /**
     * Verifica o tipo de expressoes de adicao ou subtracao e regista o tipo.
     *
     * @param ctx Contexto da operacao de adicao ou subtracao.
     */
    @Override
    public void exitAddSub(TugaParser.AddSubContext ctx) {
        String type1 = types.get(ctx.expr(0));
        String type2 = types.get(ctx.expr(1));

        if (type1.equals("erro") || type2.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        String exprType;

        if (ctx.op.getText().equals("+")) {
            if (type1.equals("string") || type2.equals("string")) {
                types.put(ctx, "string");
            } else if (areNumerics(type1, type2)) {
                exprType = getExprType(type1, type2);
                types.put(ctx, exprType);
            } else {
                error("Erro de tipo: Operacao de soma nao pode ser realizada entre os tipos '"
                        + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
                types.put(ctx, "erro");
            }
        } else if (ctx.op.getText().equals("-")) {
            if (areNumerics(type1, type2)) {
                exprType = getExprType(type1, type2);
                types.put(ctx, exprType);
            } else {
                error("Erro de tipo: Operacao de subtracao nao pode ser realizada entre os tipos '"
                        + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
                types.put(ctx, "erro");
            }
        }
    }

    /**
     * Verifica o tipo de expressoes de multiplicacao, divisao ou modulo e regista o tipo.
     *
     * @param ctx Contexto da operacao de multiplicacao, divisao ou modulo.
     */
    @Override
    public void exitMulDivMod(TugaParser.MulDivModContext ctx) {
        String type1 = types.get(ctx.expr(0));
        String type2 = types.get(ctx.expr(1));

        if (type1.equals("erro") || type2.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        String exprType;

        if (ctx.op.getText().equals("*") || ctx.op.getText().equals("/")) {
            if (areNumerics(type1, type2)) {
                exprType = getExprType(type1, type2);
                types.put(ctx, exprType);
            } else {
                error("Erro de tipo: Operacao de multiplicacao ou divisao nao pode ser realizada entre os tipos '"
                        + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
                types.put(ctx, "erro");
            }
        } else if (ctx.op.getText().equals("%")) {
            if (type1.equals("inteiro") && type2.equals("inteiro")) {
                types.put(ctx, "inteiro");
            } else {
                error("Erro de tipo: Operacao de modulo nao pode ser realizada entre os tipos '"
                        + type1 + "' e '" + type2 + "', apenas pode ser feita entre inteiros (linha " + ctx.getStart().getLine() + ")");
                types.put(ctx, "erro");
            }
        }
    }

    /**
     * Verifica o tipo de expressoes com o operador logico 'e' e regista o tipo.
     *
     * @param ctx Contexto da operacao logica 'e'.
     */
    @Override
    public void exitAnd(TugaParser.AndContext ctx) {
        String type1 = types.get(ctx.expr(0));
        String type2 = types.get(ctx.expr(1));

        if (type1.equals("erro") || type2.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        if (!(type1.equals("booleano") && type2.equals("booleano"))) {
            error("Erro de tipo: Operacao logica 'e' nao pode ser realizada entre os tipos '"
                    + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
            types.put(ctx, "erro");
        } else {
            types.put(ctx, "booleano");
        }
    }

    /**
     * Verifica o tipo de expressoes com o operador logico 'ou' e regista o tipo.
     *
     * @param ctx Contexto da operacao logica 'ou'.
     */
    @Override
    public void exitOr(TugaParser.OrContext ctx) {
        String type1 = types.get(ctx.expr(0));
        String type2 = types.get(ctx.expr(1));

        if (type1.equals("erro") || type2.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        if (!(type1.equals("booleano") && type2.equals("booleano"))) {
            error("Erro de tipo: Operacao logica 'ou' nao pode ser realizada entre os tipos '"
                    + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
            types.put(ctx, "erro");
        } else {
            types.put(ctx, "booleano");
        }
    }

    /**
     * Verifica o tipo de expressoes de igualdade ou diferenca e regista o tipo.
     *
     * @param ctx Contexto da operacao de igualdade ou diferenca.
     */
    @Override
    public void exitEqNotEq(TugaParser.EqNotEqContext ctx) {
        String type1 = types.get(ctx.expr(0));
        String type2 = types.get(ctx.expr(1));

        if (type1.equals("erro") || type2.equals("erro")) {
            types.put(ctx, "erro");
            return;
        }

        if ((type1.equals("booleano") && type2.equals("booleano")) ||
                (type1.equals("string") && type2.equals("string")) ||
                areNumerics(type1, type2)) {
            types.put(ctx, "booleano");
        } else {
            error("Erro de tipo: Operacao de igualdade ou diferenca nao pode ser realizada entre os tipos '"
                    + type1 + "' e '" + type2 + "' (linha " + ctx.getStart().getLine() + ")");
            types.put(ctx, "erro");
        }
    }
}
