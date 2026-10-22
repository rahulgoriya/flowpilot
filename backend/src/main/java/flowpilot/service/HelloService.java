package flowpilot.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getGreeting() {
        return "FlowPilot Backend is running!";
    }
}