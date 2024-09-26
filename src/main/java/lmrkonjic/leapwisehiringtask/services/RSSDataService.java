package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class RSSDataService {

    public Dictionary<String, List<ArticleDTO>> fetchRSSArticlesForURLs(List<String> urls){
        Dictionary<String, List<ArticleDTO>> articlesForNewsSites = new Hashtable<String, List<ArticleDTO>>();

        return  articlesForNewsSites;
    }
}
