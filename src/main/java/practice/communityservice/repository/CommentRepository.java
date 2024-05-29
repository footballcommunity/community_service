package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.dto.CommentDto;
import practice.communityservice.dto.request.WriteCommentRequestDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public CommentRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        objectMapper = new ObjectMapper();
    }

    public List<CommentDto> getCommentByArticleId(long articleID){
        String sql = "SELECT c.id AS comment_id, u.username AS username, c.content AS content, c.date_created AS date_created, c.date_updated AS date_updated, c.parent_id AS parent_id\n" +
                "    FROM comment AS c\n" +
                "    JOIN user AS u ON c.user_id = u.id\n" +
                "    WHERE c.article_id = ?\n" +
                "    ORDER BY c.date_created, c.parent_id;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CommentDto.class), articleID);
    }

    public Long saveComment(WriteCommentRequestDto writeCommentRequestDto){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("comment").withCatalogName("db").usingGeneratedKeyColumns("id","date_created","date_updated");
        Map<String, Object> params = objectMapper.convertValue(writeCommentRequestDto, Map.class);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }
}
