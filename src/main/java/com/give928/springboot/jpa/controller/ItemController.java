package com.give928.springboot.jpa.controller;

import com.give928.springboot.jpa.controller.form.ItemForm;
import com.give928.springboot.jpa.domain.item.Book;
import com.give928.springboot.jpa.domain.item.Item;
import com.give928.springboot.jpa.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("itemForm", ItemForm.builder().build());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(@Valid ItemForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        itemService.saveItem(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());

        return "redirect:/items";
    }

    /**
     * 상품 목록
     */
    @GetMapping(value = "/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "items/itemList";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        ItemForm form = ItemForm.builder()
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .author(item.getAuthor())
                .isbn(item.getIsbn())
                .build();

        model.addAttribute("itemForm", form);

        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     */
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute @Valid ItemForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "items/updateItemForm";
        }

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());

        return "redirect:/items";
    }
}
