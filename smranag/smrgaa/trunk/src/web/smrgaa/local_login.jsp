<%@page import="it.csi.csi.porte.proxy.PDProxy"%>
<%@page import="it.csi.csi.util.xml.PDConfigReader"%>
<%@page import="it.csi.csi.porte.InfoPortaDelegata"%>
<%@page import="it.csi.iride2.policy.entity.Identita"%>
<%@page import="it.csi.iride2.policy.interfaces.PolicyEnforcerBaseService"%>
<%@page import="java.util.HashMap"%>

<%!private static final String   DEFAULT_USER           = "CSI.DEMO 28@IPA";
  private static final String[] CODICI_FISCALI         =
                                                       { "AAAAAA00B77B000F", "AAAAAA00A11B000J", "AAAAAA00A11C000K", "AAAAAA00A11D000L", "AAAAAA00A11E000M",
                                                       "AAAAAA00A11F000N", "AAAAAA00A11G000O", "AAAAAA00A11H000P", "AAAAAA00A11I000Q", "AAAAAA00A11J000R",
                                                       "AAAAAA00A11K000S", "AAAAAA00A11L000T",
                                                       "AAAAAA00A11M000U", "AAAAAA00A11N000V", "AAAAAA00A11O000W", "AAAAAA00A11R000Z", "AAAAAA00A11S000A",
                                                       "AAAAAA00A11T000B", "AAAAAA00A11U000C",
                                                       "AAAAAA00A11V000D", "AAAAAA00A11P000X"};
  
  
  
  private static final int[]    LIVELLI_AUTENTICAZIONE =
                                                       { 1, 2, 4, 8, 16 };
  private static final String   SCELTA_RUOLO_WRUP           = "loginssl?portalName=ruparTOBECONFIG";
  private static final String   SCELTA_RUOLO_SISP           = "loginssl?portalName=sistemaTOBECONFIG";
  private static final String   SCELTA_RUOLO_BAS            = "loginssl?portalName=applicazioni";%>
