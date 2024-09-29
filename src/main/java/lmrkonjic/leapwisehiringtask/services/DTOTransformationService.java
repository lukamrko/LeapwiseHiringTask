package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DTOTransformationService {
	public AnalysisResultDTO transformHotMainNewsToAnalysisResultDTO(List<MainNews> hotMainNews) {
		var analysisResultDTO = new AnalysisResultDTO();
		var sessionID = hotMainNews.getFirst().getSession().getSessionID();
		
		var mainNewsDTO = transformMainNewsToMainNewsDTO(hotMainNews);
		
		analysisResultDTO.setSessionID(sessionID);
		analysisResultDTO.setHotTopics(mainNewsDTO);
		
		return analysisResultDTO;
	}
	
	public List<MainNewsDTO> transformMainNewsToMainNewsDTO(List<MainNews> mostTrendingNews) {
		List<MainNewsDTO> mainNewsDTOs = new ArrayList<>();
		for (var mainNews : mostTrendingNews) {
			var mainNewsDTO = new MainNewsDTO();
			mainNewsDTO.setMainNewsID(mainNews.getMainNewsID());
			mainNewsDTO.setMainNewsTitle(mainNews.getMainNewsTitle());
			
			List<ArticleDTO> articleDTOs = transformArticlesToArticleDTOs(mainNews.getArticles());
			mainNewsDTO.setArticles(articleDTOs);
			
			mainNewsDTOs.add(mainNewsDTO);
		}
		
		return mainNewsDTOs;
	}
	
	private List<ArticleDTO> transformArticlesToArticleDTOs(List<Article> articles) {
		List<ArticleDTO> articleDTOs = new ArrayList<>();
		for (var article : articles) {
			var articleDTO = new ArticleDTO();
			articleDTO.setArticleID(article.getArticleID());
			articleDTO.setArticleTitle(article.getArticleTitle());
			articleDTO.setArticleURL(article.getArticleURL());
			articleDTO.setRssSiteURL(article.getRssSiteURL());
			
			articleDTOs.add(articleDTO);
		}
		
		return articleDTOs;
	}
}
