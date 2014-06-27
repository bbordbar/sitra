package uk.ac.bham.cs;
import java.util.ArrayList;
import java.util.List;
import uk.ac.bham.sitra.RuleNotFoundException;
import uk.ac.bham.sitra.SimpleTransformerImpl;
import metamodels.Book;
import metamodels.Chapter;
import metamodels.MetamodelsFactory;
import metamodels.Publication;

public class TestRules {
	public static void main(String[] args) {
		// create a factory
		MetamodelsFactory fiveminutesFactory = MetamodelsFactory.eINSTANCE;
		// produce three chapters: chap01, chap03
		Chapter chap01 = fiveminutesFactory.createChapter();
        chap01.setTitle("Chapter 01");
        chap01.setNoOfPages(10);
        chap01.getAuthorsList().add("Author01");
        chap01.getAuthorsList().add("Author02");
        System.out.println("Chapter 01: -----");
        System.out.println(chap01.toString());
        
        Chapter chap02 = fiveminutesFactory.createChapter();
        chap02.setTitle("Chapter 02");
        chap02.setNoOfPages(20);
        chap02.getAuthorsList().add("Author02");
        chap02.getAuthorsList().add("Author03");
        System.out.println("Chapter 02: -----");
        System.out.println(chap02.toString());
        
        Chapter chap03 = fiveminutesFactory.createChapter();
        chap03.setTitle("Chapter 03");
        chap03.setNoOfPages(30);
        chap03.getAuthorsList().add("Author01");
        chap03.getAuthorsList().add("Author03");
        System.out.println("Chapter 03: -----");
        System.out.println(chap03.toString());   
        // make a book and add all three chapters to it
        Book book = fiveminutesFactory.createBook();
        book.setTitle("The Book");
        book.getChapters().add(chap01);
        book.getChapters().add(chap02);
        book.getChapters().add(chap03);
		System.out.println("Book: -----");
		System.out.println(book.toString());
	}
}
