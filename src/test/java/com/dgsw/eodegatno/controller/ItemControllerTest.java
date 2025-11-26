package com.dgsw.eodegatno.controller;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.dto.request.CreateItemRequest;
import com.dgsw.eodegatno.dto.request.UpdateItemRequest;
import com.dgsw.eodegatno.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("분실물 신고 등록 API - 성공")
    void createItem_Success() throws Exception {
        // given
        final String url = "/lost-items";
        final CreateItemRequest request = new CreateItemRequest(
                "홍길동",
                "노트북",
                LocalDateTime.of(2025, 11, 25, 14, 0),
                "도서관 3층",
                ItemStatus.LOST,
                "01098765432",
                "삼성 노트북"
        );

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isCreated());

        List<ItemEntity> items = itemRepository.findAll();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getItemName()).isEqualTo("노트북");
        assertThat(items.get(0).getReporterName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("ID로 분실물 조회 API - 성공")
    void getItemById_Success() throws Exception {
        // given
        final String url = "/lost-items/{id}";

        ItemEntity savedItem = itemRepository.save(ItemEntity.builder()
                .reporterName("이기찬")
                .itemName("에어팟")
                .lostDateTime(LocalDateTime.of(2025, 11, 20, 9, 30))
                .lostLocation("2-1 교실")
                .status(ItemStatus.LOST)
                .contactInfo("01011111111")
                .description("에어팟 2세대")
                .build());

        // when
        ResultActions result = mockMvc.perform(
                get(url, savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());

        Optional<ItemEntity> item = itemRepository.findById(savedItem.getId());
        assertThat(item).isNotEmpty();
        assertThat(item.get().getItemName()).isEqualTo(savedItem.getItemName());
        assertThat(item.get().getReporterName()).isEqualTo(savedItem.getReporterName());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 분실물 조회 API - 실패")
    void getItemById_NotFound_Fail() throws Exception {
        // given
        final String url = "/lost-items/{id}";
        final Long nonExistentId = 9999L;

        // when
        ResultActions result = mockMvc.perform(
                get(url, nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNotFound());
    }
}