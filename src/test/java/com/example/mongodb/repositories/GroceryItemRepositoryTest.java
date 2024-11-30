package com.example.mongodb.repositories;

import com.example.mongodb.models.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class GroceryItemRepositoryTest {

    @Autowired
    GroceryItemRepository groceryItemRepo;
    private final GroceryItem item1 =
            new GroceryItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks");
    private final GroceryItem item2 =
            new GroceryItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets");
    private final GroceryItem item3 =
            new GroceryItem("Dried Red Chilli", "Dried Whole Red Chilli", 2, "spices");
    private final GroceryItem item4 =
            new GroceryItem("Pearl Millet", "Healthy Pearl Millet", 1, "millets");
    private final GroceryItem item5 =
            new GroceryItem("Cheese Crackers", "Bonny Cheese Crackers Plain", 6, "snacks");

    @BeforeEach
    void setup() {
        groceryItemRepo.deleteAll();
    }

    @Test
    public void saveItems_ShouldPass() {
        // create and test
        groceryItemRepo.save(item1);
        groceryItemRepo.save(item2);
        groceryItemRepo.save(item3);
        groceryItemRepo.save(item4);
        groceryItemRepo.save(item5);

        // assert
        assertEquals(5L, groceryItemRepo.count());
    }

    @Test
    public void findAllItems_ShouldPass() {
        // create
        groceryItemRepo.saveAll(List.of(item1, item2, item3, item4, item5));

        // test
        List<GroceryItem> itemList = groceryItemRepo.findAll();

        // assert
        assertEquals(5, itemList.size());
    }

    @Test
    public void findItemByName_ShouldPass() {
        // create
        groceryItemRepo.saveAll(List.of(item1, item2, item3, item4, item5));

        // test
        GroceryItem item = groceryItemRepo.findItemByName("Whole Wheat Biscuit");

        // assert
        assertEquals("Whole Wheat Biscuit", item.getId());
        assertEquals("Whole Wheat Biscuit", item.getName());
        assertEquals("snacks", item.getCategory());
        assertEquals(5, item.getQuantity());
    }

    @Test
    public void findItemByName_providedPartialName_ShouldPass() {
        // create
        groceryItemRepo.saveAll(List.of(item1, item2, item3, item4, item5));

        // test
        GroceryItem item = groceryItemRepo.findItemByName("Wheat");

        // assert
        assertNull(item);
    }

    @Test
    public void findItemByNameContaining_providedPartialName_ShouldPass() {
        // create
        groceryItemRepo.saveAll(List.of(item1, item2, item3, item4, item5));

        // test
        List<GroceryItem> itemList = groceryItemRepo.findItemByNameContaining("Whole");

        // assert
        assertEquals(2, itemList.size());

        assertTrue(itemList.stream().anyMatch(item -> "Whole Wheat Biscuit".equals(item.getName())));

        assertTrue(itemList.stream().anyMatch(item -> "Dried Whole Red Chilli".equals(item.getName())));
    }

    @Test
    public void findItemByCategory_ShouldPass() {
        // create
        groceryItemRepo.saveAll(List.of(item1, item2, item3, item4, item5));

        // test
        // fetching only name and quantity
        List<GroceryItem> itemList = groceryItemRepo.findAll("snacks");

        // assert
        assertEquals(2, itemList.size());

        assertTrue(itemList.stream().anyMatch(item -> "Bonny Cheese Crackers Plain".equals(item.getName()) &&
                item.getQuantity() == 6));

        assertTrue(itemList.stream().anyMatch(item -> "Whole Wheat Biscuit".equals(item.getName()) &&
                item.getQuantity() == 5));
    }
}
