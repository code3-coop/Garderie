package org.code3.garderie;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    " group_id "  +
    "from child "  +
    " where id = :child_id;";

  private static final String CREATE_CHILD = "" +
    "insert into child (" +
    " firstname, " +
    " lastname, " +
    " birthdate, " +
    " image_url, " +
    " parents, " +
    " group_id " +
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
    " group_id "+
    "from child;";

  private static final String GET_CHILDREN_BY_GROUP = ""+
    "select "+
    " id, "+
    " firstname, "+
    " lastname, "+
    " birthdate, "+
    " image_url, "+
    " parents, "+
    " group_id "+
    "from child where group_id = :group_id;";

  private static final String GET_GROUP_BY_ID = "" +
    "select "+
    "  id, "+
    "  name, "+
    "  educator "+
    "from \"group\" "+
    "where " +
    "  id = :group_id; ";

  private static final String GET_GROUP_BY_ID_LIST = "" +
    "select "+
    "  id, "+
    "  name, "+
    "  educator "+
    "from \"group\" "+
    "where " +
    "  id in (:group_ids) ";

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Child getChildById(long childId) {
    log.debug("getChildById {}",  childId);
    Map<String, Long> params = Map.of(
      "child_id", childId
    );
    List<ChildRow> childrenRows =  namedParameterJdbcTemplate.query(GET_CHILD, params, childRowMapper());

    var groupId = childrenRows
      .stream()
      .map(childRow -> childRow.group_id)
      .findFirst()
      .get();

    Map<String, Object> groupParams = Map.of(
      "group_id", groupId
    );

    var group = namedParameterJdbcTemplate
      .query(GET_GROUP_BY_ID, groupParams, groupRowMapper())
      .stream()
      .findFirst()
      .get();

    return childrenRows
    .stream()
    .limit(1)
    .map(childRow -> new Child(
      childRow.id,
      childRow.firstname,
      childRow.lastname,
      childRow.birthdate,
      childRow.image_url,
      childRow.parents,
      group
    ))
    .findFirst()
    .get();
  }

  @Transactional
  public void createChild(Child child){
    log.debug("createChild {}", child.toString());
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
    log.debug("getAllChildren");

    var childRows = namedParameterJdbcTemplate.query(LIST_CHILD,childRowMapper());

    var groupIds = childRows
    .stream()
    .map(childRow -> childRow.group_id)
    .collect(Collectors.toList());

    Map<String,List<Long>> groupParams = Map.of(
      "group_ids", groupIds
    );

    List<Group> groups = namedParameterJdbcTemplate.query(GET_GROUP_BY_ID_LIST, groupParams, groupRowMapper());

    return childRows
    .stream()
    .map( childRow -> {
      var group = groups
        .stream()
        .filter(currentGroup -> currentGroup.getId().equals(childRow.group_id))
        .findFirst()
        .get();

      return new Child(
        childRow.id,
        childRow.firstname,
        childRow.lastname,
        childRow.birthdate,
        childRow.image_url,
        childRow.parents,
        group);
    })
    .collect(Collectors.toList());
  }

  private Group getGroupById(Long id){
    Map<String, Object> groupParams = Map.of(
      "groupId", id
    );
    return namedParameterJdbcTemplate
      .query(GET_GROUP_BY_ID, groupParams, groupRowMapper())
      .stream()
      .findFirst()
      .get();
  }

  public List<Child> getChildrenByGroup(Group group){
    log.debug("getChildrenByGroup" + group);
    Map<String, Object> params = Map.of(
      "group_id", group.getId()
    );
    return namedParameterJdbcTemplate
    .query(GET_CHILDREN_BY_GROUP, params, childRowMapper())
    .stream()
    .map(childRow -> new Child(
      childRow.id,
      childRow.firstname,
      childRow.lastname,
      childRow.birthdate,
      childRow.image_url,
      childRow.parents,
      group
    )).collect(Collectors.toList());
  }

  private RowMapper<ChildRow> childRowMapper(){
    return (rs, rowNum) -> new ChildRow(
      rs.getLong("id"),
      rs.getString("firstname"),
      rs.getString("lastname"),
      rs.getDate("birthdate"),
      rs.getString("image_url"),
      rs.getLong("parents"),
      rs.getLong("group_id")
    );
  }

  private RowMapper<Group> groupRowMapper(){
    return (rs, rowNum) -> new Group(
      rs.getLong("id"),
      rs.getString("name"),
      rs.getString("educator")
    );
  }

  private class ChildRow {
    Long id;
    String firstname;
    String lastname;
    Date birthdate;
    String image_url;
    Long parents;
    Long group_id;

    public ChildRow(Long id, String firstname, String lastname, Date birthdate, String image_url, Long parents, Long group_id){
      this.id = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.birthdate = birthdate;
      this.image_url = image_url;
      this.parents = parents;
      this.group_id = group_id;
    }

  };

}
