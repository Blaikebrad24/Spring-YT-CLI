package com.ProlificCoders.YTCLI;

import com.ProlificCoders.YTCLI.config.YoutubeConfigProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(YoutubeConfigProps.class)
public class YtCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtCliApplication.class, args);
	}

}
