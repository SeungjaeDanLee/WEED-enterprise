package com.example.weed.controller;

import com.example.weed.entity.Dept;
import com.example.weed.repository.W1003_DeptRepository;
import com.example.weed.repository.W1001_MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(annotations = Controller.class)
@AllArgsConstructor
@Transactional
public class W1003_OrganizeController {

    private final W1003_DeptRepository w1003DeptRepository;
    private final W1001_MemberRepository w1001MemberRepository;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        List<Dept> depts = w1003DeptRepository.findAll();
        model.addAttribute("depts", depts);

        long totalMembers = w1001MemberRepository.count();
        model.addAttribute("totalMembers",totalMembers);

    }
}
