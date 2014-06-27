---
layout: default
title: Simple Transformer (SiTra)
--- 
Simple Transformer SiTra is a simple Java library for supporting a programming
approach to writing transformations aiming to, firstly use Java for writing
transformations, and secondly, to provide a minimal framework for the execution
of transformations. SiTra consists of two interfaces and a class that
implements a transformation algorithm. The aim is to facilitate a style of
programming that incorporates the concept of transformation rules. The two
interfaces are as follows and are described below.  <pre
data-src="sitra.java">Rule and Transformer Signature</pre>

### Description ###
**Rule interface:** A class that implements this interface should be written
for each of the rules in the transformation. The methods of this interface are
described as follows:

* The implementation of the check method should return a value of true if the
rule is applicable to the source object. This is particularly important if
multiple rules are applicable for objects of the same type. This method is used
to distinguish which of multiple rules should be applied by the transformer.

* The build method should construct a target object that the source object is
to be mapped to. A recursive chain of rules must not be invoked within 
this method. 

* The setProperties method is used for setting properties of the target object
(attributes or links to other objects). Setting the properties is split from
constructing the target so that recursive calling of rules is possible when
setting properties.

**Transformer interface:** In order to use the rules, add the rule classes to
an instance of the Transformer interface and call the transform method with the
root object(s) of the source model.
