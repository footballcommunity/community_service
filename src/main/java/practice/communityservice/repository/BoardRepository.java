package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.model.Article;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public long postArticle(Long author_id, Long category_id, String title, String content) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        Map<String, Object> map = new HashMap<>();
        map.put("author_id", author_id);
        map.put("category_id", category_id);
        map.put("title", title);
        map.put("content", content);
        Number key = simpleJdbcInsert.withTableName("article").usingGeneratedKeyColumns("id","view_count", "date_created", "date_updated").executeAndReturnKey(map);
        return key.longValue();
    }

    public List<Article> getPageByTitle(int page, int pageSize, String title) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE a.title LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), title, pageStart, pageSize);
    }
    public int getPageCountByTitle(String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article WHERE title LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");},keyword);
    }

    public List<Article> getPageByAuthor(int page, int pageSize, String username) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE u.username LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), username, pageStart, pageSize);
    }
    public int getPageCountByAuthor(String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article a\n" +
                "JOIN user u ON u.id = a.author_id\n" +
                "WHERE u.username LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");},keyword);
    }

    public List<Article> getPageByContent(int page, int pageSize, String contentKey) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE a.content LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), contentKey, pageStart, pageSize);

    }
    public int getPageCountByContent(String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article\n" +
                "WHERE content LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");},keyword);
    }

    public List<Article> getPageByCategoryAndTitle(int page, int pageSize, Long categoryId, String title) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE c.id = ? AND a.title LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), categoryId, title, pageStart, pageSize);
    }
    public int getPageCountByCategoryAndTitle(Long categoryId, String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "WHERE c.id = ? AND a.title LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");}, categoryId, keyword);
    }

    public List<Article> getPageByCategoryAndAuthor(int page, int pageSize, Long categoryId, String username) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE c.id = ? AND u.username LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), categoryId, username, pageStart, pageSize);
    }
    public int getPageCountByCategoryAndAuthor(Long categoryId, String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article a\n" +
                "JOIN user u ON u.id = a.author_id\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "WHERE c.id = ? AND u.username LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");},categoryId,keyword);
    }

    public List<Article> getPageByCategoryAndContent(int page, int pageSize, Long categoryId, String contentKey) {
        int pageStart = (page-1) * pageSize;
        String sql = "SELECT a.id AS id, u.username AS author_name, c.name AS category_name, a.title AS title, a.content AS content, a.view_count AS view_count, a.date_created AS date_created, a.date_updated AS date_updated " +
                "FROM article AS a\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "JOIN user AS u ON a.author_id = u.id\n" +
                "WHERE c.id = ? AND a.content LIKE CONCAT('%',?,'%')\n" +
                "ORDER BY a.date_created DESC LIMIT ?,?;";
        return jdbcTemplate.query(sql,articleRowMapper(), categoryId, contentKey, pageStart, pageSize);

    }
    public int getPageCountByCategoryAndContent(Long categoryId, String keyword) {
        String sql = "SELECT COUNT(id) AS cnt FROM article\n" +
                "JOIN category AS c ON c.id = a.category_id\n" +
                "WHERE c.id = ? AND content LIKE CONCAT('%',?,'%');";
        return jdbcTemplate.query(sql, (rs) -> {rs.next(); return rs.getInt("cnt");}, categoryId, keyword);
    }

}
