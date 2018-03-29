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
  public static String GET_CHILD = "select id, firstname, lastname, birthdate, image_url, parents, \"group\" from child where id = ?;";
  public static String CREATE_CHILD = "insert into child (firstname, lastname, birthdate, image_url, parents, \"group\") values (?, ?, ?, ?, ?, ?);";
  public static String LIST_CHILD = "select id, firstname, lastname, birthdate, image_url, parents, \"group\" from child;";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Child getChildById(long childId) {
    log.info("Get child by id: " + childId);
    return jdbcTemplate.queryForObject(GET_CHILD, new Object[] {childId}, childRowMapper());
  }

  @Transactional
  public void createChild(Child child){
    log.debug("Create child");
    jdbcTemplate.update(CREATE_CHILD, child.getFirstname(), child.getLastname(), child.getBirthdate(), child.getImage_url(), child.getParents(), child.getGroup());
  }

  public List<Child> getAllChildren(){
    log.debug("Get list of child");
    return jdbcTemplate.query(LIST_CHILD, childRowMapper());
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
