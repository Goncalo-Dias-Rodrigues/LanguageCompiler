package CodeGenerator;

import java.io.*;
import java.util.*;
import Tuga.*;
import VM.OpCode;
import VM.Instruction.*;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Classe responsavel pela geracao de codigo para a compilacao da lingua Tuga.
 * Gera bytecodes compativeis com a Maquina Virtual S (versao mini).
 */
public class CodeGen extends TugaBaseVisitor<Void> {

    // Codigo alvo: lista de instrucoes
    private final ArrayList<Instruction> code = new ArrayList<>();
    // Pool de constantes
    private final ArrayList<Object> constantPool = new ArrayList<>();

    /**
     * Propriedade que associa os tipos as expressoes.
     */
    ParseTreeProperty<String> types;

    /**
     * Construtor da classe CodeGen.
     *
     * @param types Propriedade que associa os tipos as expressoes.
     */
    public CodeGen(ParseTreeProperty<String> types) {
        this.types = types;
    }

    /**
     * Adiciona uma constante a pool de constantes, se esta ainda nao existir.
     *
     * @param value Valor da constante.
     * @return Indice da constante na pool.
     */
    private int addConstant(Object value) {
        for (int i = 0; i < constantPool.size(); i++) {
            if (constantPool.get(i).equals(value))
                return i;
        }
        constantPool.add(value);
        return constantPool.size() - 1;
    }

