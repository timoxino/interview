package com.timoxino.interview.web.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CandidateExtractedSkillsMessage extends CandidateEstimatedMessage {
    private List<String> skills;
}
