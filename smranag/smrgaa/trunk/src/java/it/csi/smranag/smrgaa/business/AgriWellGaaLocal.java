package it.csi.smranag.smrgaa.business;

import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoDaAggiornareVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.solmr.exception.SolmrException;

import javax.ejb.Local;

@Local
public interface AgriWellGaaLocal
{
	
  public AgriWellEsitoVO agriwellServiceScriviDoquiAgri(
      AgriWellDocumentoVO agriWellDocumentoVO)  throws SolmrException;
  
  public AgriWellEsitoVO agriwellServiceLeggiDoquiAgri(
      long idDocumentoIndex)  throws SolmrException;
  
  public AgriWellEsitoVO agriwellServiceCancellaDoquiAgri(
      long idDocumentoIndex)  throws SolmrException;
  
  public AgriWellEsitoFolderVO agriwellFindFolderByPadreProcedimentoRuolo(int idProcedimento,
      String codRuoloUtente, Long idFolderMadre, boolean noEmptyFolder, Long idAzienda)  throws SolmrException;
  
  public AgriWellEsitoIdDocVO agriwellFindListaDocumentiByIdFolder(long idFolder, int idProcedimento,
      String codRuoloUtente, Long idAzienda)  throws SolmrException;
  
  public AgriWellEsitoDocumentoVO agriwellFindDocumentoByIdRange(long[] idDoc)  
      throws SolmrException;
  
  public AgriWellEsitoVO agriwellServiceUpdateDoquiAgri(
      AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO) throws SolmrException;
	
}
