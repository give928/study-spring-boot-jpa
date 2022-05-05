package com.give928.springboot.jpa.domain.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Getter
public class Book extends Item {
    private String author;
    private String isbn;

    public void update(String name, int price, int stockQuantity, String author, String isbn) {
        super.update(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}
