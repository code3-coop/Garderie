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
import org.springframework.transaction.annotation.Transactional;

@Service
public class PresenceRepository {
  private static final Logger log = LoggerFactory.getLogger(PresenceRepository.class);

  private static final String GET_PRESENCE_BY_DATE_AND_GROUP = "" +
    "select "+
    "  p.date, "+
    "  p.state, "+
    "  p.child_id, "+
    "  p.absence_reason, "+
    "  p.author, " +
    " p.last_modification " +
    "from presence p " +
    "join child c on c.id = p.child_id " +
    "where date = :date " +
    "and c.group = :groupId;";

  private static final String GET_CHILDREN_IN_LIST = "" +
    "select id, firstname, lastname, image_url from child where id in (:childrenIds)";

  private static final String UPDATE_PRESENCE = "" +
    "update presence set " +
    " state = :state," +
    " absence_reason = :absence_reason, " +
    " author = :author, "+
    " last_modification = :last_modification " +
    "where "+
    "date = :date and " +
    "child_id = :child_id;";

  private static final String GET_PRESENCE_BY_CHILD_AND_DATE = "" +
    "select " +
    "  date," +
    "  state," +
    "  child_id," +
    "  absence_reason," +
    "  author, " +
    "  last_modification " +
    "from presence where " +
    "date = :date and " +
    "child_id = :child_id;";

  private static final String INSERT_PRESENCE = "" +
    "insert into presence"+
    "("+
    "  date,"+
    "  state,"+
    "  child_id," +
    "  absence_reason,"+
    "  author," +
    "  last_modification " +
    " ) values ("+
    "  :date,"+
    "  :state,"+
    "  :child_id," +
    "  :absence_reason,"+
    "  :author, " +
    "  :last_modification" +
    ");";

  private static final String GET_PRESENCE_BY_CHILD_BETWEEN_TWO_DATES = "" +
    "select " +
    "  date," +
    "  child_id," +
    "  state," +
    "  absence_reason," +
    "  author,"+
    "  last_modification " +
    " from presence " +
    " where" +
    "  child_id = :child_id and" +
    "  date > :from and" +
    "  date < :to;";

  private final RowMapper<PresenceRow> presenceRowMapper = (rs, rowNum) -> {
    return new PresenceRow(
      rs.getDate("date"),
      rs.getString("state"),
      rs.getLong("child_id"),
      rs.getString("absence_reason"),
      rs.getString("author"),
      rs.getDate("last_modification")
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

    Map params = Map.of(
      "date", date,
      "groupId", group.getId()
    );
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

  @Transactional
  public void createOrUpdate(Presence presence){
    log.debug("createOrUpdate {}", presence);
    Map<String, Object> params1 = Map.of(
      "date", presence.getDate(),
      "child_id", presence.getChild().getId()
    );

    var currentPresence = namedParameterJdbcTemplate.query(GET_PRESENCE_BY_CHILD_AND_DATE, params1, this.presenceRowMapper);
    //if presenceIsAlreadyDefined
    var stmt = currentPresence.size() > 0 ? UPDATE_PRESENCE : INSERT_PRESENCE;
    Map<String, Object> params2 = Map.of(
      "date", presence.getDate(),
      "child_id", presence.getChild().getId(),
      "author", presence.getAuthor(),
      "state", presence.getState(),
      "absence_reason", presence.getAbsenceReason(),
      "last_modification", new Date()
    );
    namedParameterJdbcTemplate.update(stmt, params2);

  }

  public List<Presence> getPresenceByChildBetweenTwoDates(Child child, Date from, Date to){
    Map<String, Object> presenceParams = Map.of(
      "child_id", child.getId(),
      "from", from,
      "to", to
    );
    return namedParameterJdbcTemplate
    .query(GET_PRESENCE_BY_CHILD_BETWEEN_TWO_DATES, presenceParams, this.presenceRowMapper)
    .stream()
    .map(row -> new Presence(
        row.date,
        row.state,
        child,
        row.absence_reason,
        row.author
      )).collect(Collectors.toList());
  }

  private class PresenceRow {
    public Date date;
    public String state;
    public long child_id;
    public String absence_reason;
    public String author;
    public Date last_modification;

    PresenceRow(Date date, String state, long child_id, String absence_reason, String author, Date last_modification){
      this.date = date;
      this.state = state;
      this.child_id = child_id;
      this.absence_reason = absence_reason;
      this.author = author;
      this.last_modification = last_modification;
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

}
