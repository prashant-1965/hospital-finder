package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.GlobalReviewException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.GlobalReviewRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.entity.GlobalReview;
import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.repository.GlobalReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GlobalReviewServiceImpl implements GlobalReviewService {

    @Autowired
    private GlobalReviewRepo globalReviewRepo;
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public List<Top10RattingCommentProjection> getTop10RecentGlobalReviews() throws GlobalReviewException {
        List<Top10RattingCommentProjection> getRecentReviewList =  globalReviewRepo.findTop10RecentGlobalReviews(PageRequest.of(0,10));
        if(getRecentReviewList.isEmpty()){
            throw new GlobalReviewException("No review found", HttpStatus.NOT_FOUND);
        }
        return getRecentReviewList;
    }

    @Override
    public String addGlobalReview(GlobalReviewRegisterDto globalReviewRegisterDto) throws GlobalReviewException {

        Optional<AppUser> appUser = appUserRepo.findByUserEmail(globalReviewRegisterDto.getUserEmail());
        if(appUser.isEmpty()){
            throw new GlobalReviewException("Please register your "+globalReviewRegisterDto.getUserEmail()+"!",HttpStatus.BAD_REQUEST);
        }
        List<String> commentList = globalReviewRepo.getCommentByUserEmail(globalReviewRegisterDto.getUserEmail());
        for(String comments:commentList){
            if(comments.equalsIgnoreCase(globalReviewRegisterDto.getComments())){
                throw new GlobalReviewException(globalReviewRegisterDto.getComments()+" Already added!",HttpStatus.BAD_REQUEST);
            }
        }
        GlobalReview globalReview = new GlobalReview();
        globalReview.setComments(globalReviewRegisterDto.getComments());
        globalReview.setRating(globalReviewRegisterDto.getRating());
        globalReview.setCreatedAt(LocalDateTime.now());
        globalReview.setUser(appUser.get());
        globalReviewRepo.save(globalReview);
        return "Thanks for you feedback!";
    }
}
