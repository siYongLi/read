package net.ninemm.base.word.w2004.elements;


import net.ninemm.base.word.api.interfaces.IElement;

/*
 *  It inserts a Page break at the point you add this class to the document.
 * 
 */
public class PageBreak implements IElement {

	@Override
	public String getContent() {
		return "\n<w:br w:type=\"page\" />";
	}

	/**
	 * This is a different fluent way. 
	 * Because there is no create "with", we will have to create a static method to return an instance of the pageBreak. 
	 * * Notice that this class doesn't implement IFluentInterface.
	 * 
	 * */
	public static PageBreak create() {
		return new PageBreak();
	}
	
}
