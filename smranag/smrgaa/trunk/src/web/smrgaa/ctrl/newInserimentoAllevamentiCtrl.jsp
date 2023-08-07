<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoAllevamentiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,vAllevamentiAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoAllevamentiUrl = "/view/newInserimentoAllevamentiView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoFabbricatiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoSoggettiCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoAllevamenti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_ALL);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  HashMap<Long, Vector<TipoCategoriaAnimaleAnagVO>> hSpecCat = new HashMap<Long, Vector<TipoCategoriaAnimaleAnagVO>>();  
  String operazione = request.getParameter("operazione");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoAllevamenti") != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  
  Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova = null;
  if(request.getParameter("regimeInserimentoAllevamenti") == null)
  {
    //Prima volta che entro carico le ute da db
    vAllevamentiAziendaNuova = gaaFacadeClient.getAllevamentiAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  else
  {
    vAllevamentiAziendaNuova = (Vector<AllevamentoAziendaNuovaVO>)session.getAttribute("vAllevamentiAziendaNuova");
  }
  
  
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = null;
  try
  {
    vUteAziendaNuova = gaaFacadeClient.getUteAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoAllevamentiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("vUteAziendaNuova", vUteAziendaNuova);
  
  Vector<TipoASLAnagVO> vAsl = anagFacadeClient.getTipiASL();
  request.setAttribute("vAsl", vAsl);
  
  //ritorna tutti i tipi di specie!!!Anche quelli censiti su bdn
  Vector<TipoSpecieAnimaleAnagVO> vSpecie = anagFacadeClient.getTipiSpecieAnimaleAzProv();
  request.setAttribute("vSpecie", vSpecie);
  
  
  String[] idUteAziendaNuova = request.getParameterValues("idUteAziendaNuova");
  String[] codiceAziendaZootecnica = request.getParameterValues("codiceAziendaZootecnica");
  String[] idAsl = request.getParameterValues("idAsl");
  String[] idSpecieAnimale = request.getParameterValues("idSpecieAnimale");
  String[] idCategoriaAnimale = request.getParameterValues("idCategoriaAnimale");
  String[] numeroCapi = request.getParameterValues("numeroCapi");
  String[] note = request.getParameterValues("note");
  
  
  if((vAllevamentiAziendaNuova != null) && Validator.isNotEmpty(codiceAziendaZootecnica))
  {
	  for(int i=0;i<vAllevamentiAziendaNuova.size();i++)
	  {
	    AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO = vAllevamentiAziendaNuova.get(i);	    
	    
	    Long idUteAziendaNuovaLg = null;
		  if(Validator.isNotEmpty(idUteAziendaNuova[i])) 
		  {
		    idUteAziendaNuovaLg = Long.decode(idUteAziendaNuova[i]);
		  }		  
		  allevamentoAziendaNuovaVO.setIdUteAziendaNuova(idUteAziendaNuovaLg);
		  
		  allevamentoAziendaNuovaVO.setCodiceAziendaZootecnica(codiceAziendaZootecnica[i]);
		  
		  Integer idAslIt = null;
      if(Validator.isNotEmpty(idAsl[i]))
      {
        idAslIt = new Integer(idAsl[i]);
      }
      allevamentoAziendaNuovaVO.setIdAsl(idAslIt);
		  
		  Long idSpecieAnimaleLg = null;
		  if(Validator.isNotEmpty(idSpecieAnimale[i]))
		  {
		    idSpecieAnimaleLg = new Long(idSpecieAnimale[i]);
		  }
		  allevamentoAziendaNuovaVO.setIdSpecieAnimale(idSpecieAnimaleLg);		 
	    
	    if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) 
	      && (hSpecCat.get(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) == null))
	    {
	      Long idSpecieAnimaleTmp = allevamentoAziendaNuovaVO.getIdSpecieAnimale();
	      hSpecCat.put(idSpecieAnimaleTmp, anagFacadeClient.getCategorieByIdSpecie(idSpecieAnimaleTmp));	              
	    }
	    
	    Integer idCategoriaAnimaleIt = null;
      if(Validator.isNotEmpty(idCategoriaAnimale[i]))
      {
        idCategoriaAnimaleIt = new Integer(idCategoriaAnimale[i]);
      }
      allevamentoAziendaNuovaVO.setIdCategoriaAnimale(idCategoriaAnimaleIt);
      
      allevamentoAziendaNuovaVO.setStrNumeroCapi(numeroCapi[i]);
      allevamentoAziendaNuovaVO.setNote(note[i]);	        
	    
	  }
	}
	else if(vAllevamentiAziendaNuova != null)
  {
    for(int i=0;i<vAllevamentiAziendaNuova.size();i++)
    {
      AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO = vAllevamentiAziendaNuova.get(i);
      if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) 
        && (hSpecCat.get(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) == null))
	    {
	      Long idSpecieAnimaleTmp = allevamentoAziendaNuovaVO.getIdSpecieAnimale();
	      hSpecCat.put(idSpecieAnimaleTmp, anagFacadeClient.getCategorieByIdSpecie(idSpecieAnimaleTmp));                
	    }     
    }
  
  }
  
  request.setAttribute("hSpecCat", hSpecCat);
  
  if(Validator.isNotEmpty(operazione) && "cambio".equalsIgnoreCase(operazione))
  {
    //nn fa nulla....
  }
  else if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    vAllevamentiAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisci".equalsIgnoreCase(operazione))
  {
    if(vAllevamentiAziendaNuova == null)
    {
      vAllevamentiAziendaNuova = new Vector<AllevamentoAziendaNuovaVO>();
    }
    vAllevamentiAziendaNuova.add(new AllevamentoAziendaNuovaVO());
  }
  
  
  session.setAttribute("vAllevamentiAziendaNuova", vAllevamentiAziendaNuova);
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoAllevamenti") != null)) 
  {     
    
    if(Validator.isNotEmpty(vAllevamentiAziendaNuova)
      && (vAllevamentiAziendaNuova.size() > 0))
    {
    
	    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	    int countErrori = 0;
	    
	    //Controlli da effettuare
	    Vector<Integer> vAllevamentiVuoti = new Vector<Integer>();
	    for(int i=0;i<vAllevamentiAziendaNuova.size();i++)
	    {
	      int countErroriSingolo = 0;
	      ValidationErrors errors = new ValidationErrors();
	      AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO = vAllevamentiAziendaNuova.get(i);
	      
	      if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale())
	        || Validator.isNotEmpty(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica())
	        || Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdAsl())
	        || Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdCategoriaAnimale())
	        || Validator.isNotEmpty(allevamentoAziendaNuovaVO.getStrNumeroCapi()))
	      {
	      
	      
		      if (Validator.isEmpty(allevamentoAziendaNuovaVO.getIdUteAziendaNuova()))
			    {
			      errors.add("idUteAziendaNuova", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			      countErrori++;
			      countErroriSingolo++;
			    }
			    
			    
			    boolean isSpecieOK = false;
			    boolean codAziendaObbligatorio = false;
			    if (Validator.isEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()))
	        {
	          errors.add("idSpecieAnimale", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        else
	        {
	          isSpecieOK = true;
	          for(int k=0;k<vSpecie.size();k++)
	          {
	            if(allevamentoAziendaNuovaVO.getIdSpecieAnimale()
	              .compareTo(vSpecie.get(k).getIdSpecieAnimaleLong()) == 0)
	            {
	              if("S".equalsIgnoreCase(vSpecie.get(k).getFlagObbligoAsl()))
	              {
	                codAziendaObbligatorio = true;
	              }
	              break;
	            }
	          }
	        }
			    
			    
			    
			    if(isSpecieOK)
			    {
			      if(codAziendaObbligatorio)
			      { 
					    if (Validator.isEmpty(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica()))
			        {
			          errors.add("codiceAziendaZootecnica", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			          countErrori++;
			          countErroriSingolo++;
			        }
			      }
			      
			      
	          if (Validator.isNotEmpty(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica()))
	          {
	            allevamentoAziendaNuovaVO.setCodiceAziendaZootecnica(
	              allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().toUpperCase());
	            if (allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().length() != 8)
	            {
	              errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere lunga 8 caratteri e nel corretto formato."));
	              countErrori++;
	              countErroriSingolo++;
	            }
	            else
	            {
	              // il codice deve essere alfanumerico
	              if (!Validator.isAlphaNumeric(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().trim()))
	              {
	                errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere una stringa alfanumerica"));
	                countErrori++;
	                countErroriSingolo++;
	              }
	              else
	              {
	                // gli ultimi tre caratteri devono avere un valore compreso tra i due seguenti intervalli
	                if (!Validator.isNumericInteger(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().substring(5)))
	                {
	                  // A01 => A99
	                  if (!allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().substring(5, 6).equalsIgnoreCase("A") 
	                      || !Validator.isNumericInteger(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().substring(6)))
	                  {
	                    errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
	                    countErrori++;
	                    countErroriSingolo++;
	                  }
	                  else
	                  {
	                    // 01 => 99
	                    if (new Integer(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().substring(6)).intValue() == 0)
	                    {
	                      errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
	                      countErrori++;
	                      countErroriSingolo++;
	                    }
	                  }
	                }
	                else
	                {
	                  // 001 => 999
	                  if (new Integer(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica().substring(5)).intValue() == 0)
	                  {
	                    errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
	                    countErrori++;
	                    countErroriSingolo++;
	                  }
	                }
	              }
	            }            
	          }
	          
	          
		      }
	        
	        if (Validator.isEmpty(allevamentoAziendaNuovaVO.getIdAsl()))
	        {
	          errors.add("idAsl", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        
	        if (Validator.isEmpty(allevamentoAziendaNuovaVO.getIdCategoriaAnimale()))
	        {
	          errors.add("idCategoriaAnimale", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        if (Validator.isEmpty(allevamentoAziendaNuovaVO.getStrNumeroCapi()))
	        {
	          errors.add("numeroCapi", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        else
	        {
	          if(!Validator.isNumericInteger(allevamentoAziendaNuovaVO.getStrNumeroCapi())) 
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "numeroCapi", AnagErrors.ERR_CAMPO_NON_CORRETTO);
	            countErrori++;
	            countErroriSingolo++;
	          }
	          else 
	          {
	            allevamentoAziendaNuovaVO.setNumeroCapi(new Long(allevamentoAziendaNuovaVO.getStrNumeroCapi()));
	          }
	        }
	        
	        
	        if(countErroriSingolo == 0)
	        {
	          //se nn ci sono stati errori verifico che se è presente su BDN solo per alcune specie
	          boolean flagCodAziendBdn = false;
	          if(vSpecie!=null)
			      {
			        for(int j=0;j<vSpecie.size();j++)
			        {
			          TipoSpecieAnimaleAnagVO specieVO = vSpecie.get(j);
			          if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) 
			            && allevamentoAziendaNuovaVO.getIdSpecieAnimale().toString().equals(specieVO.getIdSpecieAnimale()))
			          {
			            if(!specieVO.isFlagMofCodAzZoot())
			            {
			              flagCodAziendBdn = true;
			              break;
			            }
			            
			          }
			        }
			      }
			      
			      if(flagCodAziendBdn)
			      {
			        Hashtable<BigDecimal,SianAllevamentiVO> elencoAllevamentiBdn = (Hashtable<BigDecimal,SianAllevamentiVO>)session
			          .getAttribute("elencoAllevamentiBdn");
			        if(elencoAllevamentiBdn == null)
			        {
			          try
			          {
			            elencoAllevamentiBdn = gaaFacadeClient.leggiAnagraficaAllevamentiNoProfile(
			              aziendaNuovaVO.getCuaa(), DateUtils.getCurrentDateString());
			            if(Validator.isNotEmpty(elencoAllevamentiBdn)
					          && elencoAllevamentiBdn.containsKey(new BigDecimal(-1)))
					        {
					          SianAllevamentiVO sianAllevamentiVO = elencoAllevamentiBdn.get(new BigDecimal(-1));
                    String messaggio = "SEGNALAZIONE BDN: ";       
	                  errors.add("allevamentoBDN", new ValidationError("Problemi di accesso a BDN: "+messaggio));
	                  countErrori++;
	                  countErroriSingolo++;
					        }
					        else
					        {		              
			              session.setAttribute("elencoAllevamentiBdn", elencoAllevamentiBdn);
			            }
			          }
			          catch(SolmrException se) 
			          {
			            String messaggio = se.getMessage();
			            errors.add("allevamentoBDN", new ValidationError("Problemi di accesso a BDN: "+messaggio));
		              countErrori++;
		              countErroriSingolo++;
			          }
			          
			          if(elencoAllevamentiBdn == null)
	              {
	                errors.add("allevamentoBDN", new ValidationError("NON RISULTA REGISTRATO ALCUN ALLEVAMENTO SULL''ANAGRAFE ZOOTECNICA."));
	                countErrori++;
	                countErroriSingolo++;              
	              }
			        }
		          
		          if((countErroriSingolo == 0)
		            && elencoAllevamentiBdn != null)
		          {
		            boolean trovatoBDN = false;
		            Enumeration<SianAllevamentiVO> enumeraAllevamenti = elencoAllevamentiBdn.elements();
						    while(enumeraAllevamenti.hasMoreElements()) 
						    {
						      SianAllevamentiVO sianAllevamentiVO = (SianAllevamentiVO)enumeraAllevamenti.nextElement();
						      if(sianAllevamentiVO.getAziendaCodice().equals(allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica()))
						      {
						        trovatoBDN = true;
						        break;
						      }
						       
						    }
						    
						    if(!trovatoBDN)
						    {
						      errors.add("allevamentoBDN", new ValidationError("Codice non presente in bdn per l''azienda"));
	                countErrori++;
	                countErroriSingolo++;					    
						    }
		          
		          }
			      
		          
		        }
	          
	        }
	      }
	      else
	      {
	        if(vAllevamentiVuoti == null)
	        {
	          vAllevamentiVuoti = new Vector<Integer>();
	        }       
	        vAllevamentiVuoti.add(new Integer(i));
	      }
        
		    
		    
		    
	      
		    
		    
		    elencoErrori.add(errors);
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {
	      if(Validator.isNotEmpty(vAllevamentiVuoti))
	      {
	        Collections.reverse(vAllevamentiVuoti);
	        for(int j=0;j<vAllevamentiVuoti.size();j++)
	        {
	          vAllevamentiAziendaNuova.remove(vAllevamentiVuoti.get(j).intValue());       
	        }
	      }
	       
	      
		    try 
		    {
		      if(vAllevamentiAziendaNuova.size() > 0)
          {
            gaaFacadeClient.aggiornaAllevamentiAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), vAllevamentiAziendaNuova);
          }
          else
          {
            //Cancello tutto...rimaste vuoto tutti i tasti...
            gaaFacadeClient.aggiornaAllevamentiAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
          }
		    
		      
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoAllevamentiCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
		    
		    %>
		      <jsp:forward page="<%= pageNext %>"/>
		    <%
		    return;		    
		    
		  }
		}
		else
		{
		
		  //Cancello tutto
		  try 
      {
        gaaFacadeClient.aggiornaAllevamentiAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoAllevamentiCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
		
		  %>
	      <jsp:forward page="<%= pageNext %>"/>
	    <%
	    return;
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoAllevamentiUrl %>"/>
  
