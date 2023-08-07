<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popAggiungiUsoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String popAggiungiUsoUrl = "/view/popAggiungiUsoView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	it.csi.solmr.dto.CodeDescription[] elencoIndirizziUtilizzi = null;
	ValidationError error = null;
	ValidationErrors errors = new ValidationErrors();
	
	String type = request.getParameter("type");
	request.setAttribute("type", type);
	// Parametro che arriva dalla pop-up dati territoriali, dalla pagina di modifica:
	// o dalla pagina di inserimento della particella e indica il numero di utilizzo a cui si riferisce.
	// Indispensabile per settare correttamente il valore di default relativo
	// alla combo selezionata
	//String indice = null;
	Long idConduzione = null;
	String provenienza = request.getParameter("provenienza");
	String numTotaleUtilizzo = request.getParameter("numTotaleUtilizzo");
	//String idParticellaCertificata = request.getParameter("idParticellaCertificata");
	
 
	
	if(Validator.isNotEmpty(request.getParameter("idConduzioneParticella"))) {
		session.setAttribute("idConduzione", Long.decode(request.getParameter("idConduzioneParticella")));
	}
	else {
		idConduzione = (Long)session.getAttribute("idConduzione");
		session.setAttribute("idConduzione", idConduzione);
	}
	
	
	Long idIndirizzoUtilizzo = null;
	if(Validator.isNotEmpty(request.getParameter("idIndirizzoUtilizzo"))) {
		idIndirizzoUtilizzo = Long.decode(request.getParameter("idIndirizzoUtilizzo"));
		request.setAttribute("idIndirizzoUtilizzo", idIndirizzoUtilizzo);
	}
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	try 
	{
		if(type.equalsIgnoreCase("Primario")) 
		{
			elencoIndirizziUtilizzi = anagFacadeClient.getListTipoIndirizzoUtilizzo(SolmrConstants.FLAG_N, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, true);
		}
		else 
		{
			elencoIndirizziUtilizzi = anagFacadeClient.getListTipoIndirizzoUtilizzo(SolmrConstants.FLAG_S, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, true);
		}
		request.setAttribute("elencoIndirizziUtilizzi", elencoIndirizziUtilizzi);
	}
	catch(SolmrException se) 
	{
		error = new ValidationError(AnagErrors.ERRORE_KO_INDIRIZZO_UTILIZZO);
		errors.add("idIndirizzoUtilizzo", error);
		request.setAttribute("errors", errors);
		%>
			<jsp:forward page="<%= popAggiungiUsoUrl %>" />
		<%
	}
	
	// L'utente ha selezionato il pulsante annulla
	if(Validator.isNotEmpty(request.getParameter("annulla"))) 
	{
		if(Validator.isNotEmpty(provenienza) && "modificaMultipla".equalsIgnoreCase(provenienza)) 
		{
			%>
				<html>
					<head>
    					<script type="text/javascript">
    						//window.opener.location.href='../layout/terreniParticellareModificaMultipla.htm';
    						window.close();
    					</script>
  					</head>
				</html>
    	<%
    	return;
    }
    else if(Validator.isNotEmpty(provenienza) && "modificaTerreni".equalsIgnoreCase(provenienza)) 
    {
      %>
        <html>
          <head>
              <script type="text/javascript">
                //window.opener.location.href='../layout/terreniParticellareModifica.htm';
                window.close();
              </script>
            </head>
        </html>
      <%
      return;
    }
    else if(Validator.isNotEmpty(provenienza) && "popModTerrCondUso".equalsIgnoreCase(provenienza)) 
    {
      %>
        <html>
          <head>
              <script type="text/javascript">
                //window.opener.location.href='../layout/popModificaTerritorialeCondUso.htm';
                window.close();
              </script>
            </head>
        </html>
      <%
      return;
    }
    else if(Validator.isNotEmpty(provenienza) && "inserisciParticellareCondUso".equalsIgnoreCase(provenienza)) 
    {
      %>
        <html>
          <head>
              <script type="text/javascript">
                //window.opener.location.href='../layout/terreniParticellareInserisciCondUso.htm';
                window.close();
              </script>
            </head>
        </html>
      <%
      return;
    }    
	}
	else 
	{
		TipoUtilizzoVO[] elencoTipoUtilizzoVO = null;
		if(idIndirizzoUtilizzo != null) 
		{
			try 
			{
				String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
				if(type.equalsIgnoreCase("Primario")) 
				{
					elencoTipoUtilizzoVO = anagFacadeClient.getListTipiUsoSuoloByIdIndirizzoUtilizzo(idIndirizzoUtilizzo, true, orderBy, null);
				}
				else 
				{
					elencoTipoUtilizzoVO = anagFacadeClient.getListTipiUsoSuoloByIdIndirizzoUtilizzo(idIndirizzoUtilizzo, true, orderBy, SolmrConstants.FLAG_S);
				}
			}
			catch(SolmrException se) 
			{
				error = new ValidationError(AnagErrors.ERRORE_KO_USO_PRIMARIO);
				errors.add("idTipoUtilizzo", error);
				request.setAttribute("errors", errors);
				%>
					<jsp:forward page="<%= popAggiungiUsoUrl %>" />
				<%
			}
		}
			
		Vector elenco = new Vector();
		
		String idUtilizzoPar = "";
		String descUtilizzoPar = "";
		// L'utente ha premuto il pulsante conferma dalla pop-up
		if(Validator.isNotEmpty(request.getParameter("aggiungi"))) 
		{
			// Controllo che sia stato inserito il codice uso o che sia stato
			// selezionato un valore dalla combo
			if(!Validator.isNotEmpty(request.getParameter("codiceUso")) 
			  && !Validator.isNotEmpty(request.getParameter("idTipoUtilizzo"))) 
			{
				error = new ValidationError(AnagErrors.ERRORE_USO_PRIMARIO_OBBLIGATORIO);
				errors.add("idTipoUtilizzo", error);
				request.setAttribute("errors", errors);
				%>
					<jsp:forward page="<%= popAggiungiUsoUrl %>" />
				<%	
			}
			else 
			{
				Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuolo"+type);
				if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
				{
					if(type.equalsIgnoreCase("Primario")) 
					{
						try 
						{
				 			elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
			 			}
			 			catch(SolmrException se) 
			 			{
							error = new ValidationError(AnagErrors.ERRORE_KO_USO_PRIMARIO);
							errors.add("idTipoUtilizzo", error);
							request.setAttribute("errors", errors);
							%>
								<jsp:forward page="<%= popAggiungiUsoUrl %>" />
							<%
			 			}
					}
					else 
					{
						try 
						{
				 			elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
			 			}
			 			catch(SolmrException se) 
			 			{
							error = new ValidationError(AnagErrors.ERRORE_KO_USO_SECONDARIO);
							errors.add("idTipoUtilizzo", error);
							request.setAttribute("errors", errors);
							%>
								<jsp:forward page="<%= popAggiungiUsoUrl %>" />
							<%
			 			}
					}
				}
				// Aggiungo l'utilizzo in relazione al valore della combo relativa all'uso del suolo
				if(Validator.isNotEmpty(request.getParameter("idTipoUtilizzo"))) 
				{
					for(int i = 0; i < elencoTipoUtilizzoVO.length; i++) 
					{
						TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipoUtilizzoVO[i];
						if(tipoUtilizzoVO.getIdUtilizzo().compareTo(Long.decode(request.getParameter("idTipoUtilizzo"))) == 0) 
						{
					 		elencoTipiUsoSuolo.add(tipoUtilizzoVO);
					 		idUtilizzoPar = ""+tipoUtilizzoVO.getIdUtilizzo();
					 		descUtilizzoPar = "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione().replace("'", "\\'");
					 		break;
					 	}
					}
					session.setAttribute("elencoTipiUsoSuolo"+type, elencoTipiUsoSuolo);
					session.setAttribute("idTipoUtilizzo"+type, request.getParameter("idTipoUtilizzo"));
				}
				// Aggiungo l'utilizzo in relazione al valore del codice uso
				else 
				{
					// Ricerco tutti gli utilizzi in funzione del codice
					try 
					{
						String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
						if(type.equalsIgnoreCase("Primario")) 
						{
							elencoTipoUtilizzoVO = anagFacadeClient.getListTipiUsoSuoloByCodice(request.getParameter("codiceUso"), true, orderBy, null, null);
						}
						else 
						{
							elencoTipoUtilizzoVO = anagFacadeClient.getListTipiUsoSuoloByCodice(request.getParameter("codiceUso"), true, orderBy, SolmrConstants.FLAG_S, null);
						}
						// Se non sono stati trovati usi in relazione al codice inserito
						if(elencoTipoUtilizzoVO == null || elencoTipoUtilizzoVO.length == 0) 
						{
							error = new ValidationError(AnagErrors.ERRORE_NO_USO_DISP_FOR_CODICE);
							errors.add("idTipoUtilizzo", error);
							request.setAttribute("errors", errors);
							%>
								<jsp:forward page="<%= popAggiungiUsoUrl %>" />
							<%
						}
						// Altrimenti
						else 
						{
							for(int a = 0; a < elencoTipoUtilizzoVO.length; a++) 
							{
					 			boolean isPresent = false;
					 			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipoUtilizzoVO[a];
					 			for(int i = 0; i < elencoTipiUsoSuolo.size(); i++) 
					 			{
					 				if(tipoUtilizzoVO.getIdUtilizzo().compareTo(((TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(i)).getIdUtilizzo()) == 0) 
					 				{
					 					isPresent = true;
					 					break;
					 				}
					 			}
					 			if(!isPresent) 
					 			{					 			
					 				session.setAttribute("idTipoUtilizzo"+type, tipoUtilizzoVO.getIdUtilizzo().toString());
					 				idUtilizzoPar = ""+tipoUtilizzoVO.getIdUtilizzo();
                  descUtilizzoPar = "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione().replace("'", "\\'");
					 				elencoTipiUsoSuolo.add(tipoUtilizzoVO);
					 			}
					 			else
					 			{					 			
						 			error = new ValidationError(AnagErrors.ERRORE_CODICE_USO_PRESENTE);
		              errors.add("codiceUso", error);
		              request.setAttribute("errors", errors);
		              %>
		                <jsp:forward page="<%= popAggiungiUsoUrl %>" />
		              <%
	              }
					 		}
						}
						session.setAttribute("elencoTipiUsoSuolo"+type, elencoTipiUsoSuolo);
					}
					catch(SolmrException se) 
					{
						if(type.equalsIgnoreCase("Primario")) 
						{
							error = new ValidationError(AnagErrors.ERRORE_KO_USO_PRIMARIO);
						}
						else 
						{
							error = new ValidationError(AnagErrors.ERRORE_KO_USO_SECONDARIO);
						}
						errors.add("idTipoUtilizzo", error);
						request.setAttribute("errors", errors);
						%>
							<jsp:forward page="<%= popAggiungiUsoUrl %>" />
						<%
					}
				}
			}
			
			
			
			
			// Vado alla pagina chiamante
			if(Validator.isNotEmpty(provenienza) && "modificaMultipla".equalsIgnoreCase(provenienza))
      {
        if(type.equalsIgnoreCase("Primario"))
        {
          %>
          <html>
            <head>
                <script type="text/javascript">
                  //window.opener.location.href='../layout/terreniParticellareModificaMultipla.htm';
                  window.opener.aggiungiUsoPrimario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>');
                  window.close();
                </script>
              </head>
          </html>
          <%
        }
        else
        {
          %>
          <html>
            <head>
                <script type="text/javascript">
                  //window.opener.location.href='../layout/terreniParticellareModificaMultipla.htm';
                  window.opener.aggiungiUsoSecondario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>');
                  window.close();
                </script>
              </head>
          </html>
          <%          
        }
        return;
	    }
	    // Arrivo dalla pagina di modifica terreni
    	else if(Validator.isNotEmpty(provenienza) && "modificaTerreni".equalsIgnoreCase(provenienza))
    	{
    		if(type.equalsIgnoreCase("Primario"))
   			{
    			%>
					<html>
						<head>
    						<script type="text/javascript">
    							//window.opener.location.href='../layout/terreniParticellareModifica.htm';
    							window.opener.aggiungiUsoPrimario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
	    						window.close();
    						</script>
  						</head>
					</html>
    			<%
    	  }
    	  else
    	  {
    	    %>
           <html>
             <head>
                 <script type="text/javascript">
                   //window.opener.location.href='../layout/terreniParticellareModifica.htm';
                   window.opener.aggiungiUsoSecondario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
                   window.close();
                 </script>
               </head>
           </html>
           <%	    	  
    	  }
   			return;
   		}
   		else if(Validator.isNotEmpty(provenienza) && "popModTerrCondUso".equalsIgnoreCase(provenienza))
      {
        if(type.equalsIgnoreCase("Primario"))
        {
          %>
          <html>
            <head>
                <script type="text/javascript">
                  //window.opener.location.href='../layout/popModificaTerritorialeCondUso.htm';
                  window.opener.aggiungiUsoPrimario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
                  window.close();
                </script>
              </head>
          </html>
          <%
        }
        else
        {
          %>
           <html>
             <head>
                 <script type="text/javascript">
                   //window.opener.location.href='../layout/popModificaTerritorialeCondUso.htm';
                   window.opener.aggiungiUsoSecondario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
                   window.close();
                 </script>
               </head>
           </html>
           <%         
        }
        return;
      }
      else if(Validator.isNotEmpty(provenienza) && "inserisciParticellareCondUso".equalsIgnoreCase(provenienza))
      {
        if(type.equalsIgnoreCase("Primario"))
        {
          %>
          <html>
            <head>
                <script type="text/javascript">
                  //window.opener.location.href='../layout/terreniParticellareInserisciCondUso.htm';
                  window.opener.aggiungiUsoPrimario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
                  window.close();
                </script>
              </head>
          </html>
          <%
        }
        else
        {
          %>
           <html>
             <head>
                 <script type="text/javascript">
                   //window.opener.location.href='../layout/terreniParticellareInserisciCondUso.htm';
                   window.opener.aggiungiUsoSecondario('<%=idUtilizzoPar %>', '<%=descUtilizzoPar %>', '<%=numTotaleUtilizzo %>');
                   window.close();
                 </script>
               </head>
           </html>
           <%         
        }
        return;
      }
    	
		} //fine premuto aggiungi ....
		else 
		{
			if(idIndirizzoUtilizzo != null) 
			{
				elenco = new Vector();
				Vector elencoUtiAzienda = new Vector();
				Vector elencoTipiUsoSuolo = (Vector)session.getAttribute("elencoTipiUsoSuolo"+type);
				// Estraggo solo gli utilizzi non presenti nella pagina chiamante
				if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
				{
	 				try 
	 				{
						if(type.equalsIgnoreCase("Primario")) 
						{
							elencoUtiAzienda = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
						}
						else 
						{
							elencoUtiAzienda = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
						}
	 				}
	 				catch(SolmrException se) 
	 				{
	 					error = new ValidationError(AnagErrors.ERRORE_KO_USO_PRIMARIO);
						errors.add("idTipoUtilizzo", error);
						request.setAttribute("errors", errors);
						%>
							<jsp:forward page="<%= popAggiungiUsoUrl %>" />
						<%
	 				}
				}
				else 
				{
					elencoUtiAzienda = elencoTipiUsoSuolo;
				}
		 		
	 			for(int a = 0; a < elencoTipoUtilizzoVO.length; a++) 
	 			{
	 				boolean isPresent = false;
	 				TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipoUtilizzoVO[a];
		 			if(elencoUtiAzienda != null && elencoUtiAzienda.size() > 0) 
		 			{
		 				for(int i = 0; i < elencoUtiAzienda.size(); i++) 
		 				{
			 				if(tipoUtilizzoVO.getIdUtilizzo().compareTo(((TipoUtilizzoVO)elencoUtiAzienda.elementAt(i)).getIdUtilizzo()) == 0) 
			 				{
			 					isPresent = true;
			 					break;
			 				}
			 			}
		 				if(!isPresent) 
		 				{
		 					elenco.add(tipoUtilizzoVO);	 					
		 				}
		 			}
		 			else 
		 			{
		 				elenco.add(tipoUtilizzoVO);
		 			}
		 		}
		 		if(elenco.size() == 0) 
		 		{
		 			error = new ValidationError(AnagErrors.ERRORE_NO_USO_DISP_FOR_INDIRIZZO);
					errors.add("idTipoUtilizzo", error);
					request.setAttribute("errors", errors);
					%>
						<jsp:forward page="<%= popAggiungiUsoUrl %>" />
					<%
		 		}
		 		else 
		 		{
		 			request.setAttribute("elencoTipiUsoSuolo"+type, (TipoUtilizzoVO[])elenco.toArray(new TipoUtilizzoVO[elenco.size()]));
				}
			}
		}
	}
	
	%>
  	<jsp:forward page="<%=popAggiungiUsoUrl%>" />
  <%

%>

