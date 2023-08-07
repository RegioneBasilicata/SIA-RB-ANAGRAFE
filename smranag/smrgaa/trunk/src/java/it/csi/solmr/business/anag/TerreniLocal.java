package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ParticelleVO;
import it.csi.solmr.dto.anag.TerreniVO;
import it.csi.solmr.dto.anag.terreni.TipoSettoreAbacoVO;
import it.csi.solmr.exception.SolmrException;

import java.util.Vector;
import javax.ejb.Local;

@Local
public interface TerreniLocal
{
  public Vector<Long> getAnniRilevamento() throws Exception,
      SolmrException;

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws Exception, SolmrException;

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws Exception, SolmrException;

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws Exception,
      SolmrException;

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws Exception, SolmrException;
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
      throws Exception;

}