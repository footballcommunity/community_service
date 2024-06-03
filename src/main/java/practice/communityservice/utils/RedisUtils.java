package practice.communityservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final long refreshTokenExpTime;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate,
                      @Value("${jwt.refresh_token_expiration_time}") long refreshTokenExpTime) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public void setData(String key, String value,Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "BLACKLISTED", refreshTokenExpTime, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return "BLACKLISTED".equals(redisTemplate.opsForValue().get(token));
    }
}
