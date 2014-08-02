package uk.ac.bham.cs.m2m.sitra;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.bham.cs.m2m.sitra.trace.CachedRuleInvocation;
import uk.ac.bham.cs.m2m.sitra.trace.NestedRuleInvocation;
import uk.ac.bham.cs.m2m.sitra.trace.RuleInvocation;
import uk.ac.bham.cs.m2m.sitra.trace.TraceableElement;
import uk.ac.bham.cs.m2m.sitra.trace.TraceableTransformer;
import uk.ac.bham.sitra.Rule;

/**
 * <p>
 * This instance of the {@link SimpleTransformer} maintains a list of all
 * transformations keeping the links of source, target and rule.
 * </p>
 * 
 * @author John T. Saxon
 * 
 * @see SimpleTransformer
 * @see uk.ac.bham.cs.m2m.sitra.trace.TraceableTransformer
 */
public class SimpleTraceableTransformer extends SimpleTransformer implements
		TraceableTransformer {
	/**
	 * <p>
	 * The is the trace persistable of a transformation.
	 * </p>
	 */
	private Deque<TraceableElement> transformationTrace = new ArrayDeque<TraceableElement>();

	/**
	 * <p>
	 * A stack representing the level within a transformation.
	 * </p>
	 */
	private Deque<NestedRuleInvocation<?, ?>> nestedRuleInvocationStack = new ArrayDeque<NestedRuleInvocation<?, ?>>();

	/**
	 * <p>
	 * A cache that holds the target objects for a given (source and rule)-tuple
	 * </p>
	 */
	protected final Map<Map.Entry<?, ?>, Map.Entry<?, RuleInvocation<?, ?>>> _createCache = new HashMap<Map.Entry<?, ?>, Map.Entry<?, RuleInvocation<?, ?>>>();

	
	@SuppressWarnings("unchecked")
	@Override
	public <S, T> T transform(
			final Class<? extends uk.ac.bham.sitra.Rule<S, T>> ruleClass,
			final S source) {
		// get the rule from the rule instance list
		Rule<S, T> rule = (Rule<S, T>) this.ruleInstances.get(ruleClass);
		if(rule == null) { // do we have a rule instance?
			return null;
		}

		// create a unique identifier for `source`
		// the way xtend2 does (not too sure why the array, but oh well)
		final Map.Entry<?, Class<?>> _cacheKey = new AbstractMap.SimpleEntry<S, Class<?>>(source, ruleClass);
		T target = null;
		synchronized (this._createCache) {
			// does the unique key exist?
			if (this._createCache.containsKey(_cacheKey)) {
				// get it and return the value attached
				Map.Entry<?, RuleInvocation<?, ?>> entry = this._createCache.get(_cacheKey);
				
				// get the rop of the invocation stack
				NestedRuleInvocation<?, ?> top =
						this.getNestedRuleInvocationStack().peek();
				
				// create the cache hit trace element
				CachedRuleInvocation<S, T> cachehit = 
						new CachedRuleInvocation<S, T>((RuleInvocation<S, T>) entry.getValue());
				
				// are we at the top level?
				if(top != null) {
					// add it to the nested rule invocations
					List<TraceableElement> nestedRuleInvocations = 
							top.getNestedRuleInvocations();
					nestedRuleInvocations.add(cachehit);
				} else {
					// add it to the main trace
					// DO NOT ADD IT THE STACK!
					this.getTransformationTrace().add(cachehit);
				}
				
				// return the value
				return (T) entry.getKey();
			}

			// does the rule match the source?
			if (rule.check(source)) {
				// create the instance of it
				target = rule.build(source, this);

				// create a entry for traceability
				NestedRuleInvocation<?, ?> traceEntry = new NestedRuleInvocation<S, T>(
						source, target, ruleClass);
				// ... add the trace entry to the root if empty
				if (this.getNestedRuleInvocationStack().size() == 0)
					this.getTransformationTrace().add(traceEntry);
				else
					this.getNestedRuleInvocationStack().peek()
							.getNestedRuleInvocations().add(traceEntry);

				// add this new rule to the nested stack
				this.getNestedRuleInvocationStack().push(traceEntry);

				// did we get one?
				if (target != null) {
					Map.Entry<T, RuleInvocation<?, ?>> value
						= new AbstractMap.SimpleEntry<T, RuleInvocation<?, ?>>(target, (RuleInvocation<?, ?>) traceEntry);
					this._createCache.put(_cacheKey, value);
				}

				// bind the values (using legacy method)
				rule.setProperties(target, source, this);

				// the above may add to the stack
				// we must clean up after ourselves
				this.getNestedRuleInvocationStack().pop();
			}
		}

		// there is no transform
		return target;
	}

	@Override
	public Deque<TraceableElement> getTransformationTrace() {
		return this.transformationTrace;
	}

	/**
	 * <p>
	 * Get the list of nested rule invocations.
	 * </p>
	 * 
	 * @return The list of nested rule invocations.
	 */
	protected Deque<NestedRuleInvocation<?, ?>> getNestedRuleInvocationStack() {
		return this.nestedRuleInvocationStack;
	}
}
