package com.timoxino.interview.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionComplexityPatchRequest {
    private String uuid;
    private List<QuestionComplexityPatch> patch;
}
