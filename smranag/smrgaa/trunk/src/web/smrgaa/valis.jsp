<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<html>
<head>
<title>SOLMR GAA - VALIS</title>
<link rel="stylesheet" type="text/css" href="rupar/css/stile.css">
<link rel="stylesheet" type="text/css" href="rupar/css/stilebase.css">
<script language="JavaScript">
function expand(sessionId) { manage("EXPAND", sessionId); }

function evaluate(sessionId) { manage("EVALUATE", sessionId); }

function invalidate(sessionId) { manage("INVALIDATE", sessionId); }

function forceGC() { manage("FORCEGC", ""); }

function remove(sessionId, other) { manage("", sessionId, other); }

function manage(actionType, sessionId) { manage(actionType, sessionId, ""); }

function manage(actionType, sessionId, other) {
  var currentAction = document.valisForm.action.value;
  document.valisForm.action.value = actionType;
  var currentTarget = document.valisForm.target.value;
  document.valisForm.target.value = sessionId;
  var currentSubTarget = document.valisForm.subtarget.value;
  document.valisForm.subtarget.value = other;
  document.valisForm.submit();
  document.valisForm.action.value = currentAction;
  document.valisForm.subtarget.value = currentSubTarget;
}
</script>
</head>
<body>
<div align="center">
<br>
<table cellpadding="0" cellspacing="0" width="725">
<tr><td id="banner2" width="725"><img alt="image" src="rupar/im/banner.jpg" width="725" height="29"></td></tr>
<tr><td align=center><table><tr><td><h1>VALIS - Session watcher</h1></td></tr></table></td></tr>
<tr align=center><td align=center>
<form name="valisForm" action="valis.jsp" method="POST">
<input type=hidden name="action" value="">
<input type=hidden name="target" value="">
<input type=hidden name="subtarget" value="">
<%
  String theAction = request.getParameter("action");
  String lastAction = request.getParameter("lastAction");
  String theTarget = request.getParameter("target");
  String subTarget = request.getParameter("subtarget");
  boolean showClassNames = request.getParameter("showClassNames")!=null;
  boolean showValues = request.getParameter("showValues")!=null;
  HashMap hmSessions = SolmrSessionListener.getHmSessions();
  if (theAction!=null) {
    if (theAction.equalsIgnoreCase("FORCEGC")) Runtime.getRuntime().gc();
    else if (theAction.equalsIgnoreCase("INVALIDATE")) {
      HttpSession targetSession = (HttpSession)hmSessions.get(theTarget);
      if (targetSession!=null) targetSession.invalidate();
    } else if (theAction.length()==0&&lastAction!=null&&lastAction.length()!=0) {
      theAction=lastAction;
      HttpSession targetSession = (HttpSession)hmSessions.get(theTarget);
      if (targetSession!=null) targetSession.removeAttribute(subTarget);
    }
  }
  Set sessionIds = hmSessions.keySet();
  long maxMemory = Runtime.getRuntime().totalMemory()/1024;
  long freeMemory = Runtime.getRuntime().freeMemory()/1024;
  long maxMb = maxMemory/1024;
  long freeMb = freeMemory/1024;
  String thisSessionId = request.getSession().getId();
%>
<input type=hidden name="lastAction" value="<%= theAction %>">
<table class="data" width="725" bgcolor="#DFDFDF" text="#000000">
<tr><td colspan=2>
<table width=100%><tr>
<td align=center>
<table>
<tr><td align=right>Total Memory:</td>
    <td><%= maxMb %>Mb (<%= maxMemory %>Kb)</td></tr>
<tr><td align=right>Free Memory:</td>
    <td><%= freeMb %>Mb (<%= freeMemory %>Kb)</td></tr>
