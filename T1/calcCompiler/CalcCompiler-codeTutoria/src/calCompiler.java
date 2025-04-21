import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.InputStream;
import Calc.*;
import CodeGenerator.*;

public class calCompiler {
    static boolean showAsm;    // flag for showing generated assembly

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java calcCompiler <filename> [-asm]");
            System.exit(0);
        }
        showAsm = args.length == 2 && args[1].equals("-asm");
        String inputFilename = args[0];
        if (!inputFilename.endsWith(".cal")) {
            System.out.println("input file must have a '.cal' extension");
            System.exit(0);
        }
        String outputFilename = inputFilename + "bc";

        try {
            InputStream is = new FileInputStream(inputFilename);
            CharStream input = CharStreams.fromStream(is);
            CalcLexer lexer = new CalcLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CalcParser parser = new CalcParser(tokens);
            ParseTree tree = parser.prog();
            int numParsingErrors = parser.getNumberOfSyntaxErrors();
            if (numParsingErrors != 0)
                System.out.println(inputFilename + " has " + numParsingErrors + " syntax errors");
            else {
                CodeGen codeGen = new CodeGen();
                codeGen.visit(tree);
                if (showAsm) codeGen.dumpCode();
                codeGen.saveBytecodes(outputFilename);
            }
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
    }
}
