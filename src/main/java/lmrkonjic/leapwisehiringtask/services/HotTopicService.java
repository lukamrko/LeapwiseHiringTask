package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

@Service
public class HotTopicService {
    //TODO actual implementation
    public List<MainNews> getMainNewsWithArticles(Dictionary<String, List<ArticleDTO>> rssArticlesForURLs) {
        List<MainNews> mainNews = new ArrayList<>();

        return  mainNews;
    }

    //TODO actual implementation
    public AnalysisResultDTO getHotTopicsForAnalyzedData(List<MainNews> analyzedData) {
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();

        return analysisResultDTO;
    }
}
