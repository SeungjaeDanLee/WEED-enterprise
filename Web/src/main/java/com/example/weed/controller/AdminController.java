package com.example.weed.controller;

import com.example.weed.entity.Dept;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1001_MemberRepository;
import com.example.weed.repository.W1003_DeptRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {

    private final W1003_DeptRepository w1003DeptRepository;
    private final W1001_MemberRepository w1001MemberRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String  memberList(Model model) {
        List<Member> members = w1001MemberRepository.findAll();
        model.addAttribute("members", members);

        List<Dept> deptList = w1003DeptRepository.findAll();
        model.addAttribute("deptList",deptList);
    return "W1009_admin";
    }
}
