package com.cicdlectures.menuserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.repository.MenuRepository;
import com.cicdlectures.menuserver.repository.DishRepository;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// src/test/java/com/cicdlectures/menuserver/service/ListMenuServiceTests.java

public class ListMenuServiceTests {

    private ListMenuService subject;
    private MenuRepository menuRepository;
  
    @BeforeEach
    public void init() {
      this.menuRepository = mock(MenuRepository.class);
      this.subject = new ListMenuService(this.menuRepository);
    }
  
    @Test
    @DisplayName("lists all known menus")
    public void listsKnownMenus() {
      // Défini une liste de menus avec un menus.
      Iterable<Menu> existingMenus = Arrays.asList(
        new Menu(
          Long.valueOf(1),
          "Christmas menu",
          new HashSet<>(
            Arrays.asList(
              new Dish(Long.valueOf(1), "Turkey", null),
              new Dish(Long.valueOf(2), "Pecan Pie", null)
            )
          )
        )
      );

      // On configure le menuRepository pour qu'il retourne notre liste de menus.
      when(menuRepository.findAll()).thenReturn(existingMenus);

      // On appelle notre sujet
      List<MenuDto> gotMenus = subject.listMenus();

      // On défini wantMenus, les résultats attendus
      Iterable<MenuDto> wantMenus = Arrays.asList(
          new MenuDto(
            Long.valueOf(1),
            "Christmas menu",
            new HashSet<>(
              Arrays.asList(
                new DishDto(Long.valueOf(1), "Turkey"),
                new DishDto(Long.valueOf(2), "Pecan Pie")
              )
            )
          )
        );

        // On compare la valeur obtenue avec la valeur attendue.
        assertEquals(wantMenus, gotMenus);
    }
  }