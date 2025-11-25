package com.dgsw.eodegatno.repository;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.domain.QItemEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public List<ItemEntity> findByStatus(ItemStatus status) {
        QItemEntity item = QItemEntity.itemEntity;
        
        return queryFactory
                .selectFrom(item)
                .where(item.status.eq(status))
                .orderBy(item.createdAt.desc())
                .fetch();
    }
}
