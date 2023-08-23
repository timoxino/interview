package com.timoxino.interview.web.dto;

import com.google.common.collect.Multimap;
import com.timoxino.interview.web.model.DataNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateQuestionsMessage extends CandidateEstimatedMessage {
    private Multimap<String, DataNode> questions;
}
