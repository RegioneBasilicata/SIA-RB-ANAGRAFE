package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Documenti;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Documento;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroDocumenti;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentiElenco extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTI_ELENCO";

  public DocumentiElenco() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroDocumenti quadroDocumenti = new QuadroDocumenti();
    fascicoDigitale.setQuadroDocumenti(quadroDocumenti);
    quadroDocumenti.setVisibility(true);
    
    quadroDocumenti.setTitoloDocumenti(richiestaTipoReportVO.getQuadro());
    
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    
    
    Vector<DocumentoVO> vDocumenti = gaaFacadeClient.getDocumentiStampaMd(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza,
        anagAziendaVO.getCUAA(), dataInserimentoDichiarazione);

    if (vDocumenti != null)
    {
      quadroDocumenti.setDocumenti(new Documenti());
      
      for (int i=0;i<vDocumenti.size();i++)
      {
        DocumentoVO documentoVO = vDocumenti.get(i);
        Documento documentoMd = new Documento();
        
        
        TipoDocumentoVO tipoDocumentoVO = documentoVO.getTipoDocumentoVO();

        if(tipoDocumentoVO != null)
        {
          documentoMd.setDescrizione(tipoDocumentoVO.getDescrizione());
          documentoMd.setCodice(tipoDocumentoVO.getCodiceDocumento());
        }
        
        if(documentoVO.getNumeroProtocollo() != null)
        {
          documentoMd.setNrProt(StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
          if(documentoVO.getDataProtocollo()!=null){
        	  documentoMd.setDataProt(DateUtils.formatDate(documentoVO.getDataProtocollo()));
          }
        }
        
        quadroDocumenti.getDocumenti().getDocumento().add(documentoMd);
      }
    }
    else
    {
      quadroDocumenti.setSezioneVuota(true);
    }   
    
  }
}