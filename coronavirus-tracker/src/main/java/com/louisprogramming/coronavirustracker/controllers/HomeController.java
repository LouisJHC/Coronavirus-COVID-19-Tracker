package com.louisprogramming.coronavirustracker.controllers;

import com.louisprogramming.coronavirustracker.models.Stats;
import com.louisprogramming.coronavirustracker.services.CoronavirusTrackerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronavirusTrackerDataService dataService;

    @GetMapping("/")
    public String Home(Model model) {
        List<Stats> stats = dataService.getStats();
        int totalCases = 0;
        for(Stats stat : stats) {
            totalCases += stat.getLatestConfirmedCases();
        }

        int totalNewCases = 0;

        for(Stats stat : stats) {
            totalNewCases += stat.getDiffFromPreviousDay();
        }
        model.addAttribute("stats", stats);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
