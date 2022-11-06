package org.testmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.exception.UserAlreadyExistsException;
import org.testmanager.exception.UserNotFoundException;
import org.testmanager.mapper.UserMapper;
import org.testmanager.model.Pagination;
import org.testmanager.model.UserDTO;
import org.testmanager.model.UserListDTO;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getFreeUser() {
        log.info("getFreeUser");
        User dbUser = userRepository.findFreeUser().orElseThrow(() -> new FreeUserNotFoundException());
        dbUser.setLocked(true);
        userRepository.save(dbUser);
        return userMapper.entityToDto(dbUser);
    }

    public UserDTO createUser(UserDTO userDto) {
        log.info("create user");
        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDto)));
        }
    }

    public UserListDTO getUsersPaginated(int page, int size) {
        log.info("getUsersPaginated");
        Page<User> resultPage = userRepository.findAll(PageRequest.of(page, size));
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUsers(resultPage.getContent().stream().map(u -> userMapper.entityToDto(u)).collect(Collectors.toList()));
        userListDTO.setPagination(new Pagination(resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements()));
        return userListDTO;
    }

    public UserDTO editUser(UserDTO userDto) {
        log.info("edit user");
        User dbUser = userRepository.findByLogin(userDto.getLogin()).orElseThrow(() -> new UserNotFoundException());
        dbUser.setPassword(userDto.getPassword());
        return userMapper.entityToDto(userRepository.save(dbUser));
    }

    public void deleteUser(String login) {
        log.info("delete user");
        User dbUser = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException());
        userRepository.delete(dbUser);
    }

    public void unlockUser(String login) {
        log.info("unlock user");
        User dbUser = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException());
        dbUser.setLocked(false);
        userRepository.save(dbUser);
    }


}
