package ru.job4j.cars.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.ZoneDto;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.UserRepository;
import ru.job4j.cars.service.UserService;

import java.util.*;

@Service
@AllArgsConstructor
public class RegularUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {

        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public Collection<ZoneDto> getAllZones() {
        List<TimeZone> zones = new ArrayList<>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }

        return zones.stream()
                .map(zone -> new ZoneDto(zone.getID(),
                        zone.getID() + " : " + zone.getDisplayName()))
                .toList();
    }

}
