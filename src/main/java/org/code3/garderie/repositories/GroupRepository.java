package org.code3.garderie;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class GroupRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupRepository.class);
  private static final String GET_ALL_GROUP = "select id, name, educator from \"group\";";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Group>getAllGroup(){
    return jdbcTemplate.query(GET_ALL_GROUP, groupRowMapper());
  }

  private RowMapper<Group> groupRowMapper(){
    return (rs, rowNum) -> new Group(
      rs.getLong("id"),
      rs.getString("name"),
      rs.getString("educator")
    );
  }

}
