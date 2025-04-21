package VM;

/*
  Instruction codes of the virtual machine
*/

public enum OpCode {
    // single-byte instructions, just the OpCode: no arguments
    iconst   (1),
    iuminus  (0),
    iadd     (0),
    isub     (0),
    imult    (0),
    idiv     (0),
    iprint   (0)
    ;

    private final int nArgs;    // number of arguments
    OpCode(int nArgs) {
        this.nArgs = nArgs;
    }
    public int nArgs() { return nArgs; }

    // convert byte value into an OpCode
    public static OpCode convert(byte value) {
        return OpCode.values()[value];
    }
}
