package flowpilot.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import flowpilot.service.HelloService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // NEW: Allows React (Vite) frontend to connect
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return helloService.getGreeting();
    }
}