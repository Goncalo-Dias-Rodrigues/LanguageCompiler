package VM.Instruction;

import VM.OpCode;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe que representa uma instrucao com um argumento da Maquina Virtual.
 */
public class Instruction1Arg extends Instruction {
    int arg;

    /**
     * Construtor para uma instrucao com um argumento.
     *
     * @param opc Opcode da instrucao.
     * @param arg Valor do argumento.
     */
    public Instruction1Arg(OpCode opc, int arg) {
        super(opc);
        setArg(arg);
    }

    /**
     * Retorna o argumento associado a instrucao.
     *
     * @return Valor do argumento.
     */
    public int getArg() {
        return arg;
    }

    /**
     * Define o valor do argumento.
     *
     * @param arg Novo valor do argumento.
     */
    public void setArg(int arg) {
        this.arg = arg;
    }

    /**
     * Retorna o numero de argumentos da instrucao.
     *
     * @return 1.
     */
    @Override
    public int nArgs() {
        return 1;
    }

    /**
     * Converte a instrucao para uma representacao em string.
     *
     * @return Representacao em string da instrucao.
     */
    @Override
    public String toString() {
        return opc.toString() + " " + arg;
    }

    /**
     * Escreve a instrucao num fluxo de saida.
     *
     * @param out Fluxo de dados.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        out.writeByte(super.opc.ordinal());
        out.writeInt(arg);
    }
}
