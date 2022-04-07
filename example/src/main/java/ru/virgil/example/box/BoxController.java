package ru.virgil.example.box;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/box")
@RequiredArgsConstructor
public class BoxController {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "size";
    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @GetMapping
    public List<BoxDto> getAll(@RequestParam(PAGE_PARAM) int page, @RequestParam(PAGE_SIZE_PARAM) int size) {
        return boxService.getAll(page, size).stream()
                .map(boxMapper::toDto)
                .toList();
    }

    @GetMapping("/{uuid}")
    @PostAuthorize("hasRole('ROLE_POLICEMAN') or returnObject.type != T(ru.virgil.example.box.BoxType).WEAPONED")
    public BoxDto get(@PathVariable UUID uuid) {
        Box box = boxService.get(uuid);
        return boxMapper.toDto(box);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_POLICEMAN') or #boxDto.type != T(ru.virgil.example.box.BoxType).WEAPONED")
    public BoxDto post(@RequestBody BoxDto boxDto) {
        Truth.assertThat(boxDto.getType()).isNotNull();
        Box box = boxMapper.toEntity(boxDto);
        box = boxService.create(box);
        return boxMapper.toDto(box);
    }

    @PutMapping("/{uuid}")
    public BoxDto put(@PathVariable UUID uuid, @RequestBody BoxDto patch) {
        Box patchBox = boxMapper.toEntity(patch);
        Box serverBox = boxService.edit(uuid, patchBox);
        return boxMapper.toDto(serverBox);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        boxService.delete(uuid);
    }

    @GetMapping("/weaponed")
    @RolesAllowed("ROLE_POLICEMAN")
    public List<BoxDto> getAllWeaponed() {
        return boxService.getAllWeaponed().stream()
                .map(boxMapper::toDto).toList();
    }
}
