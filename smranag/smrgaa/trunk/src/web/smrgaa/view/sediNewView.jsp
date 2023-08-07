<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sedi_new.htm");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  //HtmplUtil.setValues(htmpl, request);

  %>
   	<%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO aaVO = null;
  it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoZonaAltimetrica");
  aaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Vector<UteAtecoSecondariVO> vAtecoSecUte = (Vector<UteAtecoSecondariVO>)session.getAttribute("vAtecoSecUte");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("idAzienda",aaVO.getIdAzienda()+"");

  String confermaInserimento = (String)request.getAttribute("confermaOperazione");

  if(Validator.isNotEmpty(confermaInserimento)) 
  {
   	htmpl.bset("confermaOperazione", confermaInserimento);
  }

  htmpl.set("indirizzo",request.getParameter("indirizzo"));
  htmpl.set("provincia",request.getParameter("provincia"));
  htmpl.set("comune",request.getParameter("comune"));
  htmpl.set("cap",request.getParameter("cap"));
  htmpl.set("denominazione",request.getParameter("denominazione"));
  htmpl.set("telefono",request.getParameter("telefono"));
  htmpl.set("fax",request.getParameter("fax"));
  htmpl.set("codeOte", request.getParameter("codeOte"));
  htmpl.set("descOte", request.getParameter("descOte"));
  htmpl.set("codeAteco", request.getParameter("codeAteco"));
  htmpl.set("descAteco", request.getParameter("descAteco"));
  
  htmpl.set("dataInizioAttivitaStr", request.getParameter("dataInizioAttivitaStr"));
  htmpl.set("note", request.getParameter("note"));
  htmpl.set("motivoModifica",request.getParameter("motivoModifica"));

  HtmplUtil.setErrors(htmpl, errors, request, application);
  	
  if(elencoZonaAltimetrica != null && elencoZonaAltimetrica.length > 0) 
  {
		for(int i = 0; i < elencoZonaAltimetrica.length; i++) 
		{
			it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoZonaAltimetrica[i];
			htmpl.newBlock("blkTipiZonaAltimetrica");
			htmpl.set("blkTipiZonaAltimetrica.idZonaAltimetrica", code.getCode().toString());
			htmpl.set("blkTipiZonaAltimetrica.descrizione", code.getDescription());
			if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica")) 
			  && request.getParameter("idZonaAltimetrica").equalsIgnoreCase(code.getCode().toString())) 
			{
				htmpl.set("blkTipiZonaAltimetrica.selected", "selected=\"selected\"", null);
			}
		}
	}
	
	if(vAtecoSecUte != null) 
  {
    htmpl.newBlock("blkATECOSec");
    htmpl.newBlock("blkATECOSec.blkAttivitaATECOSec");
    
    for(int i = 0; i < vAtecoSecUte.size(); i++) 
    {
      UteAtecoSecondariVO uteAtecoSecondariVO = vAtecoSecUte.get(i);
      htmpl.newBlock("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec");
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.chkAttivitaAtecoSec", new Integer(i).toString());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.idAttivitaATECOElenco", ""+uteAtecoSecondariVO.getIdAttivitaAteco());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.codiceATECOSecElenco", uteAtecoSecondariVO.getCodiceAteco());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.descrizioneATECOSecElenco", uteAtecoSecondariVO.getDescAttivitaAteco());
    }
  }


%>

<%= htmpl.text()%>
