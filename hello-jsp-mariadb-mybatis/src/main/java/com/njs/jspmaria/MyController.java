package com.njs.jspmaria;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
	
	@Autowired
    private MyService myService;
	
	@GetMapping("/hello")
    public String select(Model model) throws Exception{
        List<MyDTO> list = myService.select();
        model.addAttribute("myList", list);
        return "hello";
    }
}
