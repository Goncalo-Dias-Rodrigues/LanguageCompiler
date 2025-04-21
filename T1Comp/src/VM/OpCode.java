package VM;

/**
 * Enumeracao que define os codigos das instrucoes da Maquina Virtual.
 */
public enum OpCode {
    // Instrucoes com 1 argumento (ocupam 5 bytes)
    iconst   (1),
    dconst   (1),
    sconst   (1),
    // Instrucoes sem argumentos (ocupam 1 byte)
    iprint   (0),
    iuminus  (0),
    iadd     (0),
    isub     (0),
    imult    (0),
    idiv     (0),
    imod     (0),
    ieq      (0),
    ineq     (0),
    ilt      (0),
    ileq     (0),
    itod     (0),
    itos     (0),
    dprint   (0),
    duminus  (0),
    dadd     (0),
    dsub     (0),
    dmult    (0),
    ddiv     (0),
    deq      (0),
    dneq     (0),
    dlt      (0),
    dleq     (0),
    dtos     (0),
    sprint   (0),
    sconcat  (0),
    seq      (0),
    sneq     (0),
    tconst   (0),
    fconst   (0),
    bprint   (0),
    beq      (0),
    bneq     (0),
    and      (0),
    or       (0),
    not      (0),
    btos     (0),
    halt     (0)
    ;

    private final int nArgs;

    /**
     * Construtor da enumeracao OpCode.
     *
     * @param nArgs Numero de argumentos da instrucao.
     */
    OpCode(int nArgs) {
        this.nArgs = nArgs;
    }

    /**
     * Retorna o numero de argumentos da instrucao.
     *
     * @return Numero de argumentos.
     */
    public int nArgs() { return nArgs; }

    /**
     * Converte um valor byte para o respetivo OpCode.
     *
     * @param value Valor byte.
     * @return OpCode correspondente.
     */
    public static OpCode convert(byte value) {
        return OpCode.values()[value];
    }
}