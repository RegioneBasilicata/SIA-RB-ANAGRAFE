<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno" %>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato" %>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  SolmrLogger.debug(this, " - terreniParticellareSigmaterCtrl.jsp - INIZIO PAGINA");

  String iridePageName = "terreniParticellareSigmaterCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String viewUrl = "/view/terreniParticellareSigmaterView.jsp";
  String terreniParticellareElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
  
  
  String actionUrl = "../layout/terreniParticellareElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione importa titolarita' Sigmater."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  String pagina = request.getParameter("pagina");
  if(Validator.isNotEmpty(pagina)) {
    session.setAttribute("pagina", pagina);
  }
  
  String regime = request.getParameter("regimeSigmater");
  String eliminaSessione = null;
  if(Validator.isNotEmpty(regime))
  {
    eliminaSessione = "filtriParticellareRicercaVO,pagina,titolarita";
  }
  else
  {
    eliminaSessione = "filtriParticellareRicercaVO,pagina";
  }  
  WebUtils.removeUselessFilter(session, eliminaSessione);
  
  
  
  
  
  
  
  
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();  
  StoricoParticellaVO storicoParticellaVO = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  try
  {
    
    FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
    Long idConduzione = null;
    //Arrivo dall'elenco
    if(Validator.isNotEmpty(request.getParameter("idConduzione")))
    {
      idConduzione = Long.decode(request.getParameter("idConduzione"));
    }
    //sono nella pagina
    else
    {
      idConduzione = Long.decode(request.getParameter("idConduzioneSigmater"));
    }
	  String codIstatComune=null;
	  String codBelfioreComune=null;
	  String sezione=null;
	  String foglio=null;
	  String numero=null;
	  String subalterno=null;
	  String progressivo=null;
	   
	  // Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	  // selezionato dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	  // di errore
	  if(filtriParticellareRicercaVO == null || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
	  {
	    request.setAttribute("messaggioErrore1",(String)AnagErrors.get("ERRORE_KO_DETTAGLIO_PARTICELLA"));
	    %>
	      <jsp:forward page="<%=viewUrl%>" />
	    <%
	    return;
	  }
	  else 
	  {    
	    // Cerco i dati della particella in relazione all'id conduzione selezionato
	    try 
	    {
	      storicoParticellaVO = anagFacadeClient.getDettaglioParticella(filtriParticellareRicercaVO, idConduzione);
	    }
	    catch(Exception e) 
	    {
	      SolmrLogger.getStackTrace(e);
	      request.setAttribute("messaggioErrore1",(String)AnagErrors.get("ERRORE_KO_DETTAGLIO_PARTICELLA"));
	      request.setAttribute("messaggioErrore2",e.toString());
	      %>
	        <jsp:forward page="<%=viewUrl%>" />
	      <%
	      return;
	    }    
	    
	    
	    request.setAttribute("storicoParticellaVO", storicoParticellaVO); 
	    
	    
	    if(storicoParticellaVO != null) 
	    {
	      codIstatComune=storicoParticellaVO.getIstatComune();
	      sezione=storicoParticellaVO.getSezione();
	      foglio=storicoParticellaVO.getFoglio();
	      numero=storicoParticellaVO.getParticella();
	      subalterno=storicoParticellaVO.getSubalterno();
	      progressivo=null;
	      
	      
	      
	      
	      
	      if(Validator.isNotEmpty(request.getParameter("confermoOperazione"))) 
			  {
			    Titolarita[] titolarita = (Titolarita[])session.getAttribute("titolarita");
			    if(Validator.isNotEmpty(titolarita))
			    {
			      try 
			      {
			        
			        gaaFacadeClient.importaTitolaritaSigmater(storicoParticellaVO.getIdParticella(), titolarita, ruoloUtenza.getIdUtente());
			        
			     
			        String valorePagina = (String)session.getAttribute("pagina");
			        %>
			          <jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" >
			            <jsp:param name="pagina" value="<%= valorePagina %>" /> 
			          </jsp:forward>
			        <%
			        
			        
			      }
			      catch(SolmrException se) 
			      {
			        SolmrLogger.info(this, " - terreniParticellareSigmaterCtrl.jsp - FINE PAGINA");
			        String messaggio = errMsg+": "+AnagErrors.KO_IMPORT_DIRITTO+".\n"+se.toString();
			        request.setAttribute("messaggioErrore",messaggio);
			        request.setAttribute("pageBack", actionUrl);
			        %>
			          <jsp:forward page="<%= erroreViewUrl %>" />
			        <%
			        return; 
			      }         
			  
			  
			      
			    }    
			  }
	      
	      
	      
	      
	      
	      
	    }
	     
	  }  
	   
	   
	  DettaglioTerreno dettTerreno=null;
	  DettaglioFabbricato dettFabbricato=null;
	  String presenzaFabbricati=SolmrConstants.FLAG_NO;
	   
	  dettTerreno=gaaFacadeClient.cercaTerreno(codIstatComune,codBelfioreComune,sezione,foglio,numero,subalterno,progressivo);             
	  request.setAttribute("dettaglioTerreno",dettTerreno);    
	                                 
	  try
	  {                 
	    dettFabbricato=gaaFacadeClient.cercaFabbricato(codIstatComune,codBelfioreComune,sezione,foglio,numero,subalterno,progressivo);
	    if (dettFabbricato!=null && dettFabbricato.getFabbricato()!=null) presenzaFabbricati=SolmrConstants.FLAG_SI;
	  }
	  catch(SolmrException se) 
	  {
	    //Se c'è un errore nella chiamata al servizio lo comunico ma continuo con il flusso del programma
	    presenzaFabbricati=(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT")+"<BR>"+se.getMessage();
	  }
	  request.setAttribute("presenzaFabbricati",presenzaFabbricati); 
	   
	  try
	  {                      
	    String idImmobile=null;
	    if (dettTerreno!=null && dettTerreno.getTerreno()!=null)
	      idImmobile=dettTerreno.getTerreno().getIdImmobile();
	    String tipoImmobile="T"; 
	    String data=DateUtils.getCurrent(DateUtils.DATA);
	    //idImmobile è un dato obbligatorio, quindi chiamo il servizio solo se è valorizzato
	    Titolarita[] titolarita = (Titolarita[])session.getAttribute("titolarita");
	    if ((idImmobile!=null) 
	      && (titolarita == null))
	    {
	      titolarita=gaaFacadeClient.cercaTitolaritaOggettoCatastale(codIstatComune,codBelfioreComune,sezione,idImmobile,tipoImmobile,data,data);
	    }
	    session.setAttribute("titolarita",titolarita); 
	  }
	  catch(SolmrException se) 
	  {
	    //Se c'è un errore nella chiamata al servizio lo comunico ma continuo con il flusso del programma
	    request.setAttribute("messaggioErrore3",(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT"));
	    request.setAttribute("messaggioErrore4",se.getMessage());
	  }
	   
	                          
	   
	}
	catch(SolmrException se) 
	{
	  request.setAttribute("messaggioErrore1",(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT"));
	  request.setAttribute("messaggioErrore2",se.getMessage());
	}
	catch(Exception e)
	{
	  SolmrLogger.getStackTrace(e);
	  request.setAttribute("messaggioErrore1",AnagErrors.ERRORE_500);
	  request.setAttribute("messaggioErrore2",e.toString());
	}
	 
	if(request.getParameter("importaTitolarita") != null) 
  {
  
    Titolarita[] titolarita = (Titolarita[])session.getAttribute("titolarita");
    if(Validator.isNotEmpty(titolarita))
    {
      try 
      {
        boolean trovati = true;
        for(int i=0;i<titolarita.length;i++)
        {
          Long idDiritto = gaaFacadeClient.getIdTipoDiritto(titolarita[i].getCodiceDiritto());
          if(idDiritto == null)
          {
            trovati = false;
            break;
          }
        }
        
        //non ho trovato almeno un diritto devo bloccare e segnalare...
        if(!trovati)
        {
          String msg = AnagErrors.KO_CODICE_DIRITTO;
          request.setAttribute("messaggioErrore", msg);      
        }
        else
        {
          int count = 0;
          //verifico che nn sia già presente su DB
          for (int i=0;i<titolarita.length;i++)
          {            
            for (int j=0;j<2;j++)
            {
              SoggettoCatastale soggetto=null;
              
              if (j==0) 
              {
                soggetto=titolarita[i].getSoggetto();
              }
              else 
              {
                soggetto=titolarita[i].getSoggettoDiRiferimento();
              }
              if (soggetto!=null)
              {
                count++;
              }
            }
          }
          
          Vector<ProprietaCertificataVO> vPropCert = null;
          try
          {
            vPropCert = anagFacadeClient.getListProprietaCertifByIdParticella(storicoParticellaVO.getIdParticella());
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - terreniParticellareSigmaterCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.KO_IMPORT_DIRITTO+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return; 
          }
          
          
          
          
          //Verifico che sigmater abbia sempre che le titolarita 
          //abbiano sempre il cuaa valorizzato
          boolean trovatoCuaa = true;
          for (int i=0;i<titolarita.length;i++)
          {
            if(trovatoCuaa)
            {                            
	            for (int j=0;j<2;j++)
	            {
	              SoggettoCatastale soggetto=null;
	              
	              if (j==0) 
	              {
	                soggetto=titolarita[i].getSoggetto();
	              }
	              else 
	              {
	                soggetto=titolarita[i].getSoggettoDiRiferimento();
	              }
	              String codiceFiscale = "";
	              if (soggetto!=null)
	              {
	                if ("P".equals(soggetto.getTipoSoggetto())) 
	                {
	                  codiceFiscale=soggetto.getSoggFisico().getCodFiscale();
	                  if(Validator.isEmpty(codiceFiscale))
	                  {
	                    trovatoCuaa = false;
	                  }
	                }
	                else
	                {
	                  if(soggetto.getSoggGiuridico()!=null)
	                  {
	                    codiceFiscale=soggetto.getSoggGiuridico().getCodFiscale();
	                    if(Validator.isEmpty(codiceFiscale))
	                    {
	                      trovatoCuaa = false;
	                    }
	                  }
	                }                    
	              } //if diversi titolarita            
	            }
	          }         
          } //for titolarità
          
          
          
          
          
          
          
          
          
          boolean diversi = false;
          boolean ugualiNumRecord = false;
          if((vPropCert != null) &&  (vPropCert.size() == count))
          {
            ugualiNumRecord = true;
            for(int h=0;h<vPropCert.size();h++)
            {
              if(!diversi)
              {
	              ProprietaCertificataVO propCert = vPropCert.get(h);
	              boolean unicaTitolarita = false;
					      if(titolarita.length == 1)
					      {
					        unicaTitolarita = true;
					      }  
					      
					      if(!diversi)
					      {  
					        boolean trovatoCuaaPropCert = false; 
			            for (int i=0;i<titolarita.length;i++)
				          {			             
				            boolean unicoSoggetto = false;
						        if(titolarita[0].getSoggettoDiRiferimento() == null)
						        {
						          unicoSoggetto = true;
						        }
				                     
				            for (int j=0;j<2;j++)
				            {
				              SoggettoCatastale soggetto=null;
				              
				              if (j==0) 
				              {
				                soggetto=titolarita[i].getSoggetto();
				              }
				              else 
				              {
				                soggetto=titolarita[i].getSoggettoDiRiferimento();
				              }
				              String codiceFiscale = "";
				              if (soggetto!=null)
				              {
				                if ("P".equals(soggetto.getTipoSoggetto())) 
				                {
				                  codiceFiscale=soggetto.getSoggFisico().getCodFiscale();
				                }
				                else
				                {
				                  if(soggetto.getSoggGiuridico()!=null)
				                  {
				                    codiceFiscale=soggetto.getSoggGiuridico().getCodFiscale();
				                  }
				                }
		                
		                    if(propCert.getCuaa().equalsIgnoreCase(codiceFiscale.toUpperCase()) && !trovatoCuaaPropCert)
		                    {
		                      trovatoCuaaPropCert = true;
		                      Long idDiritto = gaaFacadeClient.getIdTipoDiritto(titolarita[i].getCodiceDiritto());
		                      //confronto diritti
		                      if(propCert.getIdTipoDiritto().compareTo(idDiritto) == 0)
		                      {		                        
		                        
		                        BigDecimal percentualePossesso = new BigDecimal(0);
								            if(Validator.isNotEmpty(titolarita[i].getQuotaDenominatore())
								              && Validator.isNotEmpty(titolarita[i].getQuotaNumeratore()))
								            {
								              percentualePossesso = new BigDecimal(1);
								              percentualePossesso = percentualePossesso.multiply(new BigDecimal(titolarita[i].getQuotaNumeratore()));
								              percentualePossesso = percentualePossesso.divide(
								                  new BigDecimal(titolarita[i].getQuotaDenominatore()),4,BigDecimal.ROUND_HALF_UP);
								              percentualePossesso = percentualePossesso.multiply(new BigDecimal(100));
								              if(!unicoSoggetto)
								              {
								                percentualePossesso = percentualePossesso.divide(
								                    new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
								              }
								            }
								            else
								            {
								              if(unicaTitolarita)
								              {
								                percentualePossesso = new BigDecimal(100);
								                if(!unicoSoggetto)
								                {
								                  percentualePossesso = percentualePossesso.divide(
								                      new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
								                }
								              }
								              //non riesco a capire la percentuale di titolarita'
								              else
								              {
								                percentualePossesso = null;
								              }
								            }
		                        
		                        //confronto percentuali possesso
		                        if((Validator.isNotEmpty(percentualePossesso) 
		                          && Validator.isNotEmpty(propCert.getPercentualePossesso()) 
		                          && propCert.getPercentualePossesso()
		                          .compareTo(percentualePossesso) == 0)
		                          || 
		                          (Validator.isEmpty(percentualePossesso) 
                                && Validator.isEmpty(propCert.getPercentualePossesso()))
		                          )
                            {
		                          break;
		                        }
		                        else
		                        {
		                          diversi = true;
                              break;
		                        }
		                      }
		                      else
		                      {
		                        diversi = true;
		                        break;
		                      }
		                    
		                    } //if codice fiscale
		                    
		                    
		                    
				              }
				            }
				          } //for titolarita
				          
				          if(!trovatoCuaaPropCert)
                   {
                     diversi = true;
                     break;
                   }   
			          
			          } //if diversi titolarita
			        }
		        }
          
       
          
          } //if uguali
        
          if(!trovatoCuaa)
          {
            String msg = AnagErrors.KO_TITOLARITA_CUAA_NON_PRESENTE;
            request.setAttribute("messaggioErrore", msg);      
          }
          else if(ugualiNumRecord && !diversi)
          {
            String msg = AnagErrors.KO_TITOLARITA_PRESENTE;
            request.setAttribute("messaggioErrore", msg);      
          }
          else
          {
            request.setAttribute("onLoad", "onLoad");   
          }
        }
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniParticellareSigmaterCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.KO_IMPORT_DIRITTO+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return; 
      }
    }     
  }
	
  
  SolmrLogger.debug(this, " - terreniParticellareSigmaterCtrl.jsp - FINE PAGINA");
  
  
  
  
  
  
  
    
  request.getRequestDispatcher(viewUrl).forward(request, response);

%>
