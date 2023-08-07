<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellFolderVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/treeDematerializzato.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  int idProcedimento = SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE;
  String codRuoloUtente = ruoloUtenza.getCodiceRuolo();
  String idFolder = request.getParameter("idFolder");
  Long idFolderMadre = null;
  if(Validator.isNotEmpty(idFolder))
    idFolderMadre = new Long(idFolder);
  
  boolean noEmptyFolder = true;
  Long idAzienda = anagAziendaVO.getIdAzienda();
  
  AgriWellEsitoFolderVO agriWellEsitoFolderVO = gaaFacadeClient
    .agriwellFindFolderByPadreProcedimentoRuolo(idProcedimento, codRuoloUtente, idFolderMadre, noEmptyFolder, idAzienda);
    
  if(Validator.isNotEmpty(agriWellEsitoFolderVO)
    && Validator.isNotEmpty(agriWellEsitoFolderVO.getEsito())
    && (agriWellEsitoFolderVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK)
    && (agriWellEsitoFolderVO.getElencoFolder().length > 0))
  {
    htmpl.set("divFolderPadre","ch"+idFolder);
    for(int i=0;i<agriWellEsitoFolderVO.getElencoFolder().length;i++)
    {
      AgriWellFolderVO agriWellFolderVO = agriWellEsitoFolderVO.getElencoFolder()[i];
      htmpl.newBlock("blkCartella");
      /*if("S".equalsIgnoreCase(agriWellFolderVO.getHasChildren()))
      {
        htmpl.newBlock("blkCartella.blkRadio");
        htmpl.set("blkCartella.blkRadio.idFolder",""+agriWellFolderVO.getIdNomeFolder());
      }*/
      htmpl.set("blkCartella.divFolder","ch"+agriWellFolderVO.getIdNomeFolder());
      htmpl.set("blkCartella.aFolder","a"+agriWellFolderVO.getIdNomeFolder());
      htmpl.set("blkCartella.idFolder",""+agriWellFolderVO.getIdNomeFolder());
      htmpl.set("blkCartella.nomeFolder", agriWellFolderVO.getNomeFolder());    
    }
  
  
  }
    
  
	
 	

%>
<%= htmpl.text()%>
