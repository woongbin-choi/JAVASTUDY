package com.bincolog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return (long) (Math.max(1,page) - 1) * Math.min(size, MAX_SIZE);
    }

    // 생성자에서 Builder 쓰면 Default 안 먹어서 Nullpoint 뜨는 오류 ~!
    // 클래스에서 해줘야함.
//    @Builder
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }
}
