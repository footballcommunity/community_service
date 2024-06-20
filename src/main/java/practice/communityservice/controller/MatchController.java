package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practice.communityservice.dto.response.GetMatchListResponseDto;
import practice.communityservice.service.MatchService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping()
    public GetMatchListResponseDto getAllMatchList(@RequestParam(required = false, defaultValue = "1") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                   @RequestParam(required = false, defaultValue = "5") int blockSize,
                                                   @RequestParam LocalDateTime currentTime){
        return matchService.getAllMatchList(page, pageSize, blockSize, currentTime);
    }
}
