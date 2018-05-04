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
public class ParentsRepository {
  private static final Logger log = LoggerFactory.getLogger(ParentsRepository.class);

  private static final String GET_PARENTS_BY_CHILD = "" +
    "select " +
    "  parent_1_name," +
    "  parent_2_name," +
    "  parent_1_phone," +
    "  parent_2_phone" +
    " from parents " +
    " where" +
    "  id = :parent_id";

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Parents getParentsByChild(Child child){
    log.debug("getParentsByChild {} {}", child);
    Map<String, Object> parentsParams = Map.of(
      "parent_id", child.getParents()
    );
    return namedParameterJdbcTemplate
    .query(GET_PARENTS_BY_CHILD, parentsParams, this.parentsRowMapper)
    .stream()
    .limit(1)
    .map(row -> new Parents(
        row.parent_1_name,
        row.parent_2_name,
        row.parent_1_phone,
        row.parent_1_phone
      ))
      .findFirst()
      .get();
  }

  private final RowMapper<ParentsRow> parentsRowMapper = (rs, rowNum) -> {
    return new ParentsRow(
      rs.getString("parent_1_name"),
      rs.getString("parent_2_name"),
      rs.getString("parent_1_phone"),
      rs.getString("parent_2_phone")
    );
  };

  private class ParentsRow {
    public String parent_1_name;
    public String parent_2_name;
    public String parent_1_phone;
    public String parent_2_phone;

    ParentsRow(String parent_1_name, String parent_2_name, String parent_1_phone, String parent_2_phone){
      this.parent_1_name = parent_1_name;
      this.parent_2_name = parent_2_name;
      this.parent_1_phone = parent_1_phone;
      this.parent_2_phone = parent_2_phone;
    }
  }

}
