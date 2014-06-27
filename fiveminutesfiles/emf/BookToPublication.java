package uk.ac.bham.cs;
import uk.ac.bham.sitra.Rule;
import uk.ac.bham.sitra.Transformer;
import metamodels.*;
public class BookToPublication implements Rule<Book, Publication>{
	@Override
	public boolean check(Book source) {
		return true;
	}
	@Override
	public Publication build(Book source, Transformer t) {
		//create a Publication object
		// We need to use factories to produce objects
		MetamodelsFactory fiveminutesFactory = MetamodelsFactory.eINSTANCE;
		Publication pub = fiveminutesFactory.createPublication();	
		// set Title of the Publication to the title of the source
		pub.setTitle(source.getTitle());
		// add all the pages number of chapters to obtain
		// Total of pages of Book, then set NoOfPages of the Publication
		// object to TotalPage
		int TotalPage=0;
		for(Chapter chapter : source.getChapters()){
			TotalPage+= chapter.getNoOfPages();
		}
		pub.setNoOfPages(TotalPage);	
		// Notice auto-generated code provides no setAuthorList
		// method. Instead we use getAuthorList method as follows
		// this is to allow changes at runtime...
		for(Chapter chapter : source.getChapters()){
			pub.getAuthorsList().addAll(chapter.getAuthorsList());		
		}		
		return pub;
	}
	@Override
	public void setProperties(Publication target, Book source, Transformer t) {
		// We wont use this one	
	}
}
