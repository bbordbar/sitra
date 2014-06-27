package uk.ac.bham.cs;
import java.util.HashSet;
import java.util.Set;
public class Publication {
	int noOfPages=0;
	String title="";
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
		s+="[Publication: ";
		s+="{Title: " + getTitle() + "}, ";
		s+="{Author(s): " + getAuthorsList() + "}, ";
		s+="{NumberOfPages: " +getNoOfPages() + "}";
		s+="]";
		return s;
	}
}
