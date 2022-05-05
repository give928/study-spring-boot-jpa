package com.give928.springboot.jpa.service.item;

import com.give928.springboot.jpa.domain.item.Book;
import com.give928.springboot.jpa.domain.item.Item;
import com.give928.springboot.jpa.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();

        itemRepository.save(book);
    }

    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        Book book = (Book) itemRepository.findOne(id);
        book.update(name, price, stockQuantity, author, isbn);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
