package com.woo.board.repository.items;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woo.board.dto.items.*;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.boards.QBoards;
import com.woo.board.entity.items.ItemSellStatus;
import com.woo.board.entity.items.Items;
import com.woo.board.entity.items.QItemImg;
import com.woo.board.entity.items.QItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemsRepositoryCustomImpl implements ItemsRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public ItemsRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItems.items.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItems.items.createdDate.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("itemName", searchBy)) {
            return QItems.items.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItems.items.createdBy.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("itemDetails", searchBy)) {
            return QItems.items.itemDetails.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItems.items.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<ItemsDto> getItemsPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItems items = QItems.items;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<ItemsDto> results = queryFactory
                .select(new QItemsDto(
                        items.id,
                        items.itemName,
                        items.itemDetails,
                        itemImg.imgUrl,
                        items.itemPrice)
                )
                .from(itemImg)
                .join(itemImg.items, items)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(items.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemsDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Items> getRegItemsPage(ItemSearchDto itemSearchDto, Pageable pageable, String userEmail) {
        QItems items = QItems.items;

        QueryResults<Items> results = queryFactory
                .selectFrom(QItems.items)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), itemNameLike(itemSearchDto.getSearchQuery()), items.createdBy.eq(userEmail))
                .orderBy(items.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Items> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
