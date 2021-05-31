package com.kidult_playground.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {

}

/*
 ● JpaRepository
   - JpaRepository<Entity 클래스, PK 타입>상속 시 자동으로 CRUD 메소드 생성.
   - @Repository 어노테이션 추가할 필요 없음.
 ● 주의점
   - Entity 클래스와 Entity Repository는 함께 위치해야 함. (동일 Depth)
   - Entity 클래스는 Entity Repository가 없을 시 제대로 작동하지 못함.
   - 추 후 프로젝트 규모가 커져 도메인 별로 프로젝트 분리 시 Entity 클래스와 Entity Repository는 함께 움직여야 함.
     -> 도메인 패키지에서 함께 관리.
 */


