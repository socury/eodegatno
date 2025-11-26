package com.dgsw.eodegatno.controller;

import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.dto.request.CreateItemRequest;
import com.dgsw.eodegatno.dto.response.DataResponse;
import com.dgsw.eodegatno.dto.response.ErrorResponse;
import com.dgsw.eodegatno.dto.response.ItemResponse;
import com.dgsw.eodegatno.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    @Test
    void 분실물_등록_성공() throws Exception {
        // given
        CreateItemRequest request = new CreateItemRequest(
                "홍길동", "에어팟", LocalDateTime.now(), "2-1 교실",
                ItemStatus.LOST, "010-1234-5678", "흰색 에어팟"
        );
        ItemResponse itemResponse = new ItemResponse(
                1L, "홍길동", "에어팟", LocalDateTime.now(), "2-1 교실",
                ItemStatus.LOST, "010-1234-5678", "흰색 에어팟",
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(itemService.createItem(any())).thenReturn(
                ResponseEntity.status(201).body(new DataResponse<>(201, "등록 완료", itemResponse))
        );

        // when & then
        mockMvc.perform(post("/lost-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    void 전체_조회_성공() throws Exception {
        // given
        List<ItemResponse> items = Arrays.asList(
                new ItemResponse(1L, "홍길동", "에어팟", LocalDateTime.now(), "교실",
                        ItemStatus.LOST, "010-1111-1111", "에어팟", LocalDateTime.now(), LocalDateTime.now())
        );
        when(itemService.getAllItems()).thenReturn(
                ResponseEntity.ok(new DataResponse<>(200, "조회 완료", items))
        );

        // when & then
        mockMvc.perform(get("/lost-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void ID로_조회_성공() throws Exception {
        // given
        ItemResponse item = new ItemResponse(
                1L, "홍길동", "에어팟", LocalDateTime.now(), "교실",
                ItemStatus.LOST, "010-1111-1111", "에어팟", LocalDateTime.now(), LocalDateTime.now()
        );
        when(itemService.getItemById(1L)).thenReturn(
                ResponseEntity.ok(new DataResponse<>(200, "조회 완료", item))
        );

        // when & then
        mockMvc.perform(get("/lost-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void ID로_조회_실패() throws Exception {
        // given
        when(itemService.getItemById(999L)).thenReturn(
                ResponseEntity.status(404).body(new ErrorResponse(404, "없음", "Not found"))
        );

        // when & then
        mockMvc.perform(get("/lost-items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void 삭제_성공() throws Exception {
        // given
        when(itemService.deleteItem(1L)).thenReturn(
                ResponseEntity.ok(new DataResponse<>(200, "삭제 완료", null))
        );

        // when & then
        mockMvc.perform(delete("/lost-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void 삭제_실패() throws Exception {
        // given
        when(itemService.deleteItem(999L)).thenReturn(
                ResponseEntity.status(404).body(new ErrorResponse(404, "없음", "Not found"))
        );

        // when & then
        mockMvc.perform(delete("/lost-items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}