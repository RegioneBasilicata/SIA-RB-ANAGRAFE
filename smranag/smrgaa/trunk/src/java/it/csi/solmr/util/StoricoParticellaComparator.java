package it.csi.solmr.util;

import java.util.*;
import it.csi.solmr.dto.anag.terreni.*;

/**
 * Classe che si occupa di effettuare i confronti della chiave
 * logica della particella in modo da poterne effettuare l'ordinamento 
 *
 */
public class StoricoParticellaComparator implements Comparator<Object> {
	
	/**
	 * Metodo di confronto
	 */
	public int compare(Object o1, Object o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		else {
			if(o1 == null) {
				return -1;
			}
			else {
				if(o2 == null) {
					return 1;
				}
			}
		}
		StoricoParticellaVO t1 = (StoricoParticellaVO) o1;
		StoricoParticellaVO t2 = (StoricoParticellaVO) o2;

		int conf = compareString(t1.getComuneParticellaVO().getDescom(),t2.getComuneParticellaVO().getDescom());
		if(conf == 0) {
			conf = compareString(t1.getSezione(),t2.getSezione());
			if(conf == 0) {
				conf = compareNum(t1.getFoglio(),t2.getFoglio());
				if(conf == 0) {
					conf = compareNum(t1.getParticella(),t2.getParticella());
					if(conf == 0) {
						conf = compareString(t1.getSubalterno(),t2.getSubalterno());
					}
				}
			}
		}
		return conf;
  	}
	
	/**
	 * Richiamo per effettuare l'equals con i criteri impostati
	 * nel compareTo
	 */
	public boolean equals(Object obj) {
		return obj instanceof StoricoParticellaComparator;
	}
	
	/**
	 * Metodo privato per effettuare i confronti di campi da considerare
	 * numerici
	 * 
	 * @param str1
	 * @param str2
	 * @return int
	 */
	private int compareNum(String str1,String str2) {
		if(str1 == null && str2 == null) {
			return 0;
		}
		else {
			if(str1 == null) {
				return -1;
			}
			else {
				if(str2 == null) {
					return 1;
				}
			}
		}
		long num1 = 0;
		long num2 = 0;
		try {
			num1 = Long.parseLong(str1);
			num2 = Long.parseLong(str2);
		}
		catch(Exception e){}

		if(num1 > num2) {
			return 1;
		}
		else {
			if(num2 > num1) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Metodo privato per effettuare i confronti di campi da considerare
	 * stringhe
	 * 
	 * @param str1
	 * @param str2
	 * @return int
	 */
	private int compareString(String str1,String str2) {
		if(str1 == null && str2 == null) {
			return 0;
		}
		else {
			if(str1 == null) {
				return -1;
			}
			else {
				if(str2 == null) {
					return 1;
				}
			}
		}
		return str1.compareTo(str2);
	}
}
