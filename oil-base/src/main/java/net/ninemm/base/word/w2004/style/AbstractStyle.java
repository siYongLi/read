package net.ninemm.base.word.w2004.style;


import net.ninemm.base.word.api.interfaces.IElement;
import net.ninemm.base.word.api.interfaces.ISuperStylin;

public abstract class AbstractStyle implements ISuperStylin {

	private IElement element;

	@Override
	public void setElement(IElement element) {
		this.element = element;
	}

	@Override
	public IElement create() {
		return this.element;
	}
	
}
