import Tuga.*;
import TypeChecker.*;
import ErrorListener.*;
import CodeGenerator.*;

import VM.VM;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TugaCompileAndRun {
    public static void main(String[] args) throws Exception {

        boolean showLexerErrors = false;
        boolean showParserErrors = false;
        boolean showTypeCheckingErrors = false;

        String inputFile = null;

        if (args.length>0){
            inputFile = args[0];
        }

        InputStream is = System.in;

        try {
            if (inputFile != null){
                is = new FileInputStream(inputFile);
            }

            CharStream input = CharStreams.fromStream(is);

            ErrorListener el = new ErrorListener(showLexerErrors,showParserErrors);

            TugaLexer lexer = new TugaLexer(input);

            lexer.removeErrorListeners();
            lexer.addErrorListener(el);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            TugaParser parser = new TugaParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(el);

            ParseTree tree = parser.prog();

            if (el.getNumLexerErrors() > 0) {
                if (!showLexerErrors) {
                    System.out.println("Input has lexical errors");
                }
                return;
            }

            if (el.getNumParsingErrors() > 0) {
                if (!showParserErrors) {
                    System.out.println("Input has parsing errors");
                }
                return;
            }

            TugaTypeChecker typeChecker = new TugaTypeChecker(showTypeCheckingErrors);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(typeChecker, tree);

            if (typeChecker.hasErrors()) {
                if (!showTypeCheckingErrors) {
                    System.out.println("Input has type checking errors");
                }
                return;
            }

            CodeGen codeGen = new CodeGen(typeChecker.getTypes());

            codeGen.visit(tree);
            codeGen.dumpCode();

            codeGen.saveBytecodes("bytecodes");

            VM vm = new VM("bytecodes");

            vm.run();
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
    }
}