package com.cicdlectures.menuserver.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.repository.DishRepository;
import com.cicdlectures.menuserver.repository.MenuRepository;

public class CreateMenuServiceTests {

    private MenuRepository menuRepository;

    private DishRepository dishRepository;

    private CreateMenuService subject;

    @BeforeEach
    public void init() {
    this.menuRepository = mock(MenuRepository.class);
    this.dishRepository = mock(DishRepository.class);
    this.subject = new CreateMenuService(this.menuRepository, this.dishRepository);
    }
  
    @Test
    @DisplayName("create and save a menu")
    public void createNewMenus() {

        MenuDto newMenu =  new MenuDto(
            null,
            "Menu spécial du chef",
            new HashSet<>(
                Arrays.asList(
                new DishDto(null, "Bananes aux fraises"),
                new DishDto(null, "Bananes flambées")
                )
            )
        );

        Menu expectedSavedMenu = new Menu(
            null,
            "Menu spécial du chef",
            new HashSet<>(
                Arrays.asList(
                new Dish(null, "Bananes aux fraises", null),
                new Dish(null, "Bananes flambées", null)
                )
            )
        );

        when(menuRepository.save(any(Menu.class))).thenReturn(expectedSavedMenu);
        subject.createMenu(newMenu);

        ArgumentCaptor<Menu> expectedMenuCaptor = ArgumentCaptor.forClass(Menu.class);

        verify(menuRepository, times(1)).save(expectedMenuCaptor.capture());
        Menu savedMenu = expectedMenuCaptor.getValue();

        assertEquals(expectedSavedMenu, savedMenu);
    }

    @Test
    @DisplayName("deduplicate of dishes")
    public void duplicateDishes() {
        MenuDto newMenu =  new MenuDto(
            null,
            "Menu spécial du chef",
            new HashSet<>(
                Arrays.asList(
                new DishDto(null, "Bananes aux fraises"),
                new DishDto(null, "Bananes flambées")
                )
            )
        );

        Menu expectedSavedMenu = new Menu(
            null,
            "Menu spécial du chef",
            new HashSet<>(
                Arrays.asList(
                new Dish(null, "Bananes aux fraises", null),
                new Dish(Long.valueOf(33), "Bananes flambées", null)
                )
            )
        );

        Dish existingDish = new Dish(Long.valueOf(33), "Bananes flambées", null);
        when(dishRepository.findByName("Bananes flambées")).thenReturn(existingDish);

        when(menuRepository.save(any(Menu.class))).thenReturn(expectedSavedMenu);
        subject.createMenu(newMenu);

        ArgumentCaptor<Menu> expectedMenuCaptor = ArgumentCaptor.forClass(Menu.class);

        verify(menuRepository, times(1)).save(expectedMenuCaptor.capture());
        Menu savedMenu = expectedMenuCaptor.getValue();

        assertEquals(expectedSavedMenu, savedMenu);
    }
}
