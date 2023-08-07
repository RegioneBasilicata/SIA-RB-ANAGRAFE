  <%@page import="it.csi.solmr.etc.profile.AgriConstants"%>
<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  java.io.InputStream layout = application.getResourceAsStream("/layout/scegliATECOAeep.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");


  Azienda impresaInfoc = null;
  Vector<CodeDescription> tipiAttivitaATECO = null;
  TreeMap<String,CodeDescription> treeAteco = new TreeMap<String,CodeDescription>();
  try
  {
    impresaInfoc = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());

    /* Se viene restituito null segnalo che non è stata trovata nessuna anagrafica
       Non dovrebbe accadere perchè AAEP se non trova niente dovrebbe rilanciare un
       eccezione.
    */
    if (impresaInfoc==null) 
      throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));

    List<ListaSedi> listaSede = impresaInfoc.getListaSedi();

    if (listaSede!=null)
    {
      for(int i=0;i<listaSede.size();i++)
      {
        Sede sedeInfocamere = anagFacadeClient.cercaPuntualeSede(impresaInfoc.getCodiceFiscale().getValue(), listaSede.get(i).getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
        
        List<AtecoRI2007Infoc> ateco2007RI = sedeInfocamere.getListaAtecoRI2007Infoc();
        if(ateco2007RI != null && ateco2007RI.size() > 0)
        {
          for(int g=0;g<ateco2007RI.size();g++)
          {
            AtecoRI2007Infoc ateco2007 = ateco2007RI.get(g);
            if(ateco2007.getCodAteco2007().getValue() != null)
            {
              CodeDescription cdTipologia = anagFacadeClient.getAttivitaATECObyCode(ateco2007.getCodAteco2007().getValue().trim());
              if(cdTipologia != null)
              {
                cdTipologia.setSecondaryCode(ateco2007.getCodAteco2007().getValue());
              
                if(treeAteco.get(cdTipologia.getDescription()) == null)
                  treeAteco.put(cdTipologia.getDescription(), cdTipologia);
              }
            }            
          }        
        }        
      }      
    }
  }
  catch(SolmrException sx) 
  {  
    htmpl.set("exception", sx.getMessage());
    htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  }

  if(treeAteco.size() > 0)
  {
    htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    Collection c = treeAteco.values();
    Iterator tipiAttivitaATECOIterator = c.iterator();
    while(tipiAttivitaATECOIterator.hasNext()) {
      CodeDescription codeDescription = (CodeDescription)tipiAttivitaATECOIterator.next();
      htmpl.newBlock("tipiAttivitaATECO");
      htmpl.set("tipiAttivitaATECO.idAttivitaATECO",codeDescription.getCode().toString());
      htmpl.set("tipiAttivitaATECO.codiceATECO",codeDescription.getSecondaryCode().toString());
      htmpl.set("tipiAttivitaATECO.descrizioneATECO",codeDescription.getDescription());
    }
  }
  else
  {
    htmpl.set("exception",AnagErrors.RICERCAATTIVITAATECO);
    htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  }
  


%>
<%= htmpl.text()%>


