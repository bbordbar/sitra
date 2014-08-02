---
layout: default
title: 
---
##  An Example from ATL

This example is based on an example from [ATL][1].  This example describes the
transformation of a structure of families to a list of people. Each family has
a last name and contains a father, a mother and a number of sons and daughters
(0, 1 or more). Each family member has their own first name.  The transformation
will convert this representation to a list of people, where each has its full
name and can be either male or female. The meta-models of the source and
destination are as follows.

<div class="row" style="margin-bottom: 10px;">
	<div class="col-md-6">
		<img src="{{ site.baseurl }}/images/tut_3_family.png" />
		<div style="text-align: center" class="text-muted"><small>The Source Meta-Model</small></div>
	</div>
	<div class="col-md-6">
		<img src="{{ site.baseurl }}/images/tut_3_person.png" />
		<div style="text-align: center" class="text-muted"><small>The Target Meta-Model</small></div>
	</div>
</div>

The process can be split into <span class="text-danger">#</span> parts:

### Step 1: Generate Model Elements using EMF

There are a few ways you can generate these model elements.

1. Manually (as per the [Five Minute Tutorial]({{ site.baseurl }}/fiveminutes.html)).
2. Using Eclipse, using the [Ecore Model Diagram]({{ site.baseurl }}/emffiveminutes.html) or Ecore editor.
3. Using [Emfatic][2].

We won't cover the third element, but encourage the reader to have a look at
this alternative. It is a textual representation of a model, that can generate
*.ecore* files. A nice alternative if the model becomes quite cumbersome when
developing in a graphical manner.

### Step 2: Create the Transformation Rules

Imagine you have a family structure, i.e. instances of *Family* with related
instances of *Member*, and we want to list them as people, i.e. a list of
*Person*, where the **fullName** of a *Person* is the *Member*'s **firstName** 
concatenated with the *Family*'s **lastName**.

The example provided by ATL uses a single rule for each output, *Male* and
*Female*. This is to keep the example simple, since we have already got to
grips with the basics we'll move a little bit into polymorphism. You'll notice
that the assignment of **fullName** is specified twice, once in *Member2Male*
and again in *Member2Female*. This, not unlike Java, can be placed within an
abstract rule. This can be completed in SiTra as well.

Below is an abstract class *MemberToPerson* which transforms a *Member* to a
*Person*.  We have already mentioned that there are two phases within a
transformation, an *initialisation* phase and a *binding* phase. In this class
(<span class="text-info">shown below</span>) contains only the *binding* phase
and two helpers (as per ATL's example).

<style>
</style>

{% highlight java linenos %}
public abstract class MemberToPerson extends Rule<Member, Person> {
	@Override
	public void bind(Person target, Member source,
			Transformer transformer) {
		target.setFullName(source.getFirstName() + " " + lastName(source));
	}

	public static String lastName(Member source) {
		Family family;
		if((family = source.getFamilyFather()) == null) {
			if((family = source.getFamilyMother()) == null) {
				if((family = source.getFamilySon()) == null) {
					family = source.getFamilyDaughter();
				}
			}
		}

		return family.getLastName();
	}

	public static Boolean isFemale(Member source) {
		return source.getFamilyMother() != null
			|| source.getFamilyDaughter() != null;
	}
}
{% endhighlight %}

The *binding* consists of setting the **fullName** attribute within the target
element. The two helper functions:

1. ``lastName`` determines which last name to use.
2. ``isFemale`` determines whether the *Person* is female.

This class cannot be used within a transformation as it is *abstract* and even
if it wasn't it cannot generate the target objects (there is no `instantiate`
method). Thus we make two classes that extend the abstract, to return the
instances of *Male* and *Female*.

{% highlight java linenos %}
public class MemberToMale extends MemberToPerson {
	@Override
	public boolean check(Member source) {
		return !isFemale(source);
	}

	@Override
	public Person instantiate(Member source,
			Transformer transformer) {
		return PersonFactory.eINSTANCE.createMale();
	}
}

public class MemberToFemale extends MemberToPerson {
	@Override
	public boolean check(Member source) {
		return isFemale(source);
	}

	@Override
	public Person instantiate(Member source,
			Transformer transformer) {
		return PersonFactory.eINSTANCE.createFemale();
	}
}
{% endhighlight %}


