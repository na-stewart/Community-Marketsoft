package com.communitymarketsoftapi.controller;

import com.google.gson.Gson;
import com.communitymarketsoftapi.model.Item;
import com.communitymarketsoftapi.model.ItemCategory;
import com.communitymarketsoftapi.repository.ItemRepo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@RestController
public class ItemController {
    private Subject subject = SecurityUtils.getSubject();
    private ItemRepo repo = new ItemRepo();

    @GetMapping("item/all")
    public ResponseEntity getAll() throws SQLException {
        subject.checkPermission("item:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }

    @GetMapping("/item/itemcategory/all")
    public ResponseEntity getAllCategories() throws SQLException {
        return new ResponseEntity<>(repo.getCategoryRepo().getAll(), HttpStatus.OK);
    }


    @GetMapping("item/all/whereparams")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) throws SQLException {
        subject.checkPermission("item:display");
        return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
    }

    @GetMapping("item/all/where")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("param") String param) throws SQLException {
        subject.checkPermission("item:display");
        return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
    }


    @GetMapping("/item")
    public ResponseEntity get(@RequestParam int id) throws SQLException {
        subject.checkPermission("item:display");
        return new ResponseEntity<>(repo.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/item")
    public ResponseEntity delete(@RequestParam int id) throws SQLException {
        subject.checkPermission("item:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/item/itemcategory")
    public ResponseEntity deleteItemCategory(@RequestParam("id") int id) throws SQLException {
        subject.checkPermission("item:delete");
        repo.getCategoryRepo().delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/item/itemcategory")
    public ResponseEntity postItemCategory(@RequestParam("category") String category) throws SQLException {
        subject.checkPermission("item:add");
        repo.getCategoryRepo().add(new Gson().fromJson(category, ItemCategory.class));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity post (@RequestParam("item") String json) throws SQLException {
        subject.checkPermission("item:add");
        repo.add(new Gson().fromJson(json, Item.class));
        return new ResponseEntity<>(json , HttpStatus.OK);
    }

    @PutMapping("/item")
    public ResponseEntity<String> put(@RequestParam("item") String json) throws SQLException {
        subject.checkPermission("item:update");
        repo.update(new Gson().fromJson(json, Item.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
