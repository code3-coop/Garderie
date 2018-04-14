package org.code3.garderie;

public class Group {
  private Long id;
  private String name;
  private String educator;

  public Group(Long id, String name, String educator){
      this.id = id;
      this.name = name;
      this.educator = educator;
  }

  public Long getId(){
    return id;
  }
  public String getName(){
    return name;
  }
  public String getEducator(){
    return educator;
  }

}
