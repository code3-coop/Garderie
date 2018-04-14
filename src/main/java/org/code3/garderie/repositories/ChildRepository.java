package org.code3.garderie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChildRepository {

  private static final Logger log = LoggerFactory.getLogger(ChildRepository.class);
  private static final String GET_CHILD = "select id, firstname, lastname, birthdate, image_url, parents, \"group\" from child where id = ?;";
  private static final String CREATE_CHILD = "insert into child (firstname, lastname, birthdate, image_url, parents, \"group\") values (?, ?, ?, ?, ?, ?);";
  private static final String LIST_CHILD = "select id, firstname, lastname, birthdate, image_url, parents, \"group\" from child;";
  private static final String GET_CHILDREN_BY_GROUP = "select id, firstname, lastname, birthdate, image_url, parents, \"group\" from child where \"group\" = ?;";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Child getChildById(long childId) {
    log.debug("Get child by id: " + childId);
    return jdbcTemplate.queryForObject(GET_CHILD, new Object[] {childId}, childRowMapper());
  }

  @Transactional
  public void createChild(Child child){
    log.debug("Create child "+ child.toString());
    jdbcTemplate.update(CREATE_CHILD, child.getFirstname(), child.getLastname(), child.getBirthdate(), child.getImageUrl(), child.getParents(), child.getGroup());
  }//XXX This should return a complete child (with Id)

  public List<Child> getAllChildren(){
    log.debug("Get list of child");
    return jdbcTemplate.query(LIST_CHILD,childRowMapper());
  }

  public List<Child> getChildrenByGroup(Group group){
    log.debug("getChildrenByGroup" + group);
    return jdbcTemplate.query(GET_CHILDREN_BY_GROUP, new Object[]{group.getId()}, childRowMapper());
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
}
