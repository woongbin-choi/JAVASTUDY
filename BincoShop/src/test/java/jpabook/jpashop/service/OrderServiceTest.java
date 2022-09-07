package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    private Book CreateBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member CreateMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    @DisplayName("상품 주문")
    public void order() throws Exception {
        // given
        Member member = CreateMember();

        Book book = CreateBook("JPA BOOK", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 줄어야 한다", 8, book.getStockQuantity());
    }

    @Test
    @DisplayName("주문 취소")
    public void cancel() throws Exception {
        // given
        Member member = CreateMember();
        Book book = CreateBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소 된 상품은 재고 원복", 10, book.getStockQuantity());
    }

    @Test
    @DisplayName("재고 수량 초과")
    public void stockOver() throws Exception {
        // given
        Member member = CreateMember();
        Item item = CreateBook("JPA BOOK", 100000, 10);

        int orderCount = 10;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생한다.");
    }

//    @Test
//    public void lazyTest() throws Exception {
//        Order findOrder = em.createQuery("select o from Order o", Order.class).getSingleResult();
//    }
}