package lmrkonjic.leapwisehiringtask.controllers;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.services.RSSNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rss")
public class RSSNewsController {

    @Autowired
    private RSSNewsService rssNewsService;

    @PostMapping("/analyse/new")
    public ResponseEntity<Long> analyzeRSSNews(@RequestParam String queryURL) {
        Long sessionID = rssNewsService.analyzeRSSNews(queryURL);
        return ResponseEntity.ok(sessionID);
    }

    @GetMapping("/frequency/{id}")
    public ResponseEntity<List<MainNews>> fetchThreeMostTrendingNews(@PathVariable Long id) {
        List<MainNews> trendingNews = rssNewsService.fetchThreeMostTrendingNewsForSessionID(id);
        return ResponseEntity.ok(trendingNews);
    }
}
