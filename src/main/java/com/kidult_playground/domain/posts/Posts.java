package com.kidult_playground.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import javax.persistence.*;

@Getter // ⑥
@NoArgsConstructor // ⑤
@Entity // ①
public class Posts {

    @Id // ②
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ③
    private Long id;

    @Column(length = 500, nullable = false) // ④
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // ⑦
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
/*
 ① @Entity
   - 테이블과 링크될 클래스 명시 어노테이션
   - 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭함.
   ex) PostsUpload -> posts_upload table
 ② @Id
   - 해당 테이블의 PK 필드를 나타냄
 ③ @GeneratedValue
   - PK의 생성 규칙을 나타냄
   - 스프링 부트 2.0 에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 됨
 ④ @Column
   - 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 됨.
   - 사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용
   - 문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 늘리고 싶거나, 타입을 TEXT로 변경하고 싶거나 등의 경우에 사용
 ⑤ @NoArgsConstructor
   - 기본 생성자 자동 추가
   - public Posts() {}와 같은 효과
 ⑥ @Getter
   - 클래스 내 모든 필드의 Getter 메소드를 자동생성
 ⑦ @Builder
   - 해당 클래스의 빌더 패턴 클래스를 생성
   ex)Member customer = Member.build()
                            .name("홍길동")
                            .age(30)
                            .build();
   - 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함.
 */
/*
 주의사항
  - Entity 객체는 Setter를 생성하지 않음.
   -> 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분이 불가능
   -> 차후 기능 변경 시 복잡성 증가.
  - 필드의 값 변경이 필요할 경우 메소드 추가하는 형식으로 처리.
  ex) public class Posts {
    @AutoWried
    private bool status;

    public void 상태값변경(){
      posts.chageStatus();
    }
  }
  기본적으로 생성자를 통한 값 변경을 처리하지만 빌더(Builder)를 사용하여 처리.
    -> 생성자 Argument Input시 에러 발생 방지.
    ex) public Example(String a, String b){
      this.a = b;
      this.b = a;
    }
    형태로 되어있어도 결과 확인 전까지 에러검출 불가능.
    빌더(Builder) 사용 시
    Example.builder()
        .a(a)
        .b(b)
        .build();
        형태로 지정하여 값 Input 하기에 개발자 실수(Human Error)를 최소화
 */