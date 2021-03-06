package org.code3.garderie;

import java.util.Date;

public class Presence{
  private Date date;
  private String state;
  private Child child;
  private String absence_reason;
  private Date last_modification;
  private String author;
  private String day_part;

  public Presence(Date date, String state, Child child, String absence_reason, String author, String day_part){
    this.date = date;
    this.state = state;
    this.child = child;
    this.absence_reason = absence_reason;
    this.author = author;
    this.day_part = day_part;
  }
  public Date getDate(){
    return date;
  }
  public String getState(){
    return state;
  }
  public Child getChild(){
    return child;
  }
  public String getAbsenceReason(){
    return absence_reason;
  }
  public String getAuthor(){
    return author;
  }
  public String getDayPart(){
    return day_part;
  }

  @Override
  public String toString(){
    var sb = new StringBuilder();
    sb.append("Presence" + "\n");
    sb.append("  date: " + date + "\n");
    sb.append("  state" + state + "\n");
    sb.append("  child" + child + "\n");
    sb.append("  absence_reason" + absence_reason + "\n");
    sb.append("  last_modification" + last_modification + "\n");
    sb.append("  author" + author + "\n");
    sb.append("  day_part" + day_part + "\n");
    return sb.toString();
  }
}
