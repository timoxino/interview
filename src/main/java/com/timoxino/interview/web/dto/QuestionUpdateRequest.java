package com.timoxino.interview.web.dto;

import com.timoxino.interview.web.model.DataNode;

import lombok.Data;

@Data
public class QuestionUpdateRequest {
    private DataNode dataNode;
    private String categoryUuid;
    private String complexityUuid;
}
