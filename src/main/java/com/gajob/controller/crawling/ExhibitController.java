package com.gajob.controller.crawling;

import com.gajob.dto.crawling.ExhibitDto;
import com.gajob.entity.crawling.Exhibit;
import com.gajob.service.crawling.ExhibitCrawling;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/issue")
public class ExhibitController {

  private final ExhibitCrawling exhibitCrawling;

  @GetMapping("/exhibit")
  public List<Exhibit> getFairData(String url) throws IOException {
    return exhibitCrawling.getExhibitData(exhibitCrawling.getExhibitUrl(1));
  }

  @PostMapping("/exhibit") // 크롤링한 데이터 DB 저장
  public ResponseEntity<ExhibitDto> fairSave() throws Exception {
    return ResponseEntity.ok(
        exhibitCrawling.exhibitSave(new ExhibitDto(Exhibit.builder().build())));
  }
}
