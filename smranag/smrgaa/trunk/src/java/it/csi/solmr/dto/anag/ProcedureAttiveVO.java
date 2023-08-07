package it.csi.solmr.dto.anag;

import java.io.*;
import it.csi.solmr.dto.*;

public class ProcedureAttiveVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -3694543212357027197L;

  private Long idAzienda = null;
  private CodeDescription tipoProcedimento = null;
  private Long stato = null;

  public ProcedureAttiveVO(){
    tipoProcedimento = new CodeDescription();
  }

  public void setIdAzienda(Long idAzienda){
    this.idAzienda = idAzienda;
  }
  public Long getIdAzienda(){
    return idAzienda;
  }
  public void setTipoProcedimento(CodeDescription tipoProcedimento) {
    this.tipoProcedimento = tipoProcedimento;
  }
  public CodeDescription getTipoProcedimento() {
    return tipoProcedimento;
  }
  public void setStato(Long stato){
    this.stato = stato;
  }
  public Long getStato(){
    return stato;
  }

  public int hashCode(){
    return (idAzienda == null ? 0 : idAzienda.hashCode())+
           (stato == null ? 0 : stato.hashCode()) +
           (tipoProcedimento.getCode() == null ? 0 : tipoProcedimento.hashCode());
  }
  public boolean equals(Object o) {
   if (o instanceof ProcedureAttiveVO) {
     ProcedureAttiveVO other = (ProcedureAttiveVO)o;
     return (this.idAzienda == null && other.idAzienda == null || this.idAzienda == other.idAzienda)&&
            (this.tipoProcedimento.getCode() == null && other.tipoProcedimento.getCode() == null || this.tipoProcedimento.getCode().equals(other.tipoProcedimento.getCode()))&&
            (this.stato == null && other.stato == null || other.stato == other.stato);

   } else
     return false;
  }
}