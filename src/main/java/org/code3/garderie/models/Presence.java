package org.code3.garderie;

import java.util.Date;

public class Presence{
  private Date date;
  private String state;
  private Child child;
  private String absence_reason;
  //XXX found out what is the complete class path for DateTime
  // private DateTime last_modification;
  private String author;

  public Presence(Date date, String state, Child child, String absence_reason, String author){
    this.date = date;
    this.state = state;
    this.child = child;
    this.absence_reason = absence_reason;
    this.author = author;
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
}
