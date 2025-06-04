package com.pawara.bookstore.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {

    private Long id;
    private String name;
    private String biography;
    private Integer bookCount;
}
