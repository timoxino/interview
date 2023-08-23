package com.timoxino.interview.web.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateExtractedSkillsMessage extends CandidateEstimatedMessage {
    private List<String> skills;
}
