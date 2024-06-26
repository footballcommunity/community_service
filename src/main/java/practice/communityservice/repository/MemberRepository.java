package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        objectMapper = new ObjectMapper();
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum1) -> {
            User user = new User(
                    rs.getLong("id"),
                    Role.valueOf(rs.getObject("role").toString()),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    (LocalDateTime) rs.getObject("date_created"),
                    UserStatus.valueOf(rs.getObject("status").toString())
            );
            return user;
        };
    };
    public Long save(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").withCatalogName("db").usingGeneratedKeyColumns("id","date_created");
        Map<String, Object> params = objectMapper.convertValue(user, Map.class);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

public Optional<User> findByEmail(String srcEmail) {
        String sql = "SELECT * FROM user WHERE email = ?";
        List<User> userList = jdbcTemplate.query(sql, userRowMapper(),srcEmail);
        return userList.stream().findAny();
    }

    public int updateUsername(String email, String username) {
        String sql = "UPDATE user set username=?\n" +
                "WHERE email=?";
        int ret = jdbcTemplate.update(sql, username, email);
        if(ret == 0){
            throw new BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "존재하지 않는 이메일 입니다");
        }
        return ret;
    }

    public int updatePassword(String email, String password) {
        String sql = "UPDATE user set password=?\n" +
                "WHERE email=?";
        int ret = jdbcTemplate.update(sql, password, email);
        if(ret == 0){
            throw new BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "존재하지 않는 이메일 입니다");
        }
        return ret;
    }
}
