package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocTerreni;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

public class DocumentoTerreni extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_TERRENI";

  public DocumentoTerreni() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
       HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO"); 
    
    //Controllo se sono nel nuovo o nel vecchio stile
    boolean flagOldStyle = false;
    Date dataConfronto = null;
    try
    {
      String parametroFAPP = anagFacadeClient
        .getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_FAPP);
      dataConfronto = DateUtils.parseDate(parametroFAPP);
      if(documentoVO.getDataInizioValidita() != null)
      {
        if(documentoVO.getDataInizioValidita().before(dataConfronto))
        {
          flagOldStyle = true;
        }
      }
    }
    catch(Exception ex)
    {}
    
    
    
    
    int size=0;
    Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
      DocTerreni terreni = new DocTerreni();	
      terreni.setElencoParticelle((Vector<StoricoParticellaVO>) elencoParticelle);
      protocollo.setDocTerreni(terreni);   
    }
  }
}