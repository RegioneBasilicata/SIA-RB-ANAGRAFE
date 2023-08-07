<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="it.csi.iride2.policy.entity.UseCase"%>
<%@page import="it.csi.iride2.policy.entity.Application"%>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.solmr.dto.iride2.Iride2AbilitazioniVO" %>
<%@page import="it.csi.solmr.util.IrideFileParser"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>My JSP 'ruoli.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script language="javascript1.2">
		  function seleziona(value)
		  {
		    var i=0;
		    var checkboxes=document.forms[0].CU_NAME;
		    if (!checkboxes.length)
		    {
		      checkboxes.checked=value;
		      return;
		    }
		    for(i=0;i<checkboxes.length;++i)
		    {
		      checkboxes[i].checked=value;
		    }
		  }
		</script>
	</head>
	<%
  Iride2AbilitazioniVO irideVO = null;
  try
  {
    irideVO = (Iride2AbilitazioniVO)session.getAttribute("iride2AbilitazioniVO");
  }
  catch(Exception e)
  {
    //da rivedere
    //irideVO = new Iride2AbilitazioniVO(new UseCase[0]);
  }
    
  if (request.getParameter("aggiorna")!=null)
  {
    String names[]=request.getParameterValues("CU_NAME");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    if (names!=null && names.length>0)
    {
      int len=names.length;
      UseCase[] useCases=new UseCase[len];
      Application appID=new Application(SolmrConstants.APP_NAME_IRIDE2_SMRGAA);
      for(int i=0;i<len;++i)
      {
        useCases[i]=new UseCase(appID,names[i]);
      }
      //da rivedere
      //irideVO = new Iride2AbilitazioniVO(useCases);
    }
    else
    {
      //da rivedere
      //irideVO = new Iride2AbilitazioniVO(new UseCase[0]);
    }
    session.setAttribute("iride2AbilitazioniVO",irideVO);
      %>
      <h1>Dati aggiornati correttamente</h1>
      <%
    
  } 
%>
	<body>
		<form name="casiUso" method="post">
		<input type="button" onclick="seleziona(true)" value="Seleziona tutto" />&nbsp;&nbsp;&nbsp;
    <input type="button" onclick="seleziona(false)" value="Deseleziona tutto" />
			<table>
<%
  IrideFileParser iridePars = new IrideFileParser();
  HashMap hMap = iridePars.getElencoSecurity();
  TreeMap tMap = new TreeMap();
  Iterator it = hMap.keySet().iterator();
  while(it.hasNext())
  {
    String nome = (String)it.next();
    tMap.put(nome,nome);  
  }
  
  Iterator itTree = tMap.keySet().iterator();
  while(itTree.hasNext())
  {
    String nome = (String)itTree.next();
%>			
        <tr>
          <td><%=nome%>
          </td>
          <td>
            <input type="checkbox" name="CU_NAME" <%= getChecked(irideVO,nome) %> value="<%=nome%>">
          </td>
        </tr>
<%} %>        
			</table>
			<input type="submit" value="aggiorna" name="aggiorna" />
<br />
<h3><a href="util.jsp">Ritorna a util.jsp</a><br/></h3>
<h3><a href="/layout/index.htm">ritorna all'Indice</a></h3>
<h3><a href="/layout/ricerca.htm">vai alla ricerca</a></h3>
		</form>
	</body>
</html>
<%!
  private String getChecked(Iride2AbilitazioniVO irideVO, String name)
  {
    if ((irideVO !=null) && irideVO.isUtenteAbilitato(name))
    {
      return "checked";
    }
    else
    {
      return "";
    }
  }
%>