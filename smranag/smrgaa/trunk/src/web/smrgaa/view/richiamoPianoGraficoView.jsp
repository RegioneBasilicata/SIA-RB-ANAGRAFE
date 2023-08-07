<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/richiamoPianoGrafico.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Vector<ConsistenzaVO> vElencoDichiarazioni = (Vector<ConsistenzaVO>)request.getAttribute("vElencoDichiarazioni");  
  String idDichiarazioneConsistenzaStr = request.getParameter("idDichiarazioneConsistenza");
  //serve per settare correttamente la combo in caso di tasto aggiorna...
  String idDichiarazioneConsistenzaStrConfronto = null;
  //String parametroUrlPianoGrafico = (String)request.getAttribute("parametroUrlPianoGrafico");
  Vector<ErrAnomaliaDicConsistenzaVO> vErroriAnomalie = (Vector<ErrAnomaliaDicConsistenzaVO>)request.getAttribute("vErroriAnomalie");
  EsitoPianoGraficoVO esitoPianoGraficoVO = (EsitoPianoGraficoVO)request.getAttribute("esitoPianoGraficoVO");
  String operazione = request.getParameter("operazione");
  Long idDichiarazioneConsistenza = null;
  String codiceUtilita = null;
  if(Validator.isNotEmpty(idDichiarazioneConsistenzaStr))
  {
    StringTokenizer strToken = new StringTokenizer(idDichiarazioneConsistenzaStr, "_");
    idDichiarazioneConsistenza = new Long(strToken.nextToken());
    codiceUtilita = strToken.nextToken();
    
    idDichiarazioneConsistenzaStrConfronto = idDichiarazioneConsistenza+"_"+codiceUtilita;
    
  }
 	
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) 
 	{
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
 	{ 
 	  
 		
 		if(vElencoDichiarazioni != null) 
 		{
 			htmpl.newBlock("blkDati");
			
 			for(int i = 0; i < vElencoDichiarazioni.size(); i++) 
 			{
 				ConsistenzaVO consistenzaVO = vElencoDichiarazioni.get(i);
 				htmpl.newBlock("blkDati.blkElencoDichiarazione");
 				String idDichiarazioneConsistenzaTmp = consistenzaVO.getIdDichiarazioneConsistenza()
 				   +"_"+consistenzaVO.getCodiceUtilita()
 				   +"_"+consistenzaVO.getIdAccessoPianoGrafico();
 				String idDichiarazioneConsistenzaTmpConfronto = consistenzaVO.getIdDichiarazioneConsistenza()
           +"_"+consistenzaVO.getCodiceUtilita();
 				htmpl.set("blkDati.blkElencoDichiarazione.idDichiarazioneConsistenza", idDichiarazioneConsistenzaTmp);
 				String descrizione = DateUtils.formatDateTimeNotNull(consistenzaVO.getDataInserimentoDichiarazione())
 				  +" - "+consistenzaVO.getTipoMotivoDichiarazioneVO().getDescrizione();
 				
 				htmpl.set("blkDati.blkElencoDichiarazione.descrizione", descrizione);
 				
 				if(Validator.isNotEmpty(idDichiarazioneConsistenzaStrConfronto))
 				{
 				  if(idDichiarazioneConsistenzaStrConfronto.equalsIgnoreCase(idDichiarazioneConsistenzaTmpConfronto))
 				  {
 				    htmpl.set("blkDati.blkElencoDichiarazione.selected", "selected=\"selected\"", null);
 				  }
 				}
        
        
 			}
 			
 			if(Validator.isNotEmpty(operazione) && !operazione.equalsIgnoreCase("lancio"))
 			{
 			  htmpl.newBlock("blkDati.blkLancioPG"); 			
 			}
 			if(Validator.isNotEmpty(operazione) && !operazione.equalsIgnoreCase("refresh"))
      {
        if(esitoPianoGraficoVO.getIdEsitoGrafico().compareTo(new Long(4)) == 0)
          htmpl.newBlock("blkDati.blkLancioPG");  
      }
      
      
      if(vErroriAnomalie != null)
      {
      
        String percorsoErrori = null;
		if(pathToFollow.equalsIgnoreCase("rupar")) {
		  percorsoErrori = "/css_rupar/agricoltura/im/";
		}
		else if(pathToFollow.equalsIgnoreCase("sispie")) {
	  	    percorsoErrori = "/css/agricoltura/im/";
	  	}
	  	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	  	    percorsoErrori="/css/agricoltura/im/";  
	  	}
      
        String descAnomaliaOld="";
        for(int i=0;i<vErroriAnomalie.size();i++)
        {
          ErrAnomaliaDicConsistenzaVO err = vErroriAnomalie.get(i);
          if (!descAnomaliaOld.equals(err.getDescGruppoControllo()))
          {
            descAnomaliaOld=err.getDescGruppoControllo();
            htmpl.newBlock("blkDati.blkTabAnomalia");
            htmpl.set("blkDati.blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
          }
          htmpl.newBlock("blkDati.blkTabAnomalia.blkAnomalia");
          htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
          htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
          if(Validator.isNotEmpty(err.getIdDichiarazioneSegnalazione()))
          {
            if (err.isBloccante())
            {
              //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Bloccante.gif");
              // Reperisco l'immagine da ANDROMEDA
              htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
              htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
            }
            else
            {
              //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Warning.gif");
              // Reperisco l'immagine da ANDROMEDA
              htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
              htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
            }
          }
          else
          {
            htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "ok.gif");
            htmpl.set("blkDati.blkTabAnomalia.blkAnomalia.descImmagine", "Nessuna anomalia");
          }
        }
      
      }
      
      
      if(Validator.isNotEmpty(esitoPianoGraficoVO))
      {
        htmpl.newBlock("blkDati.blkTabMessaggioEsito");
        String messaggio = esitoPianoGraficoVO.getMessaggio();
        if(Validator.isNotEmpty(esitoPianoGraficoVO.getMessaggioErrore()))
          messaggio += "-"+esitoPianoGraficoVO.getMessaggioErrore();
        htmpl.set("blkDati.blkTabMessaggioEsito.messaggio", messaggio);
      }
 			
 			
 			//htmpl.set("blkDati.linkDaPassare", parametroUrlPianoGrafico);
      //htmpl.set("blkDati.cuaa", anagAziendaVO.getCUAA());
 		}
 		
 	}
  
 

%>
<%= htmpl.text()%>
