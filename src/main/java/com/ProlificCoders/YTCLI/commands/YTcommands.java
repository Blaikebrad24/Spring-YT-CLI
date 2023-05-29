package com.ProlificCoders.YTCLI.commands;
//

import com.ProlificCoders.YTCLI.model.TeamTabRow;
import com.ProlificCoders.YTCLI.model.Video;
import com.ProlificCoders.YTCLI.service.CommandService;
import com.ProlificCoders.YTCLI.service.ReportService;
import com.ProlificCoders.YTCLI.service.VideoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.TableBuilder;

import java.time.LocalDateTime;
import java.util.List;

@ShellComponent("YouTube Stats Commands")
public class YTcommands {

    private final VideoService videoService;
    private final CommandService commandService;
    private final ReportService reportService;

    public YTcommands(VideoService videoService, CommandService commandService, ReportService reportService) {
        this.videoService = videoService;
        this.commandService = commandService;
        this.reportService = reportService;
    }

    @ShellMethod(key = "recent", value = "List recent videos by max count")
    public void recent(@ShellOption(defaultValue = "5") Integer max)
    {
      List<Video> videos = videoService.findRecent(max);
      TableBuilder tableBuilder = commandService.listToArrayTableModel(videos);
      System.out.println(tableBuilder.build().render(120));
    }

    @ShellMethod(key = "filter-by-year", value = "List videos by year")
    public void filterByYear(@ShellOption(defaultValue = "2023") Integer year)
    {
        List<Video> videos = videoService.findAllByYear(year);
        TableBuilder tableBuilder = commandService.listToArrayTableModel(videos);
        System.out.println(tableBuilder.build().render(120));
    }

    @ShellMethod(key = "report", value = "Run report by year")
    public void report()
    {
        List<Video> videos = videoService.findAllByYear(LocalDateTime.now().getYear());
        List<TeamTabRow> rows = reportService.videosToTeamTabRows(videos);
        rows.forEach(TeamTabRow::print);
    }
}
