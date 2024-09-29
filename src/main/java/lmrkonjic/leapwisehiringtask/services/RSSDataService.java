package lmrkonjic.leapwisehiringtask.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.exceptions.RssContentFetchException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class RSSDataService {
    private final RestTemplate restTemplate;

    public RSSDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ArticleDTO> fetchRSSArticlesForURLs(List<String> urls){
        List<ArticleDTO> articles = new ArrayList<>();
        for (String url : urls) {
            try {
                String xmlContent = restTemplate.getForObject(url, String.class);
                SyndFeedInput input = new SyndFeedInput();
                if (xmlContent == null) {
                    throw new RssContentFetchException("Fetched content of url is null: " + url);
                }
                SyndFeed feed = input.build(new XmlReader(new ByteArrayInputStream(xmlContent.getBytes())));

                for (SyndEntry entry : feed.getEntries()) {
                    ArticleDTO article = new ArticleDTO();
                    article.setArticleTitle(entry.getTitle());
                    article.setArticleURL(entry.getLink());
                    article.setRssSiteURL(url);
                    articles.add(article);
                }
            } catch (Exception e) {
                throw new RssContentFetchException("Error processing URL: " + url + ". Error: " + e.getMessage());
            }
        }
        return  articles;

    }
}
