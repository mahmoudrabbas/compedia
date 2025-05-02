package com.compedia.DTOs;

import com.compedia.entities.UserEntity;
import com.compedia.utils.SanitizingDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    @NotNull(message = "Post Is Empty, You Should Write Anything..")
    @JsonDeserialize(using = SanitizingDeserializer.class)
    private String postContent;
    @NotNull(message = "No User Adding The Post..")
    private UserEntity user;
}
