package controllers;

import services.RumourService;
import models.Rumour;
import models.enums.VerificationStatus;

import java.util.List;

public class RumourController { // Controller for handling rumour-related requests
    private final RumourService rumourService;

    public RumourController(RumourService rumourService) {
        this.rumourService = rumourService;
    }

    public List<Rumour> listRumours() {
        return rumourService.listRumoursSortedByReportCountDesc();
    }

    public Rumour getRumourDetails(String rumourId){
        return rumourService.getRumour(rumourId);
    }

    public int getReportCount(String rumourId){
        return rumourService.getReportCount(rumourId);
    }

    public String verify(String checkerId, String rumourId, VerificationStatus status){
        try {
            rumourService.verifyRumour(checkerId, rumourId, status);
            return "Verified: " + rumourId + " as " + status;
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}
