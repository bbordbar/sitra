package uk.ac.bham.cs.m2m.sitra.trace;

import java.util.ArrayList;
import java.util.List;

import uk.ac.bham.sitra.Rule;

public class NestedRuleInvocation<S, T> extends RuleInvocation<S, T> {
	/**
	 * <p>
	 * A list of {@link RuleInvocation}s to show rules a rule has itself called.
	 * </p>
	 */
	private List<TraceableElement> nestedRuleInvocations = new ArrayList<TraceableElement>();

	/**
	 * 
	 * @param rule
	 *            The rule used to transform a source to a target.
	 */
	public NestedRuleInvocation(Class<? extends Rule<S, T>> rule) {
		super(rule);
	}

	/**
	 * @param source
	 *            The source object.
	 * @param target
	 *            The target object.
	 * @param rule
	 *            The rule used to transform them.
	 */
	public NestedRuleInvocation(S source, T target,
			Class<? extends Rule<S, T>> rule) {
		super(source, target, rule);
	}

	/**
	 * 
	 * @return The list of rules a rule has called.
	 */
	public List<TraceableElement> getNestedRuleInvocations() {
		return this.nestedRuleInvocations;
	}

	@Override
	public String toString() {
		return this.toString(0);
	}

	/**
	 * 
	 */
	public String toString(Integer depth) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("===\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("S: " + this.getSource() + "\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("T: " + this.getTarget() + "\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("R: "
				+ this.getTransformationRule().getSimpleName() + "\n");
		for (TraceableElement ruleInvocation : this
				.getNestedRuleInvocations()) {
			if (ruleInvocation instanceof NestedRuleInvocation) {
				NestedRuleInvocation<?, ?> nestedRuleInvocation = (NestedRuleInvocation<?, ?>) ruleInvocation;
				stringBuilder.append(nestedRuleInvocation.toString(depth + 1));
			} else if (ruleInvocation instanceof CachedRuleInvocation) {
				CachedRuleInvocation<?, ?> cachedRuleInvocation
				 = (CachedRuleInvocation<?, ?>) ruleInvocation;
				stringBuilder.append(cachedRuleInvocation.toString(depth + 1));
			}
		}
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("===\n");
		return stringBuilder.toString();
	}

	/**
	 * <p>
	 * A helper for use within {@link NestedRuleInvocation.toString(Integer)} to
	 * add depth using a number of &gt; symbols.
	 * </p>
	 * 
	 * @param depth
	 *            The depth of the rule invocation.
	 * @return A string of '>'s.
	 */
	private String depth(Integer depth) {
		if (depth == 0)
			return "";

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			stringBuilder.append(">");
		stringBuilder.append(" ");
		return stringBuilder.toString();
	}
}
