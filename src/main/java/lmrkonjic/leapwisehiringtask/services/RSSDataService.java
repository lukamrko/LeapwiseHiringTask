package lmrkonjic.leapwisehiringtask.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Service
public class RSSDataService {
    private final RestTemplate restTemplate;

    public RSSDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //TODO actual implementation
    public Dictionary<String, List<ArticleDTO>> fetchRSSArticlesForURLs(List<String> urls){
        Dictionary<String, List<ArticleDTO>> articlesForNewsSites = new Hashtable<String, List<ArticleDTO>>();
        for (String url : urls) {
            try {
                String xmlContent = restTemplate.getForObject(url, String.class);
                SyndFeedInput input = new SyndFeedInput();
                assert xmlContent != null;
                SyndFeed feed = input.build(new XmlReader(new ByteArrayInputStream(xmlContent.getBytes())));

                List<ArticleDTO> articles = new ArrayList<>();
                for (SyndEntry entry : feed.getEntries()) {
                    ArticleDTO article = new ArticleDTO();
                    article.setArticleTitle(entry.getTitle());
                    article.setArticleURL(entry.getLink());
                    article.setRssSiteURL(url);
                    articles.add(article);
                }
                articlesForNewsSites.put(url, articles);
            } catch (Exception e) {
                System.err.println(STR."Error processing URL: \{url}. Error: \{e.getMessage()}");
            }
        }

        return articlesForNewsSites;
    }
}
