package VM;

import VM.Instruction.*;

import java.util.*;
import java.io.*;


public class vm {
    private final boolean trace;       // trace flag
    private final byte[] bytecodes;    // the bytecodes, storing just for displaying them. Not really needed
    private Instruction[] code;        // instructions (converted from the bytecodes)
    private int IP;                    // instruction pointer
    private final Stack<Integer> stack = new Stack<>();    // runtime stack

    public vm( byte [] bytecodes, boolean trace ) {
        this.trace = trace;
        this.bytecodes = bytecodes;
        decode(bytecodes);
        this.IP = 0;
    }

    // decode the bytecodes into instructions and store them in this.code
    private void decode(byte [] bytecodes) {
        ArrayList<Instruction> inst = new ArrayList<>();
        try {
            // feed the bytecodes into a data input stream
            DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytecodes));
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
                        System.out.println("This should never happen! In file vm.java, method decode(...)");
                        System.exit(1);
                }
            }
        }
        catch (java.io.EOFException e) {
            // System.out.println("reached end of input stream");
            // reached end of input stream, convert arraylist to array
            this.code = new Instruction[ inst.size() ];
            inst.toArray(this.code);
            if (trace) {
                System.out.println("Disassembled instructions");
                //dumpInstructions();
                dumpInstructionsAndBytecodes();
            }
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    // dump the instructions, along with the corresponding bytecodes
    public void dumpInstructionsAndBytecodes() {
        int idx = 0;
        for (int i=0; i< code.length; i++) {
            StringBuilder s = new StringBuilder();
            s.append(String.format("%02X ", bytecodes[idx++]));
            if (code[i].nArgs() == 1)
                for (int k=0; k<4; k++)
                    s.append(String.format("%02X ", bytecodes[idx++]));
            System.out.println( String.format("%5s: %-15s // %s", i, code[i], s) );
        }
    }

    // dump the instructions to the screen
    public void dumpInstructions() {
        for (int i=0; i< code.length; i++)
            System.out.println( i + ": " + code[i] );
    }

    private void runtime_error(String msg) {
        System.out.println("runtime error: " + msg);
        if (trace)
            System.out.println( String.format("%22s Stack: %s", "", stack ) );
        System.exit(1);
    }

    private void exec_iconst(Integer v) {
        stack.push(v);
    }

    private void exec_iuminus() {
        int v = stack.pop();
        stack.push(-v);
    }
    private void exec_iadd() {
        int right = stack.pop();
        int left = stack.pop();
        stack.push(left + right);
    }
    private void exec_isub() {
        int right = stack.pop();
        int left = stack.pop();
        stack.push(left - right);
    }
    private void exec_imult() {
        int right = stack.pop();
        int left = stack.pop();
        stack.push(left * right);
    }
    private void exec_idiv() {
        int right = stack.pop();
        int left = stack.pop();
        if (right != 0)
            stack.push(left / right);
        else
            runtime_error("division by 0");
    }

    private void exec_iprint() {
        int v = stack.pop();
        System.out.println(v);
    }

    private void exec_inst( Instruction inst ) {
        if (trace) {
            System.out.println( String.format("%5s: %-15s Stack: %s", IP, inst, stack ) );
        }
        OpCode opc = inst.getOpCode();
        int nArgs;
        int v;
        switch(opc) {
            case iconst:
                v = ((Instruction1Arg) inst).getArg();
                exec_iconst( v ); break;
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
            case iprint:
                exec_iprint(); break;
            default:
                System.out.println("This should never happen! In file vm.java, method exec_inst()");
                System.exit(1);
        }
    }

    public void run() {
        if (trace) {
            System.out.println("Trace while running the code");
            System.out.println("Execution starts at instrution " + IP);
        }
        while (IP < code.length) {
            exec_inst( code[IP] );
            IP++;
        }
        if (trace)
            System.out.println( String.format("%22s Stack: %s", "", stack ) );
    }

}
