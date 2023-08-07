<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException"%>
<%@ page import="org.apache.commons.fileupload.FileUploadBase"%>
<%@ page import="org.apache.commons.fileupload.FileUploadException"%>
<%@ page import="org.apache.commons.io.FilenameUtils"%>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	String iridePageName = "strumentiFirmaMarcaTemporaleCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	SolmrLogger.info(this, " - strumentiFirmaMarcaTemporaleCtrl.jsp - INIZIO PAGINA");
 	String strumentiFirmaMarcaTemporaleUrl = "/view/strumentiFirmaMarcaTemporaleView.jsp";
 	
 	final String errMsg = "Impossibile procedere nella sezione documenti allegati."+
 		    "Contattare l'assistenza comunicando il seguente messaggio: ";
 	String erroreViewUrl = "/view/erroreView.jsp";
 	String actionUrl = "../layout/strumentiFirmaMarca.htm";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String messaggioErrore = null;
 	Long fileSize = null;
 	String operazione = null;
 		 	
 	ValidationErrors errors = new ValidationErrors(); 	

 	BigDecimal parametroMaxDimFileUp = null;
 	try 
 	{
 		parametroMaxDimFileUp = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAX_DIM_FILE_UP);
 	}
 	catch(SolmrException se) 
 	{
 		SolmrLogger.info(this, " - documentoAllegatiCtrl.jsp - FINE PAGINA");
 		String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAX_DIM_FILE_UP+".\n"+se.toString();
 		request.setAttribute("messaggioErrore",messaggio);
 		request.setAttribute("pageBack", actionUrl);
 		%>
 			<jsp:forward page="<%= erroreViewUrl %>" />
 		<%
 		return;
 	}
 		    
 	fileSize = new Long(parametroMaxDimFileUp.longValue());
 		    
 	HashMap hmRequest = null;
 	try
 	{
 		hmRequest = multipartRequestToHashMap(request,fileSize.longValue()); 
     }
 	 catch(FileUploadException ex)
 	{
 		errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_DIMENSIONE_MAX_FILE + " : " + fileSize.toString()));
 	}
 	
 	
 	if (Validator.isEmpty(operazione))
 	{
 		operazione = (String) getFromRequest(hmRequest, request,"operazione");
 	}	    
  		    
		  
		  if("carica".equals(operazione)){
		  
		 	String nomeFile = "";
		 	if((String)getFromRequest(hmRequest, request,"nomeFisico") != null) {
		 		nomeFile = (String)getFromRequest(hmRequest, request,"nomeFisico");
		 		session.setAttribute("fileName", nomeFile);
		 	}
		 	
		 	if((String)getFromRequest(hmRequest, request,"nomeFisico2") != null) {
		 		nomeFile = (String)getFromRequest(hmRequest, request,"nomeFisico2");
		 		session.setAttribute("fileName2", nomeFile);
		 	}

		 	if(!nomeFile.equalsIgnoreCase("")) {
		 		Object file =  ((byte[])getFromRequest(hmRequest, request,"fileAllegato"));
		 		if (file == null) {
		 			file =  ((byte[])getFromRequest(hmRequest, request,"fileAllegato2"));
		 		}		 		
		   		request.getSession().setAttribute("byteFile", file);
		 	}
		 	
	}
 	
 	%>
  	<jsp:forward page="<%= strumentiFirmaMarcaTemporaleUrl %>" />
  	
		<%!
  private Object getFromRequest(HashMap hmRequest, HttpServletRequest request,String param)
  {
    if (hmRequest != null) 
    {
      return hmRequest.get(param);
    }
    else
    {
      return request.getParameter(param);
    }   
  }
  
  //converte lo stream in un hashmap
  private HashMap multipartRequestToHashMap(HttpServletRequest request, long maxFileSize) throws FileUploadException
  {
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    HashMap hmRequest = null;
    
    if(isMultipart)
    { 
      hmRequest = new HashMap();
      
      // Create a factory for disk-based file items
      DiskFileItemFactory factory = new DiskFileItemFactory();

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setFileSizeMax(maxFileSize * 1024 * 1024);

      // Parse the request
      List items = upload.parseRequest(request);
      
      // Process the uploaded items
      Iterator iter = items.iterator();
      while (iter.hasNext()) 
      {
        FileItem item = (FileItem) iter.next();
        if (item.isFormField())
        {
          hmRequest.put(item.getFieldName(), item.getString());
        }
        else
        {
          String fieldName = item.getFieldName();
          String fileName = item.getName();
          
          if(fieldName.equalsIgnoreCase("fileAllegato")){
        
	          String contentType = item.getContentType();
	          boolean isInMemory = item.isInMemory();
	          long sizeInBytes = item.getSize();
	          byte[] data = item.get();
	
	          hmRequest.put("nomeFisico", FilenameUtils.getName(fileName));
	          hmRequest.put(fileName, data);
	          request.getSession().setAttribute("fileAllegatoA", data);
	          request.getSession().setAttribute("fileName", FilenameUtils.getName(fileName));
	      }
          
          if(fieldName.equalsIgnoreCase("fileAllegato2")){
              
	          String contentType = item.getContentType();
	          boolean isInMemory = item.isInMemory();
	          long sizeInBytes = item.getSize();
	          byte[] data = item.get();
	
	          hmRequest.put("nomeFisico2", FilenameUtils.getName(fileName));
	          hmRequest.put(fileName, data);
	          request.getSession().setAttribute("fileAllegatoB", data);
	          request.getSession().setAttribute("fileName2", FilenameUtils.getName(fileName));
	      }          
        }
      }
    }
    return hmRequest;
  }
%>
 
