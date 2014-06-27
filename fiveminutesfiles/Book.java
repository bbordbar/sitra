package uk.ac.bham.cs;

import java.util.ArrayList;

public class Book { 
     // title of the Book 
	 String title="";  
	 // List of chapters
	 ArrayList<Chapter> chapters;  
	 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	} 
	
	public String toString(){
		String s="";
		s+="[Book: { Title:  ";
		s+= getTitle() +"}"; 
		for(Chapter chap: chapters)	{
			//iterate and  print chapters
			s+=chap.toString();
		}
		s+="]";		
		return s;
	}
	public Book(String title, ArrayList<Chapter> chapters) {
		super();
		this.title = title;
		this.chapters = chapters;
	}
}
