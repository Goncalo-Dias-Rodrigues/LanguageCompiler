package VM;

import VM.Instruction.Instruction;
import VM.Instruction.Instruction1Arg;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;


public class VM {
    private Instruction[] code;
    private int IP;
    private final Stack<Object> stack = new Stack<>();
    private final ArrayList<Object> constantPool = new ArrayList<>();



    public VM(String filename) {
        decode(filename);
        this.IP = 0;
    }



    // decode the bytecodes into instructions and store them in this.code
    private void decode(String filename) {
        ArrayList<Instruction> inst = new ArrayList<>();
        try {
            // feed the bytecodes into a data input stream
            DataInputStream din = new DataInputStream(new FileInputStream(filename));

            int numConstants = din.readInt();

            for (int i = 0; i < numConstants; i++) {
                int type = din.readUnsignedByte();
                if (type == 1) {
                    constantPool.add(din.readDouble());
                } else if (type == 3) {
                    int length = din.readInt();
                    char[] chars = new char[length];
                    for (int j = 0; j < length; j++) {
                        chars[j] = din.readChar();
                    }
                    constantPool.add(new String(chars));
                } else {
                    throw new IOException("Tipo de constante invalido.");
                }
            }




            // convert them into intructions
            while (true) {
                byte b = din.readByte();
                OpCode opc = OpCode.convert(b);
                switch (opc.nArgs()) {
                    case 0:
                        inst.add(new Instruction(opc));
                        break;
                    case 1:
                        int val = din.readInt();
                        inst.add(new Instruction1Arg(opc, val));
                        break;
                    default:
                        System.out.println("This should never happen! In file VM.java, method decode(...)");
                        System.exit(1);
                }
            }
        }
        catch (EOFException e) {
            // System.out.println("reached end of input stream");
            // reached end of input stream, convert arraylist to array
            this.code = new Instruction[ inst.size() ];
            inst.toArray(this.code);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }


    // dump the instructions to the screen
    public void dumpInstructions() {
        for (int i=0; i< code.length; i++)
            System.out.println( i + ": " + code[i] );
    }

    public void dumpConstantPool() {
        for(int i = 0; i < constantPool.size(); i++) {
            System.out.println(i+": "+constantPool.get(i));
        }
    }



    private void runtime_error(String msg) {
        System.out.println("runtime error: " + msg);
        System.exit(1);
    }







    private void exec_iconst(Integer v) {
        stack.push(v);
    }


    private void exec_dconst(int index) {
        double constant = (double) constantPool.get(index);
        stack.push(constant);
    }


    private void exec_sconst(int index) {
        String constant = (String) constantPool.get(index);
        stack.push(constant);
    }






    private void exec_iprint() {
        int v = (int) stack.pop();
        System.out.println(v);
    }



    private void exec_iuminus() {
        int v = (int) stack.pop();
        stack.push(-v);
    }



    private void exec_iadd() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();
        stack.push(left + right);
    }


