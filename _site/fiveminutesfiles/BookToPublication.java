package uk.ac.bham.cs;

import java.util.HashSet;
import java.util.Set;
import uk.ac.bham.sitra.Rule;
import uk.ac.bham.sitra.Transformer;

public class BookToPublication implements Rule<Book, Publication>{
	@Override
	public boolean check(Book source) {
		return true;
	}
	@Override
	public Publication build(Book source, Transformer t) {
		//create a Publication object
		Publication pub = new  Publication();		
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
		// concatenate all authors of chapters 
		// to make a list of authors
		// we will use set because we want to avoid 
		// duplications of names. We wont bother ordering them
		// Then set the Publication object List of authors
		Set<String> setOfAuthors= new HashSet<String>();
		for(Chapter chapter : source.getChapters()){
			setOfAuthors.addAll(chapter.getAuthorsList());
		}
		pub.setAuthorsList(setOfAuthors);
		return pub;
	}
	@Override
	public void setProperties(Publication target, Book source, Transformer t) {
		// We wont use this one	
	}
}
