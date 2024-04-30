package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.model.Article;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BoardRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public BoardRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.objectMapper = new ObjectMapper();
    }

    private RowMapper<Article> articleRowMapper() {
        return (rs, rowNum1) -> {
            Article article = new Article(
                    rs.getLong("id"),
                    rs.getString("author_name"),
                    rs.getString("category_name"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("view_count"),
                    (LocalDateTime) rs.getObject("date_created"),
                    (LocalDateTime) rs.getObject("date_updated")
            );
            return article;
        };
    };

    public List<Article> getPageArticleList(int page, int pageSize){
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), pageStart, pageSize);
    }

    public int allBoardCount(){
        String sql = "SELECT COUNT(id) AS cnt FROM article;";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");});
    }

    public List<Article> getPageCategoryArticleList(int page, int pageSize, Long categoryId) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE c.id = ?\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), categoryId, pageStart, pageSize);
    }

    public int categoryBoardCount(Long categoryId) {
        String sql = "SELECT COUNT(id) AS cnt FROM article WHERE category_id = ?;";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");}, categoryId);
    }
}
