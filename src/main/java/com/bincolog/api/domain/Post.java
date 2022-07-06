package com.bincolog.api.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String title;

    @Lob
    public String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
