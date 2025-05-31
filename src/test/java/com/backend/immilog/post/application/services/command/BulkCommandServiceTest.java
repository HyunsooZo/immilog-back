package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.application.services.BulkCommandService;
import com.backend.immilog.post.domain.repositories.BulkInsertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("BulkCommandService 테스트")
class BulkCommandServiceTest {

    private final BulkInsertRepository bulkInsertRepository = mock(BulkInsertRepository.class);
    private final BulkCommandService bulkCommandService = new BulkCommandService(bulkInsertRepository);

    @Test
    @DisplayName("saveAll 메서드가 모든 엔티티를 성공적으로 저장")
    void saveAllSavesAllEntitiesSuccessfully() {
        List<String> entities = Collections.singletonList("entity");
        String command = "INSERT INTO table (column) VALUES (?)";
        BiConsumer<PreparedStatement, String> consumer = (ps, entity) -> {};

        bulkCommandService.saveAll(entities, command, consumer);

        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BiConsumer> consumerCaptor = ArgumentCaptor.forClass(BiConsumer.class);

        verify(bulkInsertRepository).saveAll(entitiesCaptor.capture(), commandCaptor.capture(), consumerCaptor.capture());

        assertThat(entitiesCaptor.getValue()).isEqualTo(entities);
        assertThat(commandCaptor.getValue()).isEqualTo(command);
        assertThat(consumerCaptor.getValue()).isEqualTo(consumer);
    }

    @Test
    @DisplayName("saveAll 메서드가 빈 엔티티 리스트를 처리")
    void saveAllHandlesEmptyEntityList() {
        List<String> entities = Collections.emptyList();
        String command = "INSERT INTO table (column) VALUES (?)";
        BiConsumer<PreparedStatement, String> consumer = (ps, entity) -> {};

        bulkCommandService.saveAll(entities, command, consumer);

        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BiConsumer> consumerCaptor = ArgumentCaptor.forClass(BiConsumer.class);

        verify(bulkInsertRepository).saveAll(entitiesCaptor.capture(), commandCaptor.capture(), consumerCaptor.capture());

        assertThat(entitiesCaptor.getValue()).isEqualTo(entities);
        assertThat(commandCaptor.getValue()).isEqualTo(command);
        assertThat(consumerCaptor.getValue()).isEqualTo(consumer);
    }

    @Test
    @DisplayName("saveAll 메서드가 null 엔티티 리스트를 처리")
    void saveAllHandlesNullEntityList() {
        List<String> entities = null;
        String command = "INSERT INTO table (column) VALUES (?)";
        BiConsumer<PreparedStatement, String> consumer = (ps, entity) -> {};

        bulkCommandService.saveAll(entities, command, consumer);

        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BiConsumer> consumerCaptor = ArgumentCaptor.forClass(BiConsumer.class);

        verify(bulkInsertRepository).saveAll(entitiesCaptor.capture(), commandCaptor.capture(), consumerCaptor.capture());

        assertThat(entitiesCaptor.getValue()).isEqualTo(entities);
        assertThat(commandCaptor.getValue()).isEqualTo(command);
        assertThat(consumerCaptor.getValue()).isEqualTo(consumer);
    }
}