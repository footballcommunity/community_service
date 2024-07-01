package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.Sex;
import practice.communityservice.dto.request.PostMatchRequestDto;
import practice.communityservice.dto.response.GetMatchListResponseDto;
import practice.communityservice.dto.response.PostMatchResponseDto;
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
    public GetMatchListResponseDto getAllMatchList(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam Sex sex,
            @RequestParam MatchStatus matchStatus) {
        return matchService.getAllMatchList(startTime, endTime, sex, matchStatus);
    }

    @PostMapping("/new")
    public PostMatchResponseDto postMatch(@RequestBody PostMatchRequestDto postMatchRequestDto) {
        return matchService.postMatch(postMatchRequestDto);
    }
}
