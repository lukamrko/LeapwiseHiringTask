package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.exceptions.RssContentFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestRSSDataService {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private RSSDataService rssDataService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void fetchRSSArticlesForURLs_ShouldReturnArticlesForValidURLs() {
		// Arrange
		String validUrl = "https://example.com/rss";
		String xmlContent = "<rss><channel><item><title>Article 1</title><link>https://example.com/article1</link></item></channel></rss>";
		when(restTemplate.getForObject(validUrl, String.class)).thenReturn(xmlContent);
		
		SyndFeed syndFeed = mock(SyndFeed.class);
		SyndEntry syndEntry = mock(SyndEntry.class);
		when(syndEntry.getTitle()).thenReturn("Article 1");
		when(syndEntry.getLink()).thenReturn("https://example.com/article1");
		
		List<SyndEntry> syndEntries = new ArrayList<>();
		syndEntries.add(syndEntry);
		when(syndFeed.getEntries()).thenReturn(syndEntries);
		
		// Act
		List<String> urls = List.of(validUrl);
		List<ArticleDTO> result = rssDataService.fetchRSSArticlesForURLs(urls);
		
		// Assert
		assertEquals(1, result.size());
		assertEquals("Article 1", result.getFirst().getArticleTitle());
		assertEquals("https://example.com/article1", result.getFirst().getArticleURL());
		assertEquals(validUrl, result.getFirst().getRssSiteURL());
		
		verify(restTemplate, times(1)).getForObject(validUrl, String.class);
	}
	
	@Test
	public void fetchRSSArticlesForURLs_ShouldThrowExceptionWhenXMLContentIsNull() {
		// Arrange
		String invalidUrl = "https://invalid-url.com/rss";
		when(restTemplate.getForObject(invalidUrl, String.class)).thenReturn(null);
		
		// Act & Assert
		List<String> urls = List.of(invalidUrl);
		assertThrows(RssContentFetchException.class, () -> {
			rssDataService.fetchRSSArticlesForURLs(urls);
		});
		
		verify(restTemplate, times(1)).getForObject(invalidUrl, String.class);
	}
	
	@Test
	public void fetchRSSArticlesForURLs_ShouldThrowExceptionWhenExceptionOccurs() {
		// Arrange
		String invalidUrl = "https://invalid-url.com/rss";
		when(restTemplate.getForObject(invalidUrl, String.class)).thenThrow(new RuntimeException("Error fetching content"));
		
		// Act & Assert
		List<String> urls = List.of(invalidUrl);
		assertThrows(RssContentFetchException.class, () -> {
			rssDataService.fetchRSSArticlesForURLs(urls);
		});
		
		verify(restTemplate, times(1)).getForObject(invalidUrl, String.class);
	}
	
	@Test
	public void fetchRSSArticlesForURLs_ShouldReturnEmptyListWhenNoArticlesPresent() {
		// Arrange
		String validUrl = "https://example.com/rss";
		String xmlContent = "<rss><channel></channel></rss>"; // No items
		when(restTemplate.getForObject(validUrl, String.class)).thenReturn(xmlContent);
		
		SyndFeed syndFeed = mock(SyndFeed.class);
		when(syndFeed.getEntries()).thenReturn(new ArrayList<>());
		
		// Act
		List<String> urls = List.of(validUrl);
		List<ArticleDTO> result = rssDataService.fetchRSSArticlesForURLs(urls);
		
		// Assert
		assertTrue(result.isEmpty());
		verify(restTemplate, times(1)).getForObject(validUrl, String.class);
	}
}
