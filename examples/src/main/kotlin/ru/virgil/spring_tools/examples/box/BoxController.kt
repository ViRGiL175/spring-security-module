package ru.virgil.spring_tools.examples.box

import jakarta.annotation.security.RolesAllowed
import jakarta.persistence.*
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.virgil.spring_tools.examples.system.rest.RestValues
import ru.virgil.spring_tools.examples.truck.TruckService
import java.util.*

@CrossOrigin(
    origins = ["http://localhost:4200/"],
    allowCredentials = true.toString()
)
@RestController
@RequestMapping("/box")
class BoxController(
    private val truckService: TruckService,
    private val boxService: BoxService,
    private val boxSecurity: BoxSecurity,
) : BoxMapper {

    @GetMapping
    fun getAll(
        @RequestParam(RestValues.pageParam) page: Int, @RequestParam(RestValues.pageSizeParam) size: Int,
    ): List<BoxDto> = boxService.getAll(page, size).stream()
        .map { it.toDto() }
        .toList()

    @RolesAllowed("ROLE_POLICE")
    @GetMapping("/weapons")
    fun getAllWeapons(): List<BoxDto> = boxService.getAllMyWeapons().stream()
        .map { it.toDto() }
        .toList()

    // TODO: Почему-то не работает, надо будет понять, почему.
    @PostAuthorize(
        """
        hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(returnObject)
        or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(returnObject)
    """
    )
    @GetMapping("/{uuid}")
    fun get(@PathVariable uuid: UUID): BoxDto {
        val box = boxService.get(uuid)
        return box.toDto()
    }

    @PreAuthorize(
        """
        hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(#boxDto)
        or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(#boxDto)
    """
    )
    @PostMapping
    fun post(@RequestBody boxDto: BoxDto): BoxDto {
        // TODO: Заменить на Object Provider
        val assignedTruck = truckService.assignTruck()
        val createdBox = boxService.create(assignedTruck, boxDto)
        return createdBox.toDto()
    }

    @PutMapping("/{uuid}")
    fun put(@PathVariable uuid: UUID, @RequestBody boxDto: BoxDto): BoxDto {
        val serverBox = boxService.edit(uuid, boxDto)
        return serverBox.toDto()
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID) {
        boxService.delete(uuid)
    }
}
