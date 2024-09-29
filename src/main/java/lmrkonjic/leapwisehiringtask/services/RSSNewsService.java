package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisRequestDTO;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;
import lmrkonjic.leapwisehiringtask.exceptions.ArticleGroupingException;
import lmrkonjic.leapwisehiringtask.exceptions.InvalidAPIParametersException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSSNewsService {
	@Value("${analyze.min-rss-sites}")
	private int minRssSites;

	private final DatabaseService databaseService;
	private final HotTopicService hotTopicService;
	private final RSSDataService rssDataService;
	private final DTOTransformationService dtoTransformationService;
	
	public RSSNewsService(
		DatabaseService databaseService,
		HotTopicService hotTopicService,
		RSSDataService rssDataService,
		DTOTransformationService dtoTransformationService) {
		this.databaseService = databaseService;
		this.hotTopicService = hotTopicService;
		this.rssDataService = rssDataService;
		this.dtoTransformationService = dtoTransformationService;
	}
	
	public AnalysisResultDTO analyzeRSSNews(AnalysisRequestDTO requestDTO) {
		List<String> rssUrls = requestDTO.getRssUrls();
		if (rssUrls == null || rssUrls.size() < minRssSites) {
			throw new InvalidAPIParametersException("The number of parameters you entered is invalid!");
		}
		
		List<ArticleDTO> rssArticlesForURLs = rssDataService.fetchRSSArticlesForURLs(rssUrls);
		List<MainNews> analyzedData;
		try {
			analyzedData = hotTopicService.getMainNewsWithArticles(rssArticlesForURLs);
		} catch (Exception e) {
			throw new ArticleGroupingException("An error occurred while grouping articles. Please try with other parameters!");
		}
		
		databaseService.saveSessionWithData(analyzedData);
		
		List<MainNews> hotMainNews = hotTopicService.getHotMainNews(analyzedData);
		return dtoTransformationService.transformHotMainNewsToAnalysisResultDTO(hotMainNews);
	}
	
	public List<MainNewsDTO> fetchMostTrendingNewsForSessionID(Long sessionID) {
		List<MainNews> mostTrendingNews = databaseService.fetchMostTrendingNewsWithArticlesForSessionID(sessionID);
		return dtoTransformationService.transformMainNewsToMainNewsDTO(mostTrendingNews);
	}
}
