package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;

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
 * @author Diana Luca
 * @version 0.1
 */
public class AllevamentoVO implements Serializable
{
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3993331450841139816L;
  
  
  
  private long idAllevamento;
  private long idUte;
  private String uteDescom;
  private String uteSglProv;
  private String uteIndirizzo;
  private long idSpecieAnimale;
  private String codiceAziendaZootecnica;
  private String istatComune;
  private String allevamentoDescom;
  private String allevamentoSglProv;
  private String allevamentoIndirizzo;
  private String allevamentoCap;
  private String codFiscDetentore;
  private String denDetentore;
  private String codFiscProprietario;
  private String denProprietario;
  private String soccida;
  
  
  private TipoSpecieAnimale tipoSpecieAnimaleVO;
  private TipoCategoriaAnimale tipoCategoriaAnimaleVO;
  private SottoCategoriaAllevamento sottoCategoriaAllevamentoVO;
  private TipoSottoCategoriaAnimale tipoSottoCategoriaAnimaleVO;
  
  
  
  
  
  public long getIdAllevamento()
  {
    return idAllevamento;
  }
  public void setIdAllevamento(long idAllevamento)
  {
    this.idAllevamento = idAllevamento;
  }
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public String getUteDescom()
  {
    return uteDescom;
  }
  public void setUteDescom(String uteDescom)
  {
    this.uteDescom = uteDescom;
  }
  public String getUteSglProv()
  {
    return uteSglProv;
  }
  public void setUteSglProv(String uteSglProv)
  {
    this.uteSglProv = uteSglProv;
  }
  public String getUteIndirizzo()
  {
    return uteIndirizzo;
  }
  public void setUteIndirizzo(String uteIndirizzo)
  {
    this.uteIndirizzo = uteIndirizzo;
  }
  public long getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }
  public void setIdSpecieAnimale(long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
  }
  public String getCodiceAziendaZootecnica()
  {
    return codiceAziendaZootecnica;
  }
  public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
  {
    this.codiceAziendaZootecnica = codiceAziendaZootecnica;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getAllevamentoDescom()
  {
    return allevamentoDescom;
  }
  public void setAllevamentoDescom(String allevamentoDescom)
  {
    this.allevamentoDescom = allevamentoDescom;
  }
  public String getAllevamentoSglProv()
  {
    return allevamentoSglProv;
  }
  public void setAllevamentoSglProv(String allevamentoSglProv)
  {
    this.allevamentoSglProv = allevamentoSglProv;
  }
  public String getAllevamentoIndirizzo()
  {
    return allevamentoIndirizzo;
  }
  public void setAllevamentoIndirizzo(String allevamentoIndirizzo)
  {
    this.allevamentoIndirizzo = allevamentoIndirizzo;
  }
  public String getAllevamentoCap()
  {
    return allevamentoCap;
  }
  public void setAllevamentoCap(String allevamentoCap)
  {
    this.allevamentoCap = allevamentoCap;
  }
  public TipoSpecieAnimale getTipoSpecieAnimaleVO()
  {
    return tipoSpecieAnimaleVO;
  }
  public void setTipoSpecieAnimaleVO(TipoSpecieAnimale tipoSpecieAnimaleVO)
  {
    this.tipoSpecieAnimaleVO = tipoSpecieAnimaleVO;
  }
  public TipoCategoriaAnimale getTipoCategoriaAnimaleVO()
  {
    return tipoCategoriaAnimaleVO;
  }
  public void setTipoCategoriaAnimaleVO(
      TipoCategoriaAnimale tipoCategoriaAnimaleVO)
  {
    this.tipoCategoriaAnimaleVO = tipoCategoriaAnimaleVO;
  }
  public SottoCategoriaAllevamento getSottoCategoriaAllevamentoVO()
  {
    return sottoCategoriaAllevamentoVO;
  }
  public void setSottoCategoriaAllevamentoVO(
      SottoCategoriaAllevamento sottoCategoriaAllevamentoVO)
  {
    this.sottoCategoriaAllevamentoVO = sottoCategoriaAllevamentoVO;
  }
  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimaleVO()
  {
    return tipoSottoCategoriaAnimaleVO;
  }
  public void setTipoSottoCategoriaAnimaleVO(
      TipoSottoCategoriaAnimale tipoSottoCategoriaAnimaleVO)
  {
    this.tipoSottoCategoriaAnimaleVO = tipoSottoCategoriaAnimaleVO;
  }
  public String getCodFiscDetentore()
  {
    return codFiscDetentore;
  }
  public void setCodFiscDetentore(String codFiscDetentore)
  {
    this.codFiscDetentore = codFiscDetentore;
  }
  public String getDenDetentore()
  {
    return denDetentore;
  }
  public void setDenDetentore(String denDetentore)
  {
    this.denDetentore = denDetentore;
  }
  public String getCodFiscProprietario()
  {
    return codFiscProprietario;
  }
  public void setCodFiscProprietario(String codFiscProprietario)
  {
    this.codFiscProprietario = codFiscProprietario;
  }
  public String getDenProprietario()
  {
    return denProprietario;
  }
  public void setDenProprietario(String denProprietario)
  {
    this.denProprietario = denProprietario;
  }
  public String getSoccida()
  {
    return soccida;
  }
  public void setSoccida(String soccida)
  {
    this.soccida = soccida;
  }

}
