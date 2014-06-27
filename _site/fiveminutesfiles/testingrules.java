package uk.ac.bham.cs;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import uk.ac.bham.sitra.Rule;
import uk.ac.bham.sitra.RuleNotFoundException;
import uk.ac.bham.sitra.SimpleTransformerImpl;
public class testingrules {
	public static void main(String[] args) {
		// produce three chapters: chap01, chap03
		Chapter chap01= new Chapter();
		chap01.setTitle("Chapter 01");
		chap01.setNoOfPages(10);
		Set<String> authorsOfChap01 = new HashSet<String>();
		authorsOfChap01.add("Author01");
		authorsOfChap01.add("Author02");
		chap01.setAuthorsList(authorsOfChap01);
		System.out.println("Chapter 01: -----");
		System.out.println(chap01.toString());
		
		Chapter chap02= new Chapter();
		chap02.setTitle("Chapter 02");
		chap02.setNoOfPages(20);
		Set<String> authorsOfChap02 = new HashSet<String>();
		authorsOfChap02.add("Author02");
		authorsOfChap02.add("Author03");
		chap02.setAuthorsList(authorsOfChap02);
		System.out.println("Chapter 02: -----");
		System.out.println(chap02.toString());

		Chapter chap03= new Chapter();
		chap03.setTitle("Chapter 03");
		chap03.setNoOfPages(30);
		Set<String> authorsOfChap03 = new HashSet<String>();
		authorsOfChap03.add("Author01");
		authorsOfChap03.add("Author03");
		chap03.setAuthorsList(authorsOfChap03);
		System.out.println("Chapter 03: -----");
		System.out.println(chap03.toString());
		
		// Make a book: first make a List of chapters 
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		chapters.add(chap01);
		chapters.add(chap02);
		chapters.add(chap03);
		// use the constructure
		Book book = new Book("The Book", chapters);
		System.out.println("Book: -----");
		System.out.println(book.toString());
		//
		List<Class<? extends Rule<?, ?>>> rules = new ArrayList<Class<? extends Rule<?, ?>>>();
		rules.add(BookToPublication.class);
		SimpleTransformerImpl transformer = new SimpleTransformerImpl(rules);
		try {
			Publication destinationPublication = (Publication) transformer.transform(book);
			System.out.println("Produced publication: ------");
			System.out.println(destinationPublication.toString());
		} catch (RuleNotFoundException e) {
			System.err.println("There has been an error");
			e.printStackTrace();
		}
	}
}