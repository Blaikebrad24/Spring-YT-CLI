package com.ProlificCoders.YTCLI.model;

import java.util.List;

public record VideoListResponse(String kind, String etag, List<Video> items) {
}
