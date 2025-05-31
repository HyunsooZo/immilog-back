//package com.backend.immilog.user.application;
//
//import com.backend.immilog.global.enums.Country;
//import com.backend.immilog.user.application.usecase.impl.LocationFetchingService;
//import com.backend.immilog.user.infrastructure.gateway.GeocodeConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.util.Pair;
//import org.springframework.http.MediaType;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.test.web.client.match.MockRestRequestMatchers;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.CompletableFuture;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
//@DisplayName("위치 서비스 테스트")
//class LocationFetchingServiceTest {
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final GeocodeConfig geocodeConfig = new GeocodeConfig("https://example.com/geocode?lat=%f&lng=%f&key=%s", "fake-key");
//    private final LocationFetchingService locationFetchingService = new LocationFetchingService(restTemplate, geocodeConfig);
//    private final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
//
//    @Test
//    @DisplayName("API 호출 성공")
//    void getCountry_success() {
//        // given
//        Double latitude = 37.5665;
//        Double longitude = 126.9780;
//
//        // Mock 서버에서 반환할 올바른 JSON 응답 설정
//        mockServer.expect(MockRestRequestMatchers.anything())
//                .andRespond(withSuccess("""
//                        {
//                        "plus_code": {
//                            "compound_code": "HX8H+H6R 대한민국 서울특별시",
//                            "global_code": "8Q98HX8H+H6R"
//                        }
//                        }""".trim(), MediaType.APPLICATION_JSON));
//
//        // when
//        var resultFuture = locationFetchingService.getCountry(latitude, longitude);
//        var result = resultFuture.join();
//
//        // then
//        assertThat(result.getFirst()).isEqualTo(Country.SOUTH_KOREA.koreanName());
//        assertThat(result.getSecond()).isEqualTo("서울특별시");
//    }
//
//    @Test
//    @DisplayName("API 호출 실패 - 서버 에러")
//    void getCountry_fail_apiException() {
//        // given
//        var latitude = 37.5665;
//        var longitude = 126.9780;
//
//        // Mock 서버에서 에러 응답 설정
//        mockServer.expect(MockRestRequestMatchers.anything()).andRespond(withServerError());
//
//        // when
//        var resultFuture = locationFetchingService.getCountry(latitude, longitude);
//        var result = resultFuture.join();
//
//        // then
//        assertThat(result.getFirst()).isEqualTo("기타 국가");
//        assertThat(result.getSecond()).isEqualTo("기타 지역");
//    }
//
//    @Test
//    @DisplayName("API 호출 실패 - 잘못된 좌표")
//    void getCountry_fail_invalidCoordinates() {
//        // given
//        Double latitude = -1.0;
//        Double longitude = -1.0;
//
//        // when
//        var resultFuture = locationFetchingService.getCountry(latitude, longitude);
//        var result = resultFuture.join();
//
//        // then
//        assertThat(result).isNull();
//    }
//
//    @Test
//    @DisplayName("API 호출 실패 - json 파싱 실패")
//    void getCountry_fail_json_invalid() {
//        // given
//        Double latitude = 37.5665;
//        Double longitude = 126.9780;
//
//        // Mock 서버에서 반환할 올바른 JSON 응답 설정
//        mockServer.expect(MockRestRequestMatchers.anything())
//                .andRespond(withSuccess("""
//                        }{
//                        """.trim(), MediaType.APPLICATION_JSON));
//
//        // when
//        var resultFuture = locationFetchingService.getCountry(latitude, longitude);
//
//        // then
//        assertThat(resultFuture.join().getFirst()).isEqualTo("기타 국가");
//    }
//
//    @Test
//    @DisplayName("CompletableFuture 객체 합치기")
//    void joinCompletableFutureLocation() {
//        // given
//        var location = Pair.of("KR", "South Korea");
//        var value = CompletableFuture.completedFuture(location);
//
//        // when
//        var result = locationFetchingService.joinCompletableFutureLocation(value);
//
//        // then
//        assertThat(result).isEqualTo(location);
//    }
//}