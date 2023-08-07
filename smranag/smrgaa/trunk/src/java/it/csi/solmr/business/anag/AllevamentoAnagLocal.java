package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.CategorieAllevamentoAnagVO;
import it.csi.solmr.dto.anag.TipoASLAnagVO;
import it.csi.solmr.dto.anag.TipoCategoriaAnimaleAnagVO;
import it.csi.solmr.dto.anag.TipoSpecieAnimaleAnagVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.Vector;


public interface AllevamentoAnagLocal{
	public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno) throws SolmrException,Exception;
	public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno) throws SolmrException,Exception;
	public AllevamentoAnagVO[] getAllevamentiByIdUTE(Long idUTE, Date dataAl) throws SolmrException,Exception;
	public AllevamentoAnagVO[] getAllevamentiByIdAzienda(Long idAzienda, Date dataAl) throws SolmrException,Exception;
	public AllevamentoAnagVO getAllevamento(Long idAllevamento) throws SolmrException,Exception;
  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException,Exception;
  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie,long idTipoProduzione) throws SolmrException,Exception;
  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) throws SolmrException,Exception;
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo, 
      long idTipoProduzioneCosman, String flagEsiste) throws SolmrException,Exception;
  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento) throws SolmrException,Exception;
	public Long insertAllevamento(AllevamentoAnagVO allevamentoVO,long idUtenteAggiornamento, boolean inserisciAllev) throws SolmrException,Exception;
	public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda) throws SolmrException,Exception;
	public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException,Exception;
	public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException,Exception;
	public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException,Exception;
	public void deleteAllevamentoAll(Long idAllevamento,boolean cancAllevamenti) throws SolmrException,Exception;
	public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException,Exception;
	public void updateAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) throws SolmrException,Exception;
	public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException,Exception;
	public void storicizzaAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) throws SolmrException,Exception;
	public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno) throws SolmrException,Exception;
	public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale) throws Exception;
	public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO, SianAllevamentiVO sianAllevamentiVO) throws Exception;
	public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(Long idAmmCompetenza, boolean isActive) throws Exception, SolmrException;
	public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws Exception;
	public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale) throws Exception;

}
