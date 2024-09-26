package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class RSSDataService {

    public Dictionary<String, List<MainNewsDTO>> fetchRSSDataForURLs(List<String> urls){
        Dictionary<String, List<MainNewsDTO>> articlesForNewsSites = new Hashtable<String, List<MainNewsDTO>>();

        return  articlesForNewsSites;
    }
}