    private void exec_isub() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();
        stack.push(left - right);
    }

    private void exec_imult() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();
        stack.push(left * right);
    }



    private void exec_idiv() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();
        if (right != 0)
            stack.push(left / right);
        else
            runtime_error("division by 0");
    }

    private void exec_imod() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();
        if (right != 0)
            stack.push(left % right);
        else
            runtime_error("0 is not valid in %");
    }


    private void exec_ieq() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();

        boolean value = left == right;
        stack.push(value);
    }


    private void exec_ineq() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();

        boolean value = left != right;
        stack.push(value);
    }


    private void exec_ilt() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();

        boolean value = left < right;
        stack.push(value);
    }



    private void exec_ileq() {
        int right = (int) stack.pop();
        int left = (int) stack.pop();

        boolean value = left <= right;
        stack.push(value);
    }


    private void exec_itod() {
        int v = (int) stack.pop();

        stack.push((double) v);
    }


    private void exec_itos() {
        int v = (int) stack.pop();

        stack.push(Integer.toString(v));
    }



    private void exec_dprint() {
        double v = (double) stack.pop();
        System.out.println(v);
    }



    private void exec_duminus() {
        double v = (double) stack.pop();
        stack.push(-v);
    }



    private void exec_dadd() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();
        stack.push(left + right);
    }


    private void exec_dsub() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();
        stack.push(left - right);
    }

    private void exec_dmult() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();
        stack.push(left * right);
    }



    private void exec_ddiv() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();
        if (Math.abs(right) >= 10e-9)
            stack.push(left / right);
        else
            runtime_error("division by 0");
    }


    private void exec_deq() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();

        boolean value = (Math.abs(left - right) < 10e-9);
        stack.push(value);
    }


    private void exec_dneq() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();

        boolean value = (Math.abs(left - right) >= 10e-9);
        stack.push(value);
    }


    private void exec_dlt() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();

        boolean value = left < right;
        stack.push(value);
    }



    private void exec_dleq() {
        double right = (double) stack.pop();
        double left = (double) stack.pop();

        boolean value = left <= right;
        stack.push(value);
    }


    private void exec_dtos() {
        double v = (double) stack.pop();

        stack.push(Double.toString(v));
    }


    private void exec_sprint() {
        String s = (String) stack.pop();
        System.out.println(s);
    }

    private void exec_sconcat() {
        String right = (String) stack.pop();
        String left = (String) stack.pop();

        stack.push(left+right);
    }


    private void exec_seq() {
        String left = (String) stack.pop();
        String right = (String) stack.pop();

        stack.push(left.equals(right));
    }

    private void exec_sneq() {
        String left = (String) stack.pop();
        String right = (String) stack.pop();

        stack.push(!left.equals(right));
    }


    private void exec_tconst() {
        stack.push(true);
    }

    private void exec_fconst() {
        stack.push(false);
    }


    private void exec_bprint() {
        boolean b = (boolean) stack.pop();
        if (b) System.out.println("verdadeiro");
        else System.out.println("falso");
    }



    private void exec_beq() {
        boolean left = (boolean) stack.pop();
        boolean right = (boolean) stack.pop();
        stack.push(left == right);
    }


    private void exec_bneq() {
        boolean left = (boolean) stack.pop();
        boolean right = (boolean) stack.pop();
        stack.push(left != right);
    }

    private void exec_and() {
        boolean left = (boolean) stack.pop();
        boolean right = (boolean) stack.pop();
        stack.push(left && right);
    }

    private void exec_or() {
        boolean left = (boolean) stack.pop();
        boolean right = (boolean) stack.pop();
        stack.push(left || right);
    }

    private void exec_not() {
        boolean b = (boolean) stack.pop();
        stack.push(!b);
    }


    private void exec_btos() {
        boolean b = (boolean) stack.pop();
        if (b) stack.push("verdadeiro");
        else stack.push("falso");
    }

    private void exec_halt() {
        System.exit(0);
    }




    private void exec_inst( Instruction inst ) {

        OpCode opc = inst.getOpCode();
        int nArgs;
        int v;
        int index;
        switch(opc) {
            case iconst:
                v = ((Instruction1Arg) inst).getArg();
                exec_iconst( v ); break;
            case dconst:
                index = ((Instruction1Arg) inst).getArg();
                exec_dconst(index); break;
            case sconst:
                index = ((Instruction1Arg) inst).getArg();
                exec_sconst(index); break;
            case iprint:
                exec_iprint(); break;
            case iuminus:
                exec_iuminus(); break;
            case iadd:
                exec_iadd(); break;
            case isub:
                exec_isub(); break;
            case imult:
                exec_imult(); break;
            case idiv:
                exec_idiv(); break;
            case imod:
                exec_imod(); break;
            case ieq:
                exec_ieq(); break;
            case ineq:
                exec_ineq(); break;
            case ilt:
                exec_ilt(); break;
            case ileq:
                exec_ileq(); break;
            case itod:
                exec_itod(); break;
            case itos:
                exec_itos(); break;
            case dprint:
                exec_dprint(); break;
            case duminus:
                exec_duminus(); break;
            case dadd:
                exec_dadd(); break;
            case dsub:
                exec_dsub(); break;
            case dmult:
                exec_dmult(); break;
            case ddiv:
                exec_ddiv(); break;
            case deq:
                exec_deq(); break;
            case dneq:
                exec_dneq(); break;
            case dlt:
                exec_dlt(); break;
            case dleq:
                exec_dleq(); break;
            case dtos:
                exec_dtos(); break;
            case sprint:
                exec_sprint(); break;
            case sconcat:
                exec_sconcat(); break;
            case seq:
                exec_seq(); break;
            case sneq:
                exec_sneq(); break;
            case tconst:
                exec_tconst(); break;
            case fconst:
                exec_fconst(); break;
            case bprint:
                exec_bprint(); break;
            case beq:
                exec_beq(); break;
            case bneq:
                exec_bneq(); break;
            case and:
                exec_and(); break;
            case or:
                exec_or(); break;
            case not:
                exec_not(); break;
            case btos:
                exec_btos(); break;
            case halt:
                exec_halt(); break;

            default:
                System.out.println("This should never happen! In file VM.java, method exec_inst()");
                System.exit(1);
        }
    }

    public void run() {
        System.out.println("*** VM output ***");
        while (IP < code.length) {
            exec_inst( code[IP] );
            IP++;
        }
    }

}