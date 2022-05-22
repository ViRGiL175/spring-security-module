package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.virgil.example.box.Box;
import ru.virgil.example.box.BoxDto;
import ru.virgil.example.box.BoxMapper;
import ru.virgil.example.box.BoxService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/truck")
@RequiredArgsConstructor
public class TruckController {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "size";
    private final TruckService truckService;
    private final UserDetailsService userDetailsService;
    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @GetMapping("/{truckUuid}/box")
    public List<BoxDto> getBoxesByTruck(@PathVariable UUID truckUuid, @RequestParam(PAGE_PARAM) int page,
            @RequestParam(PAGE_SIZE_PARAM) int size) {
        Truck truck = truckService.get(truckUuid);
        UserDetails owner = userDetailsService.getCurrentUser();
        List<Box> boxes = boxService.getAll(owner, truck, page, size);
        return boxes.stream()
                .map(boxMapper::toDto)
                .toList();
    }

}
