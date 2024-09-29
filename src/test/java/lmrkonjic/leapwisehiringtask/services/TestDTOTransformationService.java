package lmrkonjic.leapwisehiringtask.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestDTOTransformationService {
	
	@Autowired
	private DTOTransformationService dtoTransformationService;
	
	@Test
	public void testTransformHotMainNewsToAnalysisResultDTO() {
		// Arrange
		List<MainNews> hotMainNews = getMockMainNews();
		var sessionID = 1L; // Example sessionID
		
		// Act
		AnalysisResultDTO result = dtoTransformationService.transformHotMainNewsToAnalysisResultDTO(hotMainNews);
		
		// Assert
		assertNotNull(result);
		assertEquals(sessionID, result.getSessionID());
		assertNotNull(result.getHotTopics());
		assertEquals(2, result.getHotTopics().size()); // Assuming mock has 2 main news items
		
		MainNewsDTO firstMainNewsDTO = result.getHotTopics().getFirst();
		assertEquals("MainNewsTitle1", firstMainNewsDTO.getMainNewsTitle());
		assertEquals(1L, firstMainNewsDTO.getMainNewsID());
		assertEquals(2, firstMainNewsDTO.getArticles().size());
	}
	
	@Test
	public void testTransformMainNewsToMainNewsDTO() {
		// Arrange
		List<MainNews> mostTrendingNews = getMockMainNews();
		
		// Act
		List<MainNewsDTO> result = dtoTransformationService.transformMainNewsToMainNewsDTO(mostTrendingNews);
		
		// Assert
		assertNotNull(result);
		assertEquals(2, result.size()); // Assuming mock has 2 main news items
		assertEquals("MainNewsTitle1", result.getFirst().getMainNewsTitle());
		
		List<ArticleDTO> articles = result.getFirst().getArticles();
		assertEquals(2, articles.size());
		assertEquals("ArticleTitle1", articles.getFirst().getArticleTitle());
	}
	
	private List<MainNews> getMockMainNews() {
		MainNews mainNews1 = mock(MainNews.class);
		when(mainNews1.getMainNewsID()).thenReturn(1L);
		when(mainNews1.getMainNewsTitle()).thenReturn("MainNewsTitle1");
		List<Article> mockArticles = getMockArticles();
		when(mainNews1.getArticles()).thenReturn(mockArticles);
		
		MainNews mainNews2 = mock(MainNews.class);
		when(mainNews2.getMainNewsID()).thenReturn(2L);
		when(mainNews2.getMainNewsTitle()).thenReturn("MainNewsTitle2");
		when(mainNews2.getArticles()).thenReturn(mockArticles);
		
		Session mockSession = mock(Session.class);
		when(mockSession.getSessionID()).thenReturn(1L);
		when(mainNews1.getSession()).thenReturn(mockSession);
		when(mainNews2.getSession()).thenReturn(mockSession);
		
		return Arrays.asList(mainNews1, mainNews2);
	}
	
	private List<Article> getMockArticles() {
		Article article1 = mock(Article.class);
		when(article1.getArticleID()).thenReturn(1L);
		when(article1.getArticleTitle()).thenReturn("ArticleTitle1");
		when(article1.getArticleURL()).thenReturn("http://example.com/article1");
		when(article1.getRssSiteURL()).thenReturn("http://example.com/rss1");
		
		Article article2 = mock(Article.class);
		when(article2.getArticleID()).thenReturn(2L);
		when(article2.getArticleTitle()).thenReturn("ArticleTitle2");
		when(article2.getArticleURL()).thenReturn("http://example.com/article2");
		when(article2.getRssSiteURL()).thenReturn("http://example.com/rss2");
		
		return Arrays.asList(article1, article2);
	}
}
