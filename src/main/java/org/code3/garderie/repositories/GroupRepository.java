package org.code3.garderie;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public class GroupRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupRepository.class);
  private static final String GET_ALL_GROUP = "" +
   "select " +
      "id, " +
      "name, " +
      "educator "+
    "from \"group\";";

  private static final String GET_GROUP_BY_ID = "" +
    "select" +
    "  id," +
    "  name," +
    "  educator " +
    "from \"group\"" +
    "where" +
    "  id = :group_id;";

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<Group>getAllGroup(){
    return namedParameterJdbcTemplate.query(GET_ALL_GROUP, groupRowMapper());
  }

  public Group getGroupById(Long groupId){
    Map<String, Long> params = Map.of("group_id", groupId);
    return namedParameterJdbcTemplate
      .query(GET_GROUP_BY_ID, params, groupRowMapper())
      .stream()
      .findFirst()
      .get();
  }

  private RowMapper<Group> groupRowMapper(){
    return (rs, rowNum) -> new Group(
      rs.getLong("id"),
      rs.getString("name"),
      rs.getString("educator")
    );
  }

}
