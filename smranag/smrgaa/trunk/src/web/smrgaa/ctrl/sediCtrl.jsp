<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%!
	private Vector getUTE(PageContext pc,Boolean storico) throws SolmrException, Exception 
	{
   	pc.removeAttribute("v_ute", PageContext.SESSION_SCOPE);
   	AnagAziendaVO anagVO = (AnagAziendaVO)pc.getAttribute("anagAziendaVO",PageContext.SESSION_SCOPE);
   	Vector v_centri = null;
   	AnagFacadeClient client = new AnagFacadeClient();
   	v_centri = client.getUTE(anagVO.getIdAzienda(),storico);
   	if(v_centri!= null) 
   	{
   		pc.setAttribute("v_ute", v_centri, PageContext.SESSION_SCOPE);
   	}
   	return v_centri;
 	}
%>
<%
	
	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);

 	// Per evitare problemi non tocchiamo l'url sediUteModView.jsp tanto
 	// l'utente non dovrebbe arrivarci perchè non dovrebbe avere il pulsante abilitato
 	// e se ci arriva rimane comunque poi bloccato dal controller quando fa salva

 	String iridePageName = "sediCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	UteVO uteVO = null;
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();
  String url = "/view/sediView.jsp";

 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try 
 	{
   	client.checkStatoAzienda(anagVO.getIdAzienda());
 	}
 	catch(SolmrException se) 
 	{
   	request.setAttribute("statoAzienda", se);
 	}

 	Long idUte = null;
 	if(request.getParameter("operazione")!= null && request.getParameter("operazione").equals("cancella"))
 	{
   	// Recupero l'elemento selezionato dall'utente
   	String elementoSelezionato = request.getParameter("radioDettaglio");

   	// Controllo che l'utente abbia effettivamente selezionato qualcosa
   	if(!Validator.isNotEmpty(elementoSelezionato)) 
   	{
    	error = new ValidationError((String)AnagErrors.get("SELEZIONA_UTE"));
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
   	}

   	// Se l'utente ha selezionato qualcosa recupero l'id dell'unità produttiva
   	// selezionata
   	idUte = new Long(elementoSelezionato);

   	// Controllo che l'ute che si vuole eliminare non sia cessata
   	try 
   	{
    	uteVO = client.getUteById(idUte);
   	}
   	catch(SolmrException se) 
   	{
    	error= new ValidationError(se.getMessage());
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
   	}
   	if(uteVO.getDataFineAttivita() != null) 
   	{
    	error= new ValidationError((String)AnagErrors.get("ERR_UTE_GIA_CESSATA"));
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
   	}

   	session.setAttribute("idUte", idUte);
   	url = "/layout/confirmDel.htm";
 	}
 	if(request.getParameter("operazione")!=null&&request.getParameter("operazione").equals("elimina"))
 	{
   	// Recupero l'elemento selezionato dall'utente
   	String elementoSelezionato = request.getParameter("radioDettaglio");

    // Controllo che l'utente abbia effettivamente selezionato qualcosa
    if(!Validator.isNotEmpty(elementoSelezionato)) 
    {
      error = new ValidationError((String)AnagErrors.get("SELEZIONA_UTE"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    // Se l'utente ha selezionato qualcosa recupero l'id dell'unità produttiva
    // selezionata
    idUte = new Long(elementoSelezionato);
    session.setAttribute("idUte", idUte);
    url = "/layout/confirmDel.htm";
 	}
 	else if(request.getParameter("operazione") !=null 
 	  && request.getParameter("operazione").equals("inserisci"))
 	{
   	url = "/layout/sedi_new.htm";
 	}
 	else if(request.getParameter("operazione")!=null && request.getParameter("operazione").equals("modifica"))
 	{
    session.removeAttribute("voModifica");
    if(request.getParameter("radioDettaglio")!= null) 
    {
    	url = "/view/sediUteModView.jsp";
    	String elencoSediUrl = "/view/sediView.jsp";
			
			// Recupero le zone altimetriche
 	  	it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = null;
 	  	try 
 	  	{
 	  		elencoZonaAltimetrica = client.getListTipoZonaAltimetrica(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
 	  		request.setAttribute("elencoZonaAltimetrica", elencoZonaAltimetrica);
 	 	  }
 	  	catch(SolmrException se) 
 	  	{
 	  		error = new ValidationError(AnagErrors.ERRORE_KO_ZONA_ALTIMETRICA);
 	  		errors.add("error", error);
 	  		request.setAttribute("errors", errors);
 	  		request.getRequestDispatcher(url).forward(request, response);
 	  		return;
 	  	}
      		
     	String idUteStr = request.getParameter("radioDettaglio");
     	Vector v_ute = (Vector)session.getAttribute("v_ute");
    	if(v_ute!=null) 
    	{
     		for(int i = 0; i < v_ute.size(); i++) 
     		{
       		UteVO vo = (UteVO)v_ute.elementAt(i);
       		if(vo.getIdUte().toString().equals(idUteStr)) 
       		{
         		try 
         		{
           	  UteVO voModifica = client.getUteById(vo.getIdUte());
           		voModifica.setIdUte(vo.getIdUte());
					    request.setAttribute("idZonaAltimetrica", voModifica.getIdZonaAltimetrica().toString());
	            // Se la data di fine attività è valorizzata controllo che non coincida o sia
	            // posteriore a quella di sistema altrimenti impedisco che il record venga modificato
	            if(voModifica.getDataFineAttivita() != null) 
	            {
	             	if(voModifica.getDataFineAttivita().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0 
	             	  || !voModifica.getDataFineAttivita().after(DateUtils.parseDate(DateUtils.getCurrent()))) 
	             	{
	              	errors.add("error", new ValidationError((String)AnagErrors.get("ERR_UNITA_PRODUTTIVA_NO_MODIFICABILE")));
	              	request.setAttribute("errors", errors);
	              	request.getRequestDispatcher(elencoSediUrl).forward(request, response);
	              	return;
	              }
	            }
            	voModifica.setIdAzienda(vo.getIdAzienda());
            	session.setAttribute("load","true");
           		session.setAttribute("voModifica",voModifica);
          	}
            catch(SolmrException sex) 
            {
            	error = new ValidationError(sex.getMessage());
				      errors.add("error", error);
				      request.setAttribute("errors", errors);
				      request.getRequestDispatcher(url).forward(request, response);
				      return;
            }
           	break;
          }
        }
      }
    }
    // Altrimenti se l'utente non ha selezionato nulla segnalo un messaggio di errore
    else 
    {
    	error = new ValidationError((String)AnagErrors.get("SELEZIONA_UTE"));
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
  }
  else if(request.getParameter("operazione")!=null 
    && request.getParameter("operazione").equals("dettaglio"))
  {
   	//controllo se è stato checkato un valore del radioButton e memorizzo l'id selezionato...
   	if(request.getParameter("radioDettaglio")!= null) 
   	{
    	idUte = new Long(request.getParameter("radioDettaglio"));
    	try 
    	{
      	uteVO = client.getUteById(idUte);
      	session.setAttribute("uteVO", uteVO);
      }
      catch (SolmrException ex) 
      {
      	error = new ValidationError(ex.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(url).forward(request, response);
      	return;
      }
      url = "/layout/sedi_det.htm";
    }
    // Altrimenti segnalo all'utente che non è stato selezionato nulla
    // dall'elenco
    else 
    {
    	error = new ValidationError((String)AnagErrors.get("ERR_SELEZIONE_ELENCO_KO"));
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
  }
  else if(request.getParameter("operazione") != null 
    && request.getParameter("operazione").equals("aggiorna")) 
  {
   	try 
   	{
    	if("si".equals(request.getParameter("storico"))) 
    	{
      	getUTE(pageContext,new Boolean(true));
      }
      else 
      {
      	getUTE(pageContext,new Boolean(false));
      }
    }
    catch(SolmrException ex) 
    {
    	error = new ValidationError(ex.getMessage());
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher("/view/sediView.jsp").forward(request, response);
    	return;
    }

    if(anagVO.getDataCessazione()!=null && !anagVO.getDataCessazione().equals("") 
      || anagVO.getDataFineVal()!=null&&!anagVO.getDataFineVal().equals(""))
    {
    	request.setAttribute("dittaKO", "true");
    }
    if(request.getAttribute("cancOK") != null) 
    {
    	request.setAttribute("messaggio", request.getAttribute("cancOK"));
    }
  }
  else 
  {
   	// Pulisco la sessione dagli oggetti relativi alle area coinvolte nel medesimo macro d'uso
   	// (Visualizza Dati Azienda) che non sono neccesari alle funzionalità presenti sotto la voce
   	// di menù unità produttive
   	session.removeAttribute("v_soggetti");
   	session.removeAttribute("uteVO");
   	try 
   	{
    	//antlr.arrivo devo visualizzare solo le ute con data fine validità
    	//uguale a null
    	session.setAttribute("v_ute", getUTE(pageContext,new Boolean(false)));
    }
    catch(SolmrException ex) 
    {
    	if(!ex.getMessage().equalsIgnoreCase((String)AnagErrors.get("RICERCA_TERRENI_UTE"))) 
    	{
      	error = new ValidationError(ex.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher("/view/sediView.jsp").forward(request, response);
      	return;
      }
      else 
      {
      	// Creo un vettore vuoto in modo da poter gestire correttamente il menù
      	session.setAttribute("v_ute", new Vector());
      }
    }

    if(anagVO.getDataCessazione() != null && !anagVO.getDataCessazione().equals("") 
      || anagVO.getDataFineVal() != null && !anagVO.getDataFineVal().equals(""))
    {
    	request.setAttribute("dittaKO", "true");
    }
    if(request.getAttribute("cancOK") != null) 
    {
    	request.setAttribute("messaggio", request.getAttribute("cancOK"));
    }
  }
%>
<jsp:forward page="<%=url%>"/>
