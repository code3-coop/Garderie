package org.code3.garderie;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PresenceRepository {
  private static final Logger log = LoggerFactory.getLogger(PresenceRepository.class);
  private static final String GET_PRESENCE_BY_CHILD_AND_DATE = "select date, state, child, absence_reason, author from presence where child = ? and date = ?";
  private static final String CREATE_PRESENCE = "insert into presence (date, state, child, absence_reason, author) values (?, ?, ?, ?, ?);";
  private static final String GET_PRESENCE_BY_DATE = "select date, state, child, absence_reason, author from presence where date = ?";
  private static final String GET_PRESENCE_BY_CHILD = "select date, state, child, absence_reason, author from presence where child = ?";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Presence> getPresenceByDate(Date date){
    log.debug("Get presence by date "+ date.toString());
    return jdbcTemplate.query(GET_PRESENCE_BY_DATE, new Object[] {date}, presenceRowMapper());
  }

  public void createPresence(Presence presence){
    jdbcTemplate.update(CREATE_PRESENCE, presence.getDate(), presence.getState(), presence.getChild(), presence.getAbsenceReason(), presence.getAuthor());
  }

  private RowMapper<Presence> presenceRowMapper(){
    return (rs, rowNum) -> new Presence(
      rs.getDate("date"),
      rs.getString("state"),
      rs.getLong("child"),
      rs.getString("absence_reason"),
      rs.getString("author")
    );
  }
}
