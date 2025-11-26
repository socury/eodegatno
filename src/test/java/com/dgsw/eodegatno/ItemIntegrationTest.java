package com.dgsw.eodegatno;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql(scripts = "/insert-items.sql")
class ItemIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void SQL데이터_전체조회_10개() {
        // when
        List<ItemEntity> items = itemRepository.findAll();

        // then
        assertThat(items).hasSize(10);
    }

    @Test
    void LOST상태_5개조회() {
        // when
        List<ItemEntity> lostItems = itemRepository.findByStatus(ItemStatus.LOST);

        // then
        assertThat(lostItems).hasSize(5);
    }

    @Test
    void FOUND상태_5개조회() {
        // when
        List<ItemEntity> foundItems = itemRepository.findByStatus(ItemStatus.FOUND);

        // then
        assertThat(foundItems).hasSize(5);
    }
}