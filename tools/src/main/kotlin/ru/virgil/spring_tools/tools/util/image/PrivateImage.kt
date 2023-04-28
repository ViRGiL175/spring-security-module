package ru.virgil.spring_tools.tools.util.image

import ru.virgil.spring_tools.tools.util.base.entity.Identified
import ru.virgil.spring_tools.tools.util.base.entity.Owned
import java.nio.file.Path

interface PrivateImage<Owner : Identified> : Owned<Owner>, Identified {

    var fileLocation: Path

}
