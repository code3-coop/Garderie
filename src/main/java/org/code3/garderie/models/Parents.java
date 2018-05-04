package org.code3.garderie;

import java.util.Date;

public class Parents{
  private long id;
  private String parent_1_name;
  private String parent_2_name;
  private String parent_1_phone;
  private String parent_2_phone;

  public Parents(String parent_1_name,String parent_2_name,String parent_1_phone,String parent_2_phone){
    this.parent_1_name = parent_1_name;
    this.parent_2_name = parent_2_name;
    this.parent_1_phone = parent_1_phone;
    this.parent_2_phone = parent_2_phone;
  }
  public String getName1(){
    return parent_1_name;
  }
  public String getName2(){
    return parent_2_name;
  }
  public String getPhone1(){
    return parent_1_phone;
  }
  public String getPhone2(){
    return parent_2_phone;
  }

  @Override
  public String toString(){
    var sb = new StringBuilder();
    sb.append("Parents" + "\n");
    sb.append("  parent 1: " + parent_1_name + " " + parent_1_phone + "\n");
    sb.append("  parent 2: " + parent_2_name + " " + parent_2_phone + "\n");
    return sb.toString();
  }
}
