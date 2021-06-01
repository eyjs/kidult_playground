/*
 ① @After
   - Junit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
   - 보통은 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용함.
   - 여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아있어 초기화 필요. (Dirty value)
 ② postsRepository.save
   - 테이블 posts에 insert/update 쿼리를 실행.
   - id 값이 있다면 update가, 없다면 insert 쿼리를 실행.
 ③ postsRepository.findAll
   - 테이블 posts에 있는 모든 데이터를 조회해오는 메소드.
 ● H2 DataBase
   - SpringBoot 프로젝트에서 별도 설정 없을 시 H2 DataBase Auto Running

 */
package com.kidult_playground.web.domain.posts;

import com.kidult_playground.domain.posts.Posts;
import com.kidult_playground.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After  // ①
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void read_postsSave(){
        //given
        String title = "테스트 타이틀";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder() // ②
                .title(title)
                .content(content)
                .author("eyjs@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll(); // ③

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_Add() {
        // given
        LocalDateTime now = LocalDateTime.of(2021,6,1,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        // when
        List<Posts> postsList = postsRepository.findAll();
        // then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>> createDate="+posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}

