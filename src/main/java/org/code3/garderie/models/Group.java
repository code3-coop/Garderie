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

  @Override
  public String toString(){
    var sb = new StringBuilder();
    sb.append("Group" + "\n");
    sb.append("  id: " + id + "\n");
    sb.append("  name" + name + "\n");
    sb.append("  educator" + educator + "\n");
    return sb.toString();
  }

}
