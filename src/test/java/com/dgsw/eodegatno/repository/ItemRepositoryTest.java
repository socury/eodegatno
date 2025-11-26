package com.dgsw.eodegatno.repository;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.domain.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Sql("/insert-items.sql")
    @DisplayName("LOST 상태 분실물만 조회한다 - 성공")
    void findByStatus_Lost_Success() {
        // when
        List<ItemEntity> lostItems = itemRepository.findByStatus(ItemStatus.LOST);

        // then
        assertThat(lostItems).isNotEmpty();
        assertThat(lostItems).allMatch(item -> item.getStatus() == ItemStatus.LOST);
        assertThat(lostItems).hasSize(4);
    }

    @Test
    @Sql("/insert-items.sql")
    @DisplayName("FOUND 상태 분실물만 조회한다 - 성공")
    void findByStatus_Found_Success() {
        // when
        List<ItemEntity> foundItems = itemRepository.findByStatus(ItemStatus.FOUND);

        // then
        assertThat(foundItems).isNotEmpty();
        assertThat(foundItems).allMatch(item -> item.getStatus() == ItemStatus.FOUND);
        assertThat(foundItems).hasSize(6);
    }

    @Test
    @Sql("/insert-items.sql")
    @DisplayName("분실물 저장 후 조회 - 성공")
    void save_And_FindById_Success() {
        // given
        ItemEntity newItem = ItemEntity.builder()
                .reporterName("김테스")
                .itemName("테스트 물품")
                .lostDateTime(java.time.LocalDateTime.now())
                .lostLocation("테스트 위치")
                .status(ItemStatus.LOST)
                .contactInfo("01012345678")
                .description("테스트 설명")
                .build();

        // when
        ItemEntity savedItem = itemRepository.save(newItem);
        Optional<ItemEntity> foundItem = itemRepository.findById(savedItem.getId());

        // then
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getItemName()).isEqualTo("테스트 물품");
        assertThat(foundItem.get().getReporterName()).isEqualTo("김테스");
    }
}