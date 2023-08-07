package it.csi.solmr.dto.iride2;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.MacroCU;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <p>Classe per mappare le abilitazioni iride2 per un certo attore, E NON PER
 * L'UTENTE. Quindi se un utente è associato ad un attore che ha diritto di
 * accedere ad un caso d'uso di write, ma l'utente è readonly, il metodo
 * isUtenteAbilitato() risponderà true e non false, spetterà al chiamante
 * restringere il privilegio dell'utente.</p>
 * @author Chiriotti/Einaudi
 * @version 1.0
 */
public class Iride2AbilitazioniVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 860510980202296161L;
	private HashMap<String,MacroCU> abilitazioni=new HashMap<String,MacroCU>();
	/**
	 * Costruttore, crea le strutture interne
	 * @param useCases
	 * @param readWrite
	 */
	public Iride2AbilitazioniVO(MacroCU macroCU[])
	{
		int length=macroCU==null?0:macroCU.length;
		for(int i=0;i<length;i++)
		{
			abilitazioni.put(macroCU[i].getCodice(),macroCU[i]);
		}
	}

	/**
	 * Indica se un attore è abilitato ad un certo CU (Vedi nota nella
	 * documentazione della classe).
	 * @param cuName nome del cu iride, es VISUALIZZA_DATI_DITTA.
	 * @return
	 */
	public boolean isUtenteAbilitato(String cuName)
	{
		return abilitazioni.get(cuName)!=null;
	}
}