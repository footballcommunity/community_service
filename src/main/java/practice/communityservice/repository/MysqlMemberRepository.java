package practice.communityservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import practice.communityservice.domain.Member;
import practice.communityservice.dto.SignupRequestDto;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MysqlMemberRepository implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MysqlMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        objectMapper = new ObjectMapper();

    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");
        Map<String, Object> params = objectMapper.convertValue(member, Map.class);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        member.setId(key.longValue());
        return member;
    }
}
