package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<HogwartsUser> users = userService.findAll();
        List<UserDto> userDtos = users.stream().map(userToUserDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Users", userDtos);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser user) {
        HogwartsUser savedUser = userService.add(user);
        UserDto userDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add User", userDto);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable int userId) {
        HogwartsUser user = userService.findById(userId);
        UserDto userDto = userToUserDtoConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable int userId, @Valid @RequestBody UserDto userDto) {
        HogwartsUser user = userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedUser = userService.update(userId, user);
        UserDto updatedUserDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable int userId) {
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
