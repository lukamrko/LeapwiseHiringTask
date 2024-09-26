package lmrkonjic.leapwisehiringtask.controllers;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.services.RSSNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rss")
public class RSSNewsController {

    private final RSSNewsService rssNewsService;

    public RSSNewsController(RSSNewsService rssNewsService) {
        this.rssNewsService = rssNewsService;
    }

    @PostMapping("/analyse/new")
    public ResponseEntity<AnalysisResultDTO> analyzeRSSNews(@RequestParam String queryURL) {
        AnalysisResultDTO analysisResultDTO = rssNewsService.analyzeRSSNews(queryURL);
        return ResponseEntity.ok(analysisResultDTO);
    }

    @GetMapping("/frequency/{id}")
    public ResponseEntity<List<MainNews>> fetchMostTrendingNews(@PathVariable Long id) {
        List<MainNews> trendingNews = rssNewsService.fetchMostTrendingNewsForSessionID(id);
        return ResponseEntity.ok(trendingNews);
    }
}
