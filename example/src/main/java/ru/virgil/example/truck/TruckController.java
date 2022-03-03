package ru.virgil.example.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.virgil.example.box.Box;
import ru.virgil.example.box.BoxDto;
import ru.virgil.example.box.BoxMapper;
import ru.virgil.example.box.BoxService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/truck")
@RequiredArgsConstructor
public class TruckController {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "size";
    private final TruckService truckService;
    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @GetMapping("/{truckUuid}/box")
    public List<BoxDto> getBoxesByTruck(@PathVariable UUID truckUuid, @RequestParam(PAGE_PARAM) int page,
            @RequestParam(PAGE_SIZE_PARAM) int size) {
        Truck truck = truckService.get(truckUuid);
        List<Box> boxes = boxService.getAll(truck, page, size);
        return boxes.stream()
                .map(boxMapper::toDto)
                .toList();
    }

}