By default the `instantiate` method uses reflection to call the object's
empty constructor. However, since we assume you are using Ecore models;
we need to override this method to use the factory that is created.

### Step 3: Run the Transformation

Our model this time will be from an external source stored as XML Metadata
Interchange (XMI). This is an XML file that contains and instance of our meta
file. A nice way of generating this is ti use the Human-Usable Textual Notation
(HUTN).

<div class="highlight">
<pre class="language-"><code class="language-">@Spec {
	metamodel "1.0" {
		nsUri: "http://family/1.0"
	}
}

family {
	Family {
		lastName: "March"
		father: Member {
			firstName: "Jim"
		}
		mother: Member {
			firstName: "Cindy"
		}
		sons: Member {
			firstName: "Brandon"
		}
		daughters: Member {
			firstName: "Brenda"
		}
	}
	
	Family {
		lastName: "Sailor"
		father: Member {
			firstName: "Peter"
		}
		mother: Member {
			firstName: "Jackie"
		}
		sons: Member {
			firstName: "David"
		}, Member {
			firstName: "Dylan"
		}
		daughters: Member {
			firstName: "Kelly"
		}
	}
}</code></pre>
</div>

The above snippet uses creates the same model as what is used with the ATL
example. You can generate the *.model* file by right-clinking on the file in
eclipse, move to the *HUTN* menu and then select *Generate Model ...*.

In order to read our *ECORE* model from a file we need to tweak our earlier
example to use a *ResourceSet*. This is the mechanism provided by the library
to handle persistent documents. We also need to register our metamodels and
their factories so the resource manager can use them. This is shown in our
transformation code below, between lines 23 and 37.

{% highlight java linenos %}
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import family.FamilyFactory;
import family.FamilyPackage;
import person.PersonFactory;
import person.PersonPackage;
import uk.ac.bham.cs.m2m.sitra.SimpleTransformer;


public class Main {
	public static void main(String[] args) throws IOException {
		// create a collection of related documents
		ResourceSet resourceSet = new ResourceSetImpl();
		// use the ".model" extension
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
			.put("model", new XMIResourceFactoryImpl());

		// register the Family Package and Factory
		resourceSet.getPackageRegistry()
			.put(FamilyPackage.eNS_URI, FamilyPackage.eINSTANCE);
		FamilyPackage.eINSTANCE.setEFactoryInstance(FamilyFactory.eINSTANCE);
		
		// register the Person Package and Factory
		resourceSet.getPackageRegistry()
			.put(PersonPackage.eNS_URI, PersonPackage.eINSTANCE);
		PersonPackage.eINSTANCE.setEFactoryInstance(PersonFactory.eINSTANCE);
		
		// reference the input model
		URI inUri = URI.createFileURI("./model/Family.in.model");

		// open and load the input model
		Resource inResource = resourceSet.createResource(inUri);
		inResource.load(Collections.EMPTY_MAP);
		
		// create the transformation and add our rules
		SimpleTransformer transformer = new SimpleTransformer();
		transformer.addRuleType(MemberToMale.class);
		transformer.addRuleType(MemberToFemale.class);
		
		// have a place for our output model
		EList<EObject> outModel = new BasicEList<>();
		
		// flatten the model
		TreeIterator<EObject> iter = inResource.getAllContents();
		while(iter.hasNext()) { // move through it
			// transform it
			EObject target = transformer.transform(iter.next());
			if(target != null) {
				// add it to the output
				outModel.add(target);
			}
		}
		
		// save it to the output file
		URI outURI = URI.createFileURI("./model/Person.out.model");
		Resource outResource = resourceSet.createResource(outURI);
		outResource.getContents().addAll(outModel);
		outResource.save(Collections.EMPTY_MAP);
	}
}
{% endhighlight %}

You'll notice that we don't have a rule for *FamilyToPeople*. So how can we
reach the *Members*? ATL and other tools flatten the model before processing
it. Since we are using ECORE we can iterate all elements via
a *TreeIterator* as show above on line 55.

[1]: http://wiki.eclipse.org/ATL/Tutorials_-_Create_a_simple_ATL_transformation "ATL/Tutorials - Create a simple ATL transformation"
[2]: http://www.eclipse.org/modeling/emft/emfatic/ "Emfatic - A textual syntax for EMF Ecore (meta-)models."
