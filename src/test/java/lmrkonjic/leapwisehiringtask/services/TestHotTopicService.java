package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestHotTopicService {
	@Test
	public void GivenMultipleArticles_WhenTwoArticlesFromDifferentSitesAreConnected_ThenShowThatInMainNews()
	{
		HotTopicService hotTopicService = new HotTopicService();
		
		List<ArticleDTO> rssArticles = createMockArticles();
		
		List<MainNews> mainNews= new ArrayList<>();
		
		try {
			mainNews = hotTopicService.getMainNewsWithArticles(rssArticles);
		} catch (IOException e) {
			fail("IOException occurred: " + e.getMessage());
		}
		
		// Assert that mainNews is not empty
		assertFalse(mainNews.isEmpty(), "mainNews should not be empty");
		
		// Find the MainNews object with 2 articles
		MainNews newsWithTwoArticles = mainNews.stream()
			.filter(news -> news.getArticles().size() == 2)
			.findFirst()
			.orElse(null);
		
		// Assert that we found a MainNews object with 2 articles
		assertNotNull(newsWithTwoArticles, "There should be a MainNews object with 2 articles");
		
		// Assert that the articles are from different RSS sites
		assertEquals(2, newsWithTwoArticles.getArticles().stream()
			.map(Article::getRssSiteURL)
			.distinct()
			.count(), "The two articles should be from different RSS sites");
		
		// Assert that one of the articles is about Burundi
		assertTrue(newsWithTwoArticles.getArticles().stream()
				.anyMatch(article -> article.getArticleTitle().contains("Burundi")),
			"One of the articles should be about Burundi");
	}
	
	private List<ArticleDTO> createMockArticles() {
		String rssSite1 = "www.site1.com";
		String rssSite2 = "www.site2.com";
		
		ArticleDTO articleDTO1 = new ArticleDTO();
		articleDTO1.setArticleURL(generateRandomString());
		articleDTO1.setArticleTitle("To Democrats, Donald Trump Is No Longer a Laughing Matter");
		articleDTO1.setRssSiteURL(rssSite1);
		
		ArticleDTO articleDTO2 = new ArticleDTO();
		articleDTO2.setArticleURL(generateRandomString());
		articleDTO2.setArticleTitle("Burundi military sites attacked, 12 insurgents killed");
		articleDTO2.setRssSiteURL(rssSite1);
		
		ArticleDTO articleDTO3 = new ArticleDTO();
		articleDTO3.setArticleURL(generateRandomString());
		articleDTO3.setArticleTitle("San Bernardino divers return to lake seeking electronic evidence");
		articleDTO3.setRssSiteURL(rssSite1);
		
		ArticleDTO articleDTO4 = new ArticleDTO();
		articleDTO4.setArticleURL(generateRandomString());
		articleDTO4.setArticleTitle("Attacks on Military Camps in Burundi Kill Eight");
		articleDTO4.setRssSiteURL(rssSite2);
		
		ArticleDTO articleDTO5 = new ArticleDTO();
		articleDTO5.setArticleURL(generateRandomString());
		articleDTO5.setArticleTitle("Saudi Women to Vote for First Time");
		articleDTO5.setRssSiteURL(rssSite2);
		
		ArticleDTO articleDTO6 = new ArticleDTO();
		articleDTO6.setArticleURL(generateRandomString());
		articleDTO6.setArticleTitle("Platini Dealt Further Blow in FIFA Presidency Bid");
		articleDTO6.setRssSiteURL(rssSite2);
		
		return List.of(articleDTO1, articleDTO2, articleDTO3, articleDTO4, articleDTO5, articleDTO6);
	}
	
	private String generateRandomString()
	{
		int stringLength = 40;
		
		return new Random().ints(97, 123)
			.limit(stringLength)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
}
