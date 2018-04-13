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
  private static final String GET_PRESENCE_BY_CHILD = "select date, state, child, absence_reason, author from presence where child = ?";
  private static final String GET_PRESENCE_BY_DATE_AND_GROUP = "" +
    "select p.date, p.state, p.child, p.absence_reason, p.author " +
    "from presence p " +
    "join child c on c.id = p.child " +
    "where date = ? " +
    "and c.group = ?;";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Presence> getPresenceByDateAndGroup(Date date, Group group){
    log.debug("getPresenceByDate " + date.toString() + " " + group.toString());
    return jdbcTemplate.query(GET_PRESENCE_BY_DATE_AND_GROUP, new Object[] {date, group.getId()}, presenceRowMapper());
  }

  public void createPresence(Presence presence){
    log.debug("createPresence " + presence.toString());
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
