package org.code3.garderie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class ChildRepository {

  private static final Logger log = LoggerFactory.getLogger(ChildRepository.class);
  private static final String GET_CHILD = "" +
    "select "  +
    " id, "  +
    " firstname, "  +
    " lastname, "  +
    " birthdate, "  +
    " image_url, "  +
    " parents, "  +
    " \"group\" "  +
    "from child "  +
    " where id = :child_id;";

  private static final String CREATE_CHILD = "" +
    "insert into child (" +
    " firstname, " +
    " lastname, " +
    " birthdate, " +
    " image_url, " +
    " parents, " +
    " \"group\"" +
    ") values (" +
    " :firstname, " +
    " :lastname, " +
    " :birthdate, " +
    " :image_url, " +
    " :parents, " +
    " :group" +
    ");";

  private static final String LIST_CHILD = ""+
    "select "+
    " id, "+
    " firstname, "+
    " lastname, "+
    " birthdate, "+
    " image_url, "+
    " parents, "+
    " \"group\" "+
    "from child;";

  private static final String GET_CHILDREN_BY_GROUP = ""+
  "select "+
  " id, "+
  " firstname, "+
  " lastname, "+
  " birthdate, "+
  " image_url, "+
  " parents, "+
  " \"group\" "+
  "from child where \"group\" = :group_id;";

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Child getChildById(long childId) {
    log.debug("Get child by id: " + childId);
    Map<String, Long> params = Map.of(
      "child_id", childId
    );
    return namedParameterJdbcTemplate.query(GET_CHILD, params, childRowMapper()).get(0);
  }

  @Transactional
  public void createChild(Child child){
    log.debug("Create child "+ child.toString());
    Map<String, Object> params = Map.of(
      "firstname", child.getFirstname(),
      "lastname", child.getLastname(),
      "birthdate", child.getBirthdate(),
      "image_url", child.getImageUrl(),
      "parents", child.getParents(),
      "group", child.getGroup()
    );
    namedParameterJdbcTemplate.update(CREATE_CHILD, params);
  }//XXX This should return a complete child (with Id)

  public List<Child> getAllChildren(){
    log.debug("Get list of child");
    return namedParameterJdbcTemplate.query(LIST_CHILD,childRowMapper());
  }

  public List<Child> getChildrenByGroup(Group group){
    log.debug("getChildrenByGroup" + group);
    Map<String, Object> params = Map.of(
      "group_id", group.getId()
    );
    return namedParameterJdbcTemplate.query(GET_CHILDREN_BY_GROUP, params, childRowMapper());
  }

  private RowMapper<Child> childRowMapper(){
    return (rs, rowNum) -> new Child(
      rs.getLong("id"),
      rs.getString("firstname"),
      rs.getString("lastname"),
      rs.getDate("birthdate"),
      rs.getString("image_url"),
      rs.getLong("parents"),
      rs.getLong("group")
    );
  }
  // private class ChildRow {
  //   Long id;
  //   String firstname;
  //   String lastname;
  //   Date birthdate;
  //   String image_url;
  //   Long parents;
  //   Long group;
  // };
}
