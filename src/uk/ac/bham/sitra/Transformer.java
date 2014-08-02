package uk.ac.bham.sitra;

import java.util.List;

import uk.ac.bham.sitra.Rule;

/**
 * <p>
 * Implementations should use this interface to define the logic regarding how
 * the transformation rules are used.
 * </p>
 * <p>
 * SiTra comes with with two implementations
 * {@link uk.ac.bham.cs.m2m.sitra.SimpleTransformer} and
 * {@link uk.ac.bham.cs.m2m.sitra.SimpleTraceableTransformer}, these should be
 * enough for most transformations.
 * </p>
 * 
 * @author John T. Saxon
 * @author David Akehurst
 * @author Behzad Bordbar
 * 
 * @see uk.ac.bham.cs.m2m.sitra.SimpleTransformer
 * @see uk.ac.bham.cs.m2m.sitra.SimpleTraceableTransformer
 * 
 */
public interface Transformer {
	/**
	 * <p>
	 * Return a target object for a given source.
	 * </p>
	 * 
	 * <p>
	 * The type of target will depend on the order of rules present within the
	 * Transformer (@see Transformer.addRuleType).
	 * </p>
	 * 
	 * @param source
	 *            The source for transformation.
	 * @return The generated target object.
	 */
	public <S, T> T transform(final S source);

	/**
	 * <p>
	 * Return a lost of target objects for a list of given source objects.
	 * </p>
	 * 
	 * <p>
	 * The type of target will depend on the order of rules present within the
	 * Transformer (@see Transformer.addRuleType).
	 * </p>
	 * 
	 * @param source
	 *            A list of sources for transformation.
	 * @return The generated list of target objects.
	 */
	public <S, T> List<T> transformAll(final List<S> source);

	/**
	 * 
	 * @param source
	 *            The source for transformation.
	 * @param rule
	 *            The specific rule to use for the transformation.
	 * @return The generated target object.
	 */
	public <S, T> T transform(final Class<? extends Rule<S, T>> rule,
			final S source);

	/**
	 * 
	 * @param source
	 *            A list of sources for transformation.
	 * @param rule
	 *            The specific rule to use for the transformation.
	 * @return The generated list of target objects.
	 */
	public <S, T> List<T> transformAll(final Class<? extends Rule<S, T>> rule,
			final List<S> source);

	/**
	 * 
	 * @param ruleType
	 *            The rule to add to the transformer.
	 */
	public <S, T> void addRuleType(final Class<? extends Rule<S, T>> ruleType);
}
