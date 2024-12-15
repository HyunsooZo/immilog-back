package com.backend.immilog.global.application;

import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import com.backend.immilog.user.application.services.command.RefreshTokenCommandService;
import com.backend.immilog.user.application.services.query.RefreshTokenQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Redis 키/값 테스트")
class RedisServiceTest {
    private final DataRepository dataRepository = mock(DataRepository.class);
    private final RefreshTokenQueryService refreshTokenQueryService = new RefreshTokenQueryService(dataRepository);
    private final RefreshTokenCommandService refreshTokenCommandService = new RefreshTokenCommandService(dataRepository);

    @Test
    @DisplayName("키/값 저장")
    void save() {
        // given
        String key = "key";
        String value = "value";
        int expireTime = 10;

        // when
        refreshTokenCommandService.saveKeyAndValue(key, value, expireTime);

        // then
        verify(dataRepository, times(1)).save(key, value, expireTime);
    }

    @Test
    @DisplayName("키로 값 가져오기")
    void findByKey() {
        // given
        String key = "key";
        String expectedValue = "value";

        when(dataRepository.findByKey(key)).thenReturn(expectedValue);

        // when
        String result = refreshTokenQueryService.getValueByKey(key);

        // then
        assertThat(result).isEqualTo(expectedValue);
        verify(dataRepository, times(1)).findByKey(key);
    }

    @Test
    @DisplayName("키로 값 삭제")
    void deleteByKey() {
        // given
        String key = "key";

        // when
        refreshTokenCommandService.deleteValueByKey(key);

        // then
        verify(dataRepository, times(1)).deleteByKey(key);
    }
}

