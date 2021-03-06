package com.gajob.controller.crawling;

import com.gajob.dto.crawling.NewsDto;
import com.gajob.entity.crawling.News;
import com.gajob.service.crawling.NewsCrawling;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/issue")
public class NewsController {

  private final NewsCrawling newsCrawling;

  @GetMapping("/news") // 크롤링한 뉴스 데이터 가져오기
  public ResponseEntity<NewsDto> getNews() {
    return new ResponseEntity(newsCrawling.getNews(), HttpStatus.OK);
  }

  @PostMapping("/news") // 크롤링한 뉴스 데이터 DB 저장
  public ResponseEntity<NewsDto> saveNews() throws Exception {
    return ResponseEntity.ok(newsCrawling.saveNews(new NewsDto(News.builder().build())));
  }

}
