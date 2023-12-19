
package com.example.coderlab.controller.client;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.entity.VerificationToken;
import com.example.coderlab.event.RegistrationCompleteEvent;
import com.example.coderlab.event.listener.RegistrationCompleteEventListener;
import com.example.coderlab.exception.AlreadyExistsException;
import com.example.coderlab.exception.ResourceNotFoundException;
import com.example.coderlab.repository.VerificationTokenRepository;
import com.example.coderlab.service.RegistrationRequest;

import com.example.coderlab.service.UserServices;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserServices userService;
    private final ApplicationEventPublisher publisher;
    private final HttpServletRequest servletRequest;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;

    @GetMapping("")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("DEVELOPER", "", "", ""));
        return "sign-up";
    }

    @PostMapping("/process_register")
    public String processRegister(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest, BindingResult bindingResult, final HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        try {
            registrationRequest = new RegistrationRequest(registrationRequest.role(), registrationRequest.getPassword(),
                    registrationRequest.getFullName(),
                    registrationRequest.getEmail());
            UserEntity user = userService.registerUser(registrationRequest);
            //publish registration event
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            model.addAttribute("email", user.getEmail());
        } catch (AlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "sign-up";
        }
        model.addAttribute("successMessage", "Vui lòng kiểm tra địa chỉ email của bạn.");
        return "sign-up";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String url = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken==null) {
            model.addAttribute("error", "Không tìm thấy token của bạn.");
            return "sign-in";
        }
        if (theToken.getUser().isActive()) {
            model.addAttribute("success", "Tài khoản của bạn đã được kích hoạt.");
        } else {
            String verificationResult = userService.validateToken(token);
            if (verificationResult.equalsIgnoreCase("Valid")) {
                model.addAttribute("success", "Đăng ký thành công.");
            }
            else {
                model.addAttribute("error", "Đường dẫn này đã hết hạn, hãy <a style=\"color:#0b5ed7\" href=\"" + url + "\">lấy đường dẫn mới </a>để kích hoạt tài khoản của bạn!");
            }
        }
        return "sign-in";
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        try {
            VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
            UserEntity theUser = verificationToken.getUser();
            resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
            model.addAttribute("success", "Vui lòng kiểm tra liên kiết vừa được gửi về email của bạn.");

        } catch (ResourceNotFoundException e) {
            model.addAttribute("title", "Lỗi");
            model.addAttribute("message", "Có lỗi xảy ra trong quá trình tìm kiếm token !.");
        }
        return "sign-in";
    }

    private void resendVerificationTokenEmail(UserEntity theUser, String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException, MessagingException {
        String url = applicationUrl+"/register/verifyEmail?token="+verificationToken.getToken();
        eventListener.sendVerificationEmail(theUser, url);
    }
}
