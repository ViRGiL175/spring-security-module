package ru.virgil.spring_tools.examples.truck

import org.springframework.web.bind.annotation.*
import ru.virgil.spring_tools.examples.box.BoxDto
import ru.virgil.spring_tools.examples.box.BoxMapper
import ru.virgil.spring_tools.examples.box.BoxService
import ru.virgil.spring_tools.examples.system.rest.RestValues
import java.util.*

@RestController
@RequestMapping("/truck")
class TruckController(
    private val truckService: TruckService,
    private val boxService: BoxService,
) : BoxMapper {

    @GetMapping("/{truckUuid}/box")
    fun getBoxesByTruck(
        @PathVariable truckUuid: UUID, @RequestParam(RestValues.pageParam) page: Int,
        @RequestParam(RestValues.pageSizeParam) size: Int,
    ): List<BoxDto> {
        val truck = truckService.get(truckUuid)
        val boxes = boxService.getAll(truck, page, size)
        return boxes.stream()
            .map { it.toDto() }
            .toList()
    }

}
