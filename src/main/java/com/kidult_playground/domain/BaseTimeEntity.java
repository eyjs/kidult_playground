package com.kidult_playground.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // ①
@EntityListeners(AuditingEntityListener.class)  // ②
public class BaseTimeEntity {
    @CreatedDate    // ③
    private LocalDateTime createdDate;

    @LastModifiedDate   // ④
    private LocalDateTime modifiedDate;
}

/*
  ① @MappedSuperclass
    - JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들(createdDate, modifiedDate)도 컬럼으로 인식
  ② @EntityListeners(AuditingEntityListener.class)
    - BaseTimeEntity 클래스에 Auditing 기능을 포함시킴.
  ③ @CreatedDate
    - Entity가 생성되어 저장될 때 시간이 자동 저장됨.
  ④ @LastModifiedDate
    - 조회한 Entity의 값을 변경할 때 시간이 자동 저장됨.

  의문점
    - 최종 수정 시간만 확인 시 중간이력들 관리 ??
      -> maybe 기록들 Log로 Out해서 외부에서 관리추정.
 */