package it.csi.smranag.smrgaa.dto.stampe.documento;

import java.util.ArrayList;
import java.util.List;

public class DocDichiarazioni {

	public List<String> dichiarazioni;

	public void addDichiarazione(String dichiarazione)
	{
		if(dichiarazioni == null)
		{
			dichiarazioni = new ArrayList<>();
		}
		dichiarazioni.add(dichiarazione);
	}
	
	public List<String> getDichiarazioni() {
		return dichiarazioni;
	}

	public void setDichiarazioni(List<String> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}
	
	
	
	
	
}
