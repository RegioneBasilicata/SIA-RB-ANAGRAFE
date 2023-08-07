<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sedi_mod.htm");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  //UteVO uteErrorsVO = (UteVO)(request.getAttribute("uteErrorsVO"));
  UteVO voModifica = (UteVO)session.getAttribute("voModifica");

  %>
   	<%@include file = "/view/remoteInclude.inc" %>
  <%

	// Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

	it.csi.solmr.client.anag.AnagFacadeClient anagFacadeClient = new it.csi.solmr.client.anag.AnagFacadeClient();
  ValidationException valEx = null;

 	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoZonaAltimetrica");

  // l'attributo load viene settato in sediUteCtrl.jsp e serve affinche'
  // questo blocco venga eseguito solo la prima volta che entro nella modifica,
  // tutte le altre volte che entra (nel caso di errori di compilazione o altri
  // errori che ricaricano la pagina) non deve eseguire questo blocco
  UteVO vo = (UteVO)session.getAttribute("voModifica");
  if(vo != null && session.getAttribute("load") != null) 
  {
   	session.removeAttribute("load");
   	if(vo.getDataInizioAttivita() != null && !vo.getDataInizioAttivita().toString().equals("")) 
   	{
   		vo.setDataInizioAttivitaStr(DateUtils.formatDate(vo.getDataInizioAttivita()));
    }
    if(vo.getDataFineAttivita() != null && !vo.getDataFineAttivita().toString().equals("")) 
    {
    	vo.setDataFineAttivitaStr(DateUtils.formatDate(vo.getDataFineAttivita()));
    }
    htmpl.set("istatComuneOld",vo.getIstat());
    htmpl.set("descComuneOld",vo.getComune());
    //HtmplUtil.setValues(htmpl, vo);
    htmpl.set("istatComune",vo.getIstat());
    
    htmpl.set("indirizzo",vo.getIndirizzo());
    htmpl.set("provincia",vo.getProvincia());
    htmpl.set("comune",vo.getComune());
    htmpl.set("cap",vo.getCap());
    htmpl.set("denominazione",vo.getDenominazione());
    htmpl.set("telefono",vo.getTelefono());
    htmpl.set("fax",vo.getFax());
    htmpl.set("codeOte", vo.getCodeOte());
    htmpl.set("descOte", vo.getDescOte());
    htmpl.set("codeAteco", vo.getCodeAteco());
    htmpl.set("descAteco", vo.getDescAteco());
    
    htmpl.set("dataInizioAttivitaStr", vo.getDataInizioAttivitaStr());
    htmpl.set("note", vo.getNote());
    htmpl.set("motivoModifica", vo.getMotivoModifica());
  }
  else
  {

  //HtmplUtil.setValues(htmpl, request);
  
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
	}
	
  HtmplUtil.setErrors(htmpl, errors, request, application);

  String confermaModifica = (String)request.getAttribute("confermaOperazione");

  if(Validator.isNotEmpty(confermaModifica)) 
  {
   	htmpl.bset("confermaOperazione", confermaModifica);
  }
  
	if(elencoZonaAltimetrica != null && elencoZonaAltimetrica.length > 0) 
	{
		for(int i = 0; i < elencoZonaAltimetrica.length; i++) 
		{
			it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoZonaAltimetrica[i];
			htmpl.newBlock("blkTipiZonaAltimetrica");
			htmpl.set("blkTipiZonaAltimetrica.idZonaAltimetrica", code.getCode().toString());
			htmpl.set("blkTipiZonaAltimetrica.descrizione", code.getDescription());
			if((Validator.isNotEmpty(request.getParameter("idZonaAltimetrica")) && request.getParameter("idZonaAltimetrica").equalsIgnoreCase(code.getCode().toString())) ||
			   (Validator.isNotEmpty(request.getAttribute("idZonaAltimetrica")) && ((String)request.getAttribute("idZonaAltimetrica")).equalsIgnoreCase(code.getCode().toString()))) 
			{
				htmpl.set("blkTipiZonaAltimetrica.selected", "selected=\"selected\"", null);
			}
		}
	}
	
	if(vo.getvUteAtecoSec() != null) 
  {
    htmpl.newBlock("blkATECOSec");
    htmpl.newBlock("blkATECOSec.blkAttivitaATECOSec");
    
    for(int i = 0; i < vo.getvUteAtecoSec().size(); i++) 
    {
      UteAtecoSecondariVO uteAtecoSecondariVO = vo.getvUteAtecoSec().get(i);
      htmpl.newBlock("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec");
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.chkAttivitaAtecoSec", new Integer(i).toString());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.idAttivitaATECOElenco", ""+uteAtecoSecondariVO.getIdAttivitaAteco());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.codiceATECOSecElenco", uteAtecoSecondariVO.getCodiceAteco());
      htmpl.set("blkATECOSec.blkAttivitaATECOSec.blkElencoAttivitaATECOSec.descrizioneATECOSecElenco", uteAtecoSecondariVO.getDescAttivitaAteco());
    }
  }

%>

<%= htmpl.text()%>
