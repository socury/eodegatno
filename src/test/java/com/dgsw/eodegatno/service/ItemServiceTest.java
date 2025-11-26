package com.dgsw.eodegatno.service;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.dto.request.CreateItemRequest;
import com.dgsw.eodegatno.dto.request.UpdateItemRequest;
import com.dgsw.eodegatno.dto.response.DataResponse;
import com.dgsw.eodegatno.dto.response.ErrorResponse;
import com.dgsw.eodegatno.dto.response.Response;
import com.dgsw.eodegatno.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private ItemEntity testItem;
    private CreateItemRequest createRequest;

    @BeforeEach
    void setUp() {
        testItem = ItemEntity.builder()
                .reporterName("홍길동")
                .itemName("에어팟")
                .lostDateTime(LocalDateTime.of(2025, 11, 20, 9, 30))
                .lostLocation("2-1 교실")
                .status(ItemStatus.LOST)
                .contactInfo("010-1234-5678")
                .description("흰색 에어팟")
                .build();

        createRequest = new CreateItemRequest(
                "홍길동", "에어팟", LocalDateTime.now(), "2-1 교실",
                ItemStatus.LOST, "010-1234-5678", "흰색 에어팟"
        );
    }

    @Test
    void 분실물_등록_성공() {
        // given
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(testItem);

        // when
        ResponseEntity<Response> response = itemService.createItem(createRequest);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    void ID로_분실물_조회_성공() {
        // given
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        // when
        ResponseEntity<Response> response = itemService.getItemById(1L);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(DataResponse.class);
    }

    @Test
    void ID로_분실물_조회_실패() {
        // given
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<Response> response = itemService.getItemById(999L);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    }

    @Test
    void 분실물_삭제_성공() {
        // given
        when(itemRepository.existsById(1L)).thenReturn(true);

        // when
        ResponseEntity<Response> response = itemService.deleteItem(1L);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void 분실물_삭제_실패() {
        // given
        when(itemRepository.existsById(999L)).thenReturn(false);

        // when
        ResponseEntity<Response> response = itemService.deleteItem(999L);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(itemRepository, never()).deleteById(any());
    }
}