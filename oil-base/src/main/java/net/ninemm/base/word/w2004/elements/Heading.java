package net.ninemm.base.word.w2004.elements;


import net.ninemm.base.word.api.interfaces.IDocument;
import net.ninemm.base.word.api.interfaces.IFluentElement;
import net.ninemm.base.word.w2004.style.HeadingStyle;

public class Heading extends AbstractHeading<HeadingStyle> implements IFluentElement<Heading> { // implements IFluentElementStylable<HeadingStyle>

    //Constructor
    private Heading(String value){
        super("Heading1", value);
    }

    //this method is specific for each class. Constructor can be different...Don't know if we can make it generic
    /***
     * @param The value of the paragraph
     * @return the Fluent @Heading1
     */
    public static Heading with(String value) {
        return new Heading(value);
    }

    public static void addTitle(IDocument doc, String title) {
        HeadingStyle hs = new HeadingStyle();
        hs.align(HeadingStyle.Align.CENTER);
        doc.addEle(with(title).withStyle(hs).create());
    }

    @Override
    public Heading create() {
        return this;
    }

}
