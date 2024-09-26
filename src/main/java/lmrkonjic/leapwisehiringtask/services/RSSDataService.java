package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Service
public class RSSDataService {

    //TODO actual implementation
    public Dictionary<String, List<ArticleDTO>> fetchRSSArticlesForURLs(List<String> urls){
        Dictionary<String, List<ArticleDTO>> articlesForNewsSites = new Hashtable<String, List<ArticleDTO>>();

        return  articlesForNewsSites;
    }
}
