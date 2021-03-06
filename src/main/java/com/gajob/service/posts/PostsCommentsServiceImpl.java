package com.gajob.service.posts;

import com.gajob.dto.posts.PostsCommentsDto;
import com.gajob.dto.posts.PostsCommentsResponseDto;
import com.gajob.entity.posts.Posts;
import com.gajob.entity.posts.PostsComments;
import com.gajob.entity.user.User;
import com.gajob.enumtype.ErrorCode;
import com.gajob.exception.CustomException;
import com.gajob.repository.posts.PostsCommentsRepository;
import com.gajob.repository.posts.PostsRepository;
import com.gajob.repository.user.UserRepository;
import com.gajob.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsCommentsServiceImpl implements PostsCommentsService {

  private final UserRepository userRepository;
  private final PostsRepository postsRepository;
  private final PostsCommentsRepository postsCommentsRepository;

  // 사용자가 등록한 댓글을 DB에 저장
  @Transactional
  public PostsCommentsResponseDto save(Long postId, PostsCommentsDto postsCommentsDto) {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();
    Posts posts = postsRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXIST));

    return new PostsCommentsResponseDto(postsCommentsRepository.save(
        postsCommentsDto.toEntity(user, posts)));
  }

  // 게시물 별로 댓글 조회
  @Transactional(readOnly = true)
  public List<PostsCommentsResponseDto> getPostsComments(Long postId) {
    Posts posts = postsRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXIST));

    // postsCommentsRepository로 결과로 넘어온 PostsComments의 Stream을 map을 통해 PostsCommentsResponseDto로 변환
    return postsCommentsRepository.findAllByPostsId(postId).stream()
        .map(PostsCommentsResponseDto::new)
        .collect(
            Collectors.toList());
  }

  // 댓글 수정
  @Transactional
  public PostsCommentsResponseDto update(Long postId, Long commentId,
      PostsCommentsDto postsCommentsDto) {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();

    Posts posts = postsRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_ID_NOT_EXIST));

    PostsComments postsComments = postsCommentsRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_ID_NOT_EXIST));

    // 현재 로그인한 유저와 댓글 작성자의 이메일이 일치하지 않을 경우, 에러 발생
    if (!(postsComments.getUser().getEmail().equals(user.getEmail()))) {
      throw new CustomException(ErrorCode.NO_ACCESS_RIGHTS);
    }

    postsComments.update(postsCommentsDto.getComment(), postsCommentsDto.getIsSecret());

    PostsCommentsResponseDto postsCommentsResponseDto = new PostsCommentsResponseDto(postsComments);

    return postsCommentsResponseDto;
  }

  // 댓글 삭제
  @Transactional
  public String delete(Long commentId) {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();

    PostsComments postsComments = postsCommentsRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_ID_NOT_EXIST));

    // 현재 로그인한 유저와 댓글 작성자의 이메일이 일치하지 않을 경우, 에러 발생
    if (!(postsComments.getUser().getEmail().equals(user.getEmail()))) {
      throw new CustomException(ErrorCode.NO_ACCESS_RIGHTS);
    }

    postsCommentsRepository.delete(postsComments);

    return "comments-delete";
  }


}
