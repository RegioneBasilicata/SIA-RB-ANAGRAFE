package it.csi.solmr.util;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: Report dinamici</p>
 * <p>Description: Stampa funzionalità base LayoutWriter</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: CSI Piemonte</p>
 * @author Borgogno Claudio
 * @version 1.0
 */

public class PrintBaseLayout implements it.csi.smrcomms.reportdin.interfacecsi.LayoutInterface
{

  public void writeLayout(Htmpl htmpl, HttpServletRequest request)
      throws Exception
  {
    HttpSession session=request.getSession();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    String pathToFollow = (String)session.getAttribute("pathToFollow");

	SolmrLogger.debug(this,"[PrintBaseLayout::writeLayout] BEGIN.");
	
	SolmrLogger.debug(this,"-- pathToFollow ="+pathToFollow);

    if(ruoloUtenza != null)
    {
      SolmrLogger.debug(this,"-- ruoloUtenza != null");
      //Utente
      htmpl.set("utente", ruoloUtenza.getDenominazione());
      htmpl.set("ente", ruoloUtenza.getDescrizioneEnte());
      //Banner
      // Utente provinciale
      if(ruoloUtenza.isUtenteProvinciale()) {
              htmpl.bset("pathProvincia", "_" + StringUtils.getProvinciaByDescrizioneIride2(ruoloUtenza.getProvincia()).toLowerCase());
              //htmpl.set("pathProvincia", "_" + ruoloUtenza.getCodiceEnte());
      }
      // Utente regionale
      else if(ruoloUtenza.isUtenteRegionale()) {
              if(pathToFollow != null && pathToFollow.equalsIgnoreCase((String)SolmrConstants.PATH_TO_FOLLOW_RUPAR)) {
                      htmpl.bset("pathProvincia", "_regione");
              }
              else if (pathToFollow != null && pathToFollow.equalsIgnoreCase((String)SolmrConstants.PATH_TO_FOLLOW_SISTEMAPIEMONTE)) {
                      htmpl.bset("pathProvincia", "");
              }
              else if(pathToFollow != null && pathToFollow.equalsIgnoreCase((String)SolmrConstants.PATH_TO_FOLLOW_TOBECONFIG)){
            	  htmpl.bset("pathProvincia", "");
              }
              //htmpl.set("pathProvincia", "_" + ruoloUtenza.getIstatRegione());
      }
      // Comunità montana
      else if(ruoloUtenza.isUtenteComunitaMontana()) {
              htmpl.bset("pathProvincia", "_regione");
      }
      // Intermediario(Utenti CAA)
      else if(ruoloUtenza.isUtenteIntermediario()) {
              htmpl.bset("pathProvincia", "");

      }
      // Profilo Azienda agricola
      else if(ruoloUtenza.isUtenteNonIscrittoCIIA()) {
              htmpl.bset("pathProvincia", "");
              // Chiedere al dominio il corretto funzionamento per il riconoscimento della regione di appartenenza dell'azienda
              // PROV_COMPETENZA.
      }
      // Assistenza CSI
      else if(ruoloUtenza.isUtenteAssistenzaCsi() || ruoloUtenza.isUtenteServiziAgri()) {
              // Lo gestico solo ed esclusivamente in funzione del PIEMONTE e del CSI dal momento che si tratta di un utente specifico
              htmpl.bset("pathProvincia", "_regione");
      }
      // Utente OPR
      else if(ruoloUtenza.isUtenteOPR() || ruoloUtenza.isUtenteOPRGestore()) {
              // Lo setto in modo errato dal momento che non abbiamo ancora il banner a disposizione
              htmpl.bset("pathProvincia", "_opr");            
      }
      // Utente titolare o rappresentante legale
      else if(ruoloUtenza.isUtenteTitolareCf() || ruoloUtenza.isUtenteLegaleRappresentante()) {
              htmpl.bset("pathProvincia", "");             
      }
      else if(ruoloUtenza.isUtenteASL() || ruoloUtenza.isUtenteGuardiaFinanza() || ruoloUtenza.isUtenteInps()) {
              htmpl.bset("pathProvincia", "");
      }
      // Utente comune
      else if(ruoloUtenza.isUtenteComunale()) {
              htmpl.bset("pathProvincia", "_regione");
      }
    }
    

    PageCache cp=(PageCache)session.getServletContext().getAttribute("cp");

    String head= "";
    String header= "";
    String footer  = "";
    String headMenuScroll = "";
    //it.csi.jsf.htmpl.Htmpl htmplTemp = null;
    //String pathErrori = null;
    String activeSite = null;

    // Effettuo tutte le operazioni di recupero immagini solo se la sessione è attiva
    if(it.csi.solmr.util.Validator.isNotEmpty(pathToFollow))
    {
      // Parametro che arriva dalla pagina grafica di RUPAR o SISTEMA PIEMONTE
      activeSite =  (String)session.getAttribute("activeSite");

      if(!it.csi.solmr.util.Validator.isNotEmpty(activeSite))
      {
        if(pathToFollow.equalsIgnoreCase("rupar"))
        {
          activeSite = session.getServletContext().getInitParameter("immaginiSviluppoRupar");
          head = session.getServletContext().getInitParameter("headRupar");
          header = session.getServletContext().getInitParameter("headerRupar");
          footer = session.getServletContext().getInitParameter("footerRupar");
          //pathErrori = session.getServletContext().getInitParameter("erroriRupar");
          headMenuScroll = session.getServletContext().getInitParameter("headMenuScrollRupar");
        }
        else if(pathToFollow.equalsIgnoreCase("sispie"))
        {
          activeSite = session.getServletContext().getInitParameter("immaginiSviluppoSispie");
          head = session.getServletContext().getInitParameter("headSispie");
          header = session.getServletContext().getInitParameter("headerSispie");
          footer = session.getServletContext().getInitParameter("footerSispie");
          //pathErrori = session.getServletContext().getInitParameter("erroriSispie");
          headMenuScroll = session.getServletContext().getInitParameter("headMenuScrollSispie");
        }
        else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
        	activeSite = session.getServletContext().getInitParameter("immaginiSviluppoTOBECONFIG");
            head = session.getServletContext().getInitParameter("headTOBECONFIG");
            header = session.getServletContext().getInitParameter("headerTOBECONFIG");
            footer = session.getServletContext().getInitParameter("footerTOBECONFIG");
            headMenuScroll = session.getServletContext().getInitParameter("headMenuScrollTOBECONFIG");  
        }
        	
      }
      
      try
      {   
        String urlHead = activeSite + head;
        SolmrLogger.debug(this, "[PrintBaseLayout::writeLayout] urlHead: "+urlHead);
        head = cp.requestRemotePage(urlHead, session.getServletContext());
        String urlHeader = activeSite + header;
        SolmrLogger.debug(this, "[PrintBaseLayout::writeLayout] urlHeader: "+urlHeader);
        header = cp.requestRemotePage(urlHeader, session.getServletContext());
        String urlFooter = activeSite + footer;
        SolmrLogger.debug(this, "[PrintBaseLayout::writeLayout] urlFooter: "+urlFooter);
        footer = cp.requestRemotePage(urlFooter, session.getServletContext());
        headMenuScroll = activeSite + headMenuScroll;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }

      // permette la propagazione dell'oggetto cp tra i server del cluster
      session.getServletContext().setAttribute("cp", cp);

      /*
      java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(footer.getBytes());
      htmplTemp = new it.csi.jsf.htmpl.Htmpl(bais);
      footer = htmplTemp.text();
      */
    }

    // Nuova gestione fogli di stile
    htmpl.set("head", head, null);
    htmpl.set("header", header, null);
    htmpl.set("footer", footer, null);


    java.util.HashMap<?,?> iride2mappings=(java.util.HashMap<?,?>)session.getServletContext().getAttribute("iride2mappings");
    String iridePageNameForCU = "pagineRPDServlet";
    it.csi.solmr.presentation.security.Autorizzazione autorizzazione = (it.csi.solmr.presentation.security.Autorizzazione) iride2mappings.get(iridePageNameForCU);
    if(autorizzazione != null)
       autorizzazione.writeMenu(htmpl, request);
    
  

		SolmrLogger.debug(this,"[PrintBaseLayout::writeLayout] END.");
  }


}
