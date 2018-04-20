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
    sb.add("Child" + "\n");
    sb.add("  id: " id + "\n");
    sb.add("  firstname" + firstname + "\n");
    sb.add("  lastname" + lastname + "\n");
    sb.add("  birthdate" + birthdate + "\n");
    sb.add("  imageUrl" + imageUrl + "\n");
    sb.add("  parents" + parents + "\n");
    sb.add("  group" + group + "\n");
    return sb.toString();
  }

}
