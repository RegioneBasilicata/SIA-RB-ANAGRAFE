package it.csi.smranag.smrgaa.business;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoAttivitaOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;
import javax.ejb.Local;




/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface AnagrafeGaaLocal
{
  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda) throws SolmrException;
  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, Long idUde) throws SolmrException;
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(long idAzienda) throws SolmrException;
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException;
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException;
  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda() throws SolmrException;
  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte) throws SolmrException;
  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO) throws SolmrException;
  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws SolmrException;
  
  public int ricercaNumVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) 
    throws SolmrException;
  
  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
    throws SolmrException;
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza,boolean excel) 
    throws SolmrException;
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(Vector<Long> elencoIdPresaVisione,FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO) 
    throws SolmrException;

  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza)
    throws SolmrException;
  
  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda) throws SolmrException;
  
  public HashMap<Long,DelegaVO> getDelegaAndIntermediario(long ids[]) throws SolmrException;
  
  public boolean isAziendeCollegataFiglia(
      long idAzienda, long idAziendaSearch) throws SolmrException;
  
  public boolean isAziendeCollegataMenu(
      long idAzienda) throws SolmrException;
  
  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(
      long idAzienda) throws SolmrException;  
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws SolmrException;
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws SolmrException;
  
  public boolean isInAziendaProvenienza(long idAzienda) 
      throws SolmrException;
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda)
      throws SolmrException;
  
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, Long idAzienda) 
  		throws SolmrException;
  
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws SolmrException;
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws SolmrException;
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda)
      throws SolmrException;
}
