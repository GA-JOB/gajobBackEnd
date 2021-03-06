package com.gajob.repository.study;

import com.gajob.entity.study.Study;
import com.gajob.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudyRepository extends JpaRepository<Study, Long> {

  @Modifying
  @Query("update Study s set s.view=s.view+1 where s.id = :id")
  int updateView(Long id);

  @Query("select s from Study s")
  List<Study> findAll();

  List<Study> findAllByUser(User user);

  Optional<Study> deleteAllByUser(User user);

}
