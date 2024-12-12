package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommandService {
    private final PostRepository postRepository;

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }
}
