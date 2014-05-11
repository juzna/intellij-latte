package com.jantvrdik.intellij.latte.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.IntStack;

/**
 * Base class for lexers generated by JFlex.
 */
public abstract class LatteBaseFlexLexer implements FlexLexer {

	/** stack of lexical states */
	private IntStack stateStack = new IntStack(10);

	/**
	 * Changes lexical state to given state and pushes it to the stack.
	 */
	protected void pushState(int state) {
		stateStack.push(yystate());
		yybegin(state);
	}

	/**
	 * Returns to previous state and checks that the previous state is one of give states.
	 */
	protected void popState(int... states) {
		int top = stateStack.pop();
		if (states.length > 0) {
			if (ArrayUtil.indexOf(states, top) < 0) {
				String list = StringUtil.join(states, ", ");
				throw new RuntimeException("Unexpected state on stack; expected one of " + list + " but found " + top + ".");
			}
		}
		yybegin(top);
	}

	/**
	 * Rollbacks the entire match.
	 */
	protected void rollbackMatch() {
		yypushback(yylength());
	}

	/**
	 * Returns length of current match.
	 */
	public abstract int yylength();

	/**
	 * Rollbacks given number of characters from current match.
	 */
	public abstract void yypushback(int number);
}