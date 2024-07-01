package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.model.Match;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.MatchType;
import practice.communityservice.domain.model.enums.Sex;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class MatchRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MatchRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.objectMapper = new ObjectMapper();
    }

    private RowMapper<Match> matchRowMapper() {
        return (rs, rowNum1) -> {
            Match match = new Match(
                    rs.getLong("id"),
                    rs.getString("title"),
                    (LocalDateTime) rs.getObject("time"),
                    rs.getString("address"),
                    rs.getInt("price"),
                    rs.getString("info"),
                    MatchStatus.valueOf(rs.getString("status")),
                    rs.getString("link"),
                    rs.getInt("sex"),
                    MatchType.valueOf(rs.getString("type")),
                    rs.getLong("author_id"),
                    rs.getInt("total_cnt"),
                    rs.getInt("current_cnt")
            );
            return match;
        };
    }

    ;

    public List<Match> getMatchList(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT m.id AS id, m.title AS title, m.time AS time, m.address AS address, m.price AS price, m.info AS info, m.status AS status, m.link AS link, m.sex AS sex\n" +
                "FROM `match` AS m\n " +
                "WHERE DATE_FORMAT(?,'%Y-%m-%d %H:%m:%s') > time AND time >= DATE_FORMAT(?, '%Y-%m-%d %H:%m:%s')\n" +
                "ORDER BY m.time ASC;";
        return jdbcTemplate.query(sql, matchRowMapper(), endTime, startTime);
    }

    public List<Match> getMatchList(LocalDateTime startTime, LocalDateTime endTime, MatchStatus matchStatus) {
        String sql = "SELECT m.id AS id, m.title AS title, m.time AS time, m.address AS address, m.price AS price, m.info AS info, m.status AS status, m.link AS link, m.sex AS sex\n" +
                "FROM `match` AS m\n " +
                "WHERE DATE_FORMAT(?,'%Y-%m-%d %H:%m:%s') > time AND time >= DATE_FORMAT(?, '%Y-%m-%d %H:%m:%s') AND m.status != ?\n" +
                "ORDER BY m.time ASC;";
        return jdbcTemplate.query(sql, matchRowMapper(), endTime, startTime, matchStatus.getValue());
    }

    public List<Match> getMatchList(LocalDateTime startTime, LocalDateTime endTime, Sex sex) {
        String sql = "SELECT m.id AS id, m.title AS title, m.time AS time, m.address AS address, m.price AS price, m.info AS info, m.status AS status, m.link AS link, m.sex AS sex\n" +
                "FROM `match` AS m\n " +
                "WHERE DATE_FORMAT(?,'%Y-%m-%d %H:%m:%s') > time AND time >= DATE_FORMAT(?, '%Y-%m-%d %H:%m:%s') AND m.sex = ?\n" +
                "ORDER BY m.time ASC;";
        return jdbcTemplate.query(sql, matchRowMapper(), endTime, startTime, sex.getValue());
    }

    public List<Match> getMatchList(LocalDateTime startTime, LocalDateTime endTime, MatchStatus matchStatus, Sex sex) {
        String sql = "SELECT m.id AS id, m.title AS title, m.time AS time, m.address AS address, m.price AS price, m.info AS info, m.status AS status, m.link AS link, m.sex AS sex\n" +
                "FROM `match` AS m\n " +
                "WHERE DATE_FORMAT(?,'%Y-%m-%d %H:%m:%s') >= time AND time >= DATE_FORMAT(?, '%Y-%m-%d %H:%m:%s') AND m.status != ? AND m.sex = ?\n" +
                "ORDER BY m.time ASC;";
        return jdbcTemplate.query(sql, matchRowMapper(), endTime, startTime, matchStatus.getValue(), sex.getValue());
    }

    public int getMatchCountByCurrentTime(LocalDateTime currentTime) {
        LocalDateTime nextDay = currentTime.plusDays(1);
        String sql = "SELECT COUNT(id) AS cnt FROM `match`\n" +
                "WHERE DATE_FORMAT(?,'%Y-%m-%d 15:00:00') > time AND time >= DATE_FORMAT(?, '%Y-%m-%d %H:%m:%s');";
        return jdbcTemplate.query(sql, (rs) -> {
            rs.next();
            return rs.getInt("cnt");
        }, nextDay, currentTime);
    }

    public Long save(Match match) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("match").withCatalogName("db").usingGeneratedKeyColumns("id");
        Map<String, Object> params = objectMapper.convertValue(match, Map.class);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }
}
