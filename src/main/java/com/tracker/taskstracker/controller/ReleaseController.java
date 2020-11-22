package com.tracker.taskstracker.controller;

import com.tracker.taskstracker.model.request.ReleaseRequestModel;
import com.tracker.taskstracker.model.response.ReleaseResponseModel;
import com.tracker.taskstracker.service.api.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/releases", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ReleaseController {

    private final ReleaseService releaseService;

    @Autowired
    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @PostMapping
    public ResponseEntity<ReleaseResponseModel> addRelease(@Valid @RequestBody ReleaseRequestModel releaseRequestModel) {
        ReleaseResponseModel releaseResponseModel = releaseService.save(releaseRequestModel);
        return ResponseEntity.ok(releaseResponseModel);
    }
}
