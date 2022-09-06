package com.woo.board.dto;

import com.woo.board.config.auth.SessionUser;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {

    private String name;
    private String email;
    private String picture;

    public void updateUserInfo(SessionUser user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
