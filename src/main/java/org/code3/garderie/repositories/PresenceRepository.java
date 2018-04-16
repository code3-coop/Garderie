package org.code3.garderie;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PresenceRepository {
  private static final Logger log = LoggerFactory.getLogger(PresenceRepository.class);

  private static final String GET_PRESENCE_BY_DATE_AND_GROUP = "" +
    "select p.date, p.state, p.child, p.absence_reason, p.author " +
    "from presence p " +
    "join child c on c.id = p.child " +
    "where date = :date " +
    "and c.group = :groupId;";

  private static final String GET_CHILDREN_IN_LIST = "" +
    "select id, firstname, lastname, image_url from child where id in (:childrenIds)";

  private final RowMapper<PresenceRow> presenceRowMapper = (rs, rowNum) -> {
    return new PresenceRow(
      rs.getDate("date"),
      rs.getString("state"),
      rs.getLong("child"),
      rs.getString("absence_reason"),
      rs.getString("author")
    );
  };

  private final RowMapper<ChildRow> childRowMapper = (rs, rowNum) -> {
    return new ChildRow(
      rs.getLong("id"),
      rs.getString("firstname"),
      rs.getString("lastname"),
      rs.getString("image_url")
    );
  };


  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<Presence> getPresenceByDateAndGroup(Date date, Group group){
    log.debug("getPresenceByDate {} {}", date,  group);

    Map params = Map.of("date", date, "groupId", group.getId());
    List<PresenceRow> presenceRows = namedParameterJdbcTemplate.query(GET_PRESENCE_BY_DATE_AND_GROUP, params, this.presenceRowMapper);

    var childrenIds = presenceRows.stream().map((presenceRow) -> presenceRow.child_id).collect(Collectors.toList());
    var params2 = Map.of("childrenIds", childrenIds);

    List<ChildRow> childRows = namedParameterJdbcTemplate.query(GET_CHILDREN_IN_LIST, params2, this.childRowMapper);
    return presenceRows.stream().flatMap((presenceRow) -> {
      return childRows
        .stream()
        .filter((childRow) -> childRow.id.equals(presenceRow.child_id))
        .limit(1)
        .map((childRow) -> {
          return new Presence(
            presenceRow.date,
            presenceRow.state,
            childRow.toChild(),
            presenceRow.absence_reason,
            presenceRow.author
          );
        });
      }).collect(Collectors.toList());
  }


  private class PresenceRow {
    public Date date;
    public String state;
    public long child_id;
    public String absence_reason;
    public String author;

    PresenceRow(Date date, String state, long child_id, String absence_reason, String author){
      this.date = date;
      this.state = state;
      this.child_id = child_id;
      this.absence_reason = absence_reason;
      this.author = author;
    }
  }

  private class ChildRow {
    public Long id;
    public String firstname;
    public String lastname;
    public String image_url;

    ChildRow(Long id, String firstname, String lastname, String image_url){
      this.id = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.image_url =  image_url;
    }

    Child toChild(){
      return new Child(
        this.id,
        this.firstname,
        this.lastname,
        null,
        this.image_url,
        0,
        0
      );
    }
  }

  //should return an id
  // public void createPresence(Presence presence){
  //   log.debug("createPresence " + presence.toString());
  //   namedParameterJdbcTemplate.update(CREATE_PRESENCE, presence.getDate(), presence.getState(), presence.getChild(), presence.getAbsenceReason(), presence.getAuthor());
  // }

}
