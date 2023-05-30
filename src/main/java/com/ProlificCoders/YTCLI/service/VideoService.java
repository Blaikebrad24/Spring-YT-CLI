package com.ProlificCoders.YTCLI.service;

import com.ProlificCoders.YTCLI.client.YTDataClient;
import com.ProlificCoders.YTCLI.config.YoutubeConfigProps;
import com.ProlificCoders.YTCLI.model.SearchListResponse;
import com.ProlificCoders.YTCLI.model.SearchResult;
import com.ProlificCoders.YTCLI.model.Video;
import com.ProlificCoders.YTCLI.model.VideoListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/*
* - Class: VideoService
* -Purpose: Retrieving the videos from YT API
* - Attributes: Logger,List<ofTypeVideo>, YoutubeConfigProps and YTDataClient instance
* - Methods: findAll, findRecent, findAllByYear,
*            loadAllVideos, loadAllVideosByYear
 */
@Service
public class VideoService {

    private static final Logger log = LoggerFactory.getLogger(VideoService.class);
    private final List<Video> videos = new ArrayList<>();
    private final YoutubeConfigProps youtubeConfigProps;
    private final YTDataClient client;

    /*
     * Method: (Constructor)
     * Functionality: Loads all videos from
     *                client instance by youtubeConfigProps
     */
    public VideoService(YoutubeConfigProps youtubeConfigProps, YTDataClient client) {
        this.youtubeConfigProps = youtubeConfigProps;
        this.client = client;
        //make call to YT data client

        this.loadAllVideosThisYear("");
        log.info("Loaded {} videos", videos.size());
    }

    public List<Video> findAll()
    {
        return videos;
    }

    public List<Video> findRecent(Integer max)
    {
        return videos.stream().limit(max).toList();
    }

    public List<Video> findAllByYear(Integer year){
        return videos.stream().filter(v -> v.snippet().publishedAt().getYear() == year).toList();
    }

    /**
     * The YouTube search API only returns 50 results per page. This method recursively calls itself until there are no more pages.
     * @param pageToken The page token to use for the next page.
     */
    public void loadAllVideos(String pageToken)
    {
        SearchListResponse search = client.search(youtubeConfigProps.channelId(), youtubeConfigProps.key(), 50, pageToken);
        for(SearchResult result: search.items())
        {
            if(result.id().kind().equals("youtube#video"))
            {
                VideoListResponse videoListResponse = client.getVideo(result.id().videoId(), youtubeConfigProps.key());
                videos.addAll(videoListResponse.items());
            }
        }
        if(search.nextPageToken() != null && !search.nextPageToken().isEmpty())
        {
            loadAllVideos(search.nextPageToken());
        }
    }

    public void loadAllVideosThisYear(String pageToken)
    {
        SearchListResponse searchResponse = client.searchByPublishedAfter(youtubeConfigProps.channelId(), youtubeConfigProps.key(), 50, pageToken, "2023-01-01T00:00:00Z");
        for(SearchResult result: searchResponse.items())
        {
            if(result.id().kind().equals("youtube#video"))
            {
                VideoListResponse videoListResponse = client.getVideo(result.id().videoId(), youtubeConfigProps.key());
                videos.addAll(videoListResponse.items());
            }
        }
        if(searchResponse.nextPageToken() != null && !searchResponse.nextPageToken().isEmpty())
        {
            loadAllVideos(searchResponse.nextPageToken());
        }
    }
}
