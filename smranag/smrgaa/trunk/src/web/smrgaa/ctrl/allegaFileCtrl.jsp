<%@ page language="java"
  contentType="text/html"
  isErrorPage="false"
%>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException"%>
<%@ page import="org.apache.commons.fileupload.FileUploadBase"%>
<%@ page import="org.apache.commons.fileupload.FileUploadException"%>
<%@ page import="org.apache.commons.io.FilenameUtils"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>


<%

  String iridePageName = "allegaFileCtrl.jsp";
  %>
    <%@include file="/include/autorizzazione.inc"%>
  <%
  
  SolmrLogger.info(this, " - allegaFileCtrl.jsp - INIZIO PAGINA");
  
  final String errMsg = "Impossibile procedere nella sezione documenti/allega file."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  String erroreViewUrl = "/view/erroreView.jsp";
  String VIEW = "../view/allegaFileView.jsp";
  String REFRESH = "../layout/allegaFile.htm?operazione=refresh";
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  ValidationErrors errors = new ValidationErrors();
  Vector<AllegatoDocumentoVO> vElencoFileAllegati = null;
  AllegatoDocumentoVO allegatoDocumentoVO = null;
  Long fileSize = null;
  String operazione = null;
  //Long idDocumento = null;
  try
  {
  
    BigDecimal parametroMaxDimFileUp = null;
    try 
    {
      parametroMaxDimFileUp = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAX_DIM_FILE_UP);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - allegaFileCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAX_DIM_FILE_UP+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  
    
    fileSize =new Long(parametroMaxDimFileUp.longValue());
    
    HashMap hmRequest = null;
    try
    {
      hmRequest = multipartRequestToHashMap(request,fileSize.longValue()); 
    }
    catch(FileUploadException ex)
    {
      errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_DIMENSIONE_MAX_FILE + " : " + fileSize.toString()));
      //in caso di FileUploadException la HmRequest viene popolata solo parzialmente.
      operazione = "conferma";
    }
    
    if (Validator.isEmpty(operazione))
    {
      operazione = (String) getFromRequest(hmRequest, request,"operazione");
    }
    
    if (Validator.isEmpty(operazione))
    {      
      WebUtils.removeUselessFilter(session, "documentoRicercaVO"); //rimuovere dalla sessione vElencoFileAllegati e fileAllegatoVO
      
      //Arrivo dalla pagina visualizzaFIleAllegatoCtrl con un errore
      Long idDocumento = null;
      if(session.getAttribute("idDocumentoErr") != null)
      {
        idDocumento = (Long)session.getAttribute("idDocumentoErr");
        session.removeAttribute("idDocumentoErr");
      }
      else
      {
        idDocumento = new Long ((String) getFromRequest(hmRequest, request,"idDocumento"));
      }
      
       
      vElencoFileAllegati = gaaFacadeClient.getElencoFileAllegati(idDocumento);      
      allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdDocumento(idDocumento);
    }
    else
    {
      vElencoFileAllegati = (Vector<AllegatoDocumentoVO>) session.getAttribute("vElencoFileAllegati");
      
      allegatoDocumentoVO = (AllegatoDocumentoVO) session.getAttribute("allegatoDocumentoVO");
    }
    
    if ("elimina".equals(operazione))
    { 
      String idAllegato = (String) getFromRequest(hmRequest,request,"idAllegato");
      
      AllegatoDocumentoVO allegatoDocumentoTmpVO = gaaFacadeClient.getFileAllegato(new Long(idAllegato));
      allegatoDocumentoTmpVO.setIdDocumento(allegatoDocumentoVO.getIdDocumento());
	    if(allegatoDocumentoTmpVO.getFileAllegato() == null)
	    {
	      gaaFacadeClient.deleteFileAllegatoAgriWell(allegatoDocumentoTmpVO);
	    }
	    else
	    {
	      gaaFacadeClient.deleteFileAllegato(new Long(idAllegato),
	        allegatoDocumentoVO.getIdDocumento().longValue());
	    }
      
      
      
      
      
      //eseguo refresh 
      operazione =  "refresh";
    }
    
    if ("refresh".equals(operazione))
    { 
      //ricarico elenco e svuoto fileVO
      vElencoFileAllegati = gaaFacadeClient.getElencoFileAllegati(allegatoDocumentoVO.getIdDocumento());
      Long idDocumento = allegatoDocumentoVO.getIdDocumento();
      allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdDocumento(idDocumento);
      
      //imposto per fare in modo che sulla chiusura ricarichi la form padre
      request.setAttribute("reloadParent","SI");
      
    }
    else if ("conferma".equals(operazione))
    {
        allegatoDocumentoVO.setNomeLogico((String)getFromRequest(hmRequest, request,"nomeLogico"));        
        allegatoDocumentoVO.setNomeFisico((String)getFromRequest(hmRequest, request,"nomeFisico"));
        allegatoDocumentoVO.setFileAllegato((byte[])getFromRequest(hmRequest, request,"fileAllegato"));
        Date DateTmp = new Date();
        allegatoDocumentoVO.setDataRagistrazione(DateTmp);
        allegatoDocumentoVO.setDataUltimoAggiornamento(DateTmp);
        allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());

        if (errors.size() == 0)
        {
          errors = allegatoDocumentoVO.validate(fileSize.longValue());
        }
        
        if (errors.size() == 0 ) 
        {
          //gaaFacadeClient.insertFileAllegato(allegatoDocumentoVO);          
          
          gaaFacadeClient.insertFileAllegatoAgriWell(allegatoDocumentoVO);
          
          //ricarico la pagina in refresh
          response.sendRedirect(REFRESH);
          return;
        }

    }

    session.setAttribute("vElencoFileAllegati", vElencoFileAllegati);
    session.setAttribute("allegatoDocumentoVO", allegatoDocumentoVO);
    
    
    //in request per view
    request.setAttribute("errors",errors);
    request.setAttribute("vElencoFileAllegati",vElencoFileAllegati);
    request.setAttribute("allegatoDocumentoVO", allegatoDocumentoVO);
   
  
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - allegaFileCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore",messaggio);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  %><jsp:forward page="<%= VIEW %>"/><%

  SolmrLogger.info(this, " - allegaFileCtrl.jsp - FINE PAGINA");
%>
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
        
          String contentType = item.getContentType();
          boolean isInMemory = item.isInMemory();
          long sizeInBytes = item.getSize();
          byte[] data = item.get();

          hmRequest.put("nomeFisico", FilenameUtils.getName(fileName));
          hmRequest.put("fileAllegato", data);
        
          SolmrLogger.debug(this, "fieldName " + fieldName);
          SolmrLogger.debug(this, "FilenameUtils.getName(fileName) " + FilenameUtils.getName(fileName));
          SolmrLogger.debug(this, "contentType " + contentType);
          SolmrLogger.debug(this, "isInMemory " + isInMemory);
          SolmrLogger.debug(this, "sizeInBytes " + sizeInBytes);
        }
      }
    }
    return hmRequest;
  }
%>