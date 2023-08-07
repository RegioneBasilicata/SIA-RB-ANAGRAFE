package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.IterRichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.StatoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UnitaMisuraVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UteAziendaNuovaVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface NuovaIscrizioneGaaLocal 
{
  public AziendaNuovaVO getAziendaNuovaIscrizione(String cuaa, long[] arrTipoRichiesta) throws SolmrException;
  public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(String codEnte, long[] arrTipoRichiesta)      
      throws SolmrException;
  public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(Long idAziendaNuova)     
      throws SolmrException;
  public Long insertAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException;
  public Long insertAziendaNuovaRichiestaValCess(RichiestaAziendaVO richiestaAziendaVO)     
      throws SolmrException;
  public Long insertAziendaNuovaRichiestaVariazione(RichiestaAziendaVO richiestaAziendaVO)
      throws SolmrException;
  public void updateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException;
  public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova)  throws SolmrException;
  public void aggiornaUteAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, Vector<UteAziendaNuovaVO> vUteAziendaNuova)     
      throws SolmrException;
  public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(long idAziendaNuova)     
      throws SolmrException;
  public void aggiornaFabbrAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova)  throws SolmrException;
  public boolean existsDependenciesUte(long idUteAziendaNuova)      
      throws SolmrException;
  public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public Vector<UnitaMisuraVO> getListUnitaMisura()  throws SolmrException;  
  public void aggiornaParticelleAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova) throws SolmrException;
  public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public void aggiornaAllevamentiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova) throws SolmrException;
  public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public void aggiornaCCAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<CCAziendaNuovaVO> vCCAziendaNuova) throws SolmrException;
  public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws SolmrException;
  public Long insertRichAzDocAziendaNuova(RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO)
      throws SolmrException;
  public void deleteDocumentoRichiesta(long idRichiestaDocumento) throws SolmrException;
  public void insertFileStampa(long idRichiestaAzienda,  byte ba[])
      throws SolmrException;
  public void aggiornaStatoNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException;
  public void aggiornaStatoRichiestaValCess(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException;
  public Vector<Long> getElencoIdRichiestaAzienda(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione,
      Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException;
  public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws SolmrException;
  public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
      throws SolmrException;
  public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws SolmrException;
  public Vector<CodeDescription> getListTipoRichiesta() throws SolmrException;
  public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws SolmrException;
  public Vector<StatoRichiestaVO> getListStatoRichiesta() throws SolmrException;
  public RichiestaAziendaVO getPdfAziendaNuova(
      long idRichiestaAzienda) throws SolmrException;
  public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda) throws SolmrException;
  public boolean isPartitaIvaPresente(String partitaIva, long[] arrTipoRichiesta) throws SolmrException;
  public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda, 
      String flagDichiarazioneAllegati) throws SolmrException;
  public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta)
      throws SolmrException;  
  public void updateRichiestaAziendaIndex(long idRichiestaAzienda, long idDocumentoIndex)
      throws SolmrException;
  public AziendaNuovaVO getRichAzByIdAzienda(long idAzienda, long idTipoRichiesta)
      throws SolmrException;
  public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws SolmrException;
  public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
      throws SolmrException;
  public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public void aggiornaSoggettiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova)     
        throws SolmrException;
  public void aggiornaMacchineIrrAziendaNuova(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<MacchinaAziendaNuovaVO> vMacchineNuovaRichiesta)     
        throws SolmrException;
  public void aggiornaAzAssociateCaaAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException;
  public void aggiornaAzAssociateCaaRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException;
  public void aggiornaAzAssociateRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)
          throws SolmrException;
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException;
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException;
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException;
  public void aggiornaAzAssociateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException;
  public void caricaMacchineNuovaRichiesta(long idAzienda, long idRichiestaAzienda) 
      throws SolmrException;
  public void caricaAziendeAssociateRichiesta(long idAzienda, long idRichiestaAzienda, String flagSoloAggiunta)     
      throws SolmrException;
  public void caricaAziendeAssociateCaaRichiesta(long idAzienda, long idRichiestaAzienda)     
      throws SolmrException;
  public void ribaltaMacchineNuovaRichiesta(long idRichiestaAzienda, long idUtenteAggiornamento)     
      throws SolmrException;
  public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda)
      throws SolmrException;
  public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
      throws SolmrException;
  
  
}
