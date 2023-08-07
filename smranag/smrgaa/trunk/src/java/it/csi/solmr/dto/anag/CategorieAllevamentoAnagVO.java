package it.csi.solmr.dto.anag;

import it.csi.solmr.util.*;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Castagno Raffaele
 * @version 0.1
 */

public class CategorieAllevamentoAnagVO  implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = 304195125699757508L;

	private String idCategorieAllevamento = null;
	private String idCategoriaAnimale = null;
	private String idAllevamento = null;
	private String quantita = null;
	private String quantitaProprieta = null;
	private String pesoVivoUnitario = null;
	TipoCategoriaAnimaleAnagVO tipoCategoriaAnimaleAnagVO = null;

	public CategorieAllevamentoAnagVO()
	{
		tipoCategoriaAnimaleAnagVO = new TipoCategoriaAnimaleAnagVO();
	}


	public String getIdCategorieAllevamento()
	{
		return idCategorieAllevamento;
	}
	public void setIdCategorieAllevamento(String idCategorieAllevamento)
	{
		this.idCategorieAllevamento = idCategorieAllevamento;
	}
	public Long getIdCategorieAllevamentoLong()
	{
		try
		{
			return new Long(idCategorieAllevamento);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setIdCategorieAllevamentoLong(Long idCategorieAllevamento)
	{
		this.idCategorieAllevamento = idCategorieAllevamento==null?null:idCategorieAllevamento.toString();
	}

	public String getIdCategoriaAnimale()
	{
		return idCategoriaAnimale;
	}
	public void setIdCategoriaAnimale(String idCategoriaAnimale)
	{
		this.idCategoriaAnimale = idCategoriaAnimale;
	}
	public Long getIdCategoriaAnimaleLong()
	{
		try
		{
			return new Long(idCategoriaAnimale);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setIdCategoriaAnimaleLong(Long idCategoriaAnimale)
	{
		this.idCategoriaAnimale = idCategoriaAnimale==null?null:idCategoriaAnimale.toString();
	}

	public String getIdAllevamento()
	{
		return idAllevamento;
	}
	public void setIdAllevamento(String idAllevamento)
	{
		this.idAllevamento = idAllevamento;
	}
	public Long getIdAllevamentoLong()
	{
		try
		{
			return new Long(idAllevamento);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setIdAllevamentoLong(Long idAllevamento)
	{
		this.idAllevamento = idAllevamento==null?null:idAllevamento.toString();
	}

	public String getQuantita()
	{
		return quantita;
	}
	public void setQuantita(String quantita)
	{
		this.quantita = quantita;
	}
	public Long getQuantitaLong()
	{
		try
		{
			return new Long(quantita);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setQuantitaLong(Long quantita)
	{
		this.quantita = quantita==null?null:quantita.toString();
	}

	public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimaleAnagVO()
	{
		return tipoCategoriaAnimaleAnagVO;
	}
	public void setTipoCategoriaAnimaleAnagVO(TipoCategoriaAnimaleAnagVO tipoCategoriaAnimaleAnagVO)
	{
		this.tipoCategoriaAnimaleAnagVO = tipoCategoriaAnimaleAnagVO;
	}

	/**
	 * @return the pesoVivoUnitario
	 */
	public String getPesoVivoUnitario() {
		return pesoVivoUnitario;
	}

	/**
	 * @param pesoVivoUnitario the pesoVivoUnitario to set
	 */
	public void setPesoVivoUnitario(String pesoVivoUnitario) {
		this.pesoVivoUnitario = pesoVivoUnitario;
	}

	
	public ValidationErrors validate()
	{
		ValidationErrors errors = new ValidationErrors();

		return errors;
	}

	public ValidationErrors validateUpdate()
	{
		ValidationErrors errors=validate();
		return errors;
	}

  public String getQuantitaProprieta()
  {
    return quantitaProprieta;
  }

  public void setQuantitaProprieta(String quantitaProprieta)
  {
    this.quantitaProprieta = quantitaProprieta;
  }
  
  public Long getQuantitaProprietaLong()
  {
    try
    {
      return new Long(quantitaProprieta);
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  public void setQuantitaProprietaLong(Long quantitaProprieta)
  {
    this.quantitaProprieta = quantitaProprieta==null?null:quantitaProprieta.toString();
  }
	


}
