package com.ProlificCoders.YTCLI.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("youtube")
public record YoutubeConfigProps(String channelId, String key) {
}
