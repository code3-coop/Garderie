package org.code3.garderie;

import java.util.Date;

public class Child {
  private long id;
  private String firstname;
  private String lastname;
  private Date birthdate;
  private String image_url;
  private long parents;
  private long group;

  Child(long id, String firstname, String lastname, Date birthdate, String image_url, long parents, long group){
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.birthdate = birthdate;
    this.image_url = image_url;
    this.parents = parents;
    this.group = group;
  }

  public Long getId(){
    return id;
  }
  public String getFirstname(){
    return firstname;
  }
  public String getLastname(){
    return lastname;
  }
  public Date getBirthdate(){
    return birthdate;
  }
  public String getImage_url(){
    return image_url;
  }
  public Long getParents(){
    return parents;
  }
  public Long getGroup(){
    return group;
  }

}
