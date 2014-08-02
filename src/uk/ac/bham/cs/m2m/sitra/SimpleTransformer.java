package uk.ac.bham.cs.m2m.sitra;

import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.ac.bham.sitra.Rule;
import uk.ac.bham.sitra.Transformer;

/**
 * <p>
 * A Simple implementation of a transformer. Users of this class should first
 * register the transformation rule implementations using the
 * {@link #addRuleType(Class)} method. Once the rule has been registered the
 * client can use {@link #transform(Object)} or
 * {@link #transform(Class, Object)} methods to transform a source model element
 * to a target model element.
 * </p>
 * 
 * @author John T. Saxon
 * @author David Akehurst
 * @author Behzad Bordbar
 * @author Kyriakos Anastasakis
 * 
 * @see Transformer
 */
public class SimpleTransformer implements Transformer {
	/**
	 * <p>
	 * A map of real rule instances to a given class type.
	 * </p>
	 */
	protected final Map<Class<? extends uk.ac.bham.sitra.Rule<?, ?>>, Rule<?, ?>> ruleInstances = new LinkedHashMap<Class<? extends uk.ac.bham.sitra.Rule<?, ?>>, Rule<?, ?>>();

	/**
	 * <p>
	 * A cache that holds the target objects for a given (source and rule)-tuple
	 * </p>
	 */
	protected final Map<Map.Entry<?, Class<?>>, Object> _createCache = new HashMap<Map.Entry<?, Class<?>>, Object>();

	@Override
	@SuppressWarnings("unchecked")
	public <S, T> T transform(final S source) {
		// gather the relevant rules for a given source object
		List<Class<? extends Rule<S, T>>> _relevantRules = (List<Class<? extends Rule<S, T>>>) (List<?>) this
				.getRelevantRules(source.getClass());
		// loop though each relevant rule
		for (Class<? extends Rule<S, T>> ruleClass : _relevantRules) {
			// does it apply to the source
			T _target = (T) this.transform(ruleClass, source);
			// did we succeed in finding a transformation?
			if (_target != null) {
				// return the result
				return _target;
			}
		}
		
		return null;
	}

	@Override
	public <S, T> List<T> transformAll(final List<S> source) {
		// create a list for the resultant models
		List<T> _result = new Vector<T>();
		// iterate the source list
		for (S _source : source) {
			// transform the individual object
			T _target = this.transform(_source);
			// did we succeed in finding a transformation?
			if (_target != null) {
				// add it to the result model list
				_result.add(_target);
			}
		}

		return _result;
	}

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
		final Map.Entry<S, Class<?>> _cacheKey = 
				new AbstractMap.SimpleEntry<S, Class<?>>(source, rule.getClass());

		T _result = null;
		synchronized (this._createCache) {
			// does the unique key exist?
			if (this._createCache.containsKey(_cacheKey)) {
				// get it and return the value attached
				return (T) this._createCache.get(_cacheKey);
			}

			// does the rule match the source?
			if (rule.check(source)) {
				// create the instance of it
				_result = rule.build(source, this);
				// did we get one?
				if (_result != null) {
					this._createCache.put(_cacheKey, _result);
				}
				// bind the values (using legacy method)
				rule.setProperties(_result, source, this);
			}
		}

		// there is no transform
		return _result;
	}

	@Override
	public <S, T> List<T> transformAll(
			final Class<? extends uk.ac.bham.sitra.Rule<S, T>> ruleClass,
			final List<S> source) {
		// create a list for the resultant models
		List<T> _result = new Vector<T>();
		// iterate the source list
		for (S _source : source) {
			// transform the individual object
			T _target = this.transform(ruleClass, _source);
			// did we succeed in finding a transformation?
			if (_target != null) {
				// add it to the result model list
				_result.add(_target);
			}
		}

		return _result;
	}

	@Override
	public <S, T> void addRuleType(
			final Class<? extends uk.ac.bham.sitra.Rule<S, T>> ruleClass) {
		try {
			// add the rule class and an instance of the rule (for tracking)
			this.ruleInstances.put(ruleClass, uk.ac.bham.cs.m2m.sitra.Rule
					.fromRule((uk.ac.bham.sitra.Rule<S, T>) ruleClass
							.newInstance()));
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Couldn't add " + ruleClass.getCanonicalName()
					+ " to list of transformation rules.");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Get a list of relevant transformation rules that are relevant to a given
	 * source.
	 * </p>
	 * 
	 * @param sourceType
	 * @return A list of relevant rules.
	 */
	@SuppressWarnings("unchecked")
	protected <S, T> List<Class<? extends Rule<S, T>>> getRelevantRules(
			final Class<S> sourceType) {
		// create an empty list for relevant rules
		List<Class<? extends Rule<S, T>>> _relevantRules = new Vector<Class<? extends Rule<S, T>>>();
		// loop though the key set of supplied rules
		for (Class<? extends uk.ac.bham.sitra.Rule<?, ?>> ruleType : this.ruleInstances
				.keySet()) {
			// what is the source? Rule<S,T> -> Class<S>
			Class<S> fromParameterType = uk.ac.bham.cs.m2m.sitra.Rule
					._S(ruleType);

			// ...
			if (fromParameterType != null) {
				if (fromParameterType.isAssignableFrom(sourceType)) {
					// ...
					if (!Modifier.isAbstract(ruleType.getModifiers())) {
						// add the rule, it is relevant
						_relevantRules
								.add((Class<? extends Rule<S, T>>) ruleType);
					}
				}
			}
		}

		return _relevantRules;
	}
}
