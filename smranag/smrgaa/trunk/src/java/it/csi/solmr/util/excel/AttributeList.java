package it.csi.solmr.util.excel;

import java.util.Vector;

import org.jdom.*;

public class AttributeList extends Vector<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1713314836619613307L;

	public AttributeList() {
	}

	public AttributeList addAttribute(String name,String value){
		Attribute tmp = new Attribute(name,value==null?"":value);
		this.add(tmp);
		return this;
	}

	public Attribute getAttributeAt(int index){
		return (Attribute)this.elementAt(index);
	}
}
