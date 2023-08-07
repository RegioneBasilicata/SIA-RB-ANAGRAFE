package it.csi.solmr.business.anag;

import it.csi.csi.wrapper.SystemException;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ParametroRitornoVO;
import it.csi.solmr.dto.anag.TipoSezioniAaepVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.Sede;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AaepLocal
{
	
 // *** INIZIO SERVIZI INFOCAMERE ***
 public Azienda cercaPerCodiceFiscale(String codiceFiscale)throws SolmrException, Exception;
 
 public it.csi.solmr.ws.infoc.Sede cercaPuntualeSede(String codiceFiscale, String progrSede, String codFonte) throws SolmrException, Exception;
 
 public it.csi.solmr.ws.infoc.PersonaRIInfoc cercaPuntualePersona(String codiceFiscale, String progrPersona, String codFonte) throws SolmrException, Exception;
 
 public List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> cercaPerFiltriCodFiscFonte(String codiceFiscale, String codFonte) throws SolmrException, Exception;
 
 public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      Boolean controllaLegameRLsuAnagrafe,
	      boolean controllaPresenzaValidazione, boolean aziendeAttive)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception;
 
 public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception;

 
//*** FINE SERVIZI INFOCAMERE ***	
	
  
  


 public Boolean serviceAggiornaDatiAAEP(String CUAA)
       throws SolmrException,ServiceSystemException,SystemException,Exception;

 public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String CUAA)
       throws SolmrException,ServiceSystemException,SystemException,Exception;



  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws Exception;
  
  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
  	  throws Exception;
  

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO, Sede[] sedeInfocamere,String idParametri[], Long idUtenteAggiornamento)
    throws Exception;

  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda) throws Exception;
  
  public String isFlagPrevalente(Long[] idAteco) throws Exception;
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception;
  

}
