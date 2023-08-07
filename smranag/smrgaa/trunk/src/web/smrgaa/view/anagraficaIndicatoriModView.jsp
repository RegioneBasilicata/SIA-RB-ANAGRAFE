<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.GreeningVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%! @SuppressWarnings("unchecked") %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagraficaIndicatori_mod.htm");

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  //Vettore di AziendaAtecoSecVO
  Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagAziendaVO vo = (AnagAziendaVO)session.getAttribute("voAnagModifica");

  String richiestaModifica = (String)session.getAttribute("richiestaModifica");
  session.removeAttribute("richiestaModifica");
  if(richiestaModifica != null) 
  {
    htmpl.set("richiestaModifica",richiestaModifica);
  }
  else 
  {
    htmpl.set("richiestaModifica","");
  }
  
  if(session.getAttribute("modificaCampiAnagrafici") != null)
  {
    htmpl.set("disabled","disabled=\"disabled\"", null);
    htmpl.newBlock("blkFieldDisabled");
    //if(Validator.isNotEmpty(anagAziendaVO.getIdDimensioneAzienda()))
      //htmpl.set("blkFieldDisabled.idDimensioneAzienda",""+anagAziendaVO.getIdDimensioneAzienda());
    if(Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO()))
      htmpl.set("blkFieldDisabled.codiceATECO",""+anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode());
    if(Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO()))
      htmpl.set("blkFieldDisabled.descrizioneATECO",""+anagAziendaVO.getTipoAttivitaATECO().getDescription());
  }
  else
  {
    htmpl.newBlock("blkFieldEnabled");
  }

  //OTE
  //Arrivo da una pagina esterna
  /*if(!Validator.isNotEmpty(request.getParameter("regime")))
  {
    if((anagAziendaVO.getTipoAttivitaOTE() != null) && (anagAziendaVO.getTipoAttivitaOTE().getCode() != null)) 
    {  
      htmpl.set("codiceOTE", anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode().toString());
      htmpl.set("descrizioneOTE", anagAziendaVO.getTipoAttivitaOTE().getDescription());
      htmpl.set("idAttivitaOTE", anagAziendaVO.getTipoAttivitaOTE().getCode().toString());
    }
    
    //UDE
    if(anagAziendaVO.getClasseUde() != null)
    {
      htmpl.set("classeUde", anagAziendaVO.getClasseUde().toString());
    }
    
    if(anagAziendaVO.getIdUde() != null)
    {
      htmpl.set("idUde", anagAziendaVO.getIdUde().toString());
    }
    
    //RLS
    if(anagAziendaVO.getRls() != null)
    {
      htmpl.set("rls", Formatter.formatDouble2Separator(anagAziendaVO.getRls()));
    }
   
    //ULU
    if(anagAziendaVO.getUlu() != null)
    {
      htmpl.set("ulu", Formatter.formatDouble1(anagAziendaVO.getUlu()));
    }
  }
  else
  {
    //Se messo in request il calcolo ote è andato a buon fine
    PlSqlCalcoloOteVO plCode = (PlSqlCalcoloOteVO)request.getAttribute("plCode");
    if(plCode !=null) 
    {
      htmpl.set("codiceOTE", plCode.getCodiceOte());
      htmpl.set("descrizioneOTE", plCode.getDescOte());
      htmpl.set("idAttivitaOTE", ""+plCode.getIdAttivitaOte());
      htmpl.set("classeUde", ""+plCode.getClasseUde());
      htmpl.set("idUde", ""+plCode.getIdUde());
      htmpl.set("rls", Formatter.formatDouble2Separator(plCode.getRls()));
    }
    else
    {
      if(Validator.isNotEmpty(request.getParameter("codiceOTE")))
      {
        htmpl.set("codiceOTE", request.getParameter("codiceOTE"));
      }
      
      if(Validator.isNotEmpty(request.getParameter("descrizioneOTE")))
      {
        htmpl.set("descrizioneOTE", request.getParameter("descrizioneOTE"));
      }
      
      if(Validator.isNotEmpty(request.getParameter("idAttivitaOTE")))
      {
        htmpl.set("idAttivitaOTE", request.getParameter("idAttivitaOTE"));
      }
      
      if(Validator.isNotEmpty(request.getParameter("classeUde")))
      {
        htmpl.set("classeUde", request.getParameter("classeUde"));
      }
      
      if(Validator.isNotEmpty(request.getParameter("idUde")))
      {
        htmpl.set("idUde", request.getParameter("idUde"));
      }
      
      if(Validator.isNotEmpty(request.getParameter("rlsStr")))
      {
        htmpl.set("rls", request.getParameter("rlsStr"));
      }
    }
    
    //Se messo in request il calcolo ulu è andato a buon fine
    PlSqlCalcoloOteVO plCodeUlu = (PlSqlCalcoloOteVO)request.getAttribute("plCodeUlu");
    if(plCodeUlu !=null) 
    {        
      htmpl.set("ulu", Formatter.formatDouble1(plCodeUlu.getUlu()));
    }
    else
    {
      if(Validator.isNotEmpty(request.getParameter("uluStr")))
      {
        htmpl.set("ulu", request.getParameter("uluStr"));
      }
    }
  }*/
  
  if(errors == null) 
  {
    htmpl.set("idAttivitaATECOSec","");
    htmpl.set("codiceATECOSec","");
    htmpl.set("descrizioneATECOSec","");  
    
    if(vo != null && session.getAttribute("load") != null) 
    {
      session.removeAttribute("load");
      
      if(vo.getTipoAttivitaATECO() != null && vo.getTipoAttivitaATECO().getCode() != null) 
      {
        htmpl.set("idAttivitaATECO",vo.getTipoAttivitaATECO().getCode().toString());
      }
      if(vo.getTipoAttivitaATECO() != null && vo.getTipoAttivitaATECO().getSecondaryCode() != null) 
      {
        htmpl.set("codiceATECO",vo.getTipoAttivitaATECO().getSecondaryCode().toString());
      }
      if(vo.getTipoAttivitaATECO() != null && vo.getTipoAttivitaATECO().getDescription() != null) 
      {
        htmpl.set("descrizioneATECO",vo.getTipoAttivitaATECO().getDescription());
      }
      //qui
      HtmplUtil.setValues(htmpl, vo);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
    else 
    {
      HtmplUtil.setValues(htmpl, request);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
  }
  else 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }

  // Carico le combo della dimensione azienda.
  // Lo faccio qua altrimenti dovrei duplicarlo nei due controller che
  // chiamano la pagina
  /*Vector<TipoDimensioneAziendaVO> vDimAzienda = gaaFacadeClient.getListActiveTipoDimensioneAzienda();
  String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
  if(vDimAzienda != null) 
  {
    for(int i=0;i<vDimAzienda.size();i++)
    {
      TipoDimensioneAziendaVO tipoDimensioneAzienda = (TipoDimensioneAziendaVO)vDimAzienda.get(i);
      htmpl.newBlock("cmbDimAzienda");
      htmpl.set("cmbDimAzienda.idDimensioneAzienda",""+tipoDimensioneAzienda.getIdDimensioneAzienda());
      htmpl.set("cmbDimAzienda.descDimensioneAzienda",tipoDimensioneAzienda.getDescrizione());
      if(!Validator.isNotEmpty(request.getParameter("regime")))
      {
        if(anagAziendaVO != null && (anagAziendaVO.getIdDimensioneAzienda() != null)
          && (tipoDimensioneAzienda.getIdDimensioneAzienda() == anagAziendaVO.getIdDimensioneAzienda().longValue())) 
        {
          htmpl.set("cmbDimAzienda.selected","selected",null);
        }
      }
      else
      {
        if(Validator.isNotEmpty(idDimensioneAzienda))
        {
          Long idDimensioneAziendaLg = new Long(idDimensioneAzienda);
          if(tipoDimensioneAzienda.getIdDimensioneAzienda() == idDimensioneAziendaLg.longValue())
          {
            htmpl.set("cmbDimAzienda.selected","selected",null);
          }        
        }
      }
    }
  }*/
  
  if((vAziendaAtecoSec != null) && (vAziendaAtecoSec.size() > 0))
  {
    htmpl.newBlock("blkAttivitaATECOSec");
    for(int i=0;i<vAziendaAtecoSec.size();i++)
    {
      htmpl.newBlock("blkAttivitaATECOSec.blkElencoAttivitaATECOSec");
      AziendaAtecoSecVO aziendaAtecoSec = (AziendaAtecoSecVO)vAziendaAtecoSec.get(i);
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.chkAttivitaAtecoSec", new Integer(i).toString());
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.idAttivitaATECOElenco", new Long(aziendaAtecoSec.getIdAttivitaAteco()).toString()); 
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.codiceATECOSecElenco", aziendaAtecoSec.getCodAttivitaAteco());
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.descrizioneATECOSecElenco", aziendaAtecoSec.getDescAttivitaAteco());
    }
    
    if(session.getAttribute("modificaCampiAnagrafici") == null)
    {
      htmpl.newBlock("blkAttivitaATECOSec.blkFieldEnabled");
    }
  }
  
  //PAGAMENTO ECOLOGICO - Inizio
  @SuppressWarnings("unchecked")
  Vector<GruppoGreeningVO> gruppiGreening = (Vector<GruppoGreeningVO>)request.getAttribute("gruppiGreening");
  
  if (gruppiGreening!=null) {
  	for (GruppoGreeningVO gruppo : gruppiGreening) {
  		htmpl.newBlock("blkGruppoGreening");
  		
  		if (Validator.isNotEmpty(gruppo.getDescGruppoGreening())) {
  			htmpl.set("blkGruppoGreening.descGruppo", gruppo.getDescGruppoGreening().toUpperCase());
  		}
  		
  		for (GreeningVO greening : gruppo.getListaGreening()) {
  			htmpl.newBlock("blkGruppoGreening.blkGreening");
  			htmpl.set("blkGruppoGreening.blkGreening.tipo", greening.getDescTipoGreening());
  			
  			if (greening.getIdAziendaGreening()!=null) {
  				StringBuffer descrizione = new StringBuffer(greening.getDescEsitoGreening());
  				
  				if (Validator.isNotEmpty(greening.getValoreCalcolato())) {
  					descrizione.append(" ").append(greening.getValoreCalcolato());
  				}
  				
  				if (greening.getValoreCalcolatoNumber()!=null) {
  					descrizione.append(" ").append(
  						Formatter.formatDouble2Separator(greening.getValoreCalcolatoNumber()));
  				}
  				
  				if (greening.getValoreCalcolatoDate()!=null) {
  					descrizione.append(" ").append(
  						DateUtils.formatDateTimeNotNull(greening.getValoreCalcolatoDate()));
  				}
  				
  				for (int i=0; i<greening.getListaDescTipiEsonero().size(); i++) {
  					if (i==0) {
  						descrizione.append(" ");
  					}else {
  						descrizione.append(" - ");
  					}
  					
  					descrizione.append(greening.getListaDescTipiEsonero().get(i));
  				}
  				
  				htmpl.set("blkGruppoGreening.blkGreening.descrizione", descrizione.toString());
  			}
  		}
  	}
  }
	//PAGAMENTO ECOLOGICO - Fine
	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if (Validator.isNotEmpty(messaggioErrore)) {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}

%>
<%= htmpl.text()%>
