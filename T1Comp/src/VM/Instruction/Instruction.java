package VM.Instruction;

import VM.OpCode;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe que representa uma instrucao da Maquina Virtual.
 */
public class Instruction {
    OpCode opc;

    /**
     * Construtor para uma instrucao sem argumentos.
     *
     * @param opc Opcode da instrucao.
     */
    public Instruction(OpCode opc) {
        this.opc = opc;
    }

    /**
     * Retorna o opcode associado a instrucao.
     *
     * @return Opcode da instrucao.
     */
    public OpCode getOpCode() {
        return opc;
    }

    /**
     * Retorna o numero de argumentos da instrucao.
     *
     * @return Numero de argumentos (0 para esta instrucao).
     */
    public int nArgs() {
        return 0;
    }

    /**
     * Converte a instrucao para uma representacao em string.
     *
     * @return Representacao em string da instrucao.
     */
    public String toString() {
        return opc.toString();
    }

    /**
     * Escreve a instrucao num fluxo de saida.
     *
     * @param out Fluxo de dados.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    public void writeTo(DataOutputStream out) throws IOException {
        out.writeByte(opc.ordinal());
    }
}
