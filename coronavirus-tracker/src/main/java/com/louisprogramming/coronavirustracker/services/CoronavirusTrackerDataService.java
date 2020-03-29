package com.louisprogramming.coronavirustracker.services;

import com.louisprogramming.coronavirustracker.models.Stats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronavirusTrackerDataService {

    // It is not a very good practice to have state inside another spring serivce, but this is just a simulation, so I would leave this as is.
    private List<Stats> stats = new ArrayList<>();

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    private static String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    @Scheduled(cron="* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException{
        // Having an another array list here is to address the concurrency issue.
        // When the users access this application, I don't want to give them an error response while the array list is getting built.
        // So after I am done constructing the newStats, stats will get populated with newStats.
        List<Stats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader reader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        for(CSVRecord record : records) {
            String country = record.get("Country/Region");
            String state = record.get("Province/State");
            int latestConfirmedCases = Integer.parseInt(record.get(record.size()-1));
            int prevConfirmedCases = Integer.parseInt(record.get(record.size()-2));

            Stats stat = new Stats();
            stat.setCountry(country);
            stat.setState(state);
            stat.setLatestConfirmedCases(latestConfirmedCases);
            stat.setDiffFromPreviousDay(latestConfirmedCases - prevConfirmedCases);
            newStats.add(stat);
        }

        this.stats = newStats;
    }
}
