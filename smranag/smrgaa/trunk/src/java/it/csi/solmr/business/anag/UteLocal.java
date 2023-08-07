package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.UteVO;

import java.util.Date;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface UteLocal
{
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;

  public Vector<UteVO> getListUteByIdAziendaAndIdPianoRiferimento(
      Long idAzienda, long idPianoRiferimento) throws Exception;

  public UteVO findUteByPrimaryKey(Long idUte) throws Exception;

  public Date getMinDataInizioConduzione(Long idUte) throws Exception;

  public Date getMinDataInizioAllevamento(Long idUte) throws Exception;

  public Date getMinDataInizioFabbricati(Long idUte) throws Exception;
}