    /**
     * Remove as aspas de uma string, se existirem.
     *
     * @param str String com aspas.
     * @return String sem as aspas.
     */
    private String removeAspas(String str) {
        if (str != null && str.length() > 1 && str.charAt(0) == '"' &&
                str.charAt(str.length() - 1) == '"') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * Visita o no raiz do programa e emite a instrucao halt.
     *
     * @param ctx Contexto do programa.
     * @return null.
     */
    @Override
    public Void visitProg(TugaParser.ProgContext ctx) {
        visitChildren(ctx);
        emit(OpCode.halt);
        return null;
    }

    /**
     * Visita a estatistica (instrucao) e emite o bytecode de impressao adequado.
     *
     * @param ctx Contexto da estatistica.
     * @return null.
     */
    @Override
    public Void visitStat(TugaParser.StatContext ctx) {
        visit(ctx.expr());
        String type = types.get(ctx.expr());

        switch (type) {
            case "inteiro":
                emit(OpCode.iprint);
                break;
            case "real":
                emit(OpCode.dprint);
                break;
            case "booleano":
                emit(OpCode.bprint);
                break;
            case "string":
                emit(OpCode.sprint);
                break;
        }

        return null;
    }

    /**
     * Visita um literal inteiro e emite a instrucao correspondente.
     *
     * @param ctx Contexto do literal inteiro.
     * @return null.
     */
    @Override
    public Void visitInt(TugaParser.IntContext ctx) {
        emit(OpCode.iconst, Integer.parseInt(ctx.INT().getText()));
        return null;
    }

    /**
     * Visita um literal real e emite a instrucao correspondente.
     *
     * @param ctx Contexto do literal real.
     * @return null.
     */
    @Override
    public Void visitReal(TugaParser.RealContext ctx) {
        double value = Double.parseDouble(ctx.getText());
        int index = addConstant(value);
        emit(OpCode.dconst, index);
        return null;
    }

    /**
     * Visita um literal de string e emite a instrucao correspondente.
     *
     * @param ctx Contexto da string.
     * @return null.
     */
    @Override
    public Void visitString(TugaParser.StringContext ctx) {
        String value = ctx.getText();
        int index = addConstant(value);
        emit(OpCode.sconst, index);
        return null;
    }

    /**
     * Visita um literal booleano e emite a instrucao correspondente.
     *
     * @param ctx Contexto do literal booleano.
     * @return null.
     */
    @Override
    public Void visitBool(TugaParser.BoolContext ctx) {
        if (ctx.BOOL().getText().equals("verdadeiro")) {
            emit(OpCode.tconst);
        } else if (ctx.BOOL().getText().equals("falso")) {
            emit(OpCode.fconst);
        }
        return null;
    }

    /**
     * Visita uma expressao entre parenteses e propaga o tipo da expressao.
     *
     * @param ctx Contexto dos parenteses.
     * @return null.
     */
    @Override
    public Void visitParens(TugaParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * Visita o operador unario de menos e emite a instrucao adequada.
     *
     * @param ctx Contexto do operador unario '-'.
     * @return null.
     */
    @Override
    public Void visitUminus(TugaParser.UminusContext ctx) {
        visit(ctx.expr());
        String type = types.get(ctx.expr());

        if (type.equals("inteiro")) {
            emit(OpCode.iuminus);
        } else if (type.equals("real")) {
            emit(OpCode.duminus);
        }
        return null;
    }

    /**
     * Visita o operador unario 'nao' e emite a instrucao de negacao logica.
     *
     * @param ctx Contexto do operador 'nao'.
     * @return null.
     */
    @Override
    public Void visitNot(TugaParser.NotContext ctx) {
        visit(ctx.expr());
        emit(OpCode.not);
        return null;
    }

    /**
     * Visita expressoes de multiplicacao, divisao ou modulo e emite a instrucao adequada.
     *
     * @param ctx Contexto da operacao.
     * @return null.
     */
    @Override
    public Void visitMulDivMod(TugaParser.MulDivModContext ctx) {
        String tipoExpressao = types.get(ctx);

        visit(ctx.expr(0));
        String tipo1 = types.get(ctx.expr(0));

        if (tipo1.equals("inteiro") && tipoExpressao.equals("real")) {
            emit(OpCode.itod);
        }

        visit(ctx.expr(1));
        String tipo2 = types.get(ctx.expr(1));

        if (tipo2.equals("inteiro") && tipoExpressao.equals("real")) {
            emit(OpCode.itod);
        }

        if (ctx.op.getText().equals("*")) {
            if (tipoExpressao.equals("inteiro")) {
                emit(OpCode.imult);
            } else if (tipoExpressao.equals("real")) {
                emit(OpCode.dmult);
            }
        } else if (ctx.op.getText().equals("/")) {
            if (tipoExpressao.equals("inteiro")) {
                emit(OpCode.idiv);
            } else if (tipoExpressao.equals("real")) {
                emit(OpCode.ddiv);
            }
        } else if (ctx.op.getText().equals("%")) {
            emit(OpCode.imod);
        }

        return null;
    }

    /**
     * Visita expressoes de adicao ou subtracao e emite a instrucao adequada.
     *
     * @param ctx Contexto da operacao.
     * @return null.
     */
    @Override
    public Void visitAddSub(TugaParser.AddSubContext ctx) {
        String tipoExpressao = types.get(ctx);
        switch (ctx.op.getText()) {
            case "+":
                if (tipoExpressao.equals("string")) {
                    visit(ctx.expr(0));
                    if (types.get(ctx.expr(0)).equals("inteiro"))
                        emit(OpCode.itos);
                    else if (types.get(ctx.expr(0)).equals("real"))
                        emit(OpCode.dtos);
                    else if (types.get(ctx.expr(0)).equals("booleano"))
                        emit(OpCode.btos);

                    visit(ctx.expr(1));
                    if (types.get(ctx.expr(1)).equals("inteiro"))
                        emit(OpCode.itos);
                    else if (types.get(ctx.expr(1)).equals("real"))
                        emit(OpCode.dtos);
                    else if (types.get(ctx.expr(1)).equals("booleano"))
                        emit(OpCode.btos);

                    emit(OpCode.sconcat);
                } else if (tipoExpressao.equals("real")) {
                    visit(ctx.expr(0));
                    String tipo1 = types.get(ctx.expr(0));

                    if (tipo1.equals("inteiro")) {
                        emit(OpCode.itod);
                    }

                    visit(ctx.expr(1));
                    String tipo2 = types.get(ctx.expr(1));

                    if (tipo2.equals("inteiro")) {
                        emit(OpCode.itod);
                    }

                    emit(OpCode.dadd);
                } else if (tipoExpressao.equals("inteiro")) {
                    visit(ctx.expr(0));
                    visit(ctx.expr(1));
                    emit(OpCode.iadd);
                }
                break;
            case "-":
                if (tipoExpressao.equals("real")) {
                    visit(ctx.expr(0));
                    String tipo1 = types.get(ctx.expr(0));

                    if (tipo1.equals("inteiro")) {
                        emit(OpCode.itod);
                    }

                    visit(ctx.expr(1));
                    String tipo2 = types.get(ctx.expr(1));

                    if (tipo2.equals("inteiro")) {
                        emit(OpCode.itod);
                    }

                    emit(OpCode.dsub);
                } else if (tipoExpressao.equals("inteiro")) {
                    visit(ctx.expr(0));
                    visit(ctx.expr(1));
                    emit(OpCode.isub);
                }
                break;
        }

        return null;
    }

    /**
     * Visita expressoes de comparacao (<, <=, >, >=) e emite a instrucao adequada.
     *
     * @param ctx Contexto da comparacao.
     * @return null.
     */
    @Override
    public Void visitCompare(TugaParser.CompareContext ctx) {
        String tipoEsq = types.get(ctx.expr(0));
        String tipoDir = types.get(ctx.expr(1));

        String ComparacaoT = (tipoEsq.equals("real") || tipoDir.equals("real")) ? "real" : "inteiro";

        if (ctx.op.getText().equals("<") || ctx.op.getText().equals("<=")) {
            visit(ctx.expr(0));
            if (tipoEsq.equals("inteiro") && ComparacaoT.equals("real")) {
                emit(OpCode.itod);
            }
            visit(ctx.expr(1));
            if (tipoDir.equals("inteiro") && ComparacaoT.equals("real")) {
                emit(OpCode.itod);
            }
            if (ComparacaoT.equals("inteiro")) {
                switch (ctx.op.getText()) {
                    case "<":
                        emit(OpCode.ilt);
                        break;
                    case "<=":
                        emit(OpCode.ileq);
                        break;
                }
            } else if (tipoDir.equals("real")) {
                switch (ctx.op.getText()) {
                    case "<":
                        emit(OpCode.dlt);
                        break;
                    case "<=":
                        emit(OpCode.dleq);
                        break;
                }
            }
        }

        if (ctx.op.getText().equals(">") || ctx.op.getText().equals(">=")) {
            visit(ctx.expr(0));
            if (tipoEsq.equals("inteiro") && ComparacaoT.equals("real")) {
                emit(OpCode.itod);
            }
            visit(ctx.expr(1));
            if (tipoDir.equals("inteiro") && ComparacaoT.equals("real")) {
                emit(OpCode.itod);
            }
            if (ComparacaoT.equals("inteiro")) {
                switch (ctx.op.getText()) {
                    case ">":
                        emit(OpCode.ilt);
                        break;
                    case ">=":
                        emit(OpCode.ileq);
                        break;
                }
            } else if (tipoDir.equals("real")) {
                switch (ctx.op.getText()) {
                    case ">":
                        emit(OpCode.dlt);
                        break;
                    case ">=":
                        emit(OpCode.dleq);
                        break;
                }
            }
        }

        return null;
    }

    /**
     * Visita expressoes logicas (operador 'e') e emite a instrucao correspondente.
     *
     * @param ctx Contexto da operacao logica 'e'.
     * @return null.
     */
    @Override
    public Void visitAnd(TugaParser.AndContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        emit(OpCode.and);
        return null;
    }

    /**
     * Visita expressoes logicas (operador 'ou') e emite a instrucao correspondente.
     *
     * @param ctx Contexto da operacao logica 'ou'.
     * @return null.
     */
    @Override
    public Void visitOr(TugaParser.OrContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        emit(OpCode.or);
        return null;
    }

    /**
     * Visita expressoes de igualdade ou diferenca e emite a instrucao correspondente.
     *
     * @param ctx Contexto da comparacao de igualdade ou diferenca.
     * @return null.
     */
    @Override
    public Void visitEqNotEq(TugaParser.EqNotEqContext ctx) {
        String tipo1 = types.get(ctx.expr(0));
        String tipo2 = types.get(ctx.expr(1));

        String op = ctx.op.getText();

        if (tipo1.equals("inteiro") && tipo2.equals("real")) {
            visit(ctx.expr(0));
            emit(OpCode.itod);
            visit(ctx.expr(1));
            tipo1 = "real";
        } else if (tipo1.equals("real") && tipo2.equals("inteiro")) {
            visit(ctx.expr(0));
            visit(ctx.expr(1));
            emit(OpCode.itod);
            tipo2 = "real";
        } else {
            visit(ctx.expr(0));
            visit(ctx.expr(1));
        }

        if (tipo1.equals("inteiro") && tipo2.equals("inteiro")) {
            emit(op.equals("igual") ? OpCode.ieq : OpCode.ineq);
        } else if (tipo1.equals("real") && tipo2.equals("real")) {
            emit(op.equals("igual") ? OpCode.deq : OpCode.dneq);
        } else if (tipo1.equals("booleano") && tipo2.equals("booleano")) {
            emit(op.equals("igual") ? OpCode.beq : OpCode.bneq);
        } else if (tipo1.equals("string") && tipo2.equals("string")) {
            emit(op.equals("igual") ? OpCode.seq : OpCode.sneq);
        }

        return null;
    }

    /**
     * Emite uma instrucao sem argumentos.
     *
     * @param opc Opcode da instrucao.
     */
    public void emit(OpCode opc) {
        code.add(new Instruction(opc));
    }

    /**
     * Emite uma instrucao com um argumento.
     *
     * @param opc Opcode da instrucao.
     * @param val Valor do argumento.
     */
    public void emit(OpCode opc, int val) {
        code.add(new Instruction1Arg(opc, val));
    }

    /**
     * Mostra no ecran a pool de constantes e as instrucoes em formato "assembly".
     */
    public void dumpCode() {
        System.out.println("*** Constant pool ***");
        for (int i = 0; i < constantPool.size(); i++) {
            System.out.println(i + ": " + constantPool.get(i));
        }

        System.out.println("*** Instructions ***");
        for (int i = 0; i < code.size(); i++)
            System.out.println(i + ": " + code.get(i));
    }

    /**
     * Guarda os bytecodes gerados num ficheiro.
     *
     * @param filename Nome do ficheiro onde os bytecodes serao guardados.
     * @throws IOException Se ocorrer um erro de entrada/saida.
     */
    public void saveBytecodes(String filename) throws IOException {
        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(filename))) {
            dout.writeInt(constantPool.size());
            for (Object constant : constantPool) {
                if (constant instanceof Double) {
                    dout.writeByte(1);  // Tipo: double
                    dout.writeLong(Double.doubleToLongBits((Double) constant));
                } else if (constant instanceof String str) {
                    dout.writeByte(3);  // Tipo: string
                    str = removeAspas(str);
                    dout.writeInt(str.length());
                    for (int i = 0; i < str.length(); i++) {
                        dout.writeChar(str.charAt(i));
                    }
                }
            }

            for (Instruction inst : code)
                inst.writeTo(dout);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
