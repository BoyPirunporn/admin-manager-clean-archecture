package com.loko.applications.ports.in.user;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;

public interface UserUseCase {
    UserDto createUser(UserCreationDto userCreationDto);
    UserCreationDto getUserById(String id);
    PagedResult<UserDto> getAllUser(PageQuery query);
}
