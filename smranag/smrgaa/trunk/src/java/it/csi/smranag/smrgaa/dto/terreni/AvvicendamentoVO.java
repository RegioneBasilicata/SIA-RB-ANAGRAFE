package it.csi.smranag.smrgaa.dto.terreni;

import it.csi.solmr.dto.CodeDescription;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * Classe coi i campi per la visualizzazione 
 * delle uv per l'excel avvicendamento
 * 
 * @author TOBECONFIG
 *
 */
public class AvvicendamentoVO implements Serializable 
{
	
  /**
   * 
   */
  private static final long serialVersionUID = 2487261337927747694L;
  
  
  private Long idParticella;
  private Long idAzienda;
  private Integer anno;
  private String esito;
  private String messaggio;
  private Vector<CodeDescription> vTipoUtilizzo;
  private Vector<CodeDescription> vTipoVarieta;
  private Vector<BigDecimal> vSuperficieUtilizzata;
  private Vector<String> vConduzioneInAzienda;
  private Vector<String> vPresenzaDuPsr;
  
  
  
  public Long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public Integer getAnno()
  {
    return anno;
  }
  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }
  public String getEsito()
  {
    return esito;
  }
  public void setEsito(String esito)
  {
    this.esito = esito;
  }
  public String getMessaggio()
  {
    return messaggio;
  }
  public void setMessaggio(String messaggio)
  {
    this.messaggio = messaggio;
  }
  public Vector<CodeDescription> getvTipoUtilizzo()
  {
    return vTipoUtilizzo;
  }
  public void setvTipoUtilizzo(Vector<CodeDescription> vTipoUtilizzo)
  {
    this.vTipoUtilizzo = vTipoUtilizzo;
  }
  public Vector<CodeDescription> getvTipoVarieta()
  {
    return vTipoVarieta;
  }
  public void setvTipoVarieta(Vector<CodeDescription> vTipoVarieta)
  {
    this.vTipoVarieta = vTipoVarieta;
  }
  public Vector<BigDecimal> getvSuperficieUtilizzata()
  {
    return vSuperficieUtilizzata;
  }
  public void setvSuperficieUtlizzata(Vector<BigDecimal> vSuperficieUtilizzata)
  {
    this.vSuperficieUtilizzata = vSuperficieUtilizzata;
  }
  public Vector<String> getvConduzioneInAzienda()
  {
    return vConduzioneInAzienda;
  }
  public void setvConduzioneInAzienda(Vector<String> vConduzioneInAzienda)
  {
    this.vConduzioneInAzienda = vConduzioneInAzienda;
  }
  public Vector<String> getvPresenzaDuPsr()
  {
    return vPresenzaDuPsr;
  }
  public void setvPresenzaDuPsr(Vector<String> vPresenzaDuPsr)
  {
    this.vPresenzaDuPsr = vPresenzaDuPsr;
  }
  
  
  
  
  
  
  
	
}
