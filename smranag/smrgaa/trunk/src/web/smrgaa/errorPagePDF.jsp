  <%@ page language="java"

      contentType="text/html"

      isErrorPage="true"

  %>

<%

String msg = ((java.lang.Exception)session.getAttribute("exception")).getMessage();

session.removeAttribute("exception");

%>

<html>
<head>
<script>
window.close();
window.opener.alert("<%=msg%>");
</script>
</head>
<body></body>
</html>
