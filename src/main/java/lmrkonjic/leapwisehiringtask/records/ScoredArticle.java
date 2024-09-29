package lmrkonjic.leapwisehiringtask.records;

import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;

public record ScoredArticle(ArticleDTO article, float score) {
}