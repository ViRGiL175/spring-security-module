package ru.virgil.example.box;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.virgil.example.system.HttpAddressConstants;
import ru.virgil.example.truck.Truck;
import ru.virgil.example.truck.TruckService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/box")
@RequiredArgsConstructor
public class BoxController implements HttpAddressConstants {

    private final UserDetailsService userDetailsService;
    private final TruckService truckService;
    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @GetMapping
    public List<BoxDto> getAll(@RequestParam(PAGE_PARAM) int page, @RequestParam(PAGE_SIZE_PARAM) int size) {
        UserDetails owner = userDetailsService.getCurrentUser();
        return boxService.getAll(owner, page, size).stream()
                .map(boxMapper::toDto)
                .toList();
    }

    @GetMapping("/{uuid}")
    @PostAuthorize("""
            hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(returnObject)
            or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(returnObject)
            """)
    public BoxDto get(@PathVariable UUID uuid) {
        UserDetails owner = userDetailsService.getCurrentUser();
        Box box = boxService.get(owner, uuid);
        return boxMapper.toDto(box);
    }

    @PostMapping
    @PreAuthorize("""
            hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(#boxDto)
            or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(#boxDto)
            """)
    public BoxDto post(@RequestBody BoxDto boxDto) {
        Truth.assertThat(boxDto.getType()).isNotNull();
        UserDetails owner = userDetailsService.getCurrentUser();
        Truck assignedTruck = truckService.assignTruck();
        Box box = boxMapper.toEntity(boxDto);
        box = boxService.create(owner, assignedTruck, box);
        return boxMapper.toDto(box);
    }

    @PutMapping("/{uuid}")
    public BoxDto put(@PathVariable UUID uuid, @RequestBody BoxDto patch) {
        UserDetails owner = userDetailsService.getCurrentUser();
        Box patchBox = boxMapper.toEntity(patch);
        Box serverBox = boxService.edit(owner, uuid, patchBox);
        return boxMapper.toDto(serverBox);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        UserDetails owner = userDetailsService.getCurrentUser();
        boxService.delete(owner, uuid);
    }

    @GetMapping("/weapons")
    @RolesAllowed("ROLE_POLICE")
    public List<BoxDto> getAllWeapons() {
        UserDetails owner = userDetailsService.getCurrentUser();
        return boxService.getAllWeapons(owner).stream()
                .map(boxMapper::toDto)
                .toList();
    }
}
