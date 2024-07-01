package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.model.Match;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.Sex;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.domain.validation.ValueNotZeroValidator;
import practice.communityservice.dto.PageDto;
import practice.communityservice.dto.request.PostMatchRequestDto;
import practice.communityservice.dto.response.GetMatchListResponseDto;
import practice.communityservice.dto.response.PostMatchResponseDto;
import practice.communityservice.repository.MatchRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public GetMatchListResponseDto getAllMatchList(LocalDateTime startTime, LocalDateTime endTime, Sex sex, MatchStatus matchStatus) {
        // DB
        // 날짜를 선택해서 보여 주는 거로
        // 성별, status 필터링 안하는 경우
        List<Match> matchList;
        if (sex == Sex.ALL && matchStatus == MatchStatus.ALL) {
            matchList = matchRepository.getMatchList(startTime, endTime);
        } else if (sex == Sex.ALL && matchStatus != MatchStatus.ALL) {
            matchList = matchRepository.getMatchList(startTime, endTime, matchStatus);
        } else if (sex != Sex.ALL && matchStatus == MatchStatus.ALL) {
            matchList = matchRepository.getMatchList(startTime, endTime, sex);
        } else {
            matchList = matchRepository.getMatchList(startTime, endTime, matchStatus, sex);
        }

        return GetMatchListResponseDto.builder()
                .matchList(matchList)
                .build();
    }

    public PostMatchResponseDto postMatch(PostMatchRequestDto postMatchRequestDto) {
        Match newMatch = Match.from(postMatchRequestDto);
        Long matchId = matchRepository.save(newMatch);
        return new PostMatchResponseDto(matchId);
    }
}
