package com.dgsw.eodegatno.repository;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void 분실물_저장_성공() {
        // given
        ItemEntity item = ItemEntity.builder()
                .reporterName("홍길동")
                .itemName("에어팟")
                .lostDateTime(LocalDateTime.now())
                .lostLocation("2-1 교실")
                .status(ItemStatus.LOST)
                .contactInfo("010-1234-5678")
                .description("흰색 에어팟")
                .build();

        // when
        ItemEntity saved = itemRepository.save(item);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getReporterName()).isEqualTo("홍길동");
    }

    @Test
    void ID로_조회_성공() {
        // given
        ItemEntity item = createAndSaveItem();

        // when
        Optional<ItemEntity> found = itemRepository.findById(item.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getItemName()).isEqualTo("에어팟");
    }

    @Test
    void ID로_조회_실패() {
        // when
        Optional<ItemEntity> found = itemRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void 전체_조회_정렬() {
        // given
        createAndSaveItem();
        createAndSaveItem();

        // when
        List<ItemEntity> items = itemRepository.findAllByOrderByCreatedAtDesc();

        // then
        assertThat(items).hasSize(2);
    }

    private ItemEntity createAndSaveItem() {
        ItemEntity item = ItemEntity.builder()
                .reporterName("홍길동")
                .itemName("에어팟")
                .lostDateTime(LocalDateTime.now())
                .lostLocation("2-1 교실")
                .status(ItemStatus.LOST)
                .contactInfo("010-1234-5678")
                .description("흰색 에어팟")
                .build();
        return itemRepository.save(item);
    }
}