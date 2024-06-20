package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.model.Match;
import practice.communityservice.domain.model.Page;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.Sex;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MatchRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    @Autowired
    public MatchRepository(DataSource dataSource){
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
                    rs.getInt("sex")
            );
            return match;
        };
    };
    public List<Match> getMatchList(int page, int pageSize, LocalDateTime currentTime) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT m.id AS id, m.title AS title, m.time AS time, m.address AS address, m.price AS price, m.info AS info, m.status AS status, m.link AS link, m.sex AS sex\n" +
                "FROM `match` AS m\n " +
                "WHERE time >= ?\n" +
                "ORDER BY m.time ASC LIMIT ?,?;";
        return jdbcTemplate.query(sql, matchRowMapper(), currentTime, pageStart, pageSize);
    }

    public int allMatchCount() {
        String sql = "SELECT COUNT(id) AS cnt FROM `match`;";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");});
    }
}
