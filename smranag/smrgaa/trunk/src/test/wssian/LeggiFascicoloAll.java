package wssian;

import it.csi.sian.fascicoloAll.BasicHttpBinding_WSFascicoloAllServiceStub;
import it.csi.sian.fascicoloAll.EsitoFascicoloAllResponse;
import it.csi.sian.fascicoloAll.FascicoloAllLocator;
import it.csi.sian.fascicoloAll.FascicoloAllResponse;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTI;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_AA;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_GA;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_OTE;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_allev;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_anom;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_cons;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_fabb;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_legass;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_macch;
import it.csi.sian.fascicoloAll.ISWDATIRICHIESTIFlag_manod;
import it.csi.sian.fascicoloAll.ISWSAgricoltoreAttivoResp;
import it.csi.sian.fascicoloAll.ISWSEsitoFascicoloAll;
import it.csi.sian.fascicoloAll.ISWSEsitoFascicoloAllServizio;
import it.csi.sian.fascicoloAll.ISWSFASCALL;
import it.csi.sian.fascicoloAll.ISWSFASCICOLO;
import it.csi.sian.fascicoloAll.ISWSMessaggi;
import it.csi.sian.fascicoloAll.ISWSRespAnagFascicolo15;
import it.csi.sian.fascicoloAll.ISWSResponse;
import it.csi.sian.fascicoloAll.Msg;
import it.csi.sian.fascicoloAll.RichiestaFascicolo;
import it.csi.sian.oprFascicolo.SOAPAutenticazione;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.util.performance.StopWatch;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;

public class LeggiFascicoloAll {

