package ru.virgil.utils.image

import java.nio.file.Path

interface IPrivateImage<Owner : IBaseEntity> : IBaseEntity {

    var fileLocation: Path?
    var owner: Owner?
}
