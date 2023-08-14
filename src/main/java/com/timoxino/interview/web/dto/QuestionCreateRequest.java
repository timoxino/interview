package com.timoxino.interview.web.dto;

import com.timoxino.interview.web.model.DataNode;

import lombok.Data;

@Data
public class QuestionCreateRequest implements QuestionDetailsAware {
    private DataNode dataNode;
    private String categoryUuid;
    private String complexityUuid;
}
