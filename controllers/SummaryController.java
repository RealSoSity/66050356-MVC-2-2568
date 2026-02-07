package controllers;

import java.util.List;

import models.Rumour;
import services.SummaryService;

public class SummaryController {
    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    public List<Rumour> panicRumours() {
        return summaryService.getPanicRumours();
    }

    public List<Rumour> verifiedRumours() {
        return summaryService.getVerifiedRumours();
    }
}
