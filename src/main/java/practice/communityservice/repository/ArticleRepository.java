package practice.communityservice.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.dto.ArticleDto;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ArticleRepository {

    private JdbcTemplate jdbcTemplate;
    public ArticleRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<ArticleDto> getArticleById(long articleId) {
        String sql = "SELECT a.id AS article_id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content as content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated\n" +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE a.id = ?;";
        return Optional.of(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ArticleDto.class), articleId));
    }

    public Long postArticle(Long authorId, Long categoryId, String title, String content) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        Map<String, Object> map = new HashMap<>();
        map.put("author_id", authorId);
        map.put("category_id", categoryId);
        map.put("title", title);
        map.put("content", content);
        Number key = simpleJdbcInsert.withTableName("article").usingGeneratedKeyColumns("id","view_count", "date_created", "date_updated").executeAndReturnKey(map);
        return key.longValue();
    }
}
