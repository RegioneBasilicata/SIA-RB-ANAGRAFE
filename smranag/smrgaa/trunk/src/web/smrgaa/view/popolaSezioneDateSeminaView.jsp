<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaSezioneDateSemina.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
  String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
  String idVarieta = request.getParameter("idVarieta");
  String idTipoPeriodoSemina = request.getParameter("idTipoPeriodoSemina");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione) 
      && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso)
      && Validator.isNotEmpty(idVarieta))
  {  
	  try
	  {
		  CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(new Long(idUtilizzo),new Long(idVarieta), 
        new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso));
      if(catalogoMatriceVO != null
        && Validator.isNotEmpty(idTipoPeriodoSemina))      
      {
        
        CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = gaaFacadeClient
          .getCatalogoMatriceSeminaByIdTipoPeriodo(catalogoMatriceVO.getIdCatalogoMatrice(), new Long(idTipoPeriodoSemina));
        
        if(catalogoMatriceSeminaVO != null)
        {
          String parametroDataSwapSemina = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_SWAP_SEMINA);
          SimpleDateFormat sdf = new SimpleDateFormat();
          sdf.applyPattern("dd/MM/yyyy");
          Date dataConfronto = sdf.parse(parametroDataSwapSemina+"/"+DateUtils.getCurrentYear());
        
          
          Date dataInizioDestinazione = null;
          Date dataFineDestinazione = null;
          if(dataConfronto.after(new Date()))
          {
            int anno = DateUtils.getCurrentYear().intValue();
            anno = anno + catalogoMatriceSeminaVO.getAnnoDecodificaPreData();
            dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVO.getInizioDestinazioneDefault()+"/"+anno);
            dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVO.getFineDestinazioneDefault()+"/"+anno);
            
          }
          else
          {
            int anno = DateUtils.getCurrentYear().intValue();
            anno = anno + catalogoMatriceSeminaVO.getAnnoDecodificaPostData();
            dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVO.getInizioDestinazioneDefault()+"/"+anno);
            dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVO.getFineDestinazioneDefault()+"/"+anno);
          }
          
          if(Validator.isNotEmpty(dataInizioDestinazione) && Validator.isNotEmpty(dataFineDestinazione))
          {
            if(dataInizioDestinazione.after(dataFineDestinazione))
            {
              GregorianCalendar gc = new GregorianCalendar();
              gc.setTime(dataFineDestinazione);
              gc.roll(Calendar.YEAR, true);
              dataFineDestinazione = gc.getTime();
            }
          }
          
          
          htmpl.set("dataInizioDestinazione", DateUtils.formatDateNotNull(dataInizioDestinazione));
          htmpl.set("dataFineDestinazione", DateUtils.formatDateNotNull(dataFineDestinazione));
        }
      
      }         	  
		}
		catch(Exception ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.set("readOnlyDataInizioDestinazione", "readOnly=\"true\"", null);
    htmpl.set("readOnlyDataFineDestinazione", "readOnly=\"true\"", null);
  }
  
	
 	

%>
<%= htmpl.text()%>
