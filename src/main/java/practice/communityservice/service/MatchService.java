package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.model.Match;
import practice.communityservice.domain.model.Page;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.domain.validation.ValueNotZeroValidator;
import practice.communityservice.dto.PageDto;
import practice.communityservice.dto.response.GetMatchListResponseDto;
import practice.communityservice.dto.response.GetPageListResponseDto;
import practice.communityservice.repository.MatchRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public GetMatchListResponseDto getAllMatchList(int page, int pageSize, int blockSize, LocalDateTime currentTime) {
        // DB
        // 날짜를 선택해서 보여 주는 거로
        List<Match> matchList = matchRepository.getMatchList(page, pageSize, currentTime);
        int matchCount = matchRepository.allMatchCount();
        // Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new ValueNotZeroValidator(pageSize))
                .consistOf(new ValueNotZeroValidator(blockSize));
        validatorBucket.validate();
        // Logic
        int maxPage = (int) Math.ceil((double) matchCount / pageSize);
        int startPage = (((int)(Math.ceil((double) page / blockSize))) - 1) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        if(endPage  > maxPage){
            endPage = maxPage;
        }
        PageDto pageDto = new PageDto(matchCount, pageSize, blockSize, page);
        return GetMatchListResponseDto.builder()
                .page(pageDto)
                .matchList(matchList)
                .build();
    }
}
