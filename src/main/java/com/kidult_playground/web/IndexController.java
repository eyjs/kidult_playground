package com.kidult_playground.web;

import com.kidult_playground.config.auth.LoginUser;
import com.kidult_playground.config.auth.dto.SessionUser;
import com.kidult_playground.service.posts.PostsService;
import com.kidult_playground.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
/*
 ① Model
   - 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
   - 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달한다.
 @ LoginUser SessionUser user
   - SessionUser user = (SessionUser) httpSession.getAttribute("user");
     -> 기존에(User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선.
   - 이제는 어떤 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됨.

 */

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {  // ①
        model.addAttribute("posts",
                postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}


