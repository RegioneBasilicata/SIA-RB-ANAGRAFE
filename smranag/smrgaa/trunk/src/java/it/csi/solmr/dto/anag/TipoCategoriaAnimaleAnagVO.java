package it.csi.solmr.dto.anag;

import java.io.*;
import it.csi.solmr.util.*;


/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Monica Di Marco
 * @version 1.0
 */

public class TipoCategoriaAnimaleAnagVO implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -3068418113504134903L;

	private String idCategoriaAnimale = null;
	private String idSpecieAnimale = null;
	private String descrizioneCategoriaAnimale = null;
	private String consumoAnnuoUF = null;
	private String coefficienteUBA = null;
	private String pesoVivoMedio = null;
	private TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
	private String dataInizioValidita;
	private String dataFineValidita;
	private String pesoVivoMin = null;
	private String pesoVivoMax = null;
	private String lattazione;

	public TipoCategoriaAnimaleAnagVO()
	{
		tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
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

	public String getIdSpecieAnimale()
	{
		return idSpecieAnimale;
	}
	public void setIdSpecieAnimale(String idSpecieAnimale)
	{
		this.idSpecieAnimale = idSpecieAnimale;
	}
	public Long getIdSpecieAnimaleLong()
	{
		try
		{
			return new Long(idSpecieAnimale);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setIdSpecieAnimaleLong(Long idSpecieAnimale)
	{
		this.idSpecieAnimale = idSpecieAnimale==null?null:idSpecieAnimale.toString();
	}

	public String getDescrizioneCategoriaAnimale()
	{
		return descrizioneCategoriaAnimale;
	}
	public void setDescrizioneCategoriaAnimale(String descrizioneCategoriaAnimale)
	{
		this.descrizioneCategoriaAnimale = descrizioneCategoriaAnimale;
	}

	public String getConsumoAnnuoUF()
	{
		return consumoAnnuoUF;
	}
	public void setConsumoAnnuoUF(String consumoAnnuoUF)
	{
		this.consumoAnnuoUF = consumoAnnuoUF;
	}
	public Long getConsumoAnnuoUFLong()
	{
		try
		{
			return new Long(consumoAnnuoUF);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void setConsumoAnnuoUFLong(Long consumoAnnuoUF)
	{
		this.consumoAnnuoUF = consumoAnnuoUF==null?null:consumoAnnuoUF.toString();
	}

	public String getCoefficienteUBA()
	{
		return coefficienteUBA;
	}
	public void setCoefficienteUBA(String coefficienteUBA)
	{
		this.coefficienteUBA = coefficienteUBA;
	}
	public Double getCoefficienteUBADouble()
	{
		try
		{
			return new Double(coefficienteUBA);
		}
		catch (Exception ex)
		{
			return new Double(0);
		}
	}
	public void setCoefficienteUBADouble(Double coefficienteUBA)
	{
		this.coefficienteUBA = coefficienteUBA==null?null:coefficienteUBA.toString();
	}

	public String getPesoVivoMedio()
	{
		return pesoVivoMedio;
	}
	public void setPesoVivoMedio(String pesoVivoMedio)
	{
		this.pesoVivoMedio = pesoVivoMedio;
	}
	public Double getPesoVivoMedioDouble()
	{
		try
		{
			return new Double(pesoVivoMedio);
		}
		catch (Exception ex)
		{
			return new Double(0);
		}
	}
	public void setPesoVivoMedioDouble(Double pesoVivoMedio)
	{
		this.pesoVivoMedio = pesoVivoMedio==null?null:pesoVivoMedio.toString();
	}

	public void setTipoSpecieAnimaleAnagVO(TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO)
	{
		this.tipoSpecieAnimaleAnagVO = tipoSpecieAnimaleAnagVO;
	}
	public TipoSpecieAnimaleAnagVO getTipoSpecieAnimaleAnagVO()
	{
		return this.tipoSpecieAnimaleAnagVO;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(String dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the pesoVivoMin
	 */
	public String getPesoVivoMin() {
		return pesoVivoMin;
	}

	/**
	 * @param pesoVivoMin the pesoVivoMin to set
	 */
	public void setPesoVivoMin(String pesoVivoMin) {
		this.pesoVivoMin = pesoVivoMin;
	}

	/**
	 * @return the pesoVivoMax
	 */
	public String getPesoVivoMax() {
		return pesoVivoMax;
	}

	/**
	 * @param pesoVivoMax the pesoVivoMax to set
	 */
	public void setPesoVivoMax(String pesoVivoMax) {
		this.pesoVivoMax = pesoVivoMax;
	}

	public ValidationErrors validate()
	{
		return null;
	}


  public String getLattazione()
  {
    return lattazione;
  }


  public void setLattazione(String lattazione)
  {
    this.lattazione = lattazione;
  }
 
  



}
