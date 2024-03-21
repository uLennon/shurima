package com.api.shurima.controller;

import com.api.shurima.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public ModelAndView showIconFreeWeek() {
        ModelAndView mv = new ModelAndView("index");
        List<String> icons = indexService.getUrlListIcon();
        mv.addObject("icons", icons);
        return mv;
    }

    @PostMapping("/")
    public String showPageInfo(@RequestParam String name, @RequestParam String tagName) {
        return "redirect:/" + name + "/" + tagName;
    }
}
