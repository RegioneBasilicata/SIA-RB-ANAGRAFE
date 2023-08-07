<%@page import="it.csi.solmr.dto.profile.UtenteIride2VO"%>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>
<html>
<body>
<form name="user" action="user.jsp" method="post">
<%
  String idUtente=request.getParameter("idUtente");
  if (idUtente!=null)
  {
    UtenteIride2VO ui2VO= new UtenteIride2VO();
    AnagFacadeClient client= new AnagFacadeClient();
    UtenteAbilitazioni utenteAbilitazioni = client.getUtenteAbilitazioniByIdUtenteLogin(new Long(idUtente.trim()));
    RuoloUtenza ru = new RuoloUtenzaPapua(utenteAbilitazioni); 
    ui2VO.setCodiceEnte(ru.getCodiceEnte());
    ui2VO.setCodiceFiscale(ru.getCodiceFiscale());
    ui2VO.setCodiceRuolo(ru.getCodiceRuolo());
    ui2VO.setDenominazione(ru.getDenominazione());
    ui2VO.setDirittoAccesso(ru.getDirittoAccesso());
    ui2VO.setIdProcedimento(new Long(7));//Anagrafe
    //ProfiloUtenza p = client.loginPU(ui2VO);
    //session.setAttribute("profile",p);
    //session.setAttribute("ruoloUtenza",p.getRuoloUtenza());
    session.setAttribute("pathToFollow","rupar");
    String utente="CF:"+ru.getCodiceFiscale()+ ", Denominazione: "+ru.getDenominazione()+", ruolo="+ru.getCodiceRuolo();
    %>
    <h3>Nuovo utente = <%=utente%></h3>
    <br />
    <br /><%
  }
  else
  {
    idUtente="";
  }
%>
<h3>Utente: <input type="text" name="idUtente" value="<%=idUtente %>" /><input type="submit" name="invia" value="invia"/>
</h3><br />
<br />
<h3><a href="util.jsp">Ritorna a util.jsp</a><br/></h3>
<h3><a href="/layout/index.htm">ritorna all'Indice</a></h3>
<h3><a href="/layout/ricerca.htm">vai alla ricerca</a></h3>
</form>
</body>
</html>
