package com.lucana.smranag.rsdi;

import java.sql.Connection;
import java.sql.SQLException;

import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

public class VerificaAccesso extends BaseDAO {


	public VerificaAccesso(String refName) throws ResourceAccessException {
		super(refName);
	}

	public String verificaDati(String cf, Long idConduzione, Long idAzienda) throws Exception{
		SolmrLogger.debug(this, "BEGIN verificaDati");
		String url=null;

		try {
			AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
			// Verificare se il CF è un utente valido
			SolmrLogger.debug(this,"*** Verificare se il CF è un utente valido ***");		
			SolmrLogger.debug(this," -- codiceFiscale ="+cf);
			if(cf != null){
				int livelloAutenticazione  = 2;
				// -- chiamata ai servizi di papua
				SolmrLogger.debug(this," - Chiamata al servizio di papua findRuoliForPersonaInApplicazione");
				it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo[] arrRuoli = anagFacadeClient.findRuoliForPersonaInApplicazione(cf, livelloAutenticazione);
				
				// Se non sono stati trovati ruoli, l'utente non è un utente abilitato per anagrafe
				if(arrRuoli == null || arrRuoli.length==0){
				  SolmrLogger.debug(this, "-- Non sono stati trovati ruoli in findRuoliForPersonaInApplicazione");
				  throw new Exception("Non sono stati trovati ruoli in findRuoliForPersonaInApplicazione");	
				}
			}
			else{
				SolmrLogger.debug(this, "-- Il codice fiscale non e' valorizzato");
				throw new Exception("Il codice fiscale non e' valorizzato");	
			}


			// Verificare che l'idConduzioneParticella sia legato all'idAzienda passato in input
			SolmrLogger.debug(this,"*** Verificare che l'idConduzioneParticella sia legato all'idAzienda passato in input ***"); 


			Long idUte = anagFacadeClient.findIdUteByIdCondPartIdAz(idConduzione,idAzienda);			
			if(idUte != null){
				SolmrLogger.debug(this, "-- E' stato trovato l'idUte legato all'idConduzioneParticella e idAzienda passati"); 
				StoricoParticellaVO storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
				url = "&codprov=" + storicoParticellaVO.getIstatComune().substring(0, 3) + "&codcom=" + storicoParticellaVO.getIstatComune().substring(3);
				SolmrLogger.debug(this, "url ="+url);
				if (Validator.isNotEmpty(storicoParticellaVO.getSezione()))
				{
					url += "&sezione=" + storicoParticellaVO.getSezione();
				}
				url += "&foglio=" + storicoParticellaVO.getFoglio();
				if (Validator.isNotEmpty(storicoParticellaVO.getParticella()))
				{
					url += "&particella=" + storicoParticellaVO.getParticella();
				}
				if (Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
				{
					url += "&subalterno=" + storicoParticellaVO.getSubalterno();
				}			
				SolmrLogger.debug(this, "url ="+url);
			}
			else{
				SolmrLogger.debug(this, "-- Non e' stato trovato l'idUte legato all'idConduzioneParticella e idAzienda passati");
				throw new Exception("Non e' stato trovato l'idUte legato all'idConduzioneParticella e idAzienda passati");
			}
		} 
		catch(Exception e) {
			SolmrLogger.debug(this, "Exception in verificaDati ="+e.getMessage());
			throw e;
		}
		finally{
			SolmrLogger.debug(this, "END verificaDati");
		}
		return url;
	}
}
