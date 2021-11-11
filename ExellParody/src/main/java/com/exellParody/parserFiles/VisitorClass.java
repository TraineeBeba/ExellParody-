package com.exellParody.parserFiles;

public class    VisitorClass extends GrammarBaseVisitor<Double>{
    @Override
    public Double visitMultiplicativeExpr(GrammarParser.MultiplicativeExprContext ctx) {
        double left = super.visit(ctx.expression(0));
        double right = super.visit(ctx.expression(1));

        if (ctx.operatorToken.getType()==GrammarLexer.MULTIPLY)
            return left*right;
        else return left/right;
    }
    @Override
    public Double visitExponentialExpr(GrammarParser.ExponentialExprContext ctx) {
        double left = super.visit(ctx.expression(0));
        double right = super.visit(ctx.expression(1));

        return Math.pow(left, right);
    }
    @Override
    public Double visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        double left = visit(ctx.expression(0));
        double right = visit(ctx.expression(1));

        if (ctx.operatorToken.getType()==GrammarLexer.ADD)
            return left+right;
        else return left-right;
    }
    @Override
    public Double visitDividiveWithRemainderExpr(GrammarParser.DividiveWithRemainderExprContext ctx) {
        double left = visit(ctx.expression(0));
        double right = visit(ctx.expression(1));

        if (ctx.operatorToken.getType()==GrammarLexer.MOD)
            return left%right;
        else {
            int i = (int)(left/right);
            return (double)i;
        }
    }
    @Override
    public Double visitComparativeExpr(GrammarParser.ComparativeExprContext ctx) {
        double left = visit(ctx.expression(0));
        double right = visit(ctx.expression(1));

        if (ctx.operatorToken.getType()==GrammarLexer.MAX)
            return Math.max(left, right);
        else return Math.min(left, right);
    }
    @Override
    public Double visitNumberExpr(GrammarParser.NumberExprContext ctx) {
        return Double.parseDouble(ctx.getText());
    }
    @Override
    public Double visitParenthesizedExpr(GrammarParser.ParenthesizedExprContext ctx) {
        return visit(ctx.expression());
    }
}