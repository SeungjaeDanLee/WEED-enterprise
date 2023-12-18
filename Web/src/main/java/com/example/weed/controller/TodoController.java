package com.example.weed.controller;

import com.example.weed.dto.TodoDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.Todo;
import com.example.weed.repository.TodoRepository;
import com.example.weed.service.TodoService;
import com.example.weed.service.W1001_MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class TodoController {

    private final TodoService todoService;
    private final W1001_MemberService w1001_memberService;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoService todoService,W1001_MemberService w1001_memberService,TodoRepository todoRepository) {
        this.todoService = todoService;
        this.w1001_memberService=w1001_memberService;
        this.todoRepository=todoRepository;
    }

    @PostMapping("/addTodoList")
    public String addTodoList(@RequestBody List<TodoDTO> todos) {

        Member member = w1001_memberService.getLoggedInMember();

        for (TodoDTO todoDTO : todos) {
            Todo todo = new Todo();
            todo.setTitle(todoDTO.getTitle());
            todo.setContent(todoDTO.getContent());
            todo.setChecked(false);
            todo.setRegistrationDate(LocalDate.now());
            todo.setMember(member);
            todoRepository.save(todo);
        }

        return "Todo list added successfully!";
    }
    @PostMapping("/updateTodoStatus")
    public ResponseEntity<String> updateTodoStatus(@RequestParam Long todoId, @RequestParam boolean checked) {
        try {
            todoService.updateTodoStatus(todoId, checked);
            return ResponseEntity.ok("Todo status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating todo status.");
        }
    }
    @GetMapping("/getTodoList")
    public List<TodoDTO> getTodoList() {
        return todoService.getTodoList();
    }

}
