package com.bincolog.api.request;

import com.bincolog.api.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
public class PostCreate {

    // @NotBlank : null과 빈 값 모두 체크해준다.

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Builder 장점
    // 가독성이 좋다 (값 생성에 대한 유연함)
    // 필요한 값만 받을 수 있다
    // 객체의 불변성

    public void validationChk(){
        if(title.contains("바보")){
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
