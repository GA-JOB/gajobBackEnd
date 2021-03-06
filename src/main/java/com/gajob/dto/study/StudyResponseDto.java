package com.gajob.dto.study;

import com.gajob.entity.study.Study;
import com.gajob.enumtype.Area;
import com.gajob.enumtype.Status;
import com.gajob.enumtype.StudyCategory;
import java.time.LocalDate;
import lombok.Data;

@Data
public class StudyResponseDto {

  private Long id;
  private String title;
  private String content;
  private StudyCategory studyCategory;
  private Area area;
  private int maxPeople;
  private int minPeople;
  private LocalDate startDate, endDate;
  private Status status;
  private String writer;
  private int view;
  private String createdDate, modifiedDate;
  private String openTalkUrl;

  public StudyResponseDto(Study study) {
    this.id = study.getId();
    this.title = study.getTitle();
    this.content = study.getContent();
    this.studyCategory = study.getStudyCategory();
    this.area = study.getArea();
    this.maxPeople = study.getMaxPeople();
    this.minPeople = study.getMinPeople();
    this.startDate = study.getStartDate();
    this.endDate = study.getEndDate();
    this.status = study.getStatus();
    this.writer = study.getUser().getNickname();
    this.view = study.getView();
    this.createdDate = study.getCreatedDate();
    this.modifiedDate = study.getModifiedDate();
    this.openTalkUrl = study.getOpenTalkUrl();
  }

}
