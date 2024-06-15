package com.franklin.chatapp.form;

import com.franklin.chatapp.entity.GroupChat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameGroupChatForm {

    @Size(min = 1, max = 40, message = GroupChat.INVALID_NAME)
    @NotBlank(message = GroupChat.INVALID_NAME)
    private String name;
}
