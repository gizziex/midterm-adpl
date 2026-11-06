package com.seblak.app.repository;

import com.seblak.app.model.Menu;
import java.util.List;

public interface MenuRepository {
    List<Menu> getAllActiveMenus();
}