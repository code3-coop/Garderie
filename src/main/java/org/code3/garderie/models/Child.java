package org.code3.garderie;

import java.util.Date;

public class Child {
  private long id;
  private String firstname;
  private String lastname;
  private Date birthdate;
  private String imageUrl;
  private long parents;
  private Group group;

  Child(long id, String firstname, String lastname, Date birthdate, String imageUrl, long parents, Group group){
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.birthdate = birthdate;
    this.imageUrl = imageUrl;
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
  public String getImageUrl(){
    return imageUrl;
  }
  public Long getParents(){
    return parents;
  }
  public Group getGroup(){
    return group;
  }

  @Override
  public String toString(){
    var sb = new StringBuilder();
    sb.append("Child" + "\n");
    sb.append("  id: " + id + "\n");
    sb.append("  firstname" + firstname + "\n");
    sb.append("  lastname" + lastname + "\n");
    sb.append("  birthdate" + birthdate + "\n");
    sb.append("  imageUrl" + imageUrl + "\n");
    sb.append("  parents" + parents + "\n");
    sb.append("  group" + group + "\n");
    return sb.toString();
  }

}
