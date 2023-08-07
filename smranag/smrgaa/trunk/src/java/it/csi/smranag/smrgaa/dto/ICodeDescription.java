package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;

public interface ICodeDescription extends Serializable
{
  public long getCode();
  public void setCode(long code);
  public String getDescription();
  public void setDescription(String description);
  
}
