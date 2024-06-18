package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.communityservice.service.MatchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@Slf4j
public class MatchController {

    private final MatchService matchService;
}
