package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoCategoriaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoFormaPossessoGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoMacchinaGaaVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
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
public interface MacchineAgricoleGaaLocal 
{
  public long[] ricercaIdPossessoMacchina(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) 
          throws SolmrException;
  
  public long[] ricercaIdPossessoMacchinaImport(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException;
  
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws SolmrException;
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws SolmrException;
  
  public void popolaTabelleMacchineAgricoleConServizio(
      long idAzienda) throws SolmrException;
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() throws SolmrException;
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) throws SolmrException;
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina)
      throws SolmrException;
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws SolmrException;
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws SolmrException;
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flaIrroratrice) 
      throws SolmrException;
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) throws SolmrException;
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
      throws SolmrException;
  
  public Vector<CodeDescription> getElencoTipoMarca()
      throws SolmrException;
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
      throws SolmrException;
    
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso()
      throws SolmrException;
  
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) throws SolmrException;
  
  public void inserisciMacchinaAgricola(PossessoMacchinaVO possessoMacchinaVO) throws SolmrException;
  
  public void modificaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO, boolean flagModUte) 
      throws SolmrException;
  
  public void importaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO) throws SolmrException;
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo)
      throws SolmrException;
  
  public boolean isMacchinaPossMultiplo(long idMacchina)
      throws SolmrException;
  
  public Vector<CodeDescription> getElencoTipoScarico()
      throws SolmrException;
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda)
      throws SolmrException;
  
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina)
      throws SolmrException;
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
      throws SolmrException;
  
}
