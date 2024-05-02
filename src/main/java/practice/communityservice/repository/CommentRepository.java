package practice.communityservice.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import practice.communityservice.dto.CommentDto;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentRepository {

    private JdbcTemplate jdbcTemplate;

    public CommentRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CommentDto> getCommentByArticleId(long articleID){
        String sql = "SELECT c.id AS comment_id, u.username AS username, c.content AS content, c.date_created AS date_created, c.date_updated AS date_updated, c.parent_id AS parent_id\n" +
                "    FROM comment AS c\n" +
                "    JOIN user AS u ON c.user_id = u.id\n" +
                "    WHERE c.article_id = ?;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CommentDto.class), articleID);
    }

}
