package uk.ac.bham.cs.m2m.sitra.trace;

public class CachedRuleInvocation<S, T> implements TraceableElement {
	/**
	 * The "cache hit"
	 */
	private RuleInvocation<S, T> ruleInvocation;
	
	/**
	 * 
	 * @param ruleInvocation The previously called transformation.
	 */
	public CachedRuleInvocation(RuleInvocation<S, T> ruleInvocation) {
		this.setRuleInvocation(ruleInvocation);
	}

	/**
	 * Get the previously called transformation.
	 * 
	 * @return The previously called transformation.
	 */
	public RuleInvocation<S, T> getRuleInvocation() {
		return ruleInvocation;
	}

	/**
	 * Set the previously called transformation.
	 * 
	 * @param ruleInvocation The previously called transformation.
	 */
	public void setRuleInvocation(RuleInvocation<S, T> ruleInvocation) {
		this.ruleInvocation = ruleInvocation;
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
		stringBuilder.append("=== CACHE ===\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("S: " + this.getRuleInvocation().getSource() + "\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("T: " + this.getRuleInvocation().getTarget() + "\n");
		stringBuilder.append(this.depth(depth));
		stringBuilder.append("R: "
				+ this.getRuleInvocation().getTransformationRule().getSimpleName() + "\n");
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
