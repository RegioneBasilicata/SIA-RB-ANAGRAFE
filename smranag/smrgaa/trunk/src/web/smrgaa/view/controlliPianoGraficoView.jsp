<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/controlliPianoGrafico.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  String idAccessoPianoGrafico = request.getParameter("idAccessoPianoGrafico");
  String pathToFollow = (String)session.getAttribute("pathToFollow");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
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
  
 
  
  try
  {
    
    if(Validator.isNotEmpty(idAccessoPianoGrafico) && Validator.isNotEmpty(idDichiarazioneConsistenza))
    {
      EsitoPianoGraficoVO esitoPianoGraficoVO = anagFacadeClient.getEsitoPianoGraficoFromAccesso(
        new Long(idAccessoPianoGrafico));
      if(Validator.isNotEmpty(esitoPianoGraficoVO))
      {
        if("S".equalsIgnoreCase(esitoPianoGraficoVO.getFlagEseguiControlli()))
        {
	        PlSqlCodeDescription plCode = anagFacadeClient.controlliValidazionePlSql(
	          anagAziendaVO.getIdAzienda().longValue(), 11, ruoloUtenza.getIdUtente(), 
	            new Long(idDichiarazioneConsistenza));
	            
	        if (plCode==null) 
		      {
		        throw new Exception("pl null");
		      }
		      else if (plCode.getDescription()!=null) 
		      {
		        throw new Exception(plCode.getDescription());
		      }
		    }
      
        htmpl.newBlock("blkEsitoTastino");
        String messaggio = esitoPianoGraficoVO.getMessaggio();
        if(Validator.isNotEmpty(esitoPianoGraficoVO.getMessaggioErrore()))
          messaggio += "-"+esitoPianoGraficoVO.getMessaggioErrore();
        htmpl.set("blkEsitoTastino.messaggio", messaggio);
        if(Validator.isNotEmpty(esitoPianoGraficoVO.getEtichettaPulsante()))
        {
          htmpl.newBlock("blkEsitoTastino.blkTastino");
          //htmpl.set("blkEsitoTastino.blkTastino.linkDaPassare", esitoPianoGraficoVO.getLink());
          //htmpl.set("blkEsitoTastino.blkTastino.cuaa", anagAziendaVO.getCUAA());
          htmpl.set("blkEsitoTastino.blkTastino.labelTastino", esitoPianoGraficoVO.getEtichettaPulsante());
        }
      }
    
  
  
    
	    Vector<ErrAnomaliaDicConsistenzaVO> vErroriAnomalie = anagFacadeClient.getErroriAnomalieDichConsPG(
	      new Long(idDichiarazioneConsistenza), 10);
	    if(Validator.isNotEmpty(vErroriAnomalie))
	    {
			  String descAnomaliaOld="";
	      for(int i=0;i<vErroriAnomalie.size();i++)
	      {
	        ErrAnomaliaDicConsistenzaVO err = vErroriAnomalie.get(i);
	        if (!descAnomaliaOld.equals(err.getDescGruppoControllo()))
	        {
	          descAnomaliaOld=err.getDescGruppoControllo();
	          htmpl.newBlock("blkTabAnomalia");
	          htmpl.set("blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
	        }
	        htmpl.newBlock("blkTabAnomalia.blkAnomalia");
	        htmpl.set("blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
	        htmpl.set("blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
	        if(Validator.isNotEmpty(err.getIdDichiarazioneSegnalazione()))
	        {
		        if (err.isBloccante())
		        {
		          //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Bloccante.gif");
		          // Reperisco l'immagine da ANDROMEDA
		          htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
		          htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
		        }
		        else
		        {
		          //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Warning.gif");
		          // Reperisco l'immagine da ANDROMEDA
		          htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
		          htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
		        }
		      }
		      else
		      {
		        htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "ok.gif");
            htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine", "Nessuna anomalia");
		      }
	      }
		  
      }
		
	  }
	  
	  
	    
	  
  }
	      
  catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
  
  
%>
<%= htmpl.text()%>
