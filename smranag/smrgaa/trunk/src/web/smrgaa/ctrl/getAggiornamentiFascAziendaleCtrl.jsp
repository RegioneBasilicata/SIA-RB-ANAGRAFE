
<%@page import="it.csi.smranag.smrgaa.dto.sianfa.SianEsitoAggFascicoloVO"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.ws.sianfa.SianEsito"%>
<%@ page import="it.csi.smranag.smrgaa.ws.sianfa.SianEsitoAggFascicolo"%>
<%@ page import="net.sf.json.JSONSerializer"%>
<%@ page import="net.sf.json.JSON"%>
<%@ page import="net.sf.json.JsonConfig"%>
<%@ page import="net.sf.json.processors.JsonValueProcessor"%>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.sianfa.*"%>

<%

	String iridePageName = "getAggiornamentiFascAziendaleCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	// ****  Ridefinisco i metodi Json per la formattazione della data
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.registerJsonValueProcessor(XMLGregorianCalendar.class, new JsonValueProcessor() {

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {    	
        Date[] dates = (Date[])value;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
        String[] result = new String[dates.length];
        for (int index = 0; index < dates.length; index++) {
        	result[index] = df.format(dates[index]);            
        }
        return result;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
    	if(value != null){
	    	XMLGregorianCalendar calendar = (XMLGregorianCalendar) value;
	    	Date date = calendar.toGregorianCalendar().getTime();
	        //Date date = (Date)value;
	        if(date != null){
	          DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
	          return df.format(date);
	        }        
	        else{
	          return "";
	        }
    	}
    	else{
    	  return "";
    	}
        	
    }

});
	
	
	
	// ****
	
	
	
	SolmrLogger.debug(this, "BEGIN getAggiornamentiFascAziendaleCtrl");
	   
	  
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagFacadeClient client= new AnagFacadeClient();
	   
	String cuaa = request.getParameter("cuaa");
	SolmrLogger.debug(this, "-- cuaa ="+cuaa);
	String valoreRitorno = "";	

	try {
		SolmrLogger.debug(this, "---- Chiamata al servizio getAggiornamentiFascicolo per avere tutti gli aggiornamenti effettuati sul fascicolo aziendale");
		SolmrLogger.debug(this, "-- cuaa in input ="+cuaa);
		SianEsito esitoAggFascicoloSian = gaaFacadeClient.getAggiornamentiFascicolo(cuaa);
		
		if(esitoAggFascicoloSian != null && esitoAggFascicoloSian.getEsitiAggFascicolo().size()>0){	
 			SolmrLogger.debug(this, "-- sono stati trovati degli aggiornamenti :"+esitoAggFascicoloSian.getEsitiAggFascicolo().size());
 			List<SianEsitoAggFascicoloVO> esitoAggFascicoloVOlist = new ArrayList<SianEsitoAggFascicoloVO>();
 			String denominazioneUtente = "";
 			// Per ogni aggiornamento trovato, cerco l'utente che ha effettuato l'aggiornamento
 			for(int i=0;i<esitoAggFascicoloSian.getEsitiAggFascicolo().size();i++){
 				if(esitoAggFascicoloSian.getEsitiAggFascicolo().get(i).getIdUtente() != null){
 					if(esitoAggFascicoloSian.getEsitiAggFascicolo().get(i).getIdUtente().longValue() != 0){ 					  
 					  Long idUtente = esitoAggFascicoloSian.getEsitiAggFascicolo().get(i).getIdUtente();
 					  SolmrLogger.debug(this, "-- idUtente ="+idUtente);
 					  SolmrLogger.debug(this, "---- Ricerca dei dati dal servizio papua");
 					  UtenteAbilitazioni utenteAbilitazioni = client.getUtenteAbilitazioniByIdUtenteLogin(idUtente);
 					  if(utenteAbilitazioni != null){
 						denominazioneUtente = utenteAbilitazioni.getNome()+" "+utenteAbilitazioni.getCognome();				
 					  }
 					  else{
 					    SolmrLogger.debug(this, "-- NON e' stato trovato UtenteAbilitazioni");
 					  } 					  					
 					} 	 				    
 				}
 			    // Mappo l'oggetto restituito dal servizio sianfa nell'oggetto che verrà utilizzato per visualizzare i dati
 			    SolmrLogger.debug(this, "-- denominazione utente ="+denominazioneUtente);
				SianEsitoAggFascicoloVO esitoAggFascicoloVO = mapObjtSianEsitoAggFascicolo(esitoAggFascicoloSian.getEsitiAggFascicolo().get(i), denominazioneUtente);
				esitoAggFascicoloVOlist.add(esitoAggFascicoloVO); 
 			}
 			
 			SianEsitoVO esitoAggFascicolo = mapObjEsitoVO(esitoAggFascicoloSian, esitoAggFascicoloVOlist); 
			
			JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(esitoAggFascicolo.getEsitiAggFascicolo().get(0).getDataInizio().getClass());
			
			// Se il campo esito è valorizzato significa che il cuaa è in fase di aggiornamento, aggiungo un oggetto alla lista con questo messaggio, in modo che venga visualizzato
			if(esitoAggFascicolo != null){
			  SolmrLogger.debug(this, "--- esito ="+esitoAggFascicolo.getEsito());
			}  
			if(esitoAggFascicolo!= null && esitoAggFascicolo.getEsito() != null && !esitoAggFascicolo.getEsito().trim().equals("")){
				SolmrLogger.debug(this, "--- il campo esito è valorizzato significa che il cuaa è in fase di aggiornamento ed il record e' stato trovato loccato, aggiungo un oggetto alla lista con questo messaggio, in modo che venga visualizzato");
				
				SianEsitoAggFascicoloVO esitoFasc = new SianEsitoAggFascicoloVO();
				esitoFasc.setIdFaseAggiornamento(new Long(2));
				esitoFasc.setCuaa(cuaa);
				esitoFasc.setDataInizio(DateUtils.convertDateToXmlGregorianCalendar(new Date()));
				esitoFasc.setEsitoAggiornamento("Fascicolo in fase di aggiornamento");	
												
				// Devo metterlo nella prima posizione della lista, in modo che venga preso come ultimo stato aggiornamento nel js
				esitoAggFascicolo.getEsitiAggFascicolo().add(0,esitoFasc);
			}
			
			valoreRitorno = JSONSerializer.toJSON(esitoAggFascicolo.getEsitiAggFascicolo(), jsonConfig).toString();
		    SolmrLogger.debug(this, "valoreRitorno : "+valoreRitorno);		      		
		}
		else{
			SolmrLogger.debug(this, "-- NON sono stati trovati degli aggiornamenti");
		}
		
		response.reset();
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(valoreRitorno);	
		response.flushBuffer();
		return;
	
	}
	catch(Exception se) {
		SolmrLogger.error(this, "-- SolmrException in fase di chiamata a getAggiornamentiFascicolo ="+se.getMessage());
		
		response.reset();		
		response.getWriter().write("failed");	
		response.flushBuffer();
		SolmrLogger.debug(this, "  END getAggiornamentiFascAziendaleCtrl");		
	}
	finally{
	  SolmrLogger.debug(this, "END getAggiornamentiFascAziendaleCtrl");
	}
