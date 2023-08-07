package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocCorrettivaTerreni;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.exception.SolmrException;

public class DocumentoCorrettivaTerreni extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_CORRETTIVA_TERRENI";

  public DocumentoCorrettivaTerreni() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");   
    
    int size=0;
    Vector<ParticellaAssVO> elencoParticelle = anagFacadeClient
      .getParticelleDocCor(documentoVO.getIdDocumento());
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
    	DocCorrettivaTerreni correttivaTerreni = new DocCorrettivaTerreni();
    	correttivaTerreni.setElencoParticelle(elencoParticelle);
    	protocollo.setDocCorrettivaTerreni(correttivaTerreni);
    }
      
 }
    
}