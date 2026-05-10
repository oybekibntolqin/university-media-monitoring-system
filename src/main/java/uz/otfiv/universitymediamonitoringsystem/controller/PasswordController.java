package uz.otfiv.universitymediamonitoringsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/password/page")
public class PasswordController {
    private final EmployeeService employeeService;

    @GetMapping("/sendId")
    public String sendLink(Model model, @RequestParam(name = "employeeId") UUID employeeId) {
        if (!employeeService.check(employeeId)) {
            return "invalidPage";
        }
        model.addAttribute("employeeId", employeeId);
        return "password";
    }
}
