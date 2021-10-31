// Generated from D:/JavaCourse/ExellParody/src/main/java/com/exellParody/parser\Grammar.g4 by ANTLR 4.9.1
package com.exellParody.parserFiles;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code MultiplicativeExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(GrammarParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExponentialExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponentialExpr(GrammarParser.ExponentialExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AdditiveExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumberExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpr(GrammarParser.NumberExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DividiveWithRemainderExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDividiveWithRemainderExpr(GrammarParser.DividiveWithRemainderExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(GrammarParser.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparativeExpr}
	 * labeled alternative in {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparativeExpr(GrammarParser.ComparativeExprContext ctx);
}