	// jdbc Connection
	private static Connection conn = null;
	private static PreparedStatement stmt = null;

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);			
		
		org.apache.axis.AxisProperties.setProperty("axis.socketSecureFactory","org.apache.axis.components.net.SunFakeTrustSocketFactory");
		// Set proxy
		AxisProperties.setProperty("http.proxyHost", "proxyto02.aizoon.it");
		AxisProperties.setProperty("http.proxyPort", "8080");
		AxisProperties.setProperty("http.proxyUser", "alessandra.sorasio");
		AxisProperties.setProperty("http.proxyPassword", "Alesoras??40");
		AxisProperties.setProperty("http.proxySet", "true");

		FascicoloAllResponse fascicoloAllResponse = null;
		long a = 0;
		try {
			// Input : N Cuaa presi da SIAN_FASCICOLO (SMRGAA db TOBECONFIG)

			// ------------- Ottengo la connessione al db
			//System.out.println(" -- Ottengo la connessione al db");

			//createConnection();

			// 1 record
			//String query = "select cuaa from sian_fascicolo where rownum = 1";
			// 10 record
			//String query = "select cuaa from sian_fascicolo where rownum < 11";
			// 100 record
			// String query
			// ="select cuaa from sian_fascicolo where rownum < 101";
			// 200 record
			// String query
			// ="select cuaa from sian_fascicolo where rownum < 201";
			// 400 record
			// String query
			// ="select cuaa from sian_fascicolo where rownum < 401";
			// 800 record
			// String query
			// ="select cuaa from sian_fascicolo where rownum < 801";
			// 1000 record
			// String query
			// ="select cuaa from sian_fascicolo where rownum < 1001";

			
			//stmt = conn.prepareStatement(query);
			//System.out.println(" -- eseguo la query per avere i dati di input ="+ query);
			//ResultSet rs = stmt.executeQuery();

			System.out.println(" -- Effettuo le chiamate ai servizi");
			a = System.currentTimeMillis();
			stopWatch.start();
			//while (rs.next()) {
				//String cuaa = rs.getString("cuaa");
			    String cuaa = "00050690775";
				System.out.println(" - cuaa input =" + cuaa);
				
				RichiestaFascicolo richFascicolo = new RichiestaFascicolo();
				ISWSFASCALL iswsfascall = new ISWSFASCALL();
				iswsfascall.setCUAA(cuaa);
				
				ISWDATIRICHIESTI iSWDATIRICHIESTI = new ISWDATIRICHIESTI();
				
				ISWDATIRICHIESTIFlag_AA iSWDATIRICHIESTIFlag_AA = ISWDATIRICHIESTIFlag_AA.value2;
				ISWDATIRICHIESTIFlag_allev iSWDATIRICHIESTIFlag_allev= ISWDATIRICHIESTIFlag_allev.value2;
				ISWDATIRICHIESTIFlag_anom iSWDATIRICHIESTIFlag_anom = ISWDATIRICHIESTIFlag_anom.value2;
				ISWDATIRICHIESTIFlag_cons iSWDATIRICHIESTIFlag_cons = ISWDATIRICHIESTIFlag_cons.value2;
				ISWDATIRICHIESTIFlag_fabb iSWDATIRICHIESTIFlag_fabb = ISWDATIRICHIESTIFlag_fabb.value2;
				ISWDATIRICHIESTIFlag_GA iSWDATIRICHIESTIFlag_GA = ISWDATIRICHIESTIFlag_GA.value2;
				ISWDATIRICHIESTIFlag_legass iSWDATIRICHIESTIFlag_legass = ISWDATIRICHIESTIFlag_legass.value2;
				ISWDATIRICHIESTIFlag_macch iSWDATIRICHIESTIFlag_macch = ISWDATIRICHIESTIFlag_macch.value2;
				ISWDATIRICHIESTIFlag_manod iSWDATIRICHIESTIFlag_manod = ISWDATIRICHIESTIFlag_manod.value2;
				ISWDATIRICHIESTIFlag_OTE iSWDATIRICHIESTIFlag_OTE = ISWDATIRICHIESTIFlag_OTE.value2;
				
				
				
				iSWDATIRICHIESTI.setFlag_AA(iSWDATIRICHIESTIFlag_AA);
				iSWDATIRICHIESTI.setFlag_allev(iSWDATIRICHIESTIFlag_allev);
				iSWDATIRICHIESTI.setFlag_anom(iSWDATIRICHIESTIFlag_anom);
				iSWDATIRICHIESTI.setFlag_cons(iSWDATIRICHIESTIFlag_cons);
				iSWDATIRICHIESTI.setFlag_fabb(iSWDATIRICHIESTIFlag_fabb);
				iSWDATIRICHIESTI.setFlag_GA(iSWDATIRICHIESTIFlag_GA);
				iSWDATIRICHIESTI.setFlag_legass(iSWDATIRICHIESTIFlag_legass);
				iSWDATIRICHIESTI.setFlag_macch(iSWDATIRICHIESTIFlag_macch);
				iSWDATIRICHIESTI.setFlag_manod(iSWDATIRICHIESTIFlag_manod);
				iSWDATIRICHIESTI.setFlag_OTE(iSWDATIRICHIESTIFlag_OTE);
				
				iswsfascall.setDatiRichiesti(iSWDATIRICHIESTI);
				
				richFascicolo.setISWSFASC_ALL(iswsfascall);

				BasicHttpBinding_WSFascicoloAllServiceStub binding = null;
				try {
				  binding = getBinding("LeggiFascicoloAll");
				}
				catch (Exception e1) {
				  System.out.println("Eccezione in getBinding");
				  e1.printStackTrace();
				}
																								
				System.out.println(" -- Ottengo la connessione al db");
				createConnection();

				// Traccio sul db la chiamata effettuata con il codice di ritorno 
				System.err.println("*********** Traccio sul db la chiamata effettuata al servizio LeggiFascicoloAll con il codice di ritorno ***********");				
				SianChiamataServizioVO chiamServ = new SianChiamataServizioVO();
				
				chiamServ.setCuaaInput(iswsfascall.getCUAA());
				chiamServ.setIdServizio(1L);
				chiamServ.setDataChiamata(new Date());
				Long idChiamata = insertChiamataServizio(chiamServ);
				
				
				System.err.println(" -- Effettuo chiamata a LeggiFascicoloAll");							
				try {
					fascicoloAllResponse = binding.leggiFascicoloAll(richFascicolo);
				} 
				catch (Exception e) {
					SianChiamataServizioVO chiamataServOutput = new SianChiamataServizioVO();
					chiamataServOutput = new SianChiamataServizioVO();
					chiamataServOutput.setIdChiamata(idChiamata);							
					chiamataServOutput.setEccezione(e.getMessage().substring(0, 999));
					updateChiamataServizio(chiamataServOutput);
					e.printStackTrace();
					throw e;
				}
				
				if (fascicoloAllResponse != null) {
					// PROVA : TRASFORMO OGGETTO IN XML
					//String xmlOutputLeggiFascicoloAll = jaxbObjectToXML(fascicoloAllResponse);
					//System.err.println("xmlOutputLeggiFascicoloAll ="+xmlOutputLeggiFascicoloAll);
					
					if (fascicoloAllResponse.getISWSResponse() != null) {
						ISWSResponse resp = fascicoloAllResponse.getISWSResponse();
												
						String idXml = fascicoloAllResponse.getIDXML();
						System.out.println("idXml tornato da LeggiFascicoloAll="+idXml);
						
						String codiceRitorno = resp.getCodRet();
						System.out.println("codiceRitorno tornato da LeggiFascicoloAll="+codiceRitorno);
						/*
						 *  012	Operazione correttamente eseguita
							013	Errore generico
							016	Dati non trovati
							020	Campo obbligatorio
							021	Parametri non validi/non coerenti
							030	Soggetto non trovato
							031	Fascicolo non trovato
							032	Fascicolo non di competenza
							999	Fascicolo non presente in Banca Dati Normalizzata
						 */			
						
						/*
						 * Tabella dei codici di ritorno per le anomalie
							Segnalazione				CodRet
							Campo obbligatorio			020
							Valore del campo non valido	021
							Dati richiesti presenti		012
							Errore dell’applicazione	013
							Dati non trovati			016
							Dati in elaborazione		034
						 */
						
						String segnalazione = resp.getSegnalazione();
						System.out.println("segnalazione tornata da LeggiFascicoloAll ="+segnalazione);
						
						System.out.println("**** updateChiamataServizio() LeggiFascicoloAll con idChiamata ="+idChiamata);
						SianChiamataServizioVO chiamataServOutput = new SianChiamataServizioVO();  
						chiamataServOutput.setCodRetOutput(codiceRitorno);
						chiamataServOutput.setSegnalazioneOutput(segnalazione);
						chiamataServOutput.setIdXmlOutput(idXml);
						chiamataServOutput.setIdChiamata(idChiamata);
						
						updateChiamataServizio(chiamataServOutput);
					
						
						if(codiceRitorno.equals("012")){
						  System.out.println("Operazione correttamente eseguita per LeggiFascicoloAll");
						  
						  // Se è già stata fatta una chiamata ad EsitoFascicoloAllAsync con lo stesso idXml, non ripeto la chiamata :
						  Long idChiamataEffettuata = getChiamataServizioByIdXml(idXml);
						  System.out.println("idChiamataEffettuata ="+idChiamataEffettuata);
						  if(idChiamataEffettuata == null){						  						  
							  System.err.println(" -- Effettuo chiamata a EsitoFascicoloAllAsync");						  
							  ISWSEsitoFascicoloAll esitoFascicoloAll = new ISWSEsitoFascicoloAll();
							  System.out.println(" - idXml input =" + idXml);
							  esitoFascicoloAll.setIdxml(idXml);
							  System.out.println(" - cuaa input=" + cuaa);
							  esitoFascicoloAll.setCUAA(cuaa);
							  						  
							  ISWSEsitoFascicoloAllServizio iSWSEsitoFascicoloAllServizio = ISWSEsitoFascicoloAllServizio.LeggiFascicoloAll;
							  esitoFascicoloAll.setServizio(iSWSEsitoFascicoloAllServizio);
							  
							  binding = null;
							  try {
								binding = getBinding("EsitoFascicoloAllAsync");
							  } 
							  catch (Exception e1) {
								System.out.println("Eccezione in getBinding");							
								
								updateChiamataServizio(chiamataServOutput);
								e1.printStackTrace();
							  }
							  
							  
							  // Traccio sul db la chiamata effettuata con il codice di ritorno 
							  System.err.println("*********** Traccio sul db la chiamata effettuata al servizio esitoFascicoloAllAsync con il codice di ritorno ***********");				
							  chiamServ = new SianChiamataServizioVO();
								
							  chiamServ.setCuaaInput(iswsfascall.getCUAA());
							  chiamServ.setIdServizio(2L);
							  chiamServ.setDataChiamata(new Date());
							  chiamServ.setIdXmlInput(idXml);
							  Long idChiamataServ2 = insertChiamataServizio(chiamServ);
							  
							  System.err.println("idXml in input a esitoFascicoloAllAsync ="+idXml);
							  EsitoFascicoloAllResponse responseEsitoFascicoloAll;
							  try {
								responseEsitoFascicoloAll = binding.esitoFascicoloAllAsync(esitoFascicoloAll);
							  } 
							  catch (Exception e) {
							    chiamataServOutput = new SianChiamataServizioVO();
								chiamataServOutput = new SianChiamataServizioVO();
								chiamataServOutput.setIdChiamata(idChiamataServ2);							
								chiamataServOutput.setEccezione(e.getMessage().substring(0, 999));
								updateChiamataServizio(chiamataServOutput);
								e.printStackTrace();
								throw e;
							  }
							  if(responseEsitoFascicoloAll != null){							  
							    if(responseEsitoFascicoloAll.getISWSResponse() != null){
									// ISWSResponse : Esito dell’operazione
									ISWSResponse iSWSResponse = responseEsitoFascicoloAll.getISWSResponse();								  
									String codRet =  iSWSResponse.getCodRet();
									System.out.println("codRet tornato da esitoFascicoloAllAsync="+codRet);
									String segnalaz = iSWSResponse.getSegnalazione();
									System.out.println("segnalaz tornato da esitoFascicoloAllAsync="+segnalaz);
																	
									System.err.println("*********** Update chiamata al servizio EsitoFascicoloAllAsync con il codice di ritorno ***********");
									SianChiamataServizioVO chiamataServOutput2 = new SianChiamataServizioVO();
									chiamataServOutput2.setIdChiamata(idChiamataServ2);
									chiamataServOutput2.setCodRetOutput(codRet);
									chiamataServOutput2.setSegnalazioneOutput(segnalaz);								
									updateChiamataServizio(chiamataServOutput2);
									
									if(responseEsitoFascicoloAll.getISWSResponse().getCodRet().equals("012")){
									  System.out.println("Segnalazione : Operazione correttamente eseguita per EsitoFascicoloAllAsync");
									  
									  // ISWSMessaggi : Esito dell'elaborazione per i diversi ambiti oggetto della richiesta
									  System.out.println("*** EsitoFascicoloAllAsync : Inserisco l'esito dell'elaborazione per i diversi ambiti oggetto della richiesta");
									  ISWSMessaggi iSWSMessaggi = responseEsitoFascicoloAll.getISWSMessaggi();
									  
									  inserimentoISWSMessaggi(iSWSMessaggi.getAgricoltoreAttivo(),"AgricoltoreAttivo", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getConsistenzaTerreni(),"ConsistenzaTerreni", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getConsistenzaZootecnica(), "ConsistenzaZootecnica", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getFabbricati(), "Fabbricati", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getGiovaneAgricoltore(), "GiovaneAgricoltore", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getLegamiAssociativi(), "LegamiAssociativi", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getManodopera(), "Manodopera", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getMezziProduzione(), "MezziProduzione", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getOTE(), "OTE", idChiamataServ2);
									  inserimentoISWSMessaggi(iSWSMessaggi.getSegnalazioni(), "Segnalazioni", idChiamataServ2);
									  
																		  									  									  
									  // ISWSFASCICOLO : Contenitore per gli oggetti restituiti dai Metodi
									  ISWSFASCICOLO iSWSFASCICOLO = responseEsitoFascicoloAll.getISWSFASCICOLO();
									  if(iSWSFASCICOLO != null){
										  // ISWSRespAnagFascicolo15 : Anagrafica del fascicolo alla data di riferimento 
										  // ** (tabella creata : ISWSRespAnagFascicolo15)										  
										  if(iSWSFASCICOLO.getAnagraficaFascicolo() != null){
											System.err.println("** Inserisco ISWSRespAnagFascicolo15");
											insertISWSRespAnagFascicolo15(iSWSFASCICOLO.getAnagraficaFascicolo(),idChiamataServ2);											
										  }
										  // ISWSAgricoltoreAttivoResp : Informazione se il produttore è agricoltore attivo alla data di riferimento 
										  // ** (tabelle create : ISWSAGRICOLTOREATTIVORESP, ISWSIndicatori, ISWS_R_IND_AGRATTRESP)	
										  if(iSWSFASCICOLO.getAgricoltoreAttivo() != null && iSWSMessaggi.getAgricoltoreAttivo().getCodice().equals("012")){
											System.err.println("** Inserisco ISWSAgricoltoreAttivoResp");
											//insertISWSAgricoltoreAttivoResp(iSWSFASCICOLO.getAgricoltoreAttivo(),idChiamataServ2);  
										  }		
										  // DettaglioSoggettoWS : Dati del soggetto intestatario del fascicolo
										  if(iSWSFASCICOLO.getAnagraficaSoggetto() != null){
										    System.err.println("** Inserisco DettaglioSoggettoWS");
										    //insertDettaglioSoggettoWS(iSWSFASCICOLO.getAnagraficaSoggetto(),idChiamataServ2);
										  }
										  // ISWSTerritorioFS6 : Consistenza aziendale alla data di riferimento (array di ISWSTerritorioFS6)
										  if(iSWSFASCICOLO.getConsistenzaTerreni() != null && iSWSMessaggi.getConsistenzaTerreni().getCodice().equals("012")){
											  System.out.println("iSWSFASCICOLO.getConsistenzaTerreni().length ="+iSWSFASCICOLO.getConsistenzaTerreni().length);
											  for(int i=0;i<iSWSFASCICOLO.getConsistenzaTerreni().length;i++){
												 System.err.println("** Inserisco ISWSTerritorioFS6");
												 //insertISWSTerritorioFS6(iSWSFASCICOLO.getConsistenzaTerreni(i),idChiamataServ2); 
											  }
										  }
										  // ISWSAllevamento15 : Allevamenti alla data di riferimento (array di ISWSAllevamento15)
										  if(iSWSFASCICOLO.getConsistenzaZootecnica() != null && iSWSMessaggi.getConsistenzaZootecnica().getCodice().equals("012")){
											  System.out.println("iSWSFASCICOLO.getConsistenzaZootecnica().length ="+iSWSFASCICOLO.getConsistenzaZootecnica().length);
											  for(int i=0;i<iSWSFASCICOLO.getConsistenzaZootecnica().length;i++){
												 System.err.println("** Inserisco ISWSAllevamento15");
												 //insertISWSAllevamento15(iSWSFASCICOLO.getConsistenzaZootecnica(i),idChiamataServ2); 
											  }
										  }
										  // ISWSFabbricatoFS6 : Fabbricati alla data di riferimento (array di ISWSFabbricatoFS6)
										  if(iSWSFASCICOLO.getFabbricati() != null && iSWSMessaggi.getFabbricati().getCodice().equals("012")){
											  System.out.println("iSWSFASCICOLO.getFabbricati().length ="+iSWSFASCICOLO.getFabbricati().length);
											  for(int i=0;i<iSWSFASCICOLO.getFabbricati().length;i++){
												 System.err.println("** Inserisco ISWSFabbricatoFS6");
												 //insertISWSFabbricatoFS6(iSWSFASCICOLO.getFabbricati(i),idChiamataServ2); 
											  }  
										  }
										  // ISWSGiovaneAgricoltoreResp : Informazione se il produttore è giovane agricoltore alla data di riferimento
										  if(iSWSFASCICOLO.getGiovaneAgricoltore() != null && iSWSMessaggi.getGiovaneAgricoltore().getCodice().equals("012")){
											 System.err.println("** Inserisco ISWSFabbricatoFS6");
											 //insertISWSGiovaneAgricoltoreResp(iSWSFASCICOLO.getGiovaneAgricoltore(),idChiamataServ2); 											
										  }
										  // ISWSRespOA : Organismi associativi cui il CUAA risulta associato alla data di riferimento. (array di ISWSDatiSocio)
										  if(iSWSFASCICOLO.getLegamiAssociativi() != null && iSWSMessaggi.getLegamiAssociativi().getCodice().equals("012")){
											System.out.println("iSWSFASCICOLO.getLegamiAssociativi().length ="+iSWSFASCICOLO.getLegamiAssociativi().length);
											for(int i=0;i<iSWSFASCICOLO.getLegamiAssociativi().length;i++){
											  System.err.println("** Inserisco ISWSRespOA");
											  //insertISWSRespOA(iSWSFASCICOLO.getLegamiAssociativi(i),idChiamataServ2); 
											}  
										  }
										  
										  // ISWSLavoro : Manodopera alla data di riferimento (array di ISWSLavoro)
										  if(iSWSFASCICOLO.getManodopera() != null && iSWSMessaggi.getManodopera().getCodice().equals("012")){
										    System.out.println("iSWSFASCICOLO.getManodopera().length ="+iSWSFASCICOLO.getManodopera().length);
											for(int i=0;i<iSWSFASCICOLO.getManodopera().length;i++){
											  System.err.println("** Inserisco ISWSLavoro");
											  //insertManodopera(iSWSFASCICOLO.getManodopera(i),idChiamataServ2); 
											}  
										  }
										  // ISWSMacchina : Mezzi di produzione alla data di riferimento (array di ISWSMacchina)
										  if(iSWSFASCICOLO.getMezziProduzione() != null && iSWSMessaggi.getMezziProduzione().getCodice().equals("012")){
										    System.out.println("iSWSFASCICOLO.getMezziProduzione().length ="+iSWSFASCICOLO.getMezziProduzione().length);
											for(int i=0;i<iSWSFASCICOLO.getMezziProduzione().length;i++){
											  System.err.println("** Inserisco ISWSMacchina");
											  //insertMezziProduzione(iSWSFASCICOLO.getMezziProduzione(i),idChiamataServ2); 
											}   
										  }
										  // ISWSRespOTE : Orientamento tecnico-economico di un’azienda agricola 
										  if(iSWSFASCICOLO.getOTE() != null && iSWSMessaggi.getOTE().getCodice().equals("012")){
										    System.out.println("iSWSFASCICOLO.getOTE().length ="+iSWSFASCICOLO.getOTE().length);
											for(int i=0;i<iSWSFASCICOLO.getOTE().length;i++){
											  System.err.println("** Inserisco ISWSRespOTE");
											  //insertISWSRespOTE(iSWSFASCICOLO.getOTE(i),idChiamataServ2); 
											}  
										  }
										  //if(iSWSFASCICOLO.getSegnalazioni())
								      }
								  }
							  }							 							  							  
						    }
						  } 
						  else{
						    System.err.println("E' già stata effettuata una chiamata al servizio esitoFascicoloAllAsync con idXml ="+idXml+", non si deve ripetere la chiamata");	  
					      }
					   }												
					}
				}
		} 
		catch (Exception e) {
			System.out.println("Eccezione in fase di chiamata dei servizi SIAN");			
			e.printStackTrace();
		} 
		finally {
			try {
				System.out.println(" -- chiudo la connessione");
				shutdown();
				stopWatch.dumpElapsed("LeggiFascicoloAll", "LeggiFascicoloAll",
						"Servizio LeggiFascicoloAll", "# ritorno: "
								+ (fascicoloAllResponse != null ? "null"
										: "non null"));
				stopWatch.stop();

				long timeEnd = System.currentTimeMillis() - a;
				System.err.println(" Time for operation LeggiFascicoloAll = "+ timeEnd);

				// long minutes = TimeUnit.MILLISECONDS.toMinutes(timeEnd);
				// System.err.println(" -- minutes ="+minutes);
				long seconds = TimeUnit.MILLISECONDS.toSeconds(timeEnd);
				System.err.println(" -- seconds =" + seconds);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
    private static void insertISWSRespAnagFascicolo15(ISWSRespAnagFascicolo15 iSWSRespAnagFascicolo15, Long idChiamataServ)throws Exception{
    /*	Connection conn = null;
	    PreparedStatement stmt = null;	    	  
	    String query ="";
	    try{	  
	      conn = createConnection();
	    	  
	       // Inserimento in ISWSRESPANAGFASCICOLO15
	       query = "INSERT INTO BAS.ISWSRESPANAGFASCICOLO15 ( "+
	    		   			"CODICETIPODETENTORE, COMUNENASCITAPF,"+ 
	    		   			"CUAA, DATAAPERTURAFASCICOLO, DATACHIUSURAFASCICOLO,"+ 
	    		   			"DATADOCUMENTO, DATAELABORAZIONE, DATANASCITAPF,"+ 
	    		   			"DATASCADDOCUMENTO, DATASCHEDAVALIDAZIONE, DATASOTTMANDATO,"+ 
	    		   			"DATAVALIDAZFASCICOLO, DENOMINAZIONE, DESCPEC,"+ 
	    		   			"DETENTORE, EMAIL, NOMEPF,"+ 
	    		   			"NUMERODOCUMENTO, ORGANISMOPAGATORE, SCHEDAVALIDAZIONE,"+ 
	    		   			"SESSOPF, TIPOAZIENDA, TIPODOCUMENTO,"+ 
	    		   			"SEDERESIDENZA, ID_ISWS_ISCRIZIONE_REA, ID_ISWS_ISCRIZIONE_REGIMPRESE,"+ 
	    		   			"ID_ISWS_CODICE_INPS, RECAPITO, ID_CHIAMATA) "+ 
	    		   	"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    	  
	    	conn = createConnection();
		    System.out.println("-- insert in ISWSRESPANAGFASCICOLO15 ="+query);
		    stmt = conn.prepareStatement(query);
		      
		    int indice = 0;		
		   
		    System.out.println("-- idChiamata ="+idChiamataServ);
		    stmt.setString(++indice, iSWSRespAnagFascicolo15.getTipoDetentore().getValue());
		    
		    
		    stmt.setLong(++indice, idChiamataServ);
		    stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
		     		    
		    stmt.executeUpdate();	    	  	    	  
	          
	    }
	    catch (Exception e){
	      System.err.println("Exception in insertSianISWSMessaggiRMsg ="+e.getMessage());
	      e.printStackTrace();
	      throw e;
	    }
	    finally{
	      shutdown();	 	      
	    }	*/
    }
	
	private static void inserimentoISWSMessaggi(Msg msg, String nome, Long idChiamata) throws Exception{
		Long idMsgInserito = insertSianMsg(msg, nome);
		insertSianISWSMessaggiRMsg(idMsgInserito, idChiamata);
	}
	
	
	private static Long getChiamataServizioByIdXml(String idXml) throws Exception{
		String query = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;	
	    Long idChiamata = null;
	    try{	      	  
	      query = "SELECT ID_CHIAMATA FROM SIAN_CHIAMATA_SERVIZIO WHERE ID_XML_INPUT = ?";
	  
	      conn = createConnection();	      
	      System.out.println("-- query getChiamataServizioByIdXml ="+query);
	      System.out.println("-- idXml = "+idXml);
	      stmt = conn.prepareStatement(query);
	      
	      int indice = 0;
	      stmt.setString(++indice, idXml);
	      	      	 
	      ResultSet rs = stmt.executeQuery();
	      
	      while (rs.next()){
	        idChiamata = rs.getLong("ID_CHIAMATA");
	      }
	    }
	    catch (Exception ex){
	      System.out.println("Exception in getChiamataServizioByIdXml ="+ex);
	      throw ex;
	    }
	    finally{
	      shutdown();	    
	    }	  
	    return idChiamata;
	}
	
	
	private static void updateChiamataServizio(SianChiamataServizioVO chiamataServOutput) throws Exception{
		String query = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;		    
	    try{	      	  
	      query = "UPDATE SIAN_CHIAMATA_SERVIZIO " 
	            + "SET COD_RET_OUTPUT = ?, SEGNALAZIONE_OUTPUT = ?, ID_XML_OUTPUT = ? "
	            + "WHERE "
	            + "ID_CHIAMATA = ?  ";
	  
	      conn = createConnection();	      
	      System.out.println("-- query updateChiamataServizio ="+query);
	      System.out.println("-- idChiamata = "+chiamataServOutput.getIdChiamata());
	      stmt = conn.prepareStatement(query);
	      
	      int indice = 0;
	      stmt.setString(++indice, chiamataServOutput.getCodRetOutput());
	      stmt.setString(++indice, chiamataServOutput.getSegnalazioneOutput());
	      stmt.setString(++indice, chiamataServOutput.getIdXmlOutput());
	      stmt.setLong(++indice, chiamataServOutput.getIdChiamata());
	      	  
	      stmt.executeUpdate();
	    }
	    catch (Exception ex){
	      System.out.println("Exception in updateChiamataServizio ="+ex);
	      throw ex;
	    }
	    finally{
	      shutdown();	    
	    }	    
	}
	
	
	private static void insertSianISWSMessaggiRMsg(Long idMsg, Long idChiamata) throws Exception{		
	    Connection conn = null;
	    PreparedStatement stmt = null;	    	  
	    String query ="";
	    try{	  
	      conn = createConnection();
	    	  
	       // Inserimento in SIAN_ISWSMESSAGGI_R_MSG
	       query = "INSERT INTO SIAN_ISWSMESSAGGI_R_MSG "+
	    			  "(ID_MSG,"+
	    			  "ID_CHIAMATA,"+	    			  
	    			  "DATA_INSERIMENTO) "+
	    			  "VALUES "+
	    			  "(?, ?, ?)";
	    	  
	    	conn = createConnection();
		    System.out.println("-- insert in SIAN_ISWSMESSAGGI_R_MSG ="+query);
		    stmt = conn.prepareStatement(query);
		      
		    int indice = 0;		
		    System.out.println("-- idMsg ="+idMsg);
		    stmt.setLong(++indice, idMsg);
		    System.out.println("-- idChiamata ="+idChiamata);
		    stmt.setLong(++indice, idChiamata);
		    stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
		     		    
		    stmt.executeUpdate();	    	  	    	  
	          
	    }
	    catch (Exception e){
	      System.err.println("Exception in insertSianISWSMessaggiRMsg ="+e.getMessage());
	      e.printStackTrace();
	      throw e;
	    }
	    finally{
	      shutdown();	 	      
	    }	    
	}
	
	private static Long insertSianMsg(Msg messaggio, String nome) throws Exception{		
	    Connection conn = null;
	    PreparedStatement stmt = null;	    	  
	    Long idMsg = null;
	    try{	  
	
	      if(messaggio != null){
	        String queryMsg = "";	    	
	    	if(messaggio != null){
	    	  conn = createConnection();
	    	  
	    	  // Inserimento in SIAN_MSG
	    	  queryMsg = "INSERT INTO SIAN_MSG "+
	    			  "(ID_MSG, "+
	    	  		  "CODICE,"+
	    			  "DESCRIZIONE,"+
	    			  "NOME,"+
	    			  "DATA_INSERIMENTO) "+
	    			  "VALUES "+
	    			  "(?, ?, ?, ?, ?)";
	    	  
	    	  conn = createConnection();
		      System.out.println("-- insert in SIAN_MSG ="+queryMsg);
		      stmt = conn.prepareStatement(queryMsg);
		      
		      int indice = 0;
		      idMsg = getNextPrimaryKey("SEQ_SIAN_MSG");
		      System.out.println("- inserimento ID_MSG ="+idMsg);
		      stmt.setLong(++indice, idMsg);
		      stmt.setString(++indice, messaggio.getCodice());
		      stmt.setString(++indice, messaggio.getDescrizione());	 
		      System.out.println("-- nome ="+nome);
		      stmt.setString(++indice, nome);
		      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
		          		      
		      stmt.executeUpdate();	    	  	    	  
	    	}
	      }	      
	    }
	    catch (Exception e){
	      System.err.println("Exception in insertSianMsg ="+e.getMessage());
	      e.printStackTrace();
	      throw e;
	    }
	    finally{
	    	shutdown();	 	      
	    }
	    return idMsg;
	}
	
	private static Long insertChiamataServizio(SianChiamataServizioVO chiamServ) throws Exception{
		String query = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;	    	  
	    Long idChiamata = null;
	    try{	  
	      idChiamata = getNextPrimaryKey("SEQ_SIAN_CHIAMATA_SERVIZIO");
	      query = "INSERT INTO SIAN_CHIAMATA_SERVIZIO( "+
	    		  		"ID_CHIAMATA,ID_SERVIZIO,CUAA_INPUT,DATA_CHIAMATA,COD_RET_OUTPUT,SEGNALAZIONE_OUTPUT,ID_XML_OUTPUT,ID_XML_INPUT) "+
	    		  "VALUES "+
	    		  	"(?,?,?,?,?,?,?,?)";		
	     
	      conn = createConnection();
	      System.out.println("-- query insertChiamataServizio ="+query);
	      stmt = conn.prepareStatement(query);
	      
	      int indice = 0;
	      stmt.setLong(++indice, idChiamata);
	      System.err.println("*** inserisco id_chiamata ="+idChiamata);
	      System.out.println("-- idServizio ="+chiamServ.getIdServizio());
	      stmt.setLong(++indice, chiamServ.getIdServizio());
	      stmt.setString(++indice, chiamServ.getCuaaInput());	      
	      stmt.setTimestamp(++indice, convertDateToTimestamp(chiamServ.getDataChiamata()));	      
	      stmt.setString(++indice, chiamServ.getCodRetOutput());
	      stmt.setString(++indice, chiamServ.getSegnalazioneOutput());
	      stmt.setString(++indice, chiamServ.getIdXmlOutput());
	      stmt.setString(++indice, chiamServ.getIdXmlInput());	    
	      
	      stmt.executeUpdate();
	      
	      return idChiamata;
	    }
	    catch (Exception e){
	      System.err.println("Exception in insertChiamataServizio ="+e.getMessage());	
	      e.printStackTrace();
	      throw e;
	    }
	    finally{
	    	shutdown();	 	      
	    }
	}
	
	
   public static java.sql.Timestamp convertDateToTimestamp(java.util.Date val){
     if (val == null)
	   return null;
	 return new Timestamp(val.getTime());
   }
	
	public static Long getNextPrimaryKey(String sequenceName) throws DataAccessException
	  {
	    Long primaryKey = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    try
	    {
	      conn = createConnection();

	      String search = "SELECT " + sequenceName + ".NEXTVAL " + "  FROM DUAL ";
	      stmt = conn.prepareStatement(search);

	      System.out.println("Searching new Primary Key: " + search);

	      ResultSet rs = stmt.executeQuery();

	      if (rs != null)
	      {
	        while (rs.next())
	        {
	          primaryKey = new Long(rs.getLong(1));
	        }
	      }
	      else
	        throw new DataAccessException();

	      rs.close();
	      stmt.close();

	      System.out.println("Found new Primary Key: " + primaryKey);
	    }
	    catch (SQLException exc)
	    {
	      //SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
	      throw new DataAccessException(exc.getMessage());
	    }
	    catch (DataAccessException daexc)
	    {
	      //SolmrLogger.fatal(this, "ResultSet null");
	      throw daexc;
	    }
	    catch (Exception ex)
	    {
	      //SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
	      throw new DataAccessException(ex.getMessage());
	    }
	    finally
	    {
	      try
	      {
	        if (stmt != null)
	          stmt.close();
	        if (conn != null)
	          conn.close();
	      }
	      catch (SQLException exc)
	      {
	        /*SolmrLogger.fatal(this,
	            "SQLException while closing Statement and Connection: "
	                + exc.getMessage());*/
	        throw new DataAccessException(exc.getMessage());
	      }
	      catch (Exception ex)
	      {
	        /*SolmrLogger.fatal(this,
	            "Generic Exception while closing Statement and Connection: "
	                + ex.getMessage());*/
	        throw new DataAccessException(ex.getMessage());
	      }
	    }
	    return primaryKey;
	  }
	
	private static BasicHttpBinding_WSFascicoloAllServiceStub getBinding(String nomeServizio) throws Exception{
		FascicoloAllLocator locator = new FascicoloAllLocator();
		BasicHttpBinding_WSFascicoloAllServiceStub binding = null;
		
		try {
			// -- Url Prod TOBECONFIG
			//System.out.println("-- CHIAMATA ALLA URL DI PRODUZIONE");
			//locator.setEndpointAddress("BasicHttpBinding_IWSFascicoloAllService", "https://cooperazione.sian.it/wsTOAST/services/FascicoloAll");
			
			// -- Url di Test TOBECONFIG
			System.err.println("-- CHIAMATA ALLA URL DI TEST");
			locator.setEndpointAddress("BasicHttpBinding_IWSFascicoloAllService", "https://cooptest.sian.it/wsTOAST/services/FascicoloAll"); 
			
		} 
		catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			binding = (BasicHttpBinding_WSFascicoloAllServiceStub) locator.getBasicHttpBinding_IWSFascicoloAllService();
		} 
		catch (ServiceException e) {			
			e.printStackTrace();
		}

		binding.setTimeout(250000);

		SOAPAutenticazione sOAPAutenticazione = new SOAPAutenticazione();

		// -- Autenticazione Test TOBECONFIG		
		sOAPAutenticazione.setUsername("regiTOBECONFIG");
		sOAPAutenticazione.setPassword("1regiTOBECONFIG1");
		

		// -- Autenticazione Prod TOBECONFIG
		//sOAPAutenticazione.setUsername("wspddbasi09joke");
		//sOAPAutenticazione.setPassword("jekyll23");
		
		sOAPAutenticazione.setNomeServizio(nomeServizio);
		binding.setHeader("http://cooperazione.sian.it/schema/SoapAutenticazione","SOAPAutenticazione", sOAPAutenticazione);
		
		return binding;
	}
	

	// Java to Xml
	private static String jaxbObjectToXML(FascicoloAllResponse fascicoloAllResponse){
		String xmlContent = "";
        try{
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(FascicoloAllResponse.class);
             
            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 
            //Print XML String to Console
            StringWriter sw = new StringWriter();
             
            //Write XML to StringWriter
            jaxbMarshaller.marshal(fascicoloAllResponse, sw);
             
            //Verify XML Content
            xmlContent = sw.toString();            
 
        } 
        catch (JAXBException e) {
        	System.out.println("JAXBException in jaxbObjectToXML");
            e.printStackTrace();
        }
        return xmlContent;
    }
	
	private static Connection createConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// Get a connection
			// conn =
			// DriverManager.getConnection("jdbc:oracle:thin:@tst-domdb55.csi.it:1521:AGRI11T",
			// "smrgaa","mypass");
			
			// DB PRODUZIONE SMRGAA
			/*conn = DriverManager
					.getConnection(
							"jdbc:oracle:thin:@srv1-oraclesirs.hosting.int:1521/orcl.hosting.int",
							"SMRGAA", "MYPASS");*/
			
			// DB PRODUZIONE BAS
			conn = DriverManager
					.getConnection(
							"jdbc:oracle:thin:@srv1-oraclesirs.hosting.int:1521/orcl.hosting.int",
							"BAS", "MYPASS");			
			
		} 
		catch (Exception except) {
			except.printStackTrace();
		}
		return conn;
	}

	private static void shutdown() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

}
