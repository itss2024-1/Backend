package com.example.itss.controller;

import com.example.itss.domain.Resume;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.resume.ResResumeDto;
import com.example.itss.service.ResumeService;
import com.example.itss.util.error.ValidInforException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ResResumeDto>> createResume(@Valid @RequestBody Resume resume) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.createResume(resume));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<ResultPaginationDto>> fetchAllResume(
            @RequestParam(required = false) String userName,
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        Specification<Resume> combinedSpec = Specification.where(spec)
                .and(resumeService.withUserName(userName));
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.fetchAllResume(combinedSpec, pageable));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<ResResumeDto>> updateResume(@RequestBody Resume resume) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.updateResume(resume));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ResResumeDto>> fetchResumeById(@PathVariable long id) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.fetchResumeById(id));
    }
}
