// Generated from Tuga.g4 by ANTLR 4.13.2
package Tuga;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TugaParser}.
 */
public interface TugaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TugaParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(TugaParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(TugaParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link TugaParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(TugaParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(TugaParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Or}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOr(TugaParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOr(TugaParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(TugaParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(TugaParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(TugaParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(TugaParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Uminus}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUminus(TugaParser.UminusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Uminus}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUminus(TugaParser.UminusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterString(TugaParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitString(TugaParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Int}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(TugaParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(TugaParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Not}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNot(TugaParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNot(TugaParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivMod}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDivMod(TugaParser.MulDivModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivMod}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDivMod(TugaParser.MulDivModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqNotEq}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqNotEq(TugaParser.EqNotEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqNotEq}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqNotEq(TugaParser.EqNotEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBool(TugaParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBool(TugaParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd(TugaParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd(TugaParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Real}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterReal(TugaParser.RealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Real}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitReal(TugaParser.RealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompare(TugaParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link TugaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompare(TugaParser.CompareContext ctx);
}