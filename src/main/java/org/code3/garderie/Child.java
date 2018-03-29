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

  Long getId(){
    return id;
  }
  String getFirstname(){
    return firstname;
  }
  String getLastname(){
    return lastname;
  }
  Date getBirthdate(){
    return birthdate;
  }
  String getImage_url(){
    return image_url;
  }
  Long getParents(){
    return parents;
  }
  Long getGroup(){
    return group;
  }

}
