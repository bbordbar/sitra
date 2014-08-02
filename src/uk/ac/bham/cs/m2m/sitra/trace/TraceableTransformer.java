package uk.ac.bham.cs.m2m.sitra.trace;

import java.util.Deque;

import uk.ac.bham.sitra.Transformer;

/**
 * <p>
 * An interface that exposes the methods that can be used for tracing.
 * </p>
 * 
 * @author John T. Saxon
 * 
 * @see uk.ac.bham.cs.m2m.sitra.SimpleTraceableTransformer
 */
public interface TraceableTransformer extends Transformer {
	/**
	 * <p>
	 * Returns a list of traceable steps taken from completed transformations.
	 * </p>
	 * 
	 * @return A list of traceable steps.
	 */
	public Deque<TraceableElement> getTransformationTrace();
}