%>
<%!
 private SianEsitoAggFascicoloVO mapObjtSianEsitoAggFascicolo(SianEsitoAggFascicolo esitoAggFascicoloVO, String denominazUtente) throws Exception{
   SolmrLogger.debug(this, "BEGIN mapObjtSianEsitoAggFascicolo");	
   
   SianEsitoAggFascicoloVO esitoAggFascVO = new SianEsitoAggFascicoloVO();
   if(esitoAggFascicoloVO != null){
	   esitoAggFascVO.setCodErrorePl(esitoAggFascicoloVO.getCodErrorePl());
	   esitoAggFascVO.setCuaa(esitoAggFascicoloVO.getCuaa());
	   esitoAggFascVO.setDataFine(esitoAggFascicoloVO.getDataFine());
	   esitoAggFascVO.setDataInizio(esitoAggFascicoloVO.getDataInizio());
	   esitoAggFascVO.setDescrErrorePl(esitoAggFascicoloVO.getDescrErrorePl());
	   esitoAggFascVO.setEsitoAggiornamento(esitoAggFascicoloVO.getEsitoAggiornamento());
	   esitoAggFascVO.setIdChiamata(esitoAggFascicoloVO.getIdChiamata());
	   esitoAggFascVO.setIdFaseAggiornamento(esitoAggFascicoloVO.getIdFaseAggiornamento());
	   esitoAggFascVO.setIdUtente(esitoAggFascicoloVO.getIdUtente());
	   esitoAggFascVO.setSegnalazioneOutputServizio(esitoAggFascicoloVO.getSegnalazioneOutputServizio());
	   esitoAggFascVO.setUtente(denominazUtente);
   }
   SolmrLogger.debug(this, "END mapObjtSianEsitoAggFascicolo");
   return esitoAggFascVO;
 }

 private SianEsitoVO mapObjEsitoVO(SianEsito esito, List<SianEsitoAggFascicoloVO> esitoAggFascicoloVOlist) throws Exception{
	 SolmrLogger.debug(this, "BEGIN mapObjEsitoVO");
	 SianEsitoVO sianEsitoVO = new SianEsitoVO();
	 if(esito != null){
	   sianEsitoVO.setEsito(esito.getEsito());
	   sianEsitoVO.setEsitiAggFascicolo(esitoAggFascicoloVOlist);
	 }
	 SolmrLogger.debug(this, "END mapObjEsitoVO");
	 return sianEsitoVO;
 }






















%>
	

  

