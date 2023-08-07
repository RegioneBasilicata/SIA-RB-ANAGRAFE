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
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaDocumentiIndexVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.TipoFirmaVO"%>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/elencoDocDem.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  WebUtils.removeUselessFilter(session, "paginaCorIndex");
  
  String primoIngresso = request.getParameter("primoIngresso");
  if(Validator.isNotEmpty(primoIngresso)
    && "true".equalsIgnoreCase(primoIngresso))
  {
    session.removeAttribute("paginaCorIndex");
    session.setAttribute("paginaCorIndex", new Integer("1")); 
  }
  
  
  Integer paginaCorIndex = (Integer)session.getAttribute("paginaCorIndex");  
  
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
  
  
  
  try
  {
  
	  int idProcedimento = SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE;
	  String codRuoloUtente = ruoloUtenza.getCodiceRuolo();
	  String idFolder = request.getParameter("idFolder");
	  Long idFolderMadre = null;
	  if(Validator.isNotEmpty(idFolder))
	    idFolderMadre = new Long(idFolder);
	    
	  String pagina = request.getParameter("pagina");
	  
	  boolean noEmptyFolder = true;
	  Long idAzienda = anagAziendaVO.getIdAzienda();
	  
	  AgriWellEsitoIdDocVO agriWellEsitoIdDocVO = gaaFacadeClient
	    .agriwellFindListaDocumentiByIdFolder(idFolderMadre, idProcedimento, codRuoloUtente, idAzienda);
	    
	  Vector<TipoFirmaVO> vTipoFirma = gaaFacadeClient.getElencoTipoFirma();  
	   
	    
	  if(Validator.isNotEmpty(agriWellEsitoIdDocVO)
	    && Validator.isNotEmpty(agriWellEsitoIdDocVO.getEsito())
	    && (agriWellEsitoIdDocVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK)
	    && (agriWellEsitoIdDocVO.getElencoIdDocumento().length > 0))
	  {  
	          
	    PaginazioneUtils pager = PaginazioneUtils.newInstance(agriWellEsitoIdDocVO.getElencoIdDocumento(),
	        SolmrConstants.NUM_RIGHE_PER_PAGINA_INDEX, 
	        paginaCorIndex,
	        pagina, "paginaCorrente");
	    long idsForPage[] = pager.getIdForCurrentPage(true);
	    if (idsForPage != null && idsForPage.length > 0)
	    {
	      AgriWellEsitoDocumentoVO agriWellEsitoDocumentoVO = gaaFacadeClient
	        .agriwellFindDocumentoByIdRange(idsForPage);
	        
	      if(Validator.isNotEmpty(agriWellEsitoDocumentoVO)
			    && Validator.isNotEmpty(agriWellEsitoDocumentoVO.getEsito())
			    && (agriWellEsitoDocumentoVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK))
			  {
			    RigaRicercaDocumentiIndexVO[] righe = new RigaRicercaDocumentiIndexVO[agriWellEsitoDocumentoVO.getElencoDocumenti().length];
			  
			    for(int i=0;i<agriWellEsitoDocumentoVO.getElencoDocumenti().length;i++)
			    {
			      AgriWellDocumentoVO agriWellDocumentoVO = agriWellEsitoDocumentoVO.getElencoDocumenti()[i];
			      righe[i] = new RigaRicercaDocumentiIndexVO();
			      righe[i].setIdDocumento(agriWellDocumentoVO.getIdDocumentoIndex());
			      righe[i].setDescTipoDocumento(agriWellDocumentoVO.getDescTipoDocumento());
			      
			      Date dataFineValidita = null;
			      if(Validator.isNotEmpty(agriWellDocumentoVO.getAMetadati())
			        && agriWellDocumentoVO.getAMetadati().length > 0)
			      {
			        for(int j=0;j<agriWellDocumentoVO.getAMetadati().length;j++)
			        {
			          if("data_fine".equalsIgnoreCase(agriWellDocumentoVO.getAMetadati()[j].getNomeEtichetta()))
			          {		          
			            dataFineValidita = DateUtils.parseFullDate(agriWellDocumentoVO.getAMetadati()[j].getValoreEtichetta());
			            break;
			          }
			        }
			      }
			      righe[i].setDataFineValidita(dataFineValidita);
			      righe[i].setNomeFile(agriWellDocumentoVO.getNomeFile());
			      
			     
			      righe[i].setDataInserimento(it.csi.solmr.util.DateUtils.convertCalendar(agriWellDocumentoVO.getDataInserimento()));
			      righe[i].setAnno(agriWellDocumentoVO.getAnno().intValue());
			      righe[i].setNumeroProtocollo(agriWellDocumentoVO.getNumeroProtocollo());
			      righe[i].setDataProtocollo(it.csi.solmr.util.DateUtils.convertCalendar(agriWellDocumentoVO.getDataProtocollo()));
			      righe[i].setDescCategoriaDocumento(agriWellDocumentoVO.getDescCategoriaDocumento());
			      righe[i].setDescTipologiaDocumento(agriWellDocumentoVO.getDescTipologiaDocumento());
			      righe[i].setDescProcedimento(agriWellDocumentoVO.getDescProcedimento());
			      righe[i].setDaFirmare(agriWellDocumentoVO.getDaFirmare());
			      
			      if(Validator.isNotEmpty(agriWellDocumentoVO.getDaFirmare()))
			      {
			        if(vTipoFirma != null)
	            {
	              for(int j=0;j<vTipoFirma.size();j++)
	              {
	                if(vTipoFirma.get(j).getFlagFirmaDoquiAgri()
	                  .equalsIgnoreCase(agriWellDocumentoVO.getDaFirmare()))
	                {
	                  righe[i].setImmagineStampa(vTipoFirma.get(j).getStileFirma());
	                  righe[i].setTitleAllegato(vTipoFirma.get(j).getDescrizioneTipoFirma());
	                }
	              
	              }             
	            }
			      }
			      
			      
			      if(Validator.isEmpty(righe[i].getImmagineStampa()))
			      {
			        righe[i].setImmagineStampa("noFirma");
              righe[i].setTitleAllegato("Non firmata");
			      }
			      
			      
			      
			      
			      
			    }
			    
			    pager.setRighe(righe);
			    session.setAttribute("paginaCorIndex", new Integer(pager.getPaginaCorrente()));
			    
			    
				  if (pager != null && pager.getTotaleRighe() > 0)
				  {
				    
				    
				    pager.paginazioneFolder(htmpl, idFolder);
				    
				    String paginaCorrente = "1";
				    /*if(Validator.isNotEmpty(pager.getPaginaCorrente()))
				    {
				      //paginaCorrente = pager.getPaginaCorrente();
				    }*/
				    htmpl.set("blkElenco.paginaCorrente", ""+pager.getPaginaCorrente());
				    htmpl.set("blkElenco.idFolder", idFolder);
				    
				  }
			  
			  
			  }
	    
	    
	    }
	    
	    
	    
	  
	  
	  }
	  else
	  {
	    if(Validator.isNotEmpty(agriWellEsitoIdDocVO)
        && Validator.isNotEmpty(agriWellEsitoIdDocVO.getEsito())
        && agriWellEsitoIdDocVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK)
      {}  //Non si fa nulla
      else
      {
      
        htmpl.newBlock("blkNoElenco");
	      String messaggioErrore = "Errore generico: ";
	      if(Validator.isNotEmpty(agriWellEsitoIdDocVO)
	        && Validator.isNotEmpty(agriWellEsitoIdDocVO.getEsito()))
	      {
	        if(agriWellEsitoIdDocVO.getEsito().intValue() != SolmrConstants.ESITO_AGRIWELL_OK)
	        {
	          messaggioErrore += agriWellEsitoIdDocVO.getMessaggio();
	        }	        
	      }
	      else
	      {
	      
	      }
	      htmpl.set("blkNoElenco.messaggioErrore", messaggioErrore);
      
      }
	  
	    /*htmpl.newBlock("blkNoElenco");
	    String messaggioErrore = "Errore generico: ";
	    if(Validator.isNotEmpty(agriWellEsitoIdDocVO)
	      && Validator.isNotEmpty(agriWellEsitoIdDocVO.getEsito()))
	    {
	      if(agriWellEsitoIdDocVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK)
	      {
	        messaggioErrore = "NON SONO PRESENTI DOCUMENTI PER L'AZIENDA NELLA CARTELLA SELEZIONATA";
	      }
	      else
	      {
	        messaggioErrore += agriWellEsitoIdDocVO.getMessaggio();
	      }
	      
	    }
	    else
	    {
	    
	    }
	    htmpl.set("blkNoElenco.messaggioErrore", messaggioErrore);*/  
	  
	  }
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