<html>
<body>
	<form name="loginForm" action="" method="post">
		<%
		
		
		
		  HashMap<String,String> hmCodFisc = new HashMap<String,String>();
		  hmCodFisc.put("CSI.DEMO 20@IPA","AAAAAA00B77B000F");
		  hmCodFisc.put("CSI.DEMO 21@IPA","AAAAAA00A11B000J");
		  hmCodFisc.put("CSI.DEMO 22@IPA","AAAAAA00A11C000K");
		  hmCodFisc.put("CSI.DEMO 23@IPA","AAAAAA00A11D000L");
		  hmCodFisc.put("CSI.DEMO 24@IPA","AAAAAA00A11E000M");
		  hmCodFisc.put("CSI.DEMO 25@IPA","AAAAAA00A11F000N");
		  hmCodFisc.put("CSI.DEMO 26@IPA","AAAAAA00A11G000O");
		  hmCodFisc.put("CSI.DEMO 27@IPA","AAAAAA00A11H000P");
		  hmCodFisc.put("CSI.DEMO 28@IPA","AAAAAA00A11I000Q");		 
		  hmCodFisc.put("CSI.DEMO 29@IPA","AAAAAA00A11J000R");
		  hmCodFisc.put("CSI.DEMO 30@IPA","AAAAAA00A11K000S");
		  hmCodFisc.put("CSI.DEMO 31@IPA","AAAAAA00A11L000T");
		  hmCodFisc.put("CSI.DEMO 32@IPA","AAAAAA00A11M000U");
		  hmCodFisc.put("CSI.DEMO 33@IPA","AAAAAA00A11N000V");
		  hmCodFisc.put("CSI.DEMO 34@IPA","AAAAAA00A11O000W");
		  hmCodFisc.put("CSI.DEMO 35@IPA","AAAAAA00A11R000Z");
		  hmCodFisc.put("CSI.DEMO 36@IPA","AAAAAA00A11S000A");
		  hmCodFisc.put("CSI.DEMO 37@IPA","AAAAAA00A11T000B");
		  hmCodFisc.put("CSI.DEMO 38@IPA","AAAAAA00A11U000C");
		  hmCodFisc.put("CSI.DEMO 39@IPA","AAAAAA00A11V000D");
		  hmCodFisc.put("CSI.DEMO 40@IPA","AAAAAA00A11P000X");		 
		
		  String user = request.getParameter("user");
		  if (user != null)
		  {
			  /*
		    PolicyEnforcerBaseService iridePEP = null;
		    iridePEP = getPolicyEnforcerPDProxy(request);
		    Identita identita = iridePEP.identificaUserPassword(user, "TOBECONFIG");		    
		    // Setto valori nell'oggetto Identita (questi valori verranno passati in input al servizio di papuaserv login())
		    
		    System.out.println("\n\n identita.getCodFiscale():"+ identita.getCodFiscale() +"n\n");
		    System.out.println("\n\n identita.getCognome():"+ identita.getCognome() +"n\n");
		    System.out.println("\n\n identita.getNome():"+ identita.getNome() +"n\n");
		    System.out.println("\n\n identita.getLivelloAutenticazione():"+ identita.getLivelloAutenticazione() +"n\n");
		    */
		    
		    Identita identita = new Identita();
		    
		    System.err.println("-- user ="+user);
		    String codiceFiscale = hmCodFisc.get(user);
		    System.err.println("-- codiceFiscale ="+codiceFiscale);
		    String cognome = user.substring(4,user.length()-4);
		    System.err.println("-- cognome ="+cognome);
		    String nome = "TOBECONFIG";
		    System.err.println("-- nome ="+nome);
		    int livelloAutenticazione  = 2;
		    System.err.println("-- livelloAutenticazione ="+livelloAutenticazione);
		    
		    identita.setCodFiscale(codiceFiscale);
		    identita.setCognome(cognome);
		    identita.setNome(nome);
		    identita.setLivelloAutenticazione(livelloAutenticazione);
		    
		    session.setAttribute("identita", identita);
		    
		    
		    if ("sisp".equals(request.getParameter("provider")))
		    {
		      response.sendRedirect(SCELTA_RUOLO_SISP);
		    }
		    else if("wrup".equals(request.getParameter("provider")))
		    {
              response.sendRedirect(SCELTA_RUOLO_WRUP);
		    }
		    else if("bas".equals(request.getParameter("provider"))){
		    	response.sendRedirect(SCELTA_RUOLO_BAS);
		    }	
		  }
		%>
		User: <select name="user">
			<%
			  for (int i = 0; i < CODICI_FISCALI.length; ++i)
			  {
			%>
			<option value="CSI.DEMO <%=i + 20%>@IPA"
				<%=checked("CSI.DEMO " + (i + 20) + "@IPA", user)%>>CSI.DEMO
				<%=i + 20%></option>
			<%
			  }
			%>
		</select>
		<select name="provider">		  
		  <option value="bas"<%=("bas".equals(request.getParameter("provider"))?"checked=''":"") %>>Utenti TOBECONFIG</option>
		</select>
		 <input type="submit" name="login" value="login" />
	</form>
</body>
</html><%!private String checked(String current, String cfSelected)
  {
    if (cfSelected == null || cfSelected.length() == 0)
    {
      cfSelected = DEFAULT_USER;
    }
    return cfSelected.equals(current) ? "selected='selected'" : "";
  }%><%!
  
  /*
 protected static InfoPortaDelegata _infoPortaDelegataPEP = null;

  public PolicyEnforcerBaseService getPolicyEnforcerPDProxy(HttpServletRequest request) throws Exception
  {
    String pepAttributeName = PolicyEnforcerBaseService.class.getName();
    PolicyEnforcerBaseService policyEnforcerBaseService = null;
    if (request != null)
    {
      policyEnforcerBaseService = (PolicyEnforcerBaseService) request.getAttribute(pepAttributeName);
    }
    if (policyEnforcerBaseService == null)
    {
      policyEnforcerBaseService = createPEP();
      if (request != null)
      {
        request.setAttribute(pepAttributeName, policyEnforcerBaseService);
      }
    }
    return policyEnforcerBaseService;
  }

  private synchronized PolicyEnforcerBaseService createPEP() throws Exception
  {
    if (_infoPortaDelegataPEP == null)
    {
      loadPDConfig();
    }
    return (PolicyEnforcerBaseService) PDProxy.newInstance(_infoPortaDelegataPEP);
  }

  private synchronized void loadPDConfig() throws Exception
  {
    if (_infoPortaDelegataPEP == null)
    {
      _infoPortaDelegataPEP = PDConfigReader.read(this.getClass().getClassLoader().getResourceAsStream("/it/csi/solmr/etc/services/defPDPEPEJB.xml"));
    }    
  }
  */
  %>