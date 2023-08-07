<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%
  session.removeAttribute("profile");
  String userid = request.getParameter("USERID");
  String uid = request.getParameter("UID");
  String suid = request.getParameter("SUID");
  String tuid = request.getParameter("TUID");
  String fuid = request.getParameter("FUID");
  String luid = request.getParameter("LUID");
  luid = luid==null?"":luid;

  String ssid = DateUtils.getCurrent();

  AlgorithmDecoder ad = new AlgorithmDecoder("MD5", SolmrConstants.IRIDE_PASS_PHRASE);
  String md5 = ad.getHash(ssid+userid+uid+suid+tuid+fuid+luid);

  request.setAttribute("ssid", ssid);
  request.setAttribute("md5", md5);
  SolmrLogger.info(this, "Added ssid ["+ssid+"] and md5 ["+md5+"].");
%>

<jsp:forward page="servlet/IrideProfileSetter"/>
