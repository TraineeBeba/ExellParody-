package com.exellParody.parserFiles;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.HashSet;


public class Evaluate {
    private static HashSet<String> set = new HashSet<>();
    private static HashSet<Character> numsToCheck =
            new HashSet<>(Arrays.asList('0','1', '2',
                    '3', '4', '5',
                    '6', '7', '8','9'));
    public static String link(String expr, String fm[][]) throws Exception {
        char symbol;
        String temp = "";
        int i = 0;
        do{
            symbol = expr.charAt(i);
            if (symbol == '#') {
                String cell = "#" + expr.charAt(i + 1);
                String num = "";
                for (int j = i+2; j < expr.length(); j++) {
                    if (numsToCheck.contains(expr.charAt(j))){
                        num += expr.charAt(j);
                    }
                    else break;
                }
                cell += num;

                if (!set.contains(cell)) {
                    set.add(cell);
                    int row = Integer.parseInt(num) - 1;
                    int col = (expr.charAt(i + 1) - 'A');
                    String toEvaluate = link(fm[row][col], fm);
                    temp += evaluate(toEvaluate);
                    i += cell.length();
                    set.remove(cell);
                }
                else
                {
                    temp = "ERROR";
                    return temp;
                }
            }
            else
            {
                temp += symbol;
                i++;
            }
        }
        while(i < expr.length());
        return temp;
    }

    public static double evaluate(String expression) {
        GrammarLexer lexer = new GrammarLexer(new ANTLRInputStream(expression));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener());
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokenStream);
        ParseTree tree = parser.expression();
        VisitorClass visitor = new VisitorClass();
        return visitor.visit(tree);
    }
}
