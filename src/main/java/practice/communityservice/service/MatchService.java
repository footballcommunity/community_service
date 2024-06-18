package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.repository.MatchRepository;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
}
