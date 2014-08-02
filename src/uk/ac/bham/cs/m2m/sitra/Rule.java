package uk.ac.bham.cs.m2m.sitra;

import java.lang.reflect.Method;

import uk.ac.bham.sitra.Transformer;

/**
 * 
 * @author John T. Saxon
 * 
 * @param <S>
 *            The type of the source object to transform.
 * @param <T>
 *            The type of the target object generated.
 * 
 * @see uk.ac.bham.sitra.Rule
 */
public abstract class Rule<S, T> implements uk.ac.bham.sitra.Rule<S, T> {
	/**
	 * <p>
	 * To allow an older generation rule to be used with the new transformers,
	 * this function wraps the older generation within a new generation.
	 * </p>
	 * 
	 * @param rule
	 *            A rule used within SiTra.
	 * @return A new style rule from an old style.
	 */
	public static <S, T> Rule<S, T> fromRule(
			final uk.ac.bham.sitra.Rule<S, T> rule) {
		// is this already a new type?
		if (Rule.class.isAssignableFrom(rule.getClass()))
			return (Rule<S, T>) rule; // just return it.

		// a new type of rule encapsulating an old type.
		return new Rule<S, T>() {
			@Override
			public boolean check(S source) {
				return rule.check(source);
			}
			
			@Override
			public T build(S source, Transformer transformer) {
				return rule.build(source, transformer);
			}

			@Override
			public void bind(T target, S source, Transformer transformer) {
				rule.setProperties(target, source, transformer);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Class<? extends Rule<S, T>> ruleClass() {
				return (Class<? extends Rule<S, T>>) rule.getClass();
			}
		};
	}

	/**
	 * <p>
	 * This type can be used to encapsulate the old style rules, therefore
	 * <code>.getClass()</code> will not work. This provides a wrapper to access
	 * the raw Rule type.
	 * </p>
	 * 
	 * @return The actual rule type.
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Rule<S, T>> ruleClass() {
		return (Class<? extends Rule<S, T>>) this.getClass();
	}

	@Override
	@Deprecated
	public T build(final S source, final Transformer transformer) {
		return this.instantiate(source, transformer);
	}

	/**
	 * <p>
	 * Renamed to suit the rest of the MDD world.
	 * <p>
	 * 
	 * @see uk.ac.bham.sitra.Rule#build(Object, Transformer)
	 */
	public T instantiate(final S source, final Transformer transformer) {
		T _createV = null;
		Class<T> type = Rule._T(this.ruleClass());
		try {
			_createV = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			if (_createV == null) {
				e.printStackTrace();
			}
		}

		return _createV;
	}

	/**
	 * <p>
	 * This new Rule uses the {@link #bind(Object, Object, Transformer)} opposed
	 * to the old {@link #setProperties(Object, Object, Transformer)} to setup
	 * up the target object. This collects the corrent
	 * {@link java.lang.reflection.Method} object to access the generic
	 * datatypes.
	 * </p>
	 * 
	 * @param ruleType
	 *            The rule class type.
	 * @return The method regarding this rule.
	 */
	@SuppressWarnings("unchecked")
	private static Method _buildMethod(
			Class<? extends uk.ac.bham.sitra.Rule<?, ?>> ruleType) {
		// have we gone too far?
		if (ruleType.equals(Object.class))
			return null;

		// what Rule type are we using?
		String methodName;
		if (!Rule.class.isAssignableFrom(ruleType))
			methodName = "setProperties"; // by interface
		else
			methodName = "bind"; // by abstract class (this)

		// loop through each declared method
		for (Method method : ruleType.getDeclaredMethods()) {
			// ...
			if (!method.isBridge() && !method.isSynthetic()) {
				// is it the one we want?
				if (method.getName().equals(methodName)
						&& method.getReturnType().equals(Void.TYPE)) {
					return method; // return the method
				}
			}
		}

		// can we go any further up?
		if (ruleType.getSuperclass() == null)
			return null;

		// move on up, before you go go.
		return _buildMethod((Class<? extends uk.ac.bham.sitra.Rule<?, ?>>) ruleType
				.getSuperclass());
	}

	/**
	 * <p>
	 * Given a Rule<S, T> get the relevant Class<S>.
	 * </p>
	 * 
	 * @param ruleType
	 *            The rule class type.
	 * @return The target type.
	 */
	@SuppressWarnings("unchecked")
	public static <V> Class<V> _S(
			Class<? extends uk.ac.bham.sitra.Rule<?, ?>> ruleType) {
		// get the build method for this class
		Method buildMethod = Rule._buildMethod(ruleType);
		if (buildMethod != null) { // did we get one?
			// return the second parameter type (_, Source, _)
			return (Class<V>) buildMethod.getParameterTypes()[1];
		}

		return null;
	}

	/**
	 * <p>
	 * Given a Rule<S, T> get the relevant Class<T>.
	 * </p>
	 * 
	 * @param ruleType
	 *            The rule class type.
	 * @return The target type.
	 */
	@SuppressWarnings("unchecked")
	public static <V> Class<V> _T(
			Class<? extends uk.ac.bham.sitra.Rule<?, ?>> ruleType) {
		// get the build method for this class
		Method buildMethod = Rule._buildMethod(ruleType);
		if (buildMethod != null) { // did we get one?
			// return the first parameter type (Target, _, _)
			return (Class<V>) buildMethod.getParameterTypes()[0];
		}

		return null;
	}

	/**
	 * <p>
	 * Renamed to suit the rest of the MDD world.
	 * <p>
	 * 
	 * @see uk.ac.bham.sitra.Rule#setProperties(Object, Object, Transformer)
	 */
	public abstract void bind(final T target, final S source,
			final Transformer transformer);

	@Deprecated
	@Override
	public void setProperties(final T target, final S source,
			final Transformer transformer) {
		this.bind(target, source, transformer);
	}
}
