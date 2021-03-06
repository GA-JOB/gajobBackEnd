package com.gajob.dto.posts;

import com.gajob.entity.posts.Posts;
import com.gajob.entity.posts.PostsComments;
import com.gajob.entity.user.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsCommentsDto {

  private Long id;
  private String comment;

  private Boolean isSecret;

  // 년, 월, 일, 시, 분까지 나오게 포맷
  private String createdDate = LocalDateTime.now().format(
      DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
  private String modifiedDate = LocalDateTime.now()
      .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

  private User user;
  private Posts posts;

  public PostsComments toEntity(User user, Posts posts) {
    PostsComments postsComments = PostsComments.builder().id(id).comment(comment).isSecret(isSecret)
        .createdDate(createdDate)
        .modifiedDate(modifiedDate).user(user).posts(posts).build();
    return postsComments;
  }

}
