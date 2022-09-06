package com.woo.board.config;

import com.woo.board.config.auth.SessionUser;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

//@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final HttpSession httpSession;
    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = "";
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            userId = user.getEmail();
        }

        return Optional.of(userId);
    }
}
