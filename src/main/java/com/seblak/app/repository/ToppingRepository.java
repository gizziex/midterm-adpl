package com.seblak.app.repository;

import com.seblak.app.model.Topping;
import java.util.List;

public interface ToppingRepository {
    List<Topping> getAllAvailableToppings();
}