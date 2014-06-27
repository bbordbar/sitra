package uk.ac.bham.cs;

import java.util.HashSet;
import java.util.Set;

public class Chapter {
	// number of Pages in the Chapter
	int noOfPages=0;
	// title of the chapter 
	String title="";
	// set of authors (Set have no duplicate objects)
	Set<String> authorsList= new HashSet<String>();

	public int getNoOfPages() {
		return noOfPages;
	}
	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<String> getAuthorsList() {
		return authorsList;
	}
	public void setAuthorsList(Set<String> authorsList) {
		this.authorsList = authorsList;
	}

	@Override
	public String toString() {
		String s="";
		s+="[Chapter: ";
		s+="{Title: " + getTitle() + "}, ";
		s+="{Author(s): " + getAuthorsList() + "}, ";
		s+="{NumberOfPages: " +getNoOfPages() + "}";
		s+="]";
		return s;
	}
}