<tr><td colspan=2 align=center><a href="javascript:forceGC()">Request Garbage Collection</a></td></tr>
</table></td>
<% if (!sessionIds.isEmpty()) { %>
<td align=center><table>
<tr><td colspan=2><a href="javascript:expand('ALL')">Expand all JSessionIds</a></td></tr>
<tr><td colspan=2><a href="javascript:evaluate('ALL')">Evaluate all sessions' sizes</a></td></tr>
<tr><td>Show attributes' values </td>
    <td><input type=checkbox name="showValues"<%= showValues?" checked":""%>></td></tr>
<tr><td>Show attributes' class names </td>
    <td><input type=checkbox name="showClassNames"<%= showClassNames?" checked":""%>></td></tr>
</table></td>
<% } %>
</tr></table>
</td></tr>
<%
  if (!sessionIds.isEmpty()) {
    Iterator iter = sessionIds.iterator();
    boolean isExpand = theAction!=null && theAction.equalsIgnoreCase("EXPAND");
    boolean isEvaluate = theAction!=null && theAction.equalsIgnoreCase("EVALUATE");
    while (iter.hasNext()) {
      String currentId = (String)iter.next();
      HttpSession currentSession = (HttpSession)hmSessions.get(currentId);
      if (currentSession!=null) {
      Date created = new Date(currentSession.getCreationTime());
      Date lastAccessed = new Date(currentSession.getLastAccessedTime());
      boolean isThisOne = currentId.equalsIgnoreCase(thisSessionId);
%>
<tr><td align=right>JSessionID:</td>
<td><%= currentId %>&nbsp;
<%= isThisOne?"(This one)":"" %>&nbsp;
<a href="javascript:expand('<%= currentId %>')">Expand this</a>&nbsp;
<% if (!isThisOne) { %>
<a href="javascript:invalidate('<%= currentId %>')">Invalidate</a>&nbsp;
<% } %>
<a href="javascript:evaluate('<%= currentId %>')">Evaluate size</a>
</td></tr>
<%
  if ((isExpand || isEvaluate)&&
      theTarget!=null && (theTarget.equalsIgnoreCase("ALL")||theTarget.equals(currentId))) {
%>
<tr><td align=right>Created:</td>
<td><%= created %></td></tr>
<tr><td align=right>Last accessed:</td>
<td><%= lastAccessed %></td></tr>
<%
  Enumeration currentEnum = currentSession.getAttributeNames();
  if (currentEnum.hasMoreElements()) {
%>
<tr><td align=right>Attributes:</td>
<td>
<table cellpadding="5" cellspacing="2">
<th>Name</th>
<% if (showClassNames) { %> <th>Class</th> <% } %>
<% if (showValues) { %> <th>Value</th> <% } %>
<%
  if (isEvaluate) {
%>
<th>Size</th>
<%
  }
  long totalSize = 0;
  while (currentEnum.hasMoreElements()) {
    String currentName = (String)currentEnum.nextElement();
    Object currentObj = currentSession.getAttribute(currentName);
%>
  <tr><td><%= currentName %></td>
      <%= showClassNames?"<td>"+currentObj.getClass().getName()+"</td>":"" %>
      <%= showValues?"<td>"+currentObj.toString()+"</td>":"" %>
<%
  if (isEvaluate) {
    long currentSize = getBytes(currentObj);
    totalSize += currentSize;
    String strCurrentSize = currentSize/1024/1024+"Mb - "+currentSize/1024+"Kb - "+currentSize+"b";
%>
<td><%= strCurrentSize %></td>
<%
  }
%>
<td><a href="javascript:remove('<%= currentId %>', '<%= currentName %>')">Remove</a></td>
</tr>
<% } %>
</table>
<%
  if (isEvaluate) {
%>
<br>
Total Session Size: <%= totalSize/1024/1024+"Mb - "+totalSize/1024+"Kb - "+totalSize+"b" %>
<%
  }
%>
<%
    }
  }
%>
</td></tr>
<%
       if (iter.hasNext()) {
         %><tr><td colspan=2></td></tr><%
       }
     }
    }
   }
%>
</table>
</td></tr>
<tr><td id="piede" align=left><img alt="image" src="rupar/im/banner_inf.jpg" width="725" height="29"></td></tr>
</form>
</table>
</div>
</body>
</html>
<%!
private long getBytes(Object obj) {
  long result = 0;
  try{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(obj);
    oos.flush();
    result = baos.toByteArray().length;
    oos.close();
    } catch(Exception e) {
      System.out.println("Eccezione: "+e);
    }
    return result;
}
%>
