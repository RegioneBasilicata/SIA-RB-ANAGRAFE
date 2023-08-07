<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.UtenteIrideVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  public final static String VIEW = "../view/sceltaParticelleContrattoView.jsp";
%>

<%

  String iridePageName = "sceltaParticelleContrattoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  SolmrLogger.debug(this, " - sceltaParticelleContrattoCtrl.jsp - INIZIO PAGINA");

  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

    ValidationErrors errors = new ValidationErrors();

    //HashMap tenuto in session e contenente tutti i dati per l'inserimento delcontratto
    HashMap dati = (HashMap) session.getAttribute("common");
    ContrattoVO contrattoVO = (ContrattoVO)dati.get("contratto");
    Vector vParticelle = contrattoVO.getVParticelle();


      if(errors.size()>0)
        request.setAttribute("errors", errors);


    %><jsp:forward page="<%= VIEW %>"/><%
  }
  catch(Exception e)
  {
    if (e instanceof SolmrException)
    {
      setError(request, e.getMessage());
    }
    else
    {
      e.printStackTrace();
      setError(request, "Si è verificato un errore di sistema");
    }
    %><jsp:forward page="<%=VIEW%>" /><%
  }

  SolmrLogger.debug(this, " - contrattiNewProprietariCtrl.jsp - FINE PAGINA");
%>

<%!
  private void setError(HttpServletRequest request, String msg)
  {
    SolmrLogger.error(this, "\n\n\n\n\n\n\n\n\n\n\nmsg="+msg+"\n\n\n\n\n\n\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError(msg));
    request.setAttribute("errors", errors);
  }
  //Ricerca se una particella è presente in vettore di particelle
  private boolean findParticella(Vector vet,ParticellaVO part)
  {
    boolean risp=false;
    long idPart=part.getIdStoricoParticella().longValue();
    for(int i=0;i<vet.size();i++)
    {
      ParticellaVO partAppo =(ParticellaVO)vet.get(i);
      if(idPart==partAppo.getIdStoricoParticella().longValue())
      {
        risp=true;
        break;
      }
    }//end-for
    return risp;
  }
%>
