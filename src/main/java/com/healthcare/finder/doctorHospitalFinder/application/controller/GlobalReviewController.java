package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.GlobalReviewRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.GlobalReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/globalReview")
public class GlobalReviewController {

    @Autowired
    private GlobalReviewService globalReviewService;

    @PostMapping("/register")
    public ResponseEntity<String> addGlobalReview(@RequestBody GlobalReviewRegisterDto globalReviewRegisterDto){
        return ResponseEntity.status(200).body(globalReviewService.addGlobalReview(globalReviewRegisterDto));
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Top10RattingCommentProjection>> getTop10Reviews(){
        return ResponseEntity.status(200).body(globalReviewService.getTop10RecentGlobalReviews());
    }

}
