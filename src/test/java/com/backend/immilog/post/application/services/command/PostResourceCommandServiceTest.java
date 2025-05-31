package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.application.services.PostResourceCommandService;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("PostResourceCommandService 테스트")
class PostResourceCommandServiceTest {

    private final PostResourceRepository postResourceRepository = mock(PostResourceRepository.class);
    private final PostResourceCommandService postResourceCommandService = new PostResourceCommandService(postResourceRepository);

    @Test
    @DisplayName("deleteAllEntities 메서드가 모든 엔티티를 성공적으로 삭제")
    void deleteAllEntitiesDeletesAllEntitiesSuccessfully() {
        Long postSeq = 1L;
        PostType postType = PostType.POST;
        ResourceType resourceType = ResourceType.TAG;
        List<String> deleteResources = List.of("resource1", "resource2");

        postResourceCommandService.deleteAllEntities(postSeq, postType, resourceType, deleteResources);

        ArgumentCaptor<Long> postSeqCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PostType> postTypeCaptor = ArgumentCaptor.forClass(PostType.class);
        ArgumentCaptor<ResourceType> resourceTypeCaptor = ArgumentCaptor.forClass(ResourceType.class);
        ArgumentCaptor<List> deleteResourcesCaptor = ArgumentCaptor.forClass(List.class);

        verify(postResourceRepository).deleteAllEntities(postSeqCaptor.capture(), postTypeCaptor.capture(), resourceTypeCaptor.capture(), deleteResourcesCaptor.capture());

        assertThat(postSeqCaptor.getValue()).isEqualTo(postSeq);
        assertThat(postTypeCaptor.getValue()).isEqualTo(postType);
        assertThat(resourceTypeCaptor.getValue()).isEqualTo(resourceType);
        assertThat(deleteResourcesCaptor.getValue()).isEqualTo(deleteResources);
    }

    @Test
    @DisplayName("deleteAllEntities 메서드가 빈 리소스 리스트를 처리")
    void deleteAllEntitiesHandlesEmptyResourceList() {
        Long postSeq = 1L;
        PostType postType = PostType.POST;
        ResourceType resourceType = ResourceType.TAG;
        List<String> deleteResources = List.of();

        postResourceCommandService.deleteAllEntities(postSeq, postType, resourceType, deleteResources);

        ArgumentCaptor<Long> postSeqCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PostType> postTypeCaptor = ArgumentCaptor.forClass(PostType.class);
        ArgumentCaptor<ResourceType> resourceTypeCaptor = ArgumentCaptor.forClass(ResourceType.class);
        ArgumentCaptor<List> deleteResourcesCaptor = ArgumentCaptor.forClass(List.class);

        verify(postResourceRepository).deleteAllEntities(postSeqCaptor.capture(), postTypeCaptor.capture(), resourceTypeCaptor.capture(), deleteResourcesCaptor.capture());

        assertThat(postSeqCaptor.getValue()).isEqualTo(postSeq);
        assertThat(postTypeCaptor.getValue()).isEqualTo(postType);
        assertThat(resourceTypeCaptor.getValue()).isEqualTo(resourceType);
        assertThat(deleteResourcesCaptor.getValue()).isEqualTo(deleteResources);
    }

    @Test
    @DisplayName("deleteAllByPostSeq 메서드가 모든 엔티티를 성공적으로 삭제")
    void deleteAllByPostSeqDeletesAllEntitiesSuccessfully() {
        Long seq = 1L;

        postResourceCommandService.deleteAllByPostSeq(seq);

        ArgumentCaptor<Long> seqCaptor = ArgumentCaptor.forClass(Long.class);
        verify(postResourceRepository).deleteAllByPostSeq(seqCaptor.capture());

        assertThat(seqCaptor.getValue()).isEqualTo(seq);
    }

    @Test
    @DisplayName("deleteAllByPostSeq 메서드가 null 시퀀스를 처리")
    void deleteAllByPostSeqHandlesNullSeq() {
        Long seq = null;

        postResourceCommandService.deleteAllByPostSeq(seq);

        ArgumentCaptor<Long> seqCaptor = ArgumentCaptor.forClass(Long.class);
        verify(postResourceRepository).deleteAllByPostSeq(seqCaptor.capture());

        assertThat(seqCaptor.getValue()).isNull();
    }